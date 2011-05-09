package memagents.environment;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import memagents.agents.Agent;
import memagents.food.FoodGenerator;

/**
 * 
 * @author Vojtech Kopal
 * 
 */
public class Environment 
{
	protected int width;
	
	protected int height;
			
	protected Matrix<ArrayList<Agent>> agents;
	
	protected Matrix<HashMap<Integer, Integer>> food;
	
	public Environment(int width, int height)
	{
		this.width = width;
		this.height = height;
		
		agents = new Matrix<ArrayList<Agent>>(width, height);
		food = new Matrix<HashMap<Integer,Integer>>(width, height);
		
		for (int i = 0; i < this.width; i++)
			for (int j = 0; j < this.height; j++)
			{
				agents.set(i, j, new ArrayList<Agent>());
				food.set(i, j, new HashMap<Integer,Integer>());
			}
		
	}
	
		
	/**
	 * Returns a list of object at the given position.
	 * 
	 * @param x position
	 * @param y position
	 * @return ArrayList<EnvironmentObject>
	 */
	public ArrayList<Agent> getAgents(int i, int j)
	{
		return agents.get(i, j);
	}
	
	
	/**
	 * Add an object to the given position.
	 * 
	 * @param x position
	 * @param y position
	 * @param new object
	 */
	public void addAgent(int i, int j, Agent agent)
	{		
		agents.get(i, j).add(agent);
	}
	
	
	/**
	 * Remove an object from the list at the given position.
	 * 
	 * @param x position
	 * @param y position
	 * @param object to be removed
	 */
	public void removeAgent(int i, int j, Agent agent)
	{
		ArrayList<Agent> list = agents.get(i, j);
		
		if (list == null) return;
		
		list.remove(list.indexOf(agent));
	} 
	
	public void removeAll(int i, int j) {
		agents.get(i, j).clear();
	}
	
	/**
	 * It provides a list of object at the given position.
	 * 
	 * @param position
	 * @return ArrayList<EnvironmentObject>
	 */
	public ArrayList<Agent> getNeighbours(Point position)
	{
		return agents.get(position.x, position.y);
	}
	
	/**
	 * Adds food at specified position in environment.
	 * 
	 * @param x
	 * @param y
	 * @param foodKind
	 * @param amount
	 */
	public void seedFood(int x, int y, int foodKindId, int amount) {
		if (isOutOfBounds(x, y)) return;
		
		HashMap<Integer, Integer> field = food.get(x, y);
		
		int currentAmount = 0;
		
		if (field.containsKey(foodKindId)) currentAmount = field.get(foodKindId); 
		
		currentAmount += amount;
		
		field.put(foodKindId, currentAmount);
	}
	
	/**
	 * 
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	public HashMap<Integer, Integer> getAllFoodAt(int x, int y) {
		if (isOutOfBounds(x, y)) return null;
		return food.get(x, y);
	}
	
	public void eatFoodAt(int x, int y, int foodKind) {
		if (isOutOfBounds(x, y)) return;
		food.get(x, y).put(foodKind, 0);
	}
		
	/**
	 * For given position and moves available, it filters out available
	 * @param position
	 * @param moves
	 * @return ArrayList<int[]>
	 */
	public ArrayList<Point> filterMoves(Point position, ArrayList<Point> moves)
	{
		ArrayList<Point> filtered = new ArrayList<Point>();
		
		for (Point move : moves)
		{
			int nx = move.x + position.x;
			int ny = move.y + position.y;
			
			if (nx >= 0 && nx < width && ny >= 0 && ny < height) 
			{
				//TODO: eventually check collisions
			
				filtered.add(move);
			}
		}
		
		return filtered;
	}
	
	public boolean isOutOfBounds(int x, int y) {
		return x < 0 || x >= width || y < 0 || y >= height;
	}
	
	public void initNextDay()
	{		
	}
}
