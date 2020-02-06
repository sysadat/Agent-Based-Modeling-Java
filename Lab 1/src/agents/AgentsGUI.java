package agents;

import java.awt.Color;

import sim.engine.SimState;
import sweep.GUISimState;

public class AgentsGUI extends GUISimState {

	

	public AgentsGUI(SimState state, String spaceName, int gridWidth, int gridHeight, Color backdrop,
			Color agentDefaultColor, boolean agentPortrayal) {
		super(state, spaceName, gridWidth, gridHeight, backdrop, agentDefaultColor, agentPortrayal);
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		AgentsGUI.initialize(Environment.class, AgentsGUI.class, 400, 400, Color.WHITE, Color.RED, true,"space");

	}

}
