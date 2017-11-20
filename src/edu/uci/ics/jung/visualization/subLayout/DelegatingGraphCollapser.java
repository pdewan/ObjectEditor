package edu.uci.ics.jung.visualization.subLayout;

import java.awt.GraphicsConfigTemplate;
import java.lang.reflect.Field;
import java.util.Collection;

import bus.uigen.jung.ALinkedDirectedSparseGraph;
import bus.uigen.oadapters.ObjectAdapter;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.subLayout.GraphCollapser;
import edu.uci.ics.jung.graph.util.Graphs;
import edu.uci.ics.jung.graph.util.Pair;

public class DelegatingGraphCollapser extends GraphCollapser{
	protected Graph myOriginalGraph;
	public DelegatingGraphCollapser(Graph originalGraph) {
		super(originalGraph);
		myOriginalGraph = originalGraph;
		// TODO Auto-generated constructor stub
	}
	protected Graph getDelegate (Graph aDelegatingGraph, Class aDegelatingGraphClass) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		
//			Class aDegelatingGraphClass = aDelegatingGraph.getClass();
			Field aDelegateField = aDegelatingGraphClass.getDeclaredField("delegate");
			aDelegateField.setAccessible(true);
			return (Graph) aDelegateField.get(aDelegatingGraph);
		
		
	}
	
	protected Graph createGraph() throws InstantiationException, IllegalAccessException {
		//		    try {
		     if (myOriginalGraph instanceof DelegateForest) {
//		    	 Graph aDelegate = getDelegate(myOriginalGraph, myOriginalGraph.getClass());
		         
//		    	 new DelegateForest ((DirectedGraph) aDelegate);	
		    	 return new DelegateForest(new ALinkedDirectedSparseGraph());	
		    	 
		     }
		     String aClassName = myOriginalGraph.getClass().getSimpleName();
		     if (aClassName.contains("SynchronizedDirectedGraph")) {
//		    	 Graph aDelegate = getDelegate(myOriginalGraph, myOriginalGraph.getClass().getSuperclass());

//		    	 return Graphs.synchronizedDirectedGraph((DirectedGraph) aDelegate);
				 return Graphs.synchronizedDirectedGraph(new DirectedSparseMultigraph());

		     }
	        return (Graph)myOriginalGraph.getClass().newInstance();
		    
//		    } catch (NoSuchFieldException e) {
//			    
//				e.printStackTrace();
//				throw new InstantiationException();
//			} catch (IllegalArgumentException e) {
//				// TODO Auto-generated catch block
//				throw new IllegalArgumentException();
//			}
	  }
	 
	 	/*
		 * Following code reproduced from super class because of stupid default accessor
		 */
	 /*
		public Graph getClusterGraph(Graph inGraph, Collection picked) {
	        Graph clusterGraph;
	        try {
	            clusterGraph = createGraph();
	        } catch (InstantiationException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	            return null;
	        } catch (IllegalAccessException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	            return null;
	        }
	        for(Object v : picked) {
	        	clusterGraph.addVertex(v);
	            Collection edges = inGraph.getIncidentEdges(v);
	            for(Object edge : edges) {
	                Pair endpoints = inGraph.getEndpoints(edge);
	                Object v1 = endpoints.getFirst();
	                Object v2 = endpoints.getSecond();
	                if(picked.containsAll(endpoints)) {
	                    clusterGraph.addEdge(edge, v1, v2, inGraph.getEdgeType(edge));
	                }
	            }
	        }
	        return clusterGraph;
	    }

	 public Graph collapse(Graph inGraph, Graph clusterGraph) {
	        
	        if(clusterGraph.getVertexCount() < 2) return inGraph;

	        Graph graph = inGraph;
	        try {
	            graph = createGraph();
	        } catch(Exception ex) {
	            ex.printStackTrace();
	        }
	        Collection cluster = clusterGraph.getVertices();
	        
	        // add all vertices in the delegate, unless the vertex is in the
	        // cluster.
	        for(Object v : inGraph.getVertices()) {
	            if(cluster.contains(v) == false) {
	                graph.addVertex(v);
	            }
	        }
	        // add the clusterGraph as a vertex
	        graph.addVertex(clusterGraph);
	        
	        //add all edges from the inGraph, unless both endpoints of
	        // the edge are in the cluster
	        for(Object e : (Collection<?>)inGraph.getEdges()) {
	            Pair endpoints = inGraph.getEndpoints(e);
	            // don't add edges whose endpoints are both in the cluster
	            if(cluster.containsAll(endpoints) == false) {

	                if(cluster.contains(endpoints.getFirst())) {
	                	graph.addEdge(e, clusterGraph, endpoints.getSecond(), inGraph.getEdgeType(e));

	                } else if(cluster.contains(endpoints.getSecond())) {
	                	graph.addEdge(e, endpoints.getFirst(), clusterGraph, inGraph.getEdgeType(e));

	                } else {
	                	graph.addEdge(e,endpoints.getFirst(), endpoints.getSecond(), inGraph.getEdgeType(e));
	                }
	            }
	        }
	        return graph;
	    }
	 
	 public Graph expand(Graph inGraph, Graph clusterGraph) {
	        Graph graph = inGraph;
	        try {
	            graph = createGraph();
	        } catch(Exception ex) {
	            ex.printStackTrace();
	        }
	        Collection cluster = clusterGraph.getVertices();
	        logger.fine("cluster to expand is "+cluster);

	        // put all clusterGraph vertices and edges into the new Graph
	        for(Object v : cluster) {
	            graph.addVertex(v);
	            for(Object edge : clusterGraph.getIncidentEdges(v)) {
	                Pair endpoints = clusterGraph.getEndpoints(edge);
	                graph.addEdge(edge, endpoints.getFirst(), endpoints.getSecond(), clusterGraph.getEdgeType(edge));
	            }
	        }
	        // add all the vertices from the current graph except for
	        // the cluster we are expanding
	        for(Object v : inGraph.getVertices()) {
	            if(v.equals(clusterGraph) == false) {
	                graph.addVertex(v);
	            }
	        }

	        // now that all vertices have been added, add the edges,
	        // ensuring that no edge contains a vertex that has not
	        // already been added
	        for(Object v : inGraph.getVertices()) {
	            if(v.equals(clusterGraph) == false) {
	                for(Object edge : inGraph.getIncidentEdges(v)) {
	                    Pair endpoints = inGraph.getEndpoints(edge);
	                    Object v1 = endpoints.getFirst();
	                    Object v2 = endpoints.getSecond();
	                     if(cluster.containsAll(endpoints) == false) {
	                        if(clusterGraph.equals(v1)) {
	                            // i need a new v1
	                            Object originalV1 = originalGraph.getEndpoints(edge).getFirst();
	                            Object newV1 = findVertex(graph, originalV1);
	                            assert newV1 != null : "newV1 for "+originalV1+" was not found!";
	                            graph.addEdge(edge, newV1, v2, inGraph.getEdgeType(edge));
	                        } else if(clusterGraph.equals(v2)) {
	                            // i need a new v2
	                            Object originalV2 = originalGraph.getEndpoints(edge).getSecond();
	                            Object newV2 = findVertex(graph, originalV2);
	                            assert newV2 != null : "newV2 for "+originalV2+" was not found!";
	                            graph.addEdge(edge, v1, newV2, inGraph.getEdgeType(edge));
	                        } else {
	                        	graph.addEdge(edge, v1, v2, inGraph.getEdgeType(edge));
	                        }
	                    }
	                }
	            }
	        }
	        return graph;
	    }
	 Object findVertex(Graph inGraph, Object vertex) {
	        Collection vertices = inGraph.getVertices();
	        if(vertices.contains(vertex)) {
	            return vertex;
	        }
	        for(Object v : vertices) {
	            if(v instanceof Graph) {
	                Graph g = (Graph)v;
	                if(contains(g, vertex)) {
	                    return v;
	                }
	            }
	        }
	        return null;
	    }
	    
	    private boolean contains(Graph inGraph, Object vertex) {
	    	boolean contained = false;
	    	if(inGraph.getVertices().contains(vertex)) return true;
	    	for(Object v : inGraph.getVertices()) {
	    		if(v instanceof Graph) {
	    			contained |= contains((Graph)v, vertex);
	    		}
	    	}
	    	return contained;
	    }
	    */

}
