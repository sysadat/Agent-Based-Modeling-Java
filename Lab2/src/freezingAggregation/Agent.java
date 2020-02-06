package freezingAggregation;

import freezingAggregation.Agent;
import freezingAggregation.Environment;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Bag;

public class Agent implements Steppable {
	int x;
	int y;
	int xdir;
	int ydir;
	boolean isfrozen;
	
	public Agent(int x, int y, int xdir, int ydir, boolean isfrozen) {
		super();
		this.x = x;
		this.y = y;
		this.xdir = xdir;
		this.ydir = ydir;
		this.isfrozen = isfrozen;
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
			}else {
				Agent temp = (Agent)b.objs[0];
				if(temp.isfrozen) {
					this.isfrozen = true;
					this.xdir = 0;
					this.ydir = 0;
					
				}
			}
		}
		else {
			x = state.space.stx(x + xdir);
			y = state.space.sty(y + ydir);
			state.space.setObjectLocation(this, x, y);
		}
	}
	

	public void move(Environment state) {
		if(state.random.nextBoolean(state.active) && !this.isfrozen) {
			xdir = state.random.nextInt(3)-1;
			ydir = state.random.nextInt(3)-1;
			
		if(state.isBounded) {
			if(this.x - xdir < 0) {
				xdir = -1;
			}
			if(this.x + xdir > state.gridWidth - 1) {
				xdir = 1;
			}
			if(this.y + ydir < 0) {
				ydir = -1;
			}
			if(this.y + ydir > state.gridHeight - 1) {
				ydir = 1;
			}
			}
		}
		placeAgent(state);
	}
	
	@Override
	public void step(SimState state) {
		move((Environment)state);
		
	}

}
