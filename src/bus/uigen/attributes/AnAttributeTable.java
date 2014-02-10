package bus.uigen.attributes;

import java.util.Hashtable;

public class AnAttributeTable {
	Hashtable<String, Object> contents = new Hashtable();
	public Object setValue(String name, Object value) {
		return contents.put(name, value);		
	}
	public Object getValue (String name) {
		return contents.get(name);
	}
}
