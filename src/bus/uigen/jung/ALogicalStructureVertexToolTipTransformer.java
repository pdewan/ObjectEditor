package bus.uigen.jung;

import org.apache.commons.collections15.Transformer;

import bus.uigen.oadapters.ObjectAdapter;

public class ALogicalStructureVertexToolTipTransformer implements Transformer<ObjectAdapter, String> {

	@Override
	public String transform(ObjectAdapter anOriginal) {
		Object aRealObject = anOriginal.getRealObject();
		if (aRealObject == null) {
			return "null";
		}
		String retVal = aRealObject.toString();
//		String retVal = anOriginal.getExplanation();
		
//		String retVal = anOriginal.getLabel();

		return retVal;
	}

}
