package bus.uigen.sadapters;import util.annotations.StructurePatternNames;import bus.uigen.uiFrame;
import bus.uigen.attributes.AttributeNames;
public class GenericRectangleToRectangle extends GenericBoundedShapeToBoundedShape 
											implements ConcreteRectangle {
	public GenericRectangleToRectangle(Object theTargetObject, uiFrame theFrame) {		init(theTargetObject, theFrame );
	}
		public GenericRectangleToRectangle () {
	}	 public static String RECTANGLE = "Rectangle";		public String programmingPatternKeyword() {			return  super.programmingPatternKeyword() + AttributeNames.KEYWORD_SEPARATOR + RECTANGLE;		}
	
		public String typeKeyword() {			return super.typeKeyword() + AttributeNames.KEYWORD_SEPARATOR + RECTANGLE ;		}		public String getPatternName() {			return StructurePatternNames.RECTANGLE_PATTERN;				}
}