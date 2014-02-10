package bus.uigen.jung;

import java.util.Enumeration;

import util.models.Hashcodetable;

import bus.uigen.oadapters.CompositeAdapter;
import bus.uigen.oadapters.HashtableAdapter;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.oadapters.ReferenceAdapter;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Graphs;

public abstract class AbstractObjectAdapterToJungGraph<VertexType, EdgeType> implements ObjectAdapterToJungGraph<VertexType, EdgeType> {
	
//	Hashcodetable<ObjectAdapter, VertexType> objectAdapterToVertex = new Hashcodetable();
	
	  public Graph<VertexType, EdgeType> createJungGraph (ObjectAdapter aRoot, boolean isForest) {
		  return createJungGraph (new ObjectAdapter[] {aRoot}, isForest);
//		  Graph<VertexType, EdgeType> retVal =  null;
//		  if (isForest) {
////			  retVal = new DelegateForest<VertexType, EdgeType>();	
//			  retVal =   new DelegateForest<VertexType, EdgeType>(new ALinkedDirectedSparseGraph<VertexType, EdgeType>());	
//		  } else {
//			  retVal =  Graphs.<VertexType,EdgeType>synchronizedDirectedGraph(new DirectedSparseMultigraph<VertexType,EdgeType>());
//			  
//		  }
//		  traverseVisibleAndInvisible(retVal, aRoot);
//		  return retVal;		  
	  }
	  
	  public Graph<VertexType, EdgeType> createJungGraph (ObjectAdapter[] someRoots, boolean areForests) {
		  Graph<VertexType, EdgeType> retVal =  null;
		  if (areForests) {
//			  retVal = new DelegateForest<VertexType, EdgeType>();	
			  retVal =   new DelegateForest<VertexType, EdgeType>(new ALinkedDirectedSparseGraph<VertexType, EdgeType>());	
		  } else {
			  retVal =  Graphs.<VertexType,EdgeType>synchronizedDirectedGraph(new DirectedSparseMultigraph<VertexType,EdgeType>());
			  
		  }
		  for (ObjectAdapter aRoot: someRoots)
		  traverseVisibleAndInvisible(retVal, aRoot);
		  return retVal;		  
	  }
	  
//	  public VertexType getVertex(Graph<VertexType, EdgeType> aGraph, ObjectAdapter aVertexAdapter) {
//		  VertexType retVal = objectAdapterToVertex.get(aVertexAdapter);
//		  if (retVal == null) {
//			  retVal = createVertex(aVertexAdapter);
//		  }
//		  return
//	  }
	
	  public abstract void addEdge (Graph<VertexType, EdgeType> aGraph,
			  ObjectAdapter aSourceAdapter, ObjectAdapter aDestinationAdapter, VertexType aVertexSource, VertexType aVertexDestination);
		  
	  public abstract VertexType createVertex (ObjectAdapter aVertexAdapter);

	  public VertexType traverseVisibleAndInvisible(Graph<VertexType, EdgeType> aGraph, ObjectAdapter adapter) {
//		  if (isRecursive(adapter, results))
//			  return results;
		  VertexType aVertex = null;
	    if (adapter != null ) {
	    	
	    	
	    	
	    	 aVertex = createVertex(adapter);
//	    	 if (!aGraph.containsVertex(aVertex))
	    		 aGraph.addVertex(aVertex);
	    	
//	      results.addElement(visit(adapter, targetLevel, curLevel, targetNodeNum, curNodeNum++));
	    	
	      if (adapter instanceof HashtableAdapter) {
	    	  HashtableAdapter hashtableAdapter = (HashtableAdapter) adapter;
	    	  Enumeration<ObjectAdapter> keyAdapters = hashtableAdapter.getKeyAdapters();
	    	  while (keyAdapters.hasMoreElements()) {
	    		  ObjectAdapter keyAdapter = keyAdapters.nextElement();
	    		  ObjectAdapter valueAdapter = keyAdapter.getValueAdapter();
	    		  VertexType aChildVertex = traverseVisibleAndInvisible(aGraph, valueAdapter);
	    		  addEdge(aGraph, hashtableAdapter, valueAdapter, aVertex, aChildVertex );
	    		  
	    	  }
	      }
	      else if (adapter instanceof CompositeAdapter) {
			Enumeration children = (((CompositeAdapter) adapter).getVisibleAndInvisibleDynamicChildAdapters()).elements();
			while (children.hasMoreElements()) {
			   ObjectAdapter child = (ObjectAdapter) children.nextElement();
			   VertexType aChildVertex = traverseVisibleAndInvisible(aGraph, child);
			   addEdge(aGraph, adapter, child, aVertex, aChildVertex);
			   
			
	      }
	    }
		
	  }
		 return aVertex;
	  }


}
