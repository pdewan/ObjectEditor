package bus.uigen.test;

import java.awt.Color;

import util.annotations.Column;
import util.annotations.Row;
import util.misc.ThreadSupport;
import bus.uigen.OEFrame;
import bus.uigen.ObjectEditor;
import bus.uigen.attributes.AttributeNames;

public class ADynamicMethodAttributeChanger extends ARowColumnPositioner{
	
	@Row(1)
	@Column(1)
	public void incInt() {
		super.incInt();
		ObjectEditor.setMethodAttribute(ADynamicMethodAttributeChanger.class, "incInt", AttributeNames.LABEL, "Increment: " + getInt());
		ObjectEditor.setMethodAttribute(ADynamicMethodAttributeChanger.class, "incInt", AttributeNames.EXPLANATION, "Increment: " + getInt());
//		ObjectEditor.setMethodAttribute(ADynamicMethodAttributeChanger.class, "incInt", AttributeNames.COMPONENT_BACKGROUND, new Color(getInt() *30, 255, 255));
		ObjectEditor.setMethodAttribute(ADynamicMethodAttributeChanger.class, "incInt", AttributeNames.COMPONENT_FOREGROUND, new Color(getInt() *50));

	}
	
	
	public static void main (String[] args) {
		OEFrame frame = ObjectEditor.edit(new ADynamicMethodAttributeChanger());
		frame.setSize(550, 150);
	}


}
