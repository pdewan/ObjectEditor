package bus.uigen.trace.query;

import bus.uigen.query.BeanQuery;
import util.trace.Traceable;
import util.trace.query.ObjectFound;

public class QueryTargetFound extends ObjectFound {
	public QueryTargetFound(String aMessage, BeanQuery aPreviousObject, BeanQuery anExpectedObject, Object aLaterObject, Object aFinder) {
		super(aMessage, aPreviousObject, anExpectedObject, aLaterObject, aFinder);
	}
	public QueryTargetFound(String aMessage, Object aPreviousObject,
			Object anExpectedObject, Object aLaterObject) {
		super(aMessage, aPreviousObject, anExpectedObject, aLaterObject);
	}
	public static Traceable toTraceable(String aMessage) {
		return new QueryTargetFound (aMessage, 
				getPrevious(aMessage),
				getExpected(aMessage),
				getLater(aMessage));
	}
	public static QueryTargetFound newCase (Object aPreviousObject, BeanQuery anExpectedObject, Object aLaterObject) {
		String aMessage = toString(aPreviousObject, anExpectedObject, aLaterObject);
		QueryTargetFound retVal = new QueryTargetFound(aMessage, aPreviousObject, anExpectedObject, aLaterObject);
		retVal.announce();
		return retVal;
	}
	

}
