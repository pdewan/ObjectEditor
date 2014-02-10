package bus.uigen.sadapters;import util.annotations.StructurePatternNames;import bus.uigen.uiFrame;
import bus.uigen.attributes.AttributeNames;
public class GenericLineToLine extends GenericBoundedShapeToBoundedShape 
											implements ConcreteLine {
	public GenericLineToLine (Object theTargetObject, uiFrame theFrame) {		init(theTargetObject, theFrame );
	}
		public GenericLineToLine () {
	}	 public static String LINE = "Line";	public String programmingPatternKeyword() {		return  super.programmingPatternKeyword() + AttributeNames.KEYWORD_SEPARATOR + LINE;	}
	public String typeKeyword() {		return super.typeKeyword() + AttributeNames.KEYWORD_SEPARATOR + LINE ;	}	public String getPatternName() {		return StructurePatternNames.LINE_PATTERN;			}
	
}