package bus.uigen.test;

import java.lang.reflect.Method;

import bus.uigen.ObjectEditor;
import bus.uigen.controller.MethodParameters;
import util.annotations.StructurePattern;
import util.annotations.StructurePatternNames;
@StructurePattern(StructurePatternNames.BEAN_PATTERN)
public class AFunctionTester {
	
	public int doubleNumber (int arg) {
		return arg*2;
	}
	int number = 1;
	public int getNumber() {
		return number;
	}
	public int getNumberSquare() {
		return number*number;
	}
	public void setNumber(int newVal) {
		number = newVal;
	}
	
	public static void main (String[] args) {
		ObjectEditor.confirmSelectedMethodParameters(false);
		try {
			Class[] doubleNumberArgTypes = {Integer.TYPE};
			Object[] doubleNumberDefaultArgs = {3};
			String[] doubleNumberArgNames = {"numberToBeDoubled"};

			Method aDoubleNumberMethod = AFunctionTester.class.getMethod("doubleNumber", doubleNumberArgTypes);
			ObjectEditor.registerDefaultParameterValues(aDoubleNumberMethod, doubleNumberDefaultArgs);
			ObjectEditor.registerParameterNames(aDoubleNumberMethod, doubleNumberArgNames);

		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectEditor.edit(new AFunctionTester());
	}

}
