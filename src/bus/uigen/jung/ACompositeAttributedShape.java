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

import shapes.ShapeModel;

public class ACompositeAttributedShape implements Shape{
	
	List<ShapeModel> components;
	ShapeModel topMostShape;
	
	

	public ACompositeAttributedShape(List<ShapeModel> aComponents) {
		components = aComponents;
		topMostShape = aComponents.get(0);
		
	}
	
	public void initComponents(List<ShapeModel> aComponents) {
		components = aComponents;
		topMostShape = aComponents.get(0);
		
	}

	public ACompositeAttributedShape() {
	
		
	}

	@Override
	public Rectangle getBounds() {
		return topMostShape.getBounds();
	}

	@Override
	public Rectangle2D getBounds2D() {
		return topMostShape.getBounds2D();
	}

	@Override
	public boolean contains(double x, double y) {
		return topMostShape.contains(x, y);
	}

	@Override
	public boolean contains(Point2D p) {
		return topMostShape.contains(p);
	}

	@Override
	public boolean intersects(double x, double y, double w, double h) {
		return topMostShape.intersects(x, y, w, h);
	}

	@Override
	public boolean intersects(Rectangle2D r) {
		return topMostShape.intersects(r);
	}

	@Override
	public boolean contains(double x, double y, double w, double h) {
		return topMostShape.contains(x, y, w, h);
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
