package bus.uigen;

public interface ComponentSelectionListener<ParentType, ComponentType> {
	//public void select (ComponentType argument);
	public void selected (ParentType parent, ComponentType argument) ;
}
