package bus.uigen.shapes;

import java.io.Serializable;

import shapes.FlexibleShape;
import shapes.RemoteShape;
import util.undo.Listener;

public interface OEShapeModel extends FlexibleShape, Listener, Serializable  {
	RemoteShape getRemoteShape();

}
