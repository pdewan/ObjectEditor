package bus.uigen;

public interface ComponentExpansionListener<ParentType, ComponentType> {
	//public void select (ComponentType argument);
	public void expanded (ParentType parent, ComponentType argument, boolean hasExpanded) ;
}
