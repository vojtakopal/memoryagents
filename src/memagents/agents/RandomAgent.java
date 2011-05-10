package memagents.agents;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import memagents.Simulation;
import memagents.utils.Log;

/**
 * Basic idea of agent. This class should be extended for further usage.
 * @author Vojtech Kopal
 *
 */
public class RandomAgent extends Agent 
{
	protected Simulation simulation;
	protected int id;
	
	public RandomAgent(Simulation simulation) 
	{
		super();
		
		this.simulation = simulation;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	protected ArrayList<Point> availableMoves()
	{
		ArrayList<Point> moves = new ArrayList<Point>();
		
		moves.add(new Point(-1, 0));
		moves.add(new Point( 1, 0));
		moves.add(new Point( 0,-1));
		moves.add(new Point( 0, 1));
		
		return moves;
	}
	
	protected void move(Point move)
	{
		//Log.println("move " + id + " " + position.x + " " + position.y + " " + (position.x + move.x) + " " + (position.y + move.y));
		
		//simulation.getEnvironment().remove(position[0], position[1], this);
		//simulation.getEnvironment().add(position[0] + move[0], position[1] + move[1], this);
		position.x += move.x;
		position.y += move.y;
	}
	
	public void live()
	{
		// do something
		// System.out.println("Agent lives");
		
		ArrayList<Point> moves = simulation.getEnvironment().filterMoves(position, availableMoves());
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
	
	public Point[] whereIs(int foodKind) {
		return null;
	}
	
	public void draw(Graphics g, int width, int height) {
		
	}
}
