package game.tictactoe.main.net;

import java.awt.Point;

import game.tictactoe.main.enums.ButtonState;
import game.tictactoe.main.enums.Command;

//handler. Runs on one device for two players
public class LocalHandler extends CommonHandler
{
	//what the current sign is. which one to place down
	private ButtonState currentSign = ButtonState.O;
	
	public LocalHandler(int size) 
	{
		super("Local", size);
		//starts
		start();
		//always has turn
		while(true)
			hasTurn = true;
	}
	
	//when start is called, the baord shows
	@Override
	public void start() 
	{
		board.setVisible(true);
	}

	//whenever the sign is accesesed the state changes
	@Override
	public ButtonState getSign() 
	{
		return currentSign = (currentSign == ButtonState.O ? ButtonState.X : ButtonState.O);
	}
	
	/*
	 * A bunch of unneeded methods that have to do with networking
	 */
	protected void receiveData(){}
	protected void connect(){}
	public void disconnect(boolean sendCommand){}
	public void sendMove(Point pos) {}
	public void sendCommand(Command cmd) {}

}

