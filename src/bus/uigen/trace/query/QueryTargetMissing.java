package bus.uigen.trace.query;

import bus.uigen.query.BeanQuery;
import util.trace.Traceable;
import util.trace.query.ObjectFound;

public class QueryTargetMissing extends ObjectFound {
	public QueryTargetMissing(String aMessage, BeanQuery aPreviousObject, BeanQuery anExpectedObject, Object aLaterObject, Object aFinder) {
		super(aMessage, aPreviousObject, anExpectedObject, aLaterObject, aFinder);
	}
	public QueryTargetMissing(String aMessage, Object aPreviousObject,
			Object anExpectedObject, Object aLaterObject) {
		super(aMessage, aPreviousObject, anExpectedObject, aLaterObject);
	}
	public static Traceable toTraceable(String aMessage) {
		return new QueryTargetMissing (aMessage, 
				getPrevious(aMessage),
				getExpected(aMessage),
				getLater(aMessage));
	}
	public static QueryTargetMissing newCase (Object aPreviousObject, BeanQuery anExpectedObject, Object aLaterObject) {
		String aMessage = toString(aPreviousObject, anExpectedObject, aLaterObject);
		QueryTargetMissing retVal = new QueryTargetMissing(aMessage, aPreviousObject, anExpectedObject, aLaterObject);
		retVal.announce();
		return retVal;
	}
	

}
