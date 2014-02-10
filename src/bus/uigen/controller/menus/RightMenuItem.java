package bus.uigen.controller.menus;import bus.uigen.uiFrame;import bus.uigen.controller.MethodInvocationManager;import bus.uigen.introspect.IntrospectUtility;import bus.uigen.introspect.MethodDescriptorProxy;import bus.uigen.loggable.ACompositeLoggable;import bus.uigen.oadapters.ObjectAdapter;import bus.uigen.reflect.MethodProxy;import bus.uigen.undo.CommandListener;import bus.uigen.widgets.MenuItemSelector;import bus.uigen.widgets.VirtualMenuItem;import bus.uigen.widgets.events.VirtualActionEvent;import bus.uigen.widgets.events.VirtualActionListener;public class RightMenuItem /*extends MenuItem*/ implements VirtualActionListener {
  private MethodProxy method;  MethodProxy preMethod = null;  MethodDescriptorProxy methodDescriptor;
  private Object object;  CommandListener commandListener;  //MenuItem menuItem;  VirtualMenuItem menuItem;  String[] dynamicCommandParams = new String[1];  uiFrame frame;//  boolean manuallyRetargetedMenu;
  
  public RightMenuItem(String label, MethodDescriptorProxy md, MethodProxy m, CommandListener theCommandListener) {
    //super(label);	  //menuItem = new MenuItem(label);	  menuItem = MenuItemSelector.createMenuItem();	 menuItem.setLabel(label);
    method = m;    methodDescriptor = md;	commandListener = theCommandListener;	
    menuItem.addActionListener(this);
  }  public RightMenuItem(VirtualMenuItem theMenuItem,  MethodDescriptorProxy md, MethodProxy m, CommandListener theCommandListener) {	    //super(label);		  //menuItem = new MenuItem(label);		  //menuItem = MenuItemSelector.createMenuItem();		 //menuItem.setLabel(label);	  menuItem = theMenuItem;	    method = m;	    methodDescriptor = md;		commandListener = theCommandListener;	    menuItem.addActionListener(this);	  }
  //public MenuItem getMenuItem () {  public VirtualMenuItem getMenuItem () {	  return menuItem;  }  public void setMenuItem(VirtualMenuItem newVal) {	  menuItem = newVal;  }
  public MethodProxy getMethod() {
    return method;
  }
  // this is not really needed now as we take the latest object bound to the adapter
  public void setObject(Object o) {
    object = o;    preMethod = IntrospectUtility.getPre(method, ACompositeLoggable.getTargetClass(object));    dynamicCommandParams[0] = method.getName();//    manuallyRetargetedMenu = true;
  }  public void setFrame(uiFrame theFrame) {	    frame = theFrame;	  }
  
  public void checkPre() {  	/*
	  try {
		  Object[] params = {};		  		  Method preMethod = uiBean.getPre(method, object.getClass());
		  boolean result = ((Boolean) preMethod.invoke(object, params)).booleanValue();
		  this.setEnabled(result);	  } catch (Exception e) {
		  this.setEnabled(true);	  }	  */  	checkPre(object);
  }  public void checkPre(Object object) {	  try {		  Object[] params = {};		  		  //MethodProxy preMethod = uiBean.getPre(method, ClassDescriptor.getTargetClass(object));		  if (preMethod == null)		  	menuItem.setEnabled(true);		  else if (method.isDynamicCommand()) {			  boolean result = ((Boolean) preMethod.invoke(object, dynamicCommandParams)).booleanValue();			  menuItem.setEnabled(result);		  } else {		  boolean result = ((Boolean) preMethod.invoke(object, params)).booleanValue();		  menuItem.setEnabled(result);		  }	  } catch (Exception e) {		  menuItem.setEnabled(true);	  }}      
  public void actionPerformed(VirtualActionEvent e) {
	  //System.out.println("right menu item");	  //RightMenu rightMenu = (RightMenu) (menuItem.getParent());		 // System.out.println("right menu");	  //uiFrame frame = (uiFrame) rightMenu.getParent();	  	  //uiFrame theFrame = ((uiObjectAdapter) commandListener).getUIFrame();	  uiFrame theFrame = frame;	  if (theFrame == null)		  		  theFrame = ((ObjectAdapter) commandListener).getUIFrame();//	  if (!manuallyRetargetedMenu) {		  object = ((ObjectAdapter) commandListener).getRealObject();//	  }
	  if (object != null) {		  if (theFrame == null) {
			  new MethodInvocationManager(object,
											method); 			  commandListener.commandActionPerformed();		  }		  else new MethodInvocationManager(theFrame, object, methodDescriptor, method, commandListener);		  // this wasw causing multiple refreshes, another alternative is to make frame null		  //if (method.getReturnType() == method.getDeclaringClass().voidType())		  //commandListener.commandActionPerformed();	  }
  }
}
