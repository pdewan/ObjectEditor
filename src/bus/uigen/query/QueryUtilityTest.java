package bus.uigen.query;

import util.trace.TraceableLog;
import bus.uigen.trace.TraceUtility;

public class QueryUtilityTest {
	public static void main(String[] anArgs) {
		String[] list1 = {"hello", "goodbye"};
		String[] list2 = {"hello", "goodbye"};
		TraceableLog aTraceableLog = TraceUtility.startNewTrace();
		boolean retVal = QueryUtility.inOrder(list1, list2);
		TraceUtility.stopExistingTrace(aTraceableLog);
		System.out.println(retVal);
		System.out.println(aTraceableLog);
	}

}
