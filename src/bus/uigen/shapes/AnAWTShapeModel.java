package bus.uigen.shapes;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.rmi.RemoteException;

import bus.uigen.ObjectEditor;

import util.annotations.StructurePattern;
import util.annotations.StructurePatternNames;

//import shapes.shapes.LineModel;
//@StructurePattern(StructurePatternNames.AWT_SHAPE_PATTERN)
public class AnAWTShapeModel extends AShapeModel implements AWTShape{
	
	public AnAWTShapeModel (Shape theShape)
    {
		try {
        shapeModel = new shapes.AWTShapeModel(theShape);
        init();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	shapes.AWTShapeModel getAWTShapeModel() {
		return (shapes.AWTShapeModel) shapeModel;
	}
	
	@Override
	public Shape getShape() {
		return getAWTShapeModel().getShape();
	}
	@Override
	public void setShape(Shape newValue) {
		getAWTShapeModel().setShape(newValue);		 
	}
	
	
	public static void main (String[] args) {
		Shape aShape = new Rectangle(0, 0, 100, 100);
		AnAWTShapeModel aShapeModel = new AnAWTShapeModel (aShape);		
//   	    ObjectEditor.edit(aShapeModel);
   	    aShapeModel.setShape(new Rectangle(100, 100, 100, 100));
   	    ObjectEditor.edit(aShape);
    }

}
