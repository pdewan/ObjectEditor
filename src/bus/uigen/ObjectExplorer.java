package bus.uigen;public class ObjectExplorer {
	public static void main(String args[]) {
		//String objectClassName;
		bus.uigen.uiFrame editor;		int numPanes = 0;
		try {		if (args.length != 0) {			
				System.out.println(args.length);				//objectClassName = args[0];				Class objectClass = Class.forName(args[0]);
				editor = bus.uigen.uiGenerator.generateUIFrame(objectClass.newInstance());
											
		} else {
			editor = bus.uigen.uiGenerator.generateUIFrame(null);
			
		}			
	       //editor.setVisible(true);
			//editor.debugScroll(editor.getAdapter());			//System.out.println("before visible");
						//editor.setSize(200,200);
			//editor.exploreRight();
			//editor.exploreBottom();			
						editor.setSize(250,250);			//editor.doLayout();
			//editor.newWindowRight(packageVar.getInterfaces());
			editor.newWindowRight();
			if (args.length > 1) {
				numPanes = Integer.parseInt(args[1]);
				if (numPanes > 2) {			      editor.setVisible(true);			      editor.setVisible(false);			      editor.newWindowBottom();				}			}
			editor.setVisible(true);
			//editor.validate();						//System.out.println("after visible");
				//editor.debugScroll(editor.getAdapter());	} catch (Exception e) {
				System.out.println(e);			}
		}	
}

