package bus.uigen.jung;

import org.apache.commons.collections15.Transformer;

import bus.uigen.oadapters.ObjectAdapter;

public class ALogicalStructureVertexToolTipTransformer implements Transformer<ObjectAdapter, String> {

	@Override
	public String transform(ObjectAdapter anOriginal) {
		String retVal = anOriginal.getExplanation();
//		String retVal = anOriginal.getLabel();

		return retVal;
	}

}
