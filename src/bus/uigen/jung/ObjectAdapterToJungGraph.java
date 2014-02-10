package bus.uigen.jung;

import bus.uigen.oadapters.ObjectAdapter;
import edu.uci.ics.jung.graph.Graph;

public interface ObjectAdapterToJungGraph <VertexType, EdgeType>{
	
	 public Graph<VertexType, EdgeType> createJungGraph (ObjectAdapter aRoot, boolean isForest) ;
	 public Graph<VertexType, EdgeType> createJungGraph (ObjectAdapter[] someRoots, boolean areForests);
}
