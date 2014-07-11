package bus.uigen.trace.query;

import util.trace.query.TraceableIndices;
import util.trace.query.TraceableSearch;
import bus.uigen.query.AnObjectQuery;
import bus.uigen.query.ObjectQuery;

public class OrderedQueryTargetMissing extends QueryTargetMissing {
	public OrderedQueryTargetMissing(String aMessage, 
			Integer aTestIndex,
			Integer aReferenceIndex,
			ObjectQuery aPreviousObject, ObjectQuery anExpectedObject, ObjectQuery aLaterObject, Object aFinder) {
		super(aMessage, aTestIndex, aReferenceIndex, aPreviousObject, anExpectedObject, aLaterObject, aFinder);
	}
	public OrderedQueryTargetMissing(String aMessage, 
			Integer aTestIndex,
			Integer aReferenceIndex,
			
			ObjectQuery aPreviousObject,
			ObjectQuery anExpectedObject, ObjectQuery aLaterObject) {
		super(aMessage, aTestIndex, aReferenceIndex, aPreviousObject, anExpectedObject, aLaterObject);
	}
	
	public static OrderedQueryTargetMissing toTraceable(String aMessage) {
		try {
			return new OrderedQueryTargetMissing (aMessage, 
					TraceableIndices.getIndex1(aMessage),
					TraceableIndices.getIndex2(aMessage),
					AnObjectQuery.toBeanQuery(getPrevious(aMessage)),
					AnObjectQuery.toBeanQuery(getExpected(aMessage)),
					AnObjectQuery.toBeanQuery(getLater(aMessage)));
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
	}
	public static OrderedQueryTargetMissing newCase (
			Integer aTestIndex,
			Integer aReferenceIndex,
			ObjectQuery aPreviousObject, ObjectQuery anExpectedObject, ObjectQuery aLaterObject, Object aFinder) {
		String aMessage = TraceableSearch.toString(aTestIndex, aReferenceIndex, aPreviousObject, anExpectedObject, aLaterObject);
		OrderedQueryTargetMissing retVal = new OrderedQueryTargetMissing(aMessage, aTestIndex, aReferenceIndex, aPreviousObject, anExpectedObject, aLaterObject, aFinder);
		retVal.announce();
		return retVal;
	}
	

}
