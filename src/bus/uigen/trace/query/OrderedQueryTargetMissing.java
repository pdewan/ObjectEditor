package bus.uigen.trace.query;

import bus.uigen.query.ABeanQuery;
import bus.uigen.query.BeanQuery;
import util.trace.Traceable;
import util.trace.query.ObjectFound;
import util.trace.query.ObjectMissing;

public class OrderedQueryTargetMissing extends QueryTargetMissing {
	public OrderedQueryTargetMissing(String aMessage, BeanQuery aPreviousObject, BeanQuery anExpectedObject, BeanQuery aLaterObject, Object aFinder) {
		super(aMessage, aPreviousObject, anExpectedObject, aLaterObject, aFinder);
	}
	public OrderedQueryTargetMissing(String aMessage, BeanQuery aPreviousObject,
			BeanQuery anExpectedObject, BeanQuery aLaterObject) {
		super(aMessage, aPreviousObject, anExpectedObject, aLaterObject);
	}
	public static OrderedQueryTargetMissing toTraceable(String aMessage) {
		try {
			return new OrderedQueryTargetMissing (aMessage, 
					ABeanQuery.toBeanQuery(getPrevious(aMessage)),
					ABeanQuery.toBeanQuery(getExpected(aMessage)),
					ABeanQuery.toBeanQuery(getLater(aMessage)));
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
	}
	public static OrderedQueryTargetMissing newCase (BeanQuery aPreviousObject, BeanQuery anExpectedObject, BeanQuery aLaterObject, Object aFinder) {
		String aMessage = toString(aPreviousObject, anExpectedObject, aLaterObject);
		OrderedQueryTargetMissing retVal = new OrderedQueryTargetMissing(aMessage, aPreviousObject, anExpectedObject, aLaterObject, aFinder);
		retVal.announce();
		return retVal;
	}
	

}
