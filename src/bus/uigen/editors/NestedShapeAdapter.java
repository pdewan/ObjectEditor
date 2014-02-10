package bus.uigen.editors;import bus.uigen.*;
import java.awt.Rectangle;import java.awt.event.*;
import slc.SLComposer;import slgc.SLGController;import slgv.SLGView;
import util.models.Listenable;import util.undo.Listener;import shapes.ShapesAPI;import slgc.SelectionListener;
import slm.SLModel;import slm.SLSetBoundsCommand;
import shapes.RemoteShape;import shapes.ShapeModel;
//import shapes.LineModel;import shapes.TextModel;
//import java.lang.Math;
import bus.uigen.introspect.Attribute;import bus.uigen.loggable.ACompositeLoggable;import bus.uigen.oadapters.ObjectAdapter;import bus.uigen.reflect.ClassProxy;import bus.uigen.sadapters.ConcreteTextShape;import bus.uigen.widgets.VirtualComponent;import java.awt.event.ActionListener;
import bus.uigen.ars.*;import bus.uigen.attributes.AttributeNames;import bus.uigen.controller.SelectionManager;public class NestedShapeAdapter extends ShapeAdapter {

  public NestedShapeAdapter() {
  }  public static SLModel getSLModel(ObjectAdapter nestedShapesContainer) {	  return ((NestedShapesAdapter) nestedShapesContainer.getWidgetAdapter()).getSLModel();	    }  //  public SLModel getMainDrawing() {//	  uiFrame frame = this.getObjectAdapter().getUIFrame();	//	  if (frame == null) return null;//	  return frame.getDrawing();	  //  }    public void setSLModelAndShapesAdapter() {	  ObjectAdapter nestedShapedContainerAdapter = ObjectAdapter.nearestNestedShapesContainer(getObjectAdapter());	  if (nestedShapedContainerAdapter == null) {		  uiFrame frame = this.getObjectAdapter().getUIFrame();			  if (frame == null) return ;		  drawing = frame.getDrawing();		  // showDrawPanel initialized drawing adapter		  frame.showDrawPanel();		  drawingAdapter = frame.getDrawingAdapter();	  } else {		  drawingAdapter = nestedShapedContainerAdapter;		  drawing = getSLModel(nestedShapedContainerAdapter); 	  }	  shapesAdapter =  (ShapesAdapter) drawingAdapter.getWidgetAdapter();  }   // need to get drawing from parent widget adapter, maybe rewrite parent class
  public void setUIComponentTypedValue(Object newval) {	   	try {
	  //System.out.print("<shapeAdapter" + newval);  		if (newval instanceof ACompositeLoggable) {  			newval = ((ACompositeLoggable) newval).getRealObject();  		}
	  if (shapeModel == newval) {		  if (addToParentUIContainer())			  return;
		  oldBounds = new Rectangle(shapeModel.getBounds());		  return; 	  }
	  //Either adding first value or replacing one shape model with another	  if ((drawing != null) && (key != null) && (shapeModel != null)) {
		  drawing.remove(key);	  
	  }							
	  shapeModel = (RemoteShape) newval;
	  oldBounds = new Rectangle(shapeModel.getBounds());//	  uiFrame frame = this.getObjectAdapter().getUIFrame();	//	  if (frame == null) return;//	  drawing = frame.getDrawing();	  setSLModelAndShapesAdapter();	  if (drawing == null) return;	  key = (shapeNum++) + "";
	  //key = adapter.getBeautifiedPath();
	  //System.out.println("adapter path" + adapter.getPath());
	  //System.out.println("adapter label" + adapter.getBeautifiedPath());
	  //frame.createDrawPanel();	  //frame.showDrawPanel();	  drawing.put(key, shapeModel);
	  if (shapeModel instanceof TextModel) {		  textModel = (TextModel) shapeModel;		  textModel.getTextField().addActionListener(this);		  if (((ConcreteTextShape) getObjectAdapter().getConcreteObject()).isTextReadOnly())		  	textModel.getTextField().setEditable(false);
	  }	  if (label != null)		drawing.put(key, label);	  drawing.addListener(this);	  //drawingAdapter = frame.getDrawingAdapter();	  if (drawingAdapter != null) {	  
		  		  //shapesAdapter = (ShapesAdapter) drawingAdapter.getWidgetAdapter();
		  slgController = shapesAdapter.getController();
		  slgController.setUndeletable(key);
		  slgController.addSelectionListener(this);		 	  }	  //System.out.print(">");  	} catch (Exception e) {  		e.printStackTrace();  		//System.out.println(e);  	}
  }  
    
}


