package tillung.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

public class TcpClient {
	Socket socket = null;
	String addr = null;
	int port = -1;
	
	public boolean connect(String addr, int port) {
		this.addr = addr;
		this.port = port;
		try {
			socket = new Socket(addr, 9099);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			socket = null;
		}
		return false;
	}

	public static void main(String[] args) throws IOException {
		TcpClient client = new TcpClient();
		//client.connect("172.26.32.80", 9099);
		client.connect("MDLA012050", 9099);
		
		client.get("hei");
		client.get("på deg");
		client.get("vent litt....");

		// 10 sek
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		client.get("er tilbake...");
		client.shutdown();
		
		
	}
		   
		
	public void close() {
		send("close");
	}
	public void shutdown() {
		send("shutdown");
	}

	/**
	 * Send string, dont expect an answer.
	 * @param str
	 */
	public void get(String str)
	{
		long startat = System.nanoTime();
		try {
	
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			out.println(str);
			
			BufferedReader input;
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String answer = input.readLine();
	
			long stopat = System.nanoTime();
			
			double ms = (new Double(stopat - startat)) / 1000000.0;
			System.out.println("client: get:" + str + " recv:" + answer + " (" + ms + "ms)");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Send string and wait for answer
	 * @param str
	 */
	public void send(String str)
	{
		try {
			long startat = System.nanoTime();
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			out.println(str);
			long stopat = System.nanoTime();
			
			double ms = (new Double(stopat - startat)) / 1000000.0;
			System.out.println("client: sent " + str + " (" + ms + "ms)");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}}

