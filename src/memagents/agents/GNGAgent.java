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
import memagents.memory.gng.LineGNG;
import memagents.memory.gng.NodeGNG;
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
	public static int MEMORY_SIZE = 100;
	
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
		
		memory = new GNGMemory(MEMORY_SIZE, MEMORY_SIZE);
		position.x = 50;
		position.y = 50;
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
		Environment env = (Environment) this.simulation.getEnvironment();
		
		int mostDeservedFood = findMostDeservedFood();
		learn(mostDeservedFood);
		
		if (mostDeservedFood != -1) {
			// find the food!
			Point[] sample = memory.getSample(mostDeservedFood);
			Point[] points = null;
			boolean dontKnowWhereToGo = false;
			
			int knownPointsSize = knownPoints.get(mostDeservedFood).size();
			if (knownPointsSize > 0) {
				points = knownPoints.get(mostDeservedFood).toArray(new Point[knownPointsSize]);
			} else {
				Point expectedCenter = memory.getExpectedCenter(mostDeservedFood);
				float qDistance = (expectedCenter.x - getX())*(expectedCenter.x - getX()) + (expectedCenter.y - getY())*(expectedCenter.y - getY());
				if (qDistance > sight*sight) {
					points = new Point[] { expectedCenter };
				} else {
					// don't know where to go
					//ArrayList<Point> allAnsweredPoints
					
					points = new Point[] {};
				}
			}
			
			// nearest?
			Point nearestPoint = null;
			float leastQDistance = Float.MAX_VALUE;
			for (Point point : points) {
				float qDistance = ((point.x - (float)getX())*(point.x - (float)getX())+(point.y - (float)getY())*(point.y - (float)getY()));
				if (qDistance < leastQDistance) {
					leastQDistance = qDistance;
					nearestPoint = point;
				}
			}
		
			if (nearestPoint != null && leastQDistance > 0) {
				// move there
				leastQDistance = Float.MAX_VALUE;
				Point bestMove = null;
				ArrayList<Point> moves = simulation.getEnvironment().filterMoves(position, availableMoves());
				for (Point move : moves) {
					float newX = move.x + position.x;
					float newY = move.y + position.y;
					float qDistance = ((newX - nearestPoint.x)*(newX - nearestPoint.x)+(newY - nearestPoint.y)*(newY - nearestPoint.y));
					if (qDistance < leastQDistance) {
						leastQDistance = qDistance;
						bestMove = move;
					}
				}
				
				if (bestMove != null) {
					move(bestMove);
				} else {
					// dont't know where to go
					dontKnowWhereToGo = true;
				}
			} else {
				// dont't know where to go
				dontKnowWhereToGo = true;
			}
			
			if (dontKnowWhereToGo) {
				super.live();				
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
				value -= 5;
				if (value < 0) value = 0;
				needs.put(mostDeservedFood, value);
			}
		}
		
		// put information about products into memory
//		for ( EnvironmentObject object : env.getNeighbours(this.position) )
//		{
//			if ( object instanceof Product )
//			{
//				// remember it
//				Log.println("memory "+this.id+" Product "+this.position[0]+" "+this.position[1]);
//				memory.add(new MemoryItem(this.position, (Product) object));
//			}
//		}
		
		// and move
		// super.live();
		
		// fade out
//		memories.doAge();
				
//		// agents around
//		for (Agent otheragent : simulation.getAgents()) {
//			if (otheragent instanceof MemoryAgent && otheragent != this) {
//				MemoryAgent otherMemoryAgent = (MemoryAgent)otheragent;
//				double distance = Math.sqrt((otherMemoryAgent.getX() - this.getX())*(otherMemoryAgent.getX() - this.getX()) + (otherMemoryAgent.getY() - this.getY())*(otherMemoryAgent.getY() - this.getY()));
//				if (distance <= audition) { 
//					memories.beInfluenced(otherMemoryAgent.memories);
//				} 
//			}
//		}
//
//		// learn new information
//		for (int i = Math.max(getX() - sight, 0); i < Math.min(getX() + sight, TestAgent.MEMORY_SIZE); i++) {
//			for (int j = Math.max(getY() - sight, 0); j < Math.min(getY() + sight, TestAgent.MEMORY_SIZE); j++) {
//				double distanceSquare = ((i - getX()) * (i - getX()) + (j - getY()) * (j - getY()));
//				if (distanceSquare < sight * sight) {
//					int value = simulation.getEnvironment().get(i, j).size();
//					memories.doLearn(BeliefType.MRKEV, i, j, value);
//				}
//			}
//		}
//		
//		// agent moves
//		// TODO: doplnit nahodny pohyb, pokud neni uspokojující zradlo v dosahu
//		// mit to jako moznost
//		ArrayList<double[]> whereToGo = new ArrayList<double[]>();
//		double distanceImportance = 0.01;
//		
//		for (int i = 0; i < Agent.MEMORY_SIZE; i++) {
//			for (int j = 0; j < TestAgent.MEMORY_SIZE; j++) {
//				double distance = Math.sqrt((i - this.getX())*(i - this.getX()) + (j - this.getY())*(j - this.getY()));
//				//if (distance <= sight) {
//					Chunk chunk = memories.getBeliefChunkAt(BeliefType.MRKEV, i, j);
//					if (chunk.amount > 0) {
//						double value = chunk.belief * chunk.amount * chunk.amount - distance * distanceImportance;
//						whereToGo.add(new double[] {i, j, value});
//					}
//				//}
//			}
//		}
//		
//		double bestX = -1;
//		double bestY = -1;
//		double bestValue = 0;
//		
//		for (double[] spot : whereToGo) {
//			if (spot[2] > bestValue) {
//				bestValue = spot[2];
//				bestX = spot[0];
//				bestY = spot[1];
//			}
//		}
//		
//		// move there
//		if (bestX > -1 && bestY > -1) {
//			// find closest cell 
//			int closestY = this.getY();
//			int closestX = this.getX();
//			double closestDistance = Double.MAX_VALUE;
//			for (int i = -1; i < 2; i++) {
//				for (int j = -1; j < 2; j++) {
//					int X = this.getX() + i;
//					int Y = this.getY() + j;
//					double distance = Math.sqrt((X - bestX)*(X - bestX) + (Y - bestY)*(Y - bestY));
//					
//					if (distance < closestDistance) {
//						closestDistance = distance;
//						closestX = X;
//						closestY = Y;
//					}
//				}
//			}
//			
//			this.setX(closestX);
//			this.setY(closestY);		
//			
//			simulation.getEnvironment().removeAll(closestX, closestY);
//			Chunk chunk = memories.getBeliefChunkAt(BeliefType.MRKEV, closestX, closestY);
//			chunk.amount = 0;
//			chunk.belief = 1;
//		} else {
//			if (simulation.getSettings().useRandomMovement) {
//				super.live();
//			}
//		}
		
		/*

		int randX = (int)(Math.random()*getCols());
		int randY = (int)(Math.random()*getRows());
		
		touch(randX, randY);
		
		*/
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
		for (int i = Math.max(getX() - sight, 0); i < Math.min(getX() + sight, MEMORY_SIZE); i++) {
			for (int j = Math.max(getY() - sight, 0); j < Math.min(getY() + sight, MEMORY_SIZE); j++) {
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
		
		/// do i need food and dont know any point
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
		Log.print("agent " + id + " asked about " + foodKind + " and anwsered ");
		
		for (Point point : points) {
			Log.print(" " + point.x + "," + point.y);
		}
		
		Log.println();
		
		return points;
	}
	
	synchronized public void draw(Graphics g, int width, int height) {

		double xV = width / (double)memory.getWidth();
		double yV = height / (double)memory.getHeight();
		Set<Integer> keys = knownPoints.keySet();
		for (Integer foodKind : keys) {
			for (Point p : knownPoints.get(foodKind)) {
				g.setColor(simulation.getGenerator(foodKind).getColor());
				g.fillOval((int)(xV*p.x - 1), (int)(yV*p.y - 1), 2, 2);
			}
		}
		
		for (LineGNG line : memory.getLines()) {
			g.setColor(Color.GRAY);
			g.drawLine((int)(xV*line.getX1()), (int)(yV*line.getY1()), (int)(xV*line.getX2()), (int)(yV*line.getY2()));
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
		
		for (Point center : memory.getExpectedCenters()) {
			// draw expectations
			g.setColor(Color.RED);
			g.drawOval((int)(xV*(center.x - 10)), 
					   (int)(yV*(center.y - 10)), 
					   (int)xV*20, 
					   (int)yV*20);
		}
		
		// draw bars
		int w = 3;
		for (int foodKind = 0; foodKind < FoodGenerator.getSize(); foodKind++) {
			
			float value = this.getNeed(foodKind);
			g.setColor(simulation.getGenerator(foodKind).getColor());			
			g.fillRect(0 + w*foodKind, 0, w, (int)(value*50));
			
		}
		
		
	}
}
