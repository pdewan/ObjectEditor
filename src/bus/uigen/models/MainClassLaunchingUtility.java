package bus.uigen.models;
import util.models.ListenableVector;
import bus.uigen.ObjectEditor;
public class MainClassLaunchingUtility {
	public static MainClassListLauncher interactiveLaunch(Class[] classes) {
//		ListenableVector<Class> classList = new AMainClassListLauncher();
//		for (Class aClass:classes) {
//			classList.add(aClass);
//		}
		MainClassListLauncher retVal = createLauncher(classes);
		ObjectEditor.edit(retVal);
		return retVal;
	}
	
	public static MainClassListLauncher createLauncher(Class[] classes) {
		MainClassListLauncher classList = new AMainClassListLauncher();
		for (Class aClass:classes) {
			classList.add(aClass);
		}
		return classList;
	}
}
