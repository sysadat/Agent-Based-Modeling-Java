package agents;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Bag;

public class Agent implements Steppable {
	int x; //location on the x-axis
	int y; //location on the y-axis
	int xdir; //x direction of change
	int ydir; //y direction of change

	/**
	 * Constructor method for Agent class.
	 * @param x
	 * @param y
	 * @param xdir
	 * @param ydir
	 */
	public Agent(int x, int y, int xdir, int ydir) {
		super();
		this.x = x;
		this.y = y;
		this.xdir = xdir;
		this.ydir = ydir;
	}
	/**
	 * Majority rule method for aggregation on the x axis.  Returns -1 or 1 depending of the relative location (to the
	 * agent itself) of other agents.  If there is not majority in either direction, -1, 0, or 1 are returned 
	 * randomly.
	 * @param state
	 * @param neighbors
	 * @return
	 */
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

	/**
	 * Majority rule method for aggregation on the y axis.  Returns -1 or 1 depending of the relative location (to the
	 * agent itself) of other agents.  If there is not majority in either direction, -1, 0, or 1 are returned 
	 * randomly.
	 * @param state
	 * @param neighbors
	 * @return
	 */
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

	/**
	 * Majority rule method used for flocking.  Based on its neighbors, orientations of -1, 0, 1 on the x axis.
	 * @param state
	 * @param neighbors
	 * @return
	 */
	public int decideDirx(Environment state, Bag neighbors) {
		int a=0,b=0,c=0;
		for(int i=0;i<neighbors.numObjs;i++) {
			Agent x = (Agent)neighbors.objs[i];
			final int dx = x.xdir;
			if(dx == -1) a++;
			else if(dx== 0)b++;
			else c++;
		}
		if(a > b && a > c) {
			return -1; //a
		}
		else if(b > a && b > c) {
			return 0;//b
		}
		else if (c > a && c > b) {
			return 1; //c
		}// the first three conditions imply that there are at least two equal
		else if (a==b) {
			if(a > c) {
				if(state.random.nextBoolean(0.5)) {
					return -1; //a
				}
				else
					return 0;//b
			}
			else {
				return 0;
			}
		}
		else if (a==c) {
			if(a > b) {
				if(state.random.nextBoolean(0.5)) {
					return -1; //a
				}
				else
					return 1;//b
			}
			else {
				return 0;
			}
		}
		else if (b==c) {
			if(b > a) {
				if(state.random.nextBoolean(0.5)) {
					return 0; //b
				}
				else
					return 1;//c
			}
			else {
				return 0;
			}
		}
		else {
			//mathematically, this condition cannot happen and is included because the b==c is explicitly
			//included.  It could be just an else statement, but for clarity, I have included it.
			return 0;
		}
	}

	/**
	 * Majority rule method used for flocking.  Based on its neighbors, orientations of -1, 0, 1 on the y axis.
	 * @param state
	 * @param neighbors
	 * @return
	 */
	public int decideDiry(Environment state, Bag neighbors) {
		int a=0,b=0,c=0;
		for(int i=0;i<neighbors.numObjs;i++) {
			Agent x = (Agent)neighbors.objs[i];
			final int dx = x.ydir;
			if(dx == -1) a++;
			else if(dx== 0)b++;
			else c++;
		}
		if(a > b && a > c) {
			return -1; //a
		}
		else if(b > a && b > c) {
			return 0;//b
		}
		else if (c > a && c > b) {
			return 1; //c
		}
		else if (a==b) {
			if(a > c) {
				if(state.random.nextBoolean(0.5)) {
					return -1; //a
				}
				else
					return 0;//b
			}
			else {
				return 0;
			}
		}
		else if (a==c) {
			if(a > b) {
				if(state.random.nextBoolean(0.5)) {
					return -1; //a
				}
				else
					return 1;//b
			}
			else {
				return 0;
			}
		}
		else if (b==c) {
			if(b > a) {
				if(state.random.nextBoolean(0.5)) {
					return 0; //b
				}
				else
					return 1;//c
			}
			else {
				return 0;
			}
		}
		else {
			return state.random.nextInt(3)-1;
		}
	}

	/**
	 * Places an agent in space if LocationEmpty is true, then an agent will move to the new location if and only if
	 * the new location is empty, otherwise it does not move.  If locationEmpty is false, then the agent always is placed in
	 * the new location.  This method only handles toroidal spaces.
	 * @param state
	 */
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

	/**
	 * Aggregation occurs if aggregation is true and the probability of agregating evaluates to true. The method returns true if
	 * if aggregation occurs else false.
	 * @param state
	 * @return
	 */
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
	 * Flocking occurs if flock is true and the probability of flocking evaluates to true. The method returns true if
	 * if flocking occurs else false.
	 * @param state
	 * @return
	 */
	public boolean flock(Environment state) {
		if(state.flock && state.random.nextBoolean(state.pf)) {
			Bag neighbors = state.space.getMooreNeighbors(x, y, state.searchRadius, state.space.TOROIDAL, true);
			xdir = decideDirx(state, neighbors);
			ydir = decideDiry(state, neighbors);
			placeAgent( state);
			return true;
		}
		else {
			return false;
		}
	}
	/**
	 * Movement is a probabilistic function of activity level.  Movement is probabilistic.
	 * @param state
	 */
	public void move(Environment state) {
		if(state.random.nextBoolean(state.active)) {
			xdir = state.random.nextInt(3)-1;
			ydir = state.random.nextInt(3)-1;
			placeAgent( state);
		}
	}

	/**
	 * Aggregation, flocking, and movement are controlled in this method.  The method aggregate returns either true or
	 * false.  If an agent successfully aggregates, it returns true and the step method ends.  If not, it attempts to
	 * flock, if it successfully flocks, it returns true and the step method ends.  If not, it attempts to move based on
	 * activity level.  Successful aggregation occurs if aggregation is true and the probability of aggregation evaluates 
	 * to true.  Successful flocking occurs if flock is true and the probability of flocking evaluates to true.
	 */
	public void step(SimState state) {
		Environment eState = (Environment)state;
		if(aggregate(eState)) 
			return;//if aggregate returns true, then exit step method
		if(flock(eState))
			return; //if flock returns true, then exit step method
		move(eState);//if the previous steps fail, attempt to move
	}

}
