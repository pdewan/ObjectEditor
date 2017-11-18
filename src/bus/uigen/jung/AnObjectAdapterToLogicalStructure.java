package bus.uigen.jung;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import bus.uigen.oadapters.GraphEdgeReferenceAdapter;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.oadapters.PrimitiveAdapter;
import bus.uigen.oadapters.ReferenceAdapter;
import edu.uci.ics.jung.graph.Graph;

public class AnObjectAdapterToLogicalStructure extends AbstractObjectAdapterToJungGraph<ObjectAdapter, ObjectAdapter>{
	
	public static String NULL_REPRESENTAION = "null";
	protected Map<Object, ObjectAdapter> primitiveToObjectAdapter = new HashMap();
	public static final Class[] WrapperClasses = {
		Integer.class,
		Double.class,
		Long.class,
		Short.class,
		Float.class,
		Byte.class,
		Character.class
		
	};
	public static final Set<Class> WrapperClassesSet = new HashSet<>(Arrays.asList(WrapperClasses));
	
	protected ObjectAdapter maybeGetPreviousObjectAdapter(PrimitiveAdapter anAdapter) {
		Object aRealObject = anAdapter.getRealObject();
		if (aRealObject == null) {
			return anAdapter;
		}
		Class aRealClass = aRealObject.getClass();
		
		if (aRealClass == String.class || WrapperClassesSet.contains(aRealClass)) {
			int aHashCode = System.identityHashCode(aRealObject);
			ObjectAdapter aStoredAdapter = primitiveToObjectAdapter.get(aHashCode);
			if (aStoredAdapter == null) {
				aStoredAdapter = anAdapter;
				primitiveToObjectAdapter.put(aHashCode, anAdapter );
			}
			return aStoredAdapter;
			
		}
		return anAdapter;
	}
	@Override
	public void addEdge(Graph<ObjectAdapter, ObjectAdapter> aGraph, ObjectAdapter aSource,
			ObjectAdapter aDestination, ObjectAdapter aVertexSource, ObjectAdapter aVertexDestination) {
		ObjectAdapter edgeAdapter;
		try {
			if (aDestination.isValueAdapter()) {
				edgeAdapter = aDestination.getKeyAdapter(); 
			}
		// to make sure two links to the same  destination adapter are separate objects 
//			else { edgeAdapter = new ReferenceAdapter(aVertexDestination); }
			else {
//				edgeAdapter = new GraphReferenceAdapter(aVertexDestination);
				// we need the label of the immediate destination
				edgeAdapter = new GraphEdgeReferenceAdapter(aDestination);

			}

		aGraph.addEdge(edgeAdapter, aVertexSource, aVertexDestination);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		
	}

	@Override
	public ObjectAdapter createVertex(ObjectAdapter aVertexAdapter) {
		ObjectAdapter retVal = aVertexAdapter;

		if (aVertexAdapter instanceof ReferenceAdapter) {
			retVal = ((ReferenceAdapter) aVertexAdapter).getReferentAdapter();	
//			try {
//			retVal = new GraphVertexReferenceAdapter(((ReferenceAdapter) aVertexAdapter).getReferentAdapter());	
//			} catch (RemoteException e) {
//				e.printStackTrace();
//			}

		} else if (aVertexAdapter instanceof PrimitiveAdapter) {
			retVal = maybeGetPreviousObjectAdapter((PrimitiveAdapter) aVertexAdapter);
		}
		return retVal;
//		Object aRealObject = aVertexAdapter.getRealObject();
//		String aVertexNode = null;
//		if (aRealObject != null) {
//			return aRealObject.getClass().getSimpleName();
//		}
//		return "null";
	}
	
	

}
