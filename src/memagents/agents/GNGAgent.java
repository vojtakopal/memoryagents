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
import memagents.memory.GNGMemory;
import memagents.memory.Memory;
import memagents.memory.gng.EdgeGNG;
import memagents.memory.gng.NodeGNG;
import memagents.memory.gng.NodePair;
import memagents.utils.Log;

/**
 * 
 * TODO: Agenti musi videt kolem sebe.
 * 
 * 
 * @author Vojta
 *
 */
public class GNGAgent extends RandomAgent
{	
	/**
	 *	Memory is represented by two-dimensional array of double.
	 *
	 */
	//private double[][] memory = new double[MEMORY_SIZE][MEMORY_SIZE];
//	protected MemoryAgentMemories memories = new MemoryAgentMemories();
	private GNGMemory memory;
	
	private HashMap<Integer, ArrayList<Point>> knownPoints =
		new HashMap<Integer, ArrayList<Point>>();
		
	/**
	 * 	Belief.	
	 * 
	 *  "Do you believe in the Hogfather, Hex?"
	 */
	//private HashMap<Integer, double[][]> believes = new HashMap<Integer, double[][]>();
		
	public GNGAgent(Simulation simulation) 
	{
		super(simulation);
		
		memory = new GNGMemory(Simulation.SIZE, Simulation.SIZE, simulation);
		position.x = simulation.getRandom().nextInt(Simulation.SIZE);
		position.y = simulation.getRandom().nextInt(Simulation.SIZE);
	}
	
	public int getX() { return position.x; }
	public void setX(int value) { position.x = value; }
	
	public int getY() { return position.y; }
	public void setY(int value) { position.y = value; }

//	public Chunk getMemoryChunkAt(BeliefType type, int x, int y) {
//		return memories.getBeliefChunkAt(type, x, y);
//	}
	
	synchronized public void live() 
	{
		if (isDead()) return;
		
		Environment env = (Environment) this.simulation.getEnvironment();
		
		int mostDeservedFood = findMostDeservedFood();
		learn(mostDeservedFood);
		
		if (mostDeservedFood != -1) {
			// find the food!
			Point[] points = null;
			
			int knownPointsSize = knownPoints.get(mostDeservedFood).size();
			if (knownPointsSize > 0) {
				points = knownPoints.get(mostDeservedFood).toArray(new Point[knownPointsSize]);
			} else {
				Point expectedCenter = memory.getExpectedCenter(mostDeservedFood);
				float qDistance = getQDistance(position, expectedCenter);
				if (qDistance > sight*sight) {
					points = new Point[] { expectedCenter };
				} else {
					// don't know where to go
					//ArrayList<Point> allAnsweredPoints
					
					points = new Point[] {};
				}
			}
			
			// nearest?
			Point nearestPoint = getNearestPoint(position, points);	
			
			if (nearestPoint != null) {
				// moves are relative, e.g. [(1,1), (-1,0), ...]
				ArrayList<Point> moves = simulation.getEnvironment().filterMoves(position, availableMoves());
				Point bestMove = getNearestPoint(new Point(nearestPoint.x - position.x, nearestPoint.y - position.y), moves);
				
				if (bestMove != null) {
					move(bestMove);
				} else {
					// dont't know where to go
					super.live();				
				}
			}
						
		} else {
			// random, no needs
			super.live();
		}
		
		// 
		if ( mostDeservedFood != -1 ) {
			
			// eat it!
			if (this.simulation.getEnvironment().eatFoodAt(getX(), getY(), mostDeservedFood)) {
				float value = needs.get(mostDeservedFood);
				value -= 0.5;
				if (value < 0) value = 0;
				needs.put(mostDeservedFood, value);
			}
		}

//		// agent moves
//		// TODO: doplnit nahodny pohyb, pokud neni uspokojující zradlo v dosahu
//		// mit to jako moznost

		
		
	}
	
	private void learn(int mostDeservedFood) {
		Environment env = (Environment) this.simulation.getEnvironment();
		boolean knowPointFor_MostDeservedFood = false;
		
		/// reset what agents sees
		///
		knownPoints.clear();
		for (int foodKind = 0; foodKind < FoodGenerator.getSize(); foodKind++) {
			knownPoints.put(foodKind, new ArrayList<Point>());
		}
		
		/// learn new things i see
		///
		for (int i = Math.max(getX() - sight, 0); i < Math.min(getX() + sight, Simulation.SIZE); i++) {
			for (int j = Math.max(getY() - sight, 0); j < Math.min(getY() + sight, Simulation.SIZE); j++) {
				/// can I see it?
				///
				double distanceSquare = ((i - getX()) * (i - getX()) + (j - getY()) * (j - getY()));				
				if (distanceSquare < sight * sight) {
					HashMap<Integer, Integer> location = env.getAllFoodAt(i, j);
					if (location == null) continue;
					
					for (int foodKind = 0; foodKind < FoodGenerator.getSize(); foodKind++) {
						if (false == location.containsKey(foodKind)) {
							location.put(foodKind, 0);
						}
						
						int amount = location.get(foodKind);
						if (amount > 0) {
							knownPoints.get(foodKind).add(new Point(i, j));
							if (foodKind == mostDeservedFood) {
								knowPointFor_MostDeservedFood = true;
							}
						}
					}
				}
			}
		}
		
		/// do i need food and dont know any point where it is
		///
		if (mostDeservedFood != -1 && !knowPointFor_MostDeservedFood) {
			for (Agent agent : this.simulation.getAgents()) {
				double qDistanceToAgent = (agent.getPosition().getX() - getX())*(agent.getPosition().getX() - getX()) + (agent.getPosition().getY() - getY())*(agent.getPosition().getY() - getY()); 
				if (qDistanceToAgent < audition*audition) {
					Point[] answeredPoints = agent.whereIs(mostDeservedFood);
					for (Point point : answeredPoints) {
						knownPoints.get(mostDeservedFood).add(point);
					}
				}
			} 
		}
		
		for (int foodKind = 0; foodKind < FoodGenerator.getSize(); foodKind++) {
			memory.learn(foodKind, knownPoints.get(foodKind));
		}
	}

	private int findMostDeservedFood() {
		int mostDeservedFood = -1;
		double mostDeservedFoodNeed = 0;
		for (int foodKind = 0; foodKind < FoodGenerator.getSize(); foodKind++) {
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
	
	public Point[] whereIs(int foodKind) {
		Point[] points = memory.getSample(foodKind);
		//Log.print("agent " + id + " asked about " + foodKind + " and anwsered ");
		
		for (Point point : points) {
			Log.print(" " + point.x + "," + point.y);
		}
		
		Log.println();
		
		return points;
	}
	
	public Memory getMemory() {
		return memory;
	}
	
	synchronized public void draw(Graphics g, int width, int height) {

		if (isDead()) {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, width, height);
			return;
		}
		
		double xV = width / (double)memory.getWidth();
		double yV = height / (double)memory.getHeight();
		Set<Integer> keys = knownPoints.keySet();
		for (Integer foodKind : keys) {
			for (Point p : knownPoints.get(foodKind)) {
				g.setColor(simulation.getGenerator(foodKind).getColor());
				g.fillOval((int)(xV*p.x - 1), (int)(yV*p.y - 1), (int)(xV*2), (int)(yV*2));
			}
		}
		
		for (NodePair edge : memory.getEdges()) {
			g.setColor(Color.GRAY);
			g.drawLine((int)(xV*edge.first.getX()), (int)(yV*edge.first.getY()), (int)(xV*edge.second.getX()), (int)(yV*edge.second.getY()));
		}
		
		for (int foodKind : memory.getNodes().keySet()) {
			NodeGNG[] nodes = memory.getNodes().get(foodKind);
			for (NodeGNG node : nodes) {
//				int xxx = node.x;
//				int yyy = node.y;
				if (node != null) {
					g.setColor(simulation.getGenerator(foodKind).getColor());
					g.fillOval((int)(xV*(node.getX() - 2)), 
							   (int)(yV*(node.getY() - 2)), 
							   (int)xV*4, 
							   (int)yV*4);
					g.setColor(Color.BLACK);
					g.drawOval((int)(xV*(node.getX() - 2)), 
							   (int)(yV*(node.getY() - 2)), 
							   (int)xV*4, 
							   (int)yV*4);
				}
			}
		}

		HashMap<Integer, Point> centers = memory.getExpectedCenters();
		HashMap<Integer, Double> variances = memory.getExpectedVariances();
		
		for (int foodKind = 0; foodKind < FoodGenerator.getSize(); foodKind++) {
			// draw expectations
			Point center = centers.get(foodKind);
			double var = variances.get(foodKind);
			
			g.setColor(Color.RED);
			g.drawOval((int)(xV*(center.x - var/2)), 
					   (int)(yV*(center.y - var/2)), 
					   (int)(xV*var), 
					   (int)(yV*var));
		}
		
		// draw bars
		int w = 3;
		for (int foodKind = 0; foodKind < FoodGenerator.getSize(); foodKind++) {
			
			float value = this.getNeed(foodKind);
			g.setColor(simulation.getGenerator(foodKind).getColor());			
			g.fillRect(0 + w*foodKind, 0, w, (int)(value*height));
			
		}
		
		
	}
}
