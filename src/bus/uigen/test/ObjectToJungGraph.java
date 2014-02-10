package bus.uigen.test;

import javax.swing.JFrame;

import bus.uigen.ObjectEditor;
import bus.uigen.jung.AnObjectAdapterToLogicalStructure;
import bus.uigen.jung.JungGraphApplet;
import bus.uigen.jung.ALogicalStructureEdgeLabelTransformer;
import bus.uigen.jung.ALogicalStructureVertexLabelTransformer;
import bus.uigen.jung.ALogicalStructureDisplayer;
import bus.uigen.jung.ObjectAdapterToJungGraph;
import bus.uigen.oadapters.ObjectAdapter;
import edu.uci.ics.jung.graph.Graph;

public class ObjectToJungGraph {
	public static void main (String[] args) {
		Object root = new ACompositeExample();
//		new ALogicalStructureDisplayer(new JFrame());
//		ObjectEditor.edit(root);
		
		ALogicalStructureDisplayer.createLogicalStructureDisplay(root, new JFrame());
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
