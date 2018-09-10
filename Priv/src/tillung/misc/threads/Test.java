package tillung.misc.threads;

public class Test {

	public static void main(String[] args) {
		MyThread t1 = new MyThread("T1", false);
		MyThread t2 = new MyThread("T2", false);
		MyThread t3 = new MyThread("T3", false);
		MyThread t4 = new MyThread("T4", false);
		MyThread t5 = new MyThread("T5", false);
		MyThread t6 = new MyThread("T6", false);
		MyThread t7 = new MyThread("T7", true);
		
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();
		t6.start();
		t7.start();
		
		System.out.println("all threads created and started");
	}
}
