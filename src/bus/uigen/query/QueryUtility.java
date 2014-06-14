package bus.uigen.query;


import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import bus.uigen.query.ABeanQuery;
import bus.uigen.query.BeanQuery;
import util.misc.Common;
import util.trace.Traceable;
import util.trace.TraceableInfo;
import util.trace.Tracer;
import util.trace.console.ConsoleInput;
import util.trace.console.ConsoleOutput;

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
	
	public static Integer indexOfOutOfOrderIndex(List<Integer> anIndexList, int aStartIndex) {
	
		for (int anIndexIndex = aStartIndex; anIndexIndex < (anIndexList.size() -1); anIndexIndex++) {
			if (anIndexList.get(anIndexIndex) > anIndexList.get(anIndexIndex+1)) return anIndexIndex;
		}
		return -1;
	}
	public static List<Integer> indicesOfOutOfOrderIndices(List<Integer> anIndexList) {
		List<Integer> retVal = new ArrayList();
		int aStartIndex = 0;
		while (true) {
			if (aStartIndex >= anIndexList.size())
				return retVal;
			Integer nextOutOfOrderIndex = indexOfOutOfOrderIndex(anIndexList, aStartIndex);
			if (nextOutOfOrderIndex < 0)
				return retVal;
			retVal.add(nextOutOfOrderIndex);
			aStartIndex = nextOutOfOrderIndex+1;
			
		}
	}
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
	public static List<Integer>  indicesOf(List anObjectList, BeanQuery[] aQueryList, int aStartIndex, int aStopIndex) {
		return indicesOf(anObjectList, aQueryList, true, aStartIndex, aStopIndex);
	}
	public static List<Integer>  indicesOf(List anObjectList, BeanQuery[] aQueryList, boolean anOrderedQueryList, int aStartIndex) {
		return indicesOf(anObjectList, aQueryList, true, aStartIndex, anObjectList.size());
	}

	
	// look for elements of query in between the start and stop index
	// allows for missing elements
	public static List<Integer>  indicesOf(List anObjectList, BeanQuery[] aQueryList, boolean anOrderedQueryList, int aStartIndex, int aStopIndex) {
		List<Integer> retVal = new ArrayList(aQueryList.length);
		int aCurrentStartIndex = aStartIndex;
		for (int aQueryIndex = 0; aQueryIndex < aQueryList.length && aCurrentStartIndex < aStopIndex; aQueryIndex++) {
			
			int aRetVal = indexOf(anObjectList, aQueryList[aQueryIndex], aCurrentStartIndex, aStopIndex);
			retVal.add(aRetVal);
			if (aRetVal >= 0 && anOrderedQueryList)
				aCurrentStartIndex = aRetVal + 1; 
			// else look for the next matching element after same index
		}
		return retVal;	
	}
	public static List<Integer>  indicesOf(List anObjectList, Class[] aClassList, boolean anOrderedQueryList, int aStartIndex, int aStopIndex) {
		BeanQuery[] aQueryList = new BeanQuery[aClassList.length];
		for (int aClassIndex = 0; aClassIndex < aClassList.length; aClassIndex++) {
			aQueryList[aClassIndex] = new ABeanQuery(aClassList[aClassIndex]);
		}
		return indicesOf(anObjectList, aQueryList, anOrderedQueryList, aStartIndex, aStopIndex);
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
	
	public static Integer indexOf(List anObjectList, BeanQuery aQuery, int aStartIndex, int aStopIndex) {
		
		for (int anIndex = aStartIndex; anIndex < aStopIndex; anIndex++) {
			if (aQuery.matches(anObjectList.get(anIndex)))
				return anIndex;
		}
		return -1;	
	}
	public static Integer indexOf(List anObjectList, Class aClass, int aStartIndex, int aStopIndex) {
		return indexOf(anObjectList, new ABeanQuery(aClass), aStartIndex, aStopIndex);
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
	public static boolean inOrder(List<Integer> anIndexList) {
		List<Integer> anOutOfOrderList = indicesOfOutOfOrderIndices(anIndexList);
		if (anOutOfOrderList.size() != 0) {
			return false;
		}


		return true;
		
	}
	public static boolean inOrder(List anObjectList, BeanQuery[] aQueryList,  int aStartIndex, int aStopIndex) {
		List<Integer> anIndexList = indicesOf(anObjectList, aQueryList, false, aStartIndex, aStopIndex);
		return valid(anIndexList) & inOrder(anIndexList); // want both computed
	}
	
	public static boolean inOrder(List anObjectList, Class[] anExpectedClasses,  int aStartIndex, int aStopIndex) {
		List<Integer> anIndexList = indicesOf(anObjectList, anExpectedClasses, false, aStartIndex, aStopIndex);
		boolean valid = valid(anIndexList);
		boolean inOrder = inOrder(anIndexList);
		return  valid && inOrder;
	}
	public static boolean inOrder(List anObjectList, BeanQuery[] aQueryList,  int aStartIndex) {
		return inOrder(anObjectList, aQueryList, aStartIndex, anObjectList.size());
	}
	public static boolean inOrder(List anObjectList, Class[] aTargetClasses,  int aStartIndex) {
		return inOrder(anObjectList, aTargetClasses, aStartIndex, anObjectList.size());
	}
	public static boolean inOrder(List anObjectList, BeanQuery[] aQueryList) {
		return inOrder(anObjectList, aQueryList, 0);
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
		return (new ABeanQuery(anExpectedBean, aProperties)).matches(anActualBean);
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
