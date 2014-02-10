
package bus.uigen.adapters;
import bus.uigen.ars.*;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.view.SelectionColorSelector;import bus.uigen.view.SelectionColorSelector;
import bus.uigen.widgets.PasswordFieldSelector;
import bus.uigen.widgets.TextFieldSelector;
import bus.uigen.widgets.VirtualComponent;
import bus.uigen.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;


// uiLabeledTextAdapter
// An adaptor between the JPasswordField widget
// and any Primitive type

public class PasswordFieldAdapter extends /*uiWidgetAdapter*/ TextFieldAdapter 
/*implements DocumentListener, FocusListener*/ {
	public VirtualComponent instantiateComponent(Class cclass, ObjectAdapter adapter) {
		  //System.out.println("instantiating jtextfield");
		  //jtf = new JTextField("", NUM_COLUMNS);'	  
		  //jtf = new JTextField("");
		  jtf = PasswordFieldSelector.createPasswordField("");
		  instantiatedComponent = true;

		  //((JTextField) jtf).setColumns(100);
	  	//int numColumns = ((Integer) this.getObjectAdapter().getMergedAttributeValue(AttributeNames.NUM_COLUMNS)).intValue();
	  	//jtf = new JTextField("", numColumns);
	  	return jtf;
	      //return new JTextField("", NUM_COLUMNS);
	  }
  public PasswordFieldAdapter() {
  }
  /*
  // Implementation of 
  // abstract methods
  
  public String getType() {
    return "java.lang.String";
  }
  
  public void setUIComponentTypedValue(Object newval) {
    VirtualComponent c = getUIComponent();
    if (c instanceof JPasswordField) {
      ((JPasswordField) c).setText((String) newval);
    }
  }
 
  public Object getUIComponentValue() {
    VirtualComponent c = getUIComponent();
    if (c instanceof JPasswordField)
      return ((JPasswordField) c).getText();
    else
      return "";
  }
  
  public void setUIComponentEditable() {
    VirtualComponent c = getUIComponent();
    if (c instanceof JPasswordField)
      ((JPasswordField) c).setEditable(true);
  }
  
  public void setUIComponentUneditable() {
    VirtualComponent c = getUIComponent();
    if (c instanceof JPasswordField)
      ((JPasswordField) c).setEditable(false);
  }

  Color oldColor = Color.white;
  public void setUIComponentSelected() {	  super.setUIComponentSelected();
     if (getUIComponent().getBackground() != SelectionColorSelector.getColor())
       oldColor = getUIComponent().getBackground();
   getUIComponent().setBackground(SelectionColorSelector.getColor());
  }
  
  public void setUIComponentDeselected() {	  super.setUIComponentDeselected();
   getUIComponent().setBackground(oldColor);
  }

  public void linkUIComponentToMe(VirtualComponent c) {
    //if (c instanceof JPasswordField) {
	  
      JPasswordField p = (JPasswordField) c;
      p.getDocument().addDocumentListener(this);
      p.addFocusListener(this);
    //}
  }

  // Method invoked when the text value of the  
  // AWT component changes
  public void changedUpdate(DocumentEvent evt) {
    uiComponentValueChanged();
  }
  public void insertUpdate(DocumentEvent evt) {
    uiComponentValueChanged();
  }
  public void removeUpdate(DocumentEvent evt) {
    uiComponentValueChanged();
  }
  

  // Methods invoked on focus gain/loss in the view
  public void focusGained(FocusEvent e) {
    if (e.isTemporary())
      return;
    else {
      uiComponentFocusGained();
    }
    
  }

  public void focusLost(FocusEvent e) {
    if (e.isTemporary())
      return;
    else {
      uiComponentFocusLost();
    }
  }
  */
}



