package bus.uigen.editors;

public class AToolTipTextMonitor implements ToltipTextMonitor {
	
	/* (non-Javadoc)
	 * @see bus.uigen.editors.ToltipTextMonitor#waitForMouseEntered()
	 */
	public synchronized void waitForMouseEntered() {
		try {
//			System.out.println("waiting");
			this.wait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * @see bus.uigen.editors.ToltipTextMonitor#notifyMouseEntered()
	 */
	public synchronized void notifyMouseEntered() {
		try {
			this.notify();
//			System.out.println("notifying");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
