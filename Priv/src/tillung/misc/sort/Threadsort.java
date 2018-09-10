package tillung.misc.sort;

import tillung.misc.threads.Monitor;

public class Threadsort {

	public static void main(String[] args) {
		Object[] arr = Threadsort.createRandomArr(100000, 1, 100000);
		for (int c=0; c<arr.length; c++)
			System.out.print(arr[c] + " ");
		System.out.println("");
		
		// create 5 sortthreads
		Monitor mPrev = null;
		//SortMonitor mThis = null;
		Monitor mNext = null;
		
		int threads = 1;
		
		SortThread[] tarr = new SortThread[threads];
		for (int c=0; c<threads; c++)
		{
			if (c < (threads - 1))
				mNext = new Monitor();
			else
				mNext = null;
			
			int sortFrom = c * (arr.length / threads);
			int sortTo = (c + 1 ) * (arr.length / threads);
			if (c == threads - 1)
				sortTo = arr.length - 1;
			System.out.println("sortrange " + sortFrom + "-" + sortTo);
			
			//mPrev = mNext;
			//if (c == threads - 1)
				//mNext = null;
			//else
				//mNext = new SortMonitor();

			SortThread t = new SortThread(arr, sortFrom, sortTo, mPrev, mNext);
			tarr[c] = t;
			mPrev = mNext;
		}

		// link threads together
		for (int c=1; c<threads; c++)
		{
			tarr[c-1].setNextThread(tarr[c]);
			tarr[c].setPrevThread(tarr[c-1]);
		}

		// Start threads
		for (int c=0; c<threads; c++)
			tarr[c].start();
		
		//Sort.bubbleSort(arr, 0, 999);
		try {
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		long longest = 0;
		for (int c=0;c<tarr.length; c++)
		{
			long ttime = tarr[c].threadTime();
			if (ttime > longest)
				longest = ttime;
		}
		System.out.println("totaltime " + longest);
			
		/*
		for (int c=0; c<arr.length; c++)
			System.out.print(arr[c] + " ");
		System.out.println("");
		*/
	}

	static Integer[] createRandomArr(int size, int from, int to) {
		Integer[] arr = new Integer[size];
		
		int range = (to - from) + 1;
		for (int c=0; c<size; c++)
			arr[c] = ((Double)(Math.random() * range)).intValue() + from;
		return arr;
	}

}
