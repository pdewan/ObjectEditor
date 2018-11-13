package bus.uigen.test;

import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

import shapes.TextShape;

import bus.uigen.ObjectEditor;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.shapes.AStringModel;

public class ATextAttributesTester {
	public static void main (String[] aString) {
		CompositeExample example = new ACompositeExample();
		Map<TextAttribute, Object> attributes = new HashMap();	
		attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		ObjectEditor.setPropertyAttribute(ACompositeExample.class, "String", AttributeNames.TEXT_ATTRIBUTES, attributes);
		ObjectEditor.edit(example);
		TextShape stringShape = new AStringModel("hello", 50, 100);
		ObjectEditor.setAttribute(AStringModel.class, AttributeNames.TEXT_ATTRIBUTES, attributes);
		ObjectEditor.edit(stringShape);
	}

}
