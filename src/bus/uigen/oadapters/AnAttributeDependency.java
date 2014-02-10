package bus.uigen.oadapters;

public class AnAttributeDependency implements AttributeDependency{
	ObjectAdapter objectAdapter;
	String attributeName;
	public AnAttributeDependency(ObjectAdapter anObjectAdapter, String anAttributeName) {
		objectAdapter = anObjectAdapter;
		attributeName = anAttributeName;		
	}
	public ObjectAdapter getObjectAdapter() {
		return objectAdapter;
	}
	public void setObjectAdapter(ObjectAdapter objectAdapter) {
		this.objectAdapter = objectAdapter;
	}
	public String getAttributeName() {
		return attributeName;
	}
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	

}
