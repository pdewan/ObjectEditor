package bus.uigen.shapes;

import shapes.BoundedShape;
import util.models.ListenableVector;

public interface StringInShapes extends ListenableVector<BoundedShape> {
	public void setString(String aString);
	public String getString();
}
