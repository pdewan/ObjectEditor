package bus.uigen.trace.query;

import bus.uigen.query.ABeanQuery;
import bus.uigen.query.BeanQuery;
import util.trace.Traceable;
import util.trace.query.ObjectFound;
import util.trace.query.ObjectMissing;

public class QueryTargetMissing extends ObjectMissing {
	public QueryTargetMissing(String aMessage, BeanQuery aPreviousObject, BeanQuery anExpectedObject, BeanQuery aLaterObject, Object aFinder) {
		super(aMessage, aPreviousObject, anExpectedObject, aLaterObject, aFinder);
	}
	public QueryTargetMissing(String aMessage, BeanQuery aPreviousObject,
			BeanQuery anExpectedObject, BeanQuery aLaterObject) {
		super(aMessage, aPreviousObject, anExpectedObject, aLaterObject);
	}
	public static QueryTargetMissing toTraceable(String aMessage) {
		try {
			return new QueryTargetMissing (aMessage, 
					ABeanQuery.toBeanQuery(getPrevious(aMessage)),
					ABeanQuery.toBeanQuery(getExpected(aMessage)),
					ABeanQuery.toBeanQuery(getLater(aMessage)));
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
	}
	public static QueryTargetMissing newCase (BeanQuery aPreviousObject, BeanQuery anExpectedObject, BeanQuery aLaterObject, Object aFinder) {
		String aMessage = toString(aPreviousObject, anExpectedObject, aLaterObject);
		QueryTargetMissing retVal = new QueryTargetMissing(aMessage, aPreviousObject, anExpectedObject, aLaterObject, aFinder);
		retVal.announce();
		return retVal;
	}
	

}
