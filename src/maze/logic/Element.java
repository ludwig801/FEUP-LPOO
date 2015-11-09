package maze.logic;

import java.io.Serializable;
import java.util.Random;

/**
 * Class that represents all "element" type objects.
 * 
 * An element is basically every kind of object that may be found in the maze:
 *   - hero;
 *   - eagle;
 *   - dragon;
 *   - sword
 *   - exit.
 *   
 *   As such, every Element has a position (x, y), its position
 *     before any movement (oldX, oldY) - if appliable - and a representing
 *     symbol.
 *   This class implements serializable, has it is used upon game saving.
 */
public class Element implements Serializable {
	
	private static final long serialVersionUID = 1;
	
	protected int x;
	protected int y;
	protected int oldX;
	protected int oldY;
	protected char symbol;

	/**
	 * Default Constructor.
	 */
	public Element() {}

	/**
	 * Constructor for Element.
	 *   Receives its initial position (x, y) along with its symbol.
	 * 
	 * @param x : X coordinate
	 * @param y : Y coordinate
	 * @param symbol : symbol that represents the Element
	 */
	public Element(int x, int y, char symbol) {
		
		this.x = x;
		this.y = y;
		this.oldX = x;
		this.oldY = y;
		this.symbol = symbol;
	}
	
	/**
	 * Constructor for Element.
	 *   Receives its symbol and sets the initial position to te origin (0, 0).
	 * 
	 * @param symbol : symbol that represents the Element
	 */
	public Element(char symbol) {
		
		this.x = 0;
		this.y = 0;
		this.oldX = x;
		this.oldY = y;
		this.symbol = symbol;
		
	}
	
	/**
	 * Constructor for Element.
	 *   Receives the gameLogic instance of the game it is in and its symbol.
	 *   Generates the element's position randomly.
	 * 
	 * @param game : GameLogic instance
	 * @param symbol : symbol that represents the Element
	 */
	public Element(GameLogic game, char symbol) {
		
		GeneratePos(game);
		this.oldX = x;
		this.oldY = y;
		this.symbol = symbol;
		
	}

	/**
	 * Returns the element's old x coordinate.
	 * 
	 * @return the oldX
	 */
	public int getOldX() {
		return oldX;
	}

	/**
	 * Sets the element's old x coordinate.
	 * 
	 * @param oldX the oldX to set
	 */
	public void setOldX(int oldX) {
		this.oldX = oldX;
	}

	/**
	 * Returns the element's old y coordinate.
	 * 
	 * @return the oldY
	 */
	public int getOldY() {
		return oldY;
	}

	/**
	 * Sets the element's old y coordinate.
	 * 
	 * @param oldY the oldY to set
	 */
	public void setOldY(int oldY) {
		this.oldY = oldY;
	}

	/**
	 * Returns the element's x coordinate.
	 * 
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * Sets the element's x coordinate.
	 * 
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Returns the element's y coordinate.
	 * 
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * Sets the element's x coordinate.
	 * 
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}
	
	/**
	 * Returns the element's symbol.
	 * 
	 * @return the symbol
	 */
	public char getSymbol() {
		return symbol;
	}

	/**
	 * Sets the element's symbol.
	 * 
	 * @param symbol the symbol to set
	 */
	public void setSymbol(char symbol) {
		this.symbol = symbol;
	}
	
	/**
	 * Generates a valid random position for an Element. 
	 * 
	 * @param game the gameLogic instance which contains the maze
	 */
	public void GeneratePos(GameLogic game) {
		Random r = new Random();
		
		int MAX = game.getMaze().getSize();
		int posX = 0, posY = 0;

		do {
			posX = r.nextInt(MAX);
			posY = r.nextInt(MAX);

		}while(!isValidInitialPosition(game, posX, posY));
		
		this.x = posX;
		this.y = posY;
		
	}

	/**
	 * Checks if the position (x, y) is valid for the Element to be in it.
	 * 
	 * @param game the gameLogic instance which contains the maze
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return true if valid position
	 */
	protected boolean isValidInitialPosition(GameLogic game, int x, int y) {

		if(!(game.getMaze() == null)) {
			
			if(game.getMaze().getTiles()[x][y] == 'x') {
				return false;
			}
		}

		if(!(game.getHero() == null)) {
			
			if(game.getHero().isAt(x, y)) {
				return false;
			}
		}

		if(!(game.getSword() == null)) {
			
			if(game.getSword().isAt(x, y)) {
				return false;
			}
		}
		
		if(!(game.getDragons() == null)) {

			for(Dragon dragon: game.getDragons()) {

				if(!(dragon == null)) {

					if(dragon.isAt(x, y)) {
						return false;
					}
				}
			}
		}

		return true;
	}
	
	/**
	 * Checks if the Element [b] is in the same position of the Element which calls the function.
	 * 
	 * @param b element to compare
	 * @return true if in the same position
	 */
	public boolean isAt(Element b) {
		if((this.x == b.getX()) && (this.y == b.getY())) {
			return true;
		}
		return false;
	}
	
	/**
	 * Returns true if the Element is at the position (x, y).
	 * 
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return true if Element is at (x, y)
	 */
	public boolean isAt(int x, int y) {
		if(this.x == x && this.y == y)
			return true;
		return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		
		if((this.x == ((Element) obj).getX()) && (this.y == ((Element) obj).getY()))
			return true;
		
		return false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String s;
		s = "[" + this.symbol + "]";
		s += " (" + this.x;
		s += ", " + this.y + ")";
		return s;
	}

}
