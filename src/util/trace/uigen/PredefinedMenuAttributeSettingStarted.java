package util.trace.uigen;

import util.trace.ObjectInfo;

public class PredefinedMenuAttributeSettingStarted extends ObjectInfo {	
	
	public PredefinedMenuAttributeSettingStarted(String aMessage, Object anObject, Object aFinder) {
		super(aMessage, anObject, aFinder);
	}	
	public static PredefinedMenuAttributeSettingStarted newCase(Object anObject, Object aFinder) {
		String aMessage = "Setting attributes of predefined menus of object: " + anObject;
		PredefinedMenuAttributeSettingStarted retVal = new PredefinedMenuAttributeSettingStarted(aMessage, anObject, aFinder);
		retVal.announce();		
		return retVal;
	}
	
	

}
