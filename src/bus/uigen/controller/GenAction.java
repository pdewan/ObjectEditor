package bus.uigen.controller;import bus.uigen.uiFrame;import bus.uigen.uiGenerator;

import javax.swing.*;
import java.awt.event.*;
import java.lang.reflect.*;


public class GenAction extends AbstractAction {
  Object object;
  
  public GenAction(String label, 
		      Icon icon, 
		      Object obj) {
    super(label, icon);
    object = obj;
  }
  
  public void actionPerformed(ActionEvent e) {
    if (object != null) {
      uiFrame frame = uiGenerator.generateUIFrame(object);
      frame.setVisible(true);
    }
  }
}
