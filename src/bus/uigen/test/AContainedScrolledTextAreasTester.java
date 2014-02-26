package bus.uigen.test;

import javax.swing.JTextArea;

import util.annotations.ComponentHeight;
import util.annotations.ComponentWidth;
import util.annotations.PreferredWidgetClass;
import bus.uigen.ObjectEditor;
import bus.uigen.attributes.AttributeNames;

public class AContainedScrolledTextAreasTester {
	String string1 = "Line1\nLine2\nLine3\nLine4\nLine5\n";
	String string2 = "Line1\nLine2\nLine3\nLine4\nLine5\n";
	@PreferredWidgetClass(JTextArea.class)
	@ComponentWidth(200)
	@ComponentHeight(20)
	public String getString1() {
		return string1;
	}
	@PreferredWidgetClass(JTextArea.class)
	@ComponentWidth(200)
	@ComponentHeight(20)
	public String getString2() {
		return string2;
	}
	
	public static void main (String[] args) {
		ObjectEditor.setPropertyAttribute(AContainedScrolledTextAreasTester.class, 
				"string1", AttributeNames.SCROLLED, true);
		ObjectEditor.setPropertyAttribute(AContainedScrolledTextAreasTester.class, 
				"string2", AttributeNames.SCROLLED, true);
		ObjectEditor.tabEdit(new AContainedScrolledTextAreasTester());
	}


}
