package bus.uigen.test;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import bus.uigen.ObjectEditor;
import util.annotations.Column;
import util.annotations.ComponentWidth;
import util.annotations.Row;
import util.models.PropertyListenerRegistrar;

public class SquaringCounterWithButtons implements PropertyListenerRegistrar {
	
	PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	public SquaringCounterWithButtons() {
	}
	public int number;
	@Row(0)
	@Column(0)
	@ComponentWidth(100)
	public void increment() {
		setNumber(number + 1);
	}
	void setNumber(int newValue) {
		int oldValue = number;
		number = newValue;
		propertyChangeSupport.firePropertyChange("number", oldValue, number);
	}
	@Row(0)
	@Column(1)
	@ComponentWidth(100)
	public void decrement() {
		setNumber(number - 1);		
	}
	@Row(1)
	public int getNumber() {
		return number;
	}
	@Row(2)
	public int getSquare() {
		return number*number;
	}
	public static void main (String[] args) {
		ObjectEditor.edit(new SquaringCounterWithButtons());
		
	}
	@Override
	public void addPropertyChangeListener(PropertyChangeListener aListener) {
		propertyChangeSupport.addPropertyChangeListener(aListener);
		
	}

}
