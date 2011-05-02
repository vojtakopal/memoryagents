package memagents;

import java.util.ArrayList;
import java.util.Random;

import memagents.agents.Agent;
import memagents.agents.MemoryAgent;
import memagents.agents.Position;
import memagents.environment.BasicEnvironment;
import memagents.environment.Resource;
import memagents.environment.StorageEnvironment;
import memagents.schedule.Event;
import memagents.schedule.IEventer;
import memagents.schedule.Scheduler;
import memagents.utils.Log;

/**
 * Simulation leads the general processes and calculates results of agents' interaction.
 * 
 * @author Vojtech Kopal
 *
 */
public class Simulation 
{
	/**
	 *	Size of simulation stands for width and height of 2D matrix. 
	 *
	 */
	public static int SIZE = 100;
	
	protected StorageEnvironment env;
	protected Scheduler scheduler;
	protected ArrayList<Agent> agents;
	protected SimulationSettings settings = new SimulationSettings();
	
	public Simulation() 
	{
		System.out.println("Simulation starts.");
		init();
	}
	
	/**
	 * Initialization of environment, agents and their random positions.
	 */
	private void init()
	{
		env = new StorageEnvironment(SIZE, SIZE);
		agents = new ArrayList<Agent>();
		scheduler = new Scheduler();
						
		ArrayList<int[]> dest = new ArrayList<int[]>();
		
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				if (Math.random() < 0.01) {
					dest.add(new int[] {i, j});
				}
			}
		}
		
		env.addResource(new Resource(dest));
		
		System.out.println("Simulation initialized.");
	}
	
	/**
	 * 
	 */
	public MemoryAgent addAgent(final MemoryAgent agent) {
		
		Random rand = new Random();
		int agentX = rand.nextInt(SIZE);
		int agentY = rand.nextInt(SIZE);
		
		/// Add agent
		agents.add(agent);
		
		/// Position him into environment
		/// env.add(agentX, agentY, agent);
		
		Log.println("init " + (agents.size() - 1) + " " + agentX + " " + agentY);
		
		//agent.setId(agents.size()-1);
		
		/// Each agent schedules living event.
		scheduler.scheduleRepeatedEvent(new IEventer() {
			public void callback() {
				agent.live();
			}
			
			public void postcallback() {
				agent.computeKnowledge();
			}
		}, 1);
		
		return agent;
	}
	
	/**
	 * 
	 */
	public MemoryAgent addAgent(MemoryAgent agent, int x, int y) {
		addAgent(agent);
		
		Position agentPosition = agent.getPosition();
		agentPosition.x = x;
		agentPosition.y = y;
		
		return agent;
	}
	
	public ArrayList<Agent> getAgents() {
		return agents;
	}
	
	/**
	 * Shouldn't be here. Makes protected variable accessible.
	 * @return BasicEnvironment
	 */
	public StorageEnvironment getEnvironment()
	{
		return env;
	}
	
	public int getSize() {
		return SIZE;
	}
	
	public SimulationSettings getSettings() {
		return settings;
	}
	
	
	/**
	 * Runs the entire simulation. 
	 */
	public void run() 
	{
		while (!scheduler.empty())
		{
			Log.println("nextday");
			env.initNextDay();
			
			ArrayList<Event> currentEvents = new ArrayList<Event>();
			
			Event currentEvent = scheduler.dequeue();
					
			// calling processes
			while (currentEvent != null)
			{
				currentEvents.add(currentEvent);
				currentEvent.process();				
				currentEvent = currentEvent.getNext();
			}
			
			// calling post processes: computing knowledge etc.
			for (Event event : currentEvents) {
				event.postprocess();
			}
			
			currentEvents.clear();
			
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
