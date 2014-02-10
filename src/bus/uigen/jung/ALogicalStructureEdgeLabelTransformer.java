package bus.uigen.jung;

import org.apache.commons.collections15.Transformer;

import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.oadapters.ReferenceAdapter;

public class ALogicalStructureEdgeLabelTransformer implements Transformer<ObjectAdapter, String> {

	@Override
	public String transform(ObjectAdapter anOriginal) {
		String retVal = "";
		if (anOriginal.isKeyAdapter()) {
			retVal = anOriginal.getRealObject().toString();
		} else  {
			retVal = ((ReferenceAdapter) anOriginal).getReferentAdapter().getReference();
	}
//		String retVal = anOriginal.getLabel();

		return retVal;
	}

}
