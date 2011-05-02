package memagents.tests;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Calendar;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import demogng.ComputeGNG;
import demogng.NodeGNG;

public class DynamicComputeGNG extends ComputeGNG {
	
	protected int MAX_KNOWN_SPOTS = 100;
	
	Image logGraph = null;
	
	Timer timer = new Timer();
	
	double expectedValueX = 0;
	double expectedValueY = 0;
	double expectedVariance = 0;
	
	double realValueX = 0;
	double realValueY = 0;
	double realVariance = 0;
	
	double score = 0;
	double totalScore = 0;
	
	boolean silentMode;
	
	int maxSteps = 0;
	
	public DynamicComputeGNG() {
		this(false);
	}
	
	public DynamicComputeGNG(boolean silentMode) {
		super(null);
		
		this.silentMode = silentMode;
		
		algo = 0;
		distribution = 7;
		speed = 10;
		
		// optimalizujeme tyto hodnoty
		alphaGNG = 0.5f; // 0, 1 (0.5)
		betaGNG = 5.0E-4f; // 0, 1 (5.0E-4)
		epsilonGNG = 0.05f; // 0, 1 (0.05)
		epsilonGNG2 = 6.0E-4f; // 0, 1 (6.0E-4)
		NUM_NEW_NODE = 600; // 10, 2000 (600)
		MAX_EDGE_AGE = 88; // 10, 800 (88)
		maxNodes = 16;
		
		initDiscreteSignals(7);
		addNode(new Dimension(INIT_WIDTH, INIT_HEIGHT));
		addNode(new Dimension(INIT_WIDTH, INIT_HEIGHT));
		
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				recountDiscreteSignals();
				computeExpectedDistribution();
			}
		}, 0, 10);
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
	
	public void start(int maxSteps) {
		this.maxSteps = maxSteps;
		start();
	}
	
	public void run() {
		if (false == silentMode) { super.run(); return; }  
		
		long startTime = Calendar.getInstance().getTimeInMillis();
	    for (int i = 0; i < maxSteps || maxSteps == 0; i++) {
	      
	        learn();	      	
	      	repaint();

	      	totalScore = score / maxSteps;
	      	
			try {
			  Thread.sleep(speed);
			} catch (InterruptedException e) {
			  break;
			}
	    }
		long endTime = Calendar.getInstance().getTimeInMillis();
	    
	    System.out.println("Exited after "+String.valueOf(maxSteps)+" steps in "+String.valueOf(endTime-startTime)+"ms with params:");
	    System.out.println("	alpha = "+String.valueOf(alphaGNG));
	    System.out.println("	beta = "+String.valueOf(betaGNG));
	    System.out.println("	epsilon = "+String.valueOf(epsilonGNG));
	    System.out.println("	epsilon2 = "+String.valueOf(epsilonGNG2));
	    System.out.println("	numNewNode = "+String.valueOf(NUM_NEW_NODE));
	    System.out.println("	maxNodes = "+String.valueOf(MAX_NODES));
	    System.out.println("Result = "+String.valueOf(totalScore));
	    System.out.println();
	    
	}

	public Dimension size() {
		if (silentMode) {
			return new Dimension(100, 100);
		} else {
			return super.size();
		}
	}
	
	protected double getRealValueX() {
		return INIT_WIDTH / 2;
	}
	
	protected double getRealValueY() {
		return INIT_WIDTH / 2;
	}
	
	protected double getVariance() {
		return INIT_WIDTH / 10;
	}
	
	protected void recountDiscreteSignals() {
		int indexToRewrite = (int)(Math.random() * MAX_KNOWN_SPOTS);
		int newX = 0;
		int newY = 0;

		Random rand = new Random();
		newX = (int)getRealValueX() + (int)(rand.nextGaussian() * getVariance());
		newY = (int)getRealValueY() + (int)(rand.nextGaussian() * getVariance());

		
		discreteSignalsX[indexToRewrite] = newX;
		discreteSignalsY[indexToRewrite] = newY;
	}
	
	protected void initDiscreteSignals(int distribution) {
		// System.out.println("init discrete signals");
		
		int newX = 0;
		int newY = 0;
		Random rand = new Random();
		
		for (int i = 0; i < MAX_KNOWN_SPOTS; i++) {
			newX = (int)getRealValueX() + (int)(rand.nextGaussian() * getVariance());
			newY = (int)getRealValueY() + (int)(rand.nextGaussian() * getVariance());
			
			discreteSignalsX[i] = newX;
			discreteSignalsY[i] = newY;
		}
	}
	
	public void getSignal(int distribution) {
		int index = (int)(Math.random() * MAX_KNOWN_SPOTS);

		SignalX = discreteSignalsX[index];
		SignalY = discreteSignalsY[index];

//		System.out.print(index);
//		System.out.print(' ');
//		System.out.print(SignalX);
//		System.out.print(' ');
//		System.out.print(SignalY);
//		System.out.println();
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

//		System.out.print("Expected values ");
//		System.out.print(expectedValueX);
//		System.out.print(":");
//		System.out.print(expectedValueX);
//		System.out.print(" ");
//		System.out.print(expectedVariance);
//		System.out.println();
		
		double distance2 = (expectedValueX - getRealValueX())*(expectedValueX - getRealValueX()) + (expectedValueY - getRealValueY())*(expectedValueY - getRealValueY());
		double pomer = 1; //getVariance() / expectedVariance;
		
		if (expectedVariance > getVariance()) pomer = getVariance() / expectedVariance;
		else pomer = expectedVariance / getVariance();
		
		score = distance2 / ((getVariance() + expectedVariance)*(getVariance() + expectedVariance));
		
		//System.out.println("Score: "+score);
	}
	
	public void update(Graphics g) {
		super.update(g);
		
		// expected
		g.setColor(Color.PINK);
		g.fillOval((int)expectedValueX - 1, (int)expectedValueY - 1, 2, 2);
		g.setColor(Color.PINK);
		g.drawOval((int)expectedValueX - (int)expectedVariance/2, (int)expectedValueY - (int)expectedVariance/2, (int)expectedVariance, (int)expectedVariance);
		
		// real distribution
		g.setColor(Color.BLACK);
		g.fillOval((int)getRealValueX() - 1, (int)getRealValueY() - 1, 2, 2);
		g.setColor(Color.BLACK);
		g.drawOval((int)getRealValueX() - (int)getVariance()/2, (int)getRealValueY() - (int)getVariance()/2, (int)getVariance(), (int)getVariance());
		
		// shift old data in log graph
		if (logGraph == null) logGraph = createImage(INIT_WIDTH, INIT_HEIGHT / 5);
		Graphics glog = logGraph.getGraphics();
		
		glog.copyArea(0, 0, INIT_WIDTH, logGraph.getHeight(null), -2, 0);
		
		glog.setColor(Color.WHITE);
		glog.fillRect(INIT_WIDTH-2, 0, 2, logGraph.getHeight(null));
		
		if (Math.abs(score) > 0.05) {
			glog.setColor(Color.RED);
		} else if (Math.abs(score) > 0.01) {
			glog.setColor(Color.ORANGE);
		} else {
			glog.setColor(Color.GREEN);
		}
		glog.fillRect(INIT_WIDTH-2, (int)(logGraph.getHeight(null) / 2 + logGraph.getHeight(null) * score * 5), 2, 2);
		
		glog.dispose();
		
		g.drawImage(logGraph, 0, INIT_HEIGHT - logGraph.getHeight(null) - 25, null);
		g.dispose();
	}
}
