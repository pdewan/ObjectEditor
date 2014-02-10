package bus.uigen.test;

import javax.swing.JFrame;
import javax.swing.JSlider;

import util.annotations.ComponentHeight;
import util.annotations.ComponentWidth;
import util.annotations.PreferredWidgetClass;
import bus.uigen.ObjectEditor;

public class ALogo {
	JFrame frame = new JFrame();
	String meaning = "life";
	boolean hasMeaning = true;
	int meaningNumber = 42;	
	@ComponentWidth(24)
	public String getMeaning() {
		return meaning;
	}
	public boolean isHasMeaning() {
		return hasMeaning;
	}
	@ComponentHeight(35)
	@PreferredWidgetClass(JSlider.class)
	public int getMeaningNumber() {
		return meaningNumber;
	}	
	public void setMeaning(String meaning) {
		this.meaning = meaning;
	}
	public void setHasMeaning(boolean hasMeaning) {
		this.hasMeaning = hasMeaning;
	}
	public void setMeaningNumber(int meaningNumber) {
		this.meaningNumber = meaningNumber;
	}
	public static void main (String[] args) {
		ObjectEditor.edit(new ALogo());
	}

}
