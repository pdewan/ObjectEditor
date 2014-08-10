package bus.uigen.trace;



import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import bus.uigen.query.AnObjectQuery;
import bus.uigen.query.ObjectQuery;
import util.misc.Common;
import util.trace.ATraceableLog;
import util.trace.Traceable;
import util.trace.TraceableBus;
import util.trace.TraceableInfo;
import util.trace.TraceableLog;
import util.trace.Tracer;
import util.trace.console.ConsoleInput;
import util.trace.console.ConsoleOutput;

public class TraceUtility {
	
	public static Traceable toTraceable(String aMessage) {
//		if (!aMessage.startsWith("I***"))
//			continue;
		if (!Tracer.isInfo(aMessage) || Tracer.isShowInfo(aMessage))
			return null;
		try {
		Class traceableClass = TraceableInfo.toEvtTypeClass(aMessage);
		if (traceableClass == null) {
			System.err.println("Could not find traceable class for message:" + aMessage);
			return null;
		}
		Class[] parameterTypes = {String.class}; 
		Method parsingMethod = traceableClass.getMethod("toTraceable", parameterTypes);
		return (Traceable) parsingMethod.invoke(traceableClass, aMessage);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	public static List<Traceable> toTraceableList(String aFileName) {
		try {
		FileInputStream fis = new FileInputStream(aFileName);
        Scanner scanner = new Scanner(fis);  
		
       List<Traceable> retVal = new ArrayList();      
        while(scanner.hasNextLine()){
        	try {
        		String aMessage = scanner.nextLine();
        		Traceable newElement = toTraceable(aMessage);
        		if (newElement != null)
        			retVal.add(newElement);
//        		if (!aMessage.startsWith("I***"))
//        			continue;
//        		if (!Tracer.isInfo(aMessage))
//        			continue;
//        		Class traceableClass = TraceableInfo.toEvtTypeClass(aMessage);
//        		Class[] parameterTypes = {String.class}; 
//        		Method parsingMethod = traceableClass.getMethod("toTraceable", parameterTypes);
//        		Traceable newElement =  (Traceable) parsingMethod.invoke(traceableClass, aMessage);
//        		retVal.add(newElement);
        		
        	} catch (Exception e) {
        		e.printStackTrace();
        		
        	}	
        }
        return retVal;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;		
	}
	
	public static TraceableLog startNewTrace() {
		TraceableLog retVal = new ATraceableLog(100);
		TraceableBus.addTraceableListener(retVal);
		return retVal;
	}
	
		
	public static void stopExistingTrace(TraceableLog aTraceableLog) {
		TraceableBus.removeTraceableListener(aTraceableLog);
	}
	
	public static List<Traceable> filterTraceList(List<Traceable> aTraceList, Class[] aClasses) {
		List<Class> aClassesList = Arrays.asList(aClasses);
		List<Traceable> retVal = new ArrayList();
		for (Traceable aTraceable:aTraceList) {
			if (aClassesList.contains(aTraceable.getClass())) {
				retVal.add(aTraceable);
			}
		}
		return retVal;
	}
	
	
//	public boolean allValid(List<Integer> anIndexList) {
//		return indexOfInvalidIndex(anIndexList) < 0;		
//	}
//	public static  List<Class> missingClasses(
//			Class[] anExpectedClasses, List<Integer> anInvalidIndices) {
//		List<Class> retVal = new ArrayList();
//		for (int i = 0; i < anInvalidIndices.size(); i++) {
//			retVal.add(anExpectedClasses[i]);
//		}
//		return retVal;
//	}
//	
//	public static Integer indexOfInvalidIndex(List<Integer> anIndexList, int aStartIndex) {
//		for (int anIndexIndex = aStartIndex; anIndexIndex < anIndexList.size(); anIndexIndex++) {
//			if (anIndexList.get(anIndexIndex) < 0) return anIndexIndex;
//		}
//		return -1;
//	}
//	
//	public static Integer indexOfOutOfOrderIndex(List<Integer> anIndexList, int aStartIndex) {
//	
//		for (int anIndexIndex = aStartIndex; anIndexIndex < (anIndexList.size() -1); anIndexIndex++) {
//			if (anIndexList.get(anIndexIndex) > anIndexList.get(anIndexIndex+1)) return anIndexIndex;
//		}
//		return -1;
//	}
//	public static List<Integer> indicesOfOutOfOrderIndices(List<Integer> anIndexList) {
//		List<Integer> retVal = new ArrayList();
//		int aStartIndex = 0;
//		while (true) {
//			if (aStartIndex >= anIndexList.size())
//				return retVal;
//			Integer nextOutOfOrderIndex = indexOfOutOfOrderIndex(anIndexList, aStartIndex);
//			if (nextOutOfOrderIndex < 0)
//				return retVal;
//			retVal.add(nextOutOfOrderIndex);
//			aStartIndex = nextOutOfOrderIndex+1;
//			
//		}
//	}
//	// degree is actual position - expected position
//	public static List<Integer> degreeOfOutOfOrderIndices(List<Integer> anIndexList, List<Integer> anOutOfOrderList) {
//		List<Integer> retVal = new ArrayList();
//		for (Integer anOutOfOrderIndex:anOutOfOrderList) {
//			int indexOfOutOfOrderIndex = anIndexList.indexOf( anOutOfOrderIndex);
//			retVal.add(anOutOfOrderIndex - indexOfOutOfOrderIndex);
//		}
//		return retVal;
//		
//	}
//	
//	public static List<Integer> indicesOfInvalidIndices(List<Integer> anIndexList) {
//		List<Integer> retVal = new ArrayList();
//		int aStartIndex = 0;
//		while (true) {
//			if (aStartIndex >= anIndexList.size())
//				return retVal;
//			Integer nextInvalidIndex = indexOfInvalidIndex(anIndexList, aStartIndex);
//			if (nextInvalidIndex < 0)
//				return retVal;
//			retVal.add(nextInvalidIndex);
//			aStartIndex = nextInvalidIndex+1;
//			
//		}
//	}
//	public static int indexOfInvalidIndex(List<Integer> anIndexList) {
//		return indexOfInvalidIndex(anIndexList, 0);
//	}
//	// look for elements of query in the same order but not necessarily consecutive in the traceable list
//		// allow for missing elements
//	public static List<Integer>  indicesOf(List<Traceable> aTraceableList, BeanQuery[] aQueryList, int aStartIndex, int aStopIndex) {
//		return indicesOf(aTraceableList, aQueryList, true, aStartIndex, aStopIndex);
//	}
//	public static List<Integer>  indicesOf(List<Traceable> aTraceableList, BeanQuery[] aQueryList, boolean anOrderedQueryList, int aStartIndex) {
//		return indicesOf(aTraceableList, aQueryList, true, aStartIndex, aTraceableList.size());
//	}
//
//	
//	// look for elements of query in between the start and stop index
//	// allows for missing elements
//	public static List<Integer>  indicesOf(List<Traceable> aTraceableList, BeanQuery[] aQueryList, boolean anOrderedQueryList, int aStartIndex, int aStopIndex) {
//		List<Integer> retVal = new ArrayList(aQueryList.length);
//		int aCurrentStartIndex = aStartIndex;
//		for (int aQueryIndex = 0; aQueryIndex < aQueryList.length && aCurrentStartIndex < aStopIndex; aQueryIndex++) {
//			
//			int aRetVal = indexOf(aTraceableList, aQueryList[aQueryIndex], aCurrentStartIndex, aStopIndex);
//			retVal.add(aRetVal);
//			if (aRetVal >= 0 && anOrderedQueryList)
//				aCurrentStartIndex = aRetVal + 1; 
//			// else look for the next matching element after same index
//		}
//		return retVal;	
//	}
//	public static List<Integer>  indicesOf(List<Traceable> aTraceableList, Class[] aClassList, boolean anOrderedQueryList, int aStartIndex, int aStopIndex) {
//		BeanQuery[] aQueryList = new BeanQuery[aClassList.length];
//		for (int aClassIndex = 0; aClassIndex < aClassList.length; aClassIndex++) {
//			aQueryList[aClassIndex] = new ABeanQuery(aClassList[aClassIndex]);
//		}
//		return indicesOf(aTraceableList, aQueryList, anOrderedQueryList, aStartIndex, aStopIndex);
//	}
//	public static List<Integer>  indicesOf(List<Traceable> aTraceableList, Class[] aClassList, int aStartIndex, int aStopIndex) {
//		return indicesOf(aTraceableList, aClassList, true, aStartIndex, aStopIndex);
//	}
//	public static List<Integer>  indicesOf(List<Traceable> aTraceableList, Class[] aClassList, boolean aOrderedQueryList, int aStartIndex) {
//		return indicesOf(aTraceableList, aClassList, aOrderedQueryList, aStartIndex, aTraceableList.size());
//	}
//	public static List<Integer>  indicesOf(List<Traceable> aTraceableList, Class[] aClassList, boolean aOrderedQueryList) {
//		return indicesOf(aTraceableList, aClassList, aOrderedQueryList, 0, aTraceableList.size());
//	}
//	
//	public static List<Integer>  indicesOf(List<Traceable> aTraceableList, Class[] aClassList, int aStartIndex) {
//		return indicesOf(aTraceableList, aClassList, aStartIndex, aTraceableList.size());
//	}
//	public static List<Integer>  indicesOf(List<Traceable> aTraceableList, Class[] aClassList) {
//		return indicesOf(aTraceableList, aClassList, 0, aTraceableList.size());
//	}
//	
//	public static Integer indexOf(List<Traceable> aTraceableList, BeanQuery aQuery, int aStartIndex, int aStopIndex) {
//		
//		for (int anIndex = aStartIndex; anIndex < aStopIndex; anIndex++) {
//			if (aQuery.matches(aTraceableList.get(anIndex)))
//				return anIndex;
//		}
//		return -1;	
//	}
//	public static Integer indexOf(List<Traceable> aTraceableList, Class aClass, int aStartIndex, int aStopIndex) {
//		return indexOf(aTraceableList, new ABeanQuery(aClass), aStartIndex, aStopIndex);
//	}
//	
//	public static Integer indexOf (List<Traceable> aTraceableList, Class aClass, int aStartIndex) {
//		return indexOf(aTraceableList, aClass, aStartIndex, aTraceableList.size());
//		
//
//	}
//	public static boolean valid(List<Integer> anIndexList) {
//		List<Integer> anInvalidIndices = indicesOfInvalidIndices(anIndexList);
//		if (anInvalidIndices.size() != 0) {
////			System.out.println("Missing events:" + missingClasses(anExpectedClasses, anInvalidIndices));
//			return false;
//		}
//		return true;
//		
//	}
//	public static boolean inOrder(List<Integer> anIndexList) {
//		List<Integer> anOutOfOrderList = indicesOfOutOfOrderIndices(anIndexList);
//		if (anOutOfOrderList.size() != 0) {
//			return false;
//		}
//
//
//		return true;
//		
//	}
//	public static boolean inOrder(List<Traceable> aTraceableList, BeanQuery[] aQueryList,  int aStartIndex, int aStopIndex) {
//		List<Integer> anIndexList = indicesOf(aTraceableList, aQueryList, false, aStartIndex, aStopIndex);
//		return valid(anIndexList) & inOrder(anIndexList); // want both computed
//	}
//	
//	public static boolean inOrder(List<Traceable> aTraceableList, Class[] anExpectedClasses,  int aStartIndex, int aStopIndex) {
//		List<Integer> anIndexList = indicesOf(aTraceableList, anExpectedClasses, false, aStartIndex, aStopIndex);
//		boolean valid = valid(anIndexList);
//		boolean inOrder = inOrder(anIndexList);
//		return  valid && inOrder;
//	}
//	public static boolean inOrder(List<Traceable> aTraceableList, BeanQuery[] aQueryList,  int aStartIndex) {
//		return inOrder(aTraceableList, aQueryList, aStartIndex, aTraceableList.size());
//	}
//	public static boolean inOrder(List<Traceable> aTraceableList, Class[] aTargetClasses,  int aStartIndex) {
//		return inOrder(aTraceableList, aTargetClasses, aStartIndex, aTraceableList.size());
//	}
//	public static boolean inOrder(List<Traceable> aTraceableList, BeanQuery[] aQueryList) {
//		return inOrder(aTraceableList, aQueryList, 0);
//	}
//	public static boolean inOrder(List<Traceable> aTraceableList, Class[] aTargetClasses) {
//		return inOrder(aTraceableList, aTargetClasses, 0);
//	}
//	
//	public static List<Traceable> toTraceableList (List<Traceable> aTraceableList, List<Integer> anIndexList) {
//		List<Traceable> retVal = new ArrayList();
//		for (Integer anIndex: anIndexList) {
//			if (anIndex >= 0)
//				retVal.add(aTraceableList.get(anIndex));
//			else
//				retVal.add(null);
//		}
//		return retVal;
//	}
//	public static boolean matches (Object anActualBean, Object anExpectedBean, String[] aProperties) {
//		return (new ABeanQuery(anExpectedBean, aProperties)).matches(anActualBean);
//	}
//	public static boolean matches (List anActualBeans, Object anExpectedBean, String[] aProperties) {
//		boolean retVal = true;
//		for (Object anActualBean:anActualBeans) {
//			if (!matches (anActualBean, anExpectedBean, aProperties)) {
//				retVal = false;
//			}
//		}
//		return true;
//	}
//	public static boolean matches (List anActualBeans, String[] aProperties) {
//		if (anActualBeans.size() <= 1) return true;
//		Object anExpectedBean = anActualBeans.get(0);
////		anActualBeans.remove(0); // ouch inefficient!			
//		return matches(anActualBeans, anExpectedBean, aProperties);
//	}

}
