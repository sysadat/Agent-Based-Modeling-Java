package khmodel;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.util.Bag;

public class Agent implements Steppable {
	int x;
	int y;
	int xdir;
	int ydir;
	boolean female;
	boolean male;
	double attractivenes;
	double dates = 0;
	boolean dated = false;
	Stoppable event;
	boolean findLocalDate;
	int dateSearchRadius;
	
	public void move(Environment state) {
		if(state.random.nextBoolean(state.active)) {
			xdir = state.random.nextInt(3)-1;
			ydir = state.random.nextInt(3)-1;
			placeAgent( state);
		}
	}
	
    public void replicate (Environment state, boolean female){
        //Your code here
    	if(female == false) {
    		int x = state.random.nextInt(state.gridWidth);
			int y = state.random.nextInt(state.gridHeight);
			double attractiveness = state.random.nextInt((int)state.maxAttractiveness)+1;
			Agent a = new Agent(false,attractiveness);
			state.space.setObjectLocation(a, x, y);
			a.event = state.schedule.scheduleRepeating(a);
			state.gui.setOvalPortrayal2DColor(a, (float)1, (float)0, (float)0, (float)(attractiveness/state.maxAttractiveness));
    	}
    	if(female == true) {
    		int x = state.random.nextInt(state.gridWidth);
			int y = state.random.nextInt(state.gridHeight);
			double attractiveness = state.random.nextInt((int)state.maxAttractiveness)+1;
			Agent a = new Agent(true,attractiveness);
			state.space.setObjectLocation(a, x, y);
			a.event = state.schedule.scheduleRepeating(a);
			state.gui.setOvalPortrayal2DColor(a, (float)0, (float)0, (float)1, (float)(attractiveness/state.maxAttractiveness));
    	}
			
		
     }
	
	public int decidex(Environment state, Bag neighbors){
		int posx = 0, negx = 0;
		for(int i=0; i < neighbors.numObjs;i++){
			Agent a = (Agent) neighbors.objs[i];
			if(a.x > x)
				posx++;
			if(a.x < x) negx++;
		}
		if(posx > negx){
			return 1;
		}

		if(negx > posx){
			return -1;
		}
		return state.random.nextInt(3)-1; //move randomly

	}
	
	
	public int decidey(Environment state, Bag neighbors){
		int posy = 0, negy = 0;
		for(int i=0; i < neighbors.numObjs;i++){
			Agent a = (Agent) neighbors.objs[i];
			if(a.y > y)
				posy++;
			if(a.y < y)
				negy++;
		}

		if(posy > negy){
			return 1;
		}

		if(negy > posy){
			return -1;
		}
		return state.random.nextInt(3)-1;  //move randomly
	}

	public void placeAgent(Environment state) {
		if(state.locationEmpty) {
			int tempx = state.space.stx(x + xdir);
			int tempy = state.space.sty(y + ydir);
			Bag b = state.space.getObjectsAtLocation(tempx, tempy);
			if(b == null || b.numObjs == 0){
				x = tempx;
				y = tempy;
				state.space.setObjectLocation(this, x, y);
			}
		}
		else {
			x = state.space.stx(x + xdir);
			y = state.space.sty(y + ydir);
			state.space.setObjectLocation(this, x, y);
		}
	}
	
	public boolean aggregate(Environment state) {
		if(state.aggregate && state.random.nextBoolean(state.pa)) {
			Bag neighbors = state.space.getMooreNeighbors(x, y, state.searchRadius, state.space.TOROIDAL, false);
			xdir = decidex(state, neighbors);
			ydir = decidey(state, neighbors);
			placeAgent( state);
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Rule that calculates attractiveness of an agent based on the choose the best rule.
	 * 
	 * @param state
	 * @param a
	 * @return
	 */
	public double p1(Environment state, Agent a) {
		return Math.pow(a.attractivenes, state.n)/Math.pow(state.maxAttractiveness, state.n);
	}
	 /**
	  * Rule that calculates attractiveness of an agent based on the choose the most similar rule.
	  * @param state
	  * @param a
	  * @return
	  */
	public double p2(Environment state, Agent a) {
		return Math.pow(state.maxAttractiveness-Math.abs(this.attractivenes-a.attractivenes), state.n)/
				Math.pow(state.maxAttractiveness, state.n);
	}
	/**
	 * Closing time rule that weight the probability of choosing a partner as a function of attractiveness and
	 * how many dates an agent has had.
	 * 
	 * @param state
	 * @param probability
	 * @return
	 */
	public double ct(Environment state, double probability) {
		return Math.pow(probability, (state.maxDates - dates)/state.maxDates);
	}
	
	/**
	 * Allows an agent to find a random date from the whole population of agents.
	 * @param state
	 * @return
	 */
	public Agent findDate (Environment state) {
		Bag agents = state.space.allObjects;
		if(agents.numObjs == 0) return null; //no agents
		int r = state.random.nextInt(agents.numObjs);
		for(int i =r;i<agents.numObjs;i++) {
			Agent a = (Agent)agents.objs[i];
			if(!a.dated && female != a.female) {
				return a;
			}
		}
		for(int i =0; i<r;i++) {
			Agent a = (Agent)agents.objs[i];
			if(!a.dated && female != a.female) {
				return a;
		}
		}
		return null;
	}
	/**
	 * First calculates the attractiveness probabilities based on either choose the best rule or 
	 * choose the most similar rule for both agents.  Next is calculates the closing time probabilities for each
	 * agent.  Next, both closing time probabilities most evaluate to true when placed in random.nextBoolean(p).
	 * If the conditional evaluates to true, the observer records the data for males and females and then they
	 * remove themselves from the simulation.  Otherwise they are mark as having dated (so that they don't date
	 * more than once on each step).  dates are updated if less than maxDates for both agents. 
	 * @param state
	 * @param other
	 */
	public void date(Environment state, Agent other) {
		double p;
		double q;
		if(state.best) {
			p = p1(state,other);
			q = other.p1(state, this);
		}
		else {//similar
			p = p2(state,other);
			q = other.p2(state, this);
		}
		p = ct(state,p);
		q = other.ct(state, q);
		if(state.random.nextBoolean(p) && state.random.nextBoolean(q)) {
			if(this.female == true) {
				state.observer.getData(this, other);
			}else {
				state.observer.getData(other,this);
			}
			this.remove(state);
			other.remove(state);
			if(state.replicate == true) {
				replicate(state, true);
				replicate(state,false);
			}
		}
		else {
			dated = true;
			other.dated = true;
			if(dates < state.maxDates) {
				dates++;
			}
			if(other.dates < state.maxDates) {
				other.dates++;
			}
		}

	}
	/**
	 * Method allows agents to remove themselves from the simulation by first removing themselves from the schedule
	 * and then from the space.  Garbage collection does the rest.
	 * @param state
	 */
	public void remove(Environment state) {
		event.stop();
		state.space.remove(this);
	}
	
	/**
	 * If an agent has alread dated on this round, nothing left to do, so return.  Otherwise, find a random partner. If
	 * on cannot be found, return.  Otherwise date.
	 */
	public void step(SimState state) {
		
		if(dated) return;
		Agent a = findDate((Environment)state);
		if(a == null) return;
		date((Environment)state,a);
	}
	/**
	 * Constructor method for agent.  Assign attractiveness and gender.
	 * @param female
	 * @param attractivenes
	 */
	public Agent(boolean female, double attractivenes) {
		super();
		this.attractivenes = attractivenes;
		this.female = female;
	}

}
