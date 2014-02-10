package bus.uigen;

public class AnOEFrameShutDownHook implements Runnable {
	OEFrame oeFrame;
	public AnOEFrameShutDownHook(OEFrame anOEFrame) {
		oeFrame = anOEFrame;
		Thread thread = new Thread(this);
		Runtime.getRuntime().addShutdownHook(thread);
	}
	@Override
	public void run() {
		oeFrame.getFrame().dispose();
	}

}
