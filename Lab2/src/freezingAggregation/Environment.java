package freezingAggregation;

import sim.engine.SimState;
import sim.field.grid.SparseGrid2D;

public class Environment extends SimState {
	public SparseGrid2D space;
	public int gridWidth = 50;
	public int gridHeight = 50;
	public int n = 2;
	public double active = 0.1;
	public boolean locationEmpty = true;
	public boolean isBounded = true;
	
	public Environment(long seed) {
		super(seed);
		// TODO Auto-generated constructor stub
	}
	
	public void makeAgents() {
		Agent f = new 	Agent(gridWidth / 2, gridHeight / 2, 0, 0, true);
		schedule.scheduleRepeating(f);
		space.setObjectLocation(f, gridWidth/2, gridHeight / 2);
		for(int i=0;i<n - 1;i++) {
			int x = random.nextInt(gridWidth);
			int y = random.nextInt(gridHeight);
			int xdir = random.nextInt(3)-1;
			int ydir = random.nextInt(3)-1;
			Agent a = new 	Agent(x,y,xdir,ydir, false);
			schedule.scheduleRepeating(a);
			space.setObjectLocation(a, x, y);
		}
		
	}
	
	public void start() {
		super.start();
		space = new SparseGrid2D(gridWidth, gridHeight); //creates a sparse grid 2D space
		makeAgents();
		
	}

}
