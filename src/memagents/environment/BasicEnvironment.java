package memagents.environment;

import java.util.ArrayList;

import memagents.agents.Position;

/**
 * This class contains matrix where EnvironmentObjects are stored.
 * 
 * 
 * @author Vojtech Kopal
 *
 */
public class BasicEnvironment 
{

	protected Matrix<ArrayList<EnvironmentObject>> matrix;
	
	protected int width;
	protected int height;
	
	public BasicEnvironment(int width, int height)
	{
		this.width = width;
		this.height = height;
		
		matrix = new Matrix<ArrayList<EnvironmentObject>>(width, height);
		
		for (int i = 0; i < this.width; i++)
			for (int j = 0; j < this.height; j++)
			{
				matrix.set(i, j, new ArrayList<EnvironmentObject>());
			}
	}
	
	
	/**
	 * Returns a list of object at the given position.
	 * 
	 * @param x position
	 * @param y position
	 * @return ArrayList<EnvironmentObject>
	 */
	public ArrayList<EnvironmentObject> get(int i, int j)
	{
		return matrix.get(i, j);
	}
	
	
	/**
	 * Add an object to the given position.
	 * 
	 * @param x position
	 * @param y position
	 * @param new object
	 */
	public void add(int i, int j, EnvironmentObject object)
	{		
		matrix.get(i, j).add(object);
	}
	
	
	/**
	 * Remove an object from the list at the given position.
	 * 
	 * @param x position
	 * @param y position
	 * @param object to be removed
	 */
	public void remove(int i, int j, EnvironmentObject object)
	{
		ArrayList<EnvironmentObject> list = matrix.get(i, j);
		
		if (list == null) return;
		
		list.remove(list.indexOf(object));
	} 
	
	public void removeAll(int i, int j) {
		matrix.get(i, j).clear();
	}
	
	/**
	 * It provides a list of object at the given position.
	 * 
	 * @param position
	 * @return ArrayList<EnvironmentObject>
	 */
	public ArrayList<EnvironmentObject> getNeighbours(int[] position)
	{
		return matrix.get(position[0], position[1]);
	}
	
	
	/**
	 * Process any changes in environment such as new terrain, etc.
	 * To be overrided.
	 */
	public void initNextDay()
	{
		
	}
	
	
	/**
	 * For given position and moves available, it filters out available
	 * @param position
	 * @param moves
	 * @return ArrayList<int[]>
	 */
	public ArrayList<int[]> filterMoves(Position position, ArrayList<int[]> moves)
	{
		ArrayList<int[]> filtered = new ArrayList<int[]>();
		
		for (int[] move : moves)
		{
			int nx = move[0] + position.x;
			int ny = move[1] + position.y;
			
			if (nx >= 0 && nx < width && ny >= 0 && ny < height) 
			{
				//TODO: eventually check collisions
			
				filtered.add(move);
			}
		}
		
		return filtered;
	}
}
