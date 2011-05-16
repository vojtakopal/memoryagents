package memagents.memory;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import memagents.Simulation;
import memagents.agents.Agent;
import memagents.environment.Matrix;
import memagents.food.FoodGenerator;
import memagents.memory.gng.NodeGNG;
import memagents.memory.quad.QuadNode;

public class QuadMemory extends Memory {

	/**
	 * Max levels of the quadtree.
	 * 
	 */
	protected int maxLevels = 4;
	
	protected int cols = 0;
	
	protected int rows = 0;
	
	protected HashMap<Integer, Matrix<QuadNode>> matrices;
	
	protected HashMap<Integer, ExpectedGauss> expectedGausses;
	
	public QuadMemory(int width, int height, Simulation simulation, Agent agent) {
		super(width, height, simulation, agent);
		
		cols = 16;
		rows = 16;
		
		matrices = new HashMap<Integer, Matrix<QuadNode>>();
		
		for (int foodKind = 0; foodKind < FoodGenerator.getSize(); foodKind++) {
			matrices.put(foodKind, new Matrix<QuadNode>(cols, rows));
		}
		
		expectedGausses = new HashMap<Integer, ExpectedGauss>();
	}
	
	public int getCols() {
		return cols;
	}
	
	public int getRows() {
		return rows;
	}

	public Matrix<QuadNode> getMatrix(int foodKind) {
		return matrices.get(foodKind);
	}
	
	public void run() {
		// nothing
	}

	public void learn(int foodKind, ArrayList<Point> food) {
		Matrix<QuadNode> matrix = matrices.get(foodKind);
		
		Matrix<Boolean> hasInput = new Matrix<Boolean>(cols, rows);
		for (int i = 0; i < cols; i++) {
			for (int j = 0; j < rows; j++) {
				hasInput.set(i, j, false);
			}
		}
		
		for (Point point : food) {
			int x = point.x / 16;
			int y = point.y / 16;

			if (x >= 0 && x < cols && y >= 0 && y < rows) {
				hasInput.set(x, y, true);
			}
		}
		
		for (int i = 0; i < cols; i++) {
			for (int j = 0; j < rows; j++) {
				QuadNode node = getNodeAt(matrix, i, j);
				if (hasInput.get(i, j)) {
					getNodeAt(matrix, i, j).incPositive();
				} else {
					Point nodeCenter = new Point(i*16+8, j*16+8);
					if (agent.getQDistance(agent.getPosition(), nodeCenter) < agent.getSight()*agent.getSight()) {
						getNodeAt(matrix, i, j).incNegative();
					}
				}
			}
		}
		
		computeExpectedGauss(matrix);
	}
	
	protected void computeExpectedGauss(Matrix<QuadNode> matrix) {
		ExpectedGauss gauss = new ExpectedGauss();
		gauss.x = 0;
		gauss.y = 0;
		gauss.var = 0;
		
		long total = 0;
		for (int i = 0; i < cols; i++) {
			for (int j = 0; j < rows; j++) {
				QuadNode node = getNodeAt(matrix, i, j);
				
				total += node.getPositive();
				gauss.x += node.getPositive() * (i * 16 + 8);
				gauss.y += node.getPositive() * (j * 16 + 8);
			}
		}
		
		if (total == 0) {
			return;
		}
		
		gauss.x /= total;
		gauss.y /= total;

		for (int i = 0; i < cols; i++) {
			for (int j = 0; j < rows; j++) {
				QuadNode node = getNodeAt(matrix, i, j);
				
				double qDistance = agent.getQDistance(gauss, new Point((i * 16 + 8), (j * 16 + 8)));
				gauss.var += qDistance*node.getPositive();
			}
		}
		gauss.var /= total;
	}

	public QuadNode getNodeAt(Matrix<QuadNode> matrix, int x, int y) {
		QuadNode node = matrix.get(x, y);
		if (node == null) {
			node = new QuadNode();
			matrix.set(x, y, node);
		}
		return node;
	}
	
	public Point[] getSample(int foodKind) {
		Point[] sample = new Point[Simulation.ANSWER_SAMPLE];
		Matrix<QuadNode> matrix = matrices.get(foodKind);
		ExpectedGauss gauss = expectedGausses.get(foodKind);
		
		Random random = simulation.getRandom(); 
		int randomX = 0;
		int randomY = 0;
		
		for (int i = 0; i < Simulation.ANSWER_SAMPLE; i++) {
			if (gauss != null) {
				randomX = (int)(gauss.x + random.nextGaussian() * gauss.var);
				randomY = (int)(gauss.y + random.nextGaussian() * gauss.var);
			} else {
				randomX = random.nextInt(width);
				randomY = random.nextInt(height);
			}
			
			sample[i] = new Point(randomX, randomY);
		}
		
		return sample;
	}

	public HashMap<Integer, ExpectedGauss> getExpectedGausses() {
		return expectedGausses;
	}
}