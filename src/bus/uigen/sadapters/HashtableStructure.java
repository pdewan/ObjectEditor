package bus.uigen.sadapters;
import bus.uigen.uiFrame;
import bus.uigen.controller.MethodInvocationManager;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.undo.CommandListener;import java.util.Vector;
//import java.util.Enumeration;public interface HashtableStructure extends DynamicStructure  {
	public ClassProxy keyType ();
	public ClassProxy elementType ();
	public Vector keys();
	//public Vector elements(); 	//public boolean canDeleteKey();
	public Object get (Object key);
	public Object put (Object key, Object value);	public Object put (Object key, Object value, CommandListener cl);		public Object remove( Object key, CommandListener cl);
	public Object remove( Object key);
	public boolean isEditableKey(Object key);
	public boolean isEditableElement(Object key);
	public boolean isRemovable (Object key) ;
	//public int size();	//public void setTarget(Object theTargetObject);
}
