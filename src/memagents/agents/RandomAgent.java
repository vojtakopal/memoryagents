package memagents.agents;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import memagents.Simulation;
import memagents.memory.ExpectedGauss;
import memagents.memory.IMemory;
import memagents.memory.Memory;
import memagents.utils.Log;

/**
 * Basic idea of agent. This class should be extended for further usage.
 * @author Vojtech Kopal
 *
 */
public class RandomAgent extends Agent 
{
	private ExpectedGauss expectedGauss;
	
	public RandomAgent(Simulation simulation) 
	{
		super(simulation);

		expectedGauss = new ExpectedGauss();
		
		expectedGauss.var = Simulation.SIZE / 2;
		expectedGauss.x = Simulation.SIZE / 2;
		expectedGauss.y = Simulation.SIZE / 2;
	}
	
	public String getName() {
		return "random";
	}
		
	public IMemory getMemory() {
		return new IMemory() {
			@Override
			public void learn(int foodKind, ArrayList<Point> food) {
				// nothing				
			}
			
			@Override
			public Point[] getSample(int foodKind) {
				return null;
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
				return expectedGauss;
			}

			@Override
			public void run() {
				
			}
		};
	}
	
}
