package bus.uigen.undo;
import java.util.Vector;
import java.lang.reflect.Method;

import util.undo.ExecutedCommand;
public class ExecutedCommandList implements ExecutedCommand {
	Vector commands = new Vector();
	public void addElement(ExecutedCommand c) {
		commands.addElement(c);
	}
	public int size() {
		return commands.size();
	}
	public ExecutedCommand elementAt(int i) {
		return (ExecutedCommand) commands.elementAt(i);
	}
	
	public  void  undo() {
		//for (int i = 0; i < commands.size(); i++) {
		for (int i = commands.size() - 1; i >= 0 ; i--) {
			ExecutedCommand c = (ExecutedCommand) commands.elementAt(i);
			c.undo();
		}
	}
	
	public void redo()    {
		for (int i = 0; i < commands.size(); i++) {
			ExecutedCommand c = (ExecutedCommand) commands.elementAt(i);
			c.redo();
		}
        
    }  
	
	
}
