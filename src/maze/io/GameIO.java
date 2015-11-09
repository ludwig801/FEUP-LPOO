package maze.io;

import maze.logic.GameLogic;
import java.io.*;

/**
 * Class that handles all "save/load from file" options.
 * 
 * It is used to save a game or a maze to ".sav" or ".maze" files (accordingly).
 * It is also used to load a custom maze or a saved game. 
 */
public class GameIO {

	/**
	 * Default Constructor.
	 */
	public GameIO() {}

	/**
	 * Saves a given GameLogic to a file with the extension '.sav'.
	 *   The file name is passed as a parameter.
	 *   
	 * @param game : the GameLogic instance
	 * @param fileName : the filename of the saved file
	 * 
	 * @return 0 if OK
	 */
	public int save(GameLogic game, String fileName, String extension) {
		ObjectOutputStream os = null;
		try {
			
			if(!fileName.contains(extension)) {
				fileName += extension;
			}
			os = new ObjectOutputStream(new FileOutputStream(fileName));
			
			os.writeObject(game);
		}
		catch(IOException e) {
			e.printStackTrace();
			return -1;
		}
		finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
					return -2;
				}
			}
		}
		
		return 0;
	}
	
	/**
	 * Loads to a given GameLogic the game in the file with the filename: "fileName".sav
	 *   
	 * @param game : game to be set
	 * @param fileName : filename to be read the information from
	 * 
	 * @return 0 if OK
	 */
	public int loadGame(GameLogic game, String fileName) {
		ObjectInputStream in = null;

		GameLogic temp = null;
		
		try {
			in = new ObjectInputStream(new FileInputStream(fileName));
			
			temp = (GameLogic) in.readObject();	
		}
		catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
		catch(ClassNotFoundException e ) {
			e.printStackTrace();
			return -1;
		}
		finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
					return -2;
				}
			}
		}
		
		game.setBoard(temp.getBoard());
		game.setConfig(temp.getConfig());
		game.setDragons(temp.getDragons());
		game.setEagle(temp.getEagle());
		game.setHero(temp.getHero());
		game.setMaze(temp.getMaze());
		game.setSword(temp.getSword());
		game.setTasks(temp.getTasks());
		game.setValid(temp.isValid());
		
		game.getConfigWindow().setConfig(game.getConfig());
		
		return 0;
	}
	
	/**
	 * Loads to a given GameLogic a game in the file with the filename: "fileName".maze
	 *   The difference to the method above is that this one reads only the maze and the elements (hero, sword, etc...)
	 *   
	 * @param game : game to be set
	 * @param fileName : filename to be read the information from
	 * 
	 * @return 0 if OK
	 */
	public int loadMaze(GameLogic game, String fileName) {
		ObjectInputStream in = null;

		GameLogic temp = null;
		
		try {
			in = new ObjectInputStream(new FileInputStream(fileName));
			
			temp = (GameLogic) in.readObject();	
		}
		catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
		catch(ClassNotFoundException e ) {
			e.printStackTrace();
			return -1;
		}
		finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
					return -2;
				}
			}
		}
		
		if(temp == null) {
			System.out.println("null temp");
		}

		game.setDragons(temp.getDragons());
		game.setEagle(temp.getEagle());
		game.setHero(temp.getHero());
		game.setMaze(temp.getMaze());
		game.setSword(temp.getSword());
		
		return 0;
	}
}
