package bus.uigen;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.WindowListener;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JComponent;

import util.undo.ExecutedCommand;
import bus.uigen.controller.models.InteractiveMethodInvoker;
import bus.uigen.controller.models.MethodInvocationFrameCreationListener;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.widgets.VirtualComponent;
import bus.uigen.widgets.VirtualContainer;
import bus.uigen.widgets.VirtualDimension;
import bus.uigen.widgets.VirtualFrame;
import bus.uigen.widgets.VirtualPoint;
import bus.uigen.widgets.events.VirtualActionEvent;

public class ADummyCompleteOEFrame implements CompleteOEFrame {
	boolean isDummy = false;

	public void setIsDummy(boolean newVal) {
		isDummy = newVal;
	}

	public boolean isDummy() {
		return isDummy;
	}

	public ADummyCompleteOEFrame() {
//		setIsDummy(true);
		// TODO Auto-generated constructor stub
	}

	@Override
	public VirtualContainer getDrawPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Component getDrawComponent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VirtualComponent getDrawVirtualComponent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void showToolBar() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hideToolBar() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showDrawPanel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hideMainPanel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showTreePanel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSize(int newWidth, int newHeight) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTitle(String newVal) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLocation(int newX, int newY) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFullRefreshOnEachOperation(boolean newVal) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getFullRefreshOnEachOperation() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void select(Object object, int index) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void select(Object object, String property) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDemoFont() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> getSourceClassNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ClassProxy> getSourceClasses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ObjectAdapter getObjectAdapterFromPath(String path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VirtualFrame getFrame() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getGlassPaneModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JComponent setGlassPaneModel(Object aGlassPaneModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JComponent setTelePointerModel(Object aGlassPaneModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VirtualDimension getSize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VirtualPoint getLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addMethodInvocationFrameCreationListener(
			MethodInvocationFrameCreationListener aListener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyMethodInvocationFrameCreated(OEFrame aParentFrame,
			OEFrame anInvocationFrame,
			InteractiveMethodInvoker anInteractiveMethodInvoker) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ObjectAdapter getObjectAdapter(Object anObject) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setGraphicsWindowLocked(boolean newVal) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isSuppressPropertyNotifications() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setSuppressPropertyNotifications(
			boolean suppressPropertyNotifications) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAutoExitEnabled(boolean newVal) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getAutoExitEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addWindowListener(WindowListener newVal) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addMenuObject(Object menuObject) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void focus(Object object, String property) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(VirtualActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void undoHistoryEmpty(boolean isEmpty) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void redoHistoryEmpty(boolean isEmpty) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void commandUndone(ExecutedCommand c, int index) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void commandRedone(ExecutedCommand c, int index) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void commandExecuted(ExecutedCommand c, int index) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setVisible(boolean aNewVal) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isVisible() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSelfAttributes(Hashtable newVal) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public VirtualContainer getContainer() {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public void setSelfAttributes(Hashtable newVal) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public VirtualContainer getContainer() {
//		// TODO Auto-generated method stub
//		return null;
//	}

//	@Override
//	public void setSelfAttributes(Hashtable newVal) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public VirtualContainer getContainer() {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
