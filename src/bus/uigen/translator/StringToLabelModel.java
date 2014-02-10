package bus.uigen.translator;

import javax.swing.ImageIcon;

import com.adobe.acrobat.gui.tree.Icon;

import util.models.ALabelModel;
import util.models.Hashcodetable;
import util.models.LabelModel;

public class StringToLabelModel implements Translator<String, LabelModel> {

	static Hashcodetable<String, LabelModel> stringToLabelModel = new Hashcodetable<>();

	public LabelModel translate(String obj) throws FormatException {
		LabelModel retVal = stringToLabelModel.get((String) obj);
		if (retVal == null)
			retVal = new ALabelModel((String) obj);
		stringToLabelModel.put((String) obj, retVal);
		return retVal;
//		return new ALabelModel((String) obj);


	}

}
