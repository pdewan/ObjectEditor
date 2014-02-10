package bus.uigen.test;

import java.awt.Color;

import util.models.AListenableVector;
import util.models.ListenableVector;
import bus.uigen.ObjectEditor;
import bus.uigen.attributes.AttributeNames;

public class TableTester {

public static void main (String[] args) {
	ObjectEditor.setPropertyAttribute(ACompositeExample.class, "String", AttributeNames.COMPONENT_BACKGROUND, Color.blue);
	ListenableVector<ACompositeExample> list = new AListenableVector<>();
	list.add(new ACompositeExample("aaa", 1));
	list.add(new ACompositeExample("bbb", 2));
	ObjectEditor.tableEdit(list);
	

}
 

}
