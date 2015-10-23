package bus.uigen.oadapters;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.rmi.RemoteException;

import javax.swing.ImageIcon;

import shapes.ImageModel;
import shapes.RemoteImage;
import shapes.RemoteShape;
import shapes.RemoteText;
import util.misc.Common;
import util.models.RemotePropertyChangeListener;
import bus.uigen.sadapters.ConcreteImageShape;
import bus.uigen.trace.IllegalPropertyNotification;
public class ImageShapeAdapter extends BoundedShapeAdapter implements RemotePropertyChangeListener  { 
//public class ImageShapeAdapter extends ShapeObjectAdapter implements RemotePropertyChangeListener  { 
	public ImageShapeAdapter () throws RemoteException {
		
	}	public ConcreteImageShape getConcreteImageShape() {		return (ConcreteImageShape) getConcreteObject();
	}
	  String oldImageFileName;  public void setViewObject(Object viewObject) {
	  try {
	  super.setViewObject(viewObject);
	  if (getTextMode()) return;	  Object obj = computeAndMaybeSetViewObject();
	  RemoteImage shape = (RemoteImage) viewObject;
	  oldImageFileName = shape.getImageFileName();	  if (obj instanceof shapes.LabelModel && oldImageFileName != null) {		  ImageModel oldShape = (ImageModel) obj;
		  //if (!oldShape.getImageFileName().equals(oldImageFileName))
		  if (!oldImageFileName.equals(oldShape.getImageFileName()))		  oldShape.setImageFileName(oldImageFileName);	  }
	  } catch (Exception e) {
		  e.printStackTrace();
	  }
	    }
  void setImageFileName(RemoteImage shape, String newVal) {
	  try {
		 
		  shape.setImageFileName(newVal);
		  Object object = getRealObject();
		  Image image = Common.toImage(newVal, object);
			ImageIcon icon = new ImageIcon(image);
		
		shape.setImageData(newVal, icon, Common.toImage(newVal, object));
		  oldImageFileName = newVal;
	  } catch (Exception e) {
		  e.printStackTrace();
	  }
  }
  boolean respondToPropertyChange (PropertyChangeEvent event) {
		 try {
		  String propertyName = event.getPropertyName().trim().toLowerCase();
		  if (textMode) {
			  super.subPropertyChange(event);
			  return true;
		  }
		  RemoteImage shape = (RemoteImage) computeAndMaybeSetViewObject();
		  if (propertyName.equals("imagefilename")) {
			  setImageFileName(shape, (String) event.getNewValue());			
			  return true;
		  }
		  
		  return super.respondToPropertyChange(event);
		 } catch (Exception e) {
			IllegalPropertyNotification.newCase(event, event.getSource(), this);

			 return false;
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
    public RemoteShape recalculateViewObject(RemoteShape shape, Object object) {	  //ShapeModel shape = (ShapeModel) getViewObject();	  //Object object = getRealObject();	  ImageModel imageShape = (ImageModel) shape;
	  super.recalculateViewObject(shape, object);	  
	  try {		    /*
		    Object[] params = {};			String text = (String) getTextMethod.invoke(object, params);		  */		  String imageFileName = getConcreteImageShape().getImageFileName();
			if (!imageShape.getImageFileName().equals(imageFileName)) {
				setImageFileName (imageShape, imageFileName);//				Image image = Common.toImage(imageFileName, object);
//				ImageIcon icon = new ImageIcon(image);
//			
//			imageShape.setImageData(imageFileName, icon, Common.toImage(imageFileName, object));
			}
//			oldImageFileName = imageFileName;
		} catch (Exception e) {		  System.out.println("E**: exception invoking imageFileName methods");		  e.printStackTrace();
		}	
	  return shape;   }
  public boolean recalculateRealObject() {
	  boolean retVal = super.recalculateRealObject();
	  if (textMode) return false;	  ImageModel shape = (ImageModel) computeAndMaybeSetViewObject();	  Object object = getRealObject();
	  try {
		  String imageFileName = shape.getImageFileName();		  if (oldImageFileName != imageFileName) {			  /*
		    Object[] params = {text};			setTextMethod.invoke(object, params);			  */			  getConcreteImageShape().setImageFileName(imageFileName);
			retVal = true;		  }
		  oldImageFileName = imageFileName;			 
		} catch (Exception e) {		  System.out.println("E**: exception invoking set imageFileName  methods");		  e.printStackTrace();
		}	  return retVal;
  }
  public static ImageShapeAdapter createImageShapeAdapter (Object obj,											  Object obj1, 											  Object parentObject, 											  String name, 											  Class inputClass, 											  boolean propertyFlag, 											  ObjectAdapter adaptor)throws RemoteException  {
      
      ImageShapeAdapter imageShapeAdapter = new ImageShapeAdapter();
	  //ShapeModel shape = uiBean.toPointModel(obj1);	  //pointAdapter.setViewObject(shape);
      return imageShapeAdapter;  }  
}
