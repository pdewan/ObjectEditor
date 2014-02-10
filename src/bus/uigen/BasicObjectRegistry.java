package bus.uigen;

import bus.uigen.oadapters.ObjectAdapter;

public interface BasicObjectRegistry {

	public void mapObjectToAdapter(Object obj, ObjectAdapter uioa);

	// returns the latest adapter in all frames
	public ObjectAdapter getObjectAdapter(Object obj);
	
	public ObjectAdapter remove(Object obj);

}