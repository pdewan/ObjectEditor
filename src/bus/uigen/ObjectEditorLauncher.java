package bus.uigen;
public class ObjectEditorLauncher extends java.applet.Applet  {
		public void init() {
		System.out.println("initing");
		System.out.println((ObjectEditor.class).getDeclaredFields());
		//ObjectEditor.registerEditors();
		System.out.println("registered editors");
		bus.uigen.ObjectEditor.edit(new ObjectEditor(false));
		
	}
	
		
}

