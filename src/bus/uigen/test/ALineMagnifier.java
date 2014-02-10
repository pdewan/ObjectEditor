package bus.uigen.test;

import bus.uigen.misc.ShapeMouseClickListener;
import bus.uigen.shapes.ALineModel;
import bus.uigen.widgets.events.VirtualMouseEvent;

public class ALineMagnifier extends ALineModel implements ShapeMouseClickListener{
	public ALineMagnifier(int theX, int theY, int theWidth, int theHeight) {
		super(theX, theY, theWidth, theHeight);
	}
	@Override
	public void mouseClicked(VirtualMouseEvent mouseEvent) {
		this.setSize(this.getWidth()*2, this.getHeight()*2);
		
	}

}
