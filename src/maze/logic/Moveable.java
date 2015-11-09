package maze.logic;

/**
 * Class that represents all "moveable" type objects.
 * 
 * A moveable has the following atributes:
 *   - alive: whether it is alive or not (since only animated
 *       elements may die);
 *   - hasSword: whether it has hold of the sword or not.
 *   
 * @see Element
 */
public class Moveable extends Element {
	
	private static final long serialVersionUID = 1;
	
	protected boolean alive;
	protected boolean hasSword;
	
	/**
	 * Default Constructor.
	 */
	public Moveable() {
		super();
	}
	
	/**
	 * Constructor for Element.
	 * Receives the current GameLogic in order to call super(game, symbol).
	 * Sets the parameter [alive] to true.
	 * 
	 * @param game current GameLogic
	 * @param symbol : symbol that represents the Moveable
	 */
	public Moveable(GameLogic game, char symbol) {
		
		super(game, symbol);
		
		this.alive = true;
	}

	/**
	 * Constructor for Element.
	 * Receives the x and y coordinates in order to call super(x, y, symbol).
	 * Sets the parameter [alive] to true.
	 * 
	 * @param x : X coordinate
	 * @param y : Y coordinate
	 * @param symbol : symbol that represents the Moveable
	 */
	public Moveable(int x, int y, char symbol) {
		
		super(x, y, symbol);
		
		this.alive = true;
	}

	/**
	 * Gets the value of the parameter [hasSword].
	 * 
	 * @return true if the element has a sword
	 */
	public boolean hasSword() {
		return hasSword;
	}

	/**
	 * Sets the value of the parameter [hasSword].
	 * 
	 * @param hasSword : value to be set
	 */
	public void setHasSword(boolean hasSword) {
		this.hasSword = hasSword;
	}

	/**
	 * Gets the value of the parameter [alive].
	 * 
	 * @return true if the element is alive
	 */
	public boolean isAlive() {
		return alive;
	}

	/**
	 * Sets the value of the parameter [alive].
	 * 
	 * @param alive : value to be set
	 */
	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	/**
	 * Moves and Element in the board in the given direction
	 * 
	 * @param game : current GameLogic instance
	 * @param direction : direction to move upon
	 */
	public void move(GameLogic game, int direction) {
		
		updateOldCoord();
		
		switch(direction) {
		case 0: //UP
			if(isValidMove(getX() - 1, getY(), game)) moveUp();
			break;
		case 1: //RIGHT
			if(isValidMove(getX(), getY() + 1, game)) moveRight();
			break;
		case 2: //DOWN
			if(isValidMove(getX() + 1, getY(), game)) moveDown();
			break;
		case 3: //LEFT
			if(isValidMove(getX(), getY() - 1, game)) moveLeft();
			break;
		default:
			break;
		}
	}
	
	/**
	 * Updates the old position (x, y) prior to the last movement.
	 */
	private void updateOldCoord() {
		setOldX(x);
		setOldY(y);
	}
	

	/**
	 * Moves an Element up by decrementing its x coordinate.
	 */
	public void moveUp() {
		this.x -= 1;
	}

	/**
	 * Moves an Element down by incrementing its x coordinate.
	 */
	public void moveDown() {
		this.x += 1;
	}
	
	/**
	 * Moves an Element right by incrementing its y coordinate.
	 */
	public void moveRight() {
		this.y += 1;
	}

	/**
	 * Moves an Element left by decrementing its y coordinate.
	 */
	public void moveLeft() {
		this.y -= 1;
	}
	
	/**
	 * Moves an Element to its old position prior to the last movement.
	 */
	public void moveBack() {
		this.x = oldX;
		this.y = oldY;
	}
	
	/**
	 * "Kills" an Element by erasing its symbol and setting the parameter [alive] to false.
	 */
	public void die() {
		this.alive = false;
		this.symbol = ' ';
	}
	
	/**
	 * Checks if an Element is trying to move to a valid position.
	 * Positions containing walls or dragons would be invalid, but positions containing the sword or the eagle are valid.
	 * 
	 * @param x : vertical coordinate of the movement
	 * @param y : horizontal coordinate of the movement
	 * @param game : current GameLogic instance
	 * @return true if valid movement, false otherwise
	 */
	public boolean isValidMove(int x, int y, GameLogic game) {

		if(game.getDragons() != null) {

			for(Dragon dragon: game.getDragons()) {

				if(!(this == dragon)) {

					if(dragon.isAlive() && dragon.isAt(x, y)) {
						return false;
					}
				}
			}

		}

		if(this == game.getHero()) {
			
			if(game.getMaze().getExit() != null) {

				if(game.getMaze().getExit().isAt(x,y)) {
					return true;
				}

			}

		}
		
		if(game.getMaze().getTiles()[x][y] == 'x') {
			return false;
		}

		if(game.getHero() != null) {
			if(game.getHero().isAt(x, y)) {
				return false;
			}
		}
		
		if(game.getMaze().getTiles()[x][y] == ' ') {
			return true;
		}
		
		if(game.getHero() != null) {
			if(game.getSword().isAt(x, y)) {
				return true;
			}
		}

		return false;
	}
	
	/* (non-Javadoc)
	 * @see maze.logic.Element#toString()
	 */
	@Override
	public String toString() {
		String s = super.toString();
		s += "\n    alive: " + this.alive;
		return s;
	}
	
	/**
	 * Checks if the Element has found another.
	 * This is done so by calculating the distance between the Elements using Pitagoras' theorem: x^2 + y^2 = d^2
	 * 
	 * @param element : element to check
	 * @return true if found the element, false otherwise
	 */
	public boolean checkIfFound(Element element, int minDistance) {
		// Calculating the real distance between the dragon and the player (contiguous cells will necessarily be 1 unit apart)
		//
		// formula: sqrt(deltaX + deltaY) <--- Pitagoras' theorem
		//
		if(Math.sqrt(Math.abs(this.x - element.getX()) + Math.abs(this.y - element.getY())) <= minDistance) {
			return true;
		}
		
		return false;
	}
	
}
