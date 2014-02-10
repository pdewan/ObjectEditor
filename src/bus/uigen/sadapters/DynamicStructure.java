package bus.uigen.sadapters;
import util.models.VectorInterface;
import bus.uigen.uiFrame;
import bus.uigen.undo.CommandListener;
//import java.util.Enumeration;public interface DynamicStructure  extends RecordStructure {	public boolean hasDeleteChildMethod();
	public boolean hasAddChildMethod();
	public boolean hasInsertChildMethod();	public boolean hasSetChildMethod();
	public boolean hasClearMethod();
	public void clear();
	public void clear(CommandListener commandListener);
	public int size();
}
