package bus.uigen.controller.menus;import bus.uigen.uiFrame;
import bus.uigen.ars.*;import bus.uigen.widgets.MenuItemSelector;import bus.uigen.widgets.VirtualMenuItem;import java.lang.reflect.*;


public class MethodMenuItem{
  //Method method;
  //Constructor constructor;  Object targetObject;  VirtualMenuItem menuItem = MenuItemSelector.createMenuItem();
  //uiFrame frame = null;  /*
  public Method getMethod() {
    return method;
  }

  public Constructor getConstructor() {
    return constructor;
  }  */  public VirtualMenuItem getMenuItem() {	  return menuItem;  }    public void setMenuItem(VirtualMenuItem theItem) {	  menuItem = theItem;  }  public MethodMenuItem(Object theTargetObject,  VirtualMenuItem theMenuItem) {	  menuItem = theMenuItem;	  //menuItem.setLabel(methodName);	  menuItem.setUserObject(this);	  //super(methodName);	    //method = m;	    //constructor = null;	    targetObject = theTargetObject;	    	  }  public MethodMenuItem(Object theTargetObject, String methodName) {	  menuItem.setLabel(methodName);	  menuItem.setUserObject(this);	  //super(methodName);	    //method = m;	    //constructor = null;	    targetObject = theTargetObject;	  }  public MethodMenuItem(String methodName) {	    //super(methodName);		  menuItem.setLabel(methodName);		  menuItem.setUserObject(this);	    //method = m;	    //constructor = null;	    //frame = theFrame;	  } 
/*
  public uiMethodMenuItem(String methodName, Method m) {
    //super(methodName);	  menuItem.setLabel(methodName);	  menuItem.setUserObject(this);
    method = m;
    constructor = null;    //frame = theFrame;
  }    public uiMethodMenuItem(Object theTargetObject, String methodName, Method m) {	  menuItem.setLabel(methodName);	  menuItem.setUserObject(this);	  //super(methodName);	    method = m;	    constructor = null;	    targetObject = theTargetObject;	  }  public uiMethodMenuItem(Object theTargetObject,  Method m, VirtualMenuItem theMenuItem) {	  menuItem = theMenuItem;	  //menuItem.setLabel(methodName);	  menuItem.setUserObject(this);	  //super(methodName);	    method = m;	    constructor = null;	    targetObject = theTargetObject;	    	  }

  public uiMethodMenuItem(String constructorName, Constructor c) {
    //super(constructorName);	  menuItem.setLabel(constructorName);	  menuItem.setUserObject(this);
    constructor = c;
    method = null;
  }  */    public void setToolTipText(String theText) {	  if (theText != null && theText != "")	  menuItem.setToolTipText(theText);  }    public Object getTargetObject () {	  return targetObject;  }
}

