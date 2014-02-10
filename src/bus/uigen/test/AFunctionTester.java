package bus.uigen.test;

import bus.uigen.ObjectEditor;

public class AFunctionTester {
	
	public double aFunction (double arg) {
		return arg*2;
	}
	
	public static void main (String[] args) {
		ObjectEditor.edit(new AFunctionTester());
	}

}
