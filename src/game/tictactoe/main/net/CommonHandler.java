package game.tictactoe.main.net;

import java.awt.Point;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

import game.tictactoe.main.board.GameBoard;
import game.tictactoe.main.enums.ButtonState;
import game.tictactoe.main.enums.Command;

/*a handler that is used to manage the button presses and states
	gets extended by HostHandler, ClientHandler, and LocalHandler
*/
public abstract class CommonHandler 
{
	//the input
	protected DataInputStream input;
	//the output
	protected DataOutputStream output;
	//the connected
	protected Socket connection;
	//the IP to the server
	protected String serverIP;
	//the game board
	protected GameBoard board;
	//whether the player is allowed to go or if its the other persons turn
	public boolean hasTurn = false;
	
	protected CommonHandler(String name, int size)
	{
		//creates a new board for the handler
		this.board = new GameBoard(this, size);
		//sets the window title of the board
		board.windowTitle += "TicTacToe " + name + " - ";
		board.updateTitle();
	}
	
	//called only from subclasses
	//LocalHandler overrides
	protected void start()
	{
		//shows the board
		board.setVisible(true);
		/*attempts connection
		 on server will wait till someone connects
		 on client will time out if it takes too long
		 */
		connect();
		//updates the board title to be informative
		board.windowTitle += "Connected! ";
		board.updateTitle();
		//tries to set up the input and output streams based on the connection
		try {
			output = new DataOutputStream(connection.getOutputStream());
			output.flush();
			input = new DataInputStream(connection.getInputStream());
			//keeps running till terminated. Will wait for data and then do stuff
			while(true)
			{
				receiveData();	
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	//to set up connections
	protected abstract void connect();
	
	//used to determine whether an X or O should be placed down
	//overriden only in LocalHandler
	public ButtonState getSign()
	{
		return ButtonState.O;
	}

	//used to safely close connections
	public void disconnect(boolean sendCommand)
	{
		if(sendCommand) sendCommand(Command.DISCONNECT);
		try{
			output.close();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				input.close(); 
				
			} catch(Exception e) {
				e.printStackTrace();
			} finally {
				try {
					connection.close();
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	//used to send moves to the other player
	public void sendMove(Point pos)
	{
		try {
			output.writeUTF("x:" + pos.x + "_" + "y:" + pos.y);
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//used to send commands to the other player
	//Restart and Disconnect
	public void sendCommand(Command cmd)
	{
		if(connection != null)
		{
			try {
				output.writeUTF("cmd:"+cmd);
				output.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//used to recieve data, parse it, then act on it
	protected void receiveData()
	{
		try {
			//map of everything entered
			HashMap<String, String> map = new HashMap<>();
			//parses data into sections
			//section is x8_y7
			for(String section : input.readUTF().split("_"))
			{
				//splits it into components
				String[] pair = section.split(":");
				//places into map
				//ex. x8 = key(x) -> 8
				map.put(pair[0], pair[1]);
			}

			//if the data contained x and y pos
			if(map.containsKey("x") && map.containsKey("y"))
			{
				//parses data and updates the button at the right coords
				board.buttons[Integer.parseInt(map.get("x"))-1][Integer.parseInt(map.get("y"))-1].changeState(ButtonState.X);
				return;
			}
			//when recieving commands
			if(map.containsKey("cmd"))
			{
				//gets the Command that corrosponds to the string
				switch(Command.getCommand(map.get("cmd")))
				{
					//disconnects and doesnt send a cmd back
					case DISCONNECT:
						close(false);
						break;
						
					//restarts the board
					case RESTART:
						board.restart();

						break;
						
					default:
						
				}
			}
			
		} catch (EOFException e) {
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public GameBoard getBoard() 
	{
		return board;
	}
	
	//disconnectes and closes
	//boolean determines whether it should tell the other user to disconnect too
	public void close(boolean sendCommand)
	{
		board.dispose();
		disconnect(sendCommand);
		System.exit(0);
	}
}