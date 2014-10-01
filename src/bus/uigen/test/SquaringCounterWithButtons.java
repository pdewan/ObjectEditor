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
	public int number = 2;
	@Row(0)
	@Column(0)
	@ComponentWidth(100)
	public void increment() {
		setNumber(number + 1);
	}
	void setNumber(int newValue) {
		int oldNumber = number;
		int oldSquare = getSquare();
		number = newValue;
		propertyChangeSupport.firePropertyChange("number", oldNumber, number);
		propertyChangeSupport.firePropertyChange("square", oldSquare, getSquare());

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
