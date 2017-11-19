package bus.uigen.jung;

import org.apache.commons.collections15.Transformer;

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
//				return anOriginalObject.getClass().getSimpleName();
				String aSimpleName = anOriginalObject.getClass().getSimpleName();
				String aHashString = Integer.toHexString(anOriginalObject.hashCode());
				String aValue = anOriginalObjectAdapter.isLeaf()?"="+ anOriginalObject.toString():":";
				return aSimpleName + "(" + aHashString + ")" + aValue ;

			}
		}
		else
//			return anOriginal.toString();
		    return "Collapsed node";

	}

}
