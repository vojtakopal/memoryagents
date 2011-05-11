package memagents.memory;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import memagents.Simulation;
import memagents.memory.quad.QuadNode;

public class QuadMemory extends Memory {

	/**
	 * Max levels of the quadtree.
	 * 
	 */
	protected int maxLevels = 4;
		
	protected HashMap<Integer, QuadNode> roots;
	
	public QuadMemory(int width, int height, Simulation simulation) {
		super(width, height, simulation);
		
		roots = new HashMap<Integer, QuadNode>();
	}

	public void run() {

	}

	public void learn(int foodKind, ArrayList<Point> food) {

	}

	public Point[] getSample(int foodKind) {
		return null;
	}

}
