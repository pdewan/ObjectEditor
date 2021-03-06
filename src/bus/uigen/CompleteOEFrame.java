package bus.uigen;

import java.awt.event.KeyListener;
import java.util.Hashtable;

import bus.uigen.undo.HistoryUndoerListener;
import bus.uigen.widgets.VirtualContainer;
import bus.uigen.widgets.events.VirtualActionListener;

public interface CompleteOEFrame extends OEFrame, VirtualActionListener,  Runnable, HistoryUndoerListener, 
KeyListener{

	void setSelfAttributes(Hashtable newVal);
//	public void addMenuObject(Object menuObject);
//	public void addMenuObject(Object menuObject);
//	public void showDrawPanel() ;
//	public void hideMainPanel();
//	public void showTreePanel();
//	public void setSize(int newWidth, int newHeight);
//	public void setTitle(String newVal);
//	public void setLocation(int newX, int newY);
//	public void setFullRefreshOnEachOperation(boolean newVal);
//	public boolean getFullRefreshOnEachOperation();

	VirtualContainer getContainer();

}
