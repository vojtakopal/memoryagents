package memagents;

import java.util.ArrayList;

import memagents.agents.IAgent;
import memagents.agents.MemoryAgent;
import memagents.environment.BasicEnvironment;
import memagents.environment.Resource;
import memagents.environment.StorageEnvironment;
import memagents.schedule.Event;
import memagents.schedule.IEventer;
import memagents.schedule.Scheduler;
import memagents.utils.Log;

/**
 * Simulation leads the general processes and calculates results of agents' interaction.
 * @author Vojtech Kopal
 *
 */
public class Simulation 
{
	protected StorageEnvironment env;
	protected Scheduler scheduler;
	protected ArrayList<IAgent> agents;
	
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
		env = new StorageEnvironment(10,10);
		agents = new ArrayList<IAgent>();
		scheduler = new Scheduler();
				
		/// A list of possible positions.
		ArrayList<int[]> randPosition = new ArrayList<int[]>();
		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 10; j++) 
			{
				randPosition.add(new int[]{i, j});
			}
		
		ArrayList<int[]> dest = new ArrayList<int[]>();
		dest.add(new int[] {2, 2});
		dest.add(new int[] {4, 2});
		dest.add(new int[] {2, 4});
		dest.add(new int[] {5, 5});
		dest.add(new int[] {3, 8});
		
		env.addResource(new Resource(dest));
		
		/// Creating a group of agents and positioning them.
		for (int i = 0; i < 10; i++)
		{
			int rand = (int)(Math.random()*randPosition.size());
			int[] position = randPosition.remove(rand);
			final MemoryAgent agent = new MemoryAgent(this, position);
			
			/// Add agent
			agents.add(agent);
			
			/// Position him randomly into environment
			env.add(position[0], position[1], agent);
			
			Log.println("init " + (agents.size() - 1) + " " + position[0] + " " + position[1]);
			
			agent.setId(agents.size()-1);
			
			/// Each agent schedules living event.
			scheduler.scheduleRepeatedEvent(new IEventer() {
				public void callback()
				{
					agent.live();
				}
			}, 1);
		}
		
		System.out.println("Simulation initialized.");
	}

	
	/**
	 * Shouldn't be here. Makes protected variable accessible.
	 * @return BasicEnvironment
	 */
	public BasicEnvironment getEnvironment()
	{
		return env;
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
			
			Event currentEvent = scheduler.dequeue();
					
			while (currentEvent != null)
			{
				currentEvent.process();				
				currentEvent = currentEvent.getNext();
			}
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
