package maze.logic;

/**
 * Class that represents all "eagle" type objects.
 * 
 * An eagle has the following atributes:
 *   - moving/movingHorizontally: both refer to flying movement;
 *   - flying: whether it is or not flying;
 *   - useful: whether it has or not some use (if the sword belong to the player already,
 *       then the eagle is of no use to the player).
 *   
 * @see Moveable
 */
public class Eagle extends Moveable {
	
	private static final long serialVersionUID = 1;
	
	private boolean movingHorizontally;
	private boolean moving;
	private boolean flying;
	private boolean useful;
	
	/**
	 * Default Constructor.
	 */
	public Eagle() {
		super();		
	}

	/**
	 * Constructor for Eagle.
	 * Receives the x and y coordinates in order to call super(x, y, symbol).
	 * 
	 * @param x : X coordinate
	 * @param y : Y coordinate
	 * @param symbol : symbol that represents the Eagle
	 */
	public Eagle(int x, int y, char symbol) {
		
		super(x, y, symbol);
		
		this.hasSword = false;
		this.movingHorizontally = true;
		this.moving = false;
		this.flying = false;
		this.useful = true;			
	}

	/**
	 * Gets the value of the parameter [movingHorizontally].
	 * 
	 * @return true if the eagle is moving horizontally
	 */
	public boolean isMovingHorizontally() {
		return movingHorizontally;
	}

	/**
	 * Sets the value of the parameter [movingHorizontally].
	 * 
	 * @param movingHorizontally : value to be set
	 */
	public void setMovingHorizontally(boolean movingHorizontally) {
		this.movingHorizontally = movingHorizontally;
	}

	/**
	 * Gets the value of the parameter [moving].
	 * 
	 * @return true if the eagle is on the move 
	 */
	public boolean isMoving() {
		return moving;
	}

	/**
	 * Sets the value of the parameter [moving].
	 * 
	 * @param moving : value to be set
	 */
	public void setMoving(boolean moving) {
		this.moving = moving;
	}
	

	/**
	 * Gets the value of the parameter [flying].
	 * 
	 * @return true if the eagle is flying
	 */
	public boolean isFlying() {
		return flying;
	}

	/**
	 * Sets the value of the parameter [flying].
	 * 
	 * @param flying : value to be set
	 */
	public void setFlying(boolean flying) {
		this.flying = flying;
	}
	
	/**
	 * Gets the value of the parameter [useful].
	 * 
	 * @return true if the eagle has not retrieved the sword yet
	 */
	public boolean isUseful() {
		return useful;
	}

	/**
	 * Sets the value of the parameter [useful].
	 * 
	 * @param useful : value to be set
	 */
	public void setUseful(boolean useful) {
		this.useful = useful;
	}

	/**
	 * Moves the eagle to the sword's position (x, y).
	 * 
	 * @param sword : ELement to retrieve
	 */
	public void moveToSword(Element sword) {
		
		flying = true;
		
		boolean done = false;
		while(!done) {
			
			if(movingHorizontally) {
				if(x != sword.getX()) {
				
					if(x < sword.getX()) moveDown();
					else moveUp();
					
					movingHorizontally = false;
					done = true;
					
				} 
			} else {
				if(y != sword.getY()) {
				
					if(y < sword.getY()) moveRight();
					else moveLeft();
					
					done = true;
					movingHorizontally = true;
					
				}
			}
			
			if(!done) movingHorizontally = !movingHorizontally;
			
		}
		
	}

	/**
	 * Moves the eagle from the sword's position back
	 * to the position where the player loosed her.
	 */
	public void moveBack() {
		
		flying = true;
		
		// Moves in x. 
		if(x != oldX) {
			if(x < oldX) x++;
			else x--;
		} 
		// Moves in y.
		else if(y != oldY) {
			if(y < oldY) y++;
			else y--;
		}
		// Stops moving
		else if(this.isAt(oldX, oldY)){
			flying = false;
		}
		
	}
	
	/**
	 * Updates the position (x, y) of the eagle with the given parameters.
	 * 
	 * @param x : vertical coordinate
	 * @param y : horizontal coordinate
	 */
	public void updatePosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Updates the eagle's parameters upon loosing.
	 */
	public void sendEagle() {
		this.oldX = x; 
		this.oldY = y;
		this.moving = true;
	}
	
	/**
	 * Updates an eagle:
	 * 	- if the hero has the eagle - calls the update position with the hero's position;
	 *  - moves the eagle to the sword or back to the hero;
	 *  - checks if the eagle finds he sword or gets killed by a dragon.
	 *  
	 * @param game : current GameLogic instance
	 */
	public void update(GameLogic game) {
		
		if(useful) {
			
			// If the hero has the eagle
			if (game.getHero() != null) {
				if(game.getHero().hasEagle()) {
					updatePosition(game.getHero().getX(), game.getHero().getY());
				}
			}

			// If the eagle is on the move
			if(alive && moving) {
				if(game.getSword() != null) {
					if(!hasSword) {
						moveToSword(game.getSword());
						if(!hasSword() && checkIfFound(game.getSword(), 0)) {
							hasSword = true;
							flying = false;
						}
					} else {
						moveBack();
						game.getSword().setX(x);
						game.getSword().setY(y);
					}
				}
			}
			
			// If the eagle finds a dragon (dies)
			if (game.getHero() != null) {
				if(!flying && !game.getHero().hasEagle()) {

					if(game.getDragons() != null) {
						for(Dragon dragon : game.getDragons()) {

							if(checkIfFound(dragon, 0)) {

								die();

								if((game.getSword() != null) && hasSword) {

									hasSword = false;
									moving = false;
									flying = false;

									game.getSword().setX(x);
									game.getSword().setY(y);
								}
							}
						}
					}
				}
			}
		}
	}
	
}
