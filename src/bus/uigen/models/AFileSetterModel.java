package bus.uigen.models;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import bus.uigen.OEFrame;
import bus.uigen.ObjectEditor;
import util.annotations.Column;
import util.annotations.ComponentHeight;
import util.annotations.ComponentWidth;
import util.annotations.Row;
import util.annotations.StructurePattern;
import util.annotations.StructurePatternNames;
import util.annotations.Visible;
import util.models.LabelBeanModel;
@StructurePattern(StructurePatternNames.BEAN_PATTERN)
public class AFileSetterModel extends ALabelSetterModel implements FileSetterModel {
	
	JFileChooser fileChooser = new JFileChooser();
	JFrame frame;
	int filterOption;
	public AFileSetterModel (int aFilterOption) {
		filterOption = aFilterOption;
//		frame = (JFrame) aFrame.getFrame().getPhysicalComponent();
	}
	@Visible(false)
	public void initFrame(JFrame aFrame) {
		frame = aFrame;
	}

	@Override
	@Column(2) // borderlayout, so this means east
//	@Row(1)
//	@ComponentWidth(80)
	@ComponentHeight(25)
	public void browse() {
//        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setFileSelectionMode(filterOption);

        int returnValue = fileChooser.showOpenDialog(frame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            setText( fileChooser.getSelectedFile().getAbsolutePath());
        }
		
	}
	
	public static void main (String[] args) {
		AFileSetterModel aFileSetterModel = new AFileSetterModel(JFileChooser.DIRECTORIES_ONLY);
		OEFrame oeFrame = ObjectEditor.edit(aFileSetterModel);
		aFileSetterModel.initFrame((JFrame) oeFrame.getFrame().getPhysicalComponent());
		oeFrame.setSize(600,  200);
	}

}
