package tillung.misc.threads;

public class Monitor {
	boolean locked = false;
	boolean notified = false;
	
	

	/**
	 * Lock the monitor. Other threads will wait until I unlock again.
	 * @return
	 */
	public synchronized boolean lock() {
		if (!locked) {
			locked = true;
			return true;
		}
		return false;
	}

	/**
	 * Unlock and notify all waiting threads
	 */
	public synchronized void unlock() {
		locked = false;
		notifyAll();
	}
	/**
	 * true:
	 * Set notification flag on monitor. All connected threads will be notified if sleeping or get notification when entering monitor.
	 * 
	 * false:
	 * go to sleep if not notified by other thread.
	 * 
	 * @param n
	 * @throws InterruptedException
	 */
	public synchronized void notification(boolean n) throws InterruptedException {
		if (n)
		{
			notified = true;
			notifyAll();
		}
		else
		{
			if (notified)
				notified = false;
			else
				wait();
		}
	}
}
