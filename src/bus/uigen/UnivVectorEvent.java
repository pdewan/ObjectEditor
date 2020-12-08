package bus.uigen;
import bus.uigen.oadapters.VectorAdapter;
import java.util.Vector;

import util.models.VectorChangeEvent;

public class UnivVectorEvent implements java.io.Serializable, UIGenLoggableEvent {
	int adapterID;
	String path;
	VectorChangeEvent uve;

	public UnivVectorEvent(VectorAdapter uva, VectorChangeEvent uve) {
		adapterID = ObjectRegistry.indexOfAdapter(uva);
		this.uve = uve;
	}
	
	public UnivVectorEvent(String uvaPath, VectorChangeEvent uve) {
		path = uvaPath;
		this.uve = uve;
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
			VectorAdapter adapter = null;
			if(ObjectEditor.coupleElides){
				adapter = (VectorAdapter) ObjectRegistry.adapterAt(adapterID);
			} else{
				adapter = (VectorAdapter) ObjectRegistry.getAdapter(path);
			}
			if(adapter!=null){
				adapter.subUpdateVector(uve);
			}
		} catch (Exception e) {
			System.err.println("UnivVectorEvent.execute(): Exception "+e);
			e.printStackTrace();
		}
	}
	
}