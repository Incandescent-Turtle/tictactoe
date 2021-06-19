package game.tictactoe.main.net;

import java.net.InetAddress;
import java.net.Socket;

public class ClientHandler extends CommonHandler
{
	public ClientHandler(String ip, int size)
	{
		super("Client", size);
		//sets the IP to the one passed
		serverIP = ip;
		start();
	}
	
	//connects to the server
	@Override
	protected void connect() 
	{
		try {
			connection = new Socket(InetAddress.getByName(serverIP), 6789);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Connection Failed");
			System.exit(0);
		}
	}
}
