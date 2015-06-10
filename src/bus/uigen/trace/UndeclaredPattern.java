package bus.uigen.trace;

import util.annotations.WebDocuments;
import util.trace.TraceableWarning;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;
@WebDocuments({"Lectures/DocumentsAnnotations.pdf"})
public class UndeclaredPattern extends ClassPatternWarning {

	public UndeclaredPattern(String aMessage, String aPattern, ClassProxy aTarget, Object aFinder) {
		super(aMessage, aPattern, aTarget, aFinder);
	}

	

	public static UndeclaredPattern newCase(String aPattern, ClassProxy aTarget, Object aFinder) {
		String aMessage = "Assuming implicit pattern: " + aPattern + "\n"

							+ "  If this pattern is correct, use annotation @StructurePattern(\""
											+ aPattern
											+ "\") for class "
											+ aTarget.getName();
		UndeclaredPattern retVal = new UndeclaredPattern(aMessage, aPattern, aTarget, aFinder);
		retVal.announce();
		return retVal;
	}

}
