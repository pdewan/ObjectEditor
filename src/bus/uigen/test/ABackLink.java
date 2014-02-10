package bus.uigen.test;

public class ABackLink {
	Object parent;	
	public ABackLink(Object aParent) {
		parent = aParent;
	}
	
	public Object getParent() {
		return parent;
	}

}
