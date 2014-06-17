package bus.uigen.trace.query;

import bus.uigen.query.ABeanQuery;
import bus.uigen.query.BeanQuery;
import util.trace.Traceable;
import util.trace.query.ObjectFound;
import util.trace.query.ObjectMissing;

public class OrderedQueryTargetFound extends QueryTargetFound {
	public OrderedQueryTargetFound(String aMessage, BeanQuery aPreviousObject, BeanQuery anExpectedObject, BeanQuery aLaterObject, Object aFinder) {
		super(aMessage, aPreviousObject, anExpectedObject, aLaterObject, aFinder);
	}
	public OrderedQueryTargetFound(String aMessage, BeanQuery aPreviousObject,
			BeanQuery anExpectedObject, BeanQuery aLaterObject) {
		super(aMessage, aPreviousObject, anExpectedObject, aLaterObject);
	}
	public static OrderedQueryTargetFound toTraceable(String aMessage) {
		try {
			return new OrderedQueryTargetFound (aMessage, 
					ABeanQuery.toBeanQuery(getPrevious(aMessage)),
					ABeanQuery.toBeanQuery(getExpected(aMessage)),
					ABeanQuery.toBeanQuery(getLater(aMessage)));
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
	}
	public static OrderedQueryTargetFound newCase (BeanQuery aPreviousObject, BeanQuery anExpectedObject, BeanQuery aLaterObject, Object aFinder) {
		String aMessage = toString(aPreviousObject, anExpectedObject, aLaterObject);
		OrderedQueryTargetFound retVal = new OrderedQueryTargetFound(aMessage, aPreviousObject, anExpectedObject, aLaterObject, aFinder);
		retVal.announce();
		return retVal;
	}
	

}
