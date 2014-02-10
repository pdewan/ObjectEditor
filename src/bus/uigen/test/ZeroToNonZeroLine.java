package bus.uigen.test;

import shapes.FlexibleShape;
import util.models.AListenableVector;
import util.models.ListenableVector;
import bus.uigen.ObjectEditor;
import bus.uigen.shapes.ALineModel;

public class ZeroToNonZeroLine {
	public static void main (String[] args) {
		FlexibleShape line = new ALineModel(100, 100, 0, 0);
		ObjectEditor.edit(line);
		line.setHeight(100);
		ListenableVector list = new AListenableVector();
		FlexibleShape anotherLine = new ALineModel(100, 100, 0, 0);
		list.add(anotherLine);
		ListenableVector nestedList = new AListenableVector();
		nestedList.addAll(list);
		ObjectEditor.edit(nestedList);
		anotherLine.setHeight(100);

	}

}
