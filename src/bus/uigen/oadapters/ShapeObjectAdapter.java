package bus.uigen.oadapters;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Stroke;
import java.beans.PropertyChangeEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import shapes.AWTShapeModel;
import shapes.ArcModel;
import shapes.CurveModel;
import shapes.ImageModel;
import shapes.LabelModel;
import shapes.LineModel;
import shapes.OvalModel;
import shapes.PointModel;
import shapes.RectangleModel;
import shapes.RemoteShape;
import shapes.StringModel;
import shapes.TextModel;
import slgc.ShapeEventNotifier;
import util.misc.Common;
import util.trace.ImageFileNameNull;
import util.trace.Tracer;
import bus.uigen.Connector;
import bus.uigen.WidgetAdapterInterface;
import bus.uigen.uiFrame;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.introspect.Attribute;
import bus.uigen.misc.ShapeMouseClickListener;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.sadapters.ConcreteAWTShape;
import bus.uigen.sadapters.ConcreteArc;
import bus.uigen.sadapters.ConcreteBoundedShape;
import bus.uigen.sadapters.ConcreteCurve;
import bus.uigen.sadapters.ConcreteImageShape;
import bus.uigen.sadapters.ConcreteLabelShape;
import bus.uigen.sadapters.ConcreteLine;
import bus.uigen.sadapters.ConcreteOval;
import bus.uigen.sadapters.ConcretePoint;
import bus.uigen.sadapters.ConcreteRectangle;
import bus.uigen.sadapters.ConcreteLocatableShape;
import bus.uigen.sadapters.ConcreteShape;
import bus.uigen.sadapters.ConcreteStringShape;
import bus.uigen.sadapters.ConcreteTextShape;
import bus.uigen.sadapters.ConcreteType;
import bus.uigen.shapes.AWTShape;
import bus.uigen.trace.IllegalPropertyNotification;
import bus.uigen.trace.ImageYLessThanZero;
import bus.uigen.trace.ShapeRightXLessThanZero;
import bus.uigen.trace.ShapeLowerYLessThanZero;
import bus.uigen.trace.ShapeObjectAdapterReceivedPropertyChangeEvent;
import bus.uigen.trace.UnknownPropertyNotification;
import bus.uigen.view.WidgetShell;
import bus.uigen.widgets.VirtualComponent;
import bus.uigen.widgets.events.VirtualMouseEvent;
public class ShapeObjectAdapter extends ClassAdapter  { 
	boolean textMode = false;
	boolean isLocatableShape = false;
	Set observables = new HashSet();
	
	public boolean getTextMode() {		return textMode;
	}
	public boolean isGraphicsLeafObject() {
		return !getTextMode();
	}
	public boolean hasOnlyGraphicsDescendents() {
		return !getTextMode();
	}
	public void addGraphicalDescendents (List<ShapeObjectAdapter> list) {
		if (!getTextMode())
		list.add(this);
		
	}
	public List<ShapeObjectAdapter> getGraphicalDescendents() {
		  Vector<ShapeObjectAdapter> vector = new Vector();
		  if (!getTextMode())
		  vector.add(this);
		  return vector;		  
	  }
		
	// will not be dsplayed in main panel so why process it	@Override
	public void setDirection() {
		if (getTextMode())
			super.setDirection();
		
	}
	@Override
	public void processDirection() {
		if (getTextMode())
			super.processDirection();
	}
	public void setTextMode (boolean newVal) {		textMode = newVal;
	}
	public ConcreteShape getConcreteShape() {		return (ConcreteShape) getConcreteObject();
	}	public void refreshConcreteObject(Object viewObject) {
		if (concreteObject == null) return;
		concreteObject.setTarget(getRealObject());	}	public void refreshConcreteObject() {
		if (concreteObject == null) return;
		concreteObject.setTarget(getRealObject());	}
	public ShapeObjectAdapter () throws RemoteException {
		
	}

  public Object getOriginalValue() {	  // should really not have overloaded the view object
		return getRealObject();
	}
  int oldX, oldY;
  Color oldColor;
  boolean oldFilled;  public void setViewObject(Object viewObject) {
  	try {
	  super.setViewObject(viewObject);
	  if (textMode) return;	  Object obj = computeAndMaybeSetViewObject();
	  //ShapeModel shape = (ShapeModel) viewObject;
	  RemoteShape shape = (RemoteShape) viewObject;
	  oldX = shape.getX();
	  oldY = shape.getY();	  //if (obj instanceof shapes.ShapeModel) {
	  if (obj instanceof RemoteShape && obj != viewObject) {		  //ShapeModel oldShape = (ShapeModel) obj;
		  RemoteShape oldShape = (RemoteShape) obj;//		  oldShape.setX(oldX);//		  oldShape.setY(oldY);
		  setX(oldShape, oldX);
		  setY(oldShape, oldY);
		  
		  setViewObjectAttributes(oldShape);
		 
		  
		  	  }
  	} catch (Exception e) {
  		System.out.println(e);
  	}	      }
  static Hashtable objectToShapeModel = new Hashtable();
    //public ShapeModel toShapeModel (Object viewObject) {
  public RemoteShape toShapeModel (Object viewObject, boolean checkCache) {
	  // this caching precludes dags's and also does not allow an object to change
	if (!checkCache)
		  return shapeModelFromConcreteObject(viewObject);

  	RemoteShape shapeModel = (RemoteShape) objectToShapeModel.get(viewObject);
  	if (shapeModel != null) return shapeModel;
  	shapeModel = shapeModelFromConcreteObject(viewObject);
  	objectToShapeModel.put(viewObject, shapeModel);
  	return shapeModel;  
  }
  
  RemoteShape shapeModelFromConcreteObject(Object viewObject) {
	  ConcreteShape concreteShape = getConcreteShape();	 
	  if (concreteShape instanceof ConcretePoint) 
		  return toPointModel((ConcretePoint) concreteShape, viewObject);
	  else if (concreteShape instanceof ConcreteRectangle)
		  return toRectangleModel ((ConcreteRectangle) concreteShape, viewObject);
	  else if (concreteShape instanceof ConcreteOval)
		  return toOvalModel ((ConcreteOval) concreteShape, viewObject);
	  else if (concreteShape instanceof ConcreteLine)
		  return toLineModel ((ConcreteLine) concreteShape, viewObject);

	  else if (concreteShape instanceof ConcreteLabelShape)
		  return toLabelModel ((ConcreteLabelShape) concreteShape, viewObject);
	  else if (concreteShape instanceof ConcreteStringShape)
		  return toStringModel ((ConcreteStringShape) concreteShape, viewObject);
	  else if (concreteShape instanceof ConcreteImageShape)
		  return toImageModel ((ConcreteImageShape) concreteShape, viewObject);
	  else if (concreteShape instanceof ConcreteTextShape) 
		  return toTextModel((ConcreteTextShape) concreteShape, viewObject);
	  else if (concreteShape instanceof ConcreteArc) 
		  return toArcModel((ConcreteArc) concreteShape, viewObject);
	  else if (concreteShape instanceof ConcreteCurve) 
		  return toCurveModel((ConcreteCurve) concreteShape, viewObject);	
	  else if (concreteShape instanceof ConcreteAWTShape)
		  return toAWTShapeModel((ConcreteAWTShape) concreteShape, viewObject);		  
	  return null;
	  
  }
  PointModel toPointModel(ConcretePoint concretePoint, Object viewObject) {
		if (viewObject instanceof PointModel)
			return (PointModel) viewObject;	
		try {
		PointModel pointModel = new PointModel(concretePoint.getX(), concretePoint.getY());
		return pointModel;
		} catch (Exception e) {
			Tracer.error("Could not get X/Y coordinates of: " + viewObject);
			e.printStackTrace();
			return null;
		}

}
 


  void setViewObjectAttributes(RemoteShape shape) {
	  recalculateViewObjectColor(shape);
	  recalculateViewObjectFilled(shape);
	  recalculateViewObjectZAxis(shape);
	  recalculateViewObject3D(shape);
	  recalculateViewObjectRounded(shape);
	  recalculateViewObjectFont(shape);
	  recalculateViewObjectFontSize(shape);
	  recalculateViewObjectGradientPaint(shape);
	  recalculateViewObjectBasicStroke(shape);
	  
  }
  // not sure if view object should even be looked at!
  OvalModel toOvalModel (ConcreteOval concreteOval, Object viewObject) {
			if (viewObject instanceof OvalModel)
				return (OvalModel) viewObject;	
			try {
			OvalModel ovalModel = new OvalModel();
			fillBounds(concreteOval, ovalModel);
			setViewObjectAttributes(ovalModel);
			return ovalModel;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
	  
  }
  RectangleModel toRectangleModel(ConcreteRectangle concreteRectangle, Object viewObject) {
		if (viewObject instanceof RectangleModel)
			return (RectangleModel) viewObject;	
		try {
		RectangleModel rectangleModel = new RectangleModel();
		fillBounds(concreteRectangle, rectangleModel);
		setViewObjectAttributes(rectangleModel);
		return rectangleModel;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

}
  LineModel toLineModel(ConcreteLine concreteLine, Object viewObject) {
		if (viewObject instanceof LineModel)
			return (LineModel) viewObject;	
		try {
		LineModel lineModel = new LineModel();
		fillBounds(concreteLine, lineModel);
		setViewObjectAttributes(lineModel);
		
		return lineModel;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

}
  TextModel toTextModel(ConcreteTextShape concreteText, Object viewObject) {
		if (viewObject instanceof TextModel)
			return (TextModel) viewObject;	
		try {
		//TextModel textModel = new TextModel(concreteText.getText());
		TextModel textModel = new TextModel(concreteText.getText(), concreteText.getWidth(), concreteText.getHeight());
//		textModel.setX(concreteText.getX());
//		textModel.setY(concreteText.getY());
		setX(textModel, concreteText.getX());
		setY(textModel, concreteText.getY());
		setViewObjectAttributes(textModel); // color still makes sense
		return textModel;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

}
  StringModel toStringModel(ConcreteTextShape concreteText, Object viewObject) {
		if (viewObject instanceof StringModel)
			return (StringModel) viewObject;	
		try {
		//TextModel textModel = new TextModel(concreteText.getText());
			int x = concreteText.getX();
			int y = concreteText.getY();
			String text = concreteText.getText();
		if (y  + concreteText.getHeight() < -1) { // width is -1 if auto set
//			Tracer.warning("The Y property of String shape " + getRealObject() + " is <= 0; the shape will not be visible." );
			ShapeLowerYLessThanZero.newCase(null, viewObject, y  + concreteText.getHeight(), this );
		}
		if (text == null) {
			Tracer.warning("The Text property of String shape " + getRealObject() + " null" );

		}
		StringModel textModel = new StringModel(text, x, y) ;
//		textModel.setX(concreteText.getX());
//		textModel.setY(concreteText.getY());
		setViewObjectAttributes(textModel); // color still makes sense
		return textModel;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

}
  AWTShapeModel toAWTShapeModel(ConcreteAWTShape aConcreteAWTShape, Object viewObject) {
		if (viewObject instanceof AWTShapeModel)
			return (AWTShapeModel) viewObject;	
		try {
		//TextModel textModel = new TextModel(concreteText.getText());
		AWTShapeModel retVal = new AWTShapeModel(aConcreteAWTShape.getAWTShape());
		
		setViewObjectAttributes(retVal); // color still makes sense
		return retVal;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

}
  ArcModel toArcModel(ConcreteArc concreteArc, Object viewObject) {
		if (viewObject instanceof ArcModel)
			return (ArcModel) viewObject;	
		try {
		//TextModel textModel = new TextModel(concreteText.getText());
		ArcModel arcModel = new ArcModel(concreteArc.getX(), concreteArc.getY(),
				concreteArc.getWidth(), concreteArc.getHeight(),
				concreteArc.getStartAngle(), concreteArc.getEndAngle());
		
		setViewObjectAttributes(arcModel); // color still makes sense
		return arcModel;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

}
  CurveModel toCurveModel(ConcreteCurve concreteCurve, Object viewObject) {
		if (viewObject instanceof CurveModel)
			return (CurveModel) viewObject;	
		try {
		Integer controlX2 = 	concreteCurve.getControlX2();
		Integer controlY2 = concreteCurve.getControlY2();
		//TextModel textModel = new TextModel(concreteText.getText());
		/*
		CurveModel curveModel =		
		new CurveModel(concreteCurve.getX(), concreteCurve.getY(),
				concreteCurve.getWidth(), concreteCurve.getHeight(),
				concreteCurve.getControlX(), concreteCurve.getControlY(),
				concreteCurve.getControlX2(), concreteCurve.getControlY2()
				);
		*/
		CurveModel curveModel =		
			new CurveModel(concreteCurve.getX(), concreteCurve.getY(),
					concreteCurve.getWidth(), concreteCurve.getHeight(),
					concreteCurve.getControlX(), concreteCurve.getControlY()
					);
		curveModel.setControlX2(concreteCurve.getControlX2());
		curveModel.setControlY2(concreteCurve.getControlY2());
		
		
		//setViewObjectAttributes(curveModel); // color still makes sense
		return curveModel;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

}
  LabelModel toLabelModel(ConcreteLabelShape concreteLabel, Object viewObject) {
		if (viewObject instanceof LabelModel)
			return (LabelModel) viewObject;	
		
		try {
		//LabelModel textModel = new LabelModel(concreteLabel.getLabel());
//		LabelModel labelModel = new LabelModel(concreteLabel.getText(), concreteLabel.getImageFileName(), concreteLabel.getWidth(), concreteLabel.getHeight());
		LabelModel labelModel = new LabelModel(concreteLabel.getText(), null, concreteLabel.getWidth(), concreteLabel.getHeight());
		String imageFile =  concreteLabel.getImageFileName();
		if (imageFile != null) {
		ImageIcon imageIcon =  Common.toImageIcon(imageFile, viewObject);
		Image image = null;
		if (imageIcon != null) {
			image = imageIcon.getImage();
		}
		labelModel.setImageData(imageFile, imageIcon, image);
		}
//		if (imageFile != null) {
//			Icon icon = new ImageIcon(imageFile);
//			if (icon.getIconHeight() < 0 || icon.getIconWidth() < 0) {
//				ClassLoader classLoader = viewObject.getClass().getClassLoader();
//				InputStream inputStream = classLoader.getResourceAsStream(imageFile);
//				byte imageBytes[] = new byte[inputStream.available()];
//	    	    inputStream.read(imageBytes);
//	    	    icon = new ImageIcon(imageBytes);
//			}
////				ImageFileMissing.newCase(imageFile, this);
////			} else {
//				labelModel.setIcon(icon);
////			}
//		}
		labelModel.setX(concreteLabel.getX());
		labelModel.setY(concreteLabel.getY());
		setViewObjectAttributes(labelModel); // color makes sense
		return labelModel;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

}
  ImageModel toImageModel(ConcreteImageShape concreteImage, Object viewObject) {
		if (viewObject instanceof ImageModel)
			return (ImageModel) viewObject;	
		try {
		//LabelModel textModel = new LabelModel(concreteLabel.getLabel());
			int x = concreteImage.getX();
			int y = concreteImage.getY();
			String imageFileName = concreteImage.getImageFileName();
		if (y < 0) {
			ImageYLessThanZero.newCase(this, this);
//			Tracer.warning("The Y property of Image shape " + getRealObject() + " is <= 0" );
		}
		if (imageFileName == null) {
			ImageFileNameNull.newCase(getRealObject(), this);
//			Tracer.warning("The ImageFileName property of Image shape " + getRealObject() + " null" );

		}
//		ImageModel imageModel = new ImageModel(concreteImage.getImageFileName(), concreteImage.getX(), concreteImage.getY());
		
		// set image Data being called twice!
		ImageModel imageModel = new ImageModel(imageFileName, concreteImage.getX(), concreteImage.getY(), concreteImage.getWidth(), concreteImage.getHeight());
	
//		imageModel.setIcon(new ImageIcon(imageFileName));
		Image image = Common.toImage(imageFileName, viewObject);
		if (image == null) {
			Tracer.error("Could not find image file:" + imageFileName);
			return null;
		}
		ImageIcon icon = new ImageIcon(image);
		imageModel.setImageData(imageFileName, icon, image);


		//		imageModel.setX(concreteImage.getX());
//		imageModel.setY(concreteImage.getY());
		setViewObjectAttributes(imageModel); // color makes sense
		return imageModel;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

}
  
  void fillBounds (ConcreteBoundedShape concreteShape, RemoteShape shapeModel ) {
	  
	  try {
	  shapeModel.setBounds(concreteShape.getX(), concreteShape.getY(), concreteShape.getWidth(), concreteShape.getHeight());
	  } catch (Exception e) {
		  e.printStackTrace();
	  }
  }

  public void setNameChild() {
	    if (getTextMode())
	    	super.setNameChild();
		
	}
  Object originalViewObject;
  ShapeMouseClickListener shapeMouseClickListener;
  
  public Object getOriginalViewObject() {
	  return originalViewObject;
  }
  public ShapeMouseClickListener getShapeMouseClickListener() {
	  return shapeMouseClickListener;
  }
  void processOriginalViewObject() {
	  if (!(originalViewObject instanceof ShapeMouseClickListener))
		  return;
	  shapeMouseClickListener = (ShapeMouseClickListener) originalViewObject;
	  uiFrame frame = getUIFrame();
	  
//	  WidgetAdapterInterface myWidgetAdapter = getWidgetAdapter();
//	  if (!(myWidgetAdapter instanceof ShapeAdapter))
//		  return;
//	  ((ShapeAdapter) myWidgetAdapter).getShapesAdapter().getView().getShapeEventNotifier().addMouseClickListener(this);
	  
  }
  public void registerAsMouseClickListener(ShapeEventNotifier shapeEventNotifier) {
	  // add yourself as listener as we may want to add right click listener
	  if (shapeMouseClickListener == null) return;
	  shapeEventNotifier.addMouseListener(this);
  }
  public void setViewObject(Object viewObject, boolean textMode) {
	  setTextMode(textMode);
	  if (textMode) {		  super.setViewObject(viewObject);		  return;
	  }
	  originalViewObject = viewObject;
	  processOriginalViewObject();
	  
	  //setViewObject(uiBean.toShapeModel(viewObject));
	  setViewObject(toShapeModel(viewObject, this.registeredAsListener));	  // link my properties too	  //linkPropertyToAdapter(viewObject, "", this);
  }  public Object computeViewObject(Object realObject) {	  if (textMode) return super.computeViewObject(getParentAdapter(), realObject);
	  //ShapeModel shape = (ShapeModel) getViewObject();
	  RemoteShape shape = (RemoteShape) computeAndMaybeSetViewObject();
	  if (shape == null)		  return super.computeViewObject(realObject);
	  return recalculateViewObject(shape, realObject);  }
  // will keep same view object for different real objects (of same class)
  public Object computeAndMaybeSetViewObject() { 
				
		return getCurrentViewObject();
	}
  public void recalculateViewObject() {	  //ShapeModel shape = (ShapeModel) getViewObject();
	  if (textMode) {
		  super.recalculateViewObject();
		  refreshChildren(getRealObject(), false);
//		  this.implicitRefresh();
		  return;
	  }
	  RemoteShape shape = (RemoteShape) computeAndMaybeSetViewObject();	  Object object = getRealObject();	  recalculateViewObject(shape, object);
	  /*
	  recalculateViewObjectColor(shape, object);
	  recalculateViewObjectFilled(shape, object);
	  recalculateViewObjectZAxis(shape, object);
	  */
	  /*
	  recalculateViewObjectColor(shape);
	  recalculateViewObjectFilled(shape);
	  recalculateViewObjectZAxis(shape);
	  recalculateViewObject3D(shape);
	  recalculateViewObjectRounded(shape);
	  recalculateViewObjectFont(shape);
	  recalculateViewObjectGradientPaint(shape);
	  recalculateViewObjectBasicStroke(shape);
	  */
	  setViewObjectAttributes(shape);	  /*
	  try {
		    Object[] params = {};			int x = ((Integer) getXMethod.invoke(object, params)).intValue();			int y = ((Integer) getYMethod.invoke(object, params)).intValue();
			if (shape.getX() != x)				shape.setX(x);
			if (shape.getY() != y)				shape.setY(y);   			oldX = x;			oldY = y;
		} catch (Exception e) {		  System.out.println("E**: exception invoking location methods");
		}
	  */   }
  
  public boolean setRealObject(Object obj) {
	  Object oldValue = getRealObject();
	  if (oldValue != obj) {
//		  	boolean retVal = super.setRealObject(obj);
			maybeAddPropertyChangeListener(obj, this);
//			return retVal;	  
	  }
	  return super.setRealObject(obj);
  }
  
  public void refreshValueButNotAtomic(Object newValue, boolean forceUpdate) {
	  	if (textMode || isTreeNode()) 
	  		super.refreshValueButNotAtomic(newValue, forceUpdate);
	  	else {
//	  	 done by refreshSelf which is not called for shapes
	  		if (getRealObject() != newValue) { 
	  			Tracer.warning("New shape  object. Let the instructor know if the display does not work correctly");
	  			setRealObject(newValue);
	  			refreshConcreteObject();
	  			maybeAddPropertyChangeListener(newValue, this);
	  		}	  			
	  		recalculateViewObject();
	  	}
																
	}
  
  public void retargetConcrete() {
		getConcreteObject().setTarget(getRealObject());		
	}
  
  
  public void removeShape() {
	  WidgetAdapterInterface wadapter = getWidgetAdapter();
	  if (wadapter != null)
		  wadapter.removeFromParentUIContainer();	
	  
  }
  public void addShape() {
	  WidgetAdapterInterface wadapter = getWidgetAdapter();
	  if (wadapter != null)
		  wadapter.setUIComponentValue(computeAndMaybeSetViewObject());	
  }
  public void cleanUpForReuse() {
	  /*
	  uiWidgetAdapterInterface wadapter = getWidgetAdapter();
	  if (wadapter != null)
		  wadapter.removeFromParentUIContainer();
		  */
	  removeShape();
	  super.cleanUpForReuse();
	  
  }
  
  public List<ObjectAdapter> getGraphicsAdaptersInPathToRoot() {
	  List<ObjectAdapter> retVal = new ArrayList();
	  ObjectAdapter currentNode = this;	  
	  while (true) {
		  retVal.add(currentNode);
		  CompositeAdapter currentParent = currentNode.getParentAdapter();		 
		  if (currentParent == null 
				  || currentParent.isRootAdapter()
//				  currentParent.isTopDisplayedAdapter || 
//				  || !currentParent.hasOnlyGraphicsDescendents()
				  )
			  break;
		  currentNode = currentParent;		  
	  }	  
	  return retVal;
  }
    //public ShapeModel recalculateViewObject(ShapeModel shape, Object object) {
  public RemoteShape recalculateViewObject(RemoteShape shape, Object object) {	  //ShapeModel shape = (ShapeModel) getViewObject();	  //Object object = getRealObject();
	  
	  return recalculateViewLocation(shape, object);	  
//	  try {//			/*
//		    Object[] params = {};
//		//			int x = ((Integer) getXMethod.invoke(object, params)).intValue();//			int y = ((Integer) getYMethod.invoke(object, params)).intValue();
//			*///			 int x = getConcreteShape().getX();
//			int y = getConcreteShape().getY();
//			if (shape.getX() != x)//				shape.setX(x);
//			if (shape.getY() != y)//				shape.setY(y);   //			oldX = x;//			oldY = y;
//		} catch (Exception e) {//		  System.out.println("E**: exception invoking location methods");//		  //System.exit(-1);
//		}	//	  return shape;
		     }
  void setX(RemoteShape shape, int newVal) {
	  try {
		  if (newVal + shape.getWidth() < 0 && shape.getHeight() != 0 && shape.getHeight() == 0)  {
			  ShapeRightXLessThanZero.newCase(this, getRealObject(), newVal, this);
//			  Tracer.warning("X < 0 of " + getRealObject());
		  }
		  shape.setX(newVal);
		  oldX = newVal;
	  } catch (Exception e) {
		  e.printStackTrace();
	  }
  }
  void setY(RemoteShape shape, int newVal) {
	  try {
		  if (newVal < 0)  {
			  ShapeLowerYLessThanZero.newCase(this, getRealObject(), newVal, this);;
		  }
		  shape.setY(newVal);
		  oldY = newVal;
	  } catch (Exception e) {
		  e.printStackTrace();
	  }
  }
  public RemoteShape recalculateViewLocation(RemoteShape shape, Object object) {
	  //ShapeModel shape = (ShapeModel) getViewObject();
	  //Object object = getRealObject();
	  if (!isLocatableShape()) return shape;
	  
	  try {
			/*
		    Object[] params = {};
		
			int x = ((Integer) getXMethod.invoke(object, params)).intValue();
			int y = ((Integer) getYMethod.invoke(object, params)).intValue();
			*/
			 int x = ((ConcreteLocatableShape) getConcreteShape()).getX();
			int y =((ConcreteLocatableShape) getConcreteShape()).getY();
			if (shape.getX() != x)
//				shape.setX(x);
				setX(shape, x);
			if (shape.getY() != y)
//				shape.setY(y);  
				setY(shape, y);
//			oldX = x; // not really needed now
//			oldY = y; // not really needed, but keeping it for now
		} catch (Exception e) {
		  System.out.println("E**: exception invoking location methods");
		  //System.exit(-1);
		}	
	  return shape;
		  
   }
//  public RemoteShape recalculateViewLocation(RemoteShape shape, Object object, int x, int y) {
//	  //ShapeModel shape = (ShapeModel) getViewObject();
//	  //Object object = getRealObject();
//	  if (!isLocatableShape()) return shape;
//	  
//	  try {
//			/*
//		    Object[] params = {};
//		
//			int x = ((Integer) getXMethod.invoke(object, params)).intValue();
//			int y = ((Integer) getYMethod.invoke(object, params)).intValue();
//			*/
////			 int x = ((ConcreteLocatableShape) getConcreteShape()).getX();
////			int y =((ConcreteLocatableShape) getConcreteShape()).getY();
//			if (shape.getX() != x)
////				shape.setX(x);
//				setX(shape, x);
//			if (shape.getY() != y)
////				shape.setY(y);  
//				setY(shape, y);
//			oldX = x;
//			oldY = y;
//		} catch (Exception e) {
//		  System.out.println("E**: exception invoking location methods");
//		  //System.exit(-1);
//		}	
//	  return shape;
//		  
//   }
  // need to worry about changing to another graphics or textual adapter.
  public ObjectAdapter replaceAdapter(ObjectAdapter child, 
		  Object newValue) {
	  System.out.println("E*** need to worry about changing to another graphics or textual adapter");
	  return super.replaceAdapter(child, newValue);
	  
  }
  /*
  public void propagateChange() {
  	super.propagateChange();
  	//propagate to ShapeModel if originating view object is some other  object
  	if (!parent.isSLModelAdapter())
  		recalculateViewObject();
  
  }
  */  //public Color recalculateViewObjectColor(ShapeModel shape, Object object) {
  //public Color recalculateViewObjectColor(RemoteShape shape, Object object) {
  void setColor(RemoteShape shape, Color newVal) {
	  try {
		 
		  shape.setColor(newVal);
		oldColor = newVal;

	  } catch (Exception e) {
		  e.printStackTrace();
	  }
  }
  public Color recalculateViewObjectColor(RemoteShape shape) {	  //ShapeModel shape = (ShapeModel) getViewObject();	  //Object object = getRealObject();
	  //if (getColorMethod == null) return null;
	  if (getTextMode()) return null;	  if (!getConcreteShape().hasColor()) return null;
	  try {		    /*
		    Object[] params = {};			Color color = ((Color) getColorMethod.invoke(object, params));			*/
		    //shape.setFilled(true);			Color color = getConcreteShape().getColor();
			if (oldColor != color)
				setColor(shape, color);//				shape.setColor(color);//			oldColor = color; // not needed now
		} catch (Exception e) {		  System.out.println("E**: exception invoking color methods");
		}	
	  return oldColor;   }
  
  void setFilled(RemoteShape shape, boolean newVal) {
	  try {
		 
		  shape.setFilled(newVal);
		oldFilled = newVal;

	  } catch (Exception e) {
		  e.printStackTrace();
	  }
  }
 
  public boolean recalculateViewObjectFilled(RemoteShape shape) {
	  
	  if (getTextMode()) return false;
	  if (!getConcreteShape().hasFilled()) return false;
	  try {
		    
			boolean filled = getConcreteShape().getFilled();
			if (oldFilled != filled)
//				shape.setFilled(filled);
				setFilled(shape, filled);
			oldFilled = filled;
		} catch (Exception e) {
		  System.out.println("E**: exception invoking fill methods");
		}	
	  return oldFilled;
   }
  boolean oldRounded;
  public boolean recalculateViewObjectRounded(RemoteShape shape) {
	  
	  if (getTextMode()) return false;
	  if (!getConcreteShape().hasRounded()) return false;
	  try {
		    
			boolean rounded = getConcreteShape().getRounded();
			if (oldRounded != rounded)
				shape.setRounded(rounded);
			oldRounded = rounded;
		} catch (Exception e) {
		  System.out.println("E**: exception invoking rounded methods");
		}	
	  return oldRounded;
   }
  
  int zAxis;
//  public int recalculateViewObjectZAxis(RemoteShape shape) {
//	  
//	  if (getTextMode()) return 0;
//	  if (!getConcreteShape().hasZIndex()) {
//		  return this.getIndex();
//	  }
//	  try {
//		    
//			int zAxis = getConcreteShape().getZIndex();
//			if (oldZAxis != zAxis)				
//				shape.setZIndex(zAxis);
//			oldZAxis = zAxis;
//		} catch (Exception e) {
//		  System.out.println("E**: exception invoking Z axis method");
//		}	
//	  return oldZAxis;
//   }
  
  static final int autoZIndex = 0;
public int recalculateViewObjectZAxis(RemoteShape shape) {
	  
	  if (getTextMode()) return 0;
//	  if (!getConcreteShape().hasZIndex()) {
//		  return this.getIndex();
//	  }
	  try {
		    
			//int zAxis = getConcreteShape().getZIndex();
//		    int zAxis;
		    zAxis= getTreeIndex();
			//if (oldZAxis != zAxis)	
//			if (oldZAxis != shape.getZIndex())
			if (zAxis != shape.getZIndex()) // everywhere old variable is kept, but this seems best as shape has it

				shape.setZIndex(zAxis);
//			oldZAxis = zAxis;
		} catch (Exception e) {
		  System.out.println("E**: exception invoking Z axis method");
		}	
//	  return oldZAxis;
	  return zAxis;
   }
  
  boolean old3D;
  public boolean recalculateViewObject3D(RemoteShape shape) {
	  
	  if (getTextMode()) return false;
	  if (!getConcreteShape().has3D()) {
		  return false;
	  }
	  try {
		    
			boolean threeD = getConcreteShape().get3D();
			if (old3D != threeD)				
				shape.set3D(threeD);
			old3D = threeD;
		} catch (Exception e) {
		  System.out.println("E**: exception invoking Z axis method");
		}	
	  return old3D;
   }
  
  void setFont(RemoteShape shape, Font newVal) {
	  try {
		 
		  shape.setFont(newVal);
		oldFont = newVal;

	  } catch (Exception e) {
		  e.printStackTrace();
	  }
  }
  
  Font oldFont;
  public Font recalculateViewObjectFont(RemoteShape shape) {
	  
	  if (getTextMode()) return null;
	  if (!getConcreteShape().hasFont()) {
		  return null;
	  }
	  try {
		   
			Font font = getConcreteShape().getFont();
			if (oldFont != font)				
//				shape.setFont(font);
				setFont(shape, font);
//			oldFont = font;
//			int fontSize = getConcreteShape().getFontSize();
//			if (oldFontSize != fontSize)
//				shape.setFontSize(fontSize);
		} catch (Exception e) {
		  System.out.println("E**: exception invoking font method");
		}	
	  return oldFont;
   }
  void setFontSize(RemoteShape shape, int newVal) {
	  try {
		 
		  shape.setFontSize(newVal);
		oldFontSize = newVal;

	  } catch (Exception e) {
		  e.printStackTrace();
	  }
  }
  int oldFontSize;
  public int recalculateViewObjectFontSize(RemoteShape shape) {
	  
	  if (getTextMode()) return -1;
	  if (!getConcreteShape().hasFontSize()) {
		  return -1;
	  }
	  try {
		   
//			Font font = getConcreteShape().getFont();
//			if (oldFont != font)				
//				shape.setFont(font);
			int fontSize = getConcreteShape().getFontSize();
			if (oldFontSize != fontSize)
//				shape.setFontSize(fontSize);
				setFontSize(shape, fontSize);
//			oldFontSize = fontSize;
		} catch (Exception e) {
		  System.out.println("E**: exception invoking font method");
		}	
	  return oldFontSize;
   }
  Paint oldGradientPaint;
  public Paint recalculateViewObjectGradientPaint(RemoteShape shape) {
	  
	  if (getTextMode()) return null;
	  if (!getConcreteShape().hasGradientPaint()) {
		  return null;
	  }
	  try {
		   
			Paint gradientPaint = getConcreteShape().getGradientPaint();
			if (oldGradientPaint != gradientPaint)				
				shape.setPaint(gradientPaint);
			oldGradientPaint = gradientPaint;
		} catch (Exception e) {
		  System.out.println("E**: exception invoking paint method");
		}	
	  return oldGradientPaint;
   }
  
  Stroke oldBasicStroke;
  public Stroke recalculateViewObjectBasicStroke(RemoteShape shape) {
	  
	  if (getTextMode()) return null;
	  if (!getConcreteShape().hasBasicStroke()) {
		  return null;
	  }
	  try {
		   
			Stroke basicStroke = getConcreteShape().getBasicStroke();
			if (oldBasicStroke != basicStroke)				
				shape.setStroke(basicStroke);
			oldBasicStroke = basicStroke;
		} catch (Exception e) {
		  System.out.println("E**: exception invoking stroke method");
		}	
	  return oldBasicStroke;
   }
  
  public boolean recalculateRealObject() {	  if (textMode) return super.recalculateRealObject();
	  boolean retVal = false;
	  if (!isEditable)
		  return false;
	  if (!isLocatableShape)
		  return false;	  //ShapeModel shape = (ShapeModel) getViewObject();
	  RemoteShape shape = (RemoteShape) computeAndMaybeSetViewObject();	  //Object object = getRealObject();	  ConcreteLocatableShape concreteShape =  (ConcreteLocatableShape) getConcreteShape();
	  this.setSupressPropertyChange(true);
	  
	  try {
		  int x = shape.getX();
		  int y = shape.getY();
		  if (oldX != x && oldY != y) {
			  concreteShape.setXY(x, y);
		  } else 		  if (oldX != x) {			  /*
		    Object[] params = {new Integer(x)};			setXMethod.invoke(object, params);			  */			  concreteShape.setX(x);
			retVal = true;		  } else		  if (oldY != y) {
			  //Object[] params = {new Integer(y)};
			//setYMethod.invoke(object, params);
			  concreteShape.setY(y);			retVal = true;
		  }
		  oldX = x;
		  oldY = y;			 
		} catch (Exception e) {		  System.out.println("E**: exception invoking location methods");
		  this.setSupressPropertyChange(false);
		}
		this.setSupressPropertyChange(false);	  return retVal;
  }
  
  public boolean isLocatableShape() {
	  return isLocatableShape;
  }  
  public boolean recalculateRealObjectColor() {
	  boolean retVal = false;	  //ShapeModel shape = (ShapeModel) getViewObject();
	  RemoteShape shape = (RemoteShape) computeAndMaybeSetViewObject();	  //Object object = getRealObject();	  ConcreteShape concreteShape = getConcreteShape();
	  try {
		  Color color = shape.getColor();
		  if (color != oldColor) {			  /*
		    Object[] params = {color};			setColorMethod.invoke(object, params);			  */			 concreteShape.setColor(color); 
			retVal = true;		  }
		  oldColor = color; 
		} catch (Exception e) {		  System.out.println("E**: exception invoking  color methods");		 
		}	  return retVal;
  }  public void setWidgetAdapter(WidgetAdapterInterface wa) {
	  super.setWidgetAdapter(wa);	  /*
	  if (isUnEditable())
		  wa.setUIComponentUneditable();
	  */  }
  // should really create a separate class
  public ConcreteLocatableShape getConcreteLocatableShape() {
	  return (ConcreteLocatableShape) getConcreteShape();
  }
  public boolean isUnEditable() {
	  return isLocatableShape() &&  
			  (getConcreteLocatableShape().isXReadOnly()
//					  || 
					  && // if either is not readonly, allow editing and simply do not change the readonly
			  getConcreteLocatableShape().isYReadOnly());	  //return  (setXMethod == null || setYMethod == null);		 
  }
  boolean supressPropertyChange = false;
  public void setSupressPropertyChange(boolean newVal) {
  	supressPropertyChange = newVal;
  }
  public boolean supressPropertyChange() {
  	return supressPropertyChange;
  }
  
  public void subPropertyChange(PropertyChangeEvent evt){
	  ShapeObjectAdapterReceivedPropertyChangeEvent.newCase(this, evt);
  	if (supressPropertyChange()) {
  		setSupressPropertyChange(false);
  		return;
  	}
  			if (textMode) {
  				super.subPropertyChange(evt);
  			} else {
  			boolean retVal = respondToPropertyChange (evt);
  			if (!retVal) {
  				if (!shapePropertiesSet.contains(evt.getPropertyName()))
  					UnknownPropertyNotification.newCase(evt.getPropertyName(), evt.getSource(), this);			recalculateViewObject();
  			}
  			}			//recalculateViewObjectColor()			if (getWidgetAdapter() != null)
			getWidgetAdapter().setUIComponentValue(this.computeAndMaybeSetViewObject());
		
	}
  boolean respondToPropertyChange (PropertyChangeEvent event) {
		 try {
		  String propertyName = event.getPropertyName().toLowerCase();
		  if (textMode) {
			  super.subPropertyChange(event);
			  return true; 
		  }
		  RemoteShape shape = (RemoteShape) computeAndMaybeSetViewObject();
		  if (propertyName.equals("x")) {
			  setX(shape, (int) event.getNewValue());
			  return true;
		  }
		  if (propertyName.equals("y")) {
			  setY(shape, (int) event.getNewValue());
			  return true;
		  }
		  if (propertyName.equals("color")) {
			  setColor(shape, (Color) event.getNewValue());
			  return true;
		  }
		  if (propertyName.equals("font")) {
			  setFont(shape, (Font) event.getNewValue());
			  return true;
		  }
		  if (propertyName.equals("fontsize")) {
			  setFontSize(shape, (int) event.getNewValue());
			  return true;
		  }
		  return false;
		 } catch (Exception e) {
			IllegalPropertyNotification.newCase(event, event.getSource(), this);

			 return false;
		 }
	  }
	 static String[] shapePropertiesArray = {"x", "y", "color", "filled", "zaxis", "3d", "rounded", "font", "fontsize", "gradientpaint", "basicstroke"};
	 
	  static Set<String> shapePropertiesSet = new HashSet();
	  
	  static void fillShapeProperties() {
		  for (String propertyName:shapePropertiesArray) {
			  shapePropertiesSet.add(propertyName);
		  }
	  }
	  
	  static {
		  fillShapeProperties();
	  }  /*
  public uiObjectAdapter createAdapter (Container containW,
											   Object obj, 											  Object obj1, 											  Object parentObject, 											  String name, 											  Class inputClass, 											  boolean propertyFlag, 											  uiObjectAdapter adaptor,											  boolean textMode) {	  return createShapeAdapter(containW, obj, obj1, parentObject, name, inputClass, propertyFlag, adaptor, textMode);
  }  
  public static uiShapeAdapter createShapeAdapter (Container containW,
											 Object obj, 											  Object obj1, 											  Object parentObject, 											  String name, 											  Class inputClass, 											  boolean propertyFlag, 											  uiObjectAdapter adaptor,											  //int whichShape, 
											  boolean textMode) {	   int whichShape = uiBean.NO_SHAPE;	if (!textMode && obj1 != null ) 		whichShape = uiBean.whichShape(obj1.getClass());	  if (textMode ||  whichShape == uiBean.NO_SHAPE) return null;		   
	   uiShapeAdapter classAdapter;	   switch (whichShape) {	   case uiBean.POINT_SHAPE: classAdapter = uiPointAdapter.createPointAdapter(obj, obj1, parentObject, name, inputClass, propertyFlag, adaptor);						 break;
	   case uiBean.RECTANGLE_SHAPE: classAdapter = uiRectangleAdapter.createRectangleAdapter(obj, obj1, parentObject, name, inputClass, propertyFlag, adaptor);						 break;	
	   case uiBean.OVAL_SHAPE: classAdapter = uiOvalAdapter.createOvalAdapter(obj, obj1, parentObject, name, inputClass, propertyFlag, adaptor);						 break;	
		case uiBean.LINE_SHAPE: classAdapter = uiLineAdapter.createLineAdapter(obj, obj1, parentObject, name, inputClass, propertyFlag, adaptor);						 break;
		case uiBean.TEXT_SHAPE: classAdapter = uiTextShapeAdapter.createTextShapeAdapter(obj, obj1, parentObject, name, inputClass, propertyFlag, adaptor);						 break;				 	   default: classAdapter = new uiShapeAdapter();
	   }
	   	  classAdapter.setRealObject(obj1);	  classAdapter.setMethods();
      classAdapter.setViewObject(obj);  
      setAdapterAttributes(classAdapter, obj, parentObject, name);
      classAdapter.setPropertyClass(inputClass);
      classAdapter.setPropertyName(name);
      if (propertyFlag) {
	classAdapter.setAdapterType(uiObjectAdapter.PROPERTY_TYPE);
	linkPropertyToAdapter(parentObject, name, classAdapter);
      }
	  // link my properties too	  linkPropertyToAdapter(obj1, "", classAdapter);
      classAdapter.setParentAdapter((uiContainerAdapter)adaptor);
	  classAdapter.setUIFrame(adaptor.getUIFrame());
      	  classAdapter.processAttributeList();	  
	  if (classAdapter.getUIComponent() == null) {
			
			String label = (String) classAdapter.getMergedAttributeValue(AttributeNames.LABEL);
			//label = getGenericWidget().getLabel();			containW.remove(classAdapter.getGenericWidget());	        //a.setTempAttributeValue(AttributeNames.LABEL, label);
		    //a.getGenericWidget().setLabel(label);
		    //a.setTempAttributeValue(AttributeNames.LABEL,label );			//Attribute a = new Attribute(AttributeNames.LABEL,label);   
			classAdapter.getWidgetAdapter().processAttribute(new Attribute("class." + AttributeNames.LABEL,label));			//continue;		}
	  	  
      
      return classAdapter;  }  */
  public  void init (ConcreteType concreteObject,													   /*Container containW,*/
													   Object obj, 											  Object obj1, 											  Object parentObject, 
											  int posn,											  String name, 											  ClassProxy inputClass, 											  boolean propertyFlag, 											  ObjectAdapter adaptor,
											  boolean textMode) {
	  	  super.init(concreteObject,													//containW, 
													obj, 
									obj1, 									parentObject, 
									posn,									name, 									inputClass, 									propertyFlag, 									 adaptor,
									 textMode);
	  isLocatableShape = concreteObject != null && concreteObject instanceof ConcreteLocatableShape;
	  /*	  setTextMode(textMode);	  if (!textMode) {
		  setViewObject(uiBean.toShapeModel(obj));	  }	  */
	  // link my properties too	  /*
	  if (!textMode) {	      linkPropertyToAdapter(obj1, "", this);	  
	  //if (getUIComponent() == null) {
			
			String label = (String) this.getMergedAttributeValue(AttributeNames.LABEL);
			//label = getGenericWidget().getLabel();			containW.remove(this.getGenericWidget());	        //a.setTempAttributeValue(AttributeNames.LABEL, label);
		    //a.getGenericWidget().setLabel(label);
		    //a.setTempAttributeValue(AttributeNames.LABEL,label );			//Attribute a = new Attribute(AttributeNames.LABEL,label);   
			this.getWidgetAdapter().processAttribute(new Attribute("class." + AttributeNames.LABEL,label));			//continue;		}
	  */  }
  public void setDefaultAttributes() {	  super.setDefaultAttributes();
  }
  
  public boolean isAtomic () {	  if (getTextMode()) return super.isAtomic();	  return true;
  }  
  public boolean processPreferredWidget(String widgetClassName) {
	  if (isTreeNode())
		  return false;	  if (getTextMode())		  return super.processPreferredWidget(widgetClassName);	  	
	   if (getWidgetAdapter() == null) {
			try {				
				VirtualComponent c = Connector.linkAdapterToComponent(this, widgetClassName);				
			} catch (Exception e) {				System.out.println("could not link");
				e.printStackTrace();
			}
		}
			   return false;
	}
  Vector nullVector = new Vector();  public  Enumeration getChildAdapters() {
	  if (!getTextMode())
	  return nullVector.elements();
	  else
		  return super.getChildAdapters();
  }   boolean processAttribute(Attribute attribute) {	  if (!getTextMode())
	  return false;
	  else return super.processAttribute(attribute);  }
    public void processAttributesWithDefaults() {	  super.processAttributesWithDefaults();	  if (!getTextMode()) {
		  if (this.getWidgetAdapter() == null) return;	      //linkPropertyToAdapter(getRealObject(), "", this);	  
			String label = (String) this.getMergedAttributeValue(AttributeNames.LABEL);			this.getWidgetAdapter().processAttribute(new Attribute("class." + AttributeNames.LABEL,label));
			//label = getGenericWidget().getLabel();
			WidgetShell myGenericWidget = this.getGenericWidget();			if (myGenericWidget == null) return;
			if (myGenericWidget.getParent() == null) return;			//myGenericWidget.getParent().remove(myGenericWidget);
			myGenericWidget.getParent().remove(myGenericWidget.getContainer());
			setWidgetShell(null);	        //a.setTempAttributeValue(AttributeNames.LABEL, label);
		    //a.getGenericWidget().setLabel(label);
		    //a.setTempAttributeValue(AttributeNames.LABEL,label );			//Attribute a = new Attribute(AttributeNames.LABEL,label);   
			//this.getWidgetAdapter().processAttribute(new Attribute("class." + AttributeNames.LABEL,label));			//continue;		}
  }
  
    public void setWidgetShell (WidgetShell newValue) {	  if (getTextMode()) super.setWidgetShell(newValue);	  else super.setWidgetShell(null);
	}
  
  public  void createChildrenBasic() {
	  if (getTextMode())
		  super.createChildrenBasic();
	  childrenCreated = true;
	
	}
	
   
  /*  public void processAttributeList() {
	  super.processAttributeList();
	  if (!getTextMode()) {	      linkPropertyToAdapter(getRealObject(), "", this);	  
			String label = (String) this.getMergedAttributeValue(AttributeNames.LABEL);
			//label = getGenericWidget().getLabel();
			uiGenericWidget myGenericWidget = this.getGenericWidget();			myGenericWidget.getParent().remove(myGenericWidget);	        //a.setTempAttributeValue(AttributeNames.LABEL, label);
		    //a.getGenericWidget().setLabel(label);
		    //a.setTempAttributeValue(AttributeNames.LABEL,label );			//Attribute a = new Attribute(AttributeNames.LABEL,label);   
			this.getWidgetAdapter().processAttribute(new Attribute("class." + AttributeNames.LABEL,label));			//continue;		}  }  */
  /*  public  void createChildrenBasic() {
		if (getTextMode())			super.createChildrenBasic();
		childrenCreated = true;
  }
  public  boolean addChildUIComponents() {	  if (getTextMode())			return super.addChildUIComponents();	  return true;
				
	}  */
  public void processSynthesizedAttributesWithDefaults() {	  if (getTextMode())			super.processSynthesizedAttributesWithDefaults();
  }
  /*	
  public  void createChildren() {
		if (getTextMode())			super.createChildren();
		childrenCreated = true;			
	}
  public  void createChildrenPropagating() {	  if (getTextMode())			super.createChildrenPropagating();
		childrenCreated = true;
	}
  */
@Override
public void mouseClicked(List<RemoteShape> theShapes, VirtualMouseEvent mouseEvent) {
	
	if (! theShapes.contains(getViewObject()))
		return;	
//	ShapesAdapter shapesAdapter = ((ShapeAdapter) getWidgetAdapter()).getShapesAdapter();
//	if (theShapes.get(0) == getViewObject())
//		shapesAdapter.maybeShowPopup(mouseEvent, this);
	if (shapeMouseClickListener == null)
			return;
//	int clickType = (mouseEvent.getClickCount() >= 2)?VirtualMouseAdapter.MouseDoubleClick:VirtualMouseAdapter.MouseClick;
	shapeMouseClickListener.mouseClicked(mouseEvent);
	//refresh();
}

public String getDebugInfo() {
//	RemoteShape shape = (RemoteShape) getViewObject();
	String name = "root" + getPath();
//	String name = uiGenerator.toPath(this, propertyName, index);
	return getPatternName() + " " + name + " " + getViewObject();
//	try {
//		int x = shape.getX();
//		int y = shape.getY();
//		int width = shape.getWidth();
//		int height = shape.getHeight();
//		boolean isFilled = shape.
//	} catch (Exception e) {
//		return "";
//	}
	
}
@Override
public String getPatternName() {
	return getConcreteShape().getPatternName();
}

  
}
