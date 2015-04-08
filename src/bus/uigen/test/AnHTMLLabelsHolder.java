package bus.uigen.test;


import java.util.List;

import bus.uigen.ObjectEditor;
import util.misc.Common;
import util.models.ALabelBeanModel;
import util.models.AListenableVector;
import util.models.LabelBeanModel;
import util.models.ListenableVector;

public class AnHTMLLabelsHolder {
	LabelBeanModel labelBeanModel = new ALabelBeanModel(
			Common.toBlueColoredUnderlinedHrefHTML("https://www.google.com", "google"));
	ListenableVector labels = new AListenableVector();
	public AnHTMLLabelsHolder() {
		labels.add(labelBeanModel);
	}
	
	public List<LabelBeanModel> getLabelBeanModel() {
		return labels;
	}
	
	public void changeLabel() {
		labelBeanModel.setText(Common.toBlueColoredUnderlinedHrefHTML("https://www.bing.com", "bing"));
	}
	public void emptyLabel() {
		labelBeanModel.setText(Common.toBlueColoredUnderlinedHrefHTML("", ""));
	}
	
	public static void main (String[] args) {
		ObjectEditor.edit(new AnHTMLLabelsHolder());
	}
	

}
