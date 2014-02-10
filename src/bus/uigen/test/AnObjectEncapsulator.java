package bus.uigen.test;

import bus.uigen.ObjectEditor;

public class AnObjectEncapsulator {
	String foo = "foo";
	public String getFoo() {
		return foo;
	}
	
	public static void main (String[] args) {
		ObjectEditor.edit(new AnObjectEncapsulator());
	}

}
