package bus.uigen.test;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import bus.uigen.OEFrame;
import bus.uigen.ObjectEditor;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.controller.models.AWindowOperationsModel;
import bus.uigen.shapes.ARectangleModel;
import bus.uigen.shapes.OEShapeModel;
import shapes.RectangleModel;
import util.annotations.Column;
import util.annotations.ComponentWidth;
import util.annotations.Row;
import util.annotations.ShowDebugInfoWithToolTip;
import util.models.PropertyListenerRegisterer;
/**
 * Overriding only those methods of SquaringCounterAndRectangle whose positions need to be 
 * specified in the main panel
 *
 */
public class SquaringCounterAndRectangleWithButtons extends  SquaringCounterAndRectangle {
	
	 
	// Let the increment method be mapped to a button in the main panel at row 0, column 0
	// Let its width be 100
	@Row(0)
	@Column(0)
	@ComponentWidth(100)
	public void increment() {
		super.increment();
	}
	 
	// Let the decrement method be mapped to a button in the main panel at row 0, column 1
	// Let its width be 100
	@Row(0)
	@Column(1)
	@ComponentWidth(100)
	public void decrement() {
		setNumber(number - 1);		
	}
	// Let the number property be method be mapped to a text field in the main panel at row 1
	// Let its width be 100
	@Row(1)
	public int getNumber() {
		return super.getNumber();
	}
	@Row(2)
	public int getSquare() {
		return super.getSquare();
	}
	
	public static void main (String[] args) {
			
			OEFrame oeFrame = ObjectEditor.edit(new SquaringCounterAndRectangleWithButtons());


			
			
	}
	

}
