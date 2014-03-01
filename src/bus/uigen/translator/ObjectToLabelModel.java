package bus.uigen.translator;

import javax.swing.ImageIcon;

import util.models.ALabelBeanModel;
import util.models.Hashcodetable;
import util.models.LabelBeanModel;

public class ObjectToLabelModel implements Translator {

//	static Hashcodetable<Object, LabelModel> stringToLabelModel = new Hashcodetable<>();

	public Object translate(Object obj) throws FormatException {
//		LabelModel retVal = stringToLabelModel.get(obj);
//		if (retVal == null) {
//			retVal = new ALabelModel( obj.toString());
//			stringToLabelModel.put(obj, retVal);
//		}
//		return retVal.to;
		return new ALabelBeanModel( obj.toString());


	}

}
