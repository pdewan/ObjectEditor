package bus.uigen.controller;import util.models.ABoundedBuffer;import util.trace.Tracer;import bus.uigen.uiFrame;public class MethodInvocationRunnable implements Runnable {
	ABoundedBuffer boundedBuffer;
	uiFrame parentFrame;
	public MethodInvocationRunnable(ABoundedBuffer theBoundedBuffer, uiFrame theParentFrame) {
		boundedBuffer = theBoundedBuffer;
		parentFrame = theParentFrame;
	}
	public void run() {		
		Tracer.info(this, "Thread started:" + Thread.currentThread());
		for (;;) {
			MethodInvocation methodInvocation = null;			try {				methodInvocation = (MethodInvocation) boundedBuffer.get();			} catch (InterruptedException e) {				// TODO Auto-generated catch block				e.printStackTrace();			}			Tracer.info(this, "Got next method:");
			methodInvocation.execute();
			parentFrame.doImplicitRefresh();
			
		}
		
	}
	
}