package bus.uigen.test.vehicle;

import java.awt.Color;
import java.util.Observable;

import util.annotations.Position;
import util.annotations.StructurePattern;
import util.annotations.StructurePatternNames;
import bus.uigen.ObjectEditor;

@StructurePattern(StructurePatternNames.BEAN_PATTERN)
//@PropertyNames({"FrontTire", "BackTire", "UpperDeck", "Body", "DoubleDecker", "TextDisplay"})
public class ABus extends Observable implements Bus {	
	Shape upperDeck = new ARectangle();
	Shape body = new ARectangle();
	Shape frontTire = new AnOval();
	Shape backTire = new AnOval();	
	int busWidth = 100;
	int busX = 150;
	int busY = 150;
	boolean doubleDecker = false;
	public static final Color BUS_COLOR = Color.red;
//	public static final Color TIRE_COLOR = Color.black;

	
	

	public static final double BODY_HEIGHT_WIDTH_RATIO = 0.4;
	public static final double TIRE_WIDTH_WIDTH_RATIO = 0.18;
	public static final double TIRE_BODY_END_OFFSET = 0.1;
	public static final int X_INCREMENT = 10;
	public static final double SCALE_INCREMENT = 1.3;
	
	
	public ABus() {
		body.setColor(Color.RED);
		upperDeck.setColor(Color.RED);
		upperDeck.setFilled(true);
		body.setFilled(true);
		frontTire.setFilled(true);
		backTire.setFilled(true);
		placeAndSizeComponents();		
	}
	
	
	public boolean isDoubleDecker() {
		return doubleDecker;
	}

	public void setDoubleDecker(boolean doubleDecker) {
		this.doubleDecker = doubleDecker;
		setChanged();
		notifyObservers();
	}
	
	public boolean preGetUpperDeck() {
		return doubleDecker;
	}
	
	public Shape getUpperDeck() {
		return upperDeck;
	}
	
	@Override
	public Shape getBody() {
		return body;
	}

	@Override
	public void setBody(Shape body) {
		this.body = body;
	}

	@Override
	@Position(0)
	public Shape getFrontTire() {
		return frontTire;
	}

	
	@Override
	public void setFrontTire(Shape frontTire) {
		this.frontTire = frontTire;
	}
	
//	public boolean preGetBackTire() {
//		return false;
//	}

	@Override
	@Position(1)
	public Shape getBackTire() {
		return backTire;
	}

	
	@Override
	public void setBackTire(Shape backTire) {
		this.backTire = backTire;
	}

	/* (non-Javadoc)
	 * @see bus.uigen.test.vehicle.Bus#getBusWidth()
	 */
	@Override
	public int getBusWidth() {
		return busWidth;
	}

	/* (non-Javadoc)
	 * @see bus.uigen.test.vehicle.Bus#setBusWidth(int)
	 */
	@Override
	public void setBusWidth(int busWidth) {
		this.busWidth = busWidth;
		placeAndSizeComponents();
	}

	/* (non-Javadoc)
	 * @see bus.uigen.test.vehicle.Bus#getBusX()
	 */
	@Override
	public int getBusX() {
		return busX;
	}

	/* (non-Javadoc)
	 * @see bus.uigen.test.vehicle.Bus#setBusX(int)
	 */
	@Override
	public void setBusX(int busX) {
		this.busX = busX;
		placeAndSizeComponents();
	}

	/* (non-Javadoc)
	 * @see bus.uigen.test.vehicle.Bus#getBusY()
	 */
	
	@Override
	public int getBusY() {
		return busY;
	}

	/* (non-Javadoc)
	 * @see bus.uigen.test.vehicle.Bus#setBusY(int)
	 */
	@Override
	public void setBusY(int busY) {
		this.busY = busY;		
		placeAndSizeComponents();

	}

	
	
//	public void moveRight() {
//		int oldX = getBusX();
//		setBusX(oldX+X_INCREMENT);
//	}
	
	/* (non-Javadoc)
	 * @see bus.uigen.test.vehicle.Bus#move(int)
	 */
	@Override
	public void move (int increment) {
		int oldX = getBusX();
		setBusX(oldX + increment);
	}
	
	/* (non-Javadoc)
	 * @see bus.uigen.test.vehicle.Bus#moveLeft()
	 */
	@Override
	public void moveLeft() {
		move(-X_INCREMENT);
	}
	
	/* (non-Javadoc)
	 * @see bus.uigen.test.vehicle.Bus#moveRight()
	 */
	@Override
	public void moveRight() {
		move(X_INCREMENT);
	}
	
	/* (non-Javadoc)
	 * @see bus.uigen.test.vehicle.Bus#magnify()
	 */
	@Override
	public void magnify() {
		magnify(SCALE_INCREMENT);
	}
	
	public void shrink() {
		magnify(1/SCALE_INCREMENT);
	}
	
	void magnify (double anAmount) {
		int oldWidth = getBusWidth();
		int newBusWidth = (int) (oldWidth * anAmount);
		setBusWidth(newBusWidth);
	}
	
//	public String getTextDisplay() {
//		return "Lower Deck X:" + body.getX() + 
//			    "Lower Deck Y:" + body.getY() + 
////			    "Lower Deck Width:" + body.getWidth() +
//			    "Lower Deck Height:" + body.getHeight() +
//			     "Front Wheel X:" + frontTire.getX() +
//			     "Front Wheel Y:" + frontTire.getY() +
//			    "Front Tire Width:" + frontTire.getWidth();
//	}
	
	public String getTextDisplay() {
		return getDeckInformation() + " " + getTireInformation();
	}
	public String getDeckInformation() {
		if (isDoubleDecker())
		  return
				"Lower Deck:" + body.toString() + " Upper Deck:" + upperDeck.toString();
//				"Size:" + "(" + body.getWidth() + "," + body.getHeight() + ")\n" +
//				"Upper Deck Coordinates:" + "(" + body.getX() + "," + body.getY() + ")\n" +
//				"Upper Deck Size:" + "(" + body.getWidth() + "," + body.getHeight() + ")\n";
		else return "Body:" + body.toString();
			
	}
	public String getTireInformation() {
		return
				"Front Wheel:" + frontTire.toString() + " Back Wheel:" + backTire.toString();
//				"Size:" + "(" + body.getWidth() + "," + body.getHeight() + ")\n" +
//				"Upper Deck Coordinates:" + "(" + body.getX() + "," + body.getY() + ")\n" +
//				"Upper Deck Size:" + "(" + body.getWidth() + "," + body.getHeight() + ")\n";
	}
	
//	public String getTireInformation() {
//		return 	 "Front Wheel X:" + frontTire.getX() +
//			     "Front Wheel Y:" + frontTire.getY() +
//			    "Front Tire Width:" + frontTire.getWidth();
//	}
//	public String getTireInformation() {
//		return
//				"Lower:" + body.toString() + " Upper:" + upperDeck.toString();
////				"Size:" + "(" + body.getWidth() + "," + body.getHeight() + ")\n" +
////				"Upper Deck Coordinates:" + "(" + body.getX() + "," + body.getY() + ")\n" +
////				"Upper Deck Size:" + "(" + body.getWidth() + "," + body.getHeight() + ")\n";
//	}
	
	void placeAndSizeComponents() {	
		// size
		body.setWidth(busWidth);
		body.setHeight((int) (body.getWidth()*BODY_HEIGHT_WIDTH_RATIO));
		upperDeck.setWidth(busWidth);
		upperDeck.setHeight(body.getHeight());		
		frontTire.setWidth((int) (busWidth*TIRE_WIDTH_WIDTH_RATIO));
		frontTire.setHeight(frontTire.getWidth());
		backTire.setWidth(frontTire.getWidth());
		backTire.setHeight(backTire.getWidth());
		//place
		body.setX(busX);
		body.setY(busY);
		upperDeck.setX(busX);
		upperDeck.setY(busY - body.getHeight());
		frontTire.setX((int) (body.getX() + busWidth*TIRE_BODY_END_OFFSET) );
		backTire.setX((int) (body.getX() + body.getWidth() - busWidth*TIRE_BODY_END_OFFSET - backTire.getWidth()));
		frontTire.setY(body.getY() + body.getHeight() - frontTire.getHeight()/2);	
		backTire.setY(frontTire.getY());
		setChanged();
		notifyObservers(this);
	
	}
	
	public static void main (String[] args) {
		ObjectEditor.drawEdit(new ABus());
	};

}
