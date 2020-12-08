package bus.uigen;
import java.beans.*;
import java.util.Vector;
import bus.uigen.oadapters.ObjectAdapter;
public class UnivPropertyChange implements java.io.Serializable, UIGenLoggableEvent {
	int adapterID;
	String path;
	PropertyChangeEvent pce;

	public UnivPropertyChange(ObjectAdapter uoa, PropertyChangeEvent pce) {
		adapterID = ObjectRegistry.indexOfAdapter(uoa);
		// System.out.println(" ("+adapterID+") --> ("+ObjectRegistry.indexOfAdapter(uoa.pathToObjectAdapter(uoa.getVectorPath()))+")");
		this.pce = pce;
	}
	
	public UnivPropertyChange(String uoaPath, PropertyChangeEvent pce){
		path = uoaPath;
		this.pce = pce;
	}
	
	public String getUIGenInternalID(){
		if(ObjectEditor.coupleElides){
			return "*";
		} else{
			return path;
		}
	}
	
	public void execute() {
		try {
			ObjectAdapter adapter = null;
			if(ObjectEditor.coupleElides){
				adapter = ObjectRegistry.adapterAt(adapterID);
			} else{
				adapter = ObjectRegistry.getAdapter(path);
			}
			if(adapter!=null){
				adapter.subPropertyChange(pce);
			}
		} catch (Exception e) {
			System.err.println("UnivPropertyChange.execute(): Exception "+e);
			e.printStackTrace();
		}
	}
	
}