package bus.uigen.view;import bus.uigen.oadapters.*;import bus.uigen.widgets.VirtualContainer;import bus.uigen.widgets.VirtualFrame;import bus.uigen.uiFrame;import java.util.Vector;import bus.uigen.ars.*;import bus.uigen.controller.menus.AMenuDescriptor;import bus.uigen.controller.menus.MenuSetter;
public interface OEFrameFactory {
  public  uiFrame createFrame  (Vector theAdapters,Vector theTargetAdaptersList, Vector theMethods );  public uiFrame createFrame (Object obj );
  public uiFrame createFrame(Object obj, boolean showMenus, MenuSetter defMenus, AMenuDescriptor menuDescriptor);
  public uiFrame createFrame();
  public  uiFrame createFrame(VirtualFrame f, VirtualContainer c);  
 }
