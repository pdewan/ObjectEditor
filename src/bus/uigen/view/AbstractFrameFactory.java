package bus.uigen.view;import bus.uigen.oadapters.*;import bus.uigen.widgets.FrameSelector;import bus.uigen.widgets.VirtualContainer;import bus.uigen.widgets.VirtualFrame;import java.util.Vector;
import bus.uigen.uiFrame;import bus.uigen.ars.*;import bus.uigen.controller.menus.AMenuDescriptor;import bus.uigen.controller.menus.MenuSetter;
public  class  AbstractFrameFactory implements OEFrameFactory {
	public  uiFrame createFrame  (Vector theAdapters,Vector theTargetAdaptersList, Vector theMethods ) {
		uiFrame  uiF = createBasicFrame();
		uiF.init(theAdapters, theTargetAdaptersList, theMethods);
		return uiF;		
	}	public uiFrame createFrame (Object obj ) {		return createFrame(obj, true, new MenuSetter(), new AMenuDescriptor());		/*
		uiFrame  uiF = createBasicFrame();
		//uiF.init(obj);		uiF.init(obj, true, null);
		return uiF;
		*/	}
	public uiFrame createFrame(Object obj, boolean showMenus, MenuSetter defMenus, AMenuDescriptor menuDescriptor) {
		/*		uiFrame  uiF = createBasicFrame();
		uiF.init(obj, showMenus, defMenus);
		return uiF;
		*/
		return createFrame(obj, showMenus, defMenus, menuDescriptor, null, null);	}
	/*	public uiFrame createFrame(Object obj, boolean showMenus, MenuSetter defMenus, Frame f) {
		
		return createFrame(obj, showMenus, defMenus, f, f);	}*/
	public uiFrame createFrame(Object obj, boolean showMenus, MenuSetter defMenus, AMenuDescriptor menuDescriptor, VirtualFrame f, VirtualContainer c) {
				uiFrame  uiF = createBasicFrame(f, c);//		if (f != null)
		uiF.init(obj, showMenus, defMenus, menuDescriptor);
		return uiF;	}	public uiFrame createFrame() {
		return createFrame(null);	}	public  uiFrame createFrame(VirtualFrame f, VirtualContainer c) {
		return createFrame (null, true, new MenuSetter(), new AMenuDescriptor(), f, c);	}
	//public abstract uiFrame createBasicFrame();	public  uiFrame createBasicFrame() {		VirtualFrame frame = FrameSelector.createFrame();		return createBasicFrame (frame, frame.getContentPane());			}	public uiFrame createBasicFrame (VirtualFrame f, VirtualContainer c) {
		//if ((f == null) || (c == null)) 		if ((f == null) && (c == null)) 
			return createBasicFrame();
		uiFrame  uiF = new uiFrame();
		uiF.init(f, c);		return uiF;	}
											  
 }
