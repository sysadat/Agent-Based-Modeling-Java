package khmodel;

import java.awt.Color;

import sim.engine.SimState;
import sweep.GUISimState;

public class GUI extends GUISimState {
	
	/**
	 * Constructor method for the GUI and SimState subclasses
	 * @param state  -- takes a SimState subclass
	 * @param spaceName -- name of the space variable declared in SimSTate subclass entered as a String type
	 * @param gridWidth -- width of the display window
	 * @param gridHeight -- height of the display window
	 * @param backdrop  -- color of the display window
	 * @param agentDefaultColor -- agent default color is not portrayed in the simulation
	 * @param agentPortrayal -- true if you want the default only
	 */
	public GUI(SimState state, String spaceName, int gridWidth, int gridHeight, Color backdrop, Color agentDefaultColor,
			boolean agentPortrayal) {
		super(state,spaceName, gridWidth, gridHeight, backdrop, agentDefaultColor, agentPortrayal);
	}
	/**
	 * Main method in which the environment and GUI are created using the initialize method in GUI.  Then we set the environment
	 * variable gui to its gui so that we can change the color of agents anytime we want.
	 * @param args
	 */
	public static void main(String[] args) {
		Environment  s = (Environment)GUI.initialize(Environment.class, GUI.class, 400, 400, Color.WHITE, Color.gray, false,"space");
		s.gui = (GUI)gui;
	}

}