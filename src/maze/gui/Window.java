package maze.gui;

import javax.swing.JFrame;

/**
 * Class that represents all "window" type objects.
 * 
 * A generic window class, that extends JFrame.
 *  Every child object as a keyCode variable, that it inherits from
 *  this class. Important for the input handlers.
 */
@SuppressWarnings("serial")
public class Window extends JFrame {
	
	protected int keyCode;
	
	/**
	 * Default Constructor.
	 */
	public Window() {}

	/**
	 * Constructor.
	 *   Receives the title of the window.
	 *   
	 * @param title : title of the window
	 */
	public Window(String title) {
		
		this.keyCode = 0;
		this.setTitle(title);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * Gets the value of the parameter [keyCode].
	 * 
	 * @return the value of keyCode
	 */
	public int getKeyCode() {
		return keyCode;
	}

	/**
	 * Sets the value of the parameter [keyCode].
	 * 
	 * @param keyCode : value to set
	 */
	public void setKeyCode(int keyCode) {
		this.keyCode = keyCode;
	}

	/**
	 * Sets the value of the parameter [keyCode] to 0.
	 */
	public void resetKeyCode() {
		this.keyCode = 0;
	}

}
