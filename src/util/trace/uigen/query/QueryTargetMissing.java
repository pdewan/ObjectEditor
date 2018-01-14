package util.trace.uigen.query;

import util.trace.Traceable;
import util.trace.query.TraceableIndices;
import util.trace.query.TraceableSearch;
import bus.uigen.query.AnObjectQuery;
import bus.uigen.query.ObjectQuery;

public class QueryTargetMissing extends TraceableSearch {
	public QueryTargetMissing(String aMessage, 
			Integer aTestIndex,
			Integer aReferenceIndex,
			ObjectQuery aPreviousObject, ObjectQuery anExpectedObject, ObjectQuery aLaterObject, Object aFinder) {
		super(aMessage, aTestIndex, aReferenceIndex, aPreviousObject, anExpectedObject, aLaterObject, aFinder);
	}
	public QueryTargetMissing(String aMessage, 
			Integer aTestIndex,
			Integer aReferenceIndex,
			
			ObjectQuery aPreviousObject,
			ObjectQuery anExpectedObject, ObjectQuery aLaterObject) {
		super(aMessage, aTestIndex, aReferenceIndex, aPreviousObject, anExpectedObject, aLaterObject);
	}
	public static String toString (Integer aTestIndex, Integer aReferenceIndex, Class aPreviousObject, Class anExpectedObject, Class aLaterObject) {
		return 
			TraceableIndices.toString(aTestIndex, aReferenceIndex) +
			((aPreviousObject == null)? "": PREVIOUS + Traceable.NESTED_LEFT_MARKER + (aPreviousObject.getName()) + Traceable.NESTED_RIGHT_MARKER)
			+ " "	
			+ ((anExpectedObject == null)? "": EXPECTED + Traceable.NESTED_LEFT_MARKER + (anExpectedObject.getName()) + Traceable.NESTED_RIGHT_MARKER)
			
			+ ((aLaterObject == null)? "": 
				LATER + Traceable.NESTED_LEFT_MARKER + (aLaterObject.getName()) + Traceable.NESTED_RIGHT_MARKER);
	}
	public static QueryTargetMissing toTraceable(String aMessage) {
		try {
			return new QueryTargetMissing (aMessage, 
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
	public static QueryTargetMissing newCase (
			Integer aTestIndex,
			Integer aReferenceIndex,
			ObjectQuery aPreviousObject, ObjectQuery anExpectedObject, ObjectQuery aLaterObject, Object aFinder) {
		String aMessage = TraceableSearch.toString(aTestIndex, aReferenceIndex, aPreviousObject, anExpectedObject, aLaterObject);
		QueryTargetMissing retVal = new QueryTargetMissing(aMessage, aTestIndex, aReferenceIndex, aPreviousObject, anExpectedObject, aLaterObject, aFinder);
		retVal.announce();
		return retVal;
	}
	

}
