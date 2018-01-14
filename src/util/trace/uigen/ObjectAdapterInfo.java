package util.trace.uigen;

import util.trace.TraceableInfo;
import bus.uigen.oadapters.ObjectAdapter;

public class ObjectAdapterInfo extends TraceableInfo {
	public ObjectAdapterInfo(String aMessage, ObjectAdapter aFinder) {
		super(aMessage, aFinder);
	}
	public ObjectAdapterInfo(String aMessage) {
		super(aMessage);
	}
	public ObjectAdapter getObjectAdapter() {
		return (ObjectAdapter) getFinder();
	}

}
