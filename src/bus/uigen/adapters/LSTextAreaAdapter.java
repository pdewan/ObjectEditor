package bus.uigen.adapters;
import bus.uigen.*;
import bus.uigen.ars.*;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import java.awt.event.*;


public class LSTextAreaAdapter	extends MSTextAreaAdapter {	
//implements DocumentListener, ActionListener, FocusListener {
  
  public String getType() {
    return "bus.uigen.AListenableString";
  }
 
   
}



