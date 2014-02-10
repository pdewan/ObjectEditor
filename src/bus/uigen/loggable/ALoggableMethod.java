package bus.uigen.loggable;

import bus.uigen.reflect.MethodProxy;

public class ALoggableMethod {
	int methodId;
	public ALoggableMethod(MethodProxy realMethod) {
		
	}
	public int getMethodId() {
		return methodId;
	}
	public void setMethodId(int methodId) {
		this.methodId = methodId;
	}

}
