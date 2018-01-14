package util.trace.uigen;


public class AttributeSettingStarted extends MajorStepInfo {	
	
	public AttributeSettingStarted(String aMessage, Object anObject, Object aFinder) {
		super(aMessage, anObject, aFinder);
	}	
	public static AttributeSettingStarted newCase(Object anObject, Object aFinder) {
		String aMessage = "Setting attributes of tree rooted by: " + anObject;
		AttributeSettingStarted retVal = new AttributeSettingStarted(aMessage, anObject, aFinder);
		retVal.announce();		
		return retVal;
	}
	
	

}
