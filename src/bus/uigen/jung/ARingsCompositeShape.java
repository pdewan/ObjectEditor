package bus.uigen.jung;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.net.ProtocolException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.awt.geom.Ellipse2D;




import bus.uigen.shapes.AnOvalModel;
import shapes.OvalModel;
import shapes.ShapeModel;

public class ARingsCompositeShape extends ACompositeAttributedShape implements RingsCompositeShape{
	Shape prototypeShape;
	int scale = 1;
	List<Color> colors;
	
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
		colors = aColors;
		try {
		for (int aRingNumber = 0; aRingNumber < aNumRings; aRingNumber++) {
				ShapeModel aRing = new OvalModel();
				components.add(aRing);
				aRing.setFilled(true);
				aRing.setColor(aColors.get(aRingNumber));
				aRing.setHeight((int) ringHeight(aRingNumber));
				aRing.setWidth((int) ringWidth(aRingNumber));
				aRing.setX(-aRing.getWidth()/2);
				aRing.setY(-aRing.getHeight()/2);
			
			
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
		return prototypeShape.getBounds().getWidth()/colors.size();
	}
	double deltaHeight() {
		return prototypeShape.getBounds().getHeight()/colors.size();
	}
	
	double ringWidth (int aRingNumber) {
		return scale * (prototypeShape.getBounds().getWidth() - aRingNumber*deltaWidth());
	}
	
	double ringHeight (int aRingNumber) {
		return scale * (prototypeShape.getBounds().getHeight() - aRingNumber*deltaHeight());
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
		return prototypeShape.contains(r);
	}
	@Override
	public PathIterator getPathIterator(AffineTransform at) {
		return prototypeShape.getPathIterator(at);
	}
	@Override
	public PathIterator getPathIterator(AffineTransform at, double flatness) {
		return prototypeShape.getPathIterator(at, flatness);
	}
	public Shape getPrototypeShape() {
		return prototypeShape;
	}
	public void setPrototypeShape(Shape prototypeShape) {
		this.prototypeShape = prototypeShape;
	}
	public int getScale() {
		return scale;
	}
	public void setScale(int scale) {
		this.scale = scale;
	}
	
	


	
}
