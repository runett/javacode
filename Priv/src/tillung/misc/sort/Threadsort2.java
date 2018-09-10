package tillung.misc.sort;

import tillung.misc.threads.Monitor;

public class Threadsort2 {

	public static void main(String[] args) {
		
		Object[] arr = Threadsort.createRandomArr(60000, 1, 1000);
		
		/*
		for (int c=0; c<arr.length; c++)
		{
			System.out.print(arr[c] + " ");
			if ((c % 20) == 0)
				System.out.println("");
		}
		*/

		SortThread t = new SortThread(arr, 0, 9999, null, null);
		t.start();

		/*
		SortThread t2 = new SortThread(arr, 10000, 19999, null, null);
		t2.start();

		SortThread t3 = new SortThread(arr, 20000, 29999, null, null);
		t3.start();

		SortThread t4 = new SortThread(arr, 30000, 39999, null, null);
		t4.start();

		SortThread t5 = new SortThread(arr, 40000, 49999, null, null);
		t5.start();

		SortThread t6 = new SortThread(arr, 50000, 59999, null, null);
		t6.start();
*/
		
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("totaltime " + t.threadTime());
		/*
		System.out.println("totaltime " + t2.threadTime());
		System.out.println("totaltime " + t3.threadTime());
		System.out.println("totaltime " + t4.threadTime());
		System.out.println("totaltime " + t5.threadTime());
		System.out.println("totaltime " + t6.threadTime());
		*/
		
		/*
		for (int c=0; c<arr.length; c++)
		{
			System.out.print(arr[c] + " ");
			if ((c % 20) == 0)
				System.out.println("");
		}
		System.out.println("");
		*/
		
	}

}
