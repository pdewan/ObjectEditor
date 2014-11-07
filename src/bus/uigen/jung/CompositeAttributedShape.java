package bus.uigen.jung;

import java.awt.Shape;
import java.util.List;

import shapes.ShapeModel;

public interface CompositeAttributedShape extends Shape {
	public void setComponents(List<ShapeModel> aComponents) ;
	public List<ShapeModel> getComponents() ;

}
