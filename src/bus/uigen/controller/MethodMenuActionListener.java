package bus.uigen.controller;import bus.uigen.uiFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;import java.lang.reflect.*;

public class MethodMenuActionListener implements ActionListener {
  Method method;
  Object object;
  
  public MethodMenuActionListener(Object obj, Method m) {
    object = obj;
    method = m;
  }

  public void actionPerformed(ActionEvent e) {
    
  }

}
