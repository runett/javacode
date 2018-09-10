package tillung.misc.sort;
/**
 * 
 * @author rtil
 * @deprecated
 */
public class SortMonitor {
	boolean locked = false;
	boolean changed = false;
	private boolean sendingUp;
	private boolean sendingDown;

	public synchronized void lock() throws InterruptedException {
		if (locked)
			wait();
		locked = true;
	}
	public synchronized void unlock() {
		locked = false;
		if (changed)
		{
			changed = false;
			notify();
		}
	}
	public synchronized void changed(boolean up) {
		changed = true;
		
		if (up)
			sendingUp = true;
		else
			sendingDown = true;
	}

}
