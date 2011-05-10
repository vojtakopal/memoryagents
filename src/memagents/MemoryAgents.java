package memagents;

import memagents.agents.GNGAgent;
import memagents.ui.MemoryVisualizer;
import memagents.utils.Log;
import memagents.utils.NeedsMonitor;

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
		NeedsMonitor monitor = new NeedsMonitor(simulation);
		
		for (int i = 0; i < Simulation.NUM_AGENTS; i++) {
			GNGAgent agent = new GNGAgent(simulation);
			agent.addMonitor(monitor);
			simulation.addAgent(agent);
			
			new MemoryVisualizer(agent, simulation);
		}
//		new MemoryVisualizer(simulation.addAgent(new GNGAgent(simulation)), simulation);
//		new MemoryVisualizer(simulation.addAgent(new GNGAgent(simulation)), simulation);
//		new MemoryVisualizer(simulation.addAgent(new GNGAgent(simulation)), simulation);
//		new MemoryVisualizer(simulation.addAgent(new GNGAgent(simulation)), simulation);
//		new MemoryVisualizer(simulation.addAgent(new GNGAgent(simulation)), simulation);
//		new MemoryVisualizer(simulation.addAgent(new GNGAgent(simulation)), simulation);
//		new MemoryVisualizer(simulation.addAgent(new MemoryAgent(simulation)), simulation);
//		new MemoryVisualizer(simulation.addAgent(new MemoryAgent(simulation)), simulation);
//		new MemoryVisualizer(simulation.addAgent(new MemoryAgent(simulation)), simulation);
		
		simulation.run();
	}
}