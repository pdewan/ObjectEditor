package bus.uigen.shapes;

import java.awt.Color;
import java.awt.Rectangle;
import java.rmi.RemoteException;

import shapes.ArcShape;

import bus.uigen.ObjectEditor;


@util.annotations.StructurePattern(util.annotations.StructurePatternNames.ARC_PATTERN)
public class AnArcModel extends AShapeModel implements ArcShape {
	
	
	public AnArcModel (int x, int y, int width, int height, int startAngle, int endAngle)
    {
		try {
        shapeModel = new shapes.ArcModel(x, y, width, height, startAngle, endAngle);
        init();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	public AnArcModel ()
    {
		try {
        shapeModel = new shapes.ArcModel(); 
        init();
    }catch (Exception e) {
		e.printStackTrace();
	}
    }
	@Override
    public int getStartAngle() {
    	try {
    	return getArcModel().getStartAngle();
    	} catch (Exception e) {
    		return 0;
    	}
    }
    	
	@Override
	public int getEndAngle() {
		try {
	    	return getArcModel().getEndAngle();
	    	} catch (Exception e) {
	    		return 0;
	    	}
	}
	@Override
	public void setStartAngle(int newVal) {
		try {
	    	getArcModel().setEndAngle(newVal);
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    	}
		
	}
	@Override
	public void setEndAngle(int newVal) {
		try {
	    	getArcModel().setEndAngle(newVal);
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    	}
		
	}
	shapes.ArcModel getArcModel() {
		return (shapes.ArcModel) shapeModel;
	}
	public static void main (String[] args) {
		AnArcModel arcModel = new AnArcModel (0, 0, 100, 100, 0, 180);
//		arcModel.setFilled(true);
		arcModel.setColor(Color.BLUE);
   	 ObjectEditor.edit(arcModel);
    }
    
    

}
