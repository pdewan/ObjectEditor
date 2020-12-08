package bus.uigen.oadapters;
import java.rmi.RemoteException;
import java.util.*;import java.lang.reflect.*;

import shapes.RemoteLabel;
import shapes.RemoteShape;
import shapes.ShapeModel;import shapes.LabelModel;import util.models.RemotePropertyChangeListener;
import bus.uigen.*;import bus.uigen.introspect.*;import bus.uigen.sadapters.ConcreteLabelShape;
public class LabelShapeAdapter extends TextShapeAdapter implements RemotePropertyChangeListener  { 
	public LabelShapeAdapter () throws RemoteException {
		
	}	public ConcreteLabelShape getConcreteLabelShape() {		return (ConcreteLabelShape) getConcreteObject();
	}
	  String oldImageFileName;  public void setViewObject(Object viewObject) {
	  try {
	  super.setViewObject(viewObject);
	  if (getTextMode()) return;	  Object obj = computeAndMaybeSetViewObject();
	  RemoteLabel shape = (RemoteLabel) viewObject;
	  oldImageFileName = shape.getImageFileName();	  if (obj instanceof shapes.LabelModel && oldImageFileName != null) {		  LabelModel oldShape = (LabelModel) obj;
		  //if (!oldShape.getImageFileName().equals(oldImageFileName))
		  if (!oldImageFileName.equals(oldShape.getImageFileName()))		  oldShape.setImageFileName(oldImageFileName);	  }
	  } catch (Exception e) {
		  e.printStackTrace();
	  }
	    }
  static String[] imageShapePropertiesArray = {"imagefilename"};
	 
  
  static void fillImageShapeProperties() {
	  for (String propertyName:imageShapePropertiesArray) {
		  shapePropertiesSet.add(propertyName);
	  }
  }
  
  static {
	  fillImageShapeProperties();
  }

  
  /*  public Object getViewObject(Object realObject) {
	  ShapeModel shape = (ShapeModel) getViewObject();
	  if (shape == null)		  return super.getViewObject(realObject);
	  return recalculateViewObject(shape, realObject);  }  */
    public RemoteShape recalculateViewObject(RemoteShape shape, Object object) {	  //ShapeModel shape = (ShapeModel) getViewObject();	  //Object object = getRealObject();	  LabelModel labelShape = (LabelModel) shape;
	  super.recalculateViewObject(shape, object);	  
	  try {		    /*
		    Object[] params = {};			String text = (String) getTextMethod.invoke(object, params);		  */		  String imageFileName = getConcreteLabelShape().getImageFileName();
			if (labelShape.getImageFileName() != imageFileName)				labelShape.setImageFileName(imageFileName); 			oldImageFileName = imageFileName;
		} catch (Exception e) {		  System.err.println("E**: exception invoking imageFileName methods");		  e.printStackTrace();
		}	
	  return shape;   }
  public boolean recalculateRealObject() {
	  boolean retVal = super.recalculateRealObject();
	  if (textMode) return false;	  LabelModel shape = (LabelModel) computeAndMaybeSetViewObject();	  Object object = getRealObject();
	  try {
		  String imageFileName = shape.getImageFileName();		  if (oldImageFileName != imageFileName) {			  /*
		    Object[] params = {text};			setTextMethod.invoke(object, params);			  */			  getConcreteLabelShape().setImageFileName(imageFileName);
			retVal = true;		  }
		  oldImageFileName = imageFileName;			 
		} catch (Exception e) {		  System.err.println("E**: exception invoking set imageFileName  methods");		  e.printStackTrace();
		}	  return retVal;
  }
  public static LabelShapeAdapter createLabelShapeAdapter (Object obj,											  Object obj1, 											  Object parentObject, 											  String name, 											  Class inputClass, 											  boolean propertyFlag, 											  ObjectAdapter adaptor)throws RemoteException  {
      
      LabelShapeAdapter labelShapeAdapter = new LabelShapeAdapter();
	  //ShapeModel shape = uiBean.toPointModel(obj1);	  //pointAdapter.setViewObject(shape);
      return labelShapeAdapter;  }  
}
