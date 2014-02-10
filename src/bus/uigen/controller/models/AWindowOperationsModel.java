package bus.uigen.controller.models;

import javax.swing.JSplitPane;

import util.annotations.Explanation;
import util.annotations.Visible;

import bus.uigen.ObjectEditor;
import bus.uigen.uiFrame;
import bus.uigen.uiGenerator;
import bus.uigen.controller.Selectable;
import bus.uigen.controller.SelectionManager;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.view.ForwardBackwardListener;
@util.annotations.StructurePattern(util.annotations.StructurePatternNames.NO_PATTERN)
public class AWindowOperationsModel extends ABasicWindowOperationsModel implements FrameModel, ForwardBackwardListener {
//	uiFrame frame;
//	@Visible(false)
//	public void init (uiFrame theFrame, Object theObject) {
//		frame = theFrame;
//		//frame.getBrowser().addForwardBackwardListener(this);
//	}
//	@Explanation ("Toggle command: Determines if the tree window, which shows the entire (textual and graphical) logical object structure, is visible")
//	public void tree() {
//		frame.getTopViewManager().treePanel();
//	}
	@Explanation ("Toggle command: Determines if the graphical window, which shows the graphical object structure, is visible")
	public void drawing() {
		frame.getTopViewManager().drawPanel(); 
	}
	@Explanation ("Toggle command: Determines if the main window, which shows the graphical object structure, is visible")
	public void mainPanel() {
		frame.getTopViewManager().mainPanel(); 		
	}
	@Explanation ("Toggle command: Determines if the tool bar, which has buttons for some of the object methods, is visible")
	public void toolbar() {
		frame.getTopViewManager().toolBar();  		
	}
	@Explanation ("Toggle command: Determines if the secondary panel, which can be used to browse individual elements in the main panel, is visible")
	public void secondaryPanel() {
		frame.getTopViewManager().secondaryPanel(); 
	}
//	@Explanation ("Displays, in a separate window, the complete widget structure, including scrollpanes, behind the textual displat in the main panel ")
//	public void displayCompleteWidgetStructure() {
//		ObjectEditor.treeEdit(frame.getComponentTree());
//	}
	@Explanation ("Displays, in a separate window, the  widget structure behind the textual display in the main panel, filtering out certain kinds of widgets such as scrollpanes ")
	public void displayWidgetTree() {
		ObjectEditor.treeEdit(frame.getTopAdapterComponentTree());
	}
//	public void windows() {
//		frame.getTopViewManager().windowHistoryPanel();
//	}
	@Explanation ("Displays, a new window on the right, in which elements of a previous window such as the secodary panel can be browsed ")
	public void newWindowRight() {
		Selectable s = SelectionManager.getCurrentSelection();
		newScrollPaneRight((ObjectAdapter) s);
	}
	@Explanation ("Displays, a new window on the bottom, in which elements of a previous window such as the secodary panel can be browsed ")
	public void newWindowBottom() {
		Selectable s = SelectionManager.getCurrentSelection();
		newScrollPaneBottom((ObjectAdapter) s);
		
	}
	void newScrollPaneRight(ObjectAdapter a)   {
		//showToolBar();
		 Object obj= null;
		if (a   == null) {
			obj = uiFrame.RIGHT_WINDOW_MESSAGE;
			//return;
		} else
			obj = a.getObject();
		
		//uiFrame editor = 
		//uiGenerator.generateUIScrollPane(this,  a.getObject(),
		uiGenerator.generateInNewBrowsableContainer(frame,  
				obj,
				//a.getObject(),
			(ObjectAdapter) a,    JSplitPane.HORIZONTAL_SPLIT);
		//editor.setVisible(true);
		frame.setVisible(true);
	}
	void newScrollPaneBottom(ObjectAdapter a) {
		//showToolBar();
		 Object obj= null;
			if (a   == null) {
				obj = uiFrame.BOTTOM_WINDOW_MESSAGE;
				//return;
			} else
				obj = a.getObject();
		
		//if (a != null) {
			//uiFrame editor = uiGenerator.generateUIFrame(s.getObject());
			//uiFrame editor = 
			//uiGenerator.generateUIScrollPane(this,  a.getObject(),
			uiGenerator.generateInNewBrowsableContainer(frame,
					obj,
					//a.getObject(),
				 (ObjectAdapter) a,    JSplitPane.VERTICAL_SPLIT);
			//editor.setVisible(true);
			frame.setVisible(true);
		//}
	}
	
	boolean forwardEnabled = false;
	boolean backwardEnabled = false;
	public boolean preForward() {
		return forwardEnabled;
	}
	public boolean preBack() {
		return backwardEnabled;
	}
	@Explanation ("Traditional navigation command, undoes the effect of the back command")
	public void forward() {
		frame.getBrowser().forwardAdapter();
	}
	@Explanation ("Traditonal navigation command, replaces the current object with the previous one")
	public void back() {
		frame.getBrowser().backAdapter();
	}
	@Visible(false)
	public void forwardEnabled (boolean newVal) {
		forwardEnabled = newVal;
	}
	@Visible(false)
	public void backwardEnabled (boolean newVal) {
		backwardEnabled = newVal;
	}
	
}
