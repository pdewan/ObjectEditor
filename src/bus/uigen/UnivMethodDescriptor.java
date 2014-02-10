package bus.uigen;

import java.lang.reflect.*;

import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;

public class UnivMethodDescriptor implements java.io.Serializable
{
	public String declaringClass;
	public String name;
	public String parameterTypes[];
	public String returnType;
	
	public UnivMethodDescriptor(MethodProxy m){
		declaringClass = m.getDeclaringClass().getName();
		name = m.getName();
		ClassProxy pts[] = m.getParameterTypes();
		parameterTypes = new String[pts.length];
		for(int i=0;i<pts.length;i++){
			parameterTypes[i] = pts[i].getName();
		}
		returnType = m.getReturnType().getName();
	}
	
	public boolean describes(MethodProxy m){
		if(!declaringClass.equals(m.getDeclaringClass().getName())){
			return false;
		}
		if(!name.equals(m.getName())){
			return false;
		}
		ClassProxy pts[] = m.getParameterTypes();
		if(parameterTypes.length != pts.length){
			return false;
		}
		for(int i=0;i<parameterTypes.length;i++){
			if(!parameterTypes[i].equals(pts[i].getName())){
				return false;
			}
		}
		if(!returnType.equals(m.getReturnType().getName())){
			return false;
		}
		return true;
	}
}
