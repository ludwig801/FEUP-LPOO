package maze.gui;

import java.awt.Dialog;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import maze.logic.GameConfig;

import javax.swing.JLabel;
import javax.swing.JComboBox;

import java.awt.Font;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JSlider;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import java.awt.CardLayout;

import javax.swing.SwingConstants;
import javax.swing.JMenu;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JRadioButton;
import javax.swing.JButton;

/**
 * Class that represents all "configuration window" type objects.
 * 
 * A configuration window is where the user has a series of options:
 *   - Load a custom maze (created in the editor);
 *   - Define the parameters for the creation of a new maze;
 *   - Define custom keys to control the game character.
 *   
 * @see Window
 */
@SuppressWarnings("serial")
public class ConfigurationWindow extends Window {

	private JPanel panel;
	private JPanel layer1, layer2;
	private GameConfig config;
	private String mazeFile = null;
	
	JButton btnLoadMaze;
	JButton btnUnloadMaze;
	
	/**
	 * Default constructor.
	 */
	public ConfigurationWindow() {
		initialize();	
	}
	
	/**
	 * Constructor.
	 * Receives the title of the window and a game configuration.
	 * 
	 * @param title : the title of the window
	 * @param c : game configuration
	 */
	public ConfigurationWindow(String title, final GameConfig c) {
		super(title);
		this.config = new GameConfig(c);
		
		initialize();		
	}

	/**
	 * Initializes the entire configurations window.
	 *   Adds the labels, buttons, sliders, etc...
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initialize() {

		setBounds(100, 100, 711, 512);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		panel = new JPanel();
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(panel);
		panel.setLayout(new CardLayout(0, 0));
		
		/* _______________________________________________________________________________________
		 * 
		 * 										LAYER 1
		 * _______________________________________________________________________________________
		 */
		
		layer1 = new JPanel();
		panel.add(layer1, "maze_config");
		layer1.setLayout(new GridLayout(4, 4, 0, 0));
		
		final JLabel mazeSizeLabel = new JLabel("Maze size");
		
		mazeSizeLabel.setFocusable(false);
		mazeSizeLabel.setFont(new Font("Sakkal Majalla", Font.BOLD, 40));
		
		final JSlider mazeSize = new JSlider();
		mazeSize.setFocusable(false);
		mazeSize.setPaintLabels(true);
		mazeSize.setPaintTicks(true);
		mazeSize.setSnapToTicks(true);
		mazeSize.setMajorTickSpacing(2);
		mazeSize.setMaximum(21);
		mazeSize.setMinimum(5);
		mazeSize.setMinorTickSpacing(2);
		
		final JLabel difficultyLabel = new JLabel("Difficulty");
		difficultyLabel.setFocusable(false);
		difficultyLabel.setFont(new Font("Sakkal Majalla", Font.BOLD, 40));
		final JComboBox difficulty = new JComboBox();
		difficulty.setFocusable(false);
		difficulty.setModel(new DefaultComboBoxModel(new String[] {"Dumb dragons ", "Smart dragons (they sleep)", "Dragons high on caffeine"}));
		difficulty.setFont(new Font("Sakkal Majalla", Font.PLAIN, 28));
		
		/*
		 * Dragons percentage
		 */
		final JSlider dragonPerc = new JSlider();
		dragonPerc.setFocusable(false);
		dragonPerc.setValue(4);
		dragonPerc.setToolTipText("");
		dragonPerc.setSnapToTicks(true);
		dragonPerc.setMinorTickSpacing(2);
		dragonPerc.setMinimum(4);
		dragonPerc.setMaximum(10);
		dragonPerc.setMajorTickSpacing(2);
		
		final JLabel dragonPercLabel = new JLabel("Dragons   " + dragonPerc.getValue() + "%");
		
		dragonPercLabel.setFocusable(false);
		dragonPercLabel.setFont(new Font("Sakkal Majalla", Font.BOLD, 40));

		dragonPerc.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				dragonPercLabel.setText("Dragons   " + dragonPerc.getValue() + "%");
			}
		});
		
		btnLoadMaze = new JButton("Load Maze");
		btnUnloadMaze = new JButton("Unload Maze");
		
		btnUnloadMaze.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				setMazeFile(null);
				
				mazeSizeLabel.setEnabled(true);
				mazeSize.setEnabled(true);
				//difficultyLabel.setEnabled(true);
				//difficulty.setEnabled(true);
				dragonPercLabel.setEnabled(true);
				dragonPerc.setEnabled(true);	
			}
		});
		
		btnLoadMaze.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(chooseMaze()) {
					btnUnloadMaze.setEnabled(true);
					
					mazeSizeLabel.setEnabled(false);
					mazeSize.setEnabled(false);
					//difficultyLabel.setEnabled(false);
					//difficulty.setEnabled(false);
					dragonPercLabel.setEnabled(false);
					dragonPerc.setEnabled(false);	
				}
			}
		});
		
		btnLoadMaze.setFont(new Font("Sakkal Majalla", Font.BOLD, 34));
		btnUnloadMaze.setFont(new Font("Sakkal Majalla", Font.BOLD, 34));
		
		btnLoadMaze.setEnabled(true);
		btnUnloadMaze.setEnabled(false);
		
		layer1.add(mazeSizeLabel);
		layer1.add(mazeSize);
		layer1.add(difficultyLabel);
		layer1.add(difficulty);
		layer1.add(dragonPercLabel);
		layer1.add(dragonPerc);
		layer1.add(btnLoadMaze);
		layer1.add(btnUnloadMaze);
		
		/* _______________________________________________________________________________________
		 * 
		 * 										LAYER 2
		 * _______________________________________________________________________________________
		 */
		
		layer2 = new JPanel();
		panel.add(layer2, "keys_config");
		layer2.setLayout(new GridLayout(5, 2, 0, 0));
		
		JLabel upKey = new JLabel("UP");
		upKey.setHorizontalAlignment(SwingConstants.CENTER);
		upKey.setFont(new Font("Sakkal Majalla", Font.BOLD, 40));
		
		JLabel downKey = new JLabel("DOWN");
		downKey.setHorizontalAlignment(SwingConstants.CENTER);
		downKey.setFont(new Font("Sakkal Majalla", Font.BOLD, 40));
		downKey.setFont(new Font("Sakkal Majalla", Font.BOLD, 40));
		
		JLabel rightKey = new JLabel("RIGHT");
		rightKey.setHorizontalAlignment(SwingConstants.CENTER);
		rightKey.setFont(new Font("Sakkal Majalla", Font.BOLD, 40));
		rightKey.setFont(new Font("Sakkal Majalla", Font.BOLD, 40));
		
		JLabel leftKey = new JLabel("LEFT");
		leftKey.setHorizontalAlignment(SwingConstants.CENTER);
		leftKey.setFont(new Font("Sakkal Majalla", Font.BOLD, 40));
		leftKey.setFont(new Font("Sakkal Majalla", Font.BOLD, 40));
		
		JLabel eagleKey = new JLabel("EAGLE AWAY!");
		eagleKey.setHorizontalAlignment(SwingConstants.CENTER);
		eagleKey.setFont(new Font("Sakkal Majalla", Font.BOLD, 40));
		eagleKey.setFont(new Font("Sakkal Majalla", Font.BOLD, 40));
		
		final JButton upKeyField = new JButton();
		upKeyField.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				upKeyField.setText(KeyEvent.getKeyText(config.getGameKeyCodes()[0]));
			}
		});
		upKeyField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				upKeyField.setText("Press the new key");
				receiveNewKey(0);
			}
		});
		upKeyField.setFocusable(false);
		upKeyField.setHorizontalAlignment(SwingConstants.CENTER);
		upKeyField.setText(KeyEvent.getKeyText(config.getGameKeyCodes()[0]));

		final JButton downKeyField = new JButton();
		downKeyField.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				downKeyField.setText(KeyEvent.getKeyText(config.getGameKeyCodes()[2]));
			}
		});
		downKeyField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				downKeyField.setText("Press the new key");
				receiveNewKey(2);
			}
		});
		downKeyField.setFocusable(false);
		downKeyField.setHorizontalAlignment(SwingConstants.CENTER);
		downKeyField.setText(KeyEvent.getKeyText(config.getGameKeyCodes()[1]));

		final JButton rightKeyField = new JButton();
		rightKeyField.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				rightKeyField.setText(KeyEvent.getKeyText(config.getGameKeyCodes()[1]));
			}
		});
		rightKeyField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rightKeyField.setText("Press the new key");
				receiveNewKey(1);
			}
		});
		rightKeyField.setFocusable(false);
		rightKeyField.setHorizontalAlignment(SwingConstants.CENTER);
		rightKeyField.setText(KeyEvent.getKeyText(config.getGameKeyCodes()[2]));

		final JButton leftKeyField = new JButton();
		leftKeyField.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				leftKeyField.setText(KeyEvent.getKeyText(config.getGameKeyCodes()[3]));
			}
		});
		leftKeyField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				leftKeyField.setText("Press the new key");
				receiveNewKey(3);
			}
		});
		leftKeyField.setFocusable(false);
		leftKeyField.setHorizontalAlignment(SwingConstants.CENTER);
		leftKeyField.setText(KeyEvent.getKeyText(config.getGameKeyCodes()[3]));
		
		final JButton eagleKeyField = new JButton();
		eagleKeyField.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				eagleKeyField.setText(KeyEvent.getKeyText(config.getGameKeyCodes()[4]));
			}
		});
		eagleKeyField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				eagleKeyField.setText("Press the new key");
				receiveNewKey(4);
			}
		});
		eagleKeyField.setFocusable(false);
		eagleKeyField.setHorizontalAlignment(SwingConstants.CENTER);
		eagleKeyField.setText(KeyEvent.getKeyText(config.getGameKeyCodes()[4]));

		layer2.add(upKey);
		layer2.add(upKeyField);
		
		layer2.add(downKey);
		layer2.add(downKeyField);
		
		layer2.add(rightKey);
		layer2.add(rightKeyField);
		
		layer2.add(leftKey);
		layer2.add(leftKeyField);
		
		layer2.add(eagleKey);
		layer2.add(eagleKeyField);
		
		/* _______________________________________________________________________________________
		 * 
		 * 										END OF LAYERS
		 * _______________________________________________________________________________________
		 */
		
		JMenu mnChanges = new JMenu("Changes");
		mnChanges.setFocusable(false);
		menuBar.add(mnChanges);

		JMenuItem btnConfirm = new JMenuItem("Save");
		btnConfirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				mazeSizeLabel.setEnabled(true);
				mazeSize.setEnabled(true);
				dragonPercLabel.setEnabled(true);
				dragonPerc.setEnabled(true);
				
				config.setDifficulty(difficulty.getSelectedIndex());
				config.setDragonPerc(dragonPerc.getValue()/100.0);
				config.setMazeSize(mazeSize.getValue());
				
				keyCode = 1;
			}
		});
		mnChanges.add(btnConfirm);
		btnConfirm.setFocusable(false);
		btnConfirm.setFont(new Font("Sakkal Majalla", Font.BOLD, 24));

		JMenuItem btnCancel = new JMenuItem("Discard");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				mazeSizeLabel.setEnabled(true);
				mazeSize.setEnabled(true);
				dragonPercLabel.setEnabled(true);
				dragonPerc.setEnabled(true);
				
				keyCode = 2;
			}
		});
		mnChanges.add(btnCancel);

		btnCancel.setFocusable(false);
		btnCancel.setFont(new Font("Sakkal Majalla", Font.BOLD, 24));
		
		/*
		 * ____________________________________________________________________
		 */
		
		final JRadioButton rdbtnMaze = new JRadioButton("Maze");
		final JRadioButton rdbtnKeys = new JRadioButton("Keys");
		
		rdbtnMaze.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				rdbtnMaze.setSelected(true);
				rdbtnKeys.setSelected(false);
				
				layer1.setVisible(true);
				layer2.setVisible(false);
			}
		});
		
		rdbtnMaze.setFocusable(false);
		rdbtnMaze.setSelected(true);
		menuBar.add(rdbtnMaze);
		
		rdbtnKeys.setRequestFocusEnabled(false);
		rdbtnKeys.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				rdbtnMaze.setSelected(false);
				rdbtnKeys.setSelected(true);
				
				layer1.setVisible(false);
				layer2.setVisible(true);
			}
		});
		rdbtnKeys.setFocusable(false);
		menuBar.add(rdbtnKeys);
		
		/*
		 * ____________________________________________________________________
		 */

		this.setFocusable(true);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);

	}
	
	protected boolean chooseMaze() {
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(".maze files", new String[] {"maze"});
		fileChooser.setFileFilter(filter);
		fileChooser.setCurrentDirectory(new File( "." ));
		
		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			
			setMazeFile(fileChooser.getSelectedFile().getName());
			return true;
		}
		
		return false;
	}

	/**
	 * Receives a new keyCode to assign to a game control key.
	 *   This is done with a modal JDialog containing KeyListener. 
	 * 
	 * @param index : index of the game key to assign (see the game key array)
	 */
	public void receiveNewKey(final int index) {
		final JDialog keyDialog = new JDialog(this,"New key",Dialog.ModalityType.APPLICATION_MODAL);
		
		keyDialog.setFocusable(true);
		keyDialog.setResizable(false);
		keyDialog.getContentPane().setLayout(new GridLayout(2, 1, 0, 0));
		keyDialog.getContentPane().add(new JLabel("  Insert the new key"));
		keyDialog.getContentPane().add(new JLabel("  [Esc] to cancel"));
		keyDialog.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				
				if(e.getKeyCode() != KeyEvent.VK_ESCAPE) {
					if(keyNotExistant(e.getKeyCode())) {
						config.setGameKey(index, e.getKeyCode());
					}
				}
				
				keyDialog.setVisible(false);
			}

			@Override
			public void keyReleased(KeyEvent arg0) {}

			@Override
			public void keyTyped(KeyEvent arg0) {}
			
		});
		
		keyDialog.pack();
		keyDialog.setLocationRelativeTo(null);
		keyDialog.setVisible(true);
	}
	
	/**
	 * Checks if a certain keyCode is already assigned or not to a game key.
	 * 
	 * @param keyCode : value to compare
	 * @return true if non-existant
	 */
	public boolean keyNotExistant(int keyCode) {
		for(int i = 0; i < config.getGameKeyCodes().length ; i++) {
			if(keyCode == config.getGameKeyCodes()[i]) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Gets the value of the parameter [config].
	 * 
	 * @return the GameConfig instance associated with this window.
	 */
	public GameConfig getConfig() {
		return this.config;
	}

	/**
	 * Sets the value of the parameter [config].
	 * 
	 * @param config : configuration to set
	 */
	public void setConfig(GameConfig config) {
		this.config = config;
	}

	/**
	 * @return the mazeFile
	 */
	public String getMazeFile() {
		return mazeFile;
	}

	/**
	 * @param mazeFile the mazeFile to set
	 */
	public void setMazeFile(String mazeFile) {
		this.mazeFile = mazeFile;
	}

	/**
	 * Enables/disables the "Load Maze"/"Unload maze" buttons.
	 * 
	 * @param disable : true if to disable the buttons
	 */
	public void setLoadDisabled(boolean disable) {
		if(disable) {
			this.btnLoadMaze.setEnabled(false);
			this.btnUnloadMaze.setEnabled(false);
		}
		else {
			this.btnLoadMaze.setEnabled(true);
			this.btnUnloadMaze.setEnabled(true);
		}
	}
	
}
