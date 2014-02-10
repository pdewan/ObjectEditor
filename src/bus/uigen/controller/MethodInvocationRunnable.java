package bus.uigen.controller;import util.models.BoundedBuffer;import util.trace.Tracer;import bus.uigen.uiFrame;public class MethodInvocationRunnable implements Runnable {
	BoundedBuffer boundedBuffer;
	uiFrame parentFrame;
	public MethodInvocationRunnable(BoundedBuffer theBoundedBuffer, uiFrame theParentFrame) {
		boundedBuffer = theBoundedBuffer;
		parentFrame = theParentFrame;
	}
	public void run() {		
		Tracer.info(this, "Thread started:" + Thread.currentThread());
		for (;;) {
			MethodInvocation methodInvocation = (MethodInvocation) boundedBuffer.get();			Tracer.info(this, "Got next method:");
			methodInvocation.execute();
			parentFrame.doImplicitRefresh();
			
		}
		
	}
	
}