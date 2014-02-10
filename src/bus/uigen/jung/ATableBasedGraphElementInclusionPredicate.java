package bus.uigen.jung;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections15.Predicate;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Context;

public class ATableBasedGraphElementInclusionPredicate<VertexType,EdgeType, ElementType > 
     implements TableBasedGraphElementInclusionPredicate<VertexType, EdgeType, ElementType> {
     
    boolean defaultInclusion = true;
    Map<ElementType, Boolean> elementToInclusion = new HashMap();
	@Override
	public boolean evaluate(
			Context<Graph<VertexType, EdgeType>, ElementType> aContext) {
//		ElementType element = aContext.element;
		Boolean retVal = elementToInclusion.get(aContext.element);
		return retVal == null?defaultInclusion:retVal;
	}

	@Override
	public void setIncludeGraphElement(ElementType anElement, boolean newVal) {
		elementToInclusion.put(anElement, newVal);
	}

	@Override
	public boolean getIncludeGraphElement(ElementType anElement) {
		return elementToInclusion.get(anElement);
	}

	@Override
	public void setDefaultInclusion(boolean newVal) {
		defaultInclusion = newVal;
	}

	@Override
	public boolean getDefaultInclusion() {
		return defaultInclusion;
	}

}
