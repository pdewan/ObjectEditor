package util.trace.uigen;

import util.trace.ObjectInfo;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.sadapters.ConcreteType;

public class ConcreteStructureNodeCreated extends ObjectInfo {	
	
	public ConcreteStructureNodeCreated(String aMessage, ConcreteType anObjectAdapter, Object aFinder) {
		super(aMessage, anObjectAdapter, aFinder);
	}	
	public static ConcreteStructureNodeCreated newCase(ConcreteType aConcreteType, Object aFinder) {
		String aMessage = "Concrete Structure Node Created: " + aConcreteType;
		ConcreteStructureNodeCreated retVal = new ConcreteStructureNodeCreated(aMessage, aConcreteType, aFinder);
		retVal.announce();		
		return retVal;
	}
	
	

}
