package bus.uigen.introspect;

import java.lang.reflect.Field;

import bus.uigen.reflect.FieldProxy;

public class AConstantDescriptorProxy extends AFieldDescriptorProxy{
	public AConstantDescriptorProxy(FieldDescriptor theFD) { 
		super (theFD);
	}
	public AConstantDescriptorProxy (FieldProxy theField ) {
		super (theField);
	}

}
