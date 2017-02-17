package bus.uigen.controller.models;

import javax.swing.JSplitPane;

import util.annotations.Explanation;
import util.annotations.Visible;

import bus.uigen.ObjectEditor;
import bus.uigen.uiFrame;
import bus.uigen.uiGenerator;
import bus.uigen.controller.Selectable;
import bus.uigen.controller.SelectionManager;
import bus.uigen.controller.menus.AMenuDescriptor;
import bus.uigen.controller.menus.MenuSetter;
import bus.uigen.introspect.AClassDescriptor;
import bus.uigen.introspect.ClassDescriptorCache;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.view.ForwardBackwardListener;
@util.annotations.StructurePattern(util.annotations.StructurePatternNames.NO_PATTERN)
public class ABasicNewEditorOperationsModel extends AnAbstractOperationsModel implements FrameModel/*, ForwardBackwardListener*/ {
//	uiFrame frame;
//	@Visible(false)
//	public void init (uiFrame theFrame, Object theObject) {
//		frame = theFrame;
//		//frame.getBrowser().addForwardBackwardListener(this);
//	}	
	//boolean myMode;
	@Explanation ("Edits the selected or top object in a  new window. Useful when the current window does not seem to show the correct state of an object. This state can be compared with the one in the new new window")
	public void newEditor() {
		//Selectable s = uiSelectionManager.getCurrentSelection();
		ObjectAdapter selection = getOperandAdapter();
		/*
		if (s != null) {
			selection = (uiObjectAdapter) s;
		} else
			selection = frame.getBrowser().getOriginalAdapter();
		*/
		uiGenerator.setTextMode (frame.getTextMode());
		//uiFrame editor = uiGenerator.generateUIFrame(selection.getObject(), (ObjectAdapter) selection);
		uiFrame editor = (uiFrame) ObjectEditor.edit(selection.getObject(),  AClassDescriptor.withAttributeRegisterer(), new MenuSetter(), new AMenuDescriptor(), (ObjectAdapter) selection, null, null );
		//uiFrame editor = uiGenerator.generateUIFrame(this, s.getObject(),
		//null);
		frame.initDerivedFrame(editor);
		editor.setVisible(true);
		
		
	}
//	public void newEditorWithoutCustomization() {
//		ClassDescriptorCache.clear();
//		AClassDescriptor.writeWithAttributeRegister(false);
//		//replaceWindow();
//		newEditor();
//		AClassDescriptor.writeWithAttributeRegister(true);
//	}
	ObjectAdapter getOperandAdapter() {
		return getOperandAdapter(frame);
	}
	protected static ObjectAdapter getOperandAdapter(uiFrame frame) {
		Selectable s = SelectionManager.getCurrentSelection();
		ObjectAdapter selection;
		if (s != null) {
			selection = (ObjectAdapter) s;
		} else
			selection = frame.getBrowser().getOriginalAdapter();
		return selection;
		
	}
//	@Explanation ("Displas the selected or top object in a new table editror, which uses the toolkit table widget to display the object")
//	public void newTableEditor() {
//		ObjectAdapter selection = getOperandAdapter();
//		uiFrame editor = ObjectEditor.tableEdit(selection.getObject());
//		frame.initDerivedFrame(editor);
//		
//	}
//	@Explanation ("Displays the entire logical structure of the root or selected object, even the graphical components, in a new main window")
//	public void newTextEditor() {
//		
//		ObjectAdapter selection = getOperandAdapter();
//		boolean prevMode = uiGenerator.textMode();
//		uiFrame editor = ObjectEditor.textEdit(selection.getObject());
////		uiGenerator.setTextMode (true);
////		uiFrame editor = ObjectEditor.edit(selection.getObject());
////		uiGenerator.setTextMode( prevMode);		
//		frame.initDerivedFrame(editor);
////		Selectable s = SelectionManager.getCurrentSelection();
////		ObjectAdapter selection;
////		
////		selection   = frame.getBrowser().getOriginalAdapter();
////		
////		boolean prevMode = uiGenerator.textMode();
////		uiGenerator.setTextMode (true);
////		uiFrame editor = uiGenerator.generateUIFrame(selection.getObject(), (ObjectAdapter)   selection);
////		uiGenerator.setTextMode( prevMode);
//	}
//	@Explanation ("Displays the children of the selected or top object in different tabs of a new window")
//
//	public void newTabEditor() {
//		ObjectAdapter selection = getOperandAdapter();
//		uiFrame editor = ObjectEditor.tabEdit(selection.getObject());
//		frame.initDerivedFrame(editor);
//		
//	}
//	@Explanation ("Displays the children of the selected or top object in a desktop window. Does not seem to work currently")
//	public void newDesktopEditor() {
//		ObjectAdapter selection = getOperandAdapter();
//		uiFrame editor = ObjectEditor.desktopEdit(selection.getObject());
//		frame.initDerivedFrame(editor);
//		
//	}	
//	
//	public void replaceWindow() {
//		ObjectAdapter selection = getOperandAdapter();
//		//frame.getBrowser().replaceFrame();
//		frame.getBrowser().replaceFrame(selection);
//	}
//	/*
//	public void newWindowRight() {
//		Selectable s = uiSelectionManager.getCurrentSelection();
//		newScrollPaneRight((uiObjectAdapter) s);
//	}
//	public void newWindowBottom() {
//		Selectable s = uiSelectionManager.getCurrentSelection();
//		newScrollPaneBottom((uiObjectAdapter) s);
//		
//	}
//	*/
//	/*
//	 void newScrollPaneRight(uiObjectAdapter a)   {
//		//showToolBar();
//		 Object obj= null;
//		if (a   == null) {
//			obj = uiFrame.RIGHT_WINDOW_MESSAGE;
//			//return;
//		} else
//			obj = a.getObject();
//		
//		//uiFrame editor = 
//		//uiGenerator.generateUIScrollPane(this,  a.getObject(),
//		uiGenerator.generateInNewBrowsableContainer(frame,  
//				obj,
//				//a.getObject(),
//			(uiObjectAdapter) a,    JSplitPane.HORIZONTAL_SPLIT);
//		//editor.setVisible(true);
//		frame.setVisible(true);
//	}
//	*/
//	/*
//	boolean forwardEnabled = false;
//	boolean backwardEnabled = false;
//	public boolean preForward() {
//		return forwardEnabled;
//	}
//	public boolean preBack() {
//		return backwardEnabled;
//	}
//	public void forward() {
//		frame.getBrowser().forwardAdapter();
//	}
//	public void back() {
//		frame.getBrowser().backAdapter();
//	}
//	public void forwardEnabled (boolean newVal) {
//		forwardEnabled = newVal;
//	}
//	public void backwardEnabled (boolean newVal) {
//		backwardEnabled = newVal;
//	}
//	*/
//	/*
//	void newScrollPaneBottom(uiObjectAdapter a) {
//		//showToolBar();
//		 Object obj= null;
//			if (a   == null) {
//				obj = uiFrame.BOTTOM_WINDOW_MESSAGE;
//				//return;
//			} else
//				obj = a.getObject();
//		
//		//if (a != null) {
//			//uiFrame editor = uiGenerator.generateUIFrame(s.getObject());
//			//uiFrame editor = 
//			//uiGenerator.generateUIScrollPane(this,  a.getObject(),
//			uiGenerator.generateInNewBrowsableContainer(frame,
//					obj,
//					//a.getObject(),
//				 (uiObjectAdapter) a,    JSplitPane.VERTICAL_SPLIT);
//			//editor.setVisible(true);
//			frame.setVisible(true);
//		//}
//	}
//	
//	*/

}
