package bus.uigen.editors;import bus.uigen.*;
import java.awt.Color;import java.awt.Component;import java.awt.Container;import java.awt.Frame;import java.awt.Point;import java.awt.event.*;
import slc.SLComposer;import slgc.SLGController;import slgc.ShapeListAWTMouseListener;import slgv.SLGView;
import util.models.Listenable;import util.undo.Listener;import shapes.ShapesAPI;
import slm.SLModel;
import shapes.RemoteShape;import shapes.ShapeModel;
import shapes.LineModel;
import java.lang.Math;
import bus.uigen.introspect.Attribute;import bus.uigen.oadapters.CompositeAdapter;import bus.uigen.oadapters.ObjectAdapter;import bus.uigen.reflect.ClassProxy;import bus.uigen.widgets.PanelSelector;import bus.uigen.widgets.VirtualComponent;import bus.uigen.widgets.VirtualContainer;import bus.uigen.widgets.VirtualFrame;import bus.uigen.widgets.awt.AWTComponent;import bus.uigen.widgets.awt.AWTContainer;import bus.uigen.widgets.swing.DelegateJPanel;import bus.uigen.widgets.swt.SWTPanel;import bus.uigen.ars.*;import bus.uigen.attributes.AttributeNames;import bus.uigen.controller.SelectionManager;import bus.uigen.controller.menus.RightMenu;import bus.uigen.controller.menus.RightMenuManager;import java.util.List;import java.util.Vector;
public class ShapesAdapter extends WidgetAdapter implements Listener, MouseListener, ShapeListAWTMouseListener {

  private SLComposer composer = null;
  private int xc = 100, yc = 100, r = 50;  static Vector controllers = new Vector();  ToolTipTextRunnable toolTipTextRunnable;
  public static final String TOOL_TIP_THREAD = "Tool Tip Thread";

  public ShapesAdapter() {	//		 if (!getObjectAdapter().getUIFrame().isGlassPane())	  try {		  toolTipTextRunnable = new AToolTipTextRunnable();		  Thread toolTipTextThread = new Thread(toolTipTextRunnable);		  toolTipTextThread.setName(TOOL_TIP_THREAD);		  toolTipTextThread.start();		  	  } catch (Exception e) {		  	  }	 	  
  }

  public void update(Listenable shape, Object arg) {
    
  }    public static void toggleIncremental() {  	for (int i = 0; i < controllers.size(); i++) {  		((SLGController) controllers.elementAt(i)).toggleIncremental();  	}  }

  public String getType() {
    return "slm.SLModel";
  }  void removeController(SLGController controller) {  	if (controller == null) return;  	if (controllers.contains(controller))  		controllers.remove(controller);  }  void addController(SLGController controller) {  	if (controller == null) return;  	if (controllers.contains(controller)) return;  	controllers.addElement(controller);  }  public void setController() {   		  if (controller != composer.getController()) {	  	removeController(controller);		controller = composer.getController();				//controller.addSelectionListener();	  }	  if (controller == null) return;	  addController(controller);	  	  controller.setIncremental(incremental);  	  }    public void setIncremental() {  	if (controller != null)  		controller.setIncremental(incremental);   }  boolean isView(VirtualContainer container) {	  return container.getPhysicalComponent() instanceof DelegateJPanel;  }
  SLModel slModel;  public SLModel getSLModel() {	  return slModel;  }
  public void setUIComponentTypedValue(Object newval) {
	  //System.out.println("new model value" + newval);	  slModel = (SLModel) newval;	  if (composer.getModel() != slModel) {		  if (virtualContainer != null && isView(virtualContainer))			  composer.setModel(slModel, (DelegateJPanel) virtualContainer.getPhysicalComponent());		  else
			  composer.setModel(slModel);				 if (!getObjectAdapter().getUIFrame().isGlassPane())		  toolTipTextRunnable.setShapesList(slModel);	  }
	  if (view != composer.getView()) {	      view = composer.getView();	      view.getContainer().addMouseListener(this);	      getObjectAdapter().getUIFrame().addKeyListener(AWTContainer.virtualContainer(view.getContainer()));	      CompositeAdapter shapesObjectAdapter =  (CompositeAdapter) getObjectAdapter();	      getView().getShapeEventNotifier().addMouseListener(this);//		  shapesObjectAdapter.registerAsMouseClickListener(getView().getShapeEventNotifier());		  if (shapesObjectAdapter.getShapeListMouseClickListener() != null) {			  shapesObjectAdapter.registerAsMouseClickListener(getView().getShapeEventNotifier());		  }	  } else {	  	view.getContainer().repaint();	  }	  setController();	  /*	  if (controller != composer.getController()) {
		controller = composer.getController();				//controller.addSelectionListener();
	  }	  */	  //System.out.println("SLModel" + newval);
  }
  
  public Object getUIComponentValue() {	  return slModel;
  }
  SLGView view; 
  SLGController controller;  VirtualContainer virtualContainer;  public SLGController getController() {
	  return controller;  }  public SLComposer getComposer() {
	  return composer;  }  public SLGView getView() {
	  return view;  }    // need to move this stuff in link component and not derive top frame from frame list!
  public VirtualComponent instantiateComponent(ClassProxy cclass, ObjectAdapter adapter) {
    //uiFrame top = (uiFrame) uiFrameList.getList().elementAt(0);	  if (adapter.getUIFrame().isGlassPane()) return null;	  VirtualComponent virtualComposer = PanelSelector.createPanel();	  return virtualComposer;	  /*    uiFrame top = adapter.getUIFrame();
    try {	  //if (slModel == null) System.out.println("Null SLModel");
      //composer = new SLComposer(top, slModel);
	  //composer = new SLComposer(top.getFrame(), slModel, false);      VirtualFrame vFrame = top.getFrame();      Frame frame = null;      if (vFrame != null)    	  frame = (Frame) vFrame.getPhysicalComponent();    	  	  //composer = new SLComposer((Frame) top.getFrame().getPhysicalComponent(), slModel, false);	  composer = new SLComposer(frame, slModel, false, (Container) virtualComposer.getPhysicalComponent());	  view = composer.getView();	  controller = composer.getController();	  //controller.setIncremental(true);
	  	  virtualComposer = AWTContainer.virtualContainer(composer.getContainer());	  
      return virtualComposer;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }    */
  }

  public void setUIComponentEditable() {	  SLGController controller = composer.getController();
  }
  public void setUIComponentUneditable() {
	  System.out.println("Uneditable");	  SLGController controller = composer.getController();	  	  controller.removeComponent("Undo");	  	  controller.removeComponent("Redo");			  	  controller.removeComponent("Load");	  	  controller.removeComponent("Save");	  	  	  controller.removeComponent("Resize");
  }    Color unselectedColor;
  public void setUIComponentSelected() {	  // do we really need it to be selected
	  // should basicall put in a selected list
	  unselectedColor = view.getContainer().getBackground();	  view.getContainer().setBackground((Color) AttributeNames.getDefaultOrSystemDefault(AttributeNames.SELECTION_COLOR));	  controller.setControlPanelBackground((Color) AttributeNames.getDefaultOrSystemDefault(AttributeNames.SELECTION_COLOR));
  }
  public void setUIComponentDeselected() {
	  // do this better//	  view.getContainer().setBackground(composer.getContainer().getBackground());	  view.getContainer().setBackground(unselectedColor);	  controller.setControlPanelBackground(composer.getContainer().getBackground());
  }
  
  public void linkUIComponentToMe(VirtualComponent theVirtualComponent) {
    // Draw the clock and set up listener interface	  //System.out.println("link called");	  virtualContainer = (VirtualContainer) theVirtualComponent;	  //if (composer != null) return;	  	  	 // virtualComposer = AWTContainer.virtualContainer(composer.getContainer());
        linkUIComponentToMe();
  }    public VirtualComponent getUIComponent() {	  return virtualContainer;  }    public void linkUIComponentToMe () {	  uiFrame top = getObjectAdapter().getUIFrame();	    try {		  //if (slModel == null) System.out.println("Null SLModel");	      //composer = new SLComposer(top, slModel);		  //composer = new SLComposer(top.getFrame(), slModel, false);	      VirtualFrame vFrame = top.getFrame();	      Frame frame = null;	      if (vFrame != null)	    	  frame = (Frame) vFrame.getPhysicalComponent();    	  		  //composer = new SLComposer((Frame) top.getFrame().getPhysicalComponent(), slModel, false);		  //passing in a null model	      if (virtualContainer == null || isView(virtualContainer))	    	  composer = new SLComposer();	      else	    	  composer = new SLComposer(frame, slModel, false, (Container) virtualContainer.getPhysicalComponent());	   // set sze etc		  super.setAttributes(virtualContainer);			      	      /*		  view = composer.getView();		  top.addKeyListener(AWTContainer.virtualContainer(view.getContainer()));		  controller = composer.getController();		  */		  //controller.setIncremental(true);		  /*	      SLModel model = composer.getModel();      	      model.addListener(this);		  view.addMouseListener(this);		  */		  /*		  //SLGController controller = composer.getController();	  		  controller.removeComponent("Clear");	  		  controller.removeComponent("Prompt");		  		  //controller.setVisibleComponent("Prompt", false);			  		  controller.removeComponent("Move");	  		  controller.removeComponent("Delete");  	  		  controller.removeComponent("Line"); 	  		  controller.removeComponent("Oval");  	  		  controller.removeComponent("Rectangle"); 		  */		  //virtualComposer = AWTContainer.virtualContainer(composer.getContainer());	      //return virtualComposer;	    } catch (Exception e) {	      e.printStackTrace();	      //return null;	    }	    }  
  Frame f= null;	//uiFrame uiF= null;
  	
	public Frame getFrame(Component c) {		//System.out.println("parameter" +c);
		if (f == null) {			//System.out.println("null f");
			while (c != null) {								//System.out.println("componnet" + c.getName());
				if (c instanceof Frame) {
					f = (Frame) c;					//uiF = (uiFrame) f;					//this.getObjectAdapter().getUI
					return f;
				}
				c = c.getParent();
			}
		}		//System.out.println("returning" + f);
		return f;
	}	ObjectAdapter getTopAdapter() {	return getObjectAdapter().getUIFrame().getOriginalAdapter();}	 public void maybeShowPopup(MouseEvent e) {//	 ObjectAdapter menuAdapter = (ObjectAdapter)  SelectionManager.getLastSelection();//	 maybeShowPopup(e, menuAdapter); }  public void mouseEntered(RemoteShape aComponent, String aToolTipText, Point aCursotPoint) {//	 if (aComponent == null)//			mouseEntered(null, getTopAdapter().getToolTipText(), aCursotPoint);	 if (!getObjectAdapter().getUIFrame().isGlassPane())	 toolTipTextRunnable.mouseEntered(aComponent, aToolTipText, aCursotPoint);	  }  public void mouseExited(RemoteShape aComponent, Point aCursorPoint) {	 if (!getObjectAdapter().getUIFrame().isGlassPane())	 toolTipTextRunnable.mouseExited(aComponent, aCursorPoint); }
  
	
  public void maybeShowPopup(MouseEvent e,  ObjectAdapter menuAdapter) {	  		if (e.getButton() == MouseEvent.BUTTON3) {// for some reason popup trigger does not work when it is on a label	  			
//			if (e.isPopupTrigger()) {//				ObjectAdapter menuAdapter = (ObjectAdapter)  SelectionManager.getLastSelection();//				if (selectedAdapter.isGraphicsCompositeObject())//					return;				if (menuAdapter == null) {					 menuAdapter = getObjectAdapter();//					 if (!menuAdapter.isNestedShapesContainer())//						 return;									}								RightMenu menu = RightMenuManager.getRightMenu(menuAdapter);				if (menu == null) 					return;				menu.checkPre(menuAdapter.getRealObject());				//				menu.configure(menuAdapter.getUIFrame(), menuAdapter.getRealObject());								menu.getPopup().show(AWTComponent.virtualComponent(e.getComponent()), e.getX(), e.getY());//				while (menuAdapter != null && menuAdapter != getObjectAdapter()) {//					if (menu != null && menu.getItemCount() > 0)//						break;//					menuAdapter = menuAdapter.getParentAdapter();					//					menu = RightMenuManager.getRightMenu(menuAdapter);//				}				//				if (menuAdapter == getObjectAdapter() && !menuAdapter.isNestedShapesContainer())//					return;//				
//				Object obj = menuAdapter.getRealObject();//				if (obj == null) return;//				if (menu != null) {
//				//					menu.configure(menuAdapter.getUIFrame(), obj);//					menu.getPopup().show(AWTComponent.virtualComponent(e.getComponent()),//							  e.getX(),//							  e.getY());//			
//				}			
			} 
		}
    public void mousePressed(MouseEvent e)
    {
		//System.out.println("Adapter heard mouse");
	  maybeShowPopup(e);
    }
    
    public void mouseReleased (MouseEvent e)
    {
		 maybeShowPopup(e);
    }
    public void mouseClicked(MouseEvent e)
    {		/*
		if (!e.isPopupTrigger()) {		uiComponentFocusGained(); 
		System.out.println("Adapter heard mouse");
		}
		*/
    }
	public void mouseEntered(MouseEvent e)
    {
		
    }
	public void mouseExited(MouseEvent e)
    {//		mouseExited(null, null);
		
    }	boolean incremental;	public boolean processAttribute(Attribute attrib) {	    if (attrib.getName().equals("actionMode") ||	    		attrib.getName().equals(AttributeNames.INCREMENTAL)) {	      if (attrib.getValue() instanceof Boolean) {		incremental = ((Boolean) attrib.getValue()).booleanValue();		setIncremental();	      }	      return true;	    }	    else	      return super.processAttribute(attrib);	  }	public  void remove(ObjectAdapter compAdapter) {			}	@Override	public void mouseClicked(List<RemoteShape> theShapes,			RemoteShape theSmallestShape, MouseEvent mouseEvent, Point aClickPoint) {			}	@Override	public void mousePressed(List<RemoteShape> theShapes,			RemoteShape theSmallestShape, MouseEvent mouseEvent, Point aClickPoint) {		maybeShowPopup(mouseEvent, getTopAdapter());			}	@Override	public void mouseReleased(List<RemoteShape> theShapes,			RemoteShape theSmallestShape, MouseEvent mouseEvent, Point aClickPoint) {		// TODO Auto-generated method stub			}	@Override	public void mouseEntered(List<RemoteShape> theShapes,			RemoteShape theSmallestShape, MouseEvent mouseEvent, Point aClickPoint) {//		System.out.println("Mouse Entered");		 if (theSmallestShape == null && getTopAdapter() != null)				mouseEntered(null, getTopAdapter().getToolTipText(), aClickPoint);//		if (theSmallestShape == null) {//			ObjectAdapter objectAdapter = getObjectAdapter().getUIFrame().getAdapter();////			try {//				mouseEntered(theSmallestShape, getObjectAdapter().getToolTipText(), aClickPoint);////			} catch (Exception e) {}////		}//		}			}	@Override	public void mouseExited(List<RemoteShape> theShapes,			RemoteShape theSmallestShape, MouseEvent mouseEvent, Point aClickPoint) {		 if (theSmallestShape == null)			 mouseExited(null, aClickPoint);			}

}


