package memagents.agents;

import java.util.ArrayList;

import memagents.Simulation;
import memagents.agents.memory.MemoryItem;
import memagents.environment.EnvironmentObject;
import memagents.environment.Product;
import memagents.environment.StorageEnvironment;
import memagents.utils.Log;

public class MemoryAgent extends BasicAgent 
{
	protected ArrayList<MemoryItem> memory;
	
	public MemoryAgent(Simulation simulation, int[] position) {
		super(simulation, position);
		
		memory = new ArrayList<MemoryItem>();
	}

	public void live()
	{
		StorageEnvironment env = (StorageEnvironment) this.simulation.getEnvironment();
		
		// put information about products into memory
		for ( EnvironmentObject object : env.getNeighbours(this.position) )
		{
			if ( object instanceof Product )
			{
				// remember it
				Log.println("memory "+this.id+" Product "+this.position[0]+" "+this.position[1]);
				memory.add(new MemoryItem(this.position, (Product) object));
			}
		}
		
		// and move
		super.live();
	}
}
