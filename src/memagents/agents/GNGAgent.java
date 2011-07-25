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
public class GNGAgent extends Agent
{	
	/**
	 *	Memory is represented by two-dimensional array of double.
	 *
	 */
	//private double[][] memory = new double[MEMORY_SIZE][MEMORY_SIZE];
//	protected MemoryAgentMemories memories = new MemoryAgentMemories();
	private GNGMemory memory;
		
	/**
	 * 	Belief.	
	 * 
	 *  "Do you believe in the Hogfather, Hex?"
	 */
	//private HashMap<Integer, double[][]> believes = new HashMap<Integer, double[][]>();
		
	public GNGAgent(Simulation simulation) 
	{
		super(simulation);
		
		memory = new GNGMemory(Simulation.SIZE, Simulation.SIZE, simulation, this);
	}
	
	public String getName() {
		return "gng";
	}
		
	public Memory getMemory() {
		return memory;
	}
	
	synchronized public void draw(Graphics g, int width, int height) {
		
		double xV = width / (double)getMemory().getWidth();
		double yV = height / (double)getMemory().getHeight();
		
		
		for (NodePair edge : memory.getEdges()) {
			g.setColor(Color.GRAY);
			g.drawLine((int)(xV*edge.first.getX()), (int)(yV*edge.first.getY()), (int)(xV*edge.second.getX()), (int)(yV*edge.second.getY()));
		}
		
		for (int foodKind : memory.getNodes().keySet()) {
			NodeGNG[] nodes = memory.getNodes().get(foodKind);
			for (NodeGNG node : nodes) {
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

		super.draw(g, width, height);
		
	}
}
