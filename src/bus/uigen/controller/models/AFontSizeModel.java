package bus.uigen.controller.models;

import java.util.Vector;

import javax.swing.JSplitPane;

import util.annotations.Visible;

import bus.uigen.ObjectEditor;
import bus.uigen.uiFrame;
import bus.uigen.uiFrameList;
import bus.uigen.uiGenerator;
import bus.uigen.controller.Selectable;
import bus.uigen.controller.SelectionManager;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.view.ForwardBackwardListener;
@util.annotations.StructurePattern(util.annotations.StructurePatternNames.NO_PATTERN)
public class AFontSizeModel extends ADemoFontOperationsModel implements FrameModel {
//	static Integer fontSize;
//	uiFrame frame;
	public static int MIN_FONT = 8;
	public static int MAX_FONT = 32;
//	public static int DEMO_FONT = 18;
	String[] fontSizes = new String[1 + (32-8)/2];
	@Override
	@Visible(false)
	public void init (uiFrame theFrame, Object theObject, ObjectAdapter theObjectAdapter) {
		super.init(theFrame, theObject, theObjectAdapter);
		int n = 0;
		for (int i = 0; i < fontSizes.length; i++) {
			String fontString = "" + (MIN_FONT + i*2);
			fontSizes[i] = fontString;			
		}
	}
//	@util.annotations.Explanation ("Makes font size bigh enough to be easily seen in a demo projected to a medium-sized room.")
//	public void demoFontSize() {
//		fontSize = DEMO_FONT;
//		setFontSizesOfAllFrames();
//	}
	public String[] getDynamicCommands() {
		return fontSizes;
	}
	public void invokeDynamicCommand (String theFontSizeString) {
		try {
			fontSize = Integer.parseInt(theFontSizeString);
			setFontSizesOfAllFrames();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
//	static void setFontSizesOfAllFrames() {
//		Vector<uiFrame> list = uiFrameList.getList();
//		for (int i=0; i < list.size(); i++) {
//			list.elementAt(i).setFontSize(fontSize);
//		}
//	}
	public static Integer getFontSize() {
		return fontSize;
	}

}
