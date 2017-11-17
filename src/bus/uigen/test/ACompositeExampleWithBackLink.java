package bus.uigen.test;

import java.util.ArrayList;
import java.util.List;

import bus.uigen.ObjectEditor;

public class ACompositeExampleWithBackLink 
//extends ACompositeExample 
{
	ALabelAndString child = new ALabelAndString();
	public Object getThis() {
		return this;
	}
	public ALabelAndString getChild() {
		return child;
	}
	public Object getSecondLink() {
		return getChild().getLabelModel();
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
