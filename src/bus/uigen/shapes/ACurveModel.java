package bus.uigen.shapes;

import java.awt.Rectangle;
import java.rmi.RemoteException;

import shapes.CurveShape;

import bus.uigen.ObjectEditor;


@util.annotations.StructurePattern(util.annotations.StructurePatternNames.CURVE_PATTERN)
public class ACurveModel extends AShapeModel implements CurveShape {
	
	
	public ACurveModel (int x, int y, int width, int height, int controlX, int controlY)
    {
		try {
        shapeModel = new shapes.CurveModel(x, y, width, height, controlX, controlY);
        init();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	public ACurveModel (int x, int y, int width, int height, 
			int controlX, 	int controlY, int controlX2, int controlY2)
    {
		try {
        shapeModel = new shapes.CurveModel(x, y, width, height, 
        		controlX, controlY, controlX2, controlY2);
        init();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	public ACurveModel ()
    {
		try {
        shapeModel = new shapes.CurveModel(); 
        init();
    }catch (Exception e) {
		e.printStackTrace();
	}
    }
	@Override
    public int getControlX() {
    	try {
    	return getCurveModel().getControlX();
    	} catch (Exception e) {
    		return 0;
    	}
    }
    	
	@Override
	public int getControlY() {
		try {
	    	return getCurveModel().getControlY();
	    	} catch (Exception e) {
	    		return 0;
	    	}
	}
	@Override
	public void setControlX(int newVal) {
		try {
	    	getCurveModel().setControlY(newVal);
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    	}
		
	}
	@Override
	public void setControlY(int newVal) {
		try {
	    	getCurveModel().setControlY(newVal);
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    	}
		
	}
	
	@Override
	public int getControlY2() {
		try {
	    	return getCurveModel().getControlY2();
	    	} catch (Exception e) {
	    		return 0;
	    	}
	}
	@Override
	public void setControlX2(int newVal) {
		try {
	    	getCurveModel().setControlY2(newVal);
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    	}
		
	}
	@Override
	public void setControlY2(int newVal) {
		try {
	    	getCurveModel().setControlY2(newVal);
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    	}
		
	}
	
	///
	
	
	shapes.CurveModel getCurveModel() {
		return (shapes.CurveModel) shapeModel;
	}
	public static void main (String[] args) {
		ACurveModel curveModel = new ACurveModel (0, 0, 10, 20, 45, 135);
		curveModel.setFilled(true);
   	 ObjectEditor.edit(curveModel);
    }
    
    

}
