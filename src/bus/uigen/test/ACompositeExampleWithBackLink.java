package bus.uigen.test;

import java.util.ArrayList;
import java.util.List;

import bus.uigen.ObjectEditor;

public class ACompositeExampleWithBackLink 
//extends ACompositeExample 
{
	String string = "hello";
	String string2 = "hello";
	String string3 = new String("hello");
	int int1 = 2;
	Integer int2 = new Integer(2);
	
	ALabelAndString child = new ALabelAndString();
//	public Object getThis() {
//		return this;
//	}
//	public String getString() {
//		return string;
//	}
//	public String getString2() {
//		return string2;
//	}
//	public String getString3() {
//		return string3;
//	}
//	public int getInt1() {
//		return int1;
//	}
	public Integer getInt2() {
		return int2;	
	}
	public Integer getInt3() {
		return int2;	
	}
//	public ALabelAndString getChild() {
//		return child;
//	}
//	public Object getSecondLink() {
//		return getChild().getLabelModel();
//	}
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
