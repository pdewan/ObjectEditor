package bus.uigen.models;
import util.models.ListenableVector;
import bus.uigen.ObjectEditor;
public class MainClassLaunchingUtility {
	public static MainClassListLauncher interactiveLaunch(Class[] classes, String aTranscriptFileName) {
//		ListenableVector<Class> classList = new AMainClassListLauncher();
//		for (Class aClass:classes) {
//			classList.add(aClass);
//		}
		MainClassListLauncher retVal = createLauncher(classes, aTranscriptFileName);
		ObjectEditor.edit(retVal);
		return retVal;
	}
	public static MainClassListLauncher interactiveLaunch(Class[] classes) {
//		ListenableVector<Class> classList = new AMainClassListLauncher();
//		for (Class aClass:classes) {
//			classList.add(aClass);
//		}
		MainClassListLauncher retVal = createLauncher(classes, null);
		ObjectEditor.edit(retVal);
		return retVal;
	}
	
	public static MainClassListLauncher createLauncher(Class[] classes, String aTranscriptFileName) {
		MainClassListLauncher classList = new AMainClassListLauncher(aTranscriptFileName);
		for (Class aClass:classes) {
			classList.add(aClass);
		}
		return classList;
	}
}
