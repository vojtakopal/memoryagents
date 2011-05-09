package memagents;

import memagents.agents.GNGAgent;
import memagents.ui.MemoryVisualizer;
import memagents.utils.Log;

//TODO: Pridat zradlo (jeden typ), pamatovani zradla, kde je. Zobrazovatko z logu.

/**
 *	Main class. 
 *
 */
public class MemoryAgents 
{
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//Log.intoFile("log.log");
		
		// simulation instance
		Simulation simulation = new Simulation();

		new MemoryVisualizer(simulation.addAgent(new GNGAgent(simulation)), simulation);
		new MemoryVisualizer(simulation.addAgent(new GNGAgent(simulation)), simulation);
		new MemoryVisualizer(simulation.addAgent(new GNGAgent(simulation)), simulation);
//		new MemoryVisualizer(simulation.addAgent(new MemoryAgent(simulation)), simulation);
//		new MemoryVisualizer(simulation.addAgent(new MemoryAgent(simulation)), simulation);
//		new MemoryVisualizer(simulation.addAgent(new MemoryAgent(simulation)), simulation);
		
		simulation.run();
	}
}