package maze.logic;

import java.io.Serializable;
import java.util.Random;
import java.util.Stack;

/**
 * Class that represents all "maze" type objects.
 * 
 * A maze consists, very shortly, in a series of tiles (chars) placed in a matrix.
 *   It has a size (actually, the matrix is SIZExSIZE) an it has an
 *   exit (instance of an Element with the symbol 'S')
 *   
 * This class implements serializable, has it is used upon game saving.
 */
public class Maze implements Serializable {
	
	private static final long serialVersionUID = 1;
	private char tiles[][];	
	private int size;
	private Element exit;
	
	/**
	 * Default Constructor.
	 */
	public Maze() {}

	/**
	 * Constructor for Maze.
	 * The game maze is generated randomly.
	 * 
	 * @param size : size of the maze to be built (eg: 7 means a 7x7 maze)
	 */
	public Maze(int size) {
		
		this.size = size;
		
		tiles = new char[size][size];
		
		exit = new Element('S');
	}
	
	public void generate() {
		SetChamber();
		DrawGrid();
		GenerateExit();
		GenerateWalls();
	}

	/**
	 * Constructor for Maze.
	 * The game maze is received has a parameter.
	 * 
	 * @param tiles : array of chars representing the maze
	 */
	public Maze(char tiles[][]) {

		this.tiles = tiles;
		this.size = tiles[0].length;
	}
	
	/**
	 * Gets the value of the parameter [size].
	 * 
	 * @return the size of the maze
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Gets the value of the parameter [tiles].
	 * 
	 * @return the tiles of the game board
	 */
	public char[][] getTiles() {
		return tiles;
	}

	/**
	 * Gets the exit of the maze.
	 * 
	 * @return the Element [Exit]
	 */
	public Element getExit() {
		return exit;
	}

	/**
	 * Sets the exit of the maze.
	 * 
	 * @param exit : Element to set
	 */
	public void setExit(Element exit) {
		this.exit = exit;
	}
	
	/*
	 *  MAZE BUILDER
	 */
	
	// -- BEGIN Maze generation
	
	/**
	 * Randomly generates a valid exit for the labyrinth. With the following restrictions:
	 *   The exit must be on a frontier.
	 *   The exit must not be on a corner. 
	 *   
	 * The coordinates for the generated exit are stored on the exit array. 
	 */
	public void GenerateExit() {
		
		Random r = new Random();
		
		/*
		 * 0 - Top
		 * 1 - Right
		 * 2 - Bottom
		 * 3 - Left
		 * 
		 */
		int side = r.nextInt(4);
		
		boolean done = false;
		while(!done) {
			
			switch(side) {
			case 0: 
				exit.setX(0);
				// Can't be at (0, 0) nor (0, N).
				exit.setY(r.nextInt(size - 2) + 1);
				if(tiles[exit.getX() + 1][exit.getY()] != 'x') done = true;
				break;
			case 1:
				exit.setX(r.nextInt(size - 2) + 1);
				exit.setY(size - 1);
				if(tiles[exit.getX()][exit.getY() - 1] != 'x') done = true;
				break;
			case 2: 
				exit.setX(size - 1);
				exit.setY(r.nextInt(size - 2) + 1);
				if(tiles[exit.getX() - 1][exit.getY()] != 'x') done = true;
				break;
			case 3:
				exit.setX(r.nextInt(size - 2) + 1);
				exit.setY(0);
				if(tiles[exit.getX()][exit.getY() + 1] != 'x') done = true;
				break;
			}
			
		}
		

		
	}
	
	/**
	 * Sets the outside walls for the maze.
	 */
	public void SetChamber() {
		
		// Top wall
		for(int i = 0; i < size; i++) tiles[0][i] = 'x';
		
		// Right wall
		for(int i = 0; i < size; i++) tiles[i][size-1] = 'x';
		
		// Bottom wall
		for(int i = 0; i < size; i++) tiles[size-1][i] = 'x';
		
		// Left wall
		for(int i = 0; i < size; i++) tiles[i][0] = 'x';
		
	}
	
	/**
	 * Draws a grid inside the previously generated maze chamber.
	 * 
	 * Example (5x5): 
	 * 
	 * 		x x x x x
	 * 		x   x   x
	 * 		x x x x x
	 * 		x   x   x
	 * 		x x x x x 
	 * 
	 */
	public void DrawGrid() {
		
		// Determines whether we are drawing a wall or not.
		boolean wall = false;
		// Determines whether we are drawing a line or not.
		boolean line = false;
		// Draws grid.
		for(int i = 1; i < (size - 1); i++) {
			for(int j = 1; j < (size - 1); j++) {
				
				if(wall && (line || j < (size - 2))) tiles[i][j] = 'x';
				else tiles[i][j] = ' ';
				
				if(!line) wall = !wall;
				
			}
			
			line = !line;
			
			if(line) wall = true;
			else wall = false;
		}
		
		// Test
		/*
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				System.out.print(" " + tiles[i][j] + " ");
			}
			System.out.println();
		}
		*/
	}
	
	/**
	 * Generates the final maze. Takes our pre-created grid and then randomly opens holes in walls.
	 * Follows the backtracking algorithm.
	 */
	public void GenerateWalls() {

		Random r = new Random();

		// Random direction.
		/*
		 * 0 - UP
		 * 1 - RIGHT
		 * 2 - DOWN
		 * 3 - LEFT
		 */
		int direction; 

		boolean visited[][] = new boolean[size][size];

		// -- BEGIN Marks all walls and the exit as visited cells, so they don't count. 
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				if(tiles[i][j] == 'x' || tiles[i][j] == 'S') visited[i][j] = true;
				else visited[i][j] = false;
			}
		}
		// -- END 

		// Test visited array.
		/*
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				System.out.print(" " + visited[i][j] + " ");
			}
			System.out.println();
		}
		 */

		Stack<Integer> stackX = new Stack<Integer>();
		Stack<Integer> stackY = new Stack<Integer>();
		//
		int current[] = new int[2];

		// -- BEGIN Make the initial cell the adjacent to the exit. 
		current[0] = exit.getX(); current[1] = exit.getY();
		if(current[0] == 0) current[0]++;
		else if(current[0] == (size - 1)) current[0]--;
		else if(current[1] == 0) current[1]++;
		else if(current[1] == (size - 1)) current[1]--;

		visited[current[0]][current[1]] = true;

		// Prints current position
		//System.out.println("Current: " + current[0] + "," + current[1]);

		// -- END 

		boolean done = false;
		while(!done) {

			// Pick valid neighbor
			boolean found = false;
			while(!found) {

				if(!hasUnvisitedNeighbors(current[0], current[1], visited)) {

					if(!stackX.isEmpty()) {
						current[0] = stackX.pop(); current[1] = stackY.pop();
					} else {
						boolean newCell = false;
						for(int i = 0; i < size; i++) {
							for(int j = 0; j < size; j++) {
								if(!visited[i][j]) {
									current[0] = i; current[1] = j;
									newCell = true;
									break;
								}
								if(newCell) break;
							}
						}
					}


				} else {

					// Push current cell to stack
					stackX.push(current[0]); stackY.push(current[1]);

					direction = r.nextInt(4);

					if(hasNeighbor(current[0], current[1], direction)) {
						switch(direction) {
						// UP
						case 0:
							if(!visited[current[0] - 2][current[1]]) {
								tiles[current[0] - 1][current[1]] = ' ';
								current[0] -= 2;
								found = true;
							}
							break;
							// RIGHT
						case 1:
							if(!visited[current[0]][current[1] + 2]) {
								tiles[current[0]][current[1] + 1] = ' ';
								current[1] += 2;
								found = true;
							}
							break;
							// DOWN
						case 2:
							if(!visited[current[0] + 2][current[1]]) {
								tiles[current[0] + 1][current[1]] = ' ';
								current[0] += 2;
								found = true;
							}
							break;
							// LEFT
						case 3:
							if(!visited[current[0]][current[1] - 2]) {
								tiles[current[0]][current[1] - 1] = ' ';
								current[1] -= 2;
								found = true;
							}
							break;
						}
						visited[current[0]][current[1]] = true;
					}
				}



			}

			// Checks if we still have unvisited cells.
			for(int i = 0; i < size; i++) {
				done = true;
				for(int j = 0; j < size; j++) {
					if(!visited[i][j]) {
						done = false;
						break;
					}
				}
				if(!done) break;
			}

		}

	}
	
	public boolean hasNeighbor(int x, int y, int direction) {
		
		switch(direction) {
		// UP
		case 0:
			return (x - 2) >= 0;
		// RIGHT
		case 1:
			if((y + 2) >= size) return false;
			else return true;
		// DOWN
		case 2:
			if((x + 2) >= size) return false;
			else return true;
		// LEFT
		case 3:
			if((y - 2) < 0) return false;
			else return true;
		}
		
		return false;
		
	}
	
	public boolean hasUnvisitedNeighbors(int x, int y, boolean visited[][]) {
		
		boolean result = false;
		for(int i = 0; i < 4; i++) {
			switch(i) {
			// UP
			case 0:
				if((x - 2) >= 0) {
					if(!visited[x - 2][y]) result = true;
				}
				break;
			// RIGHT
			case 1:
				if((y + 2) < size) {
					if(!visited[x][y + 2]) result = true;
				}
				break;
			// DOWN
			case 2:
				if((x + 2) < size) {
					if(!visited[x + 2][y]) result = true;
				}
				break;
			// LEFT
			case 3:
				if((y - 2) >= 0) {
					if(!visited[x][y - 2]) result = true;
				}
				break;
			}
			
		}
		
		return result;
	}

	// -- END Maze generation

}
