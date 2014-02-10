package bus.uigen.sadapters;import util.annotations.StructurePatternNames;import bus.uigen.uiFrame;
import bus.uigen.attributes.AttributeNames;
public class GenericOvalToOval extends GenericBoundedShapeToBoundedShape 
											implements ConcreteOval {
	public GenericOvalToOval(Object theTargetObject, uiFrame theFrame) {		init(theTargetObject, theFrame );
	}
		public GenericOvalToOval () {
	}	 public static String OVAL = "Oval";		public String programmingPatternKeyword() {			return  super.programmingPatternKeyword() + AttributeNames.KEYWORD_SEPARATOR + OVAL;		}
		public String typeKeyword() {			return super.typeKeyword() + AttributeNames.KEYWORD_SEPARATOR + OVAL ;		}
		public String getPatternName() {			return StructurePatternNames.OVAL_PATTERN;				}
}