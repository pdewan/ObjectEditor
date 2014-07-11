package bus.uigen.query;


import static bus.uigen.query.QueryUtility.inOrder;
import static bus.uigen.query.QueryUtility.traceablesToQueries;

import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.qdox.tools.QDoxTester.Reporter;

import bus.uigen.query.AnObjectQuery;
import bus.uigen.trace.TraceUtility;
import bus.uigen.trace.query.OrderedQueryTargetDisplaced;
import bus.uigen.trace.query.OrderedQueryTargetFound;
import bus.uigen.trace.query.OrderedQueryTargetMissing;
import bus.uigen.trace.query.QueryTargetFound;
import bus.uigen.trace.query.QueryTargetMissing;
import util.misc.Common;
import util.models.EqualPropertiesDefiner;
import util.trace.Traceable;
import util.trace.TraceableInfo;
import util.trace.TraceableLog;
import util.trace.Tracer;
import util.trace.console.ConsoleInput;
import util.trace.console.ConsoleOutput;
import util.trace.query.ClassInstanceFound;
import util.trace.query.ClassInstanceMissing;
import util.trace.query.EqualObjectFound;
import util.trace.query.EqualObjectMissing;
import util.trace.query.OrderedClassInstanceDisplaced;
import util.trace.query.OrderedClassInstanceFound;
import util.trace.query.OrderedClassInstanceMissing;
import util.trace.query.OrderedEqualObjectDisplaced;
import util.trace.query.OrderedEqualObjectFound;
import util.trace.query.OrderedEqualObjectMissing;
import util.trace.query.UnmatchedObject;

public class QueryUtility {
	
		
	public boolean allValid(List<Integer> anIndexList) {
		return indexOfInvalidIndex(anIndexList) < 0;		
	}
	public static  List<Class> missingClasses(
			Class[] anExpectedClasses, List<Integer> anInvalidIndices) {
		List<Class> retVal = new ArrayList();
		for (int i = 0; i < anInvalidIndices.size(); i++) {
			retVal.add(anExpectedClasses[i]);
		}
		return retVal;
	}
	
	public static Integer indexOfInvalidIndex(List<Integer> anIndexList, int aStartIndex) {
		for (int anIndexIndex = aStartIndex; anIndexIndex < anIndexList.size(); anIndexIndex++) {
			if (anIndexList.get(anIndexIndex) < 0) return anIndexIndex;
		}
		return -1;
	}
	
	public static Integer indexOfOutOfOrderIndex(/*List anObjectList,*/ List<Integer> anIndexList, int aStartIndex) {
//		if (anIndexList.size() == 1) {
//			BeanQuery anExpectedObject = anObjectList[anIndexList.get(anIndexList.get(0))];
//			if (anExpectedObject.isClassQuery()) {
//				ClassInstanceFound.newCase(null, anExpectedObject.getExpectedClass(), null);
//			} else {
//				ClassInstanceFound.newCase(null, anExpectedObject, null);
//			}
//		}
	
		for (int anIndexIndex = aStartIndex; anIndexIndex < (anIndexList.size() -1); anIndexIndex++) {
//			BeanQuery anExpectedObject = anObjectList[anIndexList.get(anIndexIndex)];
//			BeanQuery aPreviousObject = (anIndexList.get(aStartIndex) > 1)? anObjectList[anIndexList.get(aStartIndex -1)]:null;
//			BeanQuery aLaterObject = (anIndexList.get(anIndexIndex) < anObjectList.length - 1)?anObjectList[anIndexList.get(anIndexIndex+1)]:null;
			if (anIndexList.get(anIndexIndex) > anIndexList.get(anIndexIndex+1)) {
//				if (anExpectedObject.isClassQuery()) {
//					ClassInstanceMissing.newCase(aPreviousObject, anExpectedObject.getExpectedClass(), aLaterObject);
//				} else {
//					QueryTargetMissing.newCase(aPreviousObject, anExpectedObject, aLaterObject);
//				}
				return anIndexIndex;
			}
//			if (anExpectedObject.isClassQuery()) {
//				ClassInstanceFound.newCase(aPreviousObject, anExpectedObject.getExpectedClass(), aLaterObject);
//			} else {
//				ClassInstanceFound.newCase(aPreviousObject, anExpectedObject, aLaterObject);
//			}
			
		}
		return -1;
	}
	public static void traceOutOfOrderAndInOrderIndices(List anObjects, int aPrevOutOfOrderIndex, int aCurrentOutOfOderIndex ) {
		int startInOrderIndex = aPrevOutOfOrderIndex + 1;
		for (int i = startInOrderIndex; i < aCurrentOutOfOderIndex -1; i++ ) {
			Object inOrderObject = anObjects.get(i);
			Object prevObject = (i > 1)? anObjects.get(i -1):null;
			Object nextObject = (i < anObjects.size() - 1)?anObjects.get(i+1):null;
			
			
		}
	}
	public static List<Integer> indicesOfOutOfOrderIndices(/*List anObjectList,*/
			List<Integer> anIndexList) {
		List<Integer> retVal = new ArrayList();
		int aStartIndex = 0;
//		int aPreviousOutOfOrderIndex = -1;
		while (true) {
			if (aStartIndex >= anIndexList.size())
				return retVal;
			Integer nextOutOfOrderIndex = indexOfOutOfOrderIndex(/*nObjectList,*/ anIndexList, aStartIndex);			
			if (nextOutOfOrderIndex < 0) {
				
				return retVal;
			}
			retVal.add(nextOutOfOrderIndex);
			aStartIndex = nextOutOfOrderIndex+1;
			
		}
	}
//	public static List<Integer> indicesOfOutOfOrderIndices(Class[] aClassList,
//			List<Integer> anIndexList) {
//		List<Integer> retVal = new ArrayList();
//		int aStartIndex = 0;
////		int aPreviousOutOfOrderIndex = -1;
//		while (true) {
//			if (aStartIndex >= anIndexList.size())
//				return retVal;
//			Integer nextOutOfOrderIndex = indexOfOutOfOrderIndex(toQueries(aClassList), anIndexList, aStartIndex);			
//			if (nextOutOfOrderIndex < 0) {
//				
//				return retVal;
//			}
//			retVal.add(nextOutOfOrderIndex);
//			aStartIndex = nextOutOfOrderIndex+1;
//			
//		}
//	}
	// degree is actual position - expected position
	public static List<Integer> degreeOfOutOfOrderIndices(List<Integer> anIndexList, List<Integer> anOutOfOrderList) {
		List<Integer> retVal = new ArrayList();
		for (Integer anOutOfOrderIndex:anOutOfOrderList) {
			int indexOfOutOfOrderIndex = anIndexList.indexOf( anOutOfOrderIndex);
			retVal.add(anOutOfOrderIndex - indexOfOutOfOrderIndex);
		}
		return retVal;
		
	}
	
	public static List<Integer> indicesOfInvalidIndices(List<Integer> anIndexList) {
		List<Integer> retVal = new ArrayList();
		int aStartIndex = 0;
		while (true) {
			if (aStartIndex >= anIndexList.size())
				return retVal;
			Integer nextInvalidIndex = indexOfInvalidIndex(anIndexList, aStartIndex);
			if (nextInvalidIndex < 0)
				return retVal;
			retVal.add(nextInvalidIndex);
			aStartIndex = nextInvalidIndex+1;
			
		}
	}
	public static int indexOfInvalidIndex(List<Integer> anIndexList) {
		return indexOfInvalidIndex(anIndexList, 0);
	}
	// look for elements of query in the same order but not necessarily consecutive in the traceable list
		// allow for missing elements
	public static List<Integer>  indicesOf(List anObjectList, ObjectQuery[] aQueryList, int aStartIndex, int aStopIndex) {
		return indicesOf(anObjectList, aQueryList, true, aStartIndex, aStopIndex);
	}
	public static List<Integer>  indicesOf(List anObjectList, ObjectQuery[] aQueryList, boolean anOrderedQueryList, int aStartIndex) {
		return indicesOf(anObjectList, aQueryList, true, aStartIndex, anObjectList.size());
	}

	public void reportMissing(List anObjectList, ObjectQuery[] aQueryList, boolean anOrderedQueryList, int aQueryIndex) {
		
		
	}
	public static int findPreviousValidIndex (List<Integer> anIndexList, int aQueryIndex) {
		for (int i = aQueryIndex -1; i >= 0; i--) {
			if (anIndexList.get(i) >= 0)
				return i;
		}
		return -1;
	}
	public static ObjectQuery findPreviousValidObject (List<Integer> anIndexList, 
			int aQueryIndex,
			ObjectQuery[] aQueryList) {
		int anIndex = findPreviousValidIndex(anIndexList, aQueryIndex);
		return anIndex < 0? null:aQueryList[anIndex];
	}
	
	public static ObjectQuery findNextValidObject (List<Integer> anIndexList, 
			int aQueryIndex,
			ObjectQuery[]  aQueryList) {
		int anIndex = findNextValidIndex(anIndexList, aQueryIndex);
		return anIndex < 0? null:aQueryList[anIndex];
	}
	
	public static int findNextValidIndex (List<Integer> anIndexList, int aQueryIndex) {
		for (int i = aQueryIndex + 1; i < anIndexList.size(); i++) {
			if (anIndexList.get(i) >= 0)
				return i;
		}
		return -1;
	}
	
	public static void traceSearchResult(List anObjectList, ObjectQuery[] aQueryList,  boolean anOrderedQueryList, int aQueryIndex,  List<Integer> anIndexList) {
		ObjectQuery anExpectedObject = aQueryList[aQueryIndex];
		boolean aSuccess =  anIndexList.get(aQueryIndex) >= 0;		
		ObjectQuery aPreviousObject = findPreviousValidObject(anIndexList, aQueryIndex, aQueryList);
		ObjectQuery aLaterObject = findNextValidObject(anIndexList, aQueryIndex, aQueryList);
		if (aSuccess)
			traceSearchSuccess(anIndexList.get(aQueryIndex), aQueryIndex, anExpectedObject, aPreviousObject, aLaterObject, anOrderedQueryList);
		else
			traceSearchFailure(anIndexList.get(aQueryIndex), aQueryIndex, anExpectedObject, aPreviousObject, aLaterObject, anOrderedQueryList);
			
	}
	public static void traceOrderedSearchDisplacement(Integer aTestIndex, Integer aReferenceIndex, List anObjectList, ObjectQuery[] aQueryList,  int aQueryIndex,  List<Integer> anIndexList, Integer aDisplacement) {
		ObjectQuery anExpectedObject = aQueryList[aQueryIndex];
		boolean aSuccess =  anIndexList.get(aQueryIndex) >= 0;		
		ObjectQuery aPreviousObject = findPreviousValidObject(anIndexList, aQueryIndex, aQueryList);
		ObjectQuery aLaterObject = findNextValidObject(anIndexList, aQueryIndex, aQueryList);
		
		traceOrderedSearchDisplacement(anIndexList.get(aQueryIndex), aQueryIndex, anExpectedObject, aPreviousObject, aLaterObject, aDisplacement);
			
	}
	
	static void traceSearchSuccess(Integer aTestIndex, Integer aReferenceIndex, ObjectQuery anExpectedObject, ObjectQuery aPreviousObject, ObjectQuery aLaterObject, boolean anOrderedQueryList) {
		if (anOrderedQueryList)
			traceOrderedSearchSuccess(aTestIndex, aReferenceIndex, anExpectedObject, aPreviousObject, aLaterObject);
		else
			traceUnorderedSearchSuccess(aTestIndex, aReferenceIndex, anExpectedObject, aPreviousObject, aLaterObject);
	}
	
	public static void traceSearchFailure(Integer aTestIndex, Integer aReferenceIndex, ObjectQuery anExpectedObject, ObjectQuery aPreviousObject, ObjectQuery aLaterObject, boolean anOrderedQueryList) {
		if (anOrderedQueryList)
			traceOrderedSearchFailure(aTestIndex, aReferenceIndex, anExpectedObject, aPreviousObject, aLaterObject);
		else
			traceUnorderedSearchFailure(aTestIndex, aReferenceIndex, anExpectedObject, aPreviousObject, aLaterObject);
	}
	
	public static void traceOrderedSearchDisplacement(Integer aTestIndex, Integer aReferenceIndex, ObjectQuery anExpectedObject, ObjectQuery aPreviousObject, ObjectQuery aLaterObject, Integer aDisplacement) {
		if (anExpectedObject.isObjectQuery())
			OrderedEqualObjectDisplaced.newCase(aTestIndex, aReferenceIndex, expectedObject(aPreviousObject), expectedObject(anExpectedObject), expectedObject(aLaterObject), aDisplacement, QueryUtility.class);

		else if (anExpectedObject.isClassQuery()) 	
			OrderedClassInstanceDisplaced.newCase(aTestIndex, aReferenceIndex, expectedClass(aPreviousObject), expectedClass(anExpectedObject), expectedClass(aLaterObject), aDisplacement, QueryUtility.class);
		
		else
			OrderedQueryTargetDisplaced.newCase(aTestIndex, aReferenceIndex, aPreviousObject, anExpectedObject, aLaterObject, aDisplacement, QueryUtility.class);
	}
	
	public static void traceOrderedSearchFailure(Integer aTestIndex, Integer aReferenceIndex, ObjectQuery anExpectedObject, ObjectQuery aPreviousObject, ObjectQuery aLaterObject) {
		if (anExpectedObject.isObjectQuery())
			OrderedEqualObjectMissing.newCase(aTestIndex, aReferenceIndex, expectedObject(aPreviousObject), expectedObject(anExpectedObject), expectedObject(aLaterObject), QueryUtility.class);

		else if (anExpectedObject.isClassQuery()) 			
			OrderedClassInstanceMissing.newCase(aTestIndex, aReferenceIndex, expectedClass(aPreviousObject), expectedClass(anExpectedObject), expectedClass(aLaterObject), QueryUtility.class);
		else
			OrderedQueryTargetMissing.newCase(aTestIndex, aReferenceIndex, aPreviousObject, anExpectedObject, aLaterObject, QueryUtility.class);
	}
	public static void traceUnorderedSearchFailure(Integer aTestIndex, Integer aReferenceIndex, ObjectQuery anExpectedObject, ObjectQuery aPreviousObject, ObjectQuery aLaterObject) {
		if (anExpectedObject.isObjectQuery())
			EqualObjectMissing.newCase(aTestIndex, aReferenceIndex, expectedObject(aPreviousObject), expectedObject(anExpectedObject), expectedObject(aLaterObject), QueryUtility.class);
		else if (anExpectedObject.isClassQuery()) 			
			ClassInstanceMissing.newCase(aTestIndex, aReferenceIndex, expectedClass(aPreviousObject), expectedClass(anExpectedObject), expectedClass(aLaterObject), QueryUtility.class);
		else
			QueryTargetMissing.newCase(aTestIndex, aReferenceIndex, aPreviousObject, anExpectedObject, aLaterObject, QueryUtility.class);
	}
	
	public static Class expectedClass(ObjectQuery aBeanQuery) {
		return aBeanQuery == null?null:aBeanQuery.getExpectedClass();
	}
	
	public static Object expectedObject(ObjectQuery aBeanQuery) {
		return aBeanQuery == null?null:aBeanQuery.getExpectedObject();
	}
	
	public static void traceOrderedSearchSuccess(Integer aTestIndex, Integer aReferenceIndex, ObjectQuery anExpectedObject, ObjectQuery aPreviousObject, ObjectQuery aLaterObject) {
		if (anExpectedObject.isObjectQuery())
			OrderedEqualObjectFound.newCase(aTestIndex, aReferenceIndex, expectedObject(aPreviousObject), expectedObject(anExpectedObject), expectedObject(aLaterObject), QueryUtility.class);
		else if (anExpectedObject.isClassQuery()) 			
			OrderedClassInstanceFound.newCase(aTestIndex, aReferenceIndex, expectedClass(aPreviousObject), expectedClass(anExpectedObject), expectedClass(aLaterObject), QueryUtility.class);
		else
			OrderedQueryTargetFound.newCase(aTestIndex, aReferenceIndex, aPreviousObject, anExpectedObject, aLaterObject, QueryUtility.class);
	}
	public static void traceUnorderedSearchSuccess(Integer aTestIndex, Integer aReferenceIndex, ObjectQuery anExpectedObject, ObjectQuery aPreviousObject, ObjectQuery aLaterObject) {
		if (anExpectedObject.isObjectQuery())
			EqualObjectFound.newCase(aTestIndex, aReferenceIndex, expectedObject(aPreviousObject), expectedObject(anExpectedObject), expectedObject(aLaterObject), QueryUtility.class);
		else if (anExpectedObject.isClassQuery()) 			
			ClassInstanceFound.newCase(aTestIndex, aReferenceIndex, expectedClass(aPreviousObject), expectedClass(anExpectedObject), expectedClass(aLaterObject), QueryUtility.class);
		else
			QueryTargetFound.newCase(aTestIndex, aReferenceIndex, aPreviousObject, anExpectedObject, aLaterObject, QueryUtility.class);
	}
	
	
	
	public static void traceSearchResults(List anObjectList, ObjectQuery[] aQueryList, boolean anOrderedQueryList, List<Integer> anIndexList) {
		for (int aQueryIndex = 0; aQueryIndex < aQueryList.length; aQueryIndex++) {
			traceSearchResult(anObjectList, aQueryList, anOrderedQueryList, aQueryIndex, anIndexList);
		}
	}
	public static int max (List<Integer> anIndexList, Integer aStartIndex, Integer aStopIndex) {
		Integer retVal = 0;
		for (int i = aStartIndex; i < aStopIndex; i++) {
			int curVal = anIndexList.get(i);
			if (curVal > retVal)
				retVal = curVal;
		}		
		return retVal;
	}
	// look for elements of query in between the start and stop index
	// allows for missing elements
	public static List<Integer>  indicesOf(List anObjectList, ObjectQuery[] aQueryList, boolean anOrderedQueryList, int aStartIndex, int aStopIndex) {
		List<Integer> retVal = new ArrayList(aQueryList.length);
		int aCurrentStartIndex = aStartIndex;
		boolean aFoundMissing = false;
		for (int aQueryIndex = 0; aQueryIndex < aQueryList.length && aCurrentStartIndex < aStopIndex; aQueryIndex++) {
			
			int aReturnIndex = indexOf(anObjectList, aQueryList[aQueryIndex], aCurrentStartIndex, aStopIndex);
			retVal.add(aReturnIndex);
			if (aReturnIndex < 0) {
				aFoundMissing = true;
			}
			if (aReturnIndex >= 0 && anOrderedQueryList)
				aCurrentStartIndex = aReturnIndex + 1; 
			// else look for the next matching element after same index
		}
		List<Integer> unOrderedIndexList = null;
		traceSearchResults(anObjectList, aQueryList, anOrderedQueryList, retVal);
		if (aFoundMissing && anOrderedQueryList) {
			// try again to find which elements were not even in the range
			unOrderedIndexList = indicesOf(anObjectList, aQueryList, false, aStartIndex, aStopIndex);
			// we can now find the separation  between actual and real position
			for (int i = 0; i < aQueryList.length; i++) {
				if (retVal.get(i) < 0 && unOrderedIndexList.get(i) >= 0) { // not in order
					// the displacement is actual position  - the position of the max in order index before this element
					
					int aDisplacement;
					aDisplacement = unOrderedIndexList.get(i) - i;
//					if ( i == 0)
//						aDisplacement = unOrderedIndexList.get(i);
//					else
//						aDisplacement = unOrderedIndexList.get(i) - max(retVal, 0, i-1);
					
					traceOrderedSearchDisplacement(i, retVal.get(i), anObjectList, aQueryList, i, retVal, aDisplacement);
				}
					
				
			}
		}
		if (anOrderedQueryList) {
			for (int i=0; i< anObjectList.size(); i++) {
				boolean matched = (unOrderedIndexList == null)?
									retVal.contains(i):
									unOrderedIndexList.contains(i);
				if (!matched) {
					UnmatchedObject.newCase(i, -1, anObjectList.get(i), QueryUtility.class);
				}
			}
		}
		return retVal;	
	}
	public static ObjectQuery[] toQueries (Class[] aClassList) {
		ObjectQuery[] aQueryList = new ObjectQuery[aClassList.length];
		for (int aClassIndex = 0; aClassIndex < aClassList.length; aClassIndex++) {
			aQueryList[aClassIndex] = new AnObjectQuery(aClassList[aClassIndex]);
		}
		return aQueryList;
	}
	public static ObjectQuery[] toQueries (EqualPropertiesDefiner[] anObjectList) {
		ObjectQuery[] aQueryList = new ObjectQuery[anObjectList.length];
		for (int anObjectIndex = 0; anObjectIndex < anObjectList.length; anObjectIndex++) {
			aQueryList[anObjectIndex] = new AnObjectQuery(anObjectList[anObjectIndex]);
		}
		return aQueryList;
	}
	public static ObjectQuery[] toQueries (Object[] anObjectList) {
		ObjectQuery[] aQueryList = new ObjectQuery[anObjectList.length];
		for (int anObjectIndex = 0; anObjectIndex < anObjectList.length; anObjectIndex++) {
			aQueryList[anObjectIndex] = new AnObjectQuery(anObjectList[anObjectIndex]);
		}
		return aQueryList;
	}
	public static ObjectQuery[] toQueries (Object[] anObjectList, String[] aProperties) {
		ObjectQuery[] aQueryList = new ObjectQuery[anObjectList.length];
		for (int anObjectIndex = 0; anObjectIndex < anObjectList.length; anObjectIndex++) {
			aQueryList[anObjectIndex] = new AnObjectQuery(anObjectList[anObjectIndex], aProperties);
		}
		return aQueryList;
	}
	
	public static ObjectQuery[] toQueries(List<Object> anObjectList) {
		Object[] anObjectArrayList = anObjectList.toArray(new Object[anObjectList.size()]);
		return toQueries(anObjectArrayList);
	}
	public static ObjectQuery[] equalPropertiesDefinerToQueries(List<EqualPropertiesDefiner> anObjectList) {
		EqualPropertiesDefiner[] anObjectArrayList = anObjectList.toArray(new EqualPropertiesDefiner[anObjectList.size()]);
		return toQueries(anObjectArrayList);
	}
	public static ObjectQuery[] traceablesToQueries(List<Traceable> anObjectList) {
		Traceable[] anObjectArrayList = anObjectList.toArray(new Traceable[anObjectList.size()]);
		return toQueries(anObjectArrayList);
	}
	public static ObjectQuery[] toQueries(List<Object> anObjectList, String[] aProperties) {
		Object[] anObjectArrayList = anObjectList.toArray(new Object[anObjectList.size()]);
		return toQueries(anObjectArrayList, aProperties);
	}
	public static List<Integer>  indicesOf(List anObjectList, Class[] aClassList, boolean anOrderedQueryList, int aStartIndex, int aStopIndex) {
//		BeanQuery[] aQueryList = new BeanQuery[aClassList.length];
//		for (int aClassIndex = 0; aClassIndex < aClassList.length; aClassIndex++) {
//			aQueryList[aClassIndex] = new ABeanQuery(aClassList[aClassIndex]);
//		}
		return indicesOf(anObjectList, toQueries(aClassList), anOrderedQueryList, aStartIndex, aStopIndex);
	}
	public static List<Integer>  indicesOf(List anObjectList, Object[] aSecondObjectList, boolean anOrderedQueryList, int aStartIndex, int aStopIndex) {

		return indicesOf(anObjectList, toQueries(aSecondObjectList), anOrderedQueryList, aStartIndex, aStopIndex);
	}
	public static List<Integer>  indicesOf(Object[] anObjectList, Object[] aSecondObjectList, boolean anOrderedQueryList, int aStartIndex, int aStopIndex) {

		return indicesOf(Common.arrayToArrayList(anObjectList), toQueries(aSecondObjectList), anOrderedQueryList, aStartIndex, aStopIndex);
	}
	public static List<Integer>  indicesOf(Object[] anObjectList, Object[] aSecondObjectList, boolean anOrderedQueryList, int aStartIndex) {

		return indicesOf(Common.arrayToArrayList(anObjectList), toQueries(aSecondObjectList), anOrderedQueryList, aStartIndex, aSecondObjectList.length);
	}
	
	public static List<Integer>  indicesOf(Object[] anObjectList, Object[] aSecondObjectList, boolean anOrderedQueryList) {

		return indicesOf(Common.arrayToArrayList(anObjectList), toQueries(aSecondObjectList), anOrderedQueryList, 0, aSecondObjectList.length);
	}
	
	
	public static List<Integer>  indicesOf(List anObjectList, List aSecondObjectList, boolean anOrderedQueryList, int aStartIndex, int aStopIndex) {

		return indicesOf(anObjectList, 
				toQueries(aSecondObjectList.toArray(new Object[aSecondObjectList.size()])), 
				anOrderedQueryList, aStartIndex, aStopIndex);
	}
	public static List<Integer>  indicesOf(List anObjectList, List aSecondObjectList, boolean anOrderedQueryList, int aStartIndex) {

		return indicesOf(anObjectList, aSecondObjectList, anOrderedQueryList, aStartIndex, aSecondObjectList.size());
	}
	public static List<Integer>  indicesOf(List anObjectList, List aSecondObjectList, boolean anOrderedQueryList) {

		return indicesOf(anObjectList, aSecondObjectList, anOrderedQueryList, 0, aSecondObjectList.size());
	}
	
	
	
	
	public static List<Integer>  indicesOf(List anObjectList, Class[] aClassList, int aStartIndex, int aStopIndex) {
		return indicesOf(anObjectList, aClassList, true, aStartIndex, aStopIndex);
	}
	public static List<Integer>  indicesOf(List anObjectList, Class[] aClassList, boolean aOrderedQueryList, int aStartIndex) {
		return indicesOf(anObjectList, aClassList, aOrderedQueryList, aStartIndex, anObjectList.size());
	}
	public static List<Integer>  indicesOf(List anObjectList, Class[] aClassList, boolean aOrderedQueryList) {
		return indicesOf(anObjectList, aClassList, aOrderedQueryList, 0, anObjectList.size());
	}
	
	public static List<Integer>  indicesOf(List anObjectList, Class[] aClassList, int aStartIndex) {
		return indicesOf(anObjectList, aClassList, aStartIndex, anObjectList.size());
	}
	public static List<Integer>  indicesOf(List anObjectList, Class[] aClassList) {
		return indicesOf(anObjectList, aClassList, 0, anObjectList.size());
	}
	
	public static Integer indexOf(List anObjectList, ObjectQuery aQuery, int aStartIndex, int aStopIndex) {
		
		for (int anIndex = aStartIndex; anIndex < aStopIndex; anIndex++) {
			if (aQuery.matches(anObjectList.get(anIndex)))
				return anIndex;
		}
		return -1;	
	}
	public static List<Integer> indicesOf(List anObjectList, ObjectQuery aQuery, int aStartIndex, int aStopIndex) {
		List<Integer> retVal = new ArrayList();
		int aNextStartIndex = aStartIndex;
		while (true) {
			Integer aNextIndex = indexOf(anObjectList, aQuery, aStartIndex, aStopIndex);
			
			retVal.add(aNextIndex);
			aNextStartIndex = aNextIndex + 1;
			if (aNextStartIndex >= aStopIndex)
				return retVal;
		}
	}
	public static List<Integer> indicesOf(List anObjectList, Class aClass, int aStartIndex, int aStopIndex) {
		List<Integer> retVal = new ArrayList();
		int aNextStartIndex = aStartIndex;
		while (true) {
			Integer aNextIndex = indexOf(anObjectList, aClass, aStartIndex, aStopIndex);
			if (aNextIndex == -1)
				return retVal;
			retVal.add(aNextIndex);
			aNextStartIndex = aNextIndex + 1;
			if (aNextStartIndex >= aStopIndex)
				return retVal;
		}
	}
	public static List<Integer> indicesOf(List anObjectList, Class aClass, int aStartIndex) {
		return indicesOf(anObjectList, aClass, aStartIndex, anObjectList.size());
	}
	public static List<Integer> indicesOf(List anObjectList, Class aClass) {
		return indicesOf(anObjectList, aClass, 0, anObjectList.size());
	}

	public static List<Integer> indicesOf(List anObjectList, ObjectQuery aQuery, int aStartIndex) {
		return indicesOf(anObjectList, aQuery, aStartIndex, anObjectList.size());
	}
	public static List<Integer> indicesOf(List anObjectList, ObjectQuery aQuery) {
		return indicesOf(anObjectList, aQuery, 0, anObjectList.size());
	}
	public static Integer indexOf(List anObjectList, Class aClass, int aStartIndex, int aStopIndex) {
		return indexOf(anObjectList, new AnObjectQuery(aClass), aStartIndex, aStopIndex);
	}
	
	public static Integer indexOf (List anObjectList, Class aClass, int aStartIndex) {
		return indexOf(anObjectList, aClass, aStartIndex, anObjectList.size());
		

	}
	public static boolean valid(List<Integer> anIndexList) {
		List<Integer> anInvalidIndices = indicesOfInvalidIndices(anIndexList);
		if (anInvalidIndices.size() != 0) {
//			System.out.println("Missing events:" + missingClasses(anExpectedClasses, anInvalidIndices));
			return false;
		}
		return true;
		
	}
	public static boolean inOrder(/*List anObjectList,*/ List<Integer> anIndexList) {
		List<Integer> anOutOfOrderList = indicesOfOutOfOrderIndices(/*anObjectList,*/ anIndexList);
		if (anOutOfOrderList.size() != 0) {
			return false;
		}


		return true;
		
	}
//	public static boolean inOrder(Class[] anObjectList, List<Integer> anIndexList) {
//		List<Integer> anOutOfOrderList = indicesOfOutOfOrderIndices(anObjectList, anIndexList);
//		if (anOutOfOrderList.size() != 0) {
//			return false;
//		}
//
//
//		return true;
//		
//	}
	public static boolean inOrder(List anObjectList, ObjectQuery[] aQueryList,  int aStartIndex, int aStopIndex) {
//		List<Integer> anIndexList = indicesOf(anObjectList, aQueryList, false, aStartIndex, aStopIndex);
//		return valid(anIndexList) & inOrder(/*anObjectList,*/ anIndexList); // want both computed
		List<Integer> anOrderedIndexList = indicesOf(anObjectList, aQueryList, true, aStartIndex, aStopIndex);
		boolean orderedValid = valid(anOrderedIndexList);
		if (orderedValid) return true; 
		// do another search to see what elements were found but not in order
		List<Integer> anIndexList = indicesOf(anObjectList, aQueryList, false, aStartIndex, aStopIndex);
		return valid(anIndexList);
	}
	public static boolean inOrder(List anObjectList, Object[] aSecondList,  int aStartIndex, int aStopIndex) {
//		List<Integer> anIndexList = indicesOf(anObjectList, aQueryList, false, aStartIndex, aStopIndex);
//		return valid(anIndexList) & inOrder(/*anObjectList,*/ anIndexList); // want both computed
		List<Integer> anOrderedIndexList = indicesOf(anObjectList, aSecondList, true, aStartIndex, aStopIndex);
		boolean orderedValid = valid(anOrderedIndexList);
		if (orderedValid) return true; 
		// do another search to see what elements were found but not in order
		List<Integer> anIndexList = indicesOf(anObjectList, aSecondList, false, aStartIndex, aStopIndex);
		return valid(anIndexList);
	}
	public static boolean inOrder(Object[] anObjectList, Object[] aSecondList,  int aStartIndex, int aStopIndex) {
//		List<Integer> anIndexList = indicesOf(anObjectList, aQueryList, false, aStartIndex, aStopIndex);
//		return valid(anIndexList) & inOrder(/*anObjectList,*/ anIndexList); // want both computed
		List<Integer> anOrderedIndexList = indicesOf(anObjectList, aSecondList, true, aStartIndex, aStopIndex);
		boolean orderedValid = valid(anOrderedIndexList);
		if (orderedValid) return true; 
		// do another search to see what elements were found but not in order
		List<Integer> anIndexList = indicesOf(anObjectList, aSecondList, false, aStartIndex, aStopIndex);
		return valid(anIndexList);
	}
	
	
	public static boolean inOrder(List anObjectList, Class[] anExpectedClasses,  int aStartIndex, int aStopIndex) {
		List<Integer> anOrderedIndexList = indicesOf(anObjectList, anExpectedClasses, true, aStartIndex, aStopIndex);
		boolean orderedValid = valid(anOrderedIndexList);
		if (orderedValid) return true; 
		// do another search to see what elements were found but not in order
		List<Integer> anIndexList = indicesOf(anObjectList, anExpectedClasses, false, aStartIndex, aStopIndex);
		return valid(anIndexList);
//
//		boolean inOrder = inOrder(/*anObjectList,*/ anOrderedIndexList);
//		return  orderedValid && inOrder;
	}
	
//	public static boolean inOrder(List anObjectList, Class[] anExpectedClasses,  int aStartIndex, int aStopIndex) {
//		List<Integer> anIndexList = indicesOf(anObjectList, anExpectedClasses, false, aStartIndex, aStopIndex);
//		boolean valid = valid(anIndexList);
//		boolean inOrder = inOrder(/*anObjectList,*/ anIndexList);
//		return  valid && inOrder;
//	}
	
	public static boolean inOrder(List anObjectList, ObjectQuery[] aQueryList,  int aStartIndex) {
		return inOrder(anObjectList, aQueryList, aStartIndex, anObjectList.size());
	}
	public static boolean inOrder(List anObjectList, Class[] aTargetClasses,  int aStartIndex) {
		return inOrder(anObjectList, aTargetClasses, aStartIndex, anObjectList.size());
	}
	public static boolean inOrder(List anObjectList, Object[] aSecondList,  int aStartIndex) {
		return inOrder(anObjectList, aSecondList, aStartIndex, anObjectList.size());
	}
	public static boolean inOrder(Object[] anObjectList, Object[] aSecondList,  int aStartIndex) {
		return inOrder(anObjectList, aSecondList, aStartIndex, anObjectList.length);
	}
	public static boolean inOrder(Object[] anObjectList, Object[] aSecondList) {
		return inOrder(anObjectList, aSecondList, 0, anObjectList.length);
	}
	public static boolean inOrder(List anObjectList, ObjectQuery[] aQueryList) {
		return inOrder(anObjectList, aQueryList, 0);
	}
	public static boolean inOrder(
			List<Traceable> aTestTraceableList, 
			List<Traceable> aCorrectTraceableList, Class[] anExpectedClasses) {
//		Class[] anExpectedClasses = {ConsoleOutput.class};
		List<Traceable> aFilteredTest = TraceUtility.filterTraceList(aTestTraceableList, anExpectedClasses);		
		List<Traceable> aFilteredCorrect = TraceUtility.filterTraceList(aCorrectTraceableList, anExpectedClasses);
		
//		TraceableLog traceableLog = TraceUtility.startNewTrace();
//		String[] aProperties = {"Output"};
		
		ObjectQuery[] objectQueries = traceablesToQueries(aFilteredCorrect);
		boolean retVal = inOrder(aFilteredTest, objectQueries);
//		TraceUtility.stopExistingTrace(traceableLog);

		return retVal;
		
		
	}
	public static boolean inOrder(List anObjectList, Class[] aTargetClasses) {
		return inOrder(anObjectList, aTargetClasses, 0);
	}
	
	public static List toObjectList (List anObjectList, List<Integer> anIndexList) {
		List retVal = new ArrayList();
		for (Integer anIndex: anIndexList) {
			if (anIndex >= 0)
				retVal.add(anObjectList.get(anIndex));
			else
				retVal.add(null);
		}
		return retVal;
	}
	public static boolean matches (Object anActualBean, Object anExpectedBean, String[] aProperties) {
		return (new AnObjectQuery(anExpectedBean, aProperties)).matches(anActualBean);
	}
	public static boolean matches (List anActualBeans, Object anExpectedBean, String[] aProperties) {
		boolean retVal = true;
		for (Object anActualBean:anActualBeans) {
			if (!matches (anActualBean, anExpectedBean, aProperties)) {
				retVal = false;
			}
		}
		return true;
	}
	public static boolean matches (List anActualBeans, String[] aProperties) {
		if (anActualBeans.size() <= 1) return true;
		Object anExpectedBean = anActualBeans.get(0);
//		anActualBeans.remove(0); // ouch inefficient!			
		return matches(anActualBeans, anExpectedBean, aProperties);
	}

}
