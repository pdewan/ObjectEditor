package bus.uigen.shapes;

import java.awt.Rectangle;
import java.rmi.RemoteException;

//import shapes.shapes.PointModel;
@util.annotations.StructurePattern(util.annotations.StructurePatternNames.POINT_PATTERN)
public class APointModel extends AShapeModel {
	
	public APointModel (int initX, int initY )
    {
		try {
        shapeModel = new shapes.PointModel(initX, initY);
        init();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	

}
