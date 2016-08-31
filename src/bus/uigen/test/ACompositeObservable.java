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
public class ACompositeObservable extends ACompositeExample  implements PropertyListenerRegisterer {
//	String string = "a string";
//	int intVal = 5;
//	String intAndString;
	PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	public ACompositeObservable (String aString, int anInt) {
		super(aString, anInt);
//		string = aString;
//		intVal = anInt;
	}
	public ACompositeObservable() {
		
		
	}
	public void setString(String newVal) {
		String oldVal = string;

		super.setString(newVal);
		propertyChangeSupport.firePropertyChange("String", oldVal, newVal);
		propertyChangeSupport.firePropertyChange("IntAndString", null, getIntAndString());


	}
//	@Explanation("A String")
////	@PreferredWidgetClass(JLabel.class)
//	public String getAString() {
//		return string;
//	}
//	@Explanation("An Int")
//	public int getAnInt() {
//		return intVal;
//	}

	public void setInt(int newVal) {
		int oldVal = intVal;
		super.setInt(newVal);
		propertyChangeSupport.firePropertyChange("AnInt", oldVal, newVal);
		propertyChangeSupport.firePropertyChange("AnIntAndString", null, getIntAndString());

	}
	
	public void set(String newString, int newInt) {
		int oldInt = intVal;
		String oldString = string;
		super.set(newString, newInt);
		propertyChangeSupport.firePropertyChange("AnInt", oldInt, newInt);
		propertyChangeSupport.firePropertyChange("AString", oldString, newString);
		propertyChangeSupport.firePropertyChange("AnIntAndString", null, getIntAndString());


	}
//	public String getAnIntAndString() {
//		return string + intVal;
//	}
	boolean notificationsSuppressed;
	public void toggleNotifications() {
		propertyChangeSupport.firePropertyChange(OEFrame.SUPPRESS_NOTIFICATION_PROCESSING, notificationsSuppressed, !notificationsSuppressed);
		notificationsSuppressed = !notificationsSuppressed;
	}

	public static void main (String[] args) {
		ACompositeObservable example = new ACompositeObservable();
		OEFrame mainFrame = ObjectEditor.edit(example);		
		example.set("hello", 1);
		ThreadSupport.sleep(1000);
		example.set("hello", 2);
		ThreadSupport.sleep(1000);
		example.toggleNotifications();
		example.setString("bye");
		ThreadSupport.sleep(1000);		
		example.setInt(1);
		example.toggleNotifications();

		
		

//		JFrame frame = new JFrame();
//		ObjectEditor.edit(new AFooBar(), frame.getContentPane());
//		frame.setSize(100, 200);
//		frame.setVisible(true);
	}
//	@Override
	public void addPropertyChangeListener(PropertyChangeListener aListener) {
		System.out.println("property change listener called");
		propertyChangeSupport.addPropertyChangeListener(aListener);
		// TODO Auto-generated method stub
		
	}

}
