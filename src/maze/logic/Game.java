package maze.logic;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import maze.gui.EditorWindow;
import maze.gui.InputHandler;
import maze.gui.MenuWindow;
import maze.io.GameIO;

/**
 * Class that contains the main() function and the game's "main menu".
 */
public class Game {

	public static void main(String[] args) {

		gameMenu();

		System.exit(0);
	}

	/**
	 * Main menu. Deals with all of the options in the menu (in the graphical interface).
	 * Allows the player to choose the console.
	 */
	private static void gameMenu() {
		
		int state = 0;
		int GRAPHICAL = 1;
		int CONSOLE = 0;
		int mode = CONSOLE;
		
		GameLogic game;
		MenuWindow menuWindow;
		EditorWindow editorWindow;
		InputHandler menuHandler;
		InputHandler editorHandler;
		Thread inputMenuThread;
		Thread inputEditorThread;
		
		game = null;
		menuHandler = null;
		menuWindow = null;
		editorWindow = null;
		
		/*
		 * 
		 */
		switch(JOptionPane.showConfirmDialog(null, "Do you wish to play in graphical mode?")) {
		case JOptionPane.YES_OPTION: // GRAPHICAL

			menuWindow = new MenuWindow("Menu");
			menuHandler = new InputHandler(menuWindow);
			
			inputMenuThread = new Thread(menuHandler);
			inputMenuThread.start();
			
			mode = GRAPHICAL;
			
			break;
		case JOptionPane.NO_OPTION: // CONSOLE
			mode = CONSOLE;
			break;
		case JOptionPane.CANCEL_OPTION: // CANCEL
			return;
		default:
			return;
		}

		while(state != -1) {
			
			int EXIT = -1;
			int NEW_GAME = 1;
			
			if(mode == GRAPHICAL) {
				
				int returnValue = EXIT;
				int innerState = 0;
				
				/* The player choses an option from the menu */
				innerState = menuHandler.getNextCommand();
				
				if(innerState > 0) {
					menuHandler.removeCommand();
				}
				
				switch(innerState) {
				/*	___________________________________________
				 * 
				 * 					PLAY
				 *  ___________________________________________
				 */
				case 1:
					menuWindow.setVisible(false);
					
					game = new GameLogic(mode);

					do {
						if(game.isValid()) {
							game.init();
							game.initNonSerializable();
							returnValue = game.loop();
						}
						
					}while(returnValue == NEW_GAME);

					menuWindow.setVisible(true);
					break;
				case 2: // EDITOR
					int value = -1;

					try {
						value = Integer.parseInt(JOptionPane.showInputDialog("Size of the maze size for the editor? [5-21]"));
					}
					catch(NumberFormatException e) {
						break;
					}
					
					if(value > 4 && value < 22) {
						menuWindow.setVisible(false);

						editorWindow = new EditorWindow(value);
						editorWindow.setFocusable(true);
						editorWindow.setVisible(true);
						editorWindow.paint();

						editorHandler = new InputHandler(editorWindow);		
						inputEditorThread = new Thread(editorHandler);
						inputEditorThread.start();

						value = -1;
						
						do {

							value = editorHandler.getNextCommand();

							if(value > 0) {
								editorHandler.removeCommand();
							}

							switch(value) {
							case 1:
								// SAVE
								GameIO gameIO = new GameIO();
								JFileChooser fileChooser = new JFileChooser();
								FileNameExtensionFilter filter = new FileNameExtensionFilter(".maze files", new String[] {"maze"});
								fileChooser.setFileFilter(filter);
								fileChooser.setCurrentDirectory(new File( "." ));
								
								if (fileChooser.showSaveDialog(editorWindow) == JFileChooser.APPROVE_OPTION) {
									
									String fileName = fileChooser.getSelectedFile().getName();
									gameIO.save(editorWindow.getGame(), fileName, ".maze");
								}
								else {
									break;
								}
								break;
							case 2:
								// HELP
								break;
							case 3:
								// QUIT
								break;
							default:
								break;
							}

						}while(value != 3);

						editorWindow.dispose();

						menuWindow.setVisible(true);

					}
					break;
				case 3: // CREDITS
					JDialog helpDialog = new JDialog(menuWindow,"Credits",Dialog.ModalityType.APPLICATION_MODAL);

					helpDialog.setFont(new Font("Sakkal Majalla", Font.BOLD, 22));
					
					helpDialog.setLayout(new GridLayout(5,1,10,10));
					helpDialog.setFocusable(true);
					helpDialog.setResizable(false);
					
					helpDialog.add(new JLabel("", JLabel.CENTER), BorderFactory.createLineBorder(new Color(0,0,0),2));
					helpDialog.add(new JLabel("Kevin Amorim - ei12057", JLabel.CENTER), BorderFactory.createLineBorder(new Color(0,0,0),2));
					helpDialog.add(new JLabel("Luis Magalhaes - ei12054", JLabel.CENTER), BorderFactory.createLineBorder(new Color(0,0,0),2));
					helpDialog.add(new JLabel("", JLabel.CENTER), BorderFactory.createLineBorder(new Color(0,0,0),2));
					helpDialog.add(new JLabel("Os nossos agradecimentos em particular aos professores de LPOO pela ajuda"
							+ " fornecida durante todo o desenvolvimento do projeto. ", JLabel.CENTER), BorderFactory.createLineBorder(new Color(0,0,0),2));

					helpDialog.pack();
					helpDialog.setLocationRelativeTo(null);
					helpDialog.setVisible(true);
					break;
				case 4: // QUIT
					state = EXIT;
					break;
				default:// DO NOTHING
					break;
				}
				
			}
			else {
				do {
					game = new GameLogic(new GameConfig(mode));
					game.init();
					state = game.loop();
				}while(state == NEW_GAME);
			}
		}

	}
	
}
