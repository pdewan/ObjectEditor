package bus.uigen.test;

import java.beans.PropertyChangeEvent;

import bus.uigen.ObjectEditor;
import bus.uigen.oadapters.ObjectAdapter;
import util.trace.TraceableBus;
import util.trace.TraceableListener;
import util.trace.uigen.ObjectAdapterPropertyChangeEventInfo;

public class ATraceableBusTest implements TraceableListener {
	 ACompositeObservable observable = new ACompositeObservable();
	 Thread initialThread;
	
	public ATraceableBusTest() {
		ObjectAdapter anAdapter = ObjectEditor.toObjectAdapter(observable);
		TraceableBus.addTraceableListener(this);
		initialThread = Thread.currentThread();
		observable.setInt(5);
	}

	@Override
	public void newEvent(Exception aTraceable) {
		if (aTraceable instanceof ObjectAdapterPropertyChangeEventInfo) {
			ObjectAdapterPropertyChangeEventInfo aPropertyChange = (ObjectAdapterPropertyChangeEventInfo) aTraceable;
			System.out.println("Property change event:" + aTraceable);
			Thread newThread = Thread.currentThread();
			newThread.stop();
			System.out.println ("New thread == old thread:" + (newThread == initialThread));
			
		}
		
	}
	public static void main (String[] args) {
		new ATraceableBusTest();
	}

}
