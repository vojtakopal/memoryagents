package memagents.memory.gng;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import memagents.memory.GNGMemory;

public class MAComputeGNG extends ComputeGNG {
	
	public static int threadCounter = 0;
	public static int idCounter = 0;
	
	protected int MAX_KNOWN_SPOTS = 0;
	
	Image logGraph = null;
	
	Timer timer = null;
	
	double expectedValueX = 0;
	double expectedValueY = 0;
	double expectedVariance = 0;
	
	double realValueX = 0;
	double realValueY = 0;
	double realVariance = 0;
	
	double score = 0;
	double totalScore = 0;
		
	int maxSteps = 0;
	
	GNGMemory memory;
	
	public MAComputeGNG(GNGMemory memory) {
					
		algo = 0;
		distribution = 7;
		speed = 1;
		
		width = 100;
		height = 100;
				
		// optimalizujeme tyto hodnoty
		alphaGNG = 0.5f; // 0, 1 (0.5)
		betaGNG = 5.0E-4f; // 0, 1 (5.0E-4)
		epsilonGNG = 0.05f; // 0, 1 (0.05)
		epsilonGNG2 = 6.0E-4f; // 0, 1 (6.0E-4)
		NUM_NEW_NODE = 600; // 10, 2000 (600)
		MAX_EDGE_AGE = 88; // 10, 800 (88)
		maxNodes = 16;
		
		this.memory = memory;
		
		initDiscreteSignals();
		addNode(new Dimension(width, height));
		addNode(new Dimension(width, height));
	}
	
	public void setParams(float alpha, float beta, float epsilon, float epsilon2, int numNewNode, int maxEdgeAge, int maxNodes) {
		this.alphaGNG = alpha;
		this.betaGNG = beta;
		this.epsilonGNG = epsilon;
		this.epsilonGNG2 = epsilon2;
		this.NUM_NEW_NODE = numNewNode;
		this.MAX_EDGE_AGE = maxEdgeAge;
		this.maxNodes = maxNodes;
	}
	
	public void learn() {
		if (MAX_KNOWN_SPOTS == 0) return;
		super.learn();
	}
	
	public void start(int maxSteps) {
		this.maxSteps = maxSteps;

//	    relaxer = new Thread(this, "ComputeGNG #"+(++idCounter));
//	    relaxer.start();
	    
	    threadCounter++;
	}
	
	protected double getRealValueX() {
		return width / 2;
	}
	
	protected double getRealValueY() {
		return width / 2;
	}
	
	protected double getVariance() {
		return width / 10;
	}
	
	public void setDescreteSignals(ArrayList<Point> signals) {
		MAX_KNOWN_SPOTS = Math.min(signals.size(), discreteSignalsX.length);
		for (int i = 0; i < MAX_KNOWN_SPOTS; i++) {
			discreteSignalsX[i] = signals.get(i).x;
			discreteSignalsY[i] = signals.get(i).y;
		}
	}
	
	protected void recountDiscreteSignals() {

	}
	
	protected void initDiscreteSignals() {
		
	}
	
	public void getSignal(int distribution) {
		if (MAX_KNOWN_SPOTS == 0) {
			Random random = memory.getRandom();
			
			SignalX = random.nextInt(width);
			SignalY = random.nextInt(height);
		} else {
			int index = (int)(Math.random() * MAX_KNOWN_SPOTS);
	
			SignalX = discreteSignalsX[index];
			SignalY = discreteSignalsY[index];
		}
	}

	public NodeGNG[] getNodes() {
		return nodes;
	}
	
	public NodeGNG getNodeAt(int index) {
		return nodes[index];
	}
	
	public EdgeGNG[] getEdges() {
		return edges;
	}
	
	public int getNumNodes() {
		return nnodes;
	}
	
	public int getNumEdges() {
		return nedges;
	}
	
	public double getExpectedValueX() {
		return expectedValueX;
	}
	
	public double getExpectedValueY() {
		return expectedValueY;
	}
	
	public double getExpectedVariance() {
		return expectedVariance;
	}
	
	public void computeExpectedDistribution() {		
		expectedValueX = 0; // stredni hodnota
		expectedValueY = 0;
		
		for (int i = 0; i < nnodes; i++) {
			NodeGNG node = this.nodes[i];
			if (node != null) {
				expectedValueX += node.getX() / nnodes;
				expectedValueY += node.getY() / nnodes;
			}
		}
		
		expectedVariance = 0;
		for (int i = 0; i < nnodes; i++) {
			NodeGNG node = this.nodes[i];
			if (node != null) {
				double sqDistance = Math.sqrt((expectedValueX - node.getX())*(expectedValueX - node.getX()) + (expectedValueY - node.getY())*(expectedValueY - node.getY()));
				expectedVariance += sqDistance / nnodes;
			}
		}
		
		double distance2 = (expectedValueX - getRealValueX())*(expectedValueX - getRealValueX()) + (expectedValueY - getRealValueY())*(expectedValueY - getRealValueY());
		double pomer = 1; //getVariance() / expectedVariance;
		
		if (expectedVariance > getVariance()) pomer = getVariance() / expectedVariance;
		else pomer = expectedVariance / getVariance();
		
		score = distance2 / ((getVariance() + expectedVariance)*(getVariance() + expectedVariance));
		
		//System.out.println("Score: "+score);
	}
	
}
