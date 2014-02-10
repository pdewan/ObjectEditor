package bus.uigen.undo;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Util
{
	public static Object[] copy(Object[] params) {
		Object[] copyParams = new Object[params.length];
		for (int i = 0; i < params.length; i ++) {
			copyParams[i] = deepCopy (params[i]); // need to clone
			//copyParams[i] = params[i]; 
		}
		return copyParams;
	}
	public static Object deepCopy (Object original) {
		if (original == null) return null;
		Class originalClass = original.getClass();
		if (originalClass == String.class ||
				originalClass == Integer.class ||
				originalClass == Boolean.class ||
				originalClass == Double.class ||
				originalClass == Float.class ||
				originalClass == Short.class ||
				originalClass == Long.class )
			return original;
		try {
		ObjectOutputStream fo = new ObjectOutputStream(new FileOutputStream("copyFile"));
		fo.writeObject(original);
		fo.close();
		ObjectInputStream   fi = new ObjectInputStream(new FileInputStream("copyFile"));
		Object o_copy = fi.readObject();
		fi.close();
		return o_copy;
		} catch (Exception e) {
			return original;
		}
	}
	public static void assignFirst(Object[] params, Object readValue) {
		params[0] = readValue;
	}
	public static Object[] removeFirst(Object[] params) {
		Object[] newParams = new Object[params.length - 1];
		for (int i = 1; i < params.length; i++) {
			newParams[i-1] = params[i];
		}
		return newParams;
	}
	public static Object[] removeLast(Object[] params) {
		Object[] newParams = new Object[params.length - 1];
		for (int i = 0; i < params.length - 1; i ++) {
			newParams[i] = params[i];
		}
		return newParams;
	}
	
	public static void assignLast(Object[] undoWriteMethodParams, Object readValue) {
		undoWriteMethodParams[undoWriteMethodParams.length - 1] = readValue;
	}
	public static Object[] addFirst(Object[] params) {
		Object[] newParams = new Object[params.length + 1];
		for (int i = 0; i < params.length; i++) {
			newParams[i+1] = params[i];
		}
		return newParams;
	}
	public static Object[] addLast(Object[] params) {
		Object[] newParams = new Object[params.length + 1];
		for (int i = 0; i < params.length; i++) {
			newParams[i] = params[i];
		}
		return newParams;
	}
	public static boolean equal(Object[] p1Types, Object[] p2Types) {
		if (p1Types.length != p2Types.length) return false;
		for (int i = 0; i < p1Types.length; i++) {
			if (p1Types[i] != p2Types[i])
				return false;
		}
		return true;		
	}
	public static boolean isPrefix (Object[] p1Types, Object[] p2Types) {
		if (p1Types.length + 1 != p2Types.length) return false;
		for (int i = 0; i < p1Types.length; i++) {
			if (p1Types[i] != p2Types[i])
				return false;
		}
		return true;
	}	
	public static boolean isSuffix (Object[] p1Types, Object[] p2Types) {
		if (p1Types.length + 1 != p2Types.length) return false;
		for (int i = 0; i < p1Types.length; i++) {
			if (p1Types[i] != p2Types[i+1])
				return false;
		}
		return true;
	}	
	
}
