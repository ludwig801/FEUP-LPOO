package maze.test;

import static org.junit.Assert.*;

import org.junit.Test;

import maze.logic.Dragon;
import maze.logic.Element;
import maze.logic.GameConfig;
import maze.logic.GameLogic;
import maze.logic.Maze;

/**
 * Test class that handles all dragon(s)-related testing.
 */
public class TestClass2 {
	
	// Char matrix representing the labyrinth 
	private char maze[][] = {
			{ 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x' },
			{ 'x', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'x' },
			{ 'x', ' ', 'x', 'x', ' ', 'x', ' ', 'x', ' ', 'x' },
			{ 'x', ' ', 'x', 'x', ' ', 'x', ' ', 'x', ' ', 'x' },
			{ 'x', ' ', 'x', 'x', ' ', 'x', ' ', 'x', ' ', 'x' },
			{ 'x', ' ', ' ', ' ', ' ', ' ', ' ', 'x', ' ', 'x' },
			{ 'x', ' ', 'x', 'x', ' ', 'x', ' ', 'x', ' ', 'x' },
			{ 'x', ' ', 'x', 'x', ' ', 'x', ' ', 'x', ' ', 'x' },
			{ 'x', ' ', 'x', 'x', ' ', ' ', ' ', ' ', ' ', 'x' },
			{ 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x' },

	};
	
	private GameLogic gameTest;
	private String commands;
	
	@Test
	public void TestMoveDragon() {
		
		Dragon comp = new Dragon(4,4,'D');
		
		commands = "dddsss";
		
		gameTest = new GameLogic();
		
		gameTest.setDragons(new Dragon[1]);
		
		gameTest.getDragons()[0] = new Dragon(1,1,'D');
		
		gameTest.setMaze(new Maze(maze));
		
		execCommands();
		
		assertEquals(comp,gameTest.getDragons()[0]);
	}
	
	@Test
	public void TestSleepingDragon() {
		
		Dragon comp = new Dragon(1,1,'D');
		
		commands = "dddsss";
		
		gameTest = new GameLogic();
		
		gameTest.setDragons(new Dragon[1]);
		
		gameTest.getDragons()[0] = new Dragon(1,1,'D');
		
		gameTest.getDragons()[0].setAwake(false);
		
		gameTest.setMaze(new Maze(maze));
		
		gameTest.updateAllDragons();
		
		assertEquals(comp,gameTest.getDragons()[0]);
	}
	
	@Test
	public void TestDragonFoundSword() {
		
		commands = "ddds";
		
		gameTest = new GameLogic();
		
		GameConfig gameConfig = new GameConfig(10, 3, 0.2);
		
		gameTest.setConfig(gameConfig);
		
		gameTest.setDragons(new Dragon[1]);
		
		gameTest.getDragons()[0] = new Dragon(1,1,'D');
		
		gameTest.getDragons()[0].setAwake(false);
		
		gameTest.setMaze(new Maze(maze));
		
		gameTest.setSword(new Element(2,4,'E'));
		
		execCommands();
		
		gameTest.updateAllDragons();
		
		assertTrue(gameTest.getDragons()[0].hasSword());
	}
	
	@Test
	public void TestDragonsCannotExit() {
		
		Element dragonPos = new Element(1,3,'D');
		
		commands = "ddw";
		
		gameTest = new GameLogic();
	
		gameTest.setMaze(new Maze(maze));
		
		gameTest.getMaze().setExit(new Element(0,3,'S'));
		
		gameTest.setDragons(new Dragon[1]);
	
		gameTest.getDragons()[0] = new Dragon(1,1,'D');
		
		execCommands();

		assertFalse(gameTest.getDragons()[0].isAt(gameTest.getMaze().getExit()));
		
		assertTrue(gameTest.getDragons()[0].isAt(dragonPos));
	}
	
	@Test
	public void TestMultipleDragons() {
		
		Dragon comp = new Dragon(8,8,'D');
		
		commands = "ssssssswwdddaawwwwdddddds";
		
		gameTest = new GameLogic();
		
		GameConfig gameConfig = new GameConfig(10, 1, 0.2);
		
		gameTest.setConfig(gameConfig);
		
		gameTest.setSword(new Element(2,6,'E'));
		
		gameTest.setMaze(new Maze(maze));
		
		gameTest.setDragons(new Dragon[4]);
	
		gameTest.getDragons()[0] = new Dragon(1,1,'D');
		
		gameTest.getDragons()[1] = new Dragon(8,1,'D');
		
		gameTest.getDragons()[2] = new Dragon(5,4,'D');
		
		gameTest.getDragons()[2].setAwake(false);
		
		gameTest.getDragons()[3] = new Dragon(1,7,'D');
		
		execCommands();
		
		gameTest.updateAllDragons();
		
		assertTrue(gameTest.getDragons()[0].hasSword());
		
		commands = "ssssssdd";
		
		execCommands();
		
		assertEquals(comp,gameTest.getDragons()[0]);
	}
	
	/**
	 * AUXILIARES
	 */
	private void execCommands() {

		for(int i = 0; i < commands.length(); i++) {
			switch(commands.charAt(i)) {
			case 'w':
				gameTest.getDragons()[0].move(gameTest, 0);
				break;
			case 'd':
				gameTest.getDragons()[0].move(gameTest, 1);
				break;
			case 's':
				gameTest.getDragons()[0].move(gameTest, 2);
				break;
			case 'a':
				gameTest.getDragons()[0].move(gameTest, 3);
				break;
			default:
				break;
			}
		}
	}
}
