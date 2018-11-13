package bus.uigen.test;

import util.annotations.ComponentWidth;
import util.annotations.EditablePropertyNames;
import util.annotations.Explanation;
import util.annotations.Position;
import util.annotations.PropertyNames;

@PropertyNames({"String", "Int", "IntAndString"})
@EditablePropertyNames({"String", "Int"})
public interface CompositeExample {

	public abstract void incInt();

	public abstract void setString(String newVal);

	//	@PreferredWidgetClass(JLabel.class)
	public abstract String getString();

	public abstract int getInt();

	//	@Position(0)
	public abstract void setInt(int newVal);

	public abstract void set(String newString, int newInt);

	public abstract String getIntAndString();
	//	boolean notificationsSuppressed;
	//	public void toggleNotifications() {
	//		propertyChangeSupport.firePropertyChange(OEFrame.SUPPRESS_NOTIFICATION_PROCESSING, notificationsSuppressed, !notificationsSuppressed);
	//		notificationsSuppressed = !notificationsSuppressed;
	//	}

}