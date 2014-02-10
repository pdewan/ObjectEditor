package bus.uigen.models;
import java.awt.Color;
import java.awt.Font;
import java.util.Vector;

import shapes.FlexibleShape;
import shapes.FlexibleTextShape;
import shapes.RemoteShape;
import shapes.TextShape;
import slm.SLModel;
import util.models.AListenableVector;
import bus.uigen.shapes.ALineModel;
import bus.uigen.shapes.APointModel;
import bus.uigen.shapes.ARectangleModel;
import bus.uigen.shapes.AStringModel;
import bus.uigen.shapes.AnImageModel;
import bus.uigen.shapes.AnOvalModel;
public class AComponentDrawer extends AListenableVector<FlexibleShape> implements ComponentDrawer{	Color color;	Font font;
	SLModel slModel; // this is vestigial	boolean isFilled;	int shapeNum = 0;	Vector shapesDrawn = new Vector(); // vestigial
		public AComponentDrawer (SLModel theSLModel) {		slModel = theSLModel;		
	}	
	public FlexibleShape drawRectangle(int x, int y, int width, int height) {
		try {		FlexibleShape retVal = new ARectangleModel(x, y, width, height);
		add(retVal);//		showShape(retVal);					return retVal;
		} catch (Exception e){
			System.out.println(e);
			return null;
		}
	}
	public FlexibleShape fillRectangle(int x, int y, int width, int height) {
		try {
		FlexibleShape retVal = new ARectangleModel(x, y, width, height);
		retVal.setFilled(true);
		add(retVal);
//		showShape(retVal);			
		return retVal;
		} catch (Exception e){
			System.out.println(e);
			return null;
		}
	}	public FlexibleShape drawOval(int x, int y, int width, int height) {
		try {		FlexibleShape retVal = new AnOvalModel(x, y, width, height);
		add(retVal);//		showShape(retVal);			return retVal;
		} catch (Exception e){
			System.out.println(e);
			return null;
		}
	}
	public FlexibleShape fillOval(int x, int y, int width, int height) {
		try {
		FlexibleShape retVal = new AnOvalModel(x, y, width, height);
		retVal.setFilled(true);
		add(retVal);
//		showShape(retVal);	
		return retVal;
		} catch (Exception e){
			System.out.println(e);
			return null;
		}
	}
	public FlexibleShape drawLine(int x, int y, int width, int height) {
		try {		FlexibleShape retVal = new ALineModel(x, y, width, height);
		add(retVal);//		showShape(retVal);			return retVal;
		} catch (Exception e){
			System.out.println(e);
			return null;
		}
	}
	public FlexibleTextShape drawString(String text, int x, int y) {		try {
		FlexibleTextShape retVal = new AStringModel (text, x, y);
		add (retVal);
//		retVal.setBounds(x,y, width, height);//		showShape(retVal);			return retVal;
		} catch (Exception e){
			System.out.println(e);
			return null;
		}
	}
	public FlexibleShape drawImage(String fileName, int x, int y) {
		try {
		FlexibleShape retVal = new AnImageModel (fileName, x, y);
		add(retVal);
//		showShape(retVal);	
		return retVal;
		} catch (Exception e){
			System.out.println(e);
			return null;
		}
	}	public FlexibleShape drawPoint(int x, int y) {
		try {		FlexibleShape retVal = new APointModel(x, y);//		showShape(retVal);			return retVal;
		} catch (Exception e){
			System.out.println(e);
			return null;
		}
	}	
	// vestigial
	void showShape (RemoteShape shapeModel) {	
		try {		shapeNum++;
		shapeModel.setColor(color);		shapeModel.setFont (font);		String key = "Shape" + shapeNum;
		slModel.put(key, shapeModel);		shapesDrawn.addElement(key);
		} catch (Exception e ) {
    		System.out.println(e);
    	}
	}
	public void clearDrawing() {
		super.clear();		shapeNum = 0;		for (int i=0; i<shapesDrawn.size(); i++)			slModel.remove((String) shapesDrawn.elementAt(i));
	}
//	@Override
//	public boolean removeElement(OEShape c) {
//		// TODO Auto-generated method stub
//		return false;
//	}
	@Override
	public boolean remove(FlexibleShape obj) {
		return super.remove(obj);
	}
		
}

