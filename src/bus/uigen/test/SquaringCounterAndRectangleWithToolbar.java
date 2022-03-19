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

public class SquaringCounterAndRectangleWithToolbar  {
	
	
	public static void main (String[] args) {
		// Let the increment method of SquaringCounterAndRectangle be mapped to a button in the toolbar
		ObjectEditor.setMethodAttribute(SquaringCounterAndRectangle.class, "increment", AttributeNames.TOOLBAR_METHOD, true);
		
		// Let the decrement method of SquaringCounterAndRectangle be mapped to a button in the toolbar

		ObjectEditor.setMethodAttribute(SquaringCounterAndRectangle.class, "decrement", AttributeNames.TOOLBAR_METHOD, true);
		
		// display the object after setting the attributed above
		OEFrame oeFrame = ObjectEditor.edit(new SquaringCounterAndRectangle());

		// show the toolbar
		oeFrame.showToolBar();
		
	}
	
}
