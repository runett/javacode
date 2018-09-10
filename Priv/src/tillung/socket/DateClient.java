package tillung.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;



public class DateClient {
	/**     * Runs the client as an application.  First it displays a dialog 
	 *     * box asking for the IP address or hostname of a host running  
	 *        * the date server, then connects to it and displays the date that 
	 *            * it serves.   
	 *              */    
	public static void main(String[] args) throws IOException { 
		String serverAddress = "172.26.32.80";      
		Socket s = new Socket(serverAddress, 9099);
		
		long startat = System.nanoTime();

		PrintWriter out = new PrintWriter(s.getOutputStream(), true);
		out.println("getdate");
		
		BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
		String answer = input.readLine();

		long stopat = System.nanoTime();
		
		double ms = (new Double(stopat - startat)) / 1000000.0;
		System.out.println("client: " + answer + " (" + ms + "ms)");
		System.exit(0);
	}}
