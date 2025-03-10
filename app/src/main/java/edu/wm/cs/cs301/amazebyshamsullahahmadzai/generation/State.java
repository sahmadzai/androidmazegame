package edu.wm.cs.cs301.amazebyshamsullahahmadzai.generation;

/**
 * The state interface is used for the controller 
 * such that all states of the UI conform and can 
 * be treated the same way.
 * 
 * @author Peter Kemper
 *
 */
public interface State {	
	/**
	 * Starts the main operation to be performed in this state.
	 * Semantics depends on the particular state implementing it.
	 * This is polymorphism in action.
	 *
	 * @param panel is the panel to draw graphics on
	 */
    void start(MazePanel panel);
    /**
     * Handles input the user has given with the keyboard.
     * Control uses this method to pass user input 
     * to a State to handle and respond to it.
     * @param userInput input from Enum set of possible, legal commands
     * @param value carries a value, typically the skill level, optional
     * @return true if input can be handled, false otherwise
     */
    boolean handleUserInput(Constants.UserInput userInput, int value);

}
