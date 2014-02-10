package bus.uigen.shapes;

import java.awt.Color;
import java.awt.Rectangle;
import java.rmi.RemoteException;

import util.models.AListenableVector;
import util.models.ListenableVector;

import bus.uigen.ObjectEditor;

//import shapes.shapes.RectangleModel;
@util.annotations.StructurePattern(util.annotations.StructurePatternNames.RECTANGLE_PATTERN)
public class ARectangleModel extends AShapeModel {
	
	public ARectangleModel (Rectangle theBounds)
    {
		try {
        shapeModel = new shapes.RectangleModel(theBounds);
        init();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	public ARectangleModel (int x, int y, int width, int height)
    {
		try {
        shapeModel = new shapes.RectangleModel(x, y, width, height);
        init();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	public ARectangleModel ()
    {
		try {
        shapeModel = new shapes.RectangleModel(); 
        init();
    }catch (Exception e) {
		e.printStackTrace();
	}
    }
	public static void main (String[] args) {
		ListenableVector vector = new AListenableVector();
		ARectangleModel model = new ARectangleModel(30, 30, 40, 40);		
		model.setFilled(true);
		model.setColor(Color.BLUE);
		vector.add(model);
//		ObjectEditor.edit(model);
		ObjectEditor.edit(vector);
		model.setColor(Color.GREEN);		
	}

}
