package bus.uigen.test;

import bus.uigen.ObjectEditor;
import util.annotations.Column;
import util.annotations.ComponentWidth;
import util.annotations.Row;

public class SquaringCounterWithButtons {
	

	public SquaringCounterWithButtons() {
	}
	public int number;
	@Row(0)
	@Column(0)
	@ComponentWidth(100)
	public void increment() {
		number++;
	}
	@Row(0)
	@Column(1)
	@ComponentWidth(100)
	public void decrement() {
		number--;		
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

}
