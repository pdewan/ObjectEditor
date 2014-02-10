package bus.uigen.shapes;

import shapes.FlexibleLineShape;


public interface LineWithAnArrow {
	public FlexibleLineShape getLine() ;
	public Fork getFork();
	public void setX(int newVal) ;
	public void setY (int newVal);

}
