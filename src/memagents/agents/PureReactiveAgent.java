package memagents.agents;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import memagents.Simulation;
import memagents.memory.ExpectedGauss;
import memagents.memory.IMemory;

public class PureReactiveAgent extends Agent {

	public PureReactiveAgent(Simulation simulation) {
		super(simulation);
		
		sight = Simulation.SIZE * 2;
		audition = Simulation.AGENT_AUDITION;
	}
	
	public String getName() {
		return "pr";
	}
	
	public void setSight(int value) {
		// cant do that
	}
	
	public void setAudition(int value) {
		// cant do that
	}

	@Override
	public IMemory getMemory() {
		return new IMemory() {
			@Override
			public void learn(int foodKind, ArrayList<Point> food) {
				// nothing				
			}
			
			@Override
			public Point[] getSample(int foodKind) {
				ArrayList<Point> points = knownFoodLocations.get(foodKind);

				Point[] sample = new Point[Simulation.ANSWER_SAMPLE];
				if (points.size() > 0) {
					for (int i = 0; i < sample.length; i++) {
						sample[i] = points.get(simulation.getRandom().nextInt(points.size()));
					}	
				}
				return sample;
			
			}

			@Override
			public int getWidth() {
				return Simulation.SIZE;
			}

			@Override
			public int getHeight() {
				return Simulation.SIZE;
			}

			@Override
			public HashMap<Integer, ExpectedGauss> getExpectedGausses() {
				return null;
			}

			@Override
			public ExpectedGauss getExpectedGauss(int foodKind) {
				return new ExpectedGauss();
			}

			@Override
			public void run() {
				
			}
		};
	}

}
