package bus.uigen.trace;

import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import util.trace.Traceable;
import util.trace.TraceableInfo;
import util.trace.Tracer;

public class TraceUtility {
	
	public static Traceable toTraceable(String aMessage) {
//		if (!aMessage.startsWith("I***"))
//			continue;
		if (!Tracer.isInfo(aMessage))
			return null;
		try {
		Class traceableClass = TraceableInfo.toEvtTypeClass(aMessage);
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
	
	
	public boolean allValid(Integer[] anIndexList) {
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
	
	public static Integer indexOfInvalidIndex(Integer[] anIndexList, int aStartIndex) {
		for (int anIndexIndex = aStartIndex; anIndexIndex < anIndexList.length; anIndexIndex++) {
			if (anIndexList[anIndexIndex] < 0) return anIndexIndex;
		}
		return -1;
	}
	public static List<Integer> indicesOfInvalidIndices(Integer[] anIndexList) {
		List<Integer> retVal = new ArrayList();
		int aStartIndex = 0;
		while (true) {
			if (aStartIndex >= anIndexList.length)
				return retVal;
			Integer nextInvalidIndex = indexOfInvalidIndex(anIndexList, aStartIndex);
			if (nextInvalidIndex < 0)
				return retVal;
			retVal.add(nextInvalidIndex);
			aStartIndex = nextInvalidIndex+1;
			
		}
	}
	public static int indexOfInvalidIndex(Integer[] anIndexList) {
		return indexOfInvalidIndex(anIndexList, 0);
	}
	
	
	
	// look for elements of query in the same order but not necessarily consecutive in the traceable list
	// allow for missing elements
	public static Integer[]  indicesOf(List<Traceable> aTraceableList, TraceableQuery[] aQueryList, int aStartIndex, int aStopIndex) {
		Integer[] retVal = new Integer[aQueryList.length];
		int aCurrentStartIndex = aStartIndex;
		for (int aQueryIndex = 0; aQueryIndex < aQueryList.length && aCurrentStartIndex < aStopIndex; aQueryIndex++) {
			
			int aRetVal = indexOf(aTraceableList, aQueryList[aQueryIndex], aCurrentStartIndex, aStopIndex);
			retVal[aQueryIndex] = aRetVal;
			if (aRetVal >= 0)
				aCurrentStartIndex = aRetVal + 1; 
			// else look for the next matching element after same index
		}
		return retVal;	
	}
	public static Integer[]  indicesOf(List<Traceable> aTraceableList, Class[] aClassList, int aStartIndex, int aStopIndex) {
		TraceableQuery[] aQueryList = new TraceableQuery[aClassList.length];
		for (int aClassIndex = 0; aClassIndex < aClassList.length; aClassIndex++) {
			aQueryList[aClassIndex] = new ATraceableQuery(aClassList[aClassIndex]);
		}
		return indicesOf(aTraceableList, aQueryList, aStartIndex, aStopIndex);
	}
	
	public static Integer[]  indicesOf(List<Traceable> aTraceableList, Class[] aClassList, int aStartIndex) {
		return indicesOf(aTraceableList, aClassList, aStartIndex, aTraceableList.size());
	}
	public static Integer[]  indicesOf(List<Traceable> aTraceableList, Class[] aClassList) {
		return indicesOf(aTraceableList, aClassList, 0, aTraceableList.size());
	}
	
	public static Integer indexOf(List<Traceable> aTraceableList, TraceableQuery aQuery, int aStartIndex, int aStopIndex) {
		
		for (int anIndex = aStartIndex; anIndex < aStopIndex; anIndex++) {
			if (aQuery.matches(aTraceableList.get(anIndex)))
				return anIndex;
		}
		return -1;	
	}
	public static Integer indexOf(List<Traceable> aTraceableList, Class aClass, int aStartIndex, int aStopIndex) {
		return indexOf(aTraceableList, new ATraceableQuery(aClass), aStartIndex, aStopIndex);
	}
	
	public static Integer indexOf (List<Traceable> aTraceableList, Class aClass, int aStartIndex) {
		return indexOf(aTraceableList, aClass, aStartIndex, aTraceableList.size());
		
//		for (int i = anIndex; i < aTraceableList.size(); i++) {
//			Traceable element = aTraceableList.get(i);
////			if (element.getEventSource().equals(aClass.toString()))
//			if (element.getClass() == aClass)
//				return i;
//		}
//		return -1;
	}

}
