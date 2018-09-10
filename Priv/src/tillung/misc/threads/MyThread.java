package tillung.misc.threads;

public class MyThread extends Thread {
	Thread t = null;
	String name = null;
	
	boolean master = false;
	
	public MyThread(String name, boolean master)
	{
		this.name = name;
		this.master = master;
	}
	public void run()
	{
		System.out.println(name + " run start");
		for (int c=1; c<=1000000; c++)
		{
			//MyThread.print(this);
			MyThread.heavyjob();
			/*
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) { e.printStackTrace(); }
			*/
		}
		System.out.println(name + " finished");
	}
	public void start()
	{
		t = new Thread(this);
		t.start();
		System.out.println(name + " started");
	}
	public static void heavyjob()
	{
		Integer v = new Integer(0);
		for (int c=0; c<100000; c++)
			for (int c2=0; c2<400000; c2++)
				for (int c3=0; c3<800000; c3++)
					v = 1;
	}
	public static void print(MyThread t)
	{
		System.out.print("thread " + t.name + " printing sync: ");
		for (int c=0; c<50; c++)
		{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) { e.printStackTrace();	}
			System.out.print(c);
		}
		System.out.println("");
		
	}
}
