package maze.gui;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Class that represents all "input handler" type objects.
 * 
 * An input handler reads the keyCode variable from a "window" type object (and/or child objects)
 *   and saves that keyCode into an Array, reseting the "window" keyCode afterwards.
 *   Any kind of instance of this type is running in a separated thread, therefore it
 *   doens't "freeze" the main program while it waits for keyCode values.
 */
public class InputHandler implements Runnable {
	
	protected Window window;
	protected boolean terminate;

	protected CopyOnWriteArrayList<Integer> commands;
	
	/**
	 * Defaut Constructor.
	 */
	public InputHandler() {
	}
	
	/**
	 * Constructor.
	 *   Assigns a window to this handler.
	 * 
	 * @param w : window to be linked to this handler
	 */
	public InputHandler(Window w) {
		this.window = w;
		this.terminate = false;
		this.commands = new CopyOnWriteArrayList<Integer>();
	}

	/**
	 * Waits for a keyCode from the window's KeyListener to be different from 0.
	 *   When it is, its saved to the array [commands].
	 */
	@Override
	public void run() {

		while(!terminate) {

			try {
				if(window.getKeyCode() != 0) {
					commands.add(window.getKeyCode());
					window.resetKeyCode();
				}
				Thread.sleep(6);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

	}
	
	/** 
	 * Gets the first element of the array commands.
	 * 
	 * @return First element of the queue. If the queue is empty, returns -1.
	 */
	public Integer getNextCommand() {
		if(!commands.isEmpty()) {
			int i = commands.get(0);
			return i;
		} else {
			return -1;
		}
	}
	
	/**
	 * Removes the first command of the queue.
	 */
	public void removeCommand() {
		if(!commands.isEmpty()) {
			commands.remove(0);
		}
	}
	
	/**
	 * Adds the given value to the array [commands].
	 * 
	 * @param keyCode : value to set
	 */
	public void addCommand(int keyCode) {
		commands.add(keyCode);
	}

	/**
	 * Sets the value of the parameter [terminate].
	 * 
	 * @param terminate : value to set
	 */
	public void setTerminate(boolean terminate) {
		this.terminate = terminate;
	}


}
