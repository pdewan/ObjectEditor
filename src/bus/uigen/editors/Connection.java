package bus.uigen.editors;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Vector;
import java.awt.Point;
import java.awt.Dimension;
import java.util.Enumeration;
import java.awt.Rectangle;
import slm.SLModel;
import slm.SLPutCommand;
import slm.SLSetBoundsCommand;
import shapes.LineModel;
import shapes.OvalModel;
import shapes.RemoteShape;
import shapes.ShapeModel;
import util.models.AListenable;
import util.models.Listenable;
import util.undo.Listener;
public class Connection  extends AListenable implements Listener {
	public final static int NORTH = 0;
	public final static int EAST = 1;
	public final static int SOUTH = 2;
	public final static int WEST = 3;
	//int destDirection;
	//ShapeModel source, destination; 
	RemoteShape source, destination; 
	LineModel line;
	String sourceKey, destinationKey, lineKey;
	Rectangle lastSourceBounds = new Rectangle();
	Rectangle lastDestinationBounds = new Rectangle();
	Rectangle lastLineBounds = new Rectangle();
	//LineModel lastLine;
	SLModel drawing;
	
	public Connection () throws RemoteException {
	}
	public Connection (SLModel theSLModel) throws RemoteException{
		setDrawing( theSLModel);
	}
	public String getSource () {
		return sourceKey;
	}
	public String getDestination () {
		return destinationKey;
	}
	public String getLine () {
		return lineKey;
	}
	public void setDrawing(SLModel theSLModel) {
		drawing = theSLModel;
		drawing.addListener(this);
	}
	//public void setSource (String theKey, ShapeModel theSource) {
	public void setSource (String theKey, RemoteShape theSource) {
		int xOffset = 0; 
		int yOffset = 0;
		try {
		if (theSource == null) return;
		System.err.println("set source");
		if (source != null && source != theSource) 
			source.removeListener(this);
		if (source != null) {
			//xOffset = theSource.getBounds().x - source.getBounds().x;
			xOffset = theSource.getBounds().x - lastSourceBounds.x;
		    yOffset = theSource.getBounds().y - lastSourceBounds.y;
		}		
		if (source != theSource) {
		   sourceKey = theKey;
		   source = theSource;		
			source.addListener(this);
		}
		//System.err.println("changing lastSource Bounds" + lastSourceBounds);
		//Rectangle sourceBounds = source.getBounds();
		//lastSourceBounds = theSource.getBounds();
		lastSourceBounds = new Rectangle(source.getBounds());		
		//System.err.println("changing lastSource Bounds" + lastSourceBounds);
		//System.err.println ("lineOffset" + lineOffsetChosen);
		/*
		if (lineOffsetChosen)
		  System.err.println("cur dir" + direction + "new dir" + getDirection());
		*/
		//System.err.println("making connection");
		if (line != null && direction == getDirection()) {
			
			moveDestination ( xOffset, yOffset);
			moveLine (xOffset, yOffset);
		} else if (line != null)
			makeConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	public void move(RemoteShape shapeModel, int xOffset, int yOffset) {
		//System.err.println("moving" + shapeModel + "x" + xOffset + "y" + yOffset);
		try {
		Rectangle bounds = shapeModel.getBounds();
		shapeModel.setBounds(bounds.x + xOffset, bounds.y + yOffset, bounds.width, bounds.height);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void moveSource(int xOffset, int yOffset) {
		lastSourceBounds.x += xOffset;
		lastSourceBounds.y += yOffset;
		move (source,  xOffset, yOffset);
	}
	public void moveDestination(int xOffset, int yOffset) {
		lastDestinationBounds.x += xOffset;
		lastDestinationBounds.y += yOffset;
		move (destination, xOffset, yOffset);
	}
	public void moveLine(int xOffset, int yOffset) {
		lastLineBounds.x += xOffset;
		lastLineBounds.y += yOffset;
		move (line, xOffset, yOffset);
	}
	
	
	public void setSource (String theKey) {
		setSource(theKey,  drawing.get(theKey));
	}
	//public void setDestination (String theKey, ShapeModel theShape) {
	public void setDestination (String theKey, RemoteShape theShape) {
		try {
		int xOffset = 0;
		int yOffset = 0; 
		if (destination != null && destination != theShape) {
			destination.removeListener(this);
		}
		if (destination != null) {
			//xOffset = theShape.getBounds().x - destination.getBounds().x;
		    //yOffset = theShape.getBounds().y - destination.getBounds().y; 
			xOffset = theShape.getBounds().x - lastDestinationBounds.x;
		    yOffset = theShape.getBounds().y - lastDestinationBounds.y; 
		}
		destinationKey = theKey;
		destination = theShape;
		destination.addListener(this);
		lastDestinationBounds = new Rectangle (destination.getBounds());
		if (line != null && direction == getDirection()) {
			moveSource ( xOffset, yOffset);
			moveLine (xOffset, yOffset);
		} else if (line != null)
			makeConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	boolean firstLine;
	public void setLine (String theKey, LineModel lineModel) {
		System.err.println("setting line" + lineModel);
		//if (lineModel == line) return;
		if (line != null && line != lineModel)
			line.removeListener(this);
		firstLine = (line ==null);
		line = lineModel;
		lastLineBounds = new Rectangle (line.getBounds());
		drawing.put(lineKey, lineModel);
		if (line != null)
			line.addListener(this);
		//if (moveConnection(lineModel))
		if (!firstLine)
		  makeConnectionFromLine();
	}
	public void setDestination (String theKey) {
		setDestination (theKey,drawing.get(theKey));
	}
	public void setConnectionName (String newVal) {
		lineKey = newVal;
	}
	public void makeConnection(String theSourceKey, String theDestinationKey, String theConnectionName) {
		setSource(theSourceKey);
		setDestination (theDestinationKey);
		setConnectionName(theConnectionName);
		makeConnection();
	}
	public void makeConnection(String theSourceKey, RemoteShape theSourceModel,  
							   String theDestinationKey, RemoteShape theDestinationModel, String theConnectionName) {
		setSource(theSourceKey, theSourceModel);
		setDestination(theDestinationKey, theDestinationModel);
		setConnectionName(theConnectionName);
		makeConnection();
	}
	public static Point getNorth(Rectangle r) {
		return new Point (r.x + r.width/2, r.y);		
	}
	public static Point getSouth(Rectangle r) {
		return new Point (r.x + r.width/2, r.y + r.height);		
	}
	public static Point getEast(Rectangle r) {
		return new Point (r.x + r.width, r.y + r.height/2);
	}
	public static Point getWest (Rectangle r) {
		return new Point (r.x, r.y + r.height/2);
	}
	public int getDirection() {
		try {
		Point sourceSouth = getSouth(source.getBounds());
		Point destinationNorth = getNorth(destination.getBounds());
		if (sourceSouth.y <destinationNorth.y) return SOUTH;
		
		Point sourceNorth = getNorth(source.getBounds());
		Point destinationSouth = getSouth(destination.getBounds());
		if (sourceNorth.y > destinationSouth.y) return NORTH;
		
		Point sourceEast = getEast(source.getBounds());
		Point destinationWest = getWest(destination.getBounds());
		if (sourceEast.x < destinationWest.x) return EAST;
		
		Point sourceWest = getWest(source.getBounds());
		Point destinationEast = getEast(destination.getBounds());
		if (sourceWest.x > destinationWest.x) return WEST;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return -1;	
		
	}
	
	int direction = -1;
	public void makeConnection() {
		try {
		System.err.println("MAKING NEW CONNECTION");
		direction = getDirection();
		switch (direction) {
		case SOUTH:
			createLine(getSouth(source.getBounds()), getNorth(destination.getBounds()));
			break;
		case NORTH:
			createLine(getNorth(source.getBounds()), getSouth(destination.getBounds()));
			break;
		case EAST:
			createLine(getEast(source.getBounds()), getWest(destination.getBounds()));
			break;
		case WEST:
			createLine(getWest(source.getBounds()), getEast(destination.getBounds()));
			break;
		default:
			;
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void createLine(Point sourcePoint, Point destinationPoint) {
		try {
		Rectangle bounds = new Rectangle (sourcePoint.x, sourcePoint.y,
										  destinationPoint.x - sourcePoint.x,
										  destinationPoint.y - sourcePoint.y);
		LineModel lineModel = new LineModel(bounds);
		setLine(lineKey, lineModel);
		} catch (Exception e) {
			e.printStackTrace();
			//System.err.println(e);
		}
	}
	/*
	public void lineChanged() {
		direction = getDirectionFromLine();		
		makeConnectionFromLine();
	}
	*/
	static int DELTA = 5;
	boolean autoLinePlacement = true;
	public static boolean alignedTop (Point r1, Rectangle r2){
		return (Math.abs (r1.y - r2.y) < DELTA);
	}
	public static boolean alignedLeft (Point r1, Rectangle r2){
		return (Math.abs (r1.x - r2.x) < DELTA);
	}
	public static boolean onNorthEdge (Point p, Rectangle r) {
		return ((Math.abs(p.y - r.y) < DELTA) &&
				 (p.x >= r.x) && (p.x <= r.x + r.width));
	}
	public static boolean onSouthEdge (Point p, Rectangle r) {
		return ((Math.abs(p.y - (r.y + r.height)) < DELTA) &&
				 (p.x >= r.x) && (p.x <= r.x + r.width));
	}
	public static boolean onWestEdge (Point p, Rectangle r) {
		return ((Math.abs(p.x - r.x) < DELTA) &&
				 (p.y >= r.y) && (p.y <= r.y + r.height));
	}
	public static boolean onEastEdge (Point p, Rectangle r) {
		return ((Math.abs(p.x - (r.x + r.width)) < DELTA) &&
				 (p.y >= r.y) && (p.y <= r.y + r.height));
	}
	public static boolean inside(Point p, Rectangle r) {
		return (p.x > (r.x - DELTA) && p.x < r.x + (r.width + DELTA) &&
			    p.y > (r.y - DELTA) && p.y < r.y + (r.height + DELTA));
	}
	public boolean moveConnection() {
		try {
		Rectangle  newBounds = line.getBounds();
		//Rectangle  newBounds = newLine.getBounds();
		Point newLineSource = new Point(newBounds.x, newBounds.y);
		Point newLineDestination = new Point(newBounds.x + newBounds.width,
											 newBounds.y + newBounds.height);
		Rectangle sourceBounds = source.getBounds();
		Rectangle destinationBounds = destination.getBounds();
		switch (direction) {
		case NORTH:
			if (onNorthEdge(newLineSource, sourceBounds) && 
				onSouthEdge(newLineDestination, destinationBounds))
				return false;
			else
				return true;
		case SOUTH:
			if (onSouthEdge(newLineSource, sourceBounds) && 
				onNorthEdge(newLineDestination, destinationBounds))
				return false;
			else
				return true;
		case EAST:
			if (onEastEdge(newLineSource, sourceBounds) && 
				onWestEdge(newLineDestination, destinationBounds))
				return false;
			else
				return true;
		case WEST:					
			if (onWestEdge(newLineSource, sourceBounds) && 
				onEastEdge(newLineDestination, destinationBounds))
				return false;
			else
				return true;
		default:
			return true;
		}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
			
	}
	public int getDirectionFromLine() {
		Rectangle lineBounds = line.getBounds();
		if (lineBounds.height > 0) return SOUTH;
		if (lineBounds.height < 0) return NORTH;
		if (lineBounds.width > 0) return EAST;
		if (lineBounds.width < 0) return WEST;
		return -1;			
	}
	public Point originFromNorth(Point north, Rectangle r) {
		return new Point (north.x - r.width/2, north.y);		
	}
	public Point originFromSouth(Point south, Rectangle r) {
		return new Point (south.x - r.width/2, south.y - r.height);		
	}
	public Point originFromEast(Point east, Rectangle r) {
		return new Point (east.x - r.width, east.y - r.height/2);		
	}
	public Point originFromWest(Point west, Rectangle r) {
		return new Point (west.x , west.y - r.height/2);		
	}
	boolean lineOffsetChosen = false;
	public void makeConnectionFromLine() {
		try {
		if (!moveConnection()) {
			lineOffsetChosen = true;
			return;
		}
		System.err.println("moveConnection");
		direction = getDirectionFromLine();
		Rectangle lineBounds = line.getBounds();
		Rectangle sourceBounds = source.getBounds();
		Rectangle destinationBounds = destination.getBounds();
		Point lineStart = new Point(lineBounds.x, lineBounds.y);
		Point lineEnd = new Point(lineBounds.x + lineBounds.width, lineBounds.y + lineBounds.height);
		Point sourceOrigin, destinationOrigin;
		switch (direction) {
		case SOUTH:
			sourceOrigin = originFromSouth(lineStart, sourceBounds);
			destinationOrigin = originFromNorth(lineEnd, destinationBounds);
			break;
		case NORTH:
			sourceOrigin = originFromNorth(lineStart, sourceBounds);
			destinationOrigin = originFromSouth(lineEnd, destinationBounds);
			break;
		case EAST:
			sourceOrigin = originFromEast(lineStart, sourceBounds);
			destinationOrigin = originFromWest(lineEnd, destinationBounds);
			break;
		case WEST:
			sourceOrigin = originFromWest(lineStart, sourceBounds);
			destinationOrigin = originFromEast(lineEnd, destinationBounds);
			break;
		default:
			return;
		}		
		Dimension sourceDimension = new Dimension(sourceBounds.width, sourceBounds.height);
		lastSourceBounds = new Rectangle(sourceOrigin, sourceDimension);
		source.setBounds(sourceOrigin.x, sourceOrigin.y, sourceDimension.width, sourceDimension.height);
		Dimension destinationDimension = new Dimension(destinationBounds.width, destinationBounds.height);		
		lastDestinationBounds = new Rectangle(destinationOrigin, destinationDimension);
		destination.setBounds(destinationOrigin.x, destinationOrigin.y, destinationDimension.width, destinationDimension.height);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void update(Listenable model, Object arg)
    { 
		RemoteShape newShape;
		//System.err.println("update" + model);
		if (model instanceof SLModel) update((SLModel) model, arg);
		if (model instanceof RemoteShape) update((RemoteShape) model, arg);
		/*
		if (arg == null) return;
		SLChange change = (SLChange) arg;
		String changeKey = change.getKey();
		if (changeKey == null) return;
		if (changeKey.equals(sourceKey)) {
			newShape = drawing.get(sourceKey);
			if (source == newShape) return;
			source = newShape;
		} else if (changeKey.equals(destinationKey)) {
			newShape = drawing.get(destinationKey);
			if (destination == newShape) return;
			destination = newShape;
		} else if (changeKey.equals(lineKey)) {
			line = (LineModel) drawing.get(lineKey);
			lineChanged();
			return;
		} else return;
		//drawing.remove(lineKey);
		makeConnection();
		*/
	
    }
	public void update(SLModel model, Object arg)
    { 
		RemoteShape newShape;
		//System.err.println("update");
		if (arg == null) return;
		if (arg instanceof SLPutCommand) putUpdate((SLPutCommand) arg);
		//if (arg instanceof SLSetBoundsCommand) setBoundsUpdate((SLSetBoundsCommand) arg);
		/*
		if (changeKey == null) return;
		if (changeKey.equals(sourceKey)) {
			newShape = drawing.get(sourceKey);
			if (source == newShape) return;
			setSource (sourceKey,  newShape);
		} else if (changeKey.equals(destinationKey)) {
			newShape = drawing.get(destinationKey);
			if (destination == newShape) return;
			setDestination(destinationKey, newShape);
		} else if (changeKey.equals(lineKey)) {
			newShape = drawing.get(lineKey);
			if (newShape == line) return;
			setLine (lineKey, (LineModel) newShape);
			lineChanged();
			return;
		} else return;
		//drawing.remove(lineKey);
		makeConnection();	
		*/
	
    }
	
	public void putUpdate(SLPutCommand command)
    { 
		RemoteShape newShape;
		//System.err.println(command);
		String changeKey = command.getKey();
		if (changeKey == null) return;
		processChange(changeKey, drawing.get(changeKey));
		/*
		if (changeKey.equals(sourceKey)) {
			newShape = drawing.get(sourceKey);
			if (source == newShape) return;
			setSource (sourceKey,  newShape);
		} else if (changeKey.equals(destinationKey)) {
			newShape = drawing.get(destinationKey);
			if (destination == newShape) return;
			setDestination(destinationKey, newShape);
		} else if (changeKey.equals(lineKey)) {
			newShape = drawing.get(lineKey);
			if (newShape == line) return;
			setLine (lineKey, (LineModel) newShape);
			//lineChanged();
			return;
		} else return;
		//drawing.remove(lineKey);
		//if (moveConnection())
		   //makeConnection();	
		*/
	
    }
	
	public void setBoundsUpdate(SLSetBoundsCommand command)
    { 
		RemoteShape newShape;
		System.err.println(command);
		String changeKey = command.getKey();
		if (changeKey == null) return;
		processChange(changeKey, drawing.get(changeKey));
		/*
		if (changeKey.equals(sourceKey)) {
			newShape = drawing.get(sourceKey);
			if (lastSourceBounds.equals(newShape.getBounds())) return;
			System.err.println("new bounds");
			setSource (sourceKey,  newShape);
		} else if (changeKey.equals(destinationKey)) {
			newShape = drawing.get(destinationKey);
			if (lastDestinationBounds.equals(newShape.getBounds())) return;
			setDestination(destinationKey, newShape);
		} else if (changeKey.equals(lineKey)) {
			newShape = drawing.get(lineKey);
			if (lastLineBounds.equals(newShape.getBounds())) return;
			setLine (lineKey, (LineModel) newShape);
			//lineChanged();
			return;
		} else return;
		//drawing.remove(lineKey);
		//if (moveConnection())
		   //makeConnection();	
		*/
	
    }
	
	public void processChange(String changeKey, RemoteShape newShape)
    {
		try {
		//System.err.println("process change!!" + newShape.setBounds());
		if (sourceKey.equals(changeKey) || (newShape == source)) {
			//newShape = drawing.get(sourceKey);
			//System.err.println("last bounds" + lastSourceBounds);
			//System.err.println("new bounds" + newShape.getBounds());
			if (lastSourceBounds.equals(newShape.getBounds())) return;
			//System.err.println("new bounds!!!!");
			setSource (sourceKey,  newShape);
		} else if (destinationKey.equals(changeKey) || (newShape == destination)) {
			//newShape = drawing.get(destinationKey);
			if (lastDestinationBounds.equals(newShape.getBounds())) return;
			setDestination(destinationKey, newShape);
		} else if (lineKey.equals(changeKey) || (newShape == line)) {
			//newShape = drawing.get(lineKey);
			if (lastLineBounds.equals(newShape.getBounds())) return;
			setLine (lineKey, (LineModel) newShape);
			//lineChanged();
			return;
		} else return;
		} catch (Exception e) {
			e.printStackTrace();
		}
	
    }
	
	
	public void update(RemoteShape model, Object arg)
    { 
		processChange(null, model);
		/*
		//System.err.println("udate" + model);
		if ((model == source) || (model == destination)) {
			//System.err.println("
			if (moveConnection())
				makeConnection();
		} else if (model == line) {
			makeConnectionFromLine();
		}
		*/
		
    }
	/*
	Vector listeners = new Vector();
	public void addListener (Listener theListener) {
		listeners.addElement(turtleListener);
	}
	// Tells the listeners to update themselves
	void notifyListeners() {
		Enumeration elements = listeners.elements();
		while (elements.hasMoreElements())
			((TurtleListener) elements.nextElement()).turtleUpdated();
	}
	*/
}