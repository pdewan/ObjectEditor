package bus.uigen.jung;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.awt.geom.Ellipse2D;



import bus.uigen.shapes.AnOvalModel;
import shapes.OvalModel;
import shapes.ShapeModel;

public class ARingsCompositeShape extends ACompositeAttributedShape implements RingsCompositeShape{
	Shape prototypeShape;
	
	public ARingsCompositeShape( List<Color> aColors, Shape aPrototypeShape) {
		set(aColors, aPrototypeShape);
		
	}
	public ARingsCompositeShape( ) {
//		set(aColors, aPrototypeShape);
		
	}
	/* (non-Javadoc)
	 * @see bus.uigen.jung.RingsCompositeShape#set(java.util.List, java.awt.Shape)
	 */
	@Override
	public void set( List<Color> aColors, Shape aPrototypeShape) {
		prototypeShape = aPrototypeShape;
		int aNumRings = aColors.size();
		components = new ArrayList(aColors.size());
		try {
		for (int aRingNumber = 0; aRingNumber < aNumRings; aRingNumber++) {
				ShapeModel aRing = new OvalModel();
				aRing.setFilled(true);
				aRing.setColor(aColors.get(aRingNumber));
				aRing.setHeight((int) ringHeight(aRingNumber));
				aRing.setWidth((int) ringWidth(aRingNumber));
				aRing.setX(aRing.getWidth()/2);
				aRing.setY(aRing.getHeight()/2);
				components.add(aRing);
			
			
		}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setComponents(components);
		
	}
//	public void init(int aNumRings) {
//		prototypeShape = aPrototypeShape;
//		for (int aRingNumber = 0; aRingNumber < components.size(); aRingNumber++) {
//						
//		}
//	}
	double deltaWidth() {
		return prototypeShape.getBounds().getWidth()/components.size();
	}
	double deltaHeight() {
		return prototypeShape.getBounds().getWidth()/components.size();
	}
	
	double ringWidth (int aRingNumber) {
		return prototypeShape.getBounds().getWidth() - aRingNumber*deltaWidth();
	}
	
	double ringHeight (int aRingNumber) {
		return prototypeShape.getBounds().getHeight() - aRingNumber*deltaHeight();
	}
	
	


	
}
