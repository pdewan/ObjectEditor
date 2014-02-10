package bus.uigen.test;

import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.annotations.IndirectlyVisible;
import bus.uigen.ObjectEditor;
import bus.uigen.attributes.AttributeNames;

public class ADependencyExample  extends ACompositeExample{
//	String string = "demo string";
	Map<TextAttribute, Object> textAttributes = new HashMap();
	List<Map<TextAttribute, Object>> textAttributesList = new ArrayList();
	List<String> strings = new ArrayList(0);
	
	public ADependencyExample() {
		strings.add("string 1");
		strings.add("string 2");
		textAttributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		textAttributes.put(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
		textAttributesList.add(new HashMap(textAttributes));
		textAttributesList.add(new HashMap(textAttributes));
//		textAttributes.put(TextAttribute.JUSTIFICATION, TextAttribute.JUSTIFICATION_FULL);		
	}
	public List<String> getStrings() {
		return strings;
	}
	public void setStrings(List<String> strings) {
		this.strings = strings;
	}
	
	@IndirectlyVisible(true)
	public Map<TextAttribute, Object> getTextAttributes() {
		return textAttributes;
	}
	
	public void setAString(String string) {
		this.string = string;
	}
//	@PreferredWidgetClass(JLabel.class)
	public String getAString() {
		return string;
	}

	public void setTextAttributes(Map<TextAttribute, Object> someTextAttributes) {
		this.textAttributes = someTextAttributes;
	}
	@IndirectlyVisible(true)
	public List<Map<TextAttribute, Object>> getTextAttributesList() {
		return textAttributesList;
	}
	public void setTextAttributesList(
			List<Map<TextAttribute, Object>> textAttributesList) {
		this.textAttributesList = textAttributesList;
	}
	
	public static void main (String[] args) {
		ADependencyExample example = new ADependencyExample();
		ObjectEditor.setPropertyAttribute(ADependencyExample.class, "string", AttributeNames.TEXT_ATTRIBUTES, "=TextAttributes");
		ObjectEditor.setPropertyAttribute(ADependencyExample.class, "strings.element", AttributeNames.TEXT_ATTRIBUTES, "=^.TextAttributesList.%ComponentName");

		ObjectEditor.edit(example);
		
	}
	

}
