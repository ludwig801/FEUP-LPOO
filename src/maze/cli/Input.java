package maze.cli;

import java.util.Scanner;

/**
 * Class that deals with the input received from the console.
 */
public class Input {
	
	private Scanner scan = new Scanner(System.in);
	
	/**
	 * Default Constructor
	 */
	public Input() {
		
	}

	/**
	 * Gets user input and returns the keyCode associated with the first char.
	 */
	public int get() {
		
		String s = scan.nextLine();
		
		return getKeyPressed(s);
	}

	/**
	 * Gets the correspondent keyCode associated with the first char of the read String.
	 * 
	 * @return the keyCode of the char given.
	 */
	public int getKeyPressed(String s) {
		
		if(s.length() > 0) {
			
			switch(s.charAt(0)) {
			case 'w':
				return 87;
			case 'd':
				return 68;
			case 's':
				return 83;
			case 'a':
				return 65;
			case ' ':
				return 32;
			case 'q':
				return -1;
			default:
				break;
			}
		}
		
		return -1;
	}
	
	/**
	 * Gets the next line of input from the scanner.
	 * 
	 * @return the next line of input as a String
	 */
	public String getString() {
		String s = scan.nextLine();
		
		return s;
	}
}
