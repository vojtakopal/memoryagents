package memagents;

import memagents.agents.Agent;
import memagents.agents.GNGAgent;
import memagents.agents.GridAgent;
import memagents.agents.PureReactiveAgent;
import memagents.agents.RandomAgent;
import memagents.ui.MemoryVisualizer;
import memagents.utils.NeedsMonitor;

/**
 *	Main application class which contains main method. 
 *	It creates an instance of simulation class, which handles
 *	the whole process of, well, simulating. It adds a group of
 *	agents and possibly also a visualizers so as to be able to
 *	observer what is going on.
 *
 */
public class MemoryAgents 
{	
	/**
	 * 	Main method which start the whole process of simulation. 
	 * 
	 * 	@param args
	 */
	public static void main(String[] args) {
		//Log.intoFile("log.log");
		
		String simulationComment = "";
		
		boolean visualMode = true;
		
		boolean silentMode = false;
		
		String[] agentTypes = {"gng"};//args[]
		
		if (args.length > 0) {
			agentTypes = args[0].split(",");
			simulationComment = args[0];
		}
		
		if (args.length > 1) {
			if (args[1].compareTo("silent") == 0) {
				silentMode = true;
			}
			if (args[1].compareTo("novisual") == 0) {
				visualMode = false;
			}
		}
		
		if (args.length > 2) {
			if (args[2].compareTo("silent") == 0) {
				silentMode = true;
			}
			if (args[2].compareTo("novisual") == 0) {
				visualMode = false;
			}
		}
				
		// Simulation instance.
		Simulation simulation = new Simulation(simulationComment);
		
		// Monitor observes the agent inside the simulation
		// and store data about their current needs.
		NeedsMonitor monitor = new NeedsMonitor(simulation);
		
		for (int i = 0; i < Simulation.NUM_AGENTS; i++) {
			int indexType = i % agentTypes.length;
			String type = agentTypes[indexType];
			Agent agent = null;
			
			if (type.compareTo("gng") == 0) {
				agent = new GNGAgent(simulation);
			} else if (type.compareTo("grid") == 0) {
				agent = new GridAgent(simulation);
			} else if (type.compareTo("random") == 0) {
				agent = new RandomAgent(simulation);
			} else if (type.compareTo("pr") == 0) {
				agent = new PureReactiveAgent(simulation);
			}
			
			if (agent != null) {
				agent.addMonitor(monitor);
				simulation.addAgent(agent);

				if (silentMode) {
					agent.mute();
				}
				if (visualMode) {
					new MemoryVisualizer(agent, simulation);
				}
			}
		}
		
		// Add group of GNG agents.
//		for (int i = 0; i < Simulation.NUM_AGENTS; i++) {
//			GNGAgent agent = new GNGAgent(simulation);
//			agent.addMonitor(monitor);
//			simulation.addAgent(agent);
//			
//			new MemoryVisualizer(agent, simulation);
//		}
		
		// Add group of grid agents.
//		for (int i = 0; i < Simulation.NUM_AGENTS; i++) {
//			GridAgent agent = new GridAgent(simulation);
//			agent.addMonitor(monitor);
//			simulation.addAgent(agent);
//			
//			new MemoryVisualizer(agent, simulation);
//		}
		
		
//		for (int i = 0; i < Simulation.NUM_AGENTS; i++) {
//			RandomAgent agent = new RandomAgent(simulation);
//			agent.addMonitor(monitor);
//			simulation.addAgent(agent);
//			
//			new MemoryVisualizer(agent, simulation);
//		}
		
		
//		for (int i = 0; i < Simulation.NUM_AGENTS; i++) {
//			PureReactiveAgent agent = new PureReactiveAgent(simulation);
//			agent.addMonitor(monitor);
//			simulation.addAgent(agent);
//			
//			//new MemoryVisualizer(agent, simulation);
//		}
		
		
		// Start the simulation.
		simulation.run(100000);
	}
}