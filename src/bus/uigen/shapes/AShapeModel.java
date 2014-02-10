package bus.uigen.shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.Observable;
import java.util.Vector;

//import shapes.ShapeModel;
//import util.AListenable;
import java.util.Observer;

import bus.uigen.misc.ShapeMouseClickListener;
import bus.uigen.widgets.events.VirtualMouseEvent;
import shapes.BoundedShape;
import shapes.RemoteShape;
import util.annotations.Visible;
import util.misc.Common;
import util.models.Listenable;
import util.undo.Listener;

//public abstract  class AShapeModel extends Observable implements OEShape, Listener/*, ShapeMouseClickListener*/, Serializable  {
public    class AShapeModel extends Observable implements OEShapeModel  {
	
//	final static float dash1[] = {10.0f};
//    transient BasicStroke dashed;
////    = new BasicStroke(1.0f, 
////            BasicStroke.CAP_BUTT, 
////            BasicStroke.JOIN_MITER, 
////            10.0f, dash1, 0.0f);
//    transient BasicStroke dotted;
////    = new BasicStroke(
////    	      1f, 
////    	      BasicStroke.CAP_ROUND, 
////    	      BasicStroke.JOIN_ROUND, 
////    	      1f, 
////    	      new float[] {2f}, 
////    	      0f);
//    transient BasicStroke solid;
//    = new BasicStroke(1f);
	shapes.ShapeModel shapeModel;
	@Visible(false)
	public Rectangle getBounds(){ try {
		return shapeModel.getBounds();
	} catch (Exception e) {e.printStackTrace(); return null;}}
	@Visible(false)
    public void setBounds(int x, int y, int width, int height){ try {
    	shapeModel.setBounds(x, y, width, height);
    } catch (Exception e) {e.printStackTrace();}}
	@Visible(false)
	public void setBounds(Rectangle r) { try {
		shapeModel.setBounds(r);
	} catch (Exception e) {e.printStackTrace();}}
	@Visible(false)
	public void setBounds(Point northWest, Point southEast){ try {
		shapeModel.setBounds(northWest, southEast);
	} catch (Exception e) {e.printStackTrace();}}
	@Visible(false)
	public void setSize(Dimension d){ try {
		shapeModel.setSize(d);
	} catch (Exception e) {e.printStackTrace();}}
	@Visible(false)
	public void setSize(int width, int height){ try {
		shapeModel.setSize(width, height);
	} catch (Exception e) {e.printStackTrace();}}	
	public void setWidth(int width){ try { 
		shapeModel.setWidth(width);
	} catch (Exception e) {e.printStackTrace();}}
	public void setHeight(int height){ try {
		shapeModel.setHeight(height);
	} catch (Exception e) {e.printStackTrace();}}
	@Visible(false)
	public Dimension getSize(){ try {
		return shapeModel.getSize();
	} catch (Exception e) {e.printStackTrace(); return null;}}
	public int getX() { try {
		return shapeModel.getX();
	} catch (Exception e) {e.printStackTrace(); return 0;}}
	public int getY() { try {
		return shapeModel.getY();
	} catch (Exception e) {e.printStackTrace(); return 0;}}
	public void setX(int newVal) { try { 
		 shapeModel.setX(newVal);
	} catch (Exception e) {e.printStackTrace();}}
	public void setY(int newVal) { try {
		shapeModel.setY(newVal);
	} catch (Exception e) {e.printStackTrace();}}
	@Visible(false)
	@Override
	public void moveX(int increment) {
		shapeModel.moveX(increment);
//		int oldX = shapeModel.getX();
//		shapeModel.setX(oldX + increment);
	}
	@Visible(false)
	@Override
	public void moveY(int increment) {
		shapeModel.moveY(increment);
//		int oldY = shapeModel.getY();
//		shapeModel.setY(oldY + increment);
	}
	@Visible(false)
	public void setPosition(Point p) { try {
		shapeModel.setPosition(p);
	} catch (Exception e) {e.printStackTrace();}}
	@Visible(false)
	public Point getPosition() { try {
		return shapeModel.getPosition();
	} catch (Exception e) {e.printStackTrace(); return null;}}
	public int getHeight() { try {
		return shapeModel.getHeight();
	} catch (Exception e) {e.printStackTrace(); return 0;}}
	public int getWidth() { try { 
		return shapeModel.getWidth();
	} catch (Exception e) {e.printStackTrace(); return 0;}}
	@Visible(false)
	public Point getNWCorner(){ try { 
		return shapeModel.getNWCorner();
	} catch (Exception e) {e.printStackTrace(); return null;}}
	@Visible(false)
	public void setNWCorner(int x, int y){ try {
		 shapeModel.setNWCorner(x, y);
	} catch (Exception e) {e.printStackTrace();}}
	@Visible(false)
	public void setNWCorner(Point p) { try {
		shapeModel.setNWCorner(p);
	} catch (Exception e) {e.printStackTrace();}}
	@Visible(false)
	public Point getCenter(){ try {
		return shapeModel.getCenter();
	} catch (Exception e) {e.printStackTrace(); return null;}}
	@Visible(false)
	public void setCenter(Point center){ try {
		shapeModel.setCenter(center);
	} catch (Exception e) {e.printStackTrace();}}
	@Visible(false)
	public void setCenter(int x, int y ) { try {
		shapeModel.setCenter(x, y);
		} catch (Exception e) {e.printStackTrace();}}	
	@Visible(false)
	public Point getNECorner() { try {
		return shapeModel.getNECorner();
		} catch (Exception e) {e.printStackTrace(); return null;}}
	@Visible(false)
	public void setNECorner(Point p) { try {
		shapeModel.setNECorner(p);
		} catch (Exception e) {e.printStackTrace();}}
	@Visible(false)
	public void setNECorner(int x, int y ){ try {shapeModel.setNECorner(x, y);
	} catch (Exception e) {e.printStackTrace();}}
	@Visible(false)
	public Point getSWCorner() { try {
		return shapeModel.getSWCorner();
		} catch (Exception e) {e.printStackTrace(); return null;}}
	@Visible(false)
	public void setSWCorner(Point p){ try {
		shapeModel.setSWCorner(p);
		} catch (Exception e) {e.printStackTrace();}}
	@Visible(false)
	public void setSWCorner(int x, int y ) { try {shapeModel.setSWCorner(x, y);
	} catch (Exception e) {e.printStackTrace();}}
	@Visible(false)
	public Point getSECorner(){ try {
		return shapeModel.getSECorner();
		} catch (Exception e) {e.printStackTrace(); return null;}}
	@Visible(false)
	public void setSECorner(Point p){ try {shapeModel.setSECorner(p); } catch (Exception e) {e.printStackTrace();}}
	@Visible(false)
	public void setSECorner(int x, int y ) { try {shapeModel.setSECorner(x, y); } catch (Exception e) {e.printStackTrace();}}
	@Visible(false)
	public Point getNorthEnd(){ try {
		return shapeModel.getNorthEnd();
		} catch (Exception e) {e.printStackTrace(); return null;}}
	@Visible(false)
	public void setNorthEnd(Point p){ try {
		shapeModel.setNorthEnd(p); } catch (Exception e) {e.printStackTrace();}}
	@Visible(false)
	public void setNorthEnd(int x, int y ) { try {shapeModel.setNorthEnd(x, y);
	} catch (Exception e) {e.printStackTrace();}}
	@Visible(false)
	public Point getSouthEnd(){ try {
		return shapeModel.getSouthEnd();
		} catch (Exception e) {e.printStackTrace(); return null;}}
	@Visible(false)
	public void setSouthEnd(Point p){ try {shapeModel.setSouthEnd(p);
	} catch (Exception e) {e.printStackTrace();}}
	@Visible(false)
	public void setSouthEnd(int x, int y ) { try {shapeModel.setSouthEnd(x, y);
	} catch (Exception e) {e.printStackTrace();}}
	@Visible(false)
	public Point getWestEnd(){ try { return shapeModel.getWestEnd();
	} catch (Exception e) {e.printStackTrace(); return null;}}
	@Visible(false)
	public void setWestEnd(Point p){ try {shapeModel.setWestEnd(p); } catch (Exception e) {e.printStackTrace();}}
	@Visible(false)
	public void setWestEnd(int x, int y ) { try {shapeModel.setWestEnd(x, y); } catch (Exception e) {e.printStackTrace();}}
	@Visible(false)
	public Point getEastEnd(){ try {
		return shapeModel.getEastEnd();
		} catch (Exception e) {e.printStackTrace(); return null;}}
	@Visible(false)
	public void setEastEnd(Point p){ try {shapeModel.setEastEnd(p);
	} catch (Exception e) {e.printStackTrace();}}
	@Visible(false)
	public void setEastEnd(int x, int y ) { try {shapeModel.setEastEnd(x, y);
	} catch (Exception e) {e.printStackTrace();}}	
	
	public Color getColor() { try {
		return shapeModel.getColor();
	} catch (Exception e) {e.printStackTrace(); return null;}}
	public void  setColor(Color newVal) { try {
		shapeModel.setColor(newVal); } catch (Exception e) {e.printStackTrace();}}
	public boolean isFilled() { try {
		return shapeModel.isFilled();
		} catch (Exception e) {e.printStackTrace(); return false;}}
	public void  setFilled(boolean newVal) { try {
		shapeModel.setFilled(newVal); } catch (Exception e) {e.printStackTrace();}}
	
	public Font getFont() { try {
		return shapeModel.getFont(); } catch (Exception e) {e.printStackTrace(); return null;}}
	public void  setFont(Font newVal) { try {
		shapeModel.setFont(newVal); } catch (Exception e) {e.printStackTrace();}}
	@Visible(false)
    public Object remoteClone() { try {
    	return shapeModel.remoteClone(); } catch (Exception e) {e.printStackTrace(); return null;}}
	@Visible(false) 
    public boolean contains(double x, double y) { try {
    	return shapeModel.contains(x, y); } catch (Exception e) {e.printStackTrace(); return false;}}
	@Visible(false)
	public boolean contains(double x, double y, double w, double h) { try {
    	return shapeModel.contains(x, y, w, h);
    	} catch (Exception e) {e.printStackTrace(); return false;}}
	@Visible(false)
	public boolean contains(Point2D p) { try {
	return shapeModel.contains(p);
	} catch (Exception e) {e.printStackTrace(); return false;}}
	@Visible(false)
public boolean contains(Rectangle2D r) { try {
	return shapeModel.contains(r);
	} catch (Exception e) {e.printStackTrace(); return false;}}
	@Visible(false)
public Rectangle2D getBounds2D() { try {
	return shapeModel.getBounds2D(); } catch (Exception e) {e.printStackTrace(); return null;}}
	@Visible(false)
	public PathIterator getPathIterator(AffineTransform at) { try {
	return shapeModel.getPathIterator(at); } catch (Exception e) {e.printStackTrace(); return null;}}
	@Visible(false)
	public PathIterator getPathIterator(AffineTransform at, double flatness) { try {
	 return shapeModel.getPathIterator(at, flatness);
	 } catch (Exception e) {e.printStackTrace(); return null;}}
	@Visible(false)
public boolean intersects(double x, double y, double w, double h) { try {
	return shapeModel.intersects(x, y, w, h);
	} catch (Exception e) {e.printStackTrace(); return false;}}
	@Visible(false)
public boolean intersects(Rectangle2D r) { try {
	return shapeModel.intersects(r);
	} catch (Exception e) {e.printStackTrace(); return false;}}
/*
private transient Vector<Observer> listenerList = new Vector();
public void addObserver(Observer listener)
{
    listenerList.addElement(listener);
}
public void removeObserver(Observer listener)
{
    listenerList.removeElement(listener);
}

public void notifyObservers(Object info)
{
	
	try {
    for (Enumeration elements = listenerList.elements();
       elements.hasMoreElements();)
    {
       Observer listener = (Observer) elements.nextElement();
       listener.update(this, info);
    }
	} catch (Exception e) {
		e.printStackTrace();
	}
	
}
*/
@Override
public int getZIndex()  {
	try {
	return shapeModel.getZIndex();
	} catch (Exception e) {
		return 0;
	}
}
public void setZIndex(int newValue) {
	try  {
	shapeModel.setZIndex(newValue);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
@Visible(false)
public void init() {
	try {
//		 dashed = new BasicStroke(1.0f, 
//		            BasicStroke.CAP_BUTT, 
//		            BasicStroke.JOIN_MITER, 
//		            10.0f, dash1, 0.0f);
//		   dotted = new BasicStroke(
//		    	      1f, 
//		    	      BasicStroke.CAP_ROUND, 
//		    	      BasicStroke.JOIN_ROUND, 
//		    	      1f, 
//		    	      new float[] {2f}, 
//		    	      0f);
//		     solid = new BasicStroke(1f);
//	shapeModel.setStroke(solid);
	shapeModel.addListener(this);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
@Visible(false)
public void update (Listenable listenable, Object info) throws RemoteException {
	setChanged();
	notifyObservers(info);
}
/*
public Object clone()
{
    AListenable listenableClone = null;
    try
    {
        listenableClone = (AListenable) super.clone();
        listenableClone.listenerList = new Vector();
    }
    catch (Exception e)
    {
        e.printStackTrace();
    }
    finally
    {
        return (listenableClone);
    }
}
*/
@Visible(false)
@Override
public Paint getPaint() {
	try {
		return shapeModel.getPaint();
		} catch (Exception e) {
			return null;
		}
}
@Visible(false)
@Override
public Stroke getStroke() {
	try {
		return shapeModel.getStroke();
		} catch (Exception e) {
			return null;
		}
}
@Visible(false)
@Override
public boolean is3D() {
	try {
		return shapeModel.is3D();
		} catch (Exception e) {
			return false;
		}
}
@Visible(false)
@Override
public boolean isRounded() {
	try {
		return shapeModel.isRounded();
		} catch (Exception e) {
			return false;
		}
}
@Override
public void set3D(boolean newVal) {
	try  {
		shapeModel.set3D(newVal);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
}
@Visible(false)
@Override
public void setPaint(Paint newVal) {
	try  {
		shapeModel.setPaint(newVal);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
}
@Visible(false)
@Override
public void setRounded(boolean newVal) {
	try  {
		shapeModel.setRounded(newVal);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
}
@Visible(false)
@Override
public void setStroke(Stroke newVal) {
	try  {
		shapeModel.setStroke(newVal);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
}
@Visible(false)
@Override
public void setDashedStroke() {
	shapeModel.setDashedStroke();
}
@Visible(false)
@Override
public void setDottedStroke() {
	shapeModel.setDottedStroke();
}
@Visible(false)
@Override
public void setSolidStroke() {
	shapeModel.setSolidStroke(); 
}
@Visible(false)
//int fontSize = -1;
public void setFontSize(int newSize) {
	try  {
		shapeModel.setFontSize(newSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
}
@Visible(false)
public int getFontSize() { try {
	return shapeModel.getFontSize(); } catch (Exception e) {e.printStackTrace(); return -1;}}

//public void mouseClicked(VirtualMouseEvent mouseEvent) {
//	
//};
@Visible(false)
public RemoteShape getRemoteShape() {
	return shapeModel;
}
@Visible(false)
public void initSerializedObject() {
	shapeModel.initSerializedObject();
	init();
}

//public void copy (Object aReference) {
//	
//	shapeModel.copy(((AShapeModel) aReference).shapeModel);
//}
@Visible(false)
public boolean copy (BoundedShape aReference) {
	if (!( this.getClass().isAssignableFrom(aReference.getClass())))
		return false;
	
//	shapeModel.copy(((ALabelModel) aReference).shapeModel);
  return shapeModel.copy((((AShapeModel) aReference).shapeModel));

//	((LabelModel) shapeModel).copy((LabelModel) ((ALabelModel) aReference).shapeModel);

}
@Visible(false)
public boolean equals(Object aReference) {
	if (aReference instanceof AShapeModel)
	return shapeModel.equals(((AShapeModel) aReference).shapeModel);
	else
		return false;
}
@Visible(false)
public String toString() {
	return shapeModel.toString();
}
@Visible(false)
@Override
public double getAngle() {
	return shapeModel.getAngle();
}
@Visible(false)
@Override
public void setAngle(double newAngle) {
	shapeModel.setAngle(newAngle);
	
}
@Visible(false)
@Override
public double getRadius() {
	// TODO Auto-generated method stub
	return shapeModel.getRadius();
}
@Visible(false)
@Override
public void setRadius(double newRadius) {
	shapeModel.setRadius(newRadius);
}
@Visible(false)
@Override
public double getMagnification() {
	return shapeModel.getMagnification();
}
@Visible(false)
@Override
public void setMagnification(double newVal) {
	shapeModel.setMagnification(newVal);
	
}
//@Override
//public void dispose() {
//	shapeModel.dispose();
//}
@Visible(false)
@Override
public boolean copyable(BoundedShape aReference) {
	return this.getClass() == aReference.getClass() && shapeModel.copyable(((AShapeModel) aReference).shapeModel);
}
@Override
@Visible(false)
public void setDisposed(boolean newVal) {
	shapeModel.setDisposed(newVal);
	setBounds (new Rectangle (0, 0, 0, 0));
}
@Override
@Visible(false)
public boolean getDisposed() {
	return shapeModel.getDisposed();
}
@Visible(false)
@Override
public int getCenterX() {
	return shapeModel.getCenterX();
}
@Visible(false)
@Override
public void setCenterX(int newVal) {
	shapeModel.setCenterX(newVal);
}
@Visible(false)
@Override
public int getCenterY() {
	return shapeModel.getCenterY();
}
@Visible(false)
@Override
public void setCenterY(int newVal) {
	shapeModel.setCenterY(newVal);
	
}
@Override
@Visible(false)

public void joinStartToEndOf(BoundedShape shape) {
	shapeModel.joinStartToEndOf(shape);
	
}
@Override
@Visible(false)

public void joinStartToStartOf(BoundedShape shape) {
	shapeModel.joinStartToStartOf(shape);

}
@Override
@Visible(false)

public void joinEndToEndOf(BoundedShape shape) {
	shapeModel.joinEndToEndOf(shape);

	
}
@Override
@Visible(false)

public void joinEndToStartOf(BoundedShape shape) {
	shapeModel.joinEndToStartOf(shape);

}

}
