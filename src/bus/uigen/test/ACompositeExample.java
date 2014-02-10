package bus.uigen.test;

import java.beans.PropertyChangeListener;

import util.annotations.Explanation;
import util.annotations.StructurePattern;
import util.annotations.StructurePatternNames;
import util.models.PropertyListenerRegisterer;
import bus.uigen.OEFrame;
import bus.uigen.ObjectEditor;
@StructurePattern(StructurePatternNames.BEAN_PATTERN)
public class ACompositeExample  implements PropertyListenerRegisterer {
	String string = "a string";
	int intVal = 5;
	public ACompositeExample (String aString, int anInt) {
		string = aString;
		intVal = anInt;
	}
	public ACompositeExample() {
		
	}
	public void setAString(String string) {
		this.string = string;
	}
	@Explanation("A String")
//	@PreferredWidgetClass(JLabel.class)
	public String getAString() {
		return string;
	}
	@Explanation("An Int")
	public int getAnInt() {
		return intVal;
	}

	public void setAnInt(int intVal) {
		this.intVal = intVal;
	}
	
	public void set(String aString, int anIntVal) {
		string = aString;
		intVal = anIntVal;
	}

	public static void main (String[] args) {
		OEFrame mainFrame = ObjectEditor.edit(new ACompositeExample());
//		JFrame frame = new JFrame();
//		ObjectEditor.edit(new AFooBar(), frame.getContentPane());
//		frame.setSize(100, 200);
//		frame.setVisible(true);
	}
//	@Override
	public void addPropertyChangeListener(PropertyChangeListener aListener) {
		System.out.println("property chnage listener ergiusteret called");
		// TODO Auto-generated method stub
		
	}

}
