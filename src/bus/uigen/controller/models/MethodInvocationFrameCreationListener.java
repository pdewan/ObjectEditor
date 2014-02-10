package bus.uigen.controller.models;

import bus.uigen.OEFrame;

public interface MethodInvocationFrameCreationListener {
	void methodInvocationFrameCreated (OEFrame aParentFrame, OEFrame anInvocationFrame, InteractiveMethodInvoker anInteractiveMethodInvoker);

}
