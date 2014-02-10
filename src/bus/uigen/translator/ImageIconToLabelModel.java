package bus.uigen.translator;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import util.models.ALabelModel;
import util.models.Hashcodetable;
import util.models.LabelModel;


public class ImageIconToLabelModel implements Translator {
	static Hashcodetable<Icon, LabelModel> imageToLabelModel = new Hashcodetable<>();
	@Override
	public Object translate(Object obj) throws FormatException {
		LabelModel retVal = imageToLabelModel.get((Icon) obj);
		if (retVal == null)
			retVal = new ALabelModel((ImageIcon) obj);
		imageToLabelModel.put((Icon) obj, retVal);		
		return retVal;
//		return new ALabelModel((ImageIcon) obj);
	}

}
