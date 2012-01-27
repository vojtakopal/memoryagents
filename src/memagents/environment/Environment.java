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
				
	protected Matrix<HashMap<Integer, Integer>> food;
	
	public Environment(int width, int height)
	{
		this.width = width;
		this.height = height;
		
		food = new Matrix<HashMap<Integer,Integer>>(width, height);
		
		for (int i = 0; i < this.width; i++)
			for (int j = 0; j < this.height; j++) {
				food.set(i, j, new HashMap<Integer,Integer>());
			}
		
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
	
	public boolean eatFoodAt(int x, int y, int foodKind) {
		if (isOutOfBounds(x, y)) return false;
		if (food.get(x, y).get(foodKind) > 0) {
			food.get(x, y).put(foodKind, 0);
			return true;
		}
		return false;
	}
		
	/**
	 * For given position and moves available, it filters out available
	 * @param position
	 * @param moves
	 * @return ArrayList<int[]>
	 */
	public ArrayList<Point> filterMoves(Point position, ArrayList<Point> moves) {
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
	
	public void initNextDay() {		
	}
}
