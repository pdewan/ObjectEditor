package bus.uigen.misc;

import java.util.List;

import bus.uigen.widgets.events.VirtualMouseEvent;

import shapes.RemoteShape;

public interface ShapeListMouseClickListener {
	public void mouseClicked(List <RemoteShape> theShapes, VirtualMouseEvent mouseEvent) ;

}
