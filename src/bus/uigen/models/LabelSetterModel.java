package bus.uigen.models;

import util.annotations.Visible;
import util.models.LabelBeanModel;

public interface LabelSetterModel {
	public void browse();
	public LabelBeanModel getLabel() ;
	public String getText();
	void setText(String newValue);
}
