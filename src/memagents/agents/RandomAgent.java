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
	public RandomAgent(Simulation simulation) 
	{
		super(simulation);
		
	}
		
	public IMemory getMemory() {
		return new IMemory() {
			@Override
			public void learn(int foodKind, ArrayList<Point> food) {
				// nothing				
			}
			
			@Override
			public Point[] getSample(int foodKind) {
				return new Point[] {};
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
