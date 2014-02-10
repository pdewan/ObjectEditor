package bus.uigen.shapes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Stroke;

import bus.uigen.ObjectEditor;
import shapes.BoundedShape;
import shapes.BoundedTextShape;
import shapes.FlexibleShape;
import shapes.FlexibleTextShape;
import util.annotations.IsCompositeShape;
import util.annotations.Position;
import util.annotations.Visible;
import util.misc.Common;

@IsCompositeShape(true)
public class AStringInAShape implements StringInAShape {
	public static final int DEFAULT_SHAPE_WIDTH = 20;
	public static final int DEFAULT_SHAPE_HEIGHT = 20;
	public static final int DEFAULT_X_MARGIN = 4;
	public static final double DEFAULT_WIDTH_MULTIPLIER = 2;
	public static final double DEFAULT_HEIGHT_MULTIPLIER = 1;


	public static final int DEFAULT_Y_MARGIN = 0;
	public static final int Y_CORRECTION = 3;

	
	int shapeWidth = DEFAULT_SHAPE_WIDTH;
	int shapeHeight = DEFAULT_SHAPE_HEIGHT;
	int xMargin = DEFAULT_X_MARGIN;
	int yMargin = DEFAULT_Y_MARGIN;
	double widthMultiplier= DEFAULT_WIDTH_MULTIPLIER;
	double heightMultiplier = DEFAULT_HEIGHT_MULTIPLIER;
	boolean dynamicDimension = false;
//	boolean dynamicDimension = true;

	int yCorrection = Y_CORRECTION;


	FlexibleShape enclosingShape;
	FlexibleTextShape stringShape;
	String string;
	public AStringInAShape() {
		enclosingShape = new ARectangleModel();
		stringShape = new AStringModel("");
		stringShape.setWidth((int) (widthMultiplier * Common.getDefaultFontStringWidth(stringShape.getText())));
	}
	public AStringInAShape(String aString, int anX, int aY, boolean aDynamicDimension) {
		dynamicDimension = aDynamicDimension;
		enclosingShape = new ARectangleModel (anX, aY, shapeWidth, shapeHeight);
//		displayedString = new AStringModel("" + aString, anX + shapeWidth/2 -2 , aY /*+ SHAPE_HEIGHT/2 + 4*/ );	
		stringShape = new AStringModel(aString);
//		stringShape.setFontSize(12);
		setDimension();

//		stringShape.setWidth(Common.getDefaultFontStringWidth(stringShape.getText()));
//		stringShape.setHeight(Common.getDefaultFontHeight());
//		enclosingShape.setWidth(shapeWidth);
//		enclosingShape.setHeight(shapeHeight);
//		positionString();

	}
	
	void setDimension() {
		if (dynamicDimension)
			setDynamicDimension();
		else
			setFixedDimension();
	}
	public AStringInAShape(FlexibleShape anEnclosingShape, String aString, boolean aDynamicDimension) {
		dynamicDimension = aDynamicDimension;
		enclosingShape = anEnclosingShape;	

//		displayedString = new AStringModel("" + aString, anX + shapeWidth/2 -2 , aY /*+ SHAPE_HEIGHT/2 + 4*/ );	
		stringShape = new AStringModel(aString);
//		stringShape.setFontSize(16);
		
		setDimension();

//		stringShape.setWidth(Common.getDefaultFontStringWidth(stringShape.getText()));
//		stringShape.setHeight(Common.getDefaultFontHeight());
//		enclosingShape.setWidth(shapeWidth);
//		enclosingShape.setHeight(shapeHeight);
//		positionString();

	}
	
	void positionString() {
		if (stringShape.getWidth() < enclosingShape.getWidth()) {
//		  displayedString.setX(enclosingShape.getX() + enclosingShape.getWidth()/2 -2);
		  stringShape.setCenterX(enclosingShape.getCenterX());
		} else {
			stringShape.setX(enclosingShape.getX());
		}
		stringShape.setCenterY(enclosingShape.getCenterY() - yCorrection);
//		stringShape.setY(enclosingShape.getY());

	}
	@Visible(false)
	public int getyCorrection() {
		return yCorrection;
	}
	@Visible(false)
	public void setyCorrection(int yCorrection) {
		this.yCorrection = yCorrection;
	}
	@Visible(false)
	public void setDynamicDimension() {
		int stringWidth = Common.getFontStringWidth(stringShape.getFont(), stringShape.getText(), stringShape.getFontSize());
		int stringHeight = Common.getFontHeight(stringShape.getFont(), stringShape.getFontSize());
		stringShape.setWidth(stringWidth);
		stringShape.setHeight(stringHeight);
//		stringShape.setWidth((int) (stringWidth*widthMultiplier));
//		stringShape.setHeight((int) (stringHeight *heightMultiplier));
		enclosingShape.setWidth(Math.max((stringWidth)  + xMargin, shapeWidth));
		enclosingShape.setHeight(Math.max((stringHeight) + yMargin, shapeHeight));

		positionString();
	}
	@Visible(false)
	public void setFixedDimension() {
		enclosingShape.setWidth(shapeWidth);
		enclosingShape.setHeight(shapeHeight);
		positionString();
	}

	@Override
	@Position(1)
	public BoundedShape getEnclosingShape() {
		return enclosingShape;
	}
	
	@Override
	@Position(0)

	public BoundedTextShape getStringShape() {
		return stringShape;
	}
	
	

	@Override
	@Visible(false)
	public void setWidth(int width) {
		enclosingShape.setWidth(width);
		if (width > xMargin) {
			stringShape.setWidth(width - xMargin);
		} else {
			stringShape.setWidth(0);
		}
			
		positionString();
		
	}

	@Override
	@Visible(false)
	public void setHeight(int height) {
		enclosingShape.setHeight(height);
		if (height > yMargin)
		stringShape.setHeight(height - yMargin);
		else
			stringShape.setHeight(0);

		positionString();
	}

	@Override
	@Visible(false)
	public int getHeight() {
		// TODO Auto-generated method stub
		return enclosingShape.getHeight();
	}

	@Override
	@Visible(false)
	public int getWidth() {
		return enclosingShape.getWidth();
	}

	@Override
	@Visible(false)
	public Rectangle getBounds() {
		return enclosingShape.getBounds();
	}

	@Override
	@Visible(false)
	public void setBounds(Rectangle newVal) {
//		enclosingShape.setBounds(newVal);
		setX(newVal.x);
		setY(newVal.y);
		setWidth(newVal.width);
		setHeight(newVal.height);
		
////		stringShape.setBounds(newVal);
//		if (newVal.height == 0 && newVal.width == 0) {
//			stringShape.setBounds(newVal);
//		}
		positionString();
	}

	@Override
	@Visible(false)
	public boolean copy(BoundedShape aReference) {
		if (!copyable(aReference))
			return false;
		boolean retVal = false;
		
//		if (aReference instanceof CharacterInAShape) {
			StringInAShape aReferenceStringInASpace = (StringInAShape) aReference;
			retVal = enclosingShape.copy(aReferenceStringInASpace.getEnclosingShape());
			if (!retVal)
				return false;
			return stringShape.copy(aReferenceStringInASpace.getStringShape());
			
//		}
	}

	@Override
	@Visible(false)
	public int getX() {
		return enclosingShape.getX();
	}

	@Override
	@Visible(false)
	public int getY() {
		// TODO Auto-generated method stub
		return enclosingShape.getY();
	}

	@Override
	@Visible(false)
	public void setX(int newVal) {
		enclosingShape.setX(newVal);
		
	}

	@Override
	@Visible(false)
	public void setY(int newVal) {
		enclosingShape.setY(newVal);
		positionString();		
	}
	@Override
	@Visible(false)
	public boolean copyable(BoundedShape aReference) {
		return aReference instanceof StringInAShape;
	}
	
	
	@Override
	@Visible(false)
	public Color getColor() {
		// TODO Auto-generated method stub
		return enclosingShape.getColor();
	}
	@Override
	@Visible(false)
	public void setColor(Color newVal) {
		enclosingShape.setColor(newVal);

		
	}
	@Override
	@Visible(false)
	public boolean isFilled() {
		// TODO Auto-generated method stub
		return enclosingShape.isFilled();
	}
	@Override
	@Visible(false)
	public void setFilled(boolean newVal) {
		enclosingShape.setFilled(newVal);

		
	}
	@Override
	@Visible(false)
	public Font getFont() {
		// TODO Auto-generated method stub
		return enclosingShape.getFont();
	}
	@Override
	@Visible(false)
	public void setFont(Font newVal) {
		enclosingShape.setFont(newVal);

		
	}
	@Override
	@Visible(false)
	public Stroke getStroke() {
		// TODO Auto-generated method stub
		return enclosingShape.getStroke();
	}
	@Override
	@Visible(false)
	public void setStroke(Stroke newVal) {
		enclosingShape.setStroke(newVal);
		
	}
	@Override
	@Visible(false)
	public Paint getPaint() {
		// TODO Auto-generated method stub
		return enclosingShape.getPaint();
	}

	public static void main (String[] args) {
		StringInAShape characterInShape = new AStringInAShape("200000", 50, 50, true);
//		ObjectEditor.edit(characterInShape.getEnclosingShape());
		ObjectEditor.edit(characterInShape);
		
	}
	@Override
	@Visible(false)
	public void setFontSize(int newSize) {
		// TODO Auto-generated method stub
		
	}
	@Override
	@Visible(false)
	public int getFontSize() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	@Visible(false)
	public String getText() {
		return stringShape.getText();
	}
	@Override
	@Visible(false)
	public void setText(String newVal) {
		stringShape.setText(newVal);
		setDimension();
	}
	@Visible(false)
	public int getxMargin() {
		return xMargin;
	}
	@Visible(false)
	public void setxMargin(int xMargin) {
		this.xMargin = xMargin;
	}
	@Visible(false)
	public int getyMargin() {
		return yMargin;
	}
	@Visible(false)
	public void setyMargin(int yMargin) {
		this.yMargin = yMargin;
	}
	@Override
	@Visible(false)

	public void setPaint(Paint newVal) {
		// TODO Auto-generated method stub
		
	}
	@Override
	@Visible(false)

	public boolean isRounded() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	@Visible(false)

	public void setRounded(boolean newVal) {
		// TODO Auto-generated method stub
		
	}
	@Override
	@Visible(false)

	public boolean is3D() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	@Visible(false)

	public void set3D(boolean newVal) {
		// TODO Auto-generated method stub
		
	}
	@Override
	@Visible(false)

	public int getZIndex() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	@Visible(false)

	public void setZIndex(int newVal) {
		// TODO Auto-generated method stub
		
	}
}
