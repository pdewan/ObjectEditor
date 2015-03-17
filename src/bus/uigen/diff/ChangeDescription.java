package bus.uigen.diff;

import de.danielbechler.diff.node.DiffNode;

public interface ChangeDescription {

	public DiffNode.State getState();

	public void setState(DiffNode.State state);

	public Object getBase();

	public void setBase(Object base);

	public Object getModified();

	public void setModified(Object modified);

}