package bus.uigen.view;import bus.uigen.oadapters.*;import bus.uigen.widgets.VirtualContainer;import bus.uigen.widgets.VirtualFrame;import bus.uigen.uiFrame;import java.util.Vector;import bus.uigen.ars.*;import bus.uigen.controller.menus.AMenuDescriptor;import bus.uigen.controller.menus.MenuSetter;
public class OEFrameSelector  {	//static uiFrameFactory frameFactory = new SwingUIFrameFactory();	static OEFrameFactory frameFactory = new AbstractFrameFactory();
	  public static void setFrameFactory (OEFrameFactory newVal) {
		frameFactory = newVal;  }
  public  static uiFrame createFrame  (Vector theAdapters,Vector theTargetAdaptersList, Vector theMethods ) {	  return frameFactory.createFrame(theAdapters, theTargetAdaptersList, theMethods);
  }  public static uiFrame createFrame (Object obj ) {
	  return frameFactory.createFrame(obj);  }
  public static uiFrame createFrame(Object obj, boolean showMenus, MenuSetter defMenus, AMenuDescriptor menuDescriptor) {
	  return frameFactory.createFrame(obj, showMenus, defMenus, menuDescriptor);  }  public static uiFrame createFrame(Object obj, boolean showMenus) {  return createFrame (obj, showMenus, new MenuSetter(), new AMenuDescriptor());  }
  public static uiFrame createFrame() {	  return frameFactory.createFrame();	  //return frameFactory.createFrame();
  }
  public static uiFrame createFrame(VirtualFrame f, VirtualContainer c) {	  return frameFactory.createFrame(f, c);	  //return frameFactory.createFrame();
  }  
 }
