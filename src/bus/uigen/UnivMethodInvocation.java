package bus.uigen;
import java.lang.reflect.*;

import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.reflect.local.AVirtualMethod;
public class UnivMethodInvocation implements java.io.Serializable, UIGenLoggableEvent {
	int parentObjectID;
	String parentObjectPath;
	UnivMethodDescriptor methodDescriptor;
	Object[] parameterValues;
	public UnivMethodInvocation (Object theParentObject, MethodProxy theMethod, Object[] theParameterValues) {
		if(ObjectEditor.coupleElides){
			parentObjectID = ObjectRegistry.indexOf(theParentObject);
		} else{
			parentObjectPath = ObjectRegistry.getAdapterPathFor(theParentObject);
		}
		methodDescriptor = new UnivMethodDescriptor(theMethod);
		parameterValues = theParameterValues;
		if(theParameterValues!=null){
			parameterValues = new Object[theParameterValues.length];
			for(int i=0;i<parameterValues.length;i++){
				parameterValues[i] = new UnivMethodParameter(theParameterValues[i]);
			}
		}
		System.err.println("**************** XXX Calling "+theMethod+" of "+theParentObject);
	}
	
	public String getUIGenInternalID(){
		if(ObjectEditor.coupleElides){
			return "*";
		} else{
			return parentObjectPath;
		}
	}
	
	public Object execute() {
		try {
			Object[] myParams = null;
			if(parameterValues!=null){
				myParams = new Object[parameterValues.length];
				for(int i=0;i<parameterValues.length;i++){
					UnivMethodParameter ump = (UnivMethodParameter) parameterValues[i];
					myParams[i] = ump.localize();
				}
			}
			Object obj = null;
			if(ObjectEditor.coupleElides){
				obj = ObjectRegistry.objectAt(parentObjectID);
			} else{
				ObjectAdapter adapter = ObjectRegistry.getAdapter(parentObjectPath);
				if(adapter==null){
					System.err.println("local adapter ("+parentObjectPath+") does not exist");
					System.exit(1);
				} else{
					obj = adapter.computeAndMaybeSetViewObject();
				}
			}
			Class cls = obj.getClass();
			Method methods[] = cls.getMethods();
			for(int i=0;i<methods.length;i++){
				if(methodDescriptor.describes(AVirtualMethod.virtualMethod(methods[i]))){
					System.err.print("ZZZ calling "+methods[i]+" of "+obj);
					Object result = methods[i].invoke(obj,myParams);
					System.err.println(" to get "+result);
					return(result);
				}
			}
		} catch (Exception e) {
			System.err.println("UnivMethodInvocation.execute(): Exception "+e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
}