package memagents;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;

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
public class MemoryAgents {
	/**
	 * The name of the default configuration file.
	 */
	public static final String DEFAULT_CONFIG_FILE_NAME = "cfg/config.ini";
	
	public static final int EXIT_CODE_INVALID_CONFIG_FILE = 1;
	
	/**
	 * 	Main method which start the whole process of simulation. 
	 * 
	 * 	@param args
	 */
	public static void main(String[] args) {
		//Log.intoFile("log.log");
		
		String configFileName = DEFAULT_CONFIG_FILE_NAME;
		if (args.length > 0) {
			configFileName = args[0];
		}
		
		Wini ini = null;
		try {
			ini = new Wini(new File(configFileName));
		} catch (InvalidFileFormatException e) {
			e.printStackTrace();
			System.exit(EXIT_CODE_INVALID_CONFIG_FILE);
		} catch (FileNotFoundException e) {
			System.err.printf("The ini file '%s' was not found.\n", configFileName);
			System.exit(EXIT_CODE_INVALID_CONFIG_FILE);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(EXIT_CODE_INVALID_CONFIG_FILE);
		}
		Ini.Section agentsIni = ini.get("agents");
				
		// A label added to output file.
		String simulationComment = "";
		
		// A flag whether to show a memory visualisation or not.
		boolean visualMode = true;
		
		// A flag whether the agents are allowed to communicate.
		// boolean silentMode = true;
		
		// What kinds of agent are about to be in the simulation.
		// String[] agentTypes = {"graph"};//args[]
		
		// Load arguments,
//		if (args.length > 0) {
//			agentTypes = args[0].split(",");
//			simulationComment = args[0];
//		}
//		
//		if (args.length > 1) {
//			if (args[1].compareTo("silent") == 0) {
//				silentMode = true;
//			}
//			if (args[1].compareTo("novisual") == 0) {
//				visualMode = false;
//			}
//		}
//		
//		if (args.length > 2) {
//			if (args[2].compareTo("silent") == 0) {
//				silentMode = true;
//			}
//			if (args[2].compareTo("novisual") == 0) {
//				visualMode = false;
//			}
//		}
				
		
		// Simulation instance.
		Simulation simulation = new Simulation(simulationComment);
		
		// Monitor observes the agent inside the simulation
		// and store data about their current needs.
		NeedsMonitor monitor = new NeedsMonitor(simulation);
		
		int numAgents = agentsIni.get("numTypes", int.class);
		for (int i = 0; i < numAgents; i++) {
			for (int j = 0; j < agentsIni.get("num" + i, int.class); j++) {
				String type = agentsIni.get("type" + i, String.class);
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
		
					if (agentsIni.get("silent" + i, boolean.class)) {
						agent.mute();
					}
					if (visualMode) {
						new MemoryVisualizer(agent, simulation);
					}
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