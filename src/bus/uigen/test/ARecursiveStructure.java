package bus.uigen.test;

import bus.uigen.ObjectEditor;
import util.models.AListenableVector;

public class ARecursiveStructure extends AListenableVector {
	public ARecursiveStructure() {
		add(new ACompositeExampleWithBackLink());

//		add(new ACompositeExample());
//		add (this);
	}
	
	public static void main (String[] args) {
		ARecursiveStructure recursive = new ARecursiveStructure();
		ObjectEditor.edit(recursive);
	}
	
	public String toString() {
		return "Recursive Structure";
	}

}
