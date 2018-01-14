package util.trace.uigen;

import util.trace.TraceableListener;

public interface OEModelChangeBus extends TraceableListener {
	void addModelChangeListener(ModelChangeListener aModelChangeListener);

}
