package memagents.agents;

import java.util.ArrayList;

import memagents.Simulation;
import memagents.environment.EnvironmentObject;
import memagents.utils.Log;

/**
 * Basic idea of agent. This class should be extended for further usage.
 * @author Vojtech Kopal
 *
 */
public class BasicAgent extends EnvironmentObject implements IAgent 
{
	protected Simulation simulation;
	protected int[] position;
	protected int id;
	
	public BasicAgent(Simulation simulation, int[] position) 
	{
		this.simulation = simulation;
		this.position = position;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	protected ArrayList<int[]> availableMoves()
	{
		ArrayList<int[]> moves = new ArrayList<int[]>();
		
		moves.add(new int[]{-1, 0});
		moves.add(new int[]{ 1, 0});
		moves.add(new int[]{ 0,-1});
		moves.add(new int[]{ 0, 1});
		
		return moves;
	}
	
	private void move(int[] move)
	{
		Log.println("move " + id + " " + position[0] + " " + position[1] + " " + (position[0] + move[0]) + " " + (position[1] + move[1]));
		
		simulation.getEnvironment().remove(position[0], position[1], this);
		simulation.getEnvironment().add(position[0] + move[0], position[1] + move[1], this);
		position[0] += move[0];
		position[1] += move[1];
	}
	
	public void live()
	{
		// do something
		// System.out.println("Agent lives");
		
		ArrayList<int[]> moves = simulation.getEnvironment().filterMoves(position, availableMoves());
		int rand = (int)(moves.size() * Math.random());
		
		move(moves.get(rand));
		
		/*
		// met someone?
		for (int i : simulation.getEnvitonment().getNeighbours(position))
		{
			if (i != id)
			{
				Log.println(id + " met " + i);
			}			
		}
		*/
	}
}
