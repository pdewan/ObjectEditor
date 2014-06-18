package bus.uigen.trace.query;

import bus.uigen.query.AnObjectQuery;
import bus.uigen.query.ObjectQuery;
import util.trace.Traceable;
import util.trace.query.TraceableFound;
import util.trace.query.TraceableMissing;

public class QueryTargetFound extends TraceableMissing {
	public QueryTargetFound(String aMessage, ObjectQuery aPreviousObject, ObjectQuery anExpectedObject, ObjectQuery aLaterObject, Object aFinder) {
		super(aMessage, aPreviousObject, anExpectedObject, aLaterObject, aFinder);
	}
	public QueryTargetFound(String aMessage, ObjectQuery aPreviousObject,
			ObjectQuery anExpectedObject, ObjectQuery aLaterObject) {
		super(aMessage, aPreviousObject, anExpectedObject, aLaterObject);
	}
	public static QueryTargetFound toTraceable(String aMessage) {
		try {
			return new QueryTargetFound (aMessage, 
					AnObjectQuery.toBeanQuery(getPrevious(aMessage)),
					AnObjectQuery.toBeanQuery(getExpected(aMessage)),
					AnObjectQuery.toBeanQuery(getLater(aMessage)));
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
	}
	public static QueryTargetFound newCase (ObjectQuery aPreviousObject, ObjectQuery anExpectedObject, ObjectQuery aLaterObject, Object aFinder) {
		String aMessage = toString(aPreviousObject, anExpectedObject, aLaterObject);
		QueryTargetFound retVal = new QueryTargetFound(aMessage, aPreviousObject, anExpectedObject, aLaterObject, aFinder);
		retVal.announce();
		return retVal;
	}
	

}
