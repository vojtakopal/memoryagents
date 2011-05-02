package memagents.agents;

import memagents.environment.EnvironmentObject;

/**
 * 
 * @author Vojtech
 *
 */
public abstract class Agent {
	static private int nextId = 0;
	static protected int getNextId() { return nextId++; }
	
	private int id;
	protected Position position = new Position();
		
	public Agent() { this.id = getNextId(); }
	public int getId() { return id; }
	public Position getPosition() { return position; }
	
	public abstract void live();	
}
