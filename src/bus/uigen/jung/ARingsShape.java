package bus.uigen.jung;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.awt.geom.Ellipse2D;

public class ARingsShape implements Shape{
	
	List<Ellipse2D> rings;
	Shape prototypeShape;
	

	public ARingsShape(Shape aPrototypeShape, int aNumRings) {
		rings = new ArrayList(aNumRings);
		for (int aRingNumber = 0; aRingNumber < aNumRings; aRingNumber++) {
			rings.set(aRingNumber,  new Ellipse2D.Float());			
		}
	}
	double deltaWidth() {
		return prototypeShape.getBounds().getWidth()/rings.size();
	}
	double deltaHeight() {
		return prototypeShape.getBounds().getWidth()/rings.size();
	}
	
	double ringWidth (int aRingNumber) {
		return prototypeShape.getBounds().getWidth() - aRingNumber*deltaWidth();
	}
	
	double ringHeight (int aRingNumber) {
		return prototypeShape.getBounds().getHeight() - aRingNumber*deltaHeight();
	}
	
	public void setPrototypeShape(Shape aPrototypeShape) {
		prototypeShape = aPrototypeShape;
		for (int aRingNumber = 0; aRingNumber < rings.size(); aRingNumber++) {
						
		}
	}


	@Override
	public Rectangle getBounds() {
		return prototypeShape.getBounds();
	}

	@Override
	public Rectangle2D getBounds2D() {
		return prototypeShape.getBounds2D();
	}

	@Override
	public boolean contains(double x, double y) {
		return prototypeShape.contains(x, y);
	}

	@Override
	public boolean contains(Point2D p) {
		return prototypeShape.contains(p);
	}

	@Override
	public boolean intersects(double x, double y, double w, double h) {
		return prototypeShape.intersects(x, y, w, h);
	}

	@Override
	public boolean intersects(Rectangle2D r) {
		return prototypeShape.intersects(r);
	}

	@Override
	public boolean contains(double x, double y, double w, double h) {
		return prototypeShape.contains(x, y, w, h);
	}

	@Override
	public boolean contains(Rectangle2D r) {
		return false;
	}

	@Override
	public PathIterator getPathIterator(AffineTransform at) {
		return null;
	}

	@Override
	public PathIterator getPathIterator(AffineTransform at, double flatness) {
		// TODO Auto-generated method stub
		return null;
	}

}
