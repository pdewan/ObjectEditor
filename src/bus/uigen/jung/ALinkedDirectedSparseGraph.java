package bus.uigen.jung;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;

public class ALinkedDirectedSparseGraph<V,E> extends DirectedSparseGraph<V, E> {
//	  public ALinkedDirectedSparseGraph() 
//	    {
//		  super();
//	        vertices = new LinkedHashMap<V, Pair<Map<V,E>>>();
//	        edges = new LinkedHashMap<E, Pair<V>>();
//	    }
	  
	  @Override
	   public boolean addVertex(V vertex)
	    {
	        if(vertex == null) {
	            throw new IllegalArgumentException("vertex may not be null");
	        }
	        if (!containsVertex(vertex)) {
	            vertices.put(vertex, new Pair<Map<V,E>>(new LinkedHashMap<V,E>(), new LinkedHashMap<V,E>()));
	            return true;
	        } else {
	            return false;
	        }
	    }

}
