package maze.logic;

import java.io.File;
import java.io.Serializable;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import maze.cli.Input;
import maze.cli.Output;
import maze.gui.ConfigurationWindow;
import maze.gui.GameWindow;
import maze.gui.InputHandler;
import maze.io.GameIO;

/**
 * Core class. Contains all the game logic and objects that the main game uses.
 */
public class GameLogic extends Object implements Serializable {
	
	private static final long serialVersionUID = 1;

	private char board[][];

	private Maze maze;
	private Hero hero;
	private Eagle eagle;
	private Dragon[] dragons;
	private Element sword;
	
	private GameConfig config;
	
	private Task[] tasks;

	private boolean valid = true;
	
	private int CONSOLE = 0;
	private int GRAPHICAL = 1;

	private Input in;
	private Output out;
	
	private transient GameWindow gameWindow;
	private transient ConfigurationWindow configWindow;
	
	private transient InputHandler inputHandler;
	private transient InputHandler configHandler;
	
	private transient Thread inputThread;
	private transient Thread inputConfigThread;

	/** 
	 * Default GameLogic constructor.
	 */
	public GameLogic() {
	}
	
	/**
	 * GameLogic constructor.
	 * 
	 * @param config : current game configuration
	 */
	public GameLogic(GameConfig config) {

		this.config = config;
		
		if(config.getMode() == CONSOLE) {
			in = new Input();
			out = new Output();
		}
	}
	
	/**
	 * GameLogic constructor.
	 * 
	 * @param mode : mode in which the game is (CONSOLE or GRAPHICAL only)
	 */
	public GameLogic(int mode) {
		
		this.valid = true;

		if(mode == CONSOLE) {
			config = new GameConfig(mode, 0.06);
			in = new Input();
			out = new Output();
		}
		else if(mode == GRAPHICAL) {

			config = new GameConfig(mode, 1);
			configWindow = new ConfigurationWindow("Configurações", config);
			
			configHandler = new InputHandler(configWindow);
			inputConfigThread = new Thread(configHandler);
			inputConfigThread.start();
			
			if(getConfiguration(false) != 0) {
				this.valid = false;
				return;
			}
			
			configHandler.setTerminate(true);
		}
	}

	/**
	 * Initializes all game parameters related to the maze.
	 */
	public void init() {
		
		if(configWindow != null) {
			if(configWindow.getMazeFile() != null) {
				loadMaze(this, configWindow.getMazeFile());
				if(dragons != null) {
					config.setMazeDragons(dragons.length);
				}
				else {
					config.setMazeDragons(0);
					dragons = new Dragon[0];
				}
				config.setMazeSize(maze.getSize());
			}
			else {
				
				this.config.setGameKeyCodes(configWindow.getConfig().getGameKeyCodes());
				this.config.setDifficulty(configWindow.getConfig().getDifficulty());
				this.config.setDragonPerc(configWindow.getConfig().getDragonPerc());
				this.config.setMazeSize(configWindow.getConfig().getMazeSize());
				
				config.setMazeDragons((int) (config.getMazeSize() * config.getMazeSize() * config.getDragonPerc()));
				
				maze = new Maze(config.getMazeSize());
				maze.generate();
				
				hero = new Hero(this);
				eagle = new Eagle(hero.getX(), hero.getY(), 'V');
				sword = new Element(this, 'E');
				
				dragons = new Dragon[config.getMazeDragons()];
				for(int i = 0; i < dragons.length; i++) {
					dragons[i] = new Dragon(this);
				}
			}
		}
		else {
			
			config.setMazeDragons((int) (config.getMazeSize() * config.getMazeSize() * config.getDragonPerc()));
					
			maze = new Maze(config.getMazeSize());
			maze.generate();
			
			hero = new Hero(this);
			eagle = new Eagle(hero.getX(), hero.getY(), 'V');
			sword = new Element(this, 'E');
			
			dragons = new Dragon[config.getMazeDragons()];
			for(int i = 0; i < dragons.length; i++) {
				dragons[i] = new Dragon(this);
			}
		}
		
		
		board = new char[config.getMazeSize()][config.getMazeSize()];
	
		tasks = new Task[3];

		createTasks();
	}

	/**
	 * Initializes all game parameters that are transient (that is, won't be serializable -
	 *   won't be saved upon game saving).
	 */
	public void initNonSerializable() {

		if(config.getMode() == GRAPHICAL) {

			gameWindow = new GameWindow(this);
			gameWindow.setFocusable(true);
			gameWindow.setVisible(true);
			
			inputHandler = new InputHandler(gameWindow);
			inputThread = new Thread(inputHandler);
			inputThread.start();
			
			configHandler = new InputHandler(configWindow);
			inputConfigThread = new Thread(configHandler);
			inputConfigThread.start();
		}
	}

	// ++++++++++++++++++++++++++++++++++++++++	//
	//											//
	//					DRAGONS					//
	//											//
	// ++++++++++++++++++++++++++++++++++++++++	//
	
	/**
	 * Checks if all dragons are dead.
	 * 
	 * @return true if all dragons are dead
	 */
	public boolean allDragonsAreDead() {

		for(Dragon dragon: dragons) {
			if(dragon.isAlive()) {
				return false;
			}	
		}

		return true;
	}

	/**
	 * Checks if there is a dragon upon a sword. 
	 * 
	 * @return true if any dragon is upon a sword
	 */
	public boolean noDragonIsUponSword() {

		for(Dragon dragon: dragons) {
			if(dragon != null) {
				if(dragon.hasSword()) {
					return false;
				}
			}
		}

		return true;
	}

	// ++++++++++++++++++++++++++++++++++++++++	//
	//											//
	//					EAGLE					//
	//											//
	// ++++++++++++++++++++++++++++++++++++++++	//

	/**
	 * Sends the eagle towards the sword. 
	 * The hero looses the eagle and the eagle starts moving.
	 */
	public void sendEagle() {
		if(hero.hasEagle()) {
			hero.setHasEagle(false);
			hero.setSymbol('H');
			eagle.sendEagle();
		}
	}

	// ++++++++++++++++++++++++++++++++++++++++	//
	//											//
	//					TASKS					//
	//											//
	// ++++++++++++++++++++++++++++++++++++++++	//
	
	/**
	 * Creates the list of user tasks.
	 * This tasks are objectives to be completed by the hero/player.
	 */
	public void createTasks() {
		
		tasks[0] = new Task("Get a weapon.");
		tasks[1] = new Task("Kill all dragons.");
		tasks[2] = new Task("Find the exit.");
	}
	
	/**
	 * Checks the state of all the tasks.
	 */
	public void checkTasks() {
		
		if(hero.hasSword()) {
			tasks[0].setDone(true);
		}
		
		if(allDragonsAreDead()) {
			tasks[1].setDone(true);
		}
		
		if(hero.hasWon()) {
			tasks[2].setDone(true);
		}
	}
	
	// ++++++++++++++++++++++++++++++++++++++++	//
	//											//
	//					INPUT					//
	//											//
	// ++++++++++++++++++++++++++++++++++++++++	//
	
	/** 
	 * Gets user input.
	 * Depending whether the game is in graphic mode or console mode the input method is different.
	 * 
	 * @return keycode : input keycode
	 */
	public int getConsoleInput() {

		// Get command line keys
		out.drawMsg(2);
		return in.get();
	}
	
	/** 
	 * Executes a hero command (receives a command - after it has been converted from keycode - and executes the order).
	 * 
	 * @param command : order to execute
	 */
	public void runCommand(int command) {

		switch(command) {
		case 4: // SPACE
			if(eagle.isUseful()) {
				sendEagle();
			}
			break;
		default: // 0,1,2,3
			hero.move(this, command);
			break;
		}
	}

	/**
	 * Converts a keycode into a game command.
	 * 
	 * @param keyCode : key pressed by the user
	 * @return game command corresponding to the keycode
	 */
	public int getCurrentCommand(int keyCode) {

		for(int i = 0; i < config.getGameKeyCodes().length; i++) {

			if(config.getGameKeyCodes()[i] == keyCode) {

				return i;
			}
		}

		return -1;
	}

	/**
	 * Generates a board with the complete maze and all the elements.
	 */
	public void setGameBoard() {

		// Get board without elements.
		for(int i = 0; i < maze.getSize(); i++) {
			for(int j = 0; j < maze.getSize(); j++) {
				board[i][j] = maze.getTiles()[i][j];
			}
		}

		// Includes exit.
		if(maze != null && maze.getExit() != null) {
			board[maze.getExit().getX()][maze.getExit().getY()] = maze.getExit().getSymbol();
		}

		// Includes hero.
		if(hero != null) {
			board[hero.getX()][hero.getY()] = hero.getSymbol();
		}

		// Includes eagle.
		if((eagle != null) && !hero.hasEagle() && eagle.isAlive()) {
			board[eagle.getX()][eagle.getY()] = eagle.getSymbol();
		}

		// Includes all dragons.
		if(dragons != null) {
			for(Dragon dragon: dragons) {
				if(dragon != null) {
					if(dragon.isAlive()) board[dragon.getX()][dragon.getY()] = dragon.getSymbol();
				}
			}
		}
		// Includes sword.
		if(sword != null) {
			if((hero != null) && (eagle != null)) {
				if(!hero.hasSword() && noDragonIsUponSword() && !eagle.hasSword()) {
					board[sword.getX()][sword.getY()] = sword.getSymbol();
				}
			}

		}

	}

	/**
	 * Calls method update(GameLogic game) for all dragons.
	 * For more info consult the methods.
	 */
	public void updateAllDragons() {
		if(dragons != null) {
			for(Dragon dragon: dragons) {
				dragon.update(this);
			}
		}
	}

	/**
	 * Gets the configuration from the configuration window (graphical mode only).
	 * @param disableLoad 
	 * 
	 * @return 0 if successful
	 */
	public int getConfiguration(boolean disableLoad) {

		if(disableLoad) configWindow.setLoadDisabled(true);
		configWindow.setVisible(true);

		int state = -1;

		do {
			state = configHandler.getNextCommand();
		}while(state == -1);

		configHandler.removeCommand();

		if(disableLoad) configWindow.setLoadDisabled(false);
		configWindow.setVisible(false);
		

		if(state == 2) { // ERROR
			return 1;
		}

		return 0;
	}

	// **************************************** //
	// ++++++++++++++++++++++++++++++++++++++++	//
	//											//
	//			   MAIN GAME LOOP				//
	//											//
	// ++++++++++++++++++++++++++++++++++++++++	//
	// **************************************** //
	
	/**
	 * Main game loop.
	 * 
	 * @return 1 - new game; 5 - quit
	 */
	public int loop() {
		
		int NO_COMMAND = -10;

		int command = NO_COMMAND;
		boolean done = false;
		
		setGameBoard();

		// First draw.
		if(config.getMode() == CONSOLE) {
			out.draw(this);
		} else if(config.getMode() == GRAPHICAL) {
			gameWindow.paint();
		}

		// +++++++++++++++++++++++++++++++++++++
		//				Begin Loop
		// +++++++++++++++++++++++++++++++++++++
		while(hero.isAlive() && !done) {
			
			if(config.getMode() == GRAPHICAL) {
				command = inputHandler.getNextCommand();
			}
			else {
				command = getConsoleInput();
			}

			if(getCurrentCommand(command) >= 0) {

				if(config.getMode() == GRAPHICAL) {
					inputHandler.removeCommand();
				}

				updateAllDragons();
				
				runCommand(getCurrentCommand(command));
				
				eagle.update(this);

				hero.update(this);

				checkTasks();
				
				if(hero.hasWon()) {
					done = true;
					break;
				}
				
				setGameBoard();
				
				if(config.getMode() == CONSOLE) {
					out.draw(this);
				} else if(config.getMode() == GRAPHICAL) {
					gameWindow.paint();
				}

			}
			else {
				if(config.getMode() == GRAPHICAL) {
					
					if(command != -1) {
						inputHandler.removeCommand();
					}
					
					/*
					 * 			MENU OPTIONS
					 */
					switch(command) {
					/*
					 * ___________________________________
					 * 
					 * 				NEW GAME
					 * ___________________________________
					 */
					case 1:
						done = true;
						break;
						/*
						 * ___________________________________
						 * 
						 * 				SAVE GAME
						 * ___________________________________
						 */
					case 2:
						saveGame();
						break;
						/*
						 * ___________________________________
						 * 
						 * 				LOAD GAME
						 * ___________________________________
						 */
					case 3:
						if(loadGame() == 0) {
							gameWindow.paint();
						}
						break;
						/*
						 * ___________________________________
						 * 
						 * 			CONFIGURATIONS
						 * ___________________________________
						 */
					case 4:
						gameWindow.setVisible(false);
						getConfiguration(true);
						this.config.setGameKeyCodes(configWindow.getConfig().getGameKeyCodes());
						gameWindow.setVisible(true);
						gameWindow.setFocusable(true);
						break;
						/*
						 * ___________________________________
						 * 
						 * 				QUIT
						 * ___________________________________
						 */
					case 5:
						done = true;
						break;
					default:
						break;
					}
				}
				else {
					if(command == -1) {
						done = true;
					}
				}
			}

		}	
		// +++++++++++++++++++++++++++++++++++++
		//				END OF LOOP
		// +++++++++++++++++++++++++++++++++++++
		
		if(config.getMode() == GRAPHICAL) {
			gameWindow.paint();
			inputHandler.setTerminate(true);
			configHandler.setTerminate(true);
			
			this.config.setDifficulty(configWindow.getConfig().getDifficulty());
			this.config.setDragonPerc(configWindow.getConfig().getDragonPerc());
			this.config.setMazeSize(configWindow.getConfig().getMazeSize());
			
			if(command > 5) {
				if(hero.hasWon()) {
					JOptionPane.showMessageDialog(gameWindow, "You won!");
				}
				else {
					JOptionPane.showMessageDialog(gameWindow, "Game Over!");
				}
			}
			
			gameWindow.dispose();
			return command;
		}
		else if(config.getMode() == CONSOLE) {
			out.draw(this);
			
			if(hero.hasWon()) {
				out.drawWinningMessage();
			}
			else {
				out.drawGameOver();
			}
			
			if(out.playAgain(in)) {
				return 1;
			}
		}
		
		
		return -1;
	}

	// ++++++++++++++++++++++++++++++++++++++++	//
	//											//
	//			   GETTERS/SETTERS				//
	//											//
	// ++++++++++++++++++++++++++++++++++++++++	//
	
	public void loadMaze(GameLogic gameLogic, String mazeFile) {
		GameIO gameIO = new GameIO();

		gameIO.loadMaze(this, mazeFile);
	}
	
	/**
	 * Creates an instance of GameIO to load the game.
	 * For more information on GameIO consult the class.
	 * 
	 * @return 0 if the user has pressed the 'load' button
	 */
	public int loadGame() {
		GameIO gameIO = new GameIO();
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(".sav files", new String[] {"sav"});
		fileChooser.setFileFilter(filter);
		fileChooser.setCurrentDirectory(new File( "." ));
		
		if (fileChooser.showOpenDialog(gameWindow) == JFileChooser.APPROVE_OPTION) {

			String fileName = fileChooser.getSelectedFile().getName();
			gameIO.loadGame(this, fileName);
		}
		else {
			return -1;
		}
		
		return 0;
	}

	/**
	 * Creates an instance of GameIO to save the game.
	 * For more information on GameIO consult the class.
	 * 
	 * @return 0 if the user has pressed the 'save' button
	 */
	public int saveGame() {
		GameIO gameIO = new GameIO();
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(".sav files", new String[] {"sav"});
		fileChooser.setFileFilter(filter);
		fileChooser.setCurrentDirectory(new File( "." ));
		
		if (fileChooser.showSaveDialog(gameWindow) == JFileChooser.APPROVE_OPTION) {
			
			String fileName = fileChooser.getSelectedFile().getName();
			gameIO.save(this, fileName, ".sav");
		}
		else {
			return -1;
		}
		
		return 0;
	}

	/**
	 * Gets the game instance of [maze].
	 * 
	 * @return the current instance of Maze
	 */
	public Maze getMaze() {
		return maze;
	}

	/**
	 * Sets the game instance of [sword].
	 * 
	 * @param maze : parameter to be set
	 */
	public void setMaze(Maze maze) {
		this.maze = maze;
	}
	
	/**
	 * Gets the game instance of [hero].
	 * 
	 * @return the hero
	 */
	public Hero getHero() {
		return hero;
	}

	/**
	 * Sets the game instance of [hero].
	 * 
	 * @param hero : parameter to be set
	 */
	public void setHero(Hero hero) {
		this.hero = hero;
	}
	
	/**
	 * Gets the game instance of [sword].
	 * 
	 * @return the sword
	 */
	public Element getSword() {
		return sword;
	}

	/**
	 * Sets the game instance of [sword].
	 *  
	 * @param sword : parameter to be set
	 */
	public void setSword(Element sword) {
		this.sword = sword;
	}

	/**
	 * Gets the game instance of [eagle].
	 *  
	 * @return the eagle
	 */
	public Eagle getEagle() {
		return eagle;
	}

	/**
	 * Sets the game instance of [eagle].
	 * 
	 * @param eagle : parameter to be set
	 */
	public void setEagle(Eagle eagle) {
		this.eagle = eagle;
	}
	
	/**
	 * Gets the game instances of [dragon] from the array [dragons].
	 * 
	 * @return the dragons
	 */
	public Dragon[] getDragons() {
		return dragons;
	}

	/**
	 * Sets the array [dragons].
	 * 
	 * @param dragons : parameter to be set
	 */
	public void setDragons(Dragon[] dragons) {
		this.dragons = dragons;
	}
	
	/**
	 * Gets the current game configuration.
	 * 
	 * @return GameConfig configuration
	 */
	public GameConfig getConfig() {
		return this.config;
	}
	
	/**
	 * Sets the current game configuration.
	 * 
	 * @param config : configuration to set
	 */
	public void setConfig(GameConfig config) {
		this.config = config;
	}

	/**
	 * Gets the array containing the tasks.
	 * 
	 * @return the tasks
	 */
	public Task[] getTasks() {
		return tasks;
	}

	/**
	 * Sets the array containing the tasks.
	 * 
	 * @param tasks : array of tasks to be set
	 */
	public void setTasks(Task[] tasks) {
		this.tasks = tasks;
	}

	/**
	 * Gets the array containing the game board chars.
	 * 
	 * @return the board
	 */
	public char[][] getBoard() {
		return board;
	}

	/**
	 * Sets the array containing the game board chars.
	 * 
	 * @param board : array of game board chars to be set
	 */
	public void setBoard(char board[][]) {
		this.board = board;
	}

	/**
	 * Gets the valid value for this gameLogic instance.
	 * An instance of this type is valid if it has a configuration assigned to it.
	 * 
	 * @return true if valid
	 */
	public boolean isValid() {
		return valid;
	}

	/**
	 * Sets the valid value for this gameLogic instance.
	 * 
	 * @param valid : value to set
	 */
	public void setValid(boolean valid) {
		this.valid = valid;
	}

	/**
	 * Gets the configuration window instance.
	 * 
	 * @return ConfigWindow configuration window
	 */
	public ConfigurationWindow getConfigWindow() {
		return configWindow;
	}

	/**
	 * Sets the configuration window instance.
	 * 
	 * @param configWindow : configuration window to set
	 */
	public void setConfigWindow(ConfigurationWindow configWindow) {
		this.configWindow = configWindow;
	}
	
}
