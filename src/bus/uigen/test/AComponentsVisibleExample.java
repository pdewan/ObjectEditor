package bus.uigen.test;

import util.annotations.ComponentsVisible;
import util.annotations.Visible;
import bus.uigen.ObjectEditor;
import bus.uigen.attributes.AttributeNames;
@ComponentsVisible(false)
public class AComponentsVisibleExample extends ACompositeExample {
	
	@Visible(true)
	public String getString() {
		return super.getString();
	}
	
	@Visible(true)
	public void incInt() {
		 super.incInt();
	}
	
	
	public static void main (String[] args) {
		// property visible annotation wins over default attribute because of compile time binding for properties for efficiency
		AttributeNames.setDefault(AttributeNames.VISIBLE, true);
		//components visible annotation looses to default attribute
//		AttributeNames.setDefault(AttributeNames.METHODS_VISIBLE, true);


//		ObjectEditor.setMethodAttribute(AComponentsVisibleExample.class, "*", AttributeNames.VISIBLE, false);
//		ObjectEditor.setMethodAttribute(AComponentsVisibleExample.class, "incInt", AttributeNames.VISIBLE, true);

		ObjectEditor.edit(new AComponentsVisibleExample());
	}

}
