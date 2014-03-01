package bus.uigen.translator;

import javax.swing.ImageIcon;

import com.adobe.acrobat.gui.tree.Icon;

import util.models.ALabelBeanModel;
import util.models.Hashcodetable;
import util.models.LabelBeanModel;

public class StringToLabelModel implements Translator<String, LabelBeanModel> {

	static Hashcodetable<String, LabelBeanModel> stringToLabelModel = new Hashcodetable<>();

	public LabelBeanModel translate(String obj) throws FormatException {
		LabelBeanModel retVal = stringToLabelModel.get((String) obj);
		if (retVal == null)
			retVal = new ALabelBeanModel((String) obj);
		stringToLabelModel.put((String) obj, retVal);
		return retVal;
//		return new ALabelModel((String) obj);


	}

}
