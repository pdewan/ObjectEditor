package bus.uigen.shapes;

import java.awt.Shape;

import shapes.AttributedShape;

public interface AWTShape extends AttributedShape {

	Shape getShape();

	void setShape(Shape newValue);

}
