package bus.uigen.test;

import java.awt.Color;

import bus.uigen.OEFrame;
import bus.uigen.ObjectEditor;

public class TestPre extends ALabelAndString{
	boolean showText;
	boolean showLabel;
	public boolean isShowText() {
		return showText;
	}
	public void setShowText(boolean showText) {
		this.showText = showText;
	}
	public boolean isShowLabel() {
		return showLabel;
	}
	public void setShowLabel(boolean showLabel) {
		this.showLabel = showLabel;
	}
	public boolean preGetText() {
		return showText;
	}
	public boolean preGetLabelModel() {
		return showLabel;
	}
	public static void main (String args[]) {
		ObjectEditor.edit(new TestPre());
	}
}
