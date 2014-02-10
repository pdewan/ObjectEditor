package bus.uigen.jung;

import java.util.HashSet;
import java.util.Set;

import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.oadapters.PrimitiveAdapter;
import bus.uigen.reflect.ClassProxy;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.visualization.subLayout.TreeCollapser;

public class DuplicateClassCollapser extends JungForestTraverser<ObjectAdapter, ObjectAdapter> {
	Set<Class> visitedClasses = new HashSet();
	Layout layout;
	public DuplicateClassCollapser(Forest<ObjectAdapter, ObjectAdapter> aForest, Layout aLayout) {
		super(aForest);
		layout = aLayout;
	}
	static TreeCollapser treeCollapser = new TreeCollapser();
	@Override
	public Object visit(ObjectAdapter aVertex) {
		if (aVertex instanceof PrimitiveAdapter) return null;
		Object anObject = aVertex.getRealObject();
		if (anObject != null) {
			Class aClass = anObject.getClass();
			if (visitedClasses.contains(aClass)) {
				try {
					treeCollapser.collapse(layout, forest, aVertex);
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
			} else {
				visitedClasses.add(aClass);
			}
		}
		return null;
		
	}

}
