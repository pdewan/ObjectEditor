package bus.uigen;
import java.util.Vector;
public class BoundedBuffer {
	Vector buffer = new Vector();
	final int MAXSIZE = 100;
	public synchronized void put(Object element) {
		try {
			//System.out.println("put called by" + Thread.currentThread());
			while (buffer.size() >= MAXSIZE)
				this.wait();			
			//if (buffer.size() == 0) {
				//System.out.println("Notify called by" + Thread.currentThread());
				this.notify();
			//}
			buffer.addElement(element);
		} catch (Exception e) {};
	}
	public synchronized Object get() {
		try {
			while (buffer.size() == 0) {
				System.out.println("wait called by" + Thread.currentThread());
				this.wait();
			}
			System.out.println("method invocation thread:" + Thread.currentThread());
			Object retVal = buffer.elementAt(0);
			if (buffer.size() >= MAXSIZE)				
				this.notify();
			buffer.removeElementAt(0);
			return retVal;
		} catch (Exception e) {
			return null;
		}
	}

}