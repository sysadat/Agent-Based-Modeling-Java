package agents;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Bag;

public class Agent implements Steppable {
	int x; //location on the x-axis
	int y; //location on the y-axis
	int xdir; //x direction of change
	int ydir; //y direction of change

	public Agent(int x, int y, int xdir, int ydir) {
		super();
		this.x = x;
		this.y = y;
		this.xdir = xdir;
		this.ydir = ydir;
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

	public void aggregate(Environment state) {
		if(state.random.nextBoolean(state.pa)) {
			Bag neighbors = state.space.getMooreNeighbors(x, y, state.searchRadius, state.space.TOROIDAL, false);
			xdir = decidex(state, neighbors);
			ydir = decidey(state, neighbors);
			placeAgent( state);
		}
		else {
			move(state);
		}
	}

	public void move(Environment state) {
		if(state.random.nextBoolean(state.active)) {
			xdir = state.random.nextInt(3)-1;
			ydir = state.random.nextInt(3)-1;
		}
		placeAgent( state);
	}

	@Override
	public void step(SimState state) {
		if(((Environment)state).aggregate)
			aggregate((Environment)state);
		else
			move((Environment)state);
	}

}
