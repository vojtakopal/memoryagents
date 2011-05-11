package memagents.agents;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import memagents.food.FoodGenerator;
import memagents.memory.Memory;
import memagents.utils.Monitor;

/**
 * 
 * @author Vojtech
 *
 */
public abstract class Agent {
	static private int nextId = 0;
	static protected int getNextId() { return nextId++; }
	
	private int id;
	
	private boolean dead = false;
	
	protected Point position = new Point();

	protected HashMap<Integer, Float> needs = new HashMap<Integer, Float>();
	
	protected ArrayList<Monitor> monitors = new ArrayList<Monitor>();
		
	/**
	 * 	Audition is a quality of an agent. It is a distance in which the agent hears world around him.
	 * 
	 */
	protected int audition = 30;

	/**
	 * 	Sight is a quality of an agent. It is a distance in which the agent sees world around him.
	 * 
	 */
	protected int sight = 30;
	
	public Agent() { 
		this.id = getNextId(); 
	}
	
	public int getId() { 
		return id; 
	}
	
	public boolean isDead() {
		return dead;
	}
	
	public Point getPosition() { 
		return position; 
	}
	
	public void setAudition(int value) {
		this.audition = value;
	}
	
	public void setSight(int value) {
		this.sight = value;
	}
	
	public float getNeed(int foodKind) {
		float need = 0;
		
		if (needs.containsKey(foodKind)) {
			need = needs.get(foodKind);
		} else {
			needs.put(foodKind, 0.0f);
		}

		if (need > 1) kill();
		return need;
	} 
	
	public void setNeed(int foodKind, float amount) {
		needs.put(foodKind, amount);
		if (amount > 1) kill();
	}
	
	public void multiplyNeed(int foodKind, float multiplier) {
		float need = getNeed(foodKind);
		need *= multiplier;
		setNeed(foodKind, need);
		if (need > 1) kill();
	}
	
	public void kill() {
		dead = true;
	}
	
	/**
	 * Returns square of distance between points p1 and p2.
	 * 
	 * @param p1
	 * @param p2
	 * @return
	 */
	public int getQDistance(Point p1, Point p2) {
		int qDistance = (p1.x - p2.x)*(p1.x - p2.x) + (p1.y - p2.y)*(p1.y - p2.y);
		return qDistance;
	}
	
	/**
	 * Returns the nearest point to toPoint chosen from array fromPoints
	 * 
	 * @param fromPoint
	 * @param toPoints
	 * @return
	 */
	public Point getNearestPoint(Point toPoint, Point[] fromPoints) {
		Point nearestPoint = null;
		int leastQDistance = Integer.MAX_VALUE;
		int qDistance = 0;
		
		for (Point point : fromPoints) {
			qDistance = getQDistance(point, toPoint);
			if (qDistance < leastQDistance) {
				leastQDistance = qDistance;
				nearestPoint = point;
			}
		}
		
		return nearestPoint;
	}
	
	/**
	 * Returns the nearest point to toPoint chosen from arraylist fromPoints
	 * 
	 * @param fromPoint
	 * @param toPoints
	 * @return
	 */
	public Point getNearestPoint(Point toPoint, ArrayList<Point> fromPoints) {
		Point nearestPoint = null;
		int leastQDistance = Integer.MAX_VALUE;
		int qDistance = 0;
		
		for (Point point : fromPoints) {
			qDistance = getQDistance(point, toPoint);
			if (qDistance < leastQDistance) {
				leastQDistance = qDistance;
				nearestPoint = point;
			}
		}
		
		return nearestPoint;
	}
	
	
	public void addMonitor(Monitor monitor) {
		monitors.add(monitor);
	}
	
	public void processMonitors() {
		for (Monitor monitor : monitors) {
			monitor.monitor(this);
		}
	}
	
	public abstract Point[] whereIs(int foodKind);	
	public abstract void live();	
	public abstract void draw(Graphics g, int width, int height);
	public abstract Memory getMemory();
}
