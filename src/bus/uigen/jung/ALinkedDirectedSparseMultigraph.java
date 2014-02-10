package bus.uigen.jung;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.Pair;

public class ALinkedDirectedSparseMultigraph<V,E> extends DirectedSparseMultigraph<V, E> {
//	  public ALinkedDirectedSparseGraph() 
//	    {
//		  super();
//	        vertices = new LinkedHashMap<V, Pair<Map<V,E>>>();
//	        edges = new LinkedHashMap<E, Pair<V>>();
//	    }
	  
	  @Override
	   public boolean addVertex(V vertex) {
	    	if(vertex == null) {
	    		throw new IllegalArgumentException("vertex may not be null");
	    	}
	        if (!containsVertex(vertex)) {
	            vertices.put(vertex, new Pair<Set<E>>(new LinkedHashSet<E>(), new LinkedHashSet<E>()));
	            return true;
	        } else {
	            return false;
	        }
	    }
//	   public boolean addVertex(V vertex)
//	    {
//	        if(vertex == null) {
//	            throw new IllegalArgumentException("vertex may not be null");
//	        }
//	        if (!containsVertex(vertex)) {
//	            vertices.put(vertex, new Pair<Map<V,E>>(new LinkedHashMap<V,E>(), new LinkedHashMap<V,E>()));
//	            return true;
//	        } else {
//	            return false;
//	        }
//	    }

}
