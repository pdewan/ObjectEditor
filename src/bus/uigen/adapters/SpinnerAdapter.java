package bus.uigen.adapters;

import bus.uigen.*; //import bus.uigen.widgets.*;
import bus.uigen.view.SelectionColorSelector;
import bus.uigen.widgets.SpinnerSelector;
import bus.uigen.widgets.VirtualSpinner;
import bus.uigen.widgets.VirtualComponent;
import bus.uigen.widgets.VirtualTextComponent;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;

import java.awt.Component;
import java.awt.event.*;
import java.util.Vector;
import java.lang.reflect.Field;
import javax.swing.SpinnerModel;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListDataEvent;
import javax.swing.table.TableCellEditor;
import java.lang.reflect.*;

import bus.uigen.introspect.Attribute;
import bus.uigen.introspect.IntrospectUtility;
import bus.uigen.misc.OEMisc;
import bus.uigen.oadapters.EnumerationAdapter;
import java.util.EventObject; //import bus.uigen.introspect.uiBean;
import bus.uigen.ars.*;
import bus.uigen.controller.MethodInvocationManager;
import javax.swing.event.ListDataEvent;

import util.trace.Tracer;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.ClassProxy;

//public class uiJTextFieldAdapter extends uiWidgetAdapter 
public class SpinnerAdapter extends AbstractEnumerationWidgetAdapter {
	// implements ItemListener, FocusListener, KeyListener, /*SpinnerModel,*/
	// MutableSpinnerModel, TableCellEditor { //uiJTextComponentAdapter {

	VirtualSpinner spinner = null;
	boolean filledCB = false;
	boolean finishedFilling = false;
	boolean noItemState = true;
	boolean isEditable = true;
	boolean linked = false;
	String text;
	// ObjectAdapter enumAdapter;
	uiFrame frame;

	/*
	 * public String getType() { return "java.lang.String"; }
	 */
	/*
	 * public boolean isComponentAtomic() { return true; }
	 */
	public String componentToText() {
		if (spinner == null)
			return "";
		return (String) (spinner.getValue().toString());
	}

	
	public VirtualComponent instantiateComponent(ClassProxy cclass,
			ObjectAdapter adapter) {
		// System.out.println("instantiating  jcombo");		// cb = new JSpinner();
		if (spinner != null)
			return spinner;
		spinner = SpinnerSelector.createSpinner();
		instantiatedComponent = true;
		spinner.setName("JSpinnerAdapter:" + adapter.toDebugText());

		/*
		 * Misc.setWidth(cb, 100); cb.setSize(100, cb.getSize().height);
		 * cb.setPreferredSize(new Dimension (100, cb.getSize().height));
		 * //cb.setSelectedIndex(0);
		 */

		// cb.setEditable(true);
		// cb.setEnabled(true);
		// cb.setLightWeightPopupEnabled(false);
		// cb.setLightWeightPopupEnabled(true);
		// size of combox box
		/*
		 * cb.setSize(new Dimension(100, cb.getPreferredSize().height ));
		 * cb.setPreferredSize(new Dimension(100, cb.getPreferredSize().height
		 * ));
		 */

		// cb.setSize(new Dimension(100, 10 ));
		// cb.setPreferredSize(new Dimension(100, 10 ));
		// cb.setMaximumRowCount(5);		return spinner;
	}

	String[] defaultStrings;
	Vector defaultFinals;

	// Object obj;
	// boolean haveSetModel = false;
	// int oldSize;	public void setUIComponentTypedValueNew(Object newval) {
		// System.out.println("in setuictv");
		String val;
		// obj = newval;
		currentModel = newval;
		VirtualComponent c = getUIComponent();

		if (c instanceof VirtualSpinner) {

			// if ( this.getObjectAdapter() instanceof EnumerationAdapter) {			// //enumerationAdapter = (uiEnumerationAdapter)
			// this.getObjectAdapter();
			// //Object initialItem = this.getSelectedItem();
			if (!haveSetModel) {
				spinner.setModel(this);
				haveSetModel = true;
				oldSize = this.getSize();
			}
			// if (getSize() != oldSize) {
			// addElement(null);
			// }
			Object newSelectedItem = getSelectedItem();
			// if (!getSelectedItem().equals(lastSelectedItem)) {
			if (newSelectedItem != lastSelectedItem
					&& !newSelectedItem.equals(lastSelectedItem)) {
				/*
				 * if (!cb.getSelectedItem().equals(lastSelectedItem)) // combo
				 * box if confused cb.setSelectedItem(lastSelectedItem);
				 */
				lastSelectedItem = newSelectedItem;
				notifyChangeListeners();
				// cb.updateUI();
				// setIndexOfSelectedItem();				// cb.setSelectedItem(newSelectedItem);
				// cb.validate();
			}
			// cb.setSelectedItem(initialItem);

			return;
		}

	}

	public void setUIComponentTypedValue(Object newval) {
		// System.out.println("in setuictv");
		String val;
		// obj = newval;
		currentModel = newval;
		VirtualComponent c = getUIComponent();

		if (c instanceof VirtualSpinner) {

			if (this.getObjectAdapter() instanceof EnumerationAdapter || isSubRange()) {
				// enumerationAdapter = (uiEnumerationAdapter)
				// this.getObjectAdapter();
				// Object initialItem = this.getSelectedItem();
				if (!haveSetModel) {
					spinner.setModel(this);
					haveSetModel = true;
					oldSize = this.getSize();
				}
				if (getSize() != oldSize) {
					addElement(null);
				}
				Object newSelectedItem = getSelectedItem();
				// if (!getSelectedItem().equals(lastSelectedItem)) {
				if (newSelectedItem != lastSelectedItem
						&& !newSelectedItem.equals(lastSelectedItem)) {
					/*
					 * if (!cb.getSelectedItem().equals(lastSelectedItem)) //
					 * combo box if confused
					 * cb.setSelectedItem(lastSelectedItem);
					 */
					lastSelectedItem = newSelectedItem;
					notifyChangeListeners();
					// cb.updateUI();
					setIndexOfSelectedItem();
					// cb.setSelectedItem(newSelectedItem);
					// cb.validate();
				}
				// cb.setSelectedItem(initialItem);

				return;
			}

			// set ui componenet seems toput in two olu's in so this filledCB my
			// take care of the doubles
			if (!filledCB) {

				try {
					defaultStrings = IntrospectUtility.getPropertyAlternatives(
							getObjectAdapter().getParentAdapter()
									.getRealObject(), getObjectAdapter()
									.getPropertyName());

					// assume getting parent will return the class that creates
					// this property. and hence where
					// we can call the method <propertyName>Alternatives

					if (defaultStrings != null) { // if alternatives

						// System.out.println("alternatives found " +
						// defaultStrings.length );
						for (int j = 0; j < defaultStrings.length; j++) {
							String toInsert = new String(
									(String) defaultStrings[j]);
							// cb.addItem(toInsert);
							// System.out.println(toInsert);
						}

					}

				}// end try

				catch (Exception fcb) {
					System.out.println("can't fill alts");
				}

				try {

					defaultFinals = IntrospectUtility.getPropertyTypeFinals(
							getObjectAdapter().getParentAdapter()
									.getRealObject(), getObjectAdapter()
									.getPropertyName(), getObjectAdapter()
									.getPropertyClass());
					// got the finals of the property

					if (defaultFinals.size() > 0) {
						// System.out.println("finals found " +
						// defaultFinals.size() );
						for (int j = 0; j < defaultFinals.size(); j++) {
							Field thefield = (Field) defaultFinals.elementAt(j);
							Object theobject = thefield.get(getObjectAdapter()
									.getParentAdapter().getRealObject());
							// System.out.println( "the object " +
							// theobject.toString());

							String toInsert = new String(String
									.valueOf(theobject));

							// cb.addItem(toInsert);
							// System.out.println(toInsert);
						}
					}// end if

				}// ed try

				catch (Exception fcb) {
					System.out.println("can't fill finals");
				}

				// System.out.println("also " + " " + cb.getItemCount());
				// cb.setEditable(true);
				// cb.enable(true);
				// cb.setEnabled(true);
				spinner.invalidate();
				spinner.validate();

				filledCB = true;

			}// if not filled

			try {

				if (newval != null) {
					val = new String((String) newval);
					// System.out.println("adding component value to  " + val);

					if (val != null) {
						// System.out.println(cb..paramString());
						// cb.removeItem(val);

						// cb.addItem(val); //should add and select (make
						// visible the item)

					}

					else
						spinner.setValue(new String("")); // should add and
															// select (make
															// visible the item)

				} else {
					val = "";
					spinner.setValue(new String("")); // should add and select
														// (make visible the
														// item)
				}
				// System.out.println("and added" + " " + cb.getItemCount());
				text = val;
				// System.out.println("setted");

			}

			catch (ClassCastException e) {
				System.out.println("exception in setUicomp");
				e.printStackTrace();
				Tracer.error("Spinner model cannot be: "
						+ newval.getClass().getSimpleName());
				return;
			}

			if (!finishedFilling) {
				// System.out.println("====");
				finishedFilling = true;
				uiComponentValueChanged();

			}

		}// if instance

	}

	/*
	 * public Object getUIComponentValue() { Component c = getUIComponent();
	 * String val = new String(""); if (c instanceof JSpinner) {
	 * 
	 * try {
	 * 
	 * val = new String((String)cb.getSelectedItem());
	 * 
	 * } catch (ClassCastException e) {
	 * System.out.println("exeception in getUIcom"); } } return val; }
	 * 
	 * boolean isEditable = true; public void setUIComponentEditable() {
	 * isEditable = true; cb.setEditable(true); }
	 * 
	 * public void setUIComponentUneditable() { isEditable = false;
	 * cb.setEditable(false); }
	 * 
	 * Color oldColor = Color.white;
	 */
	/*
	 * public void setUIComponentSelected() { super.setUIComponentSelected(); if
	 * (getUIComponent().getBackground() != SelectionColorSelector.getColor())
	 * oldColor = getUIComponent().getBackground(); else oldColor =
	 * getOriginalBackground(getUIComponent());
	 * getUIComponent().setBackground(SelectionColorSelector.getColor()); //
	 * Repaint the component to get the color right getUIComponent().repaint();
	 * }
	 * 
	 * public void setUIComponentDeselected() {
	 * super.setUIComponentDeselected();
	 * getUIComponent().setBackground(oldColor); getUIComponent().repaint(); }
	 */

	public void linkUIComponentToMe() {
		// if (comb instanceof JSpinner) {
		// enumAdapter = this.getObjectAdapter();
		frame = getObjectAdapter().getUIFrame();
		frame.addKeyListener(spinner);
		// cb.addItemListener(this);
		spinner.addFocusListener(this);
		/*
		 * int width = getObjectAdapter().getComponentWidth(); if (width != 0)
		 * Misc.setWidth(cb, width);
		 */
		linked = true;
		setIsEnumeration();
		setIsSubRange();

		// cb.addKeyListener(this);

		// }	}

	public void linkUIComponentToMe(VirtualComponent theComponent) {
		if (spinner == theComponent && linked)
			return;
		if (spinner == theComponent) {

			super.setAttributes(theComponent);

		}

		spinner = (VirtualSpinner) theComponent;
		linkUIComponentToMe();
		/*
		 * //if (comb instanceof JSpinner) { cb.addItemListener(this);
		 * cb.addFocusListener(this); //cb.addKeyListener(this);
		 */

		// }
	}

	public VirtualComponent getUIComponent() {
		return spinner;
	}

	// at this point	/*
	 * uiObjectAdapter adapter; uiGenericWidget genericWidget;
	 */
	/*
	 * public void uiComponentValueChanged() { // if (textChanged()) { //wanna
	 * make sure we don't waste invocations on object if something is done but
	 * //doesn't ultimately change the value of the widget
	 * super.uiComponentValueChanged(); //} }
	 */

	/*
	 * public void uiComponentValueEdited() { //if (textChanged())
	 * super.uiComponentValueEdited(); }
	 */

	boolean textChanged() {
		if (haveSetModel)
			return false;
		if (text == null)
			return true;
		return !text.equals((String) (spinner.getValue()));

	}

	/*
	 * 
	 * public void actionPerformed(ActionEvent e) {
	 * 
	 * // System.out.println("adapter actionperformed called");
	 * 
	 * if (e.getSource() == cb) {
	 * 
	 * // System.out.println("comb fires an event");
	 * 
	 * } super.actionPerformed(e);
	 * 
	 * }
	 * 
	 * public boolean processAttribute(Attribute attrib) { if
	 * (attrib.getName().equals("actionMode")) { if (attrib.getValue()
	 * instanceof Boolean) { actionMode = ((Boolean)
	 * attrib.getValue()).booleanValue(); } return true; } else return
	 * super.processAttribute(attrib); }
	 */
	/*
	 * public void keyReleased(KeyEvent k) { System.out.println("000000"); }
	 * public void keyPressed(KeyEvent k) { if (haveSetModel) return;
	 * System.out.println("33434324"); if (k.getKeyCode() == KeyEvent.VK_ENTER){
	 * super.uiComponentValueEdited(true);
	 * 
	 * 
	 * }
	 * 
	 * }
	 * 
	 * 
	 * public void keyTyped (KeyEvent k) { System.out.println("ppppp");
	 * 
	 * 
	 * }
	 */

	public void itemStateChanged(ItemEvent ev) {
		if (haveSetModel)
			return;
		noItemState = false;
		uiComponentValueChanged();
	}

	/*
	 * // Methods invoked on focus gain/loss in the view public void
	 * focusGained(FocusEvent e) { if (e.isTemporary()) return; else {
	 * uiComponentFocusGained(); } }
	 * 
	 * public void focusLost(FocusEvent e) { if (e.isTemporary()) return; else {
	 * uiComponentFocusLost(); } } public void
	 * addCellEditorListener(CellEditorListener l) {};
	 * 
	 * public void cancelCellEditing() {};
	 * 
	 * public Object getCellEditorValue() { return obj; } public boolean
	 * isCellEditable(EventObject anEvent) { return isEditable;}; public void
	 * removeCellEditorListener(CellEditorListener l){
	 * 
	 * }; public boolean shouldSelectCell(EventObject anEvent){ return true; };
	 * public boolean stopCellEditing() { return true; }
	 */
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		if (spinner == null)
			return null;
		return (Component) spinner.getPhysicalComponent();
	}

	public void setSelectedItem(Object newVal) {
		ChangeEvent event = new ChangeEvent(this);
		// cb.setValue(newVal);

		// cb.updateUI();
		int oldSize = getSize();
		super.setSelectedItem(newVal);
		// cb.commitEdit();

		notifyChangeListeners(event);
		// cb.updateUI();
		/*
		 * Object curValue = getSelectedItem();
		 * 
		 * int newSize = getSize(); if (oldSize != newSize ||
		 * !curValue.equals(newVal)) { cb.updateUI(); }
		 */
	}

	/*
	 * public void removeElementAt(int arg0) { // TODO Auto-generated method
	 * stub
	 * 
	 * }
	 * 
	 * public void addElement(Object arg0) { // TODO Auto-generated method stub
	 * int newSize = getSize(); for (int i = 0; i < listDataListeners.size();
	 * i++) { ((ListDataListener)
	 * listDataListeners.elementAt(i)).contentsChanged( new ListDataEvent(this,
	 * ListDataEvent.CONTENTS_CHANGED, 0, newSize-1 ));
	 * 
	 * } } / public void removeElement(Object arg0) { // TODO Auto-generated
	 * method stub
	 * 
	 * }
	 * 
	 * public void insertElementAt(Object arg0, int arg1) { // TODO
	 * Auto-generated method stub
	 * 
	 * }
	 */
	/*
	 * public void setUIComponentEditable() { isEditable = true;
	 * 
	 * ((VirtualSpinner) getUIComponent()).setEditable(true); }
	 */

	public void setUIComponentUneditable() {
		isEditable = false;
		((VirtualSpinner) getUIComponent()).setEnabled(false);
	}

	void resetUIComponentEditable() {
		if (!isEditable)
			setUIComponentUneditable();
		// ((VirtualSpinner) getUIComponent()).setEnabled(false);

	}

	public boolean processAttribute(Attribute attrib) {
		return super.processAttribute(attrib);
		/*
		 * if (attrib.getName().equals(AttributeNames.TEXT_FIELD_LENGTH)) {
		 * cb.setMaximumRowCount(((Integer)attrib.getValue()).intValue());
		 * return true; } else return super.processAttribute(attrib);
		 */
	}

//	public Object getNextValue() {
//		if (index == getSize() - 1)
//			return getElementAt(0);
//		return (getElementAt(index + 1));
//	}
//
//	public Object getPreviousValue() {
//		if (index == 0)
//			return getElementAt(getSize() - 1);
//		return (getElementAt(index - 1));
//	}

}
