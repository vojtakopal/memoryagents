package memagents.agents;

import java.util.ArrayList;
import java.util.HashMap;

import com.sun.org.apache.bcel.internal.generic.TABLESWITCH;

import memagents.Simulation;
import memagents.agents.memory.Chunk;
import memagents.agents.memory.MemoryAgentMemories;
import memagents.agents.memory.MemoryAgentMemories.BeliefType;
import memagents.agents.memory.MemoryItem;
import memagents.environment.EnvironmentObject;
import memagents.environment.Product;
import memagents.environment.StorageEnvironment;
import memagents.utils.Log;

/**
 * 
 * TODO: Agenti musi videt kolem sebe.
 * 
 * 
 * @author Vojta
 *
 */
public class MemoryAgent extends BasicAgent 
{
	public static int MEMORY_SIZE = 100;
	
	/**
	 * 	Audition is a quality of an agent. It is a distance in which the agent hears world around him.
	 * 
	 */
	protected int audition = 30;
	
	/**
	 * 	Sight is a quality of an agent. It is a distance in which the agent sees world around him.
	 * 
	 */
	protected int sight = 10;
	
	/**
	 *	Memory is represented by two-dimensional array of double.
	 *
	 */
	//private double[][] memory = new double[MEMORY_SIZE][MEMORY_SIZE];
	protected MemoryAgentMemories memories = new MemoryAgentMemories();
	
	/**
	 * 	Belief.	
	 * 
	 *  "Do you believe in the Hogfather, Hex?"
	 */
	//private HashMap<Integer, double[][]> believes = new HashMap<Integer, double[][]>();
		
	public MemoryAgent(Simulation simulation) 
	{
		super(simulation);
	}
	
	public int getX() { return position.x; }
	public void setX(int value) { position.x = value; }
	
	public int getY() { return position.y; }
	public void setY(int value) { position.y = value; }

	public Chunk getMemoryChunkAt(BeliefType type, int x, int y) {
		return memories.getBeliefChunkAt(type, x, y);
	}
	
	public void live() 
	{
		StorageEnvironment env = (StorageEnvironment) this.simulation.getEnvironment();
		
		// put information about products into memory
//		for ( EnvironmentObject object : env.getNeighbours(this.position) )
//		{
//			if ( object instanceof Product )
//			{
//				// remember it
//				Log.println("memory "+this.id+" Product "+this.position[0]+" "+this.position[1]);
//				memory.add(new MemoryItem(this.position, (Product) object));
//			}
//		}
		
		// and move
		// super.live();
		
		// fade out
		memories.doAge();
				
		// agents around
		for (Agent otheragent : simulation.getAgents()) {
			if (otheragent instanceof MemoryAgent && otheragent != this) {
				MemoryAgent otherMemoryAgent = (MemoryAgent)otheragent;
				double distance = Math.sqrt((otherMemoryAgent.getX() - this.getX())*(otherMemoryAgent.getX() - this.getX()) + (otherMemoryAgent.getY() - this.getY())*(otherMemoryAgent.getY() - this.getY()));
				if (distance <= audition) { 
					memories.beInfluenced(otherMemoryAgent.memories);
				} 
			}
		}

		// learn new information
		for (int i = Math.max(getX() - sight, 0); i < Math.min(getX() + sight, TestAgent.MEMORY_SIZE); i++) {
			for (int j = Math.max(getY() - sight, 0); j < Math.min(getY() + sight, TestAgent.MEMORY_SIZE); j++) {
				double distanceSquare = ((i - getX()) * (i - getX()) + (j - getY()) * (j - getY()));
				if (distanceSquare < sight * sight) {
					int value = simulation.getEnvironment().get(i, j).size();
					memories.doLearn(BeliefType.MRKEV, i, j, value);
				}
			}
		}
		
		// agent moves
		// TODO: doplnit nahodny pohyb, pokud neni uspokojující zradlo v dosahu
		// mit to jako moznost
		ArrayList<double[]> whereToGo = new ArrayList<double[]>();
		double distanceImportance = 0.01;
		
		for (int i = 0; i < TestAgent.MEMORY_SIZE; i++) {
			for (int j = 0; j < TestAgent.MEMORY_SIZE; j++) {
				double distance = Math.sqrt((i - this.getX())*(i - this.getX()) + (j - this.getY())*(j - this.getY()));
				//if (distance <= sight) {
					Chunk chunk = memories.getBeliefChunkAt(BeliefType.MRKEV, i, j);
					if (chunk.amount > 0) {
						double value = chunk.belief * chunk.amount * chunk.amount - distance * distanceImportance;
						whereToGo.add(new double[] {i, j, value});
					}
				//}
			}
		}
		
		double bestX = -1;
		double bestY = -1;
		double bestValue = 0;
		
		for (double[] spot : whereToGo) {
			if (spot[2] > bestValue) {
				bestValue = spot[2];
				bestX = spot[0];
				bestY = spot[1];
			}
		}
		
		// move there
		if (bestX > -1 && bestY > -1) {
			// find closest cell 
			int closestY = this.getY();
			int closestX = this.getX();
			double closestDistance = Double.MAX_VALUE;
			for (int i = -1; i < 2; i++) {
				for (int j = -1; j < 2; j++) {
					int X = this.getX() + i;
					int Y = this.getY() + j;
					double distance = Math.sqrt((X - bestX)*(X - bestX) + (Y - bestY)*(Y - bestY));
					
					if (distance < closestDistance) {
						closestDistance = distance;
						closestX = X;
						closestY = Y;
					}
				}
			}
			
			this.setX(closestX);
			this.setY(closestY);		
			
			simulation.getEnvironment().removeAll(closestX, closestY);
			Chunk chunk = memories.getBeliefChunkAt(BeliefType.MRKEV, closestX, closestY);
			chunk.amount = 0;
			chunk.belief = 1;
		} else {
			if (simulation.getSettings().useRandomMovement) {
				super.live();
			}
		}
		
		/*

		int randX = (int)(Math.random()*getCols());
		int randY = (int)(Math.random()*getRows());
		
		touch(randX, randY);
		
		*/
	}
	
	public void computeKnowledge() {
		memories.computeKnowledge();
	}
}
