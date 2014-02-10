package bus.uigen;

import java.util.List;
import java.util.Vector;

import util.models.AListenableVector;
import util.models.AListenableVectorTester;
import util.models.AVectorMethodsListener;
import util.models.ListenableVectorTester;
import util.models.VectorMethodsListener;

public class AListenableVectorTesterDriver {
	public static void main (String[] args) {
		ListenableVectorTester<String> tester = new AListenableVectorTester<String>();		
		List<String> peer = new AListenableVector<String>();
		tester.addElement("Value 1");
		tester.addElement("Value 2");
		tester.addElement("Value 3");
		tester.initPeer(peer);
		peer.add("Peer 1");
		peer.add("Peer 2");
		peer.add("Peer 3");
		VectorMethodsListener  listener = new AVectorMethodsListener();	
		tester.addVectorMethodsListener(listener);
		
		ObjectEditor.edit(tester);
		ObjectEditor.edit(peer);		
		
	}

}
