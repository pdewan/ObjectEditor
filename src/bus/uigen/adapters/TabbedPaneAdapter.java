package bus.uigen.adapters;

import bus.uigen.*;
import bus.uigen.ars.*;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.compose.ComponentPanel;
import bus.uigen.introspect.Attribute;
import bus.uigen.models.PropertyFocusListener;
import bus.uigen.oadapters.*;
import bus.uigen.reflect.ClassProxy;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import util.models.ADynamicSparseList;





import java.awt.Component;
import java.awt.Container;
import java.awt.event.*;
import java.util.*;

import bus.uigen.visitors.*;
import bus.uigen.widgets.PanelSelector;
import bus.uigen.widgets.TabbedPaneSelector;
import bus.uigen.widgets.VirtualComponent;
import bus.uigen.widgets.VirtualContainer;
import bus.uigen.widgets.VirtualTabbedPane;
import bus.uigen.widgets.swing.SwingPanelFactory;

public class TabbedPaneAdapter extends PanelAdapter implements
		FocusListener, ChangeListener {
	ADynamicSparseList unSortedPropertiesList = new  ADynamicSparseList();
	ADynamicSparseList sortedPropertiesList = new  ADynamicSparseList();
	Map<Integer, String> indexToProperty = new HashMap();
	Map<String, Integer> propertyToIndex = new HashMap();
	public TabbedPaneAdapter() {
	}
	VirtualTabbedPane tabbedPane;
	public VirtualComponent instantiateComponent(ClassProxy cclass, ObjectAdapter adapter) {
		//tabbedPane = new JTabbedPane();
		tabbedPane = TabbedPaneSelector.createTabbedPane();
//		tabbedPane.setTabPlacement(JTabbedPane.BOTTOM);
		  instantiatedComponent = true;
		  tabbedPane.setName(adapter.toText());
		return (tabbedPane);
		//return (new SwingPanelFactory()).createPanel();
		//return PanelSelector.createPanel();
	}
	public VirtualComponent getUIComponent() {
		return tabbedPane;
	}
	public void linkUIComponentToMe(VirtualComponent c) {
		  
	    super.linkUIComponentToMe(c);
	    tabbedPane = (VirtualTabbedPane) super.getUIComponent();
	    tabbedPane.addChangeListener(this);
		  
	  }
	
	public void addUnsortedProperties () {
		for (int i = 0; i < unSortedPropertiesList.size(); i++) {
			ObjectAdapter compAdapter = (ObjectAdapter) unSortedPropertiesList.get(i);
			if (compAdapter == null) continue;
			addToTabbedPane(compAdapter);
			/*
			//Object constraint = compAdapter.getAddConstraint();
			Component comp = getComponent(compAdapter);
			if (constraint == null)
					componentPanel.add(comp);
				else 
					componentPanel.add(comp, constraint);	
					*/			
		}
		
	}
	public void processDirection() {
		
	}
	public void addSortedProperties () {
		for (int i = 0; i < sortedPropertiesList.size(); i++) {
			ObjectAdapter compAdapter = (ObjectAdapter) sortedPropertiesList.get(i);			
			if (compAdapter == null) continue;
			addToTabbedPane(compAdapter);
			/*
			int pos = Math.min(compAdapter.getPosition(), componentPanel.getComponentCount());
			Object constraint = compAdapter.getAddConstraint();
			Component comp = getComponent (compAdapter);
			
			if (constraint == null)
					componentPanel.add(comp, pos);
				else 
					componentPanel.add(comp, constraint, pos);
					*/				
		}
		
	}
	public void add(VirtualContainer parent, VirtualComponent comp,
			ObjectAdapter compAdapter) {
		tabbedPane = ((VirtualTabbedPane) parent);
		(new SetDefaultAttributesAdapterVisitor(compAdapter)).traverse();
		//(new SetDefaultSynthesizedAttributesAdapterVisitor(compAdapter, true))
		(new SetDefaultSynthesizedAttributesAdapterVisitor(compAdapter, false))
				.traversePostOrder();
		if (parent instanceof VirtualTabbedPane) {
			tabbedPane = ((VirtualTabbedPane) parent);
			int pos = compAdapter.getPosition();
				if (pos < 0)
					unSortedPropertiesList.add(compAdapter);
				else
					sortedPropertiesList.set( compAdapter.getPosition(), compAdapter);
				
			/*	
			uiContainerAdapter parentAdapter = compAdapter.getParentAdapter();

			JPanel enclosingPanel = new JPanel(); // so no strecthing occurs
			if (!parentAdapter.getStretchRows()) {
				enclosingPanel.add(comp);
				comp = enclosingPanel;
			}
			((JTabbedPane) parent).addTab(compAdapter.getLabel(), comp);
			//((JTabbedPane) parent).addTab(compAdapter.getLabel(), enclosingPanel);
			tabbedPane = ((JTabbedPane) parent);
			compAdapter.getGenericWidget().setAllowBorder(false);
			*/
		}
		
		//(new SetDefaultAttributesAdapterVisitor (compAdapter)).traverse();
		//(new SetDefaultSynthesizedAttributesAdapterVisitor (compAdapter,
		// true)).traversePostOrder();
		//parent.setSize(comp.getPreferredSize());
		//parent.setSize(new Dimension(30, 40));
	}
	
	public void setSelectedProperty(String aProperty) {
		Integer index = propertyToIndex.get(aProperty.toLowerCase());
		if (index != null)
			tabbedPane.setSelectedIndex(index);
	}
	void addToTabbedPane (ObjectAdapter compAdapter ) {
		//JPanel enclosingPanel = new JPanel(); // so no strecthing occurs
		VirtualContainer enclosingPanel = PanelSelector.createPanel();
//		setColors(enclosingPanel, compAdapter);
		setColors(enclosingPanel);

		CompositeAdapter parentAdapter = compAdapter.getParentAdapter();
		//Component comp = compAdapter.getUIComponent();
		VirtualComponent comp;
		if (compAdapter.getGenericWidget() != null)
			comp = compAdapter.getGenericWidget().getContainer();
		else
			comp = compAdapter.getUIComponent();
		if (!parentAdapter.getStretchRows()) {
			enclosingPanel.setBackground(comp.getBackground());
			enclosingPanel.add(comp);
			comp = enclosingPanel;
		}
		int index = tabbedPane.getTabCount();
		String property = compAdapter.getPropertyName().toLowerCase();	
		String tooltip = compAdapter.getToolTipText();
		if (tooltip == null || tooltip.isEmpty())
			tabbedPane.addTab(compAdapter.getLabelWithoutSuffix(), comp);
		else
			tabbedPane.addTab(compAdapter.getLabelWithoutSuffix(), null, comp, tooltip);
		indexToProperty.put(index, property);
		propertyToIndex.put(property, index);
		
		
		//((JTabbedPane) parent).addTab(compAdapter.getLabel(), enclosingPanel);		
		//compAdapter.setShowBorder(false);
		compAdapter.setOverrideLabelVisible(true);
		//compAdapter.getGenericWidget().setAllowBorder(false);
		
		
	}
	public void childComponentsAdded(boolean foundProperties) {
		if (!foundProperties)
			return;
		addSortedProperties();
		addUnsortedProperties();
		//commands = ComponentPanel.createCommandsWithButtons(this.getObjectAdapter().getUIFrame(), lastAssignedValue);
		//processCommands(commands);
		//buildPanel();
	  	
	  }
	public boolean processDescendentAttribute(ObjectAdapter descendent, Attribute attrib) {
  		if (!attrib.getName().equals(AttributeNames.LABEL)) return true;
  		String label = (String) attrib.getValue();
		CompositeAdapter myAdapter = (CompositeAdapter) getObjectAdapter();
  		int index = myAdapter.getAdapterIndex(descendent);
  		if (index == -1) return true;
  		tabbedPane.setTitleAt(index, label);
  		return true;
  		
  		
  		
  		
  		
  }
	public void setColor() {
		
		  panel.setBackground(getObjectAdapter().getContainerBackground());
	  }
	public void remove(Container parent, Component comp,
			ObjectAdapter compAdapter) {
		if (parent instanceof JTabbedPane)
			((JTabbedPane) parent).removeTabAt(((JTabbedPane) parent)
					.indexOfTab(compAdapter.getLabel()));
	}

	public boolean processDirection(String direction) {
		return true;
	}
	@Override
	public void stateChanged(ChangeEvent e) {
		Object realObject = getObjectAdapter().getRealObject();
		int index = tabbedPane.getSelectedIndex();
		String property = indexToProperty.get(index);
		if (property == null) // we have not added tabs
			return;
		if (realObject instanceof PropertyFocusListener)
			((PropertyFocusListener) realObject).newFocus(property);
			
		
		
		
	}

}