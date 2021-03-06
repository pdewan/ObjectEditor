package bus.uigen.test;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import org.apache.commons.collections15.map.HashedMap;

import util.models.AListenableHashMap;
import bus.uigen.ABasicObjectRegistry;
import bus.uigen.ObjectEditor;
import bus.uigen.ObjectRegistry;
import bus.uigen.jung.AJungGraphManagerCustomization;
import bus.uigen.jung.AnObjectAdapterToLogicalStructure;
import bus.uigen.jung.JungGraphApplet;
import bus.uigen.jung.ALogicalStructureEdgeLabelTransformer;
import bus.uigen.jung.ALogicalStructureVertexLabelTransformer;
import bus.uigen.jung.ALogicalStructureDisplayer;
import bus.uigen.jung.JungGraphManager;
import bus.uigen.jung.ObjectAdapterToJungGraph;
import bus.uigen.oadapters.ObjectAdapter;
import edu.uci.ics.jung.graph.Graph;

public class ObjectToJungGraph {
	public static void main (String[] args) {
		Map aMap = new AListenableHashMap();
		Map aChildMap = new AListenableHashMap();
		Map aChild2Map = new AListenableHashMap();
		
//		Object root = new ACompositeExample();
		Object compositeWithBackLink = new ACompositeExampleWithBackLink();
		aChildMap.put ("one", "child1Map");
		aChildMap.put("2", compositeWithBackLink);
		aChild2Map.put("two", "child2Map");
		aChild2Map.put("2",  compositeWithBackLink);
		Object composite = new ACompositeExample();
		aMap.put("one", aChildMap);
		aMap.put("two", aChild2Map );
		Object root = aMap;
		


//		new ALogicalStructureDisplayer(new JFrame());
//		ObjectEditor.edit(root);
		
		Object[] roots = {root, aChildMap};
		
//		ALogicalStructureDisplayer.createLogicalStructureDisplay(roots, new JFrame());
		JungGraphManager aJungGraphManager = ALogicalStructureDisplayer.
					createLogicalStructureDisplay(roots, new AJungGraphManagerCustomization<>());
		ObjectAdapter anAdapter = ObjectRegistry.getObjectAdapter(aChildMap);
		if (anAdapter != null) {
			aJungGraphManager.setVertexVisibile(anAdapter, false);
		}
		ObjectEditor.edit(aJungGraphManager);

//		aMap.put("3", "dynamic element");
		

//		Object root = new ACompositeExampleWithBackLink();
//		ObjectAdapter rootAdapter = ObjectEditor.toObjectAdapter(root);
//		ObjectAdapterToJungGraph<ObjectAdapter, ObjectAdapter> converter = new AnObjectAdapterToLogicalStructure();
//		boolean isForest = false;
//		if (rootAdapter.hasTreeLogicalStructure()) {
//			isForest = true;
//		}
//
//		Graph<ObjectAdapter, ObjectAdapter> graph = converter.createJungGraph(rootAdapter, isForest);
//		
//		
//		GenericJungGraphApplet applet = new GenericJungGraphApplet(graph, new ALogicalStructureVertexLabelTransformer(), new ALogicalStructureEdgeLabelTransformer(), isForest);
//		    	JFrame frame = new JFrame();
//		    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		    	frame.getContentPane().add(applet);
//
//		    	applet.init();
//		    	applet.start();
//		    	frame.pack();
//		    	frame.setVisible(true);
		    }
	

}
