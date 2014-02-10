package bus.uigen.controller.models;

public class AboutManager {
	static final String  OE_VERSION_NAME = "oeall22";
	static ObjectEditorDescription objectEditorDescription =  new AnObjectEditorDescription();
	public static void main (String[] args) {
		printAbout();
	}
	public static String getAbout () {
		return /*OE_VERSION_NAME + " " +*/ objectEditorDescription.toString();
	}
	
	public static ObjectEditorDescription getObjectEditorDescription () {
		return objectEditorDescription;
	}
	
	public static void printAbout () {
		System.out.println(getAbout());
//		System.out.println(BuildTimeManager.getBuildTimeInTextFile());
	}

}
