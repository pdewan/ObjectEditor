package bus.uigen.jung;

import java.awt.Color;
import java.awt.Shape;
import java.util.List;

public interface RingsCompositeShape extends CompositeAttributedShape {

	public abstract void set(List<Color> aColors, Shape aPrototypeShape);

}