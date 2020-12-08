package bus.uigen;
public class ObjectEditorLauncher extends java.applet.Applet  {
		public void init() {
		System.err.println("initing");
		System.err.println((ObjectEditor.class).getDeclaredFields());
		//ObjectEditor.registerEditors();
		System.err.println("registered editors");
		bus.uigen.ObjectEditor.edit(new ObjectEditor(false));
		
	}
	
		
}

