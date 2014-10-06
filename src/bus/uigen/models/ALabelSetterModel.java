package bus.uigen.models;

import util.annotations.Column;
import util.annotations.ComponentHeight;
import util.annotations.ComponentWidth;
import util.annotations.Row;
import util.annotations.Visible;
import util.models.ALabelBeanModel;
import util.models.LabelBeanModel;

public abstract class ALabelSetterModel implements LabelSetterModel{
	LabelBeanModel labelModel = new ALabelBeanModel(" A Label", null);
	String text;
	
	@Override
	@Visible(false)
	 public void setText(String newValue) {		 
		labelModel.set(newValue, null);
	}
	
//	@ComponentWidth(350)
	@ComponentHeight(25)
	@Column(1) // border layout, so this means main
//	@Row(0)

	public LabelBeanModel getLabel() {
		return labelModel;
	}
	@Visible(false)
	public String getText() {
		return labelModel.getText();
	}

}
