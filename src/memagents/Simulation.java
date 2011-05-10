package memagents;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import sun.misc.Regexp;

import memagents.agents.Agent;
import memagents.environment.Environment;
import memagents.food.FoodGenerator;
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
	public static final int SIZE = 100;
	
	/**
	 *	The number of agents in the simulation. 
	 *
	 */
	public static final int NUM_AGENTS = 15;
	
	/**
	 *	Number of food generators in the simulation. 
	 *
	 */
	public static final int NUM_FOODGENERATORS = 6;
	
	/**
	 * 	Speed of simulation (sleep in ms)
	 * 
	 */
	public static final int SPEED = 10; 
	
	protected Environment environment;
	protected ArrayList<Agent> agents;
	protected ArrayList<FoodGenerator> generators;
	protected SimulationSettings settings = new SimulationSettings();
	protected Random random = null;
	public Random getRandom() { return random; }
	
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
		random = new Random(); random.setSeed(123456789);
		environment = new Environment(SIZE, SIZE);
		agents = new ArrayList<Agent>();
		generators = new ArrayList<FoodGenerator>();
		
		for (int i = 0; i < NUM_FOODGENERATORS; i++) {
			generators.add(new FoodGenerator(random.nextInt(SIZE), random.nextInt(SIZE), this));
		}
		
		System.out.println("Simulation initialized.");
	}
	
	/**
	 * 
	 */
	public Agent addAgent(final Agent agent) {
		
		int agentX = random.nextInt(SIZE);
		int agentY = random.nextInt(SIZE);
		
		agents.add(agent);	
		
		/// Position him into environment
		/// env.add(agentX, agentY, agent);
		
		Log.println("init " + (agents.size() - 1) + " " + agentX + " " + agentY);
		
		//agent.setId(agents.size()-1);
	
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
		while (true)
		{
			Log.println("nextday");
			environment.initNextDay();
			
			for (Agent agent : agents) {
				// agent live
				agent.live();
				
				// agent hunger
				// adds 0.01 to all needs
				for (int foodKind = 0; foodKind < FoodGenerator.getSize(); foodKind++) {
					float amount = agent.getNeed(foodKind);
					amount += 0.001;
					agent.setNeed(foodKind, amount);
				}
				
				agent.processMonitors();
			}
			
			// foooood, hungryyy!!!
			for (FoodGenerator generator : generators) {
				generator.seed(environment);
			}
			
			try {
				Thread.sleep(SPEED);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
