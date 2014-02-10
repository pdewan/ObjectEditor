package bus.uigen.introspect;

import bus.uigen.reflect.FieldProxy;
import bus.uigen.reflect.local.AFieldProxy;

public class AFieldDescriptorProxy extends AFeatureDescriptorProxy implements FieldDescriptorProxy {
	FieldProxy fieldProxy;
	public AFieldDescriptorProxy (FieldDescriptor theFD) {
		super (theFD);
		fieldProxy = AFieldProxy.fieldProxy(theFD.getField());
		//fieldDescriptor = theFD;
	}
	public AFieldDescriptorProxy (FieldProxy theFieldProxy) {
		super (theFieldProxy.getName(), theFieldProxy.getName());
		fieldProxy = theFieldProxy;
		
	}
	@Override
	public FieldProxy getField() {
		// TODO Auto-generated method stub
		return fieldProxy;
	}
	
	
}
