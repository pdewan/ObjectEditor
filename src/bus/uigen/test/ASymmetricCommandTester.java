package bus.uigen.test;

import bus.uigen.introspect.IntrospectUtility;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.reflect.RemoteSelector;
import bus.uigen.undo.Command;
import bus.uigen.undo.CommandCreator;
import bus.uigen.undo.CommandListener;

public class ASymmetricCommandTester {
	public void next() {
		System.out.println("Next");
	}
	public void previous() {
		System.out.println("Prev");

	}
	
	public static void main (String args[]) {
		ASymmetricCommandTester obj = new ASymmetricCommandTester();
		ClassProxy objClass = RemoteSelector.getClass(obj);
		MethodProxy method = IntrospectUtility.getMethod(objClass, "Next",  objClass.voidType(), new ClassProxy[] {});
		Command inverse = CommandCreator.createCommandBasic (null, method,
				 obj,
				 new Object[] {});
		}
	

}
