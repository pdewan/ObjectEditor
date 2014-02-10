package bus.uigen.test.vehicle;

import util.annotations.StructurePattern;
import util.annotations.StructurePatternNames;
import bus.uigen.ObjectEditor;
@StructurePattern(StructurePatternNames.OVAL_PATTERN)
public class AnOval extends AShape {
	public static void main (String[] args) {
		Shape oval =new AnOval();
		oval.setX(100);
		oval.setY(100);
		oval.setHeight(100);
		oval.setWidth(100);
		ObjectEditor.edit(oval);
	}

}

