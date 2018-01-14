package bus.uigen.test;

import util.trace.TraceableWarning;
import util.trace.uigen.UnknownPropertyNotification;
import bus.uigen.OEFrame;
import bus.uigen.ObjectEditor;

public class AWarningDisabler extends ACompositeObservable {
	public void setString(String newVal) {
		super.setString(newVal);
		propertyChangeSupport.firePropertyChange("Nonexisting", null, true);


	}
	public static void main (String[] args) {
		TraceableWarning.doNotWarn(UnknownPropertyNotification.class);
		ACompositeObservable example = new AWarningDisabler();
		OEFrame mainFrame = ObjectEditor.edit(example);	
	}

}
