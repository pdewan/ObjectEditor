package bus.uigen.diff;

import util.annotations.DisplayToString;
import de.danielbechler.diff.node.Node;
import de.danielbechler.diff.node.Node.State;
public class AChangeDescription implements ChangeDescription {
	 Node.State state;
	 Object base; 
	 Object modified;
	public AChangeDescription(State state, Object base, Object modified) {
		super();
		this.state = state;
		this.base = base;
		this.modified = modified;
	}
	/* (non-Javadoc)
	 * @see bus.uigen.diff.ChangeDescription#getState()
	 */
	@DisplayToString(true)
	@Override
	public Node.State getState() {
		return state;
	}
	/* (non-Javadoc)
	 * @see bus.uigen.diff.ChangeDescription#setState(de.danielbechler.diff.node.Node.State)
	 */
	@Override
	public void setState(Node.State state) {
		this.state = state;
	}
	/* (non-Javadoc)
	 * @see bus.uigen.diff.ChangeDescription#getBase()
	 */
	@DisplayToString(true)
	@Override
	public Object getBase() {
		return base;
	}
	/* (non-Javadoc)
	 * @see bus.uigen.diff.ChangeDescription#setBase(java.lang.Object)
	 */
	@Override
	public void setBase(Object base) {
		this.base = base;
	}
	/* (non-Javadoc)
	 * @see bus.uigen.diff.ChangeDescription#getModified()
	 */
	@DisplayToString(true)
	@Override
	public Object getModified() {
		return modified;
	}
	/* (non-Javadoc)
	 * @see bus.uigen.diff.ChangeDescription#setModified(java.lang.Object)
	 */
	@Override
	public void setModified(Object modified) {
		this.modified = modified;
	}
	
	
	

}
