package bus.uigen.sadapters;import util.annotations.StructurePatternNames;import util.trace.Tracer;import bus.uigen.uiFrame;
import bus.uigen.attributes.AttributeNames;import bus.uigen.trace.NullLocationException;
public class GenericPointToPoint extends GenericXYShapeToShape implements ConcretePoint {
	public GenericPointToPoint (Object theTargetObject, uiFrame theFrame) {		if (theTargetObject == null) {//			Tracer.error("null location object, assuming coordinates of 0, 0");			init(theTargetObject, theFrame );			throw new NullLocationException("Assuming coordinates of 0, 0");		}		init(theTargetObject, theFrame );
	}
		public GenericPointToPoint () {
	}
  
	public static String POINT = "Point";	public String programmingPatternKeyword() {		return  super.programmingPatternKeyword() + AttributeNames.KEYWORD_SEPARATOR + POINT;	}	public String typeKeyword() {		return super.typeKeyword() + AttributeNames.KEYWORD_SEPARATOR + POINT ;	}
	public String getPatternName() {		return StructurePatternNames.POINT_PATTERN;			}
}