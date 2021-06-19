package game.tictactoe.main.enums;

//types of commands
public enum Command 
{
	DISCONNECT("disconnect"),
	RESTART("restart");
	
	private final String cmd;
	
	Command(String cmd)
	{
		this.cmd = cmd;
	}
	
	//used to pass them accross network
	@Override
	public String toString() 
	{
		return cmd;
	}
	
	//used to get the command from the string
	public static Command getCommand(String str)
	{
		for(Command cmd : values())
		{
			if(cmd.toString().equalsIgnoreCase(str)) return cmd;
		}
		return null;
	}
}
