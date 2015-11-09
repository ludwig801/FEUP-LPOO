package maze.logic;

import java.io.Serializable;

/**
 * Class that represents all "task" type objects.
 * 
 * A task is basically an objective to be fulfilled by the player.
 *   Therefore it contains two atributes:
 *     - description: what is to be done;
 *     - done: whether it has been satisfied or not.
 *   
 * This class implements serializable, has it is used upon game saving.
 */
public class Task implements Serializable {
	
	private static final long serialVersionUID = 1;
	
	private String description;
	private boolean done;

	/**
	 * Default Constructor.
	 */
	public Task() {}
	
	/**
	 * Constructor for the Task object.
	 * A task is an objective to be fulfilled by the player.
	 * 
	 * @param description
	 */
	public Task(String description) {
		this.setDescription(description);
		this.setDone(false);
	}

	/**
	 * Returns the Task description.
	 * What is to be done.
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the Task description.
	 * 
	 * @param description : string value to be set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the value of this task (whether it is performed or not).
	 * 
	 * @return true if done, false otherwise
	 */
	public boolean isDone() {
		return done;
	}

	/**
	 * Sets the value of the parameter [done]
	 * 
	 * @param done : value to be set
	 */
	public void setDone(boolean done) {
		this.done = done;
	}
	
}
