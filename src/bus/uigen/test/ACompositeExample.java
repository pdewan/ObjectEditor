package bus.uigen.test;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import util.annotations.Explanation;
import util.annotations.StructurePattern;
import util.annotations.StructurePatternNames;
import util.misc.ThreadSupport;
import util.models.PropertyListenerRegisterer;
import bus.uigen.OEFrame;
import bus.uigen.ObjectEditor;
@StructurePattern(StructurePatternNames.BEAN_PATTERN)
public class ACompositeExample  implements PropertyListenerRegisterer {
	String string = "a string";
	int intVal = 5;
	String intAndString;
	PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	public ACompositeExample (String aString, int anInt) {
		string = aString;
		intVal = anInt;
	}
	public ACompositeExample() {
		
		
	}
	public void setAString(String newVal) {
		String oldVal = string;

		this.string = newVal;
		propertyChangeSupport.firePropertyChange("AString", oldVal, newVal);
		propertyChangeSupport.firePropertyChange("AnIntAndString", null, getAnIntAndString());


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

	public void setAnInt(int newVal) {
		int oldVal = intVal;
		this.intVal = newVal;
		propertyChangeSupport.firePropertyChange("AnInt", oldVal, newVal);
		propertyChangeSupport.firePropertyChange("AnIntAndString", null, getAnIntAndString());

	}
	
	public void set(String newString, int newInt) {
		int oldInt = intVal;
		String oldString = string;
		string = newString;
		intVal = newInt;
		propertyChangeSupport.firePropertyChange("AnInt", oldInt, newInt);
		propertyChangeSupport.firePropertyChange("AString", oldString, newString);
		propertyChangeSupport.firePropertyChange("AnIntAndString", null, getAnIntAndString());


	}
	public String getAnIntAndString() {
		return string + intVal;
	}
	boolean notificationsSuppressed;
	public void toggleNotifications() {
		propertyChangeSupport.firePropertyChange(OEFrame.SUPPRESS_NOTIFICATION_PROCESSING, notificationsSuppressed, !notificationsSuppressed);
		notificationsSuppressed = !notificationsSuppressed;
	}

	public static void main (String[] args) {
		ACompositeExample example = new ACompositeExample();
		OEFrame mainFrame = ObjectEditor.edit(example);		
		example.set("hello", 1);
		ThreadSupport.sleep(1000);
		example.set("hello", 2);
		ThreadSupport.sleep(1000);
		example.toggleNotifications();
		example.setAString("bye");
		ThreadSupport.sleep(1000);		
		example.setAnInt(1);
		example.toggleNotifications();

		
		

//		JFrame frame = new JFrame();
//		ObjectEditor.edit(new AFooBar(), frame.getContentPane());
//		frame.setSize(100, 200);
//		frame.setVisible(true);
	}
//	@Override
	public void addPropertyChangeListener(PropertyChangeListener aListener) {
		System.out.println("property chnage listener ergiusteret called");
		propertyChangeSupport.addPropertyChangeListener(aListener);
		// TODO Auto-generated method stub
		
	}

}
