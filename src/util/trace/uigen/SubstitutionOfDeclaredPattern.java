package util.trace.uigen;

import util.trace.TraceableWarning;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;

public class SubstitutionOfDeclaredPattern extends ClassPatternWarning {
	String implicitPattern;
	public SubstitutionOfDeclaredPattern(String aMessage, String aPattern, String anImplicitPattern, ClassProxy aTarget, Object aFinder) {
		super(aMessage, aPattern, aTarget, aFinder);
	}
	public String getImplicitPattern() {
		return implicitPattern;
	}
	

	public static SubstitutionOfDeclaredPattern newCase(String aPattern, String anImplicitPattern, ClassProxy aTarget, Object aFinder) {
		String aMessage = "Assuming implicit pattern: " + anImplicitPattern + " instead of: "  + aPattern;
		SubstitutionOfDeclaredPattern retVal = new SubstitutionOfDeclaredPattern(aMessage, aPattern, anImplicitPattern, aTarget, aFinder);
		retVal.announce();
		return retVal;
	}

}
