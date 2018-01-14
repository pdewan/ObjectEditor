package util.trace.uigen.query;

import util.trace.Traceable;
import util.trace.query.OrderedClassInstanceDisplaced;
import util.trace.query.TraceableIndices;
import bus.uigen.query.AnObjectQuery;
import bus.uigen.query.ObjectQuery;

public class OrderedQueryTargetDisplaced extends OrderedQueryTargetMissing {
	Integer displacement;
	public OrderedQueryTargetDisplaced(String aMessage, Integer aTestIndex, Integer aReferenceIndex, ObjectQuery aPreviousObject, ObjectQuery anExpectedObject, ObjectQuery aLaterObject, 
			Integer aDisplacement, Object aFinder) {
		super(aMessage, aTestIndex, aReferenceIndex, aPreviousObject, anExpectedObject, aLaterObject, aFinder);
		displacement = aDisplacement;

	}
	public OrderedQueryTargetDisplaced(String aMessage, Integer aTestIndex, Integer aReferenceIndex, ObjectQuery aPreviousObject,
			ObjectQuery anExpectedObject, ObjectQuery aLaterObject, Integer aDisplacement) {
		super(aMessage, aTestIndex, aReferenceIndex, aPreviousObject, anExpectedObject, aLaterObject);
		displacement = aDisplacement;

	}
	public static OrderedQueryTargetDisplaced toTraceable(String aMessage) {
		try {
			return new OrderedQueryTargetDisplaced (aMessage, 
					TraceableIndices.getIndex1(aMessage),
					TraceableIndices.getIndex2(aMessage),
					AnObjectQuery.toBeanQuery(getPrevious(aMessage)),
					AnObjectQuery.toBeanQuery(getExpected(aMessage)),
					AnObjectQuery.toBeanQuery(getLater(aMessage)),
					OrderedClassInstanceDisplaced.getDisplacement(aMessage));
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
	}
	public static String toString (Integer aTestIndex, Integer aReferenceIndex, ObjectQuery aPreviousObject, ObjectQuery anExpectedObject, ObjectQuery aLaterObject, Integer aDisplacement) {
		return 
			toString(aTestIndex, aReferenceIndex, aPreviousObject, anExpectedObject, aLaterObject) + 
			OrderedClassInstanceDisplaced.DISPLACEMENT + Traceable.FLAT_LEFT_MARKER + aDisplacement + Traceable.FLAT_RIGHT_MARKER;
	}
	public static OrderedQueryTargetDisplaced newCase (Integer aTestIndex, Integer aReferenceIndex,
			ObjectQuery aPreviousObject, ObjectQuery anExpectedObject, ObjectQuery aLaterObject, Integer aDisplacement, Object aFinder) {
		String aMessage = toString(aTestIndex, aReferenceIndex, aPreviousObject, anExpectedObject, aLaterObject, aDisplacement);
		OrderedQueryTargetDisplaced retVal = new OrderedQueryTargetDisplaced(aMessage, aTestIndex, aReferenceIndex, aPreviousObject, anExpectedObject, aLaterObject, aDisplacement, aFinder);
		retVal.announce();
		return retVal;
	}
	

}
