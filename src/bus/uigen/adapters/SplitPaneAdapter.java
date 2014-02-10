package bus.uigen.adapters;

import bus.uigen.*;
import bus.uigen.ars.*;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.compose.ComponentPanel;
import bus.uigen.introspect.Attribute;
import bus.uigen.oadapters.*;

import javax.swing.*;

import util.models.ADynamicSparseList;


import java.awt.Component;
import java.awt.Container;
import java.awt.event.*;
import java.util.*;

import bus.uigen.visitors.*;
import bus.uigen.widgets.DesktopPaneSelector;
import bus.uigen.widgets.InternalFrameSelector;
import bus.uigen.widgets.PanelSelector;
import bus.uigen.widgets.SplitPaneSelector;
import bus.uigen.widgets.VirtualComponent;
import bus.uigen.widgets.VirtualContainer;
import bus.uigen.widgets.VirtualDesktopPane;
import bus.uigen.widgets.VirtualFrame;
import bus.uigen.widgets.VirtualInternalFrame;
import bus.uigen.widgets.VirtualSplitPane;
import bus.uigen.widgets.swing.SwingPanelFactory;

public class SplitPaneAdapter extends PanelAdapter implements
		FocusListener {
	ADynamicSparseList unSortedPropertiesList = new  ADynamicSparseList();
	ADynamicSparseList sortedPropertiesList = new  ADynamicSparseList();
	VirtualSplitPane mainSplitPane, currentSplitPane;
	String direction = null;
	boolean firstChild = true;
	public SplitPaneAdapter() {
	}
	VirtualSplitPane createSplitPane() {
	VirtualSplitPane mainSplitPane, currentSplitPane;
	
		VirtualSplitPane retVal = SplitPaneSelector.createSplitPane();
		retVal.setResizeWeight(0.5);
		retVal.setContinuousLayout(true);
		if (AttributeNames.HORIZONTAL.equals(direction))
				retVal.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		else
			retVal.setOrientation(JSplitPane.VERTICAL_SPLIT);
		
		
		return retVal;
	}
		
	
	public VirtualComponent instantiateComponent(Class cclass, ObjectAdapter adapter) {
		//desktopPane = new JDesktopPane();		
		firstChild = true;
		
		direction = adapter.getDirection();	
		mainSplitPane = createSplitPane();
		currentSplitPane = mainSplitPane;
		VirtualFrame frame = adapter.getUIFrame().getFrame();
		frame.getContentPane().add(mainSplitPane);
		//frame.setContentPane(mainSplitPane);
		//desktopPane.setTabPlacement(JDesktopPane.BOTTOM);
		return mainSplitPane;
		//return (new SwingPanelFactory()).createPanel();
		//return PanelSelector.createPanel();
	}
	public VirtualComponent getUIComponent() {
		return mainSplitPane;
	}
	public void linkUIComponentToMe(VirtualComponent c) {
		  
	    super.linkUIComponentToMe(c);
	    mainSplitPane = (VirtualSplitPane) super.getUIComponent();
		  
	  }
	
	public void addUnsortedProperties () {
		for (int i = 0; i < unSortedPropertiesList.size(); i++) {
			ObjectAdapter compAdapter = (ObjectAdapter) unSortedPropertiesList.get(i);
			if (compAdapter == null) continue;
			if (i < unSortedPropertiesList.size() - 1)
				addToSplitPane(compAdapter, false);
			else
				addToSplitPane(compAdapter, true);
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
			if (i < sortedPropertiesList.size() - 1)
				addToSplitPane(compAdapter, false);
			else
				addToSplitPane(compAdapter, true);
			//addToSplitPane(compAdapter);
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
		//desktopPane = ((VirtualDesktopPane) parent);
		(new SetDefaultAttributesAdapterVisitor(compAdapter)).traverse();
		(new SetDefaultSynthesizedAttributesAdapterVisitor(compAdapter, true))
				.traversePostOrder();
		//if (parent instanceof VirtualDesktopPane) {
			//desktopPane = ((VirtualDesktopPane) parent);
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
			((JDesktopPane) parent).addTab(compAdapter.getLabel(), comp);
			//((JDesktopPane) parent).addTab(compAdapter.getLabel(), enclosingPanel);
			desktopPane = ((JDesktopPane) parent);
			compAdapter.getGenericWidget().setAllowBorder(false);
			*/
		//}
		
		//(new SetDefaultAttributesAdapterVisitor (compAdapter)).traverse();
		//(new SetDefaultSynthesizedAttributesAdapterVisitor (compAdapter,
		// true)).traversePostOrder();
		//parent.setSize(comp.getPreferredSize());
		//parent.setSize(new Dimension(30, 40));
	}
	
	void addToSplitPane (ObjectAdapter compAdapter, boolean isLast ) {
		//JPanel enclosingPanel = new JPanel(); // so no strecthing occurs
		//VirtualInternalFrame internalFrame = InternalFrameSelector.createInternalFrame();
		CompositeAdapter parentAdapter = compAdapter.getParentAdapter();
		//Component comp = compAdapter.getUIComponent();
		VirtualContainer comp = compAdapter.getGenericWidget().getContainer();
		
		if (firstChild) {
			currentSplitPane.setLeftComponent(comp);
			firstChild = false;
		} else if (isLast) {
			currentSplitPane.setRightComponent(comp);
		} else {
			VirtualSplitPane newSplitPane = createSplitPane();
			currentSplitPane.setRightComponent(newSplitPane);
			currentSplitPane = newSplitPane;			
		}
		
		//((JDesktopPane) parent).addTab(compAdapter.getLabel(), enclosingPanel);		
		//compAdapter.getGenericWidget().setAllowBorder(false);
		
		
	}
	public void childComponentsAdded() {
		addSortedProperties();
		addUnsortedProperties();
		//commands = ComponentPanel.createCommandsWithButtons(this.getObjectAdapter().getUIFrame(), lastAssignedValue);
		//processCommands(commands);
		//buildPanel();
	  	
	  }
	public boolean processDescendentAttribute(ObjectAdapter descendent, Attribute attrib) {
		/*
  		if (!attrib.getName().equals(AttributeNames.LABEL)) return true;
  		String label = (String) attrib.getValue();
		uiContainerAdapter myAdapter = (uiContainerAdapter) getObjectAdapter();
  		int index = myAdapter.getAdapterIndex(descendent);
  		if (index == -1) return true;
  		//desktopPane.setTitle(index, label);
  		 * 
  		 */
  		return true;
  		
  		
  		
  		
  		
  		
  }

	public void remove(Container parent, Component comp,
			ObjectAdapter compAdapter) {
		/*
		if (parent instanceof JDesktopPane)
			((JDesktopPane) parent).remove(comp);
			*/
	}

	public boolean processDirection(String direction) {
		return true;
	}

}