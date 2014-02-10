package bus.uigen.controller;
import java.util.Vector;
import java.util.Enumeration;
import bus.uigen.oadapters.ObjectAdapter;
public class AdapterHistory {
	Vector history = new Vector();
	int curPos = 0;
	public boolean contains(ObjectAdapter newAdapter) {
		return history.contains(newAdapter);
	}
	public void addAdapter(ObjectAdapter newAdapter) {
		//System.out.println("adding " + newAdapter);
		history.insertElementAt(newAdapter, curPos);
		curPos++;
	}
	//public int getCurrentPosition
	public ObjectAdapter nextAdapter() {
		if (curPos  < history.size())
			return (ObjectAdapter) history.elementAt(curPos);
	    else return null;
	}
	public ObjectAdapter toNextAdapter() {
		ObjectAdapter retVal = nextAdapter();
		if (retVal != null)
			curPos++;
		return retVal;
	}
	public Vector prevAdapters() {
		//System.out.println(curPos);
		Vector retVal = new Vector();
		for (int i = curPos -2; i >= 0; i--)
			retVal.addElement(history.elementAt(i));
		return retVal;
	}
	public Vector nextAdapters() {
		//System.out.println(curPos);
		Vector retVal = new Vector();
		for (int i = curPos; i < history.size(); i++)
			retVal.addElement(history.elementAt(i));
		return retVal;
	}
	
	public ObjectAdapter prevAdapter() {
		//System.out.println(curPos);
		if (curPos > 1)
			return (ObjectAdapter) history.elementAt(curPos - 2);
	    else return null;
	}
	public ObjectAdapter toPrevAdapter() {
		ObjectAdapter retVal = prevAdapter();
		if (retVal != null)
			curPos--;
		return retVal;
		
	}
	public Enumeration elements() {
		return history.elements();
	}
	public int size() {
		return history.size();
	}
	public int indexOf(ObjectAdapter a) {
		return history.indexOf(a);
	}
	public void insertElementAt(ObjectAdapter a, int i) {
		 history.insertElementAt(a, i);
	}
	public void removeElementAt(int i) {
		 history.removeElementAt(i);
	}
}