package bus.uigen.jung;

import org.apache.commons.collections15.Predicate;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Context;

public interface TableBasedGraphElementInclusionPredicate<VertexType,EdgeType, ElementType > extends 
Predicate<Context<Graph<VertexType,EdgeType>, ElementType>>{
	
	void setIncludeGraphElement (ElementType anElement,  boolean newVal);
	boolean getIncludeGraphElement (ElementType anElement);
	void setDefaultInclusion(boolean newVal);
	boolean  getDefaultInclusion();
}
