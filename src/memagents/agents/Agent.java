package memagents.agents;

import java.awt.Graphics;
import java.awt.Point;
import java.util.HashMap;

import memagents.food.FoodGenerator;

/**
 * 
 * @author Vojtech
 *
 */
public abstract class Agent {
	static private int nextId = 0;
	static protected int getNextId() { return nextId++; }
	
	private int id;
	protected Point position = new Point();
		
	public Agent() { this.id = getNextId(); }
	public int getId() { return id; }
	public Point getPosition() { return position; }
	
	protected HashMap<Integer, Float> needs = new HashMap<Integer, Float>();
	public float getNeed(int foodKind) {
		float need = 0;
		
		if (needs.containsKey(foodKind)) {
			need = needs.get(foodKind);
		} else {
			needs.put(foodKind, 0.0f);
		}
		
		return need;
	} 
	public void setNeed(int foodKind, float amount) {
		needs.put(foodKind, amount);
	}
	
	public abstract void live();	
	public abstract void draw(Graphics g, int width, int height);
}
