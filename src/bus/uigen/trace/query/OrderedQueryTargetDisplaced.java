package bus.uigen.trace.query;

import bus.uigen.query.AnObjectQuery;
import bus.uigen.query.ObjectQuery;
import util.trace.Traceable;
import util.trace.query.TraceableFound;
import util.trace.query.TraceableMissing;
import util.trace.query.OrderedClassInstanceDisplaced;

public class OrderedQueryTargetDisplaced extends OrderedQueryTargetMissing {
	Integer displacement;
	public OrderedQueryTargetDisplaced(String aMessage, ObjectQuery aPreviousObject, ObjectQuery anExpectedObject, ObjectQuery aLaterObject, 
			Integer aDisplacement, Object aFinder) {
		super(aMessage, aPreviousObject, anExpectedObject, aLaterObject, aFinder);
		displacement = aDisplacement;

	}
	public OrderedQueryTargetDisplaced(String aMessage, ObjectQuery aPreviousObject,
			ObjectQuery anExpectedObject, ObjectQuery aLaterObject, Integer aDisplacement) {
		super(aMessage, aPreviousObject, anExpectedObject, aLaterObject);
		displacement = aDisplacement;

	}
	public static OrderedQueryTargetDisplaced toTraceable(String aMessage) {
		try {
			return new OrderedQueryTargetDisplaced (aMessage, 
					AnObjectQuery.toBeanQuery(getPrevious(aMessage)),
					AnObjectQuery.toBeanQuery(getExpected(aMessage)),
					AnObjectQuery.toBeanQuery(getLater(aMessage)),
					OrderedClassInstanceDisplaced.getDisplacement(aMessage));
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
	}
	public static String toString (ObjectQuery aPreviousObject, ObjectQuery anExpectedObject, ObjectQuery aLaterObject, Integer aDisplacement) {
		return 
			toString(aPreviousObject, anExpectedObject, aLaterObject) + 
			OrderedClassInstanceDisplaced.DISPLACEMENT + Traceable.FLAT_LEFT_MARKER + aDisplacement + Traceable.FLAT_RIGHT_MARKER;
	}
	public static OrderedQueryTargetDisplaced newCase (ObjectQuery aPreviousObject, ObjectQuery anExpectedObject, ObjectQuery aLaterObject, Integer aDisplacement, Object aFinder) {
		String aMessage = toString(aPreviousObject, anExpectedObject, aLaterObject, aDisplacement);
		OrderedQueryTargetDisplaced retVal = new OrderedQueryTargetDisplaced(aMessage, aPreviousObject, anExpectedObject, aLaterObject, aDisplacement, aFinder);
		retVal.announce();
		return retVal;
	}
	

}
