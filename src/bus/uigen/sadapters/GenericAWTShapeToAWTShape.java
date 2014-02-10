package bus.uigen.sadapters;import java.awt.Shape;import util.annotations.StructurePatternNames;import util.trace.Tracer;import bus.uigen.uiFrame;
import bus.uigen.attributes.AttributeNames;import bus.uigen.controller.MethodInvocationManager;import bus.uigen.introspect.ClassDescriptorCache;import bus.uigen.introspect.ClassDescriptorInterface;import bus.uigen.introspect.IntrospectUtility;import bus.uigen.introspect.PropertyDescriptorProxy;import bus.uigen.reflect.ClassProxy;import bus.uigen.reflect.MethodProxy;import bus.uigen.undo.CommandListener;
public class GenericAWTShapeToAWTShape extends GenericShapeToShape 
											implements ConcreteAWTShape {	transient MethodProxy getShapeMethod = null;	transient MethodProxy setShapeMethod = null;	boolean isAWTShape; // perhaps shold have a different adapter
	public GenericAWTShapeToAWTShape(Object theTargetObject, uiFrame theFrame) {		init(theTargetObject, theFrame );
	}		public void setMethods(ClassProxy c) {		super.setMethods(c);		setAWTShapeMethods(c);	}    public void setAWTShapeMethods(ClassProxy c) {	   isAWTShape = IntrospectUtility.isDeclaredAWTShape(c);	  if (!isAWTShape) {	  getShapeMethod = getReadMethod(AWT_SHAPE, c.awtShapeClass());	  setShapeMethod = getWriteMethod(AWT_SHAPE, c.awtShapeClass());	  }	    }
		public GenericAWTShapeToAWTShape () {
	}	 public static String AWT_SHAPE = "Shape";		public String programmingPatternKeyword() {			return  super.programmingPatternKeyword() + AttributeNames.KEYWORD_SEPARATOR + AWT_SHAPE;		}
	
		public String typeKeyword() {			return super.typeKeyword() + AttributeNames.KEYWORD_SEPARATOR + AWT_SHAPE ;		}		public String getPatternName() {			return StructurePatternNames.AWT_SHAPE_PATTERN;				}		@Override		public Shape getAWTShape() {			if (isAWTShape)				return (Shape) targetObject;						  return  ((Shape) MethodInvocationManager.invokeMethod(getShapeMethod, targetObject, emptyParams));		}//		@Override//		public boolean isEditable() {//			return !isAWTShape;//		}		// actually this method should never be called		@Override		public void setAWTShape(Shape aNewVal) {		if (isAWTShape){			Tracer.error("Cannot invoke set shape method. ");			return;		}			 Object[] params = {aNewVal};			   MethodInvocationManager.invokeMethod(frame, targetObject, setShapeMethod, params);					}		public void setAWTShape(Shape aNewVal, CommandListener cl) {			 Object[] params = {aNewVal};			   MethodInvocationManager.invokeMethod(frame, targetObject, setShapeMethod, params, cl);					}		Object[] emptyParams = {};		  
}