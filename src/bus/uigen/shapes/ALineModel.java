package bus.uigen.shapes;

import java.awt.Rectangle;
import java.rmi.RemoteException;

import bus.uigen.OEFrame;
import bus.uigen.ObjectEditor;
import shapes.FlexibleLineShape;
import shapes.LineModel;
import util.annotations.StructurePattern;
import util.annotations.StructurePatternNames;
import util.misc.ThreadSupport;

//import shapes.shapes.LineModel;
@StructurePattern(StructurePatternNames.LINE_PATTERN)
public class ALineModel extends AShapeModel implements FlexibleLineShape {
	
	public ALineModel (Rectangle theBounds)
    {
		try {
        shapeModel = new shapes.LineModel(theBounds);
        init();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	public ALineModel (int x, int y, int width, int height)
    {
		try {
        shapeModel = new shapes.LineModel(x, y, width, height);
        init();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	public ALineModel(int x, int y, double length, double angle) {
		shapeModel = new LineModel(x, y, length, angle);
		init();
	}
	public ALineModel ()
    {
		try {
        shapeModel = new shapes.LineModel(); 
        init();
    }catch (Exception e) {
		e.printStackTrace();
	}
    }
	
	LineModel lineModel() {
		return (LineModel) shapeModel;
	}
	@Override
	public double getLength() {
		return lineModel().getLength();
	}
	@Override
	public double getRelativeAngle() {
		// TODO Auto-generated method stub
		return lineModel().getRelativeAngle();
	}
	@Override
	public void setLength(double newVal) {
		lineModel().setLength(newVal);
		
	}
	@Override
	public void setRelativeAngle(double newVal) {
		lineModel().setRelativeAngle(newVal);
	}
	@Override
	public void rotate(int units) {
		lineModel().rotate(units);
	}
	@Override
	public void rotate(double angle) {
		lineModel().rotate(angle);

		
	}
	public static void main (String[] args) {
		FlexibleLineShape polarLine = new ALineModel(100, 100, 200, 200);
		
//		ObjectEditor.edit(polarLine);
		OEFrame frame = ObjectEditor.edit(polarLine);
		while (true) {
			ThreadSupport.sleep(100);	
			polarLine.rotate(Math.PI/16);
			frame.refresh();
		}
	}
//	public static void main (String[] args) {
//		ALineModel lineModel = new ALineModel (100, 100, 200, 200);
//		
//   	 ObjectEditor.edit(lineModel);
//    }
}
