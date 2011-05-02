package memagents;

import memagents.agents.MemoryAgent;
import memagents.ui.MemoryVisualizer;
import memagents.utils.Log;

//TODO: Pridat zradlo (jeden typ), pamatovani zradla, kde je. Zobrazovatko z logu.

public class MemoryAgents 
{
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//Log.intoFile("log.log");
		
		Simulation simulation = new Simulation();

		new MemoryVisualizer(simulation.addAgent(new MemoryAgent(simulation)), simulation);
		new MemoryVisualizer(simulation.addAgent(new MemoryAgent(simulation)), simulation);
		new MemoryVisualizer(simulation.addAgent(new MemoryAgent(simulation)), simulation);
		new MemoryVisualizer(simulation.addAgent(new MemoryAgent(simulation)), simulation);
		new MemoryVisualizer(simulation.addAgent(new MemoryAgent(simulation)), simulation);
		new MemoryVisualizer(simulation.addAgent(new MemoryAgent(simulation)), simulation);
		
		simulation.run();
	}
}