package bus.uigen.oadapters;

public interface AttributeDependency {
	public ObjectAdapter getObjectAdapter();
	public String getAttributeName();

	public void setObjectAdapter(ObjectAdapter objectAdapter);
	
	public void setAttributeName(String attributeName);

}
