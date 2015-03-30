package bus.uigen.editors;import bus.uigen.*;
import java.awt.Point;import java.awt.Rectangle;import java.awt.event.*;
import slc.SLComposer;import slgc.SLGController;import slgc.ShapeListAWTMouseListener;import slgv.SLGView;
import util.models.Listenable;import util.undo.Listener;import shapes.ShapesAPI;import slgc.SelectionListener;
import slm.SLModel;import slm.SLSetBoundsCommand;
import shapes.RemoteShape;import shapes.ShapeModel;
//import shapes.LineModel;import shapes.TextModel;
//import java.lang.Math;
import bus.uigen.introspect.Attribute;import bus.uigen.loggable.ACompositeLoggable;import bus.uigen.oadapters.ObjectAdapter;import bus.uigen.oadapters.ShapeObjectAdapter;import bus.uigen.reflect.ClassProxy;import bus.uigen.sadapters.ConcreteTextShape;import bus.uigen.trace.ShapeAddedToDrawingPanel;import bus.uigen.widgets.VirtualComponent;import java.awt.event.ActionListener;
import java.util.List;import bus.uigen.ars.*;import bus.uigen.attributes.AttributeNames;import bus.uigen.controller.SelectionManager;import bus.uigen.controller.menus.RightMenuManager;public class ShapeAdapter extends WidgetAdapter implements Listener, SelectionListener, ActionListener, ShapeListAWTMouseListener {

  public ShapeAdapter() {
  }  public static boolean equals (Rectangle r1, Rectangle r2) {
	  return r1.x == r2.x && r1.y == r2.y && r1.width == r2.width && r1.height == r2.height;  }  /*
  public void update(Listenable shape, Object arg) {	  //if (((ShapeModel) shape).getBounds().equals(oldBounds)) return;	  if (equals (((ShapeModel) shape).getBounds(), oldBounds)) return;
      //uiComponentValueChanged();
  }
  */  public void update(Listenable shape, Object arg) {	  if (arg instanceof SLSetBoundsCommand) {
		  SLSetBoundsCommand command = (SLSetBoundsCommand) arg;
		  if (command.getKey() == key)
			  uiComponentValueChanged();	  }
  }
  public void actionPerformed(ActionEvent e) {
	  uiComponentValueChanged();
	}  //public void selectionChanged(String newKey, ShapeModel sm) {	  public void selectionChanged(String newKey, RemoteShape sm) {
	  if (newKey == key)	     //uiSelectionManager.select(this.getObjectAdapter());
		  SelectionManager.replaceSelections(this.getObjectAdapter());  }
  public String getType() {
    //return "shapes.ShapeModel";//    return "shapes.RemoteShape";    return shapes.RemoteShape.class.getName();
  }
  SLModel drawing;  SLGController slgController;
  ObjectAdapter drawingAdapter;
  ShapesAdapter shapesAdapter;  //ShapeModel shapeModel;  RemoteShape shapeModel;
  TextModel textModel;  String key;
  String label;
  Rectangle oldBounds;  static int shapeNum = 0;  SLGController controller;  public ShapesAdapter getShapesAdapter() {	  return shapesAdapter;  }
//  public void oldsetUIComponentTypedValue(Object newval) {	 //  	try {
//	  //System.out.print("<shapeAdapter" + newval);//  		if (newval instanceof ACompositeLoggable) {//  			newval = ((ACompositeLoggable) newval).getRealObject();//  		}
//	  if (shapeModel == newval) {//		  if (addToParentUIContainer())//			  return;
//		  oldBounds = new Rectangle(shapeModel.getBounds());//		  return; //	  }
//	  //Either adding first value or replacing one shape model with another//	  if ((drawing != null) && (key != null) && (shapeModel != null)) {
//		  drawing.remove(key);	  
//	  }							
//	  uiFrame frame = this.getObjectAdapter().getUIFrame();	  //	  shapeModel = (RemoteShape) newval;
//	  oldBounds = new Rectangle(shapeModel.getBounds());//	  if (frame == null) return;//	  drawing = frame.getDrawing();//	  key = (shapeNum++) + "";
//	  //key = adapter.getBeautifiedPath();
//	  //System.out.println("adapter path" + adapter.getPath());
//	  //System.out.println("adapter label" + adapter.getBeautifiedPath());
//	  //frame.createDrawPanel();//	  frame.showDrawPanel();//	  drawing.put(key, shapeModel);
//	  if (shapeModel instanceof TextModel) {//		  textModel = (TextModel) shapeModel;//		  textModel.getTextField().addActionListener(this);//		  if (((ConcreteTextShape) getObjectAdapter().getConcreteObject()).isTextReadOnly())//		  	textModel.getTextField().setEditable(false);
//	  }//	  if (label != null)//		drawing.put(key, label);//	  drawing.addListener(this);//	  drawingAdapter = frame.getDrawingAdapter();//	  if (drawingAdapter != null) {	  
//		  //		  shapesAdapter = (ShapesAdapter) drawingAdapter.getWidgetAdapter();
//		  slgController = shapesAdapter.getController();
//		  slgController.setUndeletable(key);
//		  slgController.addSelectionListener(this);//		 //	  }//	  //System.out.print(">");//  	} catch (Exception e) {//  		e.printStackTrace();//  		//System.out.println(e);//  	}
//  }  RemoteShape removedShape = null;public void removeFromParentUIContainer() {	if (drawing !=null && key != null)	  removedShape = drawing.remove(key);  }boolean addToParentUIContainer() {	if (removedShape == null)		return false;	drawing.put(key, removedShape);	removedShape = null;	return true;	}  public void childComponentsAdded(boolean foundProperties) {	  // do nothing, override widget adapter  }  
  
  public Object getUIComponentValue() {	  return shapeModel;
  }  
  public VirtualComponent instantiateComponent(ClassProxy cclass, ObjectAdapter adapter) {	  //System.out.println("instantiating component");	  return null;
  }

  public void setUIComponentEditable() {	  //SLGController controller = composer.getController();
  }
  public void setUIComponentUneditable() {	  if (shapeModel == null)		  return;
	  //System.out.println("Uneditable");	  shapesAdapter.getController().setUneditable(key);
	  /*	  SLGController controller = composer.getController();	  	  controller.removeComponent("Undo");	  	  controller.removeComponent("Redo");			  	  controller.removeComponent("Load");	  	  controller.removeComponent("Save");	  	  	  controller.removeComponent("Resize");	  */
  }    
  public void setUIComponentSelected() {
	  // should basicall put in a selected list
	  	  //view.setBackground(Color.cyan);	  //controller.setControlPanelBackground(Color.cyan);
  }
  public void setUIComponentDeselected() {	  	if (slgController.getSelection() == key)		slgController.unselect(key, shapeModel);
	  // do this better	 // view.setBackground(composer.getBackground());	  //controller.setControlPanelBackground(composer.getBackground());
  }
  
  public void linkUIComponentToMe(VirtualComponent shape) {	  
    
    
  }  public void linkUIComponentToMe() {	    	  }  public VirtualComponent getUIComponent() {	  return null;  }
  public boolean processAttribute(Attribute attrib) {
	  boolean retVal = super.processAttribute(attrib);
	  //System.out.println("attrib" + attrib.getName());		 // System.out.println("atrib name" + attrib.getAttributeName());
	  	  //if (attrib.getAttributeName().equals(AttributeNames.LABEL)) {
	  //System.out.print("<" + attrib.getAttributeName());	  if (attrib.getName().equals(AttributeNames.LABEL)) {
	      ObjectAdapter adapter = this.getObjectAdapter();		  
	      //uiFrame frame = adapter.getUIFrame();	      //if (frame == null) return retVal;	      //drawing = frame.getDrawing();		  String oldLabel = (String) attrib.getValue();
		  if (label == null && oldLabel == null) return retVal;
		  if (label != null && oldLabel != null && label.equals(oldLabel)) return retVal;		  //if (label.equals(oldLabel)) return retVal;
		  //label = (String) attrib.getValue();		  label = oldLabel;
		  if (label == null) drawing.removeLabel(key);
		  else if (key != null)	        drawing.put(key, label);		  		  //drawing.put(key, adapter.getBeautifiedPath());		  	 
	     //System.out.println(attrib.getName()); 
	  }	  //System.out.print(">");
	  	  return retVal;
  }  public void removeUIComponentValueChangedListener(ComponentValueChangedListener l) {
    super.removeUIComponentValueChangedListener(l);	slgController.removeSelectionListener(this);
	 if ((drawing != null) && (key != null) && (shapeModel != null)) {
		  drawing.remove(key);		  shapeModel = null;
	  }		
  }  public  void remove(ObjectAdapter compAdapter) {			}  public static ObjectAdapter nearestNestedShapesContainer(ObjectAdapter candidate) {	  if (candidate == null) return null;	  if (candidate.isNestedShapesContainer())		  return candidate;	  return nearestNestedShapesContainer(candidate.getParentAdapter());	    }  public static SLModel getSLModel(ObjectAdapter nestedShapesContainer) {	  return ((NestedShapesAdapter) nestedShapesContainer.getWidgetAdapter()).getSLModel();	    }  //  public SLModel getMainDrawing() {//	  uiFrame frame = this.getObjectAdapter().getUIFrame();	//	  if (frame == null) return null;//	  return frame.getDrawing();	  //  }    public void setSLModelAndShapesAdapter() {	  if (shapesAdapter != null && drawing != null )		  return;	  ObjectAdapter nestedShapedContainerAdapter = nearestNestedShapesContainer(getObjectAdapter());	  if (nestedShapedContainerAdapter == null) {		  uiFrame frame = this.getObjectAdapter().getUIFrame();			  if (frame == null) return ;		  drawing = frame.getDrawing();		  // showDrawPanel initialized drawing adapter		  frame.showDrawPanel();		  drawingAdapter = frame.getDrawingAdapter();	  } else {		  drawingAdapter = nestedShapedContainerAdapter;		  drawing = getSLModel(nestedShapedContainerAdapter); 	  }	  shapesAdapter =  (ShapesAdapter) drawingAdapter.getWidgetAdapter();	  ((ShapeObjectAdapter) getObjectAdapter()).registerAsMouseClickListener(shapesAdapter.getView().getShapeEventNotifier());  }   // need to get drawing from parent widget adapter, maybe rewrite parent class  public void setUIComponentTypedValue(Object newval) {	   	try {  		  //System.out.print("<shapeAdapter" + newval);  		if (newval instanceof ACompositeLoggable) {  			newval = ((ACompositeLoggable) newval).getRealObject();  		}	  if (shapeModel == newval) {		  if (shapeModel == null) return;		  if (addToParentUIContainer())			  return;		  oldBounds = new Rectangle(shapeModel.getBounds());		  return; 	  }	  //Either adding first value or replacing one shape model with another	  if ((drawing != null) && (key != null) && (shapeModel != null)) {		  drawing.remove(key);	  	  }								  shapeModel = (RemoteShape) newval;	  if (shapeModel == null) 		  return;	  oldBounds = new Rectangle(shapeModel.getBounds());//	  uiFrame frame = this.getObjectAdapter().getUIFrame();	//	  if (frame == null) return;//	  drawing = frame.getDrawing();	  setSLModelAndShapesAdapter();	  if (drawing == null) return;	  key = (shapeNum++) + "";	   getObjectAdapter().refreshEditable();	  //key = adapter.getBeautifiedPath();	  //System.out.println("adapter path" + adapter.getPath());	  //System.out.println("adapter label" + adapter.getBeautifiedPath());	  //frame.createDrawPanel();	  //frame.showDrawPanel();	  drawing.put(key, shapeModel);	  if (shapeModel instanceof TextModel) {		  textModel = (TextModel) shapeModel;		  textModel.getTextField().addActionListener(this);		  if (((ConcreteTextShape) getObjectAdapter().getConcreteObject()).isTextReadOnly())		  	textModel.getTextField().setEditable(false);	  }	  if (label != null)		drawing.put(key, label);	  drawing.addListener(this);	  	  List<ObjectAdapter> objectAdaptersList = ((ShapeObjectAdapter) getObjectAdapter()).getGraphicsAdaptersInPathToRoot();	  ObjectAdapter[] adaptersArray = new ObjectAdapter[objectAdaptersList.size()];		  objectAdaptersList.toArray(adaptersArray);	 	  //drawingAdapter = frame.getDrawingAdapter();	  if (drawingAdapter != null) {	  		  		  //shapesAdapter = (ShapesAdapter) drawingAdapter.getWidgetAdapter();		  slgController = shapesAdapter.getController();		  slgController.setUndeletable(key);		  slgController.addSelectionListener(this);		  RightMenuManager.bindToRightMenu(null, adaptersArray);		  shapesAdapter.getView().getShapeEventNotifier().addMouseListener(this);		 	  }	  ShapeAddedToDrawingPanel.newCase(shapeModel, this);//	  System.out.println("Shape added: " + shapeModel + " by " + this);	  //System.out.print(">");  	} catch (Exception e) {  		e.printStackTrace();  		//System.out.println(e);  	}  }@Overridepublic void mouseClicked(List<RemoteShape> theShapes, RemoteShape theSmallestShape, MouseEvent mouseEvent, Point aClickPoint) {		}@Overridepublic void mousePressed(List<RemoteShape> theShapes, RemoteShape theSmallestShape, MouseEvent mouseEvent, Point aClickPoint) {	if (getObjectAdapter().getViewObject() == theSmallestShape)		shapesAdapter.maybeShowPopup(mouseEvent, getObjectAdapter());//	//	if (! theShapes.contains(getObjectAdapter().getViewObject()))//		return;	//	ShapesAdapter shapesAdapter = getShapesAdapter();//	if (theShapes.get(0) == getObjectAdapter().getViewObject())//		shapesAdapter.maybeShowPopup(mouseEvent, getObjectAdapter());	}@Overridepublic void mouseEntered(List<RemoteShape> theShapes,		RemoteShape theSmallestShape, MouseEvent mouseEvent, Point aClickPoint) {	if (getObjectAdapter().getViewObject() == theSmallestShape) {		try {			shapesAdapter.mouseEntered(theSmallestShape, getObjectAdapter().getToolTipText(), aClickPoint);		} catch (Exception e) {}	}	}@Overridepublic void mouseExited(List<RemoteShape> theShapes,		RemoteShape theSmallestShape, MouseEvent mouseEvent, Point aClickPoint) {	if (getObjectAdapter().getViewObject() == theSmallestShape)		shapesAdapter.mouseExited(theSmallestShape, aClickPoint);	}@Overridepublic void mouseReleased(List<RemoteShape> theShapes,		RemoteShape theSmallestShape, MouseEvent mouseEvent, Point aClickPoint) {	// TODO Auto-generated method stub	}
    
}


