package bus.uigen.adapters;
import java.awt.event.KeyEvent;

import javax.swing.event.DocumentEvent;

import util.models.AMutableString;
import util.models.VectorInterface;
import bus.uigen.widgets.events.VirtualFocusEvent;


public abstract class MSTextComponentAdapter extends TextComponentAdapter {
  //boolean mutatingString = false; VectorInterface<Character> mutableString;  public void setUIComponentValue(Object newValue) {
	  setUIComponentTypedValue(newValue);  }  public String componentToText() {
	  if (jtf == null) return "";
	  return jtf.getText();  }
  /*
   public void uiComponentValueChanged() {
	  
	  
		super.uiComponentValueChanged();
		refreshText();  
	
	
  }
  */
   boolean textChanged() {
		  return true;
		  
	  }
 public void setUIComponentTypedValue(Object newVal) {
    try {
    	if (prompt != null && firstFocus && 
    			(newVal == null || newVal.toString().equals(""))) {
//    		 mutableString = (AMutableString) newVal;
    		 mutableString = (VectorInterface) newVal;
    		 text = mutableString.toString();
    		return;    	}
    	//System.out.println("MSJTextComponentAdapter: Set value " + newVal);
      //jtf = (JTextComponent) getUIComponent();	  
	  //System.out.println(jtf.getColumns() + " " + tf.getColumns());	  //if (tf != jtf) System.out.println("setUI: not the same tf");
	  //int length = ((String) newval).length();//	  mutableString = (AMutableString) newVal;
	  mutableString = (VectorInterface) newVal;

	  text = mutableString.toString();	  //System.out.println("New text:" + text);
	  String curText = jtf.getText();	  //System.out.println("Current text:" + jtf.getText());
	  if (text.equals(curText)) return;	  //System.out.println("setUIComponentValue: Setting text:" + text );	  mutatingString = true;
      jtf.setText(text);	  mutatingString = false;
	  //System.out.println("Successfully Set text");	  //tf.setColumns(NUM_COLUMNS);
	  //tf.validate();	  //tf.setColumns(NUM_COLUMNS);	  //System.out.println((String) newval);
    } catch (ClassCastException e) {
    	e.printStackTrace();
    }
  } public Object getUIComponentValue() {
    //return mutableString;	 return new AMutableString(jtf.getText());
  }
  public void insertUpdate(DocumentEvent e) {
	  if (mutatingString) return;	  int pos = e.getOffset();
	  String oldText = jtf.getText();	  if (oldText.length() == mutableString.size()) return;
	  char newChar = oldText.charAt(pos);	  //char newChar = jtf.getText().charAt(pos);
	  //System.out.println("inserted character: " + newChar + " at pos: " + pos);	  	  mutableString.insertElementAt(newChar, pos);	;
	  // this was commented out
	  if (adapter.getImplicitRefreshOnNotification())	  this.getObjectAdapter().getUIFrame().doImplicitRefresh();
  }

  public void removeUpdate(DocumentEvent e) {
	  if (mutatingString) return;	  
	  int pos = e.getOffset();
	  //System.out.println("asked to delete at pos" + pos);	  //String oldText = jtf.getText();
	  //String modelText = mutableString.toString();	  //System.out.println("MSJTextComp: before deletion:" + modelText + "view text:" + oldText);
	  //if (oldText.equals(modelText)) return;	  //if (oldText.length() == mutableString.size()) return;
	  //System.out.println("about to delete at pos" + pos);	  mutableString.removeElementAt(pos);
	  //this was commented out
	  if (adapter.getImplicitRefreshOnNotification())
	  this.getObjectAdapter().getUIFrame().doImplicitRefresh();	 
  }
   public void keyTyped (KeyEvent k) {
	  /*	  if (actionPerformed)		  actionPerformed = false;	  else
	      super.uiComponentValueEdited(true);	
	  */  }
  //boolean firstFocus = true;
  public void focusGained(VirtualFocusEvent e) {
	   //System.out.println("focus gained");
   super.focusGained(e);
   //if (firstFocus) {
   if (firstFocus && prompt != null) {
	   mutatingString = true;
	   jtf.setText("");
	   mutatingString = false;
	   firstFocus = false;
   }
 }
  public boolean needChildrenObjectAdapters() {
		return false;
	}
}



