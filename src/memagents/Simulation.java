package memagents;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import sun.misc.Regexp;

import memagents.agents.Agent;
import memagents.environment.Environment;
import memagents.food.FoodGenerator;
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
	
	/**
	 *	The number of agents in the simulation. 
	 *
	 */
	public static int NUM_AGENTS = 3;
	
	/**
	 *	Number of food generators in the simulation. 
	 *
	 */
	public static int NUM_FOODGENERATORS = 3;
	
	protected Environment environment;
	protected Scheduler scheduler;
	protected ArrayList<Agent> agents;
	protected ArrayList<FoodGenerator> generators;
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
		environment = new Environment(SIZE, SIZE);
		agents = new ArrayList<Agent>();
		scheduler = new Scheduler();
		generators = new ArrayList<FoodGenerator>();
		
		
		Random random = new Random();
		for (int i = 0; i < NUM_FOODGENERATORS; i++) {
			generators.add(new FoodGenerator(random.nextInt(SIZE), random.nextInt(SIZE)));
		}
		
		System.out.println("Simulation initialized.");
	}
	
	/**
	 * 
	 */
	public Agent addAgent(final Agent agent) {
		
		Random rand = new Random();
		int agentX = rand.nextInt(SIZE);
		int agentY = rand.nextInt(SIZE);
		
		agents.add(agent);	
		
		/// Position him into environment
		/// env.add(agentX, agentY, agent);
		
		Log.println("init " + (agents.size() - 1) + " " + agentX + " " + agentY);
		
		//agent.setId(agents.size()-1);
		
		/// Each agent schedules living event.
		scheduler.scheduleRepeatedEvent(new IEventer() {
			public void callback() {
				// agent live
				agent.live();
				
				// agent hunger
				// adds 0.01 to all needs
				for (int foodKind = 0; foodKind < FoodGenerator.getSize(); foodKind++) {
					float amount = agent.getNeed(foodKind);
					amount += 0.01;
					agent.setNeed(foodKind, amount);
				}
			}
		}, 1);
		
		return agent;
	}
	
	/**
	 * 
	 */
	public Agent addAgent(Agent agent, int x, int y) {
		addAgent(agent);
		
		Point agentPosition = agent.getPosition();
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
	public Environment getEnvironment()
	{
		return environment;
	}
	
	public int getSize() {
		return SIZE;
	}
	
	public FoodGenerator getGenerator(int foodKind) {
		for (FoodGenerator foodGenerator : this.generators) {
			if (foodGenerator.getKind() == foodKind) {
				return foodGenerator;
			}
		}
		
		return null;
	}
	
	public SimulationSettings getSettings() {
		return settings;
	}
	
	
	/**
	 * Runs the entire simulation. 
	 */
	/**
	 * 
	 */
	public void run() 
	{
		while (!scheduler.empty())
		{
			Log.println("nextday");
			environment.initNextDay();
			
			ArrayList<Event> currentEvents = new ArrayList<Event>();
			
			Event currentEvent = scheduler.dequeue();
					
			// calling processes
			while (currentEvent != null)
			{
				currentEvents.add(currentEvent);
				currentEvent.process();				
				currentEvent = currentEvent.getNext();
			}
			
			currentEvents.clear();
			
			// foooood, hungryyy!!!
			for (FoodGenerator generator : generators) {
				generator.seed(environment);
			}
			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
