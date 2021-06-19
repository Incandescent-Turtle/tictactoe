package game.tictactoe.main.util;

//unused
public class Console 
{
	public enum ConsoleColour {

		RESET("\033[0m"),
	    BLACK("\033[0;90m"),   
	    RED("\033[0;91m"),      
	    GREEN("\033[0;92m"),   
	    YELLOW("\033[0;93m"),    
	    WHITE("\033[0;97m");

	    private final String code;

	    ConsoleColour(String code) {
	        this.code = code;
	    }

	    @Override
	    public String toString() {
	        return code;
	    }
	}
	
	public static void log(Class<?> clazz, String msg)
	{
		System.out.println(ConsoleColour.RESET + clazz.getName() + ": " + msg);
	}
	
	public static void log(Object obj, String msg)
	{
		log(obj.getClass(), msg);
	}
}
