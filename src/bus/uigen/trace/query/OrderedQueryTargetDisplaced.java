package bus.uigen.trace.query;

import bus.uigen.query.ABeanQuery;
import bus.uigen.query.BeanQuery;
import util.trace.Traceable;
import util.trace.query.ObjectFound;
import util.trace.query.ObjectMissing;
import util.trace.query.OrderedClassInstanceDisplaced;

public class OrderedQueryTargetDisplaced extends OrderedQueryTargetMissing {
	Integer displacement;
	public OrderedQueryTargetDisplaced(String aMessage, BeanQuery aPreviousObject, BeanQuery anExpectedObject, BeanQuery aLaterObject, 
			Integer aDisplacement, Object aFinder) {
		super(aMessage, aPreviousObject, anExpectedObject, aLaterObject, aFinder);
		displacement = aDisplacement;

	}
	public OrderedQueryTargetDisplaced(String aMessage, BeanQuery aPreviousObject,
			BeanQuery anExpectedObject, BeanQuery aLaterObject, Integer aDisplacement) {
		super(aMessage, aPreviousObject, anExpectedObject, aLaterObject);
		displacement = aDisplacement;

	}
	public static OrderedQueryTargetDisplaced toTraceable(String aMessage) {
		try {
			return new OrderedQueryTargetDisplaced (aMessage, 
					ABeanQuery.toBeanQuery(getPrevious(aMessage)),
					ABeanQuery.toBeanQuery(getExpected(aMessage)),
					ABeanQuery.toBeanQuery(getLater(aMessage)),
					OrderedClassInstanceDisplaced.getDisplacement(aMessage));
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
	}
	public static String toString (BeanQuery aPreviousObject, BeanQuery anExpectedObject, BeanQuery aLaterObject, Integer aDisplacement) {
		return 
			toString(aPreviousObject, anExpectedObject, aLaterObject) + 
			OrderedClassInstanceDisplaced.DISPLACEMENT + Traceable.FLAT_LEFT_MARKER + aDisplacement + Traceable.FLAT_RIGHT_MARKER;
	}
	public static OrderedQueryTargetDisplaced newCase (BeanQuery aPreviousObject, BeanQuery anExpectedObject, BeanQuery aLaterObject, Integer aDisplacement, Object aFinder) {
		String aMessage = toString(aPreviousObject, anExpectedObject, aLaterObject, aDisplacement);
		OrderedQueryTargetDisplaced retVal = new OrderedQueryTargetDisplaced(aMessage, aPreviousObject, anExpectedObject, aLaterObject, aDisplacement, aFinder);
		retVal.announce();
		return retVal;
	}
	

}
