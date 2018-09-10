package tillung.misc.threads;

public class MThread extends Thread {
	Thread t = null;
	Monitor m = null;
	String name = null;
	public MThread(String name, Monitor m) {
		this.name = name;
		this.m = m;
	}
	public void start()
	{
		t = new Thread(this);
		t.start();
	}
	public void run()
	{
		try {
			int count = 1;
			do {
				if (m.lock())
				{
					// Locked, start working...
					System.out.print(name + " working (" + count + ")");
					for (int c=0; c<30; c++)
					{
						System.out.print(".");
						Thread.sleep(100);
					}
					System.out.println(" finished");
					m.unlock();
					Thread.sleep(100);
					count++;
				}
				else
				{
					System.out.println(name + " waiting");
					m.notification(false); // Sleep if not notified by someone
					System.out.println(name + " continue");
				}
			} while (count < 5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(name + " exiting");
	}
}
