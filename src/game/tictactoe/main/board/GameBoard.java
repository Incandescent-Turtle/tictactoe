package game.tictactoe.main.board;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import game.tictactoe.main.enums.ButtonState;
import game.tictactoe.main.enums.Command;
import game.tictactoe.main.gfx.Icons;
import game.tictactoe.main.net.CommonHandler;
import game.tictactoe.main.util.Util;


public class GameBoard extends JFrame
{
	private static final long serialVersionUID = 1L;
	//3x3 board. Game cant work with any more.
	public final static int SIZE = 3;
	//array of all the buttons
	public GameButton[][] buttons = new GameButton[SIZE][SIZE];
	//need to get 3 in a row to win
	private final static String X_WIN = "XXX", O_WIN = "OOO";
	public String windowTitle = "";
	//Max window size 
	private final Dimension MAX_DIM;
	//pop up to resize the game while playing
	private WindowResizer resizer;
	//actual board size in pixels
	public static int boardSize;
	
	public GameBoard(CommonHandler handler, int size) 
	{
		super("Tic Tac Toe");
		//takes comp screen size
		final Dimension userDim = Toolkit.getDefaultToolkit().getScreenSize();
		//sets the max dim
		MAX_DIM = Util.newDim((userDim.width > userDim.height ? userDim.height : userDim.width)-100);
		//sets the board size
		boardSize = MAX_DIM.width;
		resizer = new WindowResizer(this);
		setResizable(false);
		//smallest game can be with looking normal
		setMinimumSize(Util.newDim(180));
		//any bigger it wouldnt fit
		setMaximumSize(MAX_DIM);
		//perfect size
		setPreferredSize(Util.newDim(850));
		setSize(Util.newDim(boardSize));
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		//when window closes it closes connections and exits
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) 
			{
				handler.close(true);
			}
		});
		//middle o screen
		setLocationRelativeTo(null);
		setLayout(new GridLayout(SIZE, SIZE));
		//adds buttons and passes them a Point that doesnt equal their array positions
		for(int row = 0; row < SIZE; row++)
			for(int col = 0; col < SIZE; col++)
				add(buttons[row][col] = new GameButton(handler, new Point(row+1, col+1)));
		
		//anono keyadpater
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) 
			{
				switch(e.getKeyCode())
				{
					//to easily close game
					case KeyEvent.VK_ESCAPE:
						handler.close(true);
						break;
						
					//to restart. Also sends command to other player
					case KeyEvent.VK_R:
						handler.sendCommand(Command.RESTART);
						restart();
						break;
						
					//opens resizer
					case KeyEvent.VK_S:
						resizer.setVisible(!resizer.isVisible());
						break;
				}

			}
		});
		setFocusable(true);
		requestFocus();
	}
	
	//checks for win
	public void checkForWin(Point pos)
	{
		int x = pos.x;
		int y = pos.y;
		//creates sections of 3 to check for wins
		BoardSection[] sections = new BoardSection[4];
		//checks the column
		sections[0] = new BoardSection(this, new Point(x, y), new Point(x, y+1), new Point(x, y+2));
		//checks row
		sections[1] = new BoardSection(this, new Point(x, y), new Point(x+1, y), new Point(x+2, y));
		//diagonal
		sections[2] = new BoardSection(this, new Point(1,3), new Point(2, 2), new Point(3, 1));
		//diagonal
		sections[3] = new BoardSection(this, new Point(1, 1), new Point(2, 2), new Point(3, 3));
		//checks each section for winning pattern
		for(BoardSection section : sections)
		{
			String str = section.toString();
			if(str.equals(X_WIN) || str.equals(O_WIN)) endGame(section);
		}
	}
	
	//winning buttons turn blue
	public void endGame(BoardSection section)
	{
		for(GameButton button : section.buttons)
		{
			button.setBackground(Color.BLUE);
		}
		
	}
	
	public void updateTitle()
	{
		setTitle(windowTitle);
	}
	
	//resets all the buttons 
	public void restart()
	{
		for(int row = 0; row < SIZE; row++)
		{
			for(int col = 0; col < SIZE; col++)
			{
				GameButton button = buttons[row][col];
				button.setBackground(new Color(238, 238, 238));
				button.changeState(ButtonState.EMPTY);
			}
		}
	}
	
	//updates size of icons and board based on the boardSize variable
	private void updateSize()
	{
		//gets current location
		Point pos = getLocation();
		//sets size to the new size
		setSize(Util.newDim(boardSize));
		//resets the location (sze chanegs change location)
		setLocation(pos);
		//reloads icons
		Icons.loader.loadIcons();
		//reloads icons of the buttons
		for(int row = 0; row < SIZE; row++)
		{
			for(int col = 0; col < SIZE; col++)
			{
				GameButton button = buttons[row][col];
				button.setIcon(button.getState() == ButtonState.O ? Icons.GREEN_O : button.getState() == ButtonState.X ? Icons.RED_X : null);
			}
		} 
	}
	
	//popup to resize board
	private class WindowResizer extends JFrame
	{
		private static final long serialVersionUID = 1L;
			
		private WindowResizer(GameBoard board)
		{
			//spinner to change size number
			JSpinner spinner = new JSpinner(new SpinnerNumberModel(boardSize, 180, MAX_DIM.width, 5));
			//updates the board size every time the spinner value changes
			spinner.addChangeListener(new ChangeListener() {      
				  @Override
				  public void stateChanged(ChangeEvent e) 
				  {
					  boardSize = (int) spinner.getValue();
					  updateSize();
				  }
			});
			//escape to easily close resizer
			addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) 
				{
					if(e.getKeyCode() == KeyEvent.VK_ESCAPE) setVisible(false);;
				}
			});
			
			setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			setLocationRelativeTo(this);
			setSize(new Dimension(0,80));		
			setResizable(false);
			setLayout(new GridLayout(1, 2));
			add(spinner);
		}
		
		@Override
		public void setVisible(boolean visible) 
		{
			super.setVisible(visible);
			if(visible)
			{
				setFocusable(true);
				requestFocus();
			}
		}
	}
}
