package bus.uigen.controller.models;

import java.util.Vector;

import javax.swing.JSplitPane;

import slgv.SLGView;
import util.annotations.StructurePattern;
import util.annotations.Visible;

import bus.uigen.ObjectEditor;
import bus.uigen.uiFrame;
import bus.uigen.uiFrameList;
import bus.uigen.uiGenerator;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.controller.Selectable;
import bus.uigen.controller.SelectionManager;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.view.ForwardBackwardListener;
@StructurePattern(util.annotations.StructurePatternNames.NO_PATTERN)
public class ADemoFontOperationsModel implements FrameModel {
	static Integer fontSize;
	uiFrame frame;
	
	public static Integer DEFAULT_FONT_SIZE = (Integer) AttributeNames.getDefault(AttributeNames.FONT_SIZE);

	public static int DEMO_FONT = 16;
	public static int NORMAL_FONT = 12;
	static int oldFontSize;
	boolean isDemoFont = false;
	@Visible(false)
	public void init (uiFrame theFrame, Object theObject, ObjectAdapter theObjectAdapter) {
		frame = theFrame;
	}
	@util.annotations.Explanation ("Makes font size big enough to be easily seen in a demo projected to a medium-sized room.")
	public void demoFontSize() {
		if (isDemoFont) {
			fontSize = NORMAL_FONT;
			isDemoFont = false;
			AttributeNames.setDefault(AttributeNames.FONT_SIZE, DEFAULT_FONT_SIZE);
			SLGView.setFontSize(DEFAULT_FONT_SIZE);

		}
		else {
			fontSize = DEMO_FONT;
			isDemoFont = true;
			AttributeNames.setDefault(AttributeNames.FONT_SIZE, fontSize);
			SLGView.setFontSize(fontSize);
//			frame.setFontSize(fontSize);

		}
		if (frame != null)
			frame.setFontSize(fontSize);

//		setFontSizesOfAllFrames();
	}

	
	static void setFontSizesOfAllFrames() {
		
		Vector<uiFrame> list = uiFrameList.getList();
		for (int i=0; i < list.size(); i++) {
			list.elementAt(i).setFontSize(fontSize);
		}
	}


}
