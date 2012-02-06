package memagents.agents;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import memagents.Simulation;
import memagents.environment.Environment;
import memagents.food.FoodGenerator;
import memagents.memory.ExpectedGauss;
import memagents.memory.IMemory;
import memagents.memory.Memory;
import memagents.utils.Log;
import memagents.utils.Monitor;

/**
 * Abstract class representing an agent. It defines all the behavior except for the used memory which
 * should be define in the extending class.
 * 
 * @author Vojtech Kopal 
 *
 */
public abstract class Agent {
	/**
	 * 	Each agent is given unique id, which is determined from this static variable.
	 * 
	 */
	static private int nextId = 0;
	
	/**
	 *	Return next available agent's id.
	 * 	
	 * 	@return agent's id
	 */
	static private int getNextId() { return nextId++; }
	
	/** 
	 * 	Current agent's id. A unique identifier.
	 * 
	 */
	protected int id;
	
	/**
	 * 	Flag whether the agent is dead or not. It is to true when a variable of need
	 * 	is accessed and it is above the threshold.
	 * 
	 */
	private boolean dead = false;
	
	/**
	 * 	Agent's position. Use public methods getX(), setX(int), getY(), setY(int) to access the values. 
	 * 
	 */
	protected final Point position = new Point();

	/**
	 * 
	 */
	protected boolean walkingRandomly = false;
	
	/**
	 * 
	 */
	protected Point randomWalkDestionation = new Point();
	
	/**
	 * 	Agent's needs. The key is foodKind and value is float [0, 1]
	 * 
	 */
	protected HashMap<Integer, Float> needs = new HashMap<Integer, Float>();
	
	/**
	 *  Each agent can have a couple of monitors. Those are classes that
	 *  monitors agent's internal variables and eventually store them into
	 *  log file.
	 * 
	 */
	protected ArrayList<Monitor> monitors = new ArrayList<Monitor>();
	
	/**
	 * 	Those are known food locations, e.g. locations that are directly seen by the agent, or
	 * 	agent had asked about them.
	 * 
	 */
	protected HashMap<Integer, ArrayList<Point>> knownFoodLocations = new HashMap<Integer, ArrayList<Point>>();
	
	/**
	 * 	Audition is a quality of an agent. It is a distance in which the agent hears world around him.
	 * 	Default 30, but it is usually set according to simulation settings when the agent class is instanced.
	 * 
	 */
	protected int audition = 30;

	/**
	 * 	Sight is a quality of an agent. It is a distance in which the agent sees world around him.
	 * 	Default 30, but it is usually set according to simulation settings when the agent class is instanced.
	 * 
	 */
	protected int sight = 30;

	/**
	 *  A reference of simulation in which the agent lives.
	 *  
	 */
	protected Simulation simulation;
	
	protected boolean muted = false;
	
	public Agent(Simulation simulation) { 
		this.id = getNextId(); 
		this.simulation = simulation;
		
		position.x = Simulation.SIZE / 2; //simulation.getRandom().nextInt(Simulation.SIZE);
		position.y = Simulation.SIZE / 2; //simulation.getRandom().nextInt(Simulation.SIZE);
	}
	
	/**	
	 * 	Getter. Agent's unique identifier.
	 * 
	 */
	public int getId() { 
		return id; 
	}
	
	/**
	 * 	Getter. Flag whether the agent is dead or not. It is to true when a variable of need
	 * 	is accessed and it is above the threshold.
	 * 
	 */
	public boolean isDead() {
		return dead;
	}
	
	/**
	 * 	Getter. Agent's X position.
	 * 
	 */
	public int getX() { 
		return position.x; 
	}

	/**
	 * 	Setter. Agent's X position.
	 * 
	 */
	public void setX(int value) { 
		position.x = value; 
	}

	/**
	 * 	Getter. Agent's Y position.
	 * 
	 */
	public int getY() { 
		return position.y; 
	}

	/**
	 * 	Setter. Agent's Y position.
	 * 
	 */
	public void setY(int value) { 
		position.y = value; 
	}
	
	/**
	 * 	Getter. Agent's position.
	 * 
	 */
	public Point getPosition() { 
		return position; 
	}
	
	/**
	 * 	Setter. Agent's audition.
	 * 
	 * 	Audition is a quality of an agent. It is a distance in which the agent hears world around him.
	 * 	Default 30, but it is usually set according to simulation settings when the agent class is instanced.
	 * 
	 */
	public void setAudition(int value) {
		this.audition = value;
	}
	
	/**
	 * 	Setter. Agent's sight.
	 * 
	 * 	Sight is a quality of an agent. It is a distance in which the agent sees world around him.
	 * 	Default 30, but it is usually set according to simulation settings when the agent class is instanced.
	 * 
	 */ 
	public void setSight(int value) {
		this.sight = value;
	}

	/**
	 * 	Getter. Agent's sight.
	 * 
	 * 	Sight is a quality of an agent. It is a distance in which the agent sees world around him.
	 * 	Default 30, but it is usually set according to simulation settings when the agent class is instanced.
	 * 
	 */ 
	public int getSight() {
		return this.sight;
	}
	
	/**
	 * 	Returns current value of need of specified food kind.
	 * 
	 */
	public float getNeed(int foodKind) {
		float need = 0;
		
		if (needs.containsKey(foodKind)) {
			need = needs.get(foodKind);
		} else {
			needs.put(foodKind, 0.0f);
		}

		if (need >= 1) { 
			kill(); need = 1; 
		}
		return need;
	} 
	
	/**
	 * 	
	 */
	public void setNeed(int foodKind, float amount) {
		if (amount >= 1) {
			kill();
			amount = 1;
		}		
		needs.put(foodKind, amount);
	}
	
	public String getName() {
		return "";
	}
	
	/**
	 * 
	 * 
	 * @param foodKind
	 * @param multiplier
	 */
	public void multiplyNeed(int foodKind, float multiplier) {
		float need = getNeed(foodKind);
		need *= multiplier;
		setNeed(foodKind, need);
		if (need > 1) kill();
	}
	
	/**
	 * 
	 */
	public void kill() {
		dead = true;
	}
	
	public Agent mute() {
		muted = true;
		
		return this;
	}
	
	/**
	 * Returns square of distance between points p1 and p2.
	 * 
	 * @param p1
	 * @param p2
	 * @return
	 */
	public int getQDistance(Point p1, Point p2) {
		if (p1 == null || p2 == null) return Integer.MAX_VALUE;
		
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
	
	public Point[] whereIs(int foodKind) {
		
		Point[] points = null;
		
		// look around
		points = getSeenFood(foodKind);
		
		// only when I do not see any 
		if (points == null || points.length == 0) {
			points = getMemory().getSample(foodKind);
		}
		
		
		//Log.print("agent " + id + " asked about " + foodKind + " and anwsered ");
		
//		if (points != null) {
//			for (Point point : points) {
//				if (point != null) {
//					Log.print(" " + point.x + "," + point.y);
//				}
//			}
//		}
//		
//		Log.println();
		
		return points;
	}
	
	public Point[] getSeenFood(int foodKind) {
		Point[] points = null;
		
		if (knownFoodLocations.containsKey(foodKind)) {
			int size = knownFoodLocations.get(foodKind).size();
			
			if (size > 0) {
			
				ArrayList<Point> knownPoints = knownFoodLocations.get(foodKind);
				points = new Point[Simulation.ANSWER_SAMPLE];
				
				for (int i = 0; i < Simulation.ANSWER_SAMPLE; i++) {
					points[i] = knownPoints.get(simulation.getRandom().nextInt(size));
				}
				
			}
		}
		
		return points;
	}

	/**
	 * 	First before each step an agent looks around and
	 * 	learns what he sees.
	 * 
	 */
	synchronized public void lookAround() {
		if (isDead()) return;		

		Environment environment = simulation.getEnvironment();
		
		// reset what agents sees
		knownFoodLocations.clear();
		for (int foodKind = 0; foodKind < FoodGenerator.getSize(); foodKind++) {
			knownFoodLocations.put(foodKind, new ArrayList<Point>());
		}
		
		// learn new things i see
		for (int i = Math.max(getX() - sight, 0); i < Math.min(getX() + sight, Simulation.SIZE); i++) {
			for (int j = Math.max(getY() - sight, 0); j < Math.min(getY() + sight, Simulation.SIZE); j++) {
				/// can I see it?
				///
				double distanceSquare = ((i - getX()) * (i - getX()) + (j - getY()) * (j - getY()));				
				if (distanceSquare < sight * sight) {
					HashMap<Integer, Integer> location = environment.getAllFoodAt(i, j);
					if (location == null) continue;
					
					for (int foodKind = 0; foodKind < FoodGenerator.getSize(); foodKind++) {
						if (false == location.containsKey(foodKind)) {
							location.put(foodKind, 0);
						}
						
						int amount = location.get(foodKind);
						if (amount > 0) {
							knownFoodLocations.get(foodKind).add(new Point(i, j));
						}
					}
				}
			}
		}
	}
	
	/**
	 * 	Agents live method which is called each step of the simulation.
	 * 
	 */
	synchronized public void live() {
		if (isDead()) return;
		
		boolean hasMoved = false;
		Environment environment = simulation.getEnvironment();
		
		int mostDeservedFood = findMostDeservedFood();
		
		putKnownFoodLocationsIntoMemory(mostDeservedFood);
		
		if (mostDeservedFood != -1) {
			// find the food!
			Point[] points = getSeenFoodLocationsOrExpectedCenter(mostDeservedFood);
			
			// nearest?
			Point nearestPoint = getNearestPoint(position, points);	
			if (nearestPoint != null) {
				// moves are relative, e.g. [(1,1), (-1,0), ...]
				ArrayList<Point> moves = environment.filterMoves(position, availableMoves());
				
				// best move is the one which moves agent nearer
				Point bestMove = getNearestPoint(new Point(nearestPoint.x - position.x, nearestPoint.y - position.y), moves);
				
				if (bestMove != null) {
					move(bestMove);
					walkingRandomly = false;
					hasMoved = true;
				} 
			} 
						
		}
		
		if (hasMoved == false) {
			moveRandomly();
			walkingRandomly = true;
		}
		
		// 
		if ( mostDeservedFood != -1 ) {
			// eat it!
			eatFood(mostDeservedFood);
		}
		
	}
	
	private Point[] getSeenFoodLocationsOrExpectedCenter(int foodKind) {
		Point[] points = null;
		
		int knownPointsSize = knownFoodLocations.get(foodKind).size();
		if (knownPointsSize > 0) {
			points = knownFoodLocations.get(foodKind).toArray(new Point[knownPointsSize]);
		} else {
			Point expectedCenter = getMemory().getExpectedGauss(foodKind);
			float qDistance = getQDistance(position, expectedCenter);
			if (qDistance > sight*sight) {
				points = new Point[] { expectedCenter };
			} else {
				// don't know where to go				
				points = new Point[] {};
			}
		}
		
		return points;
	}
	
	private void eatFood(int foodKind) {
		if (foodKind == -1) return;
		
		if (this.simulation.getEnvironment().eatFoodAt(getX(), getY(), foodKind)) {
			float value = needs.get(foodKind);
			value *= 0.4;
			if (value < 0) value = 0;
			needs.put(foodKind, value);
		}
	}
	
	private void putKnownFoodLocationsIntoMemory(int mostDeservedFood) {
		boolean knowPointFor_MostDeservedFood = false;
			
		if (knownFoodLocations.containsKey(mostDeservedFood) && knownFoodLocations.get(mostDeservedFood).size() > 0) {
			knowPointFor_MostDeservedFood = true;
		}
		
		if (muted == false) {
			// do i need food and dont know any point where it is
			if (mostDeservedFood != -1 && !knowPointFor_MostDeservedFood) {
				for (Agent agent : this.simulation.getAgents()) {
					if (agent.isDead()) continue;
					if (agent.muted) continue;
					
					double qDistanceToAgent = getQDistance(position, agent.getPosition());
					if (qDistanceToAgent < audition*audition && qDistanceToAgent < agent.audition*agent.audition) {
						Point[] answeredPoints = agent.whereIs(mostDeservedFood);
						if (answeredPoints != null) {
							for (Point point : answeredPoints) {
								if (point == null) continue;
								
								// but it has to be out of my sight
								double qDistanceToAnswer = getQDistance(point, position);
								if (qDistanceToAnswer > sight*sight) {
									knownFoodLocations.get(mostDeservedFood).add(point);
								}
							}
						}
					}
				} 
			}
		}
		
		for (int foodKind = 0; foodKind < FoodGenerator.getSize(); foodKind++) {
			getMemory().learn(foodKind, knownFoodLocations.get(foodKind));
		}
	}

	private int findMostDeservedFood() {
		int mostDeservedFood = -1;
		double mostDeservedFoodNeed = 0;
		for (int i = 0; i < FoodGenerator.getSize(); i++) {
			int foodKind = (id + i) % FoodGenerator.getSize();
			double foodNeed = this.getNeed(foodKind);
			if (simulation.getGenerator(foodKind).isOverThreshold(foodNeed)) {
				if (foodNeed > mostDeservedFoodNeed) {
					mostDeservedFoodNeed = foodNeed;
					mostDeservedFood = foodKind;
				}
			}
		}
		return mostDeservedFood;
	}

	public abstract IMemory getMemory();
	
	protected ArrayList<Point> availableMoves()
	{
		ArrayList<Point> moves = new ArrayList<Point>();
		
		moves.add(new Point(-1, 0));
		moves.add(new Point( 1, 0));
		moves.add(new Point( 0,-1));
		moves.add(new Point( 0, 1));
		
		return moves;
	}	
	
	protected void move(Point change) {
		position.x += change.x;
		position.y += change.y;
	}
	
	protected void moveRandomly() {
		
		if (walkingRandomly == false) {
			// reset random destination
			randomWalkDestionation = new Point(simulation.getRandom().nextInt(simulation.getSize()), simulation.getRandom().nextInt(simulation.getSize()));
		}

		if (randomWalkDestionation.x == position.x && randomWalkDestionation.y == position.y) {
			randomWalkDestionation = new Point(simulation.getRandom().nextInt(simulation.getSize()), simulation.getRandom().nextInt(simulation.getSize()));
		}
		
		Environment environment = simulation.getEnvironment();
		ArrayList<Point> randomMoves = environment.filterMoves(position, availableMoves());
		//int rand = (int)(randomMoves.size() * simulation.getRandom().nextDouble());
		
		if (randomMoves.size() > 0) {
			Point nextPosition = getNearestPoint(new Point(randomWalkDestionation.x - position.x, randomWalkDestionation.y - position.y), randomMoves);
			move(nextPosition);
		}		
	}
		
	public Agent addMonitor(Monitor monitor) {
		monitors.add(monitor);
		
		return this;
	}
	
	public void processMonitors(int step) {
		for (Monitor monitor : monitors) {
			monitor.monitor(this, step);
		}
	}
	
	/**
	 * 	Draws current agent's perception of the environment into the graphic canvas. 
	 * 
	 */
	synchronized public void draw(Graphics g, int width, int height) {
		
		if (isDead()) {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, width, height);
			return;
		}
		
		double xV = width / (double)getMemory().getWidth();
		double yV = height / (double)getMemory().getHeight();

		HashMap<Integer, ExpectedGauss> gausses = getMemory().getExpectedGausses();
		
		if (gausses != null) {
			for (int foodKind = 0; foodKind < FoodGenerator.getSize(); foodKind++) {
				if (gausses.containsKey(foodKind) == false) continue;
				
				// draw expectations
				ExpectedGauss gauss = gausses.get(foodKind);
				
				if (gauss != null) {
					g.setColor(simulation.getGenerator(foodKind).getColor());
					g.drawOval((int)(xV*(gauss.x - gauss.var/2)), 
							   (int)(yV*(gauss.y - gauss.var/2)), 
							   (int)(xV*gauss.var), 
							   (int)(yV*gauss.var));
				}
			}
		}
		
		// draw bars
		int w = 3;
		for (int foodKind = 0; foodKind < FoodGenerator.getSize(); foodKind++) {
			
			float value = this.getNeed(foodKind);
			g.setColor(simulation.getGenerator(foodKind).getColor());			
			g.fillRect(0 + w*foodKind, 0, w, (int)(value*height));
			
		}
		
		Set<Integer> keys = knownFoodLocations.keySet();
		for (Integer foodKind : keys) {
			for (Point p : knownFoodLocations.get(foodKind)) {
				g.setColor(simulation.getGenerator(foodKind).getColor());
				g.fillOval((int)(xV*p.x - 1), (int)(yV*p.y - 1), (int)(xV*2), (int)(yV*2));
			}
		}
		
	}
}
