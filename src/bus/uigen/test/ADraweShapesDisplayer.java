package bus.uigen.test;

import shapes.LineModel;
import shapes.OvalModel;
import util.models.AListenableString;
import bus.uigen.OEFrame;
import bus.uigen.ObjectEditor;

public class ADraweShapesDisplayer {
	public static void main (String[] args) {
		try {
		Object[] shapes = {new LineModel(0, 0, 100, 100), new OvalModel(0, 0, 100, 100), new AListenableString("Hello worls")};
		
		ObjectEditor.edit(shapes);
		} catch (Exception e) {
			
		}
	}

}
