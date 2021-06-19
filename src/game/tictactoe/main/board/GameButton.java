package game.tictactoe.main.board;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import game.tictactoe.main.enums.ButtonState;
import game.tictactoe.main.gfx.Icons;
import game.tictactoe.main.net.CommonHandler;

//buttons to click on
public class GameButton extends JButton 
{
	private static final long serialVersionUID = 1L;
	
	
	private ButtonState state = ButtonState.EMPTY;
	private Point pos;
	private CommonHandler handler;

	public GameButton(CommonHandler handler, Point pos)
	{
		this.handler = handler;
		this.pos = pos;
		//when button is clicked
		addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if(state == ButtonState.EMPTY && handler.hasTurn)
				{
					//sends move to opponent
					handler.sendMove(pos);
					//changes the button state depending on what the handler's sign is
					//handlers sign is only different with LocalHost
					changeState(handler.getSign());
				}
				handler.getBoard().requestFocus();
			}
		});
		setBackground(new Color(238, 238, 238));
	}
	
	//gets the position on the board
	public Point getPos()
	{
		return pos;
	}
	
	public ButtonState getState()
	{
		return state;
	}
	
	//used to change the state field and icon
	public void changeState(ButtonState state)
	{
		this.state = state;
		//used when resetting
		if(state == ButtonState.EMPTY)
		{
			setIcon(null);
			return;
		}
		//sets the button to show red X. Used when recieving moves
		if(state == ButtonState.X)
		{
			setIcon(Icons.RED_X);
			handler.hasTurn = true;
		} else {
			//used when normally clicking
			setIcon(Icons.GREEN_O);
			handler.hasTurn = false;
		}
		handler.getBoard().requestFocus();
		//checks for win
		handler.getBoard().checkForWin(pos);
	}
}
