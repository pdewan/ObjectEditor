package bus.uigen.pipes;
import util.models.ListenableVector;
import bus.uigen.ObjectEditor;
import bus.uigen.models.AMainClassListLauncher;
public class MainClassLaunchingUtility {
//	public static MainClassListLauncher interactiveLaunch(Class[] classes, String aTranscriptFileName) {
////		ListenableVector<Class> classList = new AMainClassListLauncher();
////		for (Class aClass:classes) {
////			classList.add(aClass);
////		}
//		MainClassListLauncher retVal = createLauncher(classes, aTranscriptFileName);
//		ObjectEditor.edit(retVal);
//		return retVal;
//	}
	public static MainClassListLauncher createInteractiveLauncher(Class[] classes) {
//		ListenableVector<Class> classList = new AMainClassListLauncher();
//		for (Class aClass:classes) {
//			classList.add(aClass);
//		}
		MainClassListLauncher retVal = createLauncher(classes);
		retVal.setInteractive(true);
		ObjectEditor.edit(retVal);
		return retVal;
	}
//	public static MainClassListLauncher interactiveLaunch(Class[] classes) {
////		ListenableVector<Class> classList = new AMainClassListLauncher();
////		for (Class aClass:classes) {
////			classList.add(aClass);
////		}
//		MainClassListLauncher retVal = createLauncher(classes, null);
//		ObjectEditor.edit(retVal);
//		return retVal;
//	}
	
//	public static MainClassListLauncher createLauncher(Class[] classes, String aTranscriptFileName) {
//		MainClassListLauncher classList = new AMainClassListLauncher(aTranscriptFileName);
//		for (Class aClass:classes) {
//			classList.add(aClass);
//		}
//		return classList;
//	}
	
	public static MainClassListLauncher createLauncher(Class[] classes) {
		MainClassListLauncher aLauncher = new AMainClassListLauncher();
		for (Class aClass:classes) {
			aLauncher.add(aClass);
		}
		aLauncher.setInteractive(false);

		return aLauncher;
	}
}
