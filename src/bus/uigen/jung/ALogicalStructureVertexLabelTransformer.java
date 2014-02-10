package bus.uigen.jung;

import org.apache.commons.collections15.Transformer;

import bus.uigen.oadapters.ObjectAdapter;

public class ALogicalStructureVertexLabelTransformer implements Transformer<Object, String> {

	@Override
	public String transform(Object anOriginal) {
		if (anOriginal instanceof ObjectAdapter) {
			Object anOriginalObject = ((ObjectAdapter) anOriginal)
					.getRealObject();
			if (anOriginalObject == null) {
				return "null";
			} else {
				return anOriginalObject.getClass().getSimpleName();
			}
		}
		else
//			return anOriginal.toString();
		    return "Collapsed node";

	}

}
