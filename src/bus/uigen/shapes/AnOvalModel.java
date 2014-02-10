package bus.uigen.shapes;

import java.awt.Rectangle;
import java.rmi.RemoteException;

//import shapes.shapes.OvalModel;
@util.annotations.StructurePattern(util.annotations.StructurePatternNames.OVAL_PATTERN)
public class AnOvalModel extends AShapeModel {
	
	public AnOvalModel (Rectangle theBounds)
    {
		try {
        shapeModel = new shapes.OvalModel(theBounds);
        init();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	public AnOvalModel (int x, int y, int width, int height)
    {
		try {
        shapeModel = new shapes.OvalModel(x, y, width, height);
        init();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	public AnOvalModel ()
    {
		try {
        shapeModel = new shapes.OvalModel(); 
        init();
    }catch (Exception e) {
		e.printStackTrace();
	}
    }

}
