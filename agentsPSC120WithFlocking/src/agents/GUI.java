package agents;

import java.awt.Color;

import sim.engine.SimState;
import sweep.GUISimState;

public class GUI extends GUISimState {

	
	/**
	 * Constructor method for GUI
	 * @param state  -- SimState and any of its subclasses
	 * @param spaceName  --String name of the space variable declared
	 * @param gridWidth  -- Width of the display window
	 * @param gridHeight -- Height of the display window
	 * @param backdrop -- Background color of the display window
	 * @param agentDefaultColor -- Default color of agents
	 * @param agentPortrayal -- if true, the default color is used for all agents.
	 */
	public GUI(SimState state, String spaceName, int gridWidth, int gridHeight, Color backdrop,
			Color agentDefaultColor, boolean agentPortrayal) {
		super(state, spaceName, gridWidth, gridHeight, backdrop, agentDefaultColor, agentPortrayal);
		// TODO Auto-generated constructor stub
	}
	/**
	 * Main method calls the initialization method.
	 * @param args
	 */
	public static void main(String[] args) {
		GUI.initialize(Environment.class, GUI.class, 400, 400, Color.WHITE, Color.RED, true,"space");

	}

}
