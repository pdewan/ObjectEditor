package bus.uigen.jung;

import java.awt.Color;
import java.awt.Container;
import java.rmi.RemoteException;

import javax.swing.JFrame;

import util.trace.TraceableBus;
import util.trace.uigen.LogicalStructureNodeCreated;
import bus.uigen.CompleteOEFrame;
import bus.uigen.ObjectEditor;
import bus.uigen.uiFrame;
import bus.uigen.uiGenerator;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.oadapters.ReferenceAdapter;
import bus.uigen.oadapters.RootAdapter;
import bus.uigen.widgets.VirtualComponent;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Graphs;

public class ALogicalStructureDisplayer implements LogicalStructureDisplayer {
	Graph<ObjectAdapter, ObjectAdapter> graph;
	JungGraphManager<ObjectAdapter, ObjectAdapter> jungGraphManager;
	public ALogicalStructureDisplayer(JFrame aFrame, 
			JungGraphManagerCustomization<ObjectAdapter, ObjectAdapter> aJungGraphManagerCustomization) {
		graph =   Graphs.<ObjectAdapter,ObjectAdapter>synchronizedDirectedGraph(new DirectedSparseMultigraph<ObjectAdapter,ObjectAdapter>());
		jungGraphManager = 	createLogicalStructureDisplay(graph,aFrame, false, aJungGraphManagerCustomization) ; 
		TraceableBus.addTraceableListener(this);		  
	}
	public static synchronized JungGraphManager<ObjectAdapter, ObjectAdapter> treeAndGraphDisplay(Object object,
			  
			JungGraphManagerCustomization<ObjectAdapter, ObjectAdapter> aJungGraphManagerCustomization) {
		CompleteOEFrame aFrame = ObjectEditor.textEdit(object)	;
		aFrame.showDrawPanel();
		aFrame.hideMainPanel();
		aFrame.showTreePanel();
		VirtualComponent aVirtualComponent = aFrame.getDrawVirtualComponent();
		Container aJPanel = (Container) aFrame.getDrawPanel().getPhysicalComponent();
		Container aContainer = (Container) aVirtualComponent.getPhysicalComponent();
//		JFrame aJFrame = (JFrame) aFrame.getFrame().getPhysicalComponent();
		JungGraphManager aGraphManager = ALogicalStructureDisplayer.
				createLogicalStructureDisplay(object, (JFrame) null, aContainer, aJungGraphManagerCustomization);
		aGraphManager.setOEFrame(aFrame);
		aFrame.setSize(700, 500);		
//		aJFrame.validate();			
		return aGraphManager;
	}
	public static JungGraphManager createLogicalStructureDisplay(Object[] aRoots,  
			JungGraphManagerCustomization<ObjectAdapter, ObjectAdapter> aJungGraphManagerCustomization) {
		JFrame aFrame = new JFrame();
		return createLogicalStructureDisplay(aRoots, aFrame, aJungGraphManagerCustomization);
		
		
	}
	public static JungGraphManager createLogicalStructureDisplay(Object[] aRoots, 
			JFrame aFrame, 
			JungGraphManagerCustomization<ObjectAdapter, ObjectAdapter> aJungGraphManagerCustomization) {
		JungGraphApplet applet = new JungGraphApplet();
		JungGraphManager retVal = createLogicalStructureDisplay(aRoots, aFrame, applet, aJungGraphManagerCustomization);
		 applet.init();
			applet.start();
			aFrame.pack();
			aFrame.setVisible(true);
			return retVal;
	}

	public static JungGraphManager createLogicalStructureDisplay(Object aRoot,
			JFrame aFrame,
			JungGraphManagerCustomization<ObjectAdapter, ObjectAdapter> aJungGraphManagerCustomization
			) {
		Object[] aRoots =  new Object[]{aRoot};
		return createLogicalStructureDisplay(aRoots, aFrame, aJungGraphManagerCustomization);
//		JungGraphApplet applet = new JungGraphApplet();
//		JungGraphManager retVal = createLogicalStructureDisplay(aRoot, aFrame, applet);
//		 applet.init();
//			applet.start();
//			aFrame.pack();
//			aFrame.setVisible(true);
//			return retVal;

//		ObjectAdapter rootAdapter = ObjectEditor.toObjectAdapter(aRoot);
//		ObjectAdapterToJungGraph<ObjectAdapter, ObjectAdapter> converter = new AnObjectAdapterToLogicalStructure();
//		boolean isForest = false;
//		if (rootAdapter.hasTreeLogicalStructure()) {
//			isForest = true;
//		}
//		Graph<ObjectAdapter, ObjectAdapter> graph = converter.createJungGraph(
//				rootAdapter, isForest);
//		return createLogicalStructureDisplay(graph, aFrame, isForest);

	}
	public static JungGraphManager createLogicalStructureDisplay(Object[] someRoots,
			JFrame aFrame, 
			Container aContainer, 
			JungGraphManagerCustomization<ObjectAdapter, ObjectAdapter> aJungGraphManagerCustomization) {
		ObjectAdapter[] someRootAdapters = new ObjectAdapter[someRoots.length];
		boolean areForests = true;
		for (int i = 0; i < someRoots.length; i++) {
		
		 someRootAdapters[i] = ObjectEditor.toObjectAdapter(someRoots[i]);
		 areForests = areForests && someRootAdapters[i].hasTreeLogicalStructure();
		}
		ObjectAdapterToJungGraph<ObjectAdapter, ObjectAdapter> converter = new AnObjectAdapterToLogicalStructure();
//		boolean isForest = false;
//		if (rootAdapter.hasTreeLogicalStructure()) {
//			isForest = true;
//		}
		Graph<ObjectAdapter, ObjectAdapter> graph = converter.createJungGraph(
				someRootAdapters, areForests);
		return createLogicalStructureDisplay(graph, aFrame,  areForests, aContainer, aJungGraphManagerCustomization);

	}
	public static JungGraphManager createLogicalStructureDisplay(Object aRoot,
			JFrame aFrame, 
			Container aContainer, 
			JungGraphManagerCustomization<ObjectAdapter, ObjectAdapter> aJungGraphManagerCustomization) {
		return createLogicalStructureDisplay(new Object[] {aRoot}, aFrame, aContainer, aJungGraphManagerCustomization);
//		ObjectAdapter rootAdapter = ObjectEditor.toObjectAdapter(aRoot);
//		ObjectAdapterToJungGraph<ObjectAdapter, ObjectAdapter> converter = new AnObjectAdapterToLogicalStructure();
//		boolean isForest = false;
//		if (rootAdapter.hasTreeLogicalStructure()) {
//			isForest = true;
//		}
//		Graph<ObjectAdapter, ObjectAdapter> graph = converter.createJungGraph(
//				rootAdapter, isForest);
//		return createLogicalStructureDisplay(graph, aFrame,  isForest, aContainer);

	}
	public static JungGraphManager createLogicalStructureDisplay(Object aRoot,
			CompleteOEFrame aFrame, Container aContainer, 
			JungGraphManagerCustomization<ObjectAdapter, ObjectAdapter> aJungGraphManagerCustomization) {
		JungGraphManager aRetVal = createLogicalStructureDisplay(new Object[] {aRoot}, (JFrame) null, aContainer, aJungGraphManagerCustomization);
		aRetVal.setOEFrame(aFrame);
		return aRetVal;

		//		ObjectAdapter rootAdapter = ObjectEditor.toObjectAdapter(aRoot);
//		ObjectAdapterToJungGraph<ObjectAdapter, ObjectAdapter> converter = new AnObjectAdapterToLogicalStructure();
//		boolean isForest = false;
//		if (rootAdapter.hasTreeLogicalStructure()) {
//			isForest = true;
//		}
//		Graph<ObjectAdapter, ObjectAdapter> graph = converter.createJungGraph(
//				rootAdapter, isForest);
//		return createLogicalStructureDisplay(graph, aFrame,  isForest, aContainer);

	}
	public static JungGraphManager createLogicalStructureDisplay(Graph<ObjectAdapter, ObjectAdapter> aGraph,
			JFrame aFrame, boolean  isForest, 
			JungGraphManagerCustomization<ObjectAdapter, ObjectAdapter> aJungGraphManagerCustomization) {
		JungGraphApplet applet = new JungGraphApplet();
//		applet.init();
//		JungGraphManager jungGraphManager = new AJungGraphManager<>(aGraph,
//				applet.getContentPane());
		JungGraphManager jungGraphManager = createLogicalStructureDisplay(aGraph, aFrame, isForest, applet.getContentPane(), aJungGraphManagerCustomization);
//		jungGraphManager.setForest(isForest);		
//		jungGraphManager
//				.setVertexLabelTransformer(new ALogicalStructureVertexLabelTransformer());
//		jungGraphManager
//				.setEdgeLabelTransformer(new ALogicalStructureEdgeLabelTransformer());
//		jungGraphManager
//				.setEdgeToolTipTransformer(new ALogicalStructureEdgeLabelTransformer());
//		if (isForest) {
//			Layout layout = jungGraphManager.getGraphLayout();
//			(new DuplicateClassCollapser(
//					(Forest<ObjectAdapter, ObjectAdapter>) aGraph, layout))
//					.traverse();
//		}
//		aFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		aFrame.getContentPane().add(applet);
//		applet.setBackground(Color.blue);
		 applet.init();
		applet.start();
		aFrame.pack();
		aFrame.setVisible(true);
//		ObjectEditor.edit(jungGraphManager);
		return jungGraphManager;
	}
	public static JungGraphManager createLogicalStructureDisplay(Graph<ObjectAdapter, ObjectAdapter> aGraph,
			JFrame aFrame, boolean  isForest, Container aContainer, 
			JungGraphManagerCustomization<ObjectAdapter, ObjectAdapter> aJungGraphManagerCustomization) {
//		JungGraphApplet applet = new JungGraphApplet();
//		applet.init();
//		JungGraphManager<ObjectAdapter, ObjectAdapter> jungGraphManager = new AMonolithicJungGraphManager<>(aGraph,
//				aContainer);
		JungGraphManager<ObjectAdapter, ObjectAdapter> jungGraphManager = new AModularJungGraphManager<>(aGraph,
				aContainer, aJungGraphManagerCustomization);
		jungGraphManager.setForest(isForest);		
		jungGraphManager
				.setVertexLabelTransformer(new ALogicalStructureVertexLabelTransformer<ObjectAdapter>());
		jungGraphManager
				.setEdgeLabelTransformer(new ALogicalStructureEdgeLabelTransformer());
		jungGraphManager
				.setEdgeToolTipTransformer(new ALogicalStructureEdgeLabelTransformer());
		jungGraphManager.setVertexToolTipTransformer(new ALogicalStructureVertexToolTipTransformer());
		TableDrivenColorer<ObjectAdapter> aTableDrivenColorer = new ATableDrivenObjectAdapterColorer(jungGraphManager);
		jungGraphManager.setVertexFillColorer(aTableDrivenColorer);
		jungGraphManager.setVertexDrawColorer(aTableDrivenColorer);
		if (isForest) {
			Layout layout = jungGraphManager.getGraphLayout();
			(new DuplicateClassCollapser(
					(Forest<ObjectAdapter, ObjectAdapter>) aGraph, layout))
					.traverse();
		}
		if (aFrame != null) {
		aFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		aFrame.getContentPane().add(aContainer);
		}
		aContainer.setBackground(Color.blue);
		// applet.init();
//		aContainer.start();
//		aFrame.pack();
//		aFrame.setVisible(true);
//		ObjectEditor.edit(jungGraphManager);
		return jungGraphManager;
	}
	@Override
	public void newEvent(Exception aTraceable) {
		if (aTraceable instanceof LogicalStructureNodeCreated) {
			LogicalStructureNodeCreated aLogicalStructureNodeCreated = (LogicalStructureNodeCreated) aTraceable;
			ObjectAdapter anObjectAdapter = (ObjectAdapter) aLogicalStructureNodeCreated.getTargetObject();
			if (anObjectAdapter instanceof RootAdapter) {
				return;
			}
			if (anObjectAdapter.isTopAdapter()) {
				
			jungGraphManager.addAndDisplayVertex(anObjectAdapter);
			return;
			
			}
			
//			if (anObjectAdapter.isTopAdapter())
//				return;
			ReferenceAdapter anEdgeAdapter;
			try {
				ObjectAdapter aParentAdapter = anObjectAdapter.getParentAdapter();
				anEdgeAdapter = new ReferenceAdapter(anObjectAdapter);
				jungGraphManager.addAndDisplayEdge(anEdgeAdapter, aParentAdapter, anObjectAdapter);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			
		}
		
	}

}
