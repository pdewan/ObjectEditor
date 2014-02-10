package bus.uigen.sadapters;
import util.models.VectorInterface;
import bus.uigen.uiFrame;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.undo.CommandListener;
//import java.util.Enumeration;public interface VectorStructure extends DynamicStructure  {	//public int size();	public Object elementAt(int pos);
	public ClassProxy addableElementType ();	//public boolean canDeleteChild();		//public boolean canDeleteChild();
	public boolean isEditable (int index);	public void removeElement(int index, Object element, CommandListener commandListener);
	public void removeElementAt(int index, CommandListener commandListener);	public void addElement(Object element, CommandListener commandListener);	public void setElementAt(Object element, int pos, CommandListener commandListener);	public void setElementAt(Object element, int pos);	public void insertElementAt (Object element, int pos, CommandListener commandListener);
	public boolean validateRemoveElement(Object element);
	public boolean validateRemoveElementAt(int index);
	public boolean validateAddElement(Object element);
	public boolean validateSetElementAt(Object element, int pos);
	public boolean validateInsertElementAt (Object element, int pos);
	public boolean validateElementAt(int index);
	// pre methods
	public boolean preRemoveElement();
	public boolean preRemoveElementAt();
	public boolean preAddElement();
	public boolean preSetElementAt();
	public boolean preInsertElementAt ();
	public boolean preElementAt();
	///
	//public void setTarget(Object theTargetObject);
	boolean hasValidateInsertElementAt();
	public boolean hasValidateAddElement();
}
