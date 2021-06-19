package game.tictactoe.main.board;
import static game.tictactoe.main.board.GameBoard.SIZE;

import java.awt.Point;



public class BoardSection 
{
	private final GameBoard board;
	private String sectionString = "";
	public final GameButton[] buttons;
	
	public BoardSection(GameBoard board, Point p1, Point p2, Point p3) 
	{
		this.board = board;
		sectionString = signAt(p1) + signAt(p2) + signAt(p3);
		buttons = new GameButton[] { getButtonAt(p1), getButtonAt(p2), getButtonAt(p3) };
	}
	
	private String signAt(Point pos)
	{
		int x = pos.x;
		int y = pos.y;
		
		while(x>SIZE) x-=SIZE;
		while(y>SIZE) y-=SIZE;

		return board.buttons[x-1][y-1].getState().toString();
	}
	
	@Override
	public String toString() 
	{
		return sectionString;
	}
	
	private GameButton getButtonAt(Point pos)
	{
		int x = pos.x;
		int y = pos.y;
		while(x>SIZE) x-=SIZE;
		while(y>SIZE) y-=SIZE;
		
		return board.buttons[x-1][y-1];
	}
}
