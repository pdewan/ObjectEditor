package bus.uigen.attributes;

public class AnInheritedAttributeValue {
	public AnInheritedAttributeValue (Object theValue, InheritanceKind theKind) {
		value = theValue;
		inheritanceKind = theKind;
	}
	 Object value;
	public enum InheritanceKind {INSTANCE, CLASS, FRAME_DEFAULT, DEFAULT, SYSTEM_DEFAULT};
	 InheritanceKind inheritanceKind;
	public Object getValue() {
		return value;
	}
	public Object getInheritanceKind() {
		return inheritanceKind;
	}


}
