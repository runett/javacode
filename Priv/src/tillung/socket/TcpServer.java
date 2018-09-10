package tillung.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class TcpServer
{
	ServerSocket listener = null;

	/**
	 * Start listener on given port
	 * @param port
	 */
	public TcpServer(int port)
	{
		try {
			listener = new ServerSocket(9099);
		} catch (IOException e) {
			e.printStackTrace();
			listener = null;
		}
	}
	
	
	/**
	 * Start server listening on port 9099
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException
	{
		TcpServer srv = new TcpServer(9099);
		srv.start();
	}
		
	public void start() throws IOException
	{
		
		try {
			boolean shutdown = false;
			while (!shutdown)
			{
				System.out.println("server: waiting for connection");
				Socket socket = listener.accept();
				System.out.println("server: connected");
	
				boolean close = false;
				
				try {
					while (!close)
					{       
						BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						String cmd = input.readLine();
						System.out.println("server recv command: " + cmd);
	
						
						if (cmd.equalsIgnoreCase("close"))
							close = true;
						else if (cmd.equalsIgnoreCase("shutdown"))
						{
							close = true;
							shutdown = true;
						}
						else
						{
							System.out.println("server: send " + cmd.toUpperCase());
							PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
							out.println(cmd.toUpperCase());
						}
					}
				} finally {
					socket.close();
					System.out.println("server: closing...");
				} 
			}
		}
		finally {
			listener.close();
			System.out.println("server: exit...");
		}
	}
}