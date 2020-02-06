package agents;

import sim.engine.SimState;
import sim.field.grid.SparseGrid2D;

public class Environment extends SimState {
	public SparseGrid2D space;
	public int gridWidth = 50; //x dimension of space
	public int gridHeight = 50; //y dimension of space
	public int n = 50; //The number of agents
	public double active = 0.1; //probability of activity
	public boolean aggregate = false; //Whether agents aggregate or not
	public int searchRadius = 2; //the radius of search (assuming a square area)
	public boolean locationEmpty = false; //Must a location be empty before an agent can move into it?
	public double pa = 1; //probablistic aggregation
	public boolean flock = false; //Whether agents attempt to flock
	public double pf = 1; //probability of flocking on each step

	public Environment(long seed) {
		super(seed);
		// TODO Auto-generated constructor stub
	}
	
	public int getGridWidth() {
		return gridWidth;
	}

	public void setGridWidth(int gridWidth) {
		this.gridWidth = gridWidth;
	}

	public int getGridHeight() {
		return gridHeight;
	}

	public void setGridHeight(int gridHeight) {
		this.gridHeight = gridHeight;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	public double getActive() {
		return active;
	}

	public void setActive(double active) {
		this.active = active;
	}

	public boolean isAggregate() {
		return aggregate;
	}

	public void setAggregate(boolean aggregate) {
		this.aggregate = aggregate;
	}

	public int getSearchRadius() {
		return searchRadius;
	}

	public void setSearchRadius(int searchRadius) {
		this.searchRadius = searchRadius;
	}

	public boolean isLocationEmpty() {
		return locationEmpty;
	}

	public void setLocationEmpty(boolean locationEmpty) {
		this.locationEmpty = locationEmpty;
	}

	public double getPa() {
		return pa;
	}

	public void setPa(double pa) {
		this.pa = pa;
	}

	public double getPf() {
		return pf;
	}

	public void setPf(double pf) {
		this.pf = pf;
	}

	public boolean isFlock() {
		return flock;
	}

	public void setFlock(boolean flock) {
		this.flock = flock;
	}
	/**
	 * Makes all the agents, places them randomly into space, and schedules them.
	 */
	public void makeAgents() {
		for(int i=0;i<n;i++) {
			int x = random.nextInt(gridWidth);
			int y = random.nextInt(gridHeight);
			int xdir = random.nextInt(3)-1;
			int ydir = random.nextInt(3)-1;
			Agent a = new Agent(x,y,xdir,ydir);
			schedule.scheduleRepeating(a);
			space.setObjectLocation(a, x, y);
		}
	}
	/**
	 * Start method calls super.start(), creates a SparseGrid2D space and makes the agents.
	 */
	public void start() {
		super.start();
		space = new SparseGrid2D(gridWidth, gridHeight); //creates a sparse grid 2D space
		makeAgents();
	}

}
