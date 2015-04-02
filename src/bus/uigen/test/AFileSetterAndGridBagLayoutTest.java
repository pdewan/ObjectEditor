package bus.uigen.test;

import javax.swing.JFileChooser;

import bus.uigen.OEFrame;
import bus.uigen.ObjectEditor;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.models.AFileSetterModel;
import bus.uigen.models.FileSetterModel;

import javax.swing.JFrame;

import util.annotations.LayoutName;
@LayoutName(AttributeNames.GRID_BAG_LAYOUT)
public class AFileSetterAndGridBagLayoutTest {
     FileSetterModel fileModel = new AFileSetterModel(JFileChooser.FILES_AND_DIRECTORIES);
	
	public FileSetterModel getFileSetter() {
		return fileModel;		
	}
	
	public static void main (String[] args) {
		AFileSetterAndGridBagLayoutTest fileSetter = new AFileSetterAndGridBagLayoutTest();
		OEFrame frame = ObjectEditor.edit(fileSetter);
		fileSetter.getFileSetter().initFrame((JFrame) (frame.getFrame().getPhysicalComponent())); 
		
	}

}
