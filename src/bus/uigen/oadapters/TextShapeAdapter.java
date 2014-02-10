package bus.uigen.oadapters;
import java.awt.font.TextAttribute;
import java.beans.PropertyChangeEvent;
import java.rmi.RemoteException;
import java.util.Map;

import shapes.RemoteShape;
import shapes.RemoteText;
import util.models.RemotePropertyChangeListener;
import bus.uigen.sadapters.ConcreteTextShape;
import bus.uigen.trace.IllegalPropertyNotification;
import bus.uigen.trace.ShapeRightXLessThanZero;
public class TextShapeAdapter extends BoundedShapeAdapter implements RemotePropertyChangeListener  { 
	public TextShapeAdapter () throws RemoteException {
		
	}	public ConcreteTextShape getConcreteTextShape() {		return (ConcreteTextShape) getConcreteObject();
	}
	  String oldText;  public void setViewObject(Object viewObject) {
	  try {
	  super.setViewObject(viewObject);
	  if (getTextMode()) return;	  Object obj = computeAndMaybeSetViewObject();
	  //TextModel shape = (TextModel) viewObject;
	  shapes.RemoteText shape = (shapes.RemoteText) viewObject;
	  oldText = shape.getText();	  if (obj instanceof shapes.RemoteText) {		  RemoteText oldShape = (RemoteText) obj;		  oldShape.setText(oldText);	  }
	  setAttributedString(shape);
	  setTextAttributes(shape);
	  } catch (Exception e) {
		  e.printStackTrace();
	  }	    }
  
//  AttributedString attributedString;
  
  void setAttributedString (RemoteText aRemoteText) {
	  if (!getConcreteTextShape().hasAttributedString()) return;
	 
	  try {
		aRemoteText.setAttributedString(getConcreteTextShape().getAttributedString());
	} catch (RemoteException e) {
		e.printStackTrace();
	}
	  
	  
  }
  
  void setTextAttributes (RemoteText aRemoteText) {
	  Map<TextAttribute, Object> textAttributes = getTextAttributes();
	  if (textAttributes != null)
		try {
			aRemoteText.setTextAttributes(textAttributes);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	  
	  
  }
  void setText(RemoteText shape, String newVal) {
	  try {
		 
		  shape.setText(newVal);
		  oldText = newVal;
	  } catch (Exception e) {
		  e.printStackTrace();
	  }
  }
  boolean respondToPropertyChange (PropertyChangeEvent event) {
		 try {
		  String propertyName = event.getPropertyName().trim().toLowerCase();
		  RemoteText shape = (RemoteText) computeAndMaybeSetViewObject();
		  if (propertyName.equals("text")) {
			  setText(shape, (String) event.getNewValue());
			  return true;
		  }
		  
		  return super.respondToPropertyChange(event);
		 } catch (Exception e) {
			IllegalPropertyNotification.newCase(event, event.getSource(), this);

			 return false;
		 }
	  }
	 static String[] textShapePropertiesArray = {"text"};
	 
	  
	  static void fillTextShapeProperties() {
		  for (String propertyName:textShapePropertiesArray) {
			  shapePropertiesSet.add(propertyName);
		  }
	  }
	  
	  static {
		  fillTextShapeProperties();
	  }
    public RemoteShape recalculateViewObject(RemoteShape shape, Object object) {	  //ShapeModel shape = (ShapeModel) getViewObject();	  //Object object = getRealObject();	  RemoteText textShape = (RemoteText) shape;
	  super.recalculateViewObject(shape, object);	  
	  try {		    /*
		    Object[] params = {};			String text = (String) getTextMethod.invoke(object, params);		  */		  String text = getConcreteTextShape().getText();
			if (! (textShape.getText().equals(text))) {//				textShape.setText(text); 
				setText(textShape, text);
//				if (text.equals("goodye")) {
//					System.out.println("goodye");
//				}
			}//			oldText = text;
			  setAttributedString(textShape);
			  setTextAttributes(textShape);

		} catch (Exception e) {		  System.out.println("E**: exception invoking text methods");		  e.printStackTrace();
		}	
	  return shape;   }
  public boolean recalculateRealObject() {
	  boolean retVal = super.recalculateRealObject();

	  if (textMode) return false;
		  RemoteText shape = (RemoteText) computeAndMaybeSetViewObject();	  Object object = getRealObject();
	  try {
		  String text = shape.getText();		  if (oldText != text) {			  /*
		    Object[] params = {text};			setTextMethod.invoke(object, params);			  */			  getConcreteTextShape().setText(text);
			retVal = true;		  }
		  oldText = text;			 
		} catch (Exception e) {		  System.out.println("E**: exception invoking set text  methods");		  e.printStackTrace();
		}	  return retVal;
  }
  public static TextShapeAdapter createTextShapeAdapter (Object obj,											  Object obj1, 											  Object parentObject, 											  String name, 											  Class inputClass, 											  boolean propertyFlag, 											  ObjectAdapter adaptor)throws RemoteException  {
      
      TextShapeAdapter textShapeAdapter = new TextShapeAdapter();
	  //ShapeModel shape = uiBean.toPointModel(obj1);	  //pointAdapter.setViewObject(shape);
      return textShapeAdapter;  }  
}
