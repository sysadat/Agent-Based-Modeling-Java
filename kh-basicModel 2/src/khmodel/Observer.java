package khmodel;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.util.Bag;

public class Observer implements Steppable {
	Stoppable event;
	double sX = 0;
	double sY = 0;
	double sXY = 0;
	double sX2 = 0;
	double sY2 = 0;
	double n = 0;
	
	/**
	 * Collects all the data for calculating correlations in attractiveness among partners.
	 * @param f
	 * @param m
	 */
	public void getData(Agent f, Agent m) {
		final double fx = f.attractivenes;
		final double mx = m.attractivenes;
		sX += fx;
		sY += mx;
		sXY += fx*mx;
		sX2 += fx*fx;
		sY2 += mx*mx;
		n++;
	}
	/**
	 * Computational correlation formula
	 * @return
	 */
	public double correlation() {
		double num = sXY - (sX*sY)/n;
		double div = Math.sqrt(sX2 - (sX*sX)/n)*Math.sqrt(sY2 - (sY*sY)/n);
		return num/div;
	}
	
	/**
	 * In order to avoid multiple dates on a single step.  We use a boolean to indicate dated on each step.  After a
	 * step, the oberver sets all of the remaining agents date flag to false for the next round.
	 * @param state
	 */
	public void setDated(Environment state) {
		Bag agents = state.space.allObjects;
		for(int i=0;i<agents.numObjs;i++) {
			Agent a = (Agent)agents.objs[i];
			a.dated = false;
		}
	}
	
	/**
	 * We need some way to get data out of this simulation and the easiest way is to print it to the console.  There are also
	 * graphing facilities available in MASON.
	 * @param state
	 */
	public void printData(Environment state) {
		double correlation = correlation();
		double maleA = sY/n;
		double femaleA = sX/n;
		long step = state.schedule.getSteps();
		System.out.println(step + ":   "+ n +"     "+ correlation + "     "+ maleA +"     "+femaleA);
	}
	
	/**
	 * For the first time step, print out the column headers.  From then on, print out the data.  Stop when there
	 * are no more agents left.  Update the dated boolean for each agent.
	 */
	public void step(SimState state) {
		if(state.schedule.getSteps() <=1) {
			System.out.println("steps:  pairs       correlation       maleA        femaleA");
		}
		printData((Environment)state);
		if(((Environment)state).space.getAllObjects().numObjs==0) {
			this.event.stop();
			return;
		}
		setDated((Environment)state);
	}

}
