package bus.uigen.test;

import java.util.ArrayList;
import java.util.List;

import bus.uigen.ObjectEditor;

public class ACompositeExampleWithBackLink extends ACompositeExample {
	public Object getThis() {
		return this;
	}
	
	public static void main (String[] args) {
		List list = new ArrayList();
		ACompositeExampleWithBackLink compositeExample = new ACompositeExampleWithBackLink();
		list.add(compositeExample);
		ObjectEditor.edit(list);
//		ObjectEditor.edit(new AFooBar(), frame.getContentPane());
//		frame.setSize(100, 200);
//		frame.setVisible(true);
	}

}
