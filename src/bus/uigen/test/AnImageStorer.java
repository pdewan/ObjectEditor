package bus.uigen.test;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import util.annotations.ComponentHeight;
import util.annotations.ComponentWidth;
import util.annotations.PreferredWidgetClass;
import bus.uigen.ObjectEditor;

public class AnImageStorer {
	ImageIcon image;
	String string = "foo";
	public AnImageStorer() {
		image = new ImageIcon("holygrail2.PNG");
	}
	@PreferredWidgetClass(JLabel.class)
	@ComponentHeight(40)
	@ComponentWidth(20)
	public ImageIcon getImage() {
		return image;
	}
	
	public void setImageIcon(ImageIcon image) {
		this.image = image;
	}
	@ComponentHeight(40)
	@ComponentWidth(20)
	public String getString() {
		return string;
	}
	public void setString(String string) {
		this.string = string;
	}
	public static void main (String[] args) {
		AnImageStorer imageStorer = new AnImageStorer();
		ObjectEditor.edit(imageStorer);
	
		
	}
}
