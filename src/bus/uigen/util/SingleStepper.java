package bus.uigen.util;

import util.misc.BroadcastingClearanceManager;
import util.misc.ClearanceManagerFactory;
import bus.uigen.ObjectEditor;

public class SingleStepper {
	static BroadcastingClearanceManager clearanceManager = ClearanceManagerFactory.getBroadcastingClearanceManager();
	static boolean displayedOE;
	public static void waitForNextStep() {
		if (!displayedOE)
			ObjectEditor.edit(clearanceManager);
		clearanceManager.waitForClearance();
	}	

}
