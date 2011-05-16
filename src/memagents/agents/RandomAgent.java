package memagents.agents;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import memagents.Simulation;
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
		
	public Memory getMemory() {
		return null;
	}
}
