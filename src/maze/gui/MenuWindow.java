package maze.gui;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.border.EtchedBorder;

/**
 * Class that represents all "menu window" type objects.
 * 
 * A menu window provides a main menu with the following options:
 *   - play (a new game);
 *   - editor (for the creation of custom mazes);
 *   - credits;
 *   - quit (exit the application).
 *   
 * @see Window
 */
@SuppressWarnings("serial")
public class MenuWindow extends Window {

	private JPanel jpanel;

	/**
	 * Default Constructor.
	 */
	public MenuWindow() {
		initialize();
	}
	
	/**
	 * Constructor.
	 *   Receives tihe title of the window.
	 *   
	 * @param title : title of the window
	 */
	public MenuWindow(String title) {

		super(title);
		
		initialize();
	}
	
	/**
	 * Initializes the entire game window.
	 *   Adds the labels, buttons, sliders, etc...
	 */
	private void initialize() {
		setResizable(false);
		
		setBounds(100, 100, 450, 300);
		
		jpanel = new JPanel();
		jpanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		
		setContentPane(jpanel);
		
		JButton btnPlay = new JButton("Play");
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				keyCode = 1;
			}
		});
		
		JButton btnOptions = new JButton("Editor");
		btnOptions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				keyCode = 2;
			}
		});
		
		JButton btnCredits = new JButton("Credits");
		btnCredits.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				keyCode = 3;
				
			}
		});
		
		JButton btnQuit = new JButton("Quit");
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				keyCode = 4;
			}
		});
		
		GroupLayout gl_contentPane = new GroupLayout(jpanel);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(btnPlay)
						.addComponent(btnOptions)
						.addComponent(btnCredits)
						.addComponent(btnQuit))
					.addContainerGap(335, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnPlay)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnOptions)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnCredits)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnQuit)
					.addContainerGap(91, Short.MAX_VALUE))
		);
		jpanel.setLayout(gl_contentPane);
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		
	}
}
