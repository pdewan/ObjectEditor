package bus.uigen;
import bus.uigen.oadapters.ObjectAdapter;
public class UnivMethodParameter implements java.io.Serializable
{
	int index;
	String adapterPath;
	Object unindexableParam;
	
	public UnivMethodParameter(Object param){
		if(ObjectEditor.coupleElides){
			index = ObjectRegistry.indexOf(param);
			if(index==-1){
				unindexableParam = param;
			}
		} else{
			adapterPath = ObjectRegistry.getAdapterPathFor(param);
			if(adapterPath==null){
				unindexableParam = param;
			}
		}
	}
	
	public Object localize(){
		Object retVal = unindexableParam;
		if(ObjectEditor.coupleElides){
			if(index!=-1){
				retVal = ObjectRegistry.objectAt(index);
			}
		} else{
			if(adapterPath!=null){
				ObjectAdapter adapter = ObjectRegistry.getAdapter(adapterPath);
				if(adapter==null){
					System.out.println("local adapter ("+adapterPath+") does not exist");
					System.exit(1);
				}
				retVal = adapter.computeAndMaybeSetViewObject();
			}
		}
		return(retVal);
	}
}
