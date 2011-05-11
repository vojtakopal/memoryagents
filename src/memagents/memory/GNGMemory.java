package memagents.memory;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import memagents.Simulation;
import memagents.food.FoodGenerator;
import memagents.memory.gng.EdgeGNG;
import memagents.memory.gng.MAComputeGNG;
import memagents.memory.gng.NodeGNG;
import memagents.memory.gng.NodePair;

public class GNGMemory extends Memory {
	
	protected HashMap<Integer, MAComputeGNG> gngEngines;
	protected Thread thread;
	
	public GNGMemory(int width, int height, Simulation simulation) {
		super(width, height, simulation);
		
		this.gngEngines = new HashMap<Integer, MAComputeGNG>();
		
		for (int foodKind = 0; foodKind < FoodGenerator.getSize(); foodKind++) {
			MAComputeGNG gngEngine = new MAComputeGNG(this);
			// default: 0.8 1.0E-5 0.0050 6.0E-4 600 8
			gngEngine.setParams(0.8f, 1.0E-5f, 0.005f, 6.0E-4f, 600, 88, 5);
			gngEngines.put(foodKind, gngEngine);
		}
		

//		thread = new Thread(this, "GNGMemory");
//		thread.start();
	}
	
	public void learn(int foodKind, ArrayList<Point> food) {
		gngEngines.get(foodKind).setDescreteSignals(food);
	}
	
	public Point[] getSample(int foodKind) {
		MAComputeGNG gngEngine = gngEngines.get(foodKind);
		gngEngine.computeExpectedDistribution();
		Point[] sample = new Point[numSamples];
		Random random = simulation.getRandom(); 
		int randomX = 0;
		int randomY = 0;
		
		for (int i = 0; i < numSamples; i++) {
			randomX = (int)(gngEngine.getExpectedValueX() + random.nextGaussian() * gngEngine.getExpectedVariance());
			randomY = (int)(gngEngine.getExpectedValueY() + random.nextGaussian() * gngEngine.getExpectedVariance());
			
			sample[i] = new Point(randomX, randomY);
		}
		
		return sample;
	}
	
	public Random getRandom() {
		return simulation.getRandom();
	}
	
	public HashMap<Integer, NodeGNG[]> getNodes() {
		HashMap<Integer, NodeGNG[]> nodes = new HashMap<Integer, NodeGNG[]>();
		
		for (int foodKind = 0; foodKind < FoodGenerator.getSize(); foodKind++) {
			MAComputeGNG gngEngine = gngEngines.get(foodKind);
			NodeGNG[] n = gngEngine.getNodes();
			nodes.put(foodKind, n);
		} 
		
		return nodes;
	}
	
	public ArrayList<NodePair> getEdges() {
		ArrayList<NodePair> lines = new ArrayList<NodePair>();
		
		for (int foodKind = 0; foodKind < FoodGenerator.getSize(); foodKind++) {
			MAComputeGNG gngEngine = gngEngines.get(foodKind);
			EdgeGNG[] n = gngEngine.getEdges();
			for (int i = 0; i < gngEngine.getNumEdges(); i++) {
				NodeGNG n1 = gngEngine.getNodeAt(n[i].getFrom());
				NodeGNG n2 = gngEngine.getNodeAt(n[i].getTo());
				
				lines.add(new NodePair(n1, n2));
			}
		} 
		
		return lines;
	}
	
	public HashMap<Integer, Point> getExpectedCenters() {
		HashMap<Integer, Point> centers = new HashMap<Integer, Point>();
		for (int foodKind = 0; foodKind < FoodGenerator.getSize(); foodKind++) {
			centers.put(foodKind, new Point((int)gngEngines.get(foodKind).getExpectedValueX(), (int)gngEngines.get(foodKind).getExpectedValueY()));
		}
		return centers;
	}
	
	public HashMap<Integer, Double> getExpectedVariances() {
		HashMap<Integer, Double> centers = new HashMap<Integer, Double>();
		for (int foodKind = 0; foodKind < FoodGenerator.getSize(); foodKind++) {
			centers.put(foodKind, gngEngines.get(foodKind).getExpectedVariance());
		}
		return centers;		
	}
	
	public Point getExpectedCenter(int foodKind) {
		gngEngines.get(foodKind).computeExpectedDistribution();
		return new Point((int)gngEngines.get(foodKind).getExpectedValueX(), (int)gngEngines.get(foodKind).getExpectedValueY());
	}
	
	public void run() {
//		while (true) {
			for (int foodKind = 0; foodKind < FoodGenerator.getSize(); foodKind++) {
				gngEngines.get(foodKind).learn();
			}
			
//			try {
//				Thread.sleep(10);
//			} catch (InterruptedException e) {
//				
//			}
//		}
	}
}
