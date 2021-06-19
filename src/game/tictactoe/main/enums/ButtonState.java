package game.tictactoe.main.enums;
//to keep track of the thing the button is displaying
public enum ButtonState 
{ 
	X("X"),
	O("O"),
	EMPTY(" ");
	
	private String id;
	
	private ButtonState(String id)
	{
		this.id = id;
	}
	
	//used to get the composition of rows (XXX) 
	@Override
	public String toString() 
	{
		return id;
	}
}