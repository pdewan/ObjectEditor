package bus.uigen.query;

import java.util.List;

import util.trace.Traceable;
import util.trace.TraceableLog;
import util.trace.uigen.TraceUtility;

public class QueryUtilityTest {
	public static void main(String[] anArgs) {
		String[] list1 = {"b", "a", "d", "c"};
		String[] list2 = {"a", "b"};
		TraceableLog aTraceableLog = TraceUtility.startNewTrace();
		boolean retVal = QueryUtility.inOrder(list1, list2, false);
		TraceUtility.stopExistingTrace(aTraceableLog);
		System.out.println(retVal);
//		System.out.println(aTraceableLog);
		printLog(aTraceableLog.getLog());
	}
	
	public static void printLog(List aList) {
		for (Object aTraceable:aList) {
			System.out.println(aTraceable);
		}
		
	}

}
