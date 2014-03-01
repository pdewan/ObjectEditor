package bus.uigen.translator;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import util.models.ALabelBeanModel;
import util.models.Hashcodetable;
import util.models.LabelBeanModel;


public class ImageIconToLabelModel implements Translator {
	static Hashcodetable<Icon, LabelBeanModel> imageToLabelModel = new Hashcodetable<>();
	@Override
	public Object translate(Object obj) throws FormatException {
		LabelBeanModel retVal = imageToLabelModel.get((Icon) obj);
		if (retVal == null)
			retVal = new ALabelBeanModel((ImageIcon) obj);
		imageToLabelModel.put((Icon) obj, retVal);		
		return retVal;
//		return new ALabelModel((ImageIcon) obj);
	}

}
