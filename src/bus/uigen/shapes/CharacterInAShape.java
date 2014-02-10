package bus.uigen.shapes;

import shapes.BoundedShape;
import shapes.BoundedTextShape;


public interface CharacterInAShape extends BoundedShape {

	public BoundedShape getEnclosingShape();

	public BoundedTextShape getDisplayedChar();

}