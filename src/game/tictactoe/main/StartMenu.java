package game.tictactoe.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JOptionPane;

import game.tictactoe.main.net.ClientHandler;
import game.tictactoe.main.net.HostHandler;
import game.tictactoe.main.net.LocalHandler;

//contains main
//used to promt user for launch configs
public class StartMenu
{	
	//the path to the game folder
	private final String dirPath = System.getProperty("user.home") + "/TicTacToe";
	//the file that contains the recent IP connection and size configurations
	private final File RECENT_CONNECTION_FILE = new File(dirPath + "/rec_dat.txt");
	
	//the recent window size
	private int recentSize;
	//the new size the user enters when promted
	private int newSize;
	
	//the new IP the user enters when starting a client
	private volatile String newIp;
	//the recent IP from the last client launch
	private volatile String recentIp;
	
	public StartMenu()
	{	
		//stores the recent data from rec_dat.txt to the variables
		retrieveRecentData();
		//asks the user for the size they want for the board
		promtForSize();
		//local, host, or client
		promtForLaunchType();
		//stores the configurations the user entered to rec_dat.txt
		storeNewData();
	}
	
	//main
	public static void main(String[] args) 
	{
		new StartMenu();
	}
	
	//stores data from rec_dat.txt to the proper variables
	private void retrieveRecentData()
	{
		/*tries to find the file and read contents. If the file doesnt exist
		  then it creates the file
		*/
		try {
			//scanner for the rec_dat.txt file
			Scanner scanner = new Scanner(RECENT_CONNECTION_FILE);
			//reads the IP (first line)
			recentIp = scanner.nextLine();
			//reads the size (second line)
			recentSize = Integer.parseInt(scanner.nextLine());
			scanner.close();
		} catch (FileNotFoundException e) {
			File path = new File(dirPath);
			//if the game directory doesnt exist it creates it
			if(path.exists() || path.mkdirs())
			{
				try {
					//creates the new file
					RECENT_CONNECTION_FILE.createNewFile();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			/*because the new file is empty, it uses these as placeholder values
			to prevent from NPE. This will get written to the blank file when 
			storeNewData() is called
			*/
			recentIp = "192.168.1.XX";
			recentSize = 850;
		}
	}
	
	//promts the user for the size of the board
	private void promtForSize()
	{
		//keeps promting till a valid size is entered
		do {
			try {
				//asks the user for the size
				String sizeInput = JOptionPane.showInputDialog("Enter the board size", recentSize);
				//if they cancelled, the program terminates
				if(sizeInput == null) System.exit(0);
				//parses the input to an int. If fails then the loop restarts
				newSize = Integer.parseInt(sizeInput);
			} catch(NumberFormatException e) {
				newSize = 0;
			}
		} while(newSize < 180);
		if(newSize == 0) System.exit(0);
	}
	
	//askes user whether theyd like to launch Host, Client, or Local
	private void promtForLaunchType()
	{
		//the options
		String[] options = { "Host", "Client", "Local" };
		//depending on what they answer
		switch(JOptionPane.showOptionDialog(null, "Which would you like to start?", "TicTacToe Menu", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]))
		{
			//host
			case 0: 
				newIp = recentIp;
				new Thread() {
					public void run() 
					{
						newIp = recentIp;
						new HostHandler(newSize);
					};
				}.start();
				break;
				
			//client
			case 1:
				newIp = JOptionPane.showInputDialog("Enter the host IP:", recentIp);
				if(newIp == null) System.exit(0);
				new Thread() {
					public void run() 
					{
						new ClientHandler(newIp, newSize);
					};
				}.start();
				break;
				
			//local
			case 2:
				new Thread(){
					public void run() 
					{
						newIp = recentIp;
						new LocalHandler(newSize);
					};
				}.start();
				break;
				
			//when someone exits
			default:
				System.exit(0);
		}
	}
	
	//stores the new values into rec_dat.txt
	private void storeNewData()
	{
		try {
			FileWriter fw = new FileWriter(RECENT_CONNECTION_FILE);
			fw.write(newIp+"\n"+newSize);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}