package maze.cli;

import maze.logic.*;

/**
 * Class that sends output to the console.
 */
public class Output {
	
	/**
	 * Default constructor for Output.
	 */
	public Output() {
		
	}

	/**
	 * Prints the maze to the console.
	 * 
	 * @param board
	 */
	public void drawBoard(char[][] board) {
		
		for(int i = 0; i < board.length; i++) {
			
			for(int j = 0; j < board.length; j++) {
				
					System.out.print(" " + board[i][j] + " ");
				}
			System.out.println();
		}
	}
	
	/**
	 * Draws the game commands to the console.
	 */
	public void drawCommands() {
		System.out.println("+-------- Commands -------+");
		System.out.println("Move........... [w,a,s,d] |");
		System.out.println("Confirm........  [enter]  |");
		System.out.println("Quit...........    [q]    |");
		System.out.println("+-------------------------+");
		System.out.println("|                         |");
	}
	
	/**
	 * Draws the player's goals/tasks to the console.
	 * 
	 * @param tasks
	 */
	public void drawGoals(Task[] tasks) {
		System.out.println("+--------- Goals ---------+");
		for(int i = 0; i < tasks.length; i++) {
			System.out.print((i + 1) + ". ");
			if(tasks[i].isDone()) {
				System.out.print("[X] ");
			}
			else {
				System.out.print("[ ] ");
			}
			System.out.println(tasks[i].getDescription());
		}
		System.out.println("+-------------------------+");
	}

	/**
	 * Draws a message to the console.
	 * Each message as its own unique Integer identifier, 
	 *   passed as argument.
	 *   
	 * @param msg : unique ID of the message
	 */
	public void drawMsg(int msg) {
		switch(msg) {
		case 0:
			System.out.println("Yupii, you've found a sword! Go tell your momma...");
			break;	
		case 1:
			System.out.println("You just slained a fucking dragon!! Go tell your father...");
			break;
		case 2:
			System.out.println("Key? ");
			break;
		case 3:
			System.out.println("An eagle! yey");
			break;
		case 4:
			System.out.println("Insert a maze size (the bigger the hardest (not always)) [5 - 21] <- odd number");
			break;
		case 5:
			System.out.println("I told'ya man!!!");
			break;
		case 6:
			System.out.println("Choose a dificulty: \n[1] Dumb dragons \n[2] Smart dragons (they sleep) \n[3] Dragons high on caffeine");
			break;
		case 7:
			System.out.println("Choose a percentage of dragons: [4 - 10]%");
			break;
		case 8:
			System.out.println("PlayAgain? (y/n)");
			break;
		case 69:
			System.out.println("|================|");
			break;
		default:
			break;
		}
	}
	
	/**
	 * Prints a given message to the console.
	 * Only for debugging.
	 * 
	 * @param msg : message to print
	 */
	public void debugPrint(String msg) {
		System.out.println(msg);
	}

	/**
	 * Draws all of the game state to the console.
	 * 
	 * @param game : GameLogic instance of the current game
	 */
	public void draw(GameLogic game) {
		
		// Draws menu.
		drawCommands();
		drawGoals(game.getTasks());
		
		// Draws board.
		drawBoard(game.getBoard());
		
	}
	
	/**
	 * Draws the game over screen to the console.
	 */
	public void drawGameOver() {
		System.out.println("+-------------------------+");
		System.out.println("+--------- LOSE ----------+");	
		System.out.println("+-------------------------+");
	}

	/**
	 * Draws the winning screen to the console.
	 */
	public void drawWinningMessage() {
		System.out.println("+-------------------------+");
		System.out.println("+--------- WIN -----------+");	
		System.out.println("+-------------------------+");
	}

	/**
	 * Draws the message (play again? (y/n)) and waits for input.
	 * 
	 * @param in : Input instance
	 * @return true if the player wishes to play again
	 */
	public boolean playAgain(Input in) {
		
		drawMsg(8);

		boolean done = false;
		do {
			String s = in.getString();
			if(s!=null) {
				switch(s.charAt(0)) {
				case 'Y':
					return true;
				case 'y':
					return true;
				case 'N':
					return false;
				case 'n':
					return false;
				}
			}

		} while(!done);
		return false;
	}
}
