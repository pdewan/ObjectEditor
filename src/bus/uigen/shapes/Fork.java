package bus.uigen.shapes;

import shapes.BoundedShape;
import shapes.FlexibleLineShape;
import shapes.Magnifiable;
import shapes.Rotatable;

public interface Fork extends BoundedShape, Magnifiable, Rotatable {
	public FlexibleLineShape getLeftLine();	
	public FlexibleLineShape getRightLine();

}
