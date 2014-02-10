package bus.uigen;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.MenuItem;
public class ObjectEditorRunnable implements Runnable {
	uiFrame parentFrame;
	ObjectEditor editor;
	public ObjectEditorRunnable (ObjectEditor theEditor) {
		editor = theEditor;
	}
	public void actionPerformed(ActionEvent e) {		//this.notify();		editor.notifyMe();
	}
	public void run() {
		/*
		uiFrame f = ObjectEditor.edit(o);
		MenuItem okButton = f.addDoneItem();		
		okButton.addActionListener(this);		*/
		/*		try {
			this.wait();
		} catch (Exception e) 		{			System.out.println(e);
		}		return f.getAdapter().getValue();
		editor.notify();
		*/
		
	}
	public synchronized void notifyMe() {
		try {			this.notify();		} catch (Exception e) {
			System.out.println(e);		}
	}
	
}