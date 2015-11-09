package maze.test;

import static org.junit.Assert.*;
import maze.logic.Dragon;
import maze.logic.Element;
import maze.logic.GameLogic;
import maze.logic.Hero;
import maze.logic.Maze;

import org.junit.Test;

/**
 * Test class that handles all hero-related testing.
 */
public class TestClass1 {

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
	public void TestMoveHero() {
		
		Hero comp = new Hero(1,2,'H');
		
		commands = "dw";
		
		gameTest = new GameLogic();
		
		gameTest.setHero(new Hero(1,1,'H'));
		
		gameTest.setMaze(new Maze(maze));
		
		execCommands();
		
		assertEquals(comp,gameTest.getHero());
	}
	
	@Test
	public void TestMoveHeroWall() {
		
		Hero comp = new Hero(1,1,'H');
		
		commands = "wa";
		
		gameTest = new GameLogic();
		
		gameTest.setHero(new Hero(1,1,'H'));
		
		gameTest.setMaze(new Maze(maze));
		
		execCommands();
		
		assertEquals(comp,gameTest.getHero());
	}
	
	@Test
	public void TestCatchTheSword() {
		
		commands = "ddds";
		
		gameTest = new GameLogic();
		
		gameTest.setHero(new Hero(1,1,'H'));
		
		gameTest.setMaze(new Maze(maze));
		
		gameTest.setSword(new Element(2,4,'E'));
		
		execCommands();
		
		gameTest.getHero().update(gameTest);
		
		assertTrue(gameTest.getHero().hasSword());
	}
	
	@Test
	public void TestLaDerrote() {
		
		commands = "ddd";
		
		gameTest = new GameLogic();
		
		gameTest.setHero(new Hero(1,1,'H'));
		
		gameTest.getHero().setHasSword(true);
		
		gameTest.setMaze(new Maze(maze));
		
		gameTest.setDragons(new Dragon[1]);
		
		gameTest.getDragons()[0] = new Dragon(2,4,'D');
		
		execCommands();
		
		gameTest.getHero().update(gameTest);
		
		assertFalse(gameTest.getDragons()[0].isAlive());
	}
	
	@Test
	public void TestLeSlain() {
		
		commands = "ddd";
		
		gameTest = new GameLogic();
		
		gameTest.setHero(new Hero(1,1,'H'));
		
		gameTest.setMaze(new Maze(maze));
		
		gameTest.setDragons(new Dragon[1]);
		
		gameTest.getDragons()[0] = new Dragon(2,4,'D');
		
		execCommands();
		
		gameTest.getHero().update(gameTest);
		
		assertFalse(gameTest.getHero().isAlive());
	}
	
	@Test
	public void TestLaVitoire() {
		
		commands = "ddddw";
		
		gameTest = new GameLogic();
		
		gameTest.setHero(new Hero(1,1,'H'));
		
		gameTest.getHero().setHasSword(true);
		
		gameTest.setMaze(new Maze(maze));
		
		gameTest.setDragons(new Dragon[1]);
		
		gameTest.getDragons()[0] = new Dragon(2,4,'D');
		
		gameTest.getDragons()[0].setAlive(false);
		
		gameTest.getMaze().setExit(new Element(0,5,'S'));
		
		execCommands();
		
		gameTest.getHero().update(gameTest);
		
		assertTrue(gameTest.getHero().hasWon());
	}
	
	@Test
	public void TestCoward() {
		
		commands = "ddddw";
		
		gameTest = new GameLogic();
		
		gameTest.setHero(new Hero(1,1,'H'));
		
		gameTest.setMaze(new Maze(maze));
		
		gameTest.setDragons(new Dragon[1]);
		
		gameTest.getDragons()[0] = new Dragon(2,4,'D');
		
		gameTest.getMaze().setExit(new Element(0,5,'S'));
		
		execCommands();
		
		assertFalse(gameTest.getHero().hasWon());
		
		gameTest.getHero().setHasSword(true);
		
		assertFalse(gameTest.getHero().hasWon());
	}
	
	/**
	 * AUXILIARES
	 */
	private void execCommands() {

		for(int i = 0; i < commands.length(); i++) {
			switch(commands.charAt(i)) {
			case 'w':
				gameTest.getHero().move(gameTest, 0);
				break;
			case 'd':
				gameTest.getHero().move(gameTest, 1);
				break;
			case 's':
				gameTest.getHero().move(gameTest, 2);
				break;
			case 'a':
				gameTest.getHero().move(gameTest, 3);
				break;
			default:
				break;
			}
		}
	}
	
}
