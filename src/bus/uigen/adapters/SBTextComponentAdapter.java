package bus.uigen.adapters;
import bus.uigen.*;
import bus.uigen.ars.*;
import bus.uigen.widgets.VirtualTextComponent;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;

import util.models.AMutableString;

import java.awt.event.*;


public abstract class SBTextComponentAdapter extends TextComponentAdapter {
   StringBuffer sb;
 public void setUIComponentTypedValue(Object newVal) {
    try {
      jtf = (VirtualTextComponent) getUIComponent();	  
	  //System.out.println(jtf.getColumns() + " " + tf.getColumns());	  //if (tf != jtf) System.out.println("setUI: not the same tf");
	  //int length = ((String) newval).length();	  sb = (StringBuffer) newVal;
	  text = sb.toString();
      jtf.setText(text);	  //tf.setColumns(NUM_COLUMNS);
	  //tf.validate();	  //tf.setColumns(NUM_COLUMNS);	  //System.out.println((String) newval);
    } catch (ClassCastException e) {
    }
  } public Object getUIComponentValue() {
    return sb;
  }
  public void insertUpdate(DocumentEvent e) {	  int pos = e.getOffset();	  char newChar = jtf.getText().charAt(pos);
	  System.out.println("inserted" + newChar + "at pos" + pos);	  sb.insert(pos,newChar);
  }

  public void removeUpdate(DocumentEvent e) { 	   int pos = e.getOffset();	   System.out.println("removing at pos" + pos);
	  AMutableString.removeElementAt(sb, pos, 1);	 
  }
   public void keyTyped (KeyEvent k) {	  if (actionPerformed)		  actionPerformed = false;	  else
	      super.uiComponentValueEdited(true);	  }
}



