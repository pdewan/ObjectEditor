package bus.uigen.test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.rmi.RemoteException;

import bus.uigen.OEFrame;
import bus.uigen.ObjectEditor;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.controller.models.AWindowOperationsModel;
import bus.uigen.shapes.ARectangleModel;
import bus.uigen.shapes.OEShapeModel;
import shapes.RectangleModel;
import shapes.ShapeModel;
import util.annotations.Column;
import util.annotations.ComponentWidth;
import util.annotations.Row;
import util.annotations.ShowDebugInfoWithToolTip;
import util.annotations.StructurePattern;
import util.annotations.StructurePatternNames;
import util.models.PropertyListenerRegisterer;
@StructurePattern(StructurePatternNames.BEAN_PATTERN)
public class SquaringCounterAndRectangle implements PropertyListenerRegisterer {
	
	PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	int number = 2;
	final int MAGNIFICATION = 10;
	 OEShapeModel rectangle = new ARectangleModel();
//	 ShapeModel rectangle ;

	 OEShapeModel squaredRectangle = new ARectangleModel();
//	 ShapeModel squaredRectangle;

	


	public SquaringCounterAndRectangle() {
//		  try {
//			rectangle = new shapes.RectangleModel();
//			squaredRectangle  = new shapes.RectangleModel();
//		} catch (RemoteException e) {
//			e.printStackTrace();
//		}

		rectangle.setFilled(true);
		rectangle.setColor(Color.red);
		squaredRectangle.setFilled(true);
		squaredRectangle.setColor(Color.cyan);
		rectangle.setZIndex(1);
		squaredRectangle.setZIndex(2);
		updateRectangles();
//		rectangle = new ARectangleModel(new Rectangle(0, 0, magnifiedSide, magnifiedSide));
//		 
//		squaredRectangle = new ARectangleModel(new Rectangle(0, 0, magnifiedSide*magnifiedSide, 
//				magnifiedSide*magnifiedSide));		
	}
	
	protected void updateRectangles() {
		int magnifiedSide = getNumber()*MAGNIFICATION;
		int magnifiedSquareSide = getSquare()*MAGNIFICATION;
		rectangle.setSize(new Dimension(magnifiedSide, magnifiedSide));
		squaredRectangle.setSize(new Dimension(magnifiedSquareSide, magnifiedSquareSide));
	}
	 	 
	

	public void increment() {
		setNumber(number + 1);
	}
	void setNumber(int newValue) {
		int oldNumber = number;
		int oldSquare = getSquare();
		number = newValue;
		propertyChangeSupport.firePropertyChange("number", oldNumber, number);
		propertyChangeSupport.firePropertyChange("square", oldSquare, getSquare());
		updateRectangles();

	}

	public void decrement() {
		setNumber(number - 1);		
	}
//	@Row(1)
	public int getNumber() {
		return number;
	}
//	@Row(2)
	public int getSquare() {
		return number*number;
	}
	public OEShapeModel getRectangle() {

//	public ShapeModel getRectangle() {
		return rectangle;
	}
	public OEShapeModel getSquaredRectangle() {

//	public ShapeModel getSquaredRectangle() {
		return squaredRectangle;
	}
	
	@Override
	public void addPropertyChangeListener(PropertyChangeListener aListener) {
		propertyChangeSupport.addPropertyChangeListener(aListener);
		
	}
	public static void main (String[] args) {
		
		OEFrame oeFrame = ObjectEditor.edit(new SquaringCounterAndRectangle());


		
	}

}
