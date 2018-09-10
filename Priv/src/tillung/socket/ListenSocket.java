package tillung.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class ListenSocket
{    /**     * Runs the server.     */ 
	public static void main(String[] args) throws IOException
	{
		ServerSocket listener = new ServerSocket(9099);
		try {
			while (true)
			{       
				System.out.println("server: waiting");
				Socket socket = listener.accept();
				try {
					BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					String cmd = input.readLine();

					System.out.println("server command: " + cmd);
					System.out.println("server: send");

					PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
					out.println(new Date().toString());
				} finally {
					socket.close(); 
				} 
			}
		}
		finally {
			listener.close();
		}
	}
}

