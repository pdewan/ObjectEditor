package bus.uigen.jung;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.util.TreeUtils;

public abstract class JungForestTraverser<VertexType, EdgeType> {
	Forest<VertexType, EdgeType> forest;
	public JungForestTraverser(Forest<VertexType, EdgeType> aForest) {
		forest = aForest;		
	}

	public abstract Object visit(VertexType aVertex);
	public List traverse() {
		List retVal = new ArrayList();
		Collection<VertexType> someRoots = TreeUtils.getRoots(forest);
		for (VertexType aRoot:someRoots) {
			traverse(aRoot, retVal);
		
		}
		
		return retVal;
	}
	public void traverse(VertexType aVertex, List aList) {
		Object retVal = visit(aVertex);	
		if (retVal != null) {
			aList.add(retVal);
		}
		Collection<VertexType> someChildren = forest.getChildren(aVertex);
		if (someChildren == null) return;
		List<VertexType> aChildrenList = new ArrayList();
		aChildrenList.addAll(someChildren);
//		for (VertexType aChild:someChildren) {
//			traverse(aChild, aList);
//		}
		for (VertexType aChild:aChildrenList) {
			traverse(aChild, aList);
		}
	}

}
