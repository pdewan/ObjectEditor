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
public class ACompositeExample  {
	String string = "a string";
	int intVal = 5;
	String intAndString;
//	PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	public ACompositeExample (String aString, int anInt) {
		string = aString;
		intVal = anInt;
	}
	public ACompositeExample() {
		
		
	}
	public void incInt() {
		intVal++;
	}
	public void setString(String newVal) {
		String oldVal = string;

		this.string = newVal;
//		propertyChangeSupport.firePropertyChange("AString", oldVal, newVal);
//		propertyChangeSupport.firePropertyChange("AnIntAndString", null, getAnIntAndString());


	}
	@Explanation("A String")
//	@PreferredWidgetClass(JLabel.class)
	public String getString() {
		return string;
	}
	@Explanation("An Int")
	public int getInt() {
		return intVal;
	}

	public void setInt(int newVal) {
		this.intVal = newVal;
		
	}
	
	public void set(String newString, int newInt) {
		
		string = newString;
		intVal = newInt;
		


	}
	public String getIntAndString() {
		return string + intVal;
	}
//	boolean notificationsSuppressed;
//	public void toggleNotifications() {
//		propertyChangeSupport.firePropertyChange(OEFrame.SUPPRESS_NOTIFICATION_PROCESSING, notificationsSuppressed, !notificationsSuppressed);
//		notificationsSuppressed = !notificationsSuppressed;
//	}

	public static void main (String[] args) {
		ACompositeExample example = new ACompositeExample();
		OEFrame mainFrame = ObjectEditor.edit(example);		
		example.set("hello", 1);
		ThreadSupport.sleep(1000);
		example.set("hello", 2);
		ThreadSupport.sleep(1000);
//		example.toggleNotifications();
		example.setString("bye");
		ThreadSupport.sleep(1000);		
		example.setInt(1);
//		example.toggleNotifications();

		
		

//		JFrame frame = new JFrame();
//		ObjectEditor.edit(new AFooBar(), frame.getContentPane());
//		frame.setSize(100, 200);
//		frame.setVisible(true);
	}
//	@Override
//	public void addPropertyChangeListener(PropertyChangeListener aListener) {
//		System.out.println("property chnage listener ergiusteret called");
//		propertyChangeSupport.addPropertyChangeListener(aListener);
//		// TODO Auto-generated method stub
//		
//	}

}
