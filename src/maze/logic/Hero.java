package maze.logic;

import java.util.Random;

/**
 * Class that represents all "hero" type objects.
 * 
 * An hero has the following atributes:
 *   - won: if the hero has the sword and killed all dragons,
 *       it is in a "won" position and is able to exit the maze and wind the game.
 *   - hasEagle: determines whether the eagle follows the hero or not.
 *   
 * @see Moveable
 */
public class Hero extends Moveable {
	
	private static final long serialVersionUID = 1;
	
	private boolean won;
	private boolean hasEagle;
	
	/**
	 * Deafult Constructor.
	 */
	public Hero() {
		super();
	}

	/**
	 * Constructor for Hero.
	 * Receives the current GameLogic in order to call super(game, symbol).
	 * 
	 * @param game current GameLogic
	 */
	public Hero(GameLogic game) {
		
		super(game, 'Y');
		
		this.hasSword = false;
		this.hasEagle = true;
		this.won = false;
	}
	
	/**
	 * Constructor for Hero.
	 * Receives the x and y coordinates in order to call super(x, y, symbol).
	 * 
	 * @param x : X coordinate
	 * @param y : Y coordinate
	 * @param symbol : symbol that represents the Hero
	 */
	public Hero(int x, int y, char symbol) {
		
		super(x, y, symbol);
		
		this.hasSword = false;
		this.hasEagle = false;
		this.won = false;
	}

	/**
	 * Gets the value of the parameter [hasEagle].
	 * 
	 * @return true if the hero has the eagle
	 */
	public boolean hasEagle() {
		return hasEagle;
	}

	/**
	 * Sets the value of the parameter [hasEagle].
	 * 
	 * @param hasEagle : value to be set
	 */
	public void setHasEagle(boolean hasEagle) {
		this.hasEagle = hasEagle;
	}
	
	/**
	 * Gets the value of the parameter [won].
	 * 
	 * @return true if the hero has won the game
	 */
	public boolean hasWon() {
		return won;
	}

	/**
	 * Sets the value of the parameter [won].
	 * This parameter declares if the hero has won the game or not.
	 * 
	 * @param win : value to be set
	 */
	public void setWon(boolean win) {
		this.won = win;
	}

	/**
	 * Arms the player by changing its representing symbol and by setting the parameter [hasSword] to true.
	 */
	public void arm() {
		this.hasSword = true;
		this.symbol = 'A';
	}

	/* (non-Javadoc)
	 * @see maze.logic.Moveable#toString()
	 */
	@Override
	public String toString() {
		String s = super.toString();
		s += "\n    armed: " + this.hasSword;
		return s;
	}

	/* (non-Javadoc)
	 * @see maze.logic.Element#GeneratePos(maze.logic.GameLogic)
	 */
	public void GeneratePos(GameLogic game) {
		Random r = new Random();
		
		int MAX = game.getMaze().getSize();
		int posX = 0, posY = 0;
		
		boolean done = false;
		while(!done) {
			posX = r.nextInt(MAX);
			posY = r.nextInt(MAX);
			
			// Checks if it's a valid position.
			
			done = isValidInitialPosition(game, posX, posY);
		}
		
		this.x = posX;
		this.y = posY;
		
	}

	/* (non-Javadoc)
	 * @see maze.logic.Element#isValidInitialPosition(maze.logic.GameLogic, int, int)
	 */
	protected boolean isValidInitialPosition(GameLogic game, int x, int y) {
		
		if(game.getMaze().getTiles()[x][y] == 'x') {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Updates a hero:
	 * 	- checks for found eagle;
	 *  - checks for found sword;
	 *  - checks for dragon encounters;
	 *  - check for found exit.
	 *  
	 * @param game : current GameLogic instance
	 */
	public void update(GameLogic game) {
		
		// Checks if hero has found the eagle.
		if(game.getEagle() != null) {
			if(!hasEagle && checkIfFound(game.getEagle(), 0)) {
				hasEagle = true;
				
				// If the eagle has the sword
				if(game.getEagle().hasSword()) {
					arm();
					game.getEagle().setHasSword(false);
					game.getEagle().setUseful(false);
				} else {
					symbol = 'Y';
				}
				
				game.getEagle().setMoving(false);
				game.getEagle().setFlying(false);
			}
		}

		
		// Checks if hero has found the sword. 
		if(game.getSword() != null) {
			if(!hasSword && checkIfFound(game.getSword(), 0)) {
				arm();
				if(game.getEagle() != null) {
					game.getEagle().setHasSword(false);
					game.getEagle().setUseful(false);
				}
			}
		}

		// Checks if hero has found any dragon.
		if(game.getDragons() != null) {
			Dragon dragonFound = checkIfFoundAnyDragon(game);
			if(dragonFound != null) {
				if(hasSword) {
					dragonFound.die();
				}
				else {
					if(dragonFound.isAwake()) {
						die();
					}
				}
			}
		}


		//Checks if hero has won the game and sets a flag.
		if(game.getMaze() != null && game.getMaze().getExit() != null) {
			if(isAt(game.getMaze().getExit())) {
				won = true;
				
				for(Dragon dragon : game.getDragons()) {
					if(dragon.isAlive()) {
						won = false;
					}
				}
				
				if(!won) {
					moveBack();
				}
				else {
					won = true;
				}
			}
		}

	}
	
	/**
	 * Returns any instance of Dragon if it has been
	 *   found by the player (distance between them <= 1).
	 * 
	 * @param game : GameLogic instance of the game
	 * @return an instance of Dragon if found, null otherwise
	 */
	public Dragon checkIfFoundAnyDragon(GameLogic game) {
		
		int maxDistance = 1;
				
		for(Dragon dragon : game.getDragons()) {

			if(dragon.isAlive() && checkIfFound(dragon, maxDistance)) {
				
				return dragon;
				
			}
		}	
		
		return null;
	}
	
}
