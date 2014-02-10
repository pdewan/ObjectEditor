package bus.uigen.controller.menus;import bus.uigen.uiFrame;

import javax.swing.*;
import java.awt.event.*;
import java.beans.MethodDescriptor;import java.lang.reflect.*;import java.util.Vector;
import bus.uigen.introspect.IntrospectUtility;import bus.uigen.loggable.ACompositeLoggable;import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.RemoteSelector;import bus.uigen.reflect.MethodProxy;import bus.uigen.widgets.VirtualButton;
public class VirtualMethodAction extends AbstractAction {
  uiFrame frame;
  MethodProxy method;  MethodProxy preMethod;  MethodDescriptor md;
  Vector methods = new Vector();  Icon icon;  String label;  Object targetObject;
  public VirtualMethodAction(uiFrame f, Object theTargetObject,
		       String theLabel, 
		       Icon theIcon, 
		       MethodProxy m) {
    super(theLabel, theIcon);    //putValue (Action.SHORT_DESCRIPTION, "foo");
    method = m;    preMethod = IntrospectUtility.getPre(method, ACompositeLoggable.getTargetClass(theTargetObject));    targetObject = theTargetObject;	methods.addElement(m);
    frame = f;
	label = theLabel;
	icon = theIcon;	
  }  
  public void checkPre() {	  try {		  Object[] params = {};		  		  //VirtualMethod preMethod = uiBean.getPre(method, object.getClass());		  if (preMethod == null || targetObject == null)		  	setEnabled(true);		  else {		  boolean result = ((Boolean) preMethod.invoke(targetObject, params)).booleanValue();		  setEnabled(result);		  }	  } catch (Exception e) {		  setEnabled(true);	  }	    }
  public void actionPerformed(ActionEvent e) {
    Object object = frame.getAdapter().getRealObject();    
    if (method != null) {
		//frame.processMethod(method);
		frame.processMethod(targetObject, method);		/*
      uiMethodInvocationManager iman = new uiMethodInvocationManager(frame, object,
								     method);		*/
    }
  }
  public boolean isDuplicate(VirtualMethodAction other) {	  return (icon != null && icon.equals(other.icon )) || (label != null && label.equals( other.label));
  }    public VirtualMethodAction getDuplicate(Vector existing) {
	  for (int i=0; i < existing.size(); i++) {		  VirtualMethodAction other = (VirtualMethodAction) existing.elementAt(i);
		  if (isDuplicate(other))			  return other;
	  }	  return null;  			    }  public void addMethod(MethodProxy m) {	  if (!methods.contains(m))
		methods.addElement(m);  }  //JButton button;  VirtualButton button;  //public JButton getButton() {  public VirtualButton getButton() {
	  return button;  }  /*  public void setButton(JButton theButton) {  //public void setButton(VirtualButton theButton) {
	  button = theButton;  }  */  public void setButton(VirtualButton theButton) {	  button = theButton;  }
}
