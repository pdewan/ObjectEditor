package bus.uigen.jung;

import org.apache.commons.collections15.Transformer;

import bus.uigen.attributes.AttributeNames;
import bus.uigen.oadapters.ObjectAdapter;

public class ALogicalStructureVertexLabelTransformer<ElementType> implements Transformer<ElementType, String> {

	@Override
	public String transform(Object anOriginal) {
		if (anOriginal instanceof ObjectAdapter) {
			ObjectAdapter anOriginalObjectAdapter = (ObjectAdapter) anOriginal;
			
			Object anOriginalObject = anOriginalObjectAdapter
					.getRealObject();
			
			if (anOriginalObject == null) {
				return "null";
			} else {
				
				String aLabel = VertexObjectToLabelFactory.getLabeler().toLabel(anOriginalObject);
				if (aLabel != null) {
					return aLabel;
				}
				
				if (anOriginalObjectAdapter.getNodeLabelIsToString()) {
					return anOriginalObject.toString();
				}
//				return anOriginalObject.getClass().getSimpleName();
				String aSimpleName = anOriginalObject.getClass().getSimpleName();
				
				int aHashCode = anOriginalObject.getClass().equals(String.class)?
						anOriginalObject.hashCode():System.identityHashCode(anOriginalObject);
				
				String aHashString = Integer.toHexString(aHashCode);
				
				String aValue = anOriginalObjectAdapter.isLeaf()?"="+ anOriginalObject.toString():":";
				return aSimpleName + "(" + aHashString + ")" + aValue ;

			}
		}
		else
//			return anOriginal.toString();
		    return "Collapsed node";

	}

}
