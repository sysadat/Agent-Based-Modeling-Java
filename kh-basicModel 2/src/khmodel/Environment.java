package khmodel;

import sim.engine.SimState;
import sim.field.grid.SparseGrid2D;
import sweep.GUISimState;

public class Environment extends SimState {
	public SparseGrid2D space;
	int gridWidth = 100;
	int gridHeight = 100;
	int males = 1000;
	int females = 1000;
	double maxAttractiveness = 10;
	double n =3;
	double maxDates = 50;
	boolean best = true;
	Observer observer;
	double active = .1;
	boolean locationEmpty = true;
	double pa;
	public boolean isAggregate() {
		return aggregate;
	}

	public void setAggregate(boolean aggregate) {
		this.aggregate = aggregate;
	}

	public boolean isReplacement() {
		return replacement;
	}

	public void setReplacement(boolean replacement) {
		this.replacement = replacement;
	}
	boolean aggregate;
	boolean replacement;
	boolean globalLocalSearch;
	boolean findLocalDate;
	int searchRadius;
	int dateSearchRadius;
	GUISimState gui = null;
	boolean replicate = false;


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

	public int getMales() {
		return males;
	}

	public void setMales(int males) {
		this.males = males;
	}

	public int getFemales() {
		return females;
	}

	public void setFemales(int females) {
		this.females = females;
	}

	public double getMaxAttractiveness() {
		return maxAttractiveness;
	}

	public void setMaxAttractiveness(double maxAttractiveness) {
		this.maxAttractiveness = maxAttractiveness;
	}

	public double getN() {
		return n;
	}

	public void setN(double n) {
		this.n = n;
	}

	public double getMaxDates() {
		return maxDates;
	}

	public void setMaxDates(double maxDates) {
		this.maxDates = maxDates;
	}

	public boolean isBest() {
		return best;
	}

	public void setBest(boolean best) {
		this.best = best;
	}

	public boolean isReplicate() {
		return replicate;
	}

	public void setReplicate(boolean replicate) {
		this.replicate = replicate;
	}

	public Environment(long seed) {
		super(seed);
	}
	
	/**
	 * Creates the male and female agents. Assigns an attractiveness value to each and then randomly places each agent in space.
	 * Agents are then scheduled and a handle is retained for each agent so that it can be removed from the schedule.  Finally,
	 * a colored portrayal is assigned to each agent to represent gender and attractiveness
	 */
	public void makeAgents() {
		for(int i=0;i<males;i++) {
			int x = random.nextInt(gridWidth);
			int y = random.nextInt(gridHeight);
			double attractiveness = random.nextInt((int)maxAttractiveness)+1;
			Agent a = new Agent(false,attractiveness);
			space.setObjectLocation(a, x, y);
			a.event = schedule.scheduleRepeating(a);
			gui.setOvalPortrayal2DColor(a, (float)0, (float)0, (float)1, (float)(attractiveness/maxAttractiveness));
		}
		for(int i=0;i<females;i++) {
			int x = random.nextInt(gridWidth);
			int y = random.nextInt(gridHeight);
			double attractiveness = random.nextInt((int)maxAttractiveness)+1;
			Agent a = new Agent(true,attractiveness);
			space.setObjectLocation(a, x, y);
			a.event = schedule.scheduleRepeating(a);
			gui.setOvalPortrayal2DColor(a, (float)1, (float)0, (float)0, (float)(attractiveness/maxAttractiveness));
		}
	}
	/**
	 * Start method first calls super.start to use the start methods in SimState.  Next we create the space, agents, Observer and
	 * schedule the observer to repeat and to be at a higher level than the other agents and so is called last in each step.
	 */
	public void start() {
		super.start();
		space = new SparseGrid2D(gridWidth,gridHeight);
		makeAgents();
		observer = new Observer();
		observer.event = schedule.scheduleRepeating(0, 2, observer);
	}
}
