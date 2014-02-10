package bus.uigen.trace;

import java.util.List;

import util.annotations.StructurePattern;
import util.annotations.StructurePatternNames;
import util.annotations.Visible;
import util.models.AListenableVector;
import util.singlestep.AThreadWithSingleStepperAndListBrowser;
import util.singlestep.GlobalThreadSingleStepper;
import util.singlestep.SingleStepperAndListBrowser;
import util.singlestep.ThreadWithSingleStepperAndBrowser;
import util.trace.Traceable;
import bus.uigen.ObjectEditor;
@StructurePattern(StructurePatternNames.BEAN_PATTERN)
public class ATraceableDisplayAndWaitManager implements TraceableDisplayAndWaitManager{	
//	static Map<Thread, OEFrame> threadToOEFrame = new HashMap();
	List<Thread> notifyingThreads = new AListenableVector();
	List<ThreadWithSingleStepperAndBrowser> threadsWithSingleSteppers = new AListenableVector();
	String name;
//	public ATraceableDisplayAndWaitManager(String aName) {
//		name = aName;
//	}
	@Visible(false)	
	@Override
	public void newEvent(Exception anException) {
		if (anException instanceof Traceable) {
			Traceable traceable = (Traceable)  anException;
			if (traceable.getDisplay() || traceable.getWait()) {
				Thread thread = Thread.currentThread();
//				OEFrame oeFrame = threadToOEFrame.get(thread);	
				SingleStepperAndListBrowser singleStepper = GlobalThreadSingleStepper.getSingleStepper();				
				singleStepper.addObject(traceable);
//				if (oeFrame == null) {
				if (!notifyingThreads.contains(thread)) {
					ThreadWithSingleStepperAndBrowser threadWithSingleStepper = 
							new AThreadWithSingleStepperAndListBrowser(thread, singleStepper);
//					oeFrame = ObjectEditor.edit(threadWithSingleStepper);
//					threadToOEFrame.put(thread, oeFrame);	
					notifyingThreads.add(thread);
					threadsWithSingleSteppers.add(threadWithSingleStepper);
				}
				if (traceable.getWait()) {
					singleStepper.waitForUser();
				}
			}
		}
	}
	
	@Visible(false)
	public List getNotifyingThreads() {
		return notifyingThreads;
	}
	
	public List getSingleSteppedThreads() {
		return threadsWithSingleSteppers;
	}
	public void exit() {
		System.exit(0);
	}
	
	public static void main (String args[]) {
		SingleStepperAndListBrowser singleStepper =  GlobalThreadSingleStepper.getSingleStepper();
//		singleStepper.addObject("Hello");
//		singleStepper.addObject("gODBYE");
//		ObjectEditor.edit(singleStepper);
//		ObjectEditor.edit(Thread.currentThread());
		Traceable traceable1 = new MissingObserverParameter("test message", null, null, null);
		Traceable traceable2 = new MissingObserverParameter("test message", null, null, null);
		singleStepper.addObject(traceable1);
		singleStepper.addObject(traceable2);
		ObjectEditor.edit(singleStepper);

//		ObjectEditor.edit(traceable);
//		ObjectEditor.edit(clearanceManagerWithHistory);
	}
	

}
