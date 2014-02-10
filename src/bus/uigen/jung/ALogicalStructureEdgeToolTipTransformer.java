package bus.uigen.jung;

import org.apache.commons.collections15.Transformer;

import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.oadapters.ReferenceAdapter;

public class ALogicalStructureEdgeToolTipTransformer implements Transformer<ObjectAdapter, String> {

	@Override
	public String transform(ObjectAdapter anOriginal) {
//		String retVal = anOriginal.getExplanation();
		String retVal = ((ReferenceAdapter) anOriginal).getReferentAdapter().getExplanation();
//		String retVal = anOriginal.getLabel();

		return retVal;
	}

}
