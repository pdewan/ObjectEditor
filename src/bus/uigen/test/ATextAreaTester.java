package bus.uigen.test;

import java.util.ArrayList;
import java.util.List;

import bus.uigen.ObjectEditor;
import bus.uigen.widgets.VirtualTextArea;

public class ATextAreaTester {
	public static void main (String[] args) {
		List<String> list = new ArrayList();
		list.add("hello");
		list.add("bye");
		ObjectEditor.edit(list, VirtualTextArea.class);
	}

}
