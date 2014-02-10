package bus.uigen.diff;

import de.danielbechler.diff.node.Node;

public interface ChangeDescription {

	public Node.State getState();

	public void setState(Node.State state);

	public Object getBase();

	public void setBase(Object base);

	public Object getModified();

	public void setModified(Object modified);

}