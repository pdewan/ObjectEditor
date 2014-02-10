/*
 * Created on May 9, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package bus.uigen.compose;

import java.lang.reflect.Method;

import bus.uigen.reflect.MethodProxy;

public class AnObjectMethod {
	Object object;
	MethodProxy method;
	public AnObjectMethod (Object theObject, MethodProxy theMethod) {
		object = theObject;
		method = theMethod;
	}
	public Object getObject() {
		return object;
	}
	public MethodProxy getMethod() {
		return method;
	}

}
