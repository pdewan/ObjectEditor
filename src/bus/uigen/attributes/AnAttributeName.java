package bus.uigen.attributes;
import java.util.Vector;

public class AnAttributeName   {
	String currentAttribute = AttributeNames.LABEL;
	public AnAttributeName() {
		init();	
	}
	void init () {		
		
	}
	public AnAttributeName(String initVal) {
		currentAttribute = initVal;
		init();	
	}
	public int choicesSize() {
		return AttributeNames.getAttributeNames().length;
	}
	public String choiceAt(int i) {
		return AttributeNames.getAttributeNames()[i];
	}
	public String getValue() {
		return currentAttribute;
	}
	public void setValue(String newVal) {
		currentAttribute = newVal;
		/*
		name = newVal;
		if (!bmiNames.contains(newVal)) {
			bmiNames.addElement(newVal);
		}
		*/
	}
	public String toString() {
		return currentAttribute;
	}

}