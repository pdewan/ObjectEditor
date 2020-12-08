package bus.uigen.controller;import bus.uigen.uiFrame;
import bus.uigen.uiFrameList;
import bus.uigen.ars.*;import bus.uigen.introspect.AClassDescriptor;import bus.uigen.reflect.ClassProxy;import bus.uigen.reflect.MethodProxy;import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.lang.reflect.*;

public class ConstructorChooser {
  //Constructor constructor = null;  MethodProxy constructor = null;

  //public Constructor getConstructor() {  public MethodProxy getConstructor() {
    return constructor;
  }
  
  public ConstructorChooser(ClassProxy c) {
    //Constructor[] constructors = c.getConstructors();    //VirtualMethod[] constructors = ClassDescriptor.getConstructors(c);    MethodProxy[] constructors = c.getConstructors();
	if (constructors.length == 0) {
		System.err.println ("null constructor for " + c.getName());
		return;
	}	//uiFrame firstFrame = (uiFrame) uiFrameList.getList().elementAt(0);	//JFrame f;	    //firstFrame.setFontSize(JOptionPane.getRootFrame());		if (constructors.length == 1) constructor = constructors[0];    //else    constructor = (Constructor) JOptionPane.showInputDialog(null,    else    constructor = (MethodProxy) JOptionPane.showInputDialog(null,    		
							    "Constructors",
							    "Choose a constructor",
							    JOptionPane.INFORMATION_MESSAGE,
							    null,
							    constructors,
							    constructors[0]);
  }
}
