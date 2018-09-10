package tillung.misc.sort;

import java.util.Hashtable;
import java.util.Vector;

import tillung.misc.threads.Monitor;

public class Threadsort3 {

	public static void main(String[] args) {
		int cores = Runtime.getRuntime().availableProcessors();
		System.out.println("cores: " + cores);
		
		Object[] arr = Threadsort.createRandomArr(100, 1, 100);
		for (int c=0; c<arr.length; c++)
			System.out.print(arr[c] + " ");
		System.out.println("");
		
		// create sortthreads
		int threads = cores; // Start as many threads as there are cores...
		
		SortThread[] tarr = new SortThread[threads];

		// One monitor
		Monitor monitor = new Monitor();
		
		Vector<Hashtable<String, Integer>> ranges = new Vector<Hashtable<String, Integer>>();

		for (int c=0; c<threads; c++)
		{
			int sortFrom = c * (arr.length / threads);
			int sortTo = (c + 1 ) * (arr.length / threads);
			sortTo--;
			
			Hashtable<String, Integer> r = new Hashtable<String, Integer>();
			r.put("from", new Integer(sortFrom));
			r.put("to", new Integer(sortTo));
			ranges.add(r);
			
			System.out.println("sortrange " + sortFrom + "-" + sortTo);
			
			// only sorts its range, not communicating with other threads
			SortThread t = new SortThread(arr, sortFrom, sortTo, null, null);
			tarr[c] = t;
			t.start();
		}

		try {
			Thread.sleep(1000);
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
		System.out.println("totaltime " + longest + "ms");
		
		// Merging the elements
		long mergeStart = System.currentTimeMillis();
		
		while (ranges.size() > 1)
		{
			Hashtable<String, Integer> r1 = ranges.remove(0);
			Hashtable<String, Integer> r2 = ranges.get(0);
			
			Integer from1 = r1.get("from");
			Integer to1 = r1.get("to");

			Integer from2 = r2.get("from");
			Integer to2 = r2.get("to");
			
			SortThread.merge(arr, from1, to1, from2, to2);
			
			r2.put("from", from1);
		}
		long mergeStop = System.currentTimeMillis();
		System.out.println("merged: " + (mergeStop - mergeStart) + "ms");
		
		for (int c=0; c<arr.length; c++)
			System.out.print(arr[c] + " ");
		System.out.println("");

	}

	static Integer[] createRandomArr(int size, int from, int to) {
		Integer[] arr = new Integer[size];
		
		int range = (to - from) + 1;
		for (int c=0; c<size; c++)
			arr[c] = ((Double)(Math.random() * range)).intValue() + from;
		return arr;
	}

}
