package bus.uigen.shapes;

import java.awt.Rectangle;

import bus.uigen.ObjectEditor;
import shapes.BoundedShape;
import shapes.BoundedTextShape;


public class ACharacterInAShape implements CharacterInAShape {
	public static final int SHAPE_WIDTH = 20;
	public static final int SHAPE_HEIGHT = 20;


	BoundedShape enclosingShape;
	BoundedTextShape displayedChar;
	char c;
	public ACharacterInAShape() {
		enclosingShape = new ARectangleModel();
		displayedChar = new AStringModel("");
	}
	public ACharacterInAShape(char aChar, int anX, int aY) {
		enclosingShape = new ARectangleModel (anX, aY, SHAPE_WIDTH, SHAPE_HEIGHT);
		displayedChar = new AStringModel("" + aChar, anX + SHAPE_WIDTH/2 -2 , aY /*+ SHAPE_HEIGHT/2 + 4*/ );		
	}
	
	void centerCharacter() {
		displayedChar.setX(enclosingShape.getX() + SHAPE_WIDTH/2 -2);
		displayedChar.setY(enclosingShape.getY());
	}

	@Override
	public BoundedShape getEnclosingShape() {
		return enclosingShape;
	}
	
	@Override
	public BoundedTextShape getDisplayedChar() {
		return displayedChar;
	}
	
	public static void main (String[] args) {
		CharacterInAShape characterInShape = new ACharacterInAShape('c', 50, 50);
//		ObjectEditor.edit(characterInShape.getEnclosingShape());
		ObjectEditor.edit(characterInShape);
		
	}

	@Override
	public void setWidth(int width) {
		enclosingShape.setWidth(width);
		centerCharacter();
		
	}

	@Override
	public void setHeight(int height) {
		enclosingShape.setHeight(height);
		centerCharacter();
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return enclosingShape.getHeight();
	}

	@Override
	public int getWidth() {
		return enclosingShape.getWidth();
	}

	@Override
	public Rectangle getBounds() {
		return enclosingShape.getBounds();
	}

	@Override
	public void setBounds(Rectangle newVal) {
		enclosingShape.setBounds(newVal);
		if (newVal.height == 0 && newVal.width == 0) {
			displayedChar.setBounds(newVal);
		}
		centerCharacter();
	}

	@Override
	public boolean copy(BoundedShape aReference) {
		if (!copyable(aReference))
			return false;
		boolean retVal = false;
		
//		if (aReference instanceof CharacterInAShape) {
			CharacterInAShape aReferenceCharacterInASpace = (CharacterInAShape) aReference;
			retVal = enclosingShape.copy(aReferenceCharacterInASpace.getEnclosingShape());
			if (!retVal)
				return false;
			return displayedChar.copy(aReferenceCharacterInASpace.getDisplayedChar());
			
//		}
	}

	@Override
	public int getX() {
		return enclosingShape.getX();
	}

	@Override
	public int getY() {
		// TODO Auto-generated method stub
		return enclosingShape.getY();
	}

	@Override
	public void setX(int newVal) {
		enclosingShape.setX(newVal);
		
	}

	@Override
	public void setY(int newVal) {
		enclosingShape.setY(newVal);
		centerCharacter();		
	}
	@Override
	public boolean copyable(BoundedShape aReference) {
		return aReference instanceof CharacterInAShape;
	}


}
