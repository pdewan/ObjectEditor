package bus.uigen.shapes;

import shapes.AttributedShape;
import shapes.BoundedShape;
import shapes.BoundedTextShape;
import util.misc.Common;


public interface StringInAShape extends AttributedShape {

	public BoundedShape getEnclosingShape();

	public BoundedTextShape getStringShape();
	public void setDynamicDimension() ;
	
	public void setFixedDimension() ;
	public String getText();
	public void setText(String newVal);
	public int getxMargin() ;
	public void setxMargin(int xMargin) ;
	public int getyMargin();
	public void setyMargin(int yMargin) ;
	public int getyCorrection() ;
	public void setyCorrection(int yCorrection) ;

}