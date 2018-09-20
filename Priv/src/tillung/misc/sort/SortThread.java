package tillung.misc.sort;

import tillung.misc.threads.Monitor;

public class SortThread extends Thread {
	Object[] arr = null;
	int from;
	int to;
	
	Monitor mPrev = null; // Monitor for the first element
	Monitor threadMonitor = null; // This threads monitor
	Monitor mNext = null; // Monitor for the last element
	
	SortThread prevThread = null;
	SortThread nextThread = null;
	
	
	Thread t = null;
	private long started = 0;
	private long lastStopped = 0;
	
	public SortThread(Object[] arr, int sortFrom, int sortTo, Monitor mPrev,Monitor mNext) {
		this.arr = arr;
		this.from = sortFrom;
		this.to = sortTo;
		this.mPrev = mPrev;
		this.threadMonitor = new Monitor();
		this.mNext = mNext;
	}
	public long threadTime()
	{
		return this.lastStopped - this.started;
	}
	public void start()
	{
		//System.out.println("t " + mPrev + " " + mNext + " " + prevThread + " " + nextThread);
		
		this.started = System.currentTimeMillis();
		t = new Thread(this);
		t.start();
	}
	
	public void run()
	{
		try {
			boolean finished = false;
			
			do {
				this.bubbleSort(arr, from, to, mPrev, mNext);
				
				
				//print(arr, from, to);
				//System.out.println(this.getName() + " going to sleep");

				this.lastStopped = System.currentTimeMillis();
				this.threadMonitor.notification(false); // Wait on my own monitor
				//System.out.println(this.getName() + " waking up");
				//print(arr, from, to);
				
			} while (!finished);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void bubbleSort(Object[] arr, int from, int to, Monitor mPrev, Monitor mNext) throws InterruptedException {
		if (arr != null)
		{
			int c = from;
			
			boolean firstChanged = false;
			boolean lastChanged = false;
			
			while (c <= to)
			{
				if (c > from)
				{
					// swapping first element
					if ((mPrev != null) && ((c - 1) == from))
						mPrev.lock();
					// swapping last element
					else if ((mNext != null) && (c == to))
						mNext.lock();
					
					if (Sort.greater(arr[c - 1], arr[c]))
					{
						// swapping first element
						if ((c - 1) == from)
							firstChanged = true;
						// swapping last element
						else if (c == to)
							lastChanged = true;

						Object tmp = arr[c - 1];
						arr[c - 1] = arr[c];
						arr[c] = tmp;
						c--;
						c--;
					}

					// swapping first element
					if ((mPrev != null) && ((c - 1) == from))
						mPrev.unlock();
					// swapping last element
					else if ((mNext != null) && (c == to))
						mNext.unlock();
				}
				c++;
			}
			
			// Wake up previous thread
			if (firstChanged && (prevThread != null))
			{
				//System.out.println(this.getName() + " wake up prev thread");
				prevThread.getMonitor().notification(true);
			}

			// Wake up previous thread
			if (lastChanged && (nextThread != null))
			{
				//System.out.println(this.getName() + " wake up next thread");
				nextThread.getMonitor().notification(true);
			}
		}
	}
	
	/**
	 * Merging to intervals
	 * assuming the two intervals already is individually sorted.
	 * 
	 * @param arr
	 * @param from1
	 * @param to1
	 * @param from2
	 * @param to2
	 */
	public static void merge(Object[] arr, int from1, int to1, int from2, int to2)
	{
		// Length of the two intervals together
		int len = (to1 - from1) + 1 + (to2 - from2) + 1;

		int pos1 = from1;
		int pos2 = from2;
		
		Object[] sorted = new Object[len];
		
		System.out.println("unmerged:");
		for (int c=from1; c<=to1; c++)
			System.out.print(arr[c] + " ");
		System.out.println("");
		
		for (int c=from2; c<=to2; c++)
			System.out.print(arr[c] + " ");
		System.out.println("");

		
		int sPos = 0;
		// Loop interval 1
		while ((pos1 <= to1) && (pos2 <= to2))
		{
			if (Sort.greater(arr[pos1], arr[pos2]))
			{
				sorted[sPos] = arr[pos2];
				pos2++;
			}
			else
			{
				sorted[sPos] = arr[pos1];
				pos1++;
			}
			
			/* hva er dette?
			else
			{
				if (Sort.greater(arr[pos1], arr[pos2]))
					pos2++;
				else
					pos1++;
			}
			*/
		}

		System.out.println("merged:");
		for (int c=from1; c<=to1; c++)
			System.out.print(arr[c] + " ");
		System.out.println("");
		
		for (int c=from2; c<=to2; c++)
			System.out.print(arr[c] + " ");
		System.out.println("");

	}
	
	private Monitor getMonitor() {
		return this.threadMonitor;
	}
	public void setNextThread(SortThread t) {
		this.nextThread = t;
	}
	public void setPrevThread(SortThread t) {
		this.prevThread = t;
	}
}
