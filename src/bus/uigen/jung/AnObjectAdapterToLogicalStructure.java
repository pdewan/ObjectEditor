package bus.uigen.jung;

import bus.uigen.oadapters.GraphReferenceAdapter;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.oadapters.ReferenceAdapter;
import edu.uci.ics.jung.graph.Graph;

public class AnObjectAdapterToLogicalStructure extends AbstractObjectAdapterToJungGraph<ObjectAdapter, ObjectAdapter>{
	
	public static String NULL_REPRESENTAION = "null";
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
			else { edgeAdapter = new GraphReferenceAdapter(aVertexDestination); }

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
