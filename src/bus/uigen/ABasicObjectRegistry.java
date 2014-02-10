package bus.uigen;

import java.beans.PropertyChangeListener;
import java.util.Hashtable;
import java.util.Vector;

//import logging.loggable.GenericLoggableInterface;
//import logging.loggable.GenericLoggerUI;
//import logging.loggable.PseudoServerFront;
import util.models.Hashcodetable;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.view.WidgetShell;
public class ABasicObjectRegistry implements BasicObjectRegistry 
{
	 Hashcodetable<Object, ObjectAdapter> objectToObjectAdapterMapping = new Hashcodetable();
	
	

	/* (non-Javadoc)
	 * @see bus.uigen.BasicObjectRegistry#mapObjectToAdapter(java.lang.Object, bus.uigen.oadapters.ObjectAdapter)
	 */
	@Override
	public  void mapObjectToAdapter(Object obj, ObjectAdapter uioa){
		if(obj != null){
			objectToObjectAdapterMapping.put(obj,uioa);
		}
	}
	// returns the latest adapter in all frames
	/* (non-Javadoc)
	 * @see bus.uigen.BasicObjectRegistry#getObjectAdapter(java.lang.Object)
	 */
	@Override
	public  ObjectAdapter getObjectAdapter(Object obj) {
		return objectToObjectAdapterMapping.get(obj);
	}
	@Override
	public ObjectAdapter remove(Object obj) {
		return objectToObjectAdapterMapping.remove(obj);
	}

	
}
