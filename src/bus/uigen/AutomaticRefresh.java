package bus.uigen;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.view.WidgetShell;
public class AutomaticRefresh implements java.io.Serializable, UIGenLoggableEvent
{
	public int id;
	private String adapterPath;
	public String command;
	public int sid;
	private String adapterPath2;
	public int num;
	public String source;
	
	public void setSource(String s){
		source = s;
	}
	
	public AutomaticRefresh(uiFrame f, String c){
		//if (ObjectEditor.colabMode())
		id = ObjectRegistry.indexOfUIFrame(f);
		command = c;
	}
	
	public AutomaticRefresh(WidgetShell a){
		//if (ObjectEditor.colabMode())
		id = ObjectRegistry.indexOfWidget(a);
		command = "expand";
	}
	
	public AutomaticRefresh(WidgetShell a, String c){
		//if (ObjectEditor.colabMode())
		id = ObjectRegistry.indexOfWidget(a);
		command = c;
	}
	
	public AutomaticRefresh(uiFrame f, ObjectAdapter a, int l){
		//if (ObjectEditor.colabMode())
		id = ObjectRegistry.indexOfUIFrame(f);
		command = "deepElide";
		if (ObjectEditor.colabMode())
		sid = ObjectRegistry.indexOfAdapter(a);
		num = l;
	}
	
	public AutomaticRefresh(uiFrame f, ObjectAdapter a){
		//if (ObjectEditor.colabMode())
		id = ObjectRegistry.indexOfUIFrame(f);
		command = "getChildCount";
		if (ObjectEditor.colabMode())
		sid = ObjectRegistry.indexOfAdapter(a);
	}
	
	public AutomaticRefresh(String adapterPath){
		this.adapterPath = adapterPath;
		command = "expand";
	}
	
	public AutomaticRefresh(String adapterPath, String c){
		this.adapterPath = adapterPath;
		command = c;
	}
	
	public AutomaticRefresh(uiFrame f, String adapterPath, int l){
		//if (ObjectEditor.colabMode())
		id = ObjectRegistry.indexOfUIFrame(f);
		command = "deepElide";
		adapterPath2 = adapterPath;
		num = l;
	}
	
	public AutomaticRefresh(uiFrame f, String adapterPath, String c){
		//if (ObjectEditor.colabMode())
		id = ObjectRegistry.indexOfUIFrame(f);
		adapterPath2 = adapterPath;
		command = c;
	}
	
	public String getUIGenInternalID(){
		if(ObjectEditor.coupleElides ||
			command.equals("doUpdateAll") ||
			command.equals("doImplicitRefresh")){
			return "*";
		} else if (command.equals("expand") || command.equals("toggleElide")){
			return adapterPath;
		} else if (command.equals("deepElide") || command.equals("getChildCount")){
			return adapterPath2;
		} else {
			System.err.println("Unrecognized command: "+command);
			System.exit(1);
		}
		return null;
	}
	
	public void execute(String localName){
		//if (!ObjectEditor.colabMode()) return;
		if(localName!=null &&
			!ObjectEditor.coupleElides &&
			ObjectRegistry.loggableRole.equals(ObjectRegistry.UI_ROLE) &&
			!source.equals(localName) &&
			!command.equals("doUpdateAll") && !command.equals("doImplicitRefresh")){
			return;
		}
		System.err.println("########### XXX AutomaticRefresh.execute(): executing "+command);
		if(command.equals("doUpdateAll") || command.equals("doImplicitRefresh")){
			uiFrame f = ObjectRegistry.uiFrameAt(id);
			if(command.equals("doUpdateAll")){
				f.subDoUpdateAll();
			} else {
				f.subDoImplicitRefresh();
			}
		} else if (command.equals("expand")){
			WidgetShell a = null;
			if(ObjectEditor.coupleElides){
				a = (WidgetShell) ObjectRegistry.widgetAt(id);
			} else{
				ObjectAdapter adapter = ObjectRegistry.getAdapter(adapterPath);
				a = adapter.getGenericWidget();
			}
			a.subExpand();
		} else if (command.equals("deepElide")){
			uiFrame f = ObjectRegistry.uiFrameAt(id);
			ObjectAdapter adapter = null;
			if(ObjectEditor.coupleElides){
				adapter = ObjectRegistry.adapterAt(sid);
			} else{
				adapter = ObjectRegistry.getAdapter(adapterPath2);
			}
			f.subDeepElide(adapter,num);
		} else if (command.equals("getChildCount")){
			uiFrame f = ObjectRegistry.uiFrameAt(id);
			ObjectAdapter adapter = null;
			if(ObjectEditor.coupleElides){
				adapter = ObjectRegistry.adapterAt(sid);
			} else{
				adapter = ObjectRegistry.getAdapter(adapterPath2);
			}
			f.subGetChildCount(adapter);
		} else if (command.equals("toggleElide")){
			WidgetShell a = null;
			if(ObjectEditor.coupleElides){
				a = (WidgetShell) ObjectRegistry.widgetAt(id);
			} else{
				ObjectAdapter adapter = ObjectRegistry.getAdapter(adapterPath);
				a = adapter.getGenericWidget();
			}
			a.subToggleElide();
		} else {
			System.out.println("Unrecognized command: "+command);
			System.exit(1);
		}
	}
}
