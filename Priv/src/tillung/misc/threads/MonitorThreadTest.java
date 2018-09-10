package tillung.misc.threads;

public class MonitorThreadTest {

	public static void main(String[] args) {
		Monitor m = new Monitor();
		MThread t1 = new MThread("T1", m);
		MThread t2 = new MThread("T2", m);
		
		t1.start();
		t2.start();

	}

}
