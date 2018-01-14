package util.trace.uigen;

import util.trace.TraceableBus;

public class TraceableDisplayAndWaitManagerFactory {
	static TraceableDisplayAndWaitManager traceableDisplayAndPrintManager;
	public static TraceableDisplayAndWaitManager getTraceableDisplayAndPrintManager() {
		if (traceableDisplayAndPrintManager == null) {
			traceableDisplayAndPrintManager = new ATraceableDisplayAndWaitManager();
			TraceableBus.addTraceableListener(traceableDisplayAndPrintManager);
		}
		return traceableDisplayAndPrintManager;
	}

}
