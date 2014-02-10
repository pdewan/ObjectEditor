package bus.uigen.shapes;

import java.awt.BasicStroke;
import java.awt.Color;

import shapes.FlexibleShape;
import shapes.LabelShape;
import util.models.AListenableVector;

@util.annotations.StructurePattern(util.annotations.StructurePatternNames.BEAN_PATTERN)
public class ATimeLine implements TimeLine {
	AListenableVector<FlexibleShape> contents = new AListenableVector();
	final int PIXELS_IN_CHARACTER = 15;
	int scale = 100;
	String name = "";
	int labelYOffset = - 20;
	int lowX;
	int y;
	Color color;
	BasicStroke stroke;
	boolean isDashed;
	LabelShape label;
	public ATimeLine (String theName, int theLowX, int theScale, int theY, Color theColor, BasicStroke theStroke, boolean theIsDashed ) {

		setColor(theColor);
		setScale(theScale);
		setLowX(theLowX);
		setY(theY);
		setStroke(theStroke);
		setDashed(theIsDashed);

		setName(theName);
	}
	public ATimeLine (String theName, int theY, Color theColor) {
		setColor(theColor);
		setY(theY);
		setName(theName);
	}
	public ATimeLine (int theY, Color theColor) {
		setY(theY);
		setColor(theColor);
	}
	public ATimeLine() {
		
	}
	int getLabelYOffset() {
		return labelYOffset;
	}
	void createNameLabel() {
		label = new ALabelModel(name);
		label.setX(getLowX());
		label.setY(getY() + getLabelYOffset());	
		label.setColor(getColor());
	}
	
	public int getScale() {
		return scale;
	}
	public void setScale(int scale) {
		int prevScale = getScale();
		this.scale = scale;
		for (int i = 0; i < contents.size(); i++) {
			FlexibleShape element = contents.get(i);
			int currentX = toX(element.getX(), prevScale);
			//int currentWidth = element.getWidth()*prevScale;
			int currentWidth = element.getWidth()*100/prevScale;
			int newX = toActualX(currentX);
			element.setX(toActualX(currentX));
			//element.setWidth(currentWidth/getScale());
			element.setWidth(currentWidth*scale/100);
			
		}

		
	}
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		if (label == null)
			createNameLabel();
		this.name = name;
		label.setText(name);
		label.setWidth(name.length()*PIXELS_IN_CHARACTER);
		
	}
	public int getLowX() {
		return lowX;
	}
	public void setLowX(int theLowX) {
		int xIncrement = theLowX - lowX;
		moveLowX(xIncrement);	
		//this.lowX = theLowX;
	}
	@Override
	public void moveLowX(int xIncrement) {
		if (xIncrement == 0) return;
		for (int i = 0; i < contents.size(); i++) {
			contents.get(i).moveX(xIncrement);
		}
		if (label != null)
			label.moveX(xIncrement);
		this.lowX += xIncrement;
	}
	 Color getColor() {
		return color;
	}
	 void setColor(Color color) {
		this.color = color;
	}
	BasicStroke getStroke() {
		return stroke;
	}
	 void setStroke(BasicStroke stroke) {
		this.stroke = stroke;
	}
	
	boolean isDashed() {
		return isDashed;
	}
	void setDashed(boolean isDashed) {
		this.isDashed = isDashed;
	}
	int toActualX (int x) {
		//return  (x + getLowX())/getScale();
		//return  getLowX() + x/getScale();
		return  getLowX() + x*scale/100;
	}
	int toX(int actualX, int theScale) {
		//return theScale*actualX - getLowX();
		return (100*actualX)/theScale - getLowX();
	}
	int maxEnd = 0;
	@Override
	public int getMaxTime() {
		return maxEnd;
	}
	public void addSegment (int start, int end) {
		if (end <= start) return;
		maxEnd = Math.max(maxEnd, end);
		//Shape line = new ALineModel(toActualX(start), y, (end-start)/getScale(), 0);
		FlexibleShape line = new ALineModel(toActualX(start), y, (end-start) *scale/100, 0);
		if (getStroke() != null)
			line.setStroke(getStroke());
		else if (isDashed)
			line.setDashedStroke();
		else
			line.setSolidStroke();
		if (getColor() != null)
			line.setColor(getColor());
		contents.add(line);
		
	}
	public int getY() {
		return y;
	}
	public void setY(int theY) {
		int yIncrement = theY - y;
		moveY(yIncrement);
		
		//y = theY;
		
	}
	@Override
	public void moveY(int yIncrement) {
		if (yIncrement == 0) return;
		for (int i = 0; i < contents.size(); i++) {
			contents.get(i).moveY(yIncrement);
		}
		if (label != null)
			label.moveY(yIncrement);
		y += yIncrement;
		
	}
	
	public LabelShape getNameLabel() {
		return label;
		
	}
	
	public AListenableVector<FlexibleShape> getLines() {
		return contents;
	}
	
	public void removeFirst() {
		if (contents.size() > 0)
			contents.remove(0);
	}
	
	public static void main(String[] args) {
		TimeLine timeLine = new ATimeLine("Demo", 20, Color.BLUE);
		//timeLine.setDashed(true);
		//timeLine.setColor(Color.BLUE);
		timeLine.addSegment(0, 100);
		timeLine.addSegment(200, 300);
		bus.uigen.ObjectEditor.edit(timeLine);
	}
	

}
