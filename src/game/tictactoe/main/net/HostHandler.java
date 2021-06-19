package game.tictactoe.main.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

//handler that clients connect to 
public class HostHandler extends CommonHandler
{
	private ServerSocket server;

	public HostHandler(int size) 
	{
		super("Host", size);
		//displays the servers IP in the title
		InetAddress inetAddress = null;
		try {
			inetAddress = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		serverIP = inetAddress.getHostAddress();
		board.windowTitle += serverIP + " - ";
		board.updateTitle();
		start();
	}

	//closes the port
	@Override
	public void disconnect(boolean sendCommand) 
	{
		super.disconnect(sendCommand);
		try {
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//listens on port 6789 and waits for client to connect 
	@Override
	protected void connect() 
	{
		try {
			server = new ServerSocket(6789, 100);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			connection = server.accept();
		} catch (IOException e) {
			e.printStackTrace();
		}
		hasTurn = true;
	}
}
