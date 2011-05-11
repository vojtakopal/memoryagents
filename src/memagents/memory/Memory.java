package memagents.memory;

import java.awt.Point;
import java.util.ArrayList;

import memagents.Simulation;
import memagents.food.FoodGenerator;

abstract public class Memory implements Runnable {

	protected int numSamples = 5;
	
	protected int width;
	public int getWidth() { return height; }
	
	protected int height;
	public int getHeight() { return height; }
	
	protected Simulation simulation;
	
	public Memory(int width, int height, Simulation simulation) {
		this.width = width;
		this.height = height;
		this.simulation = simulation;
		this.numSamples = Simulation.ANSWER_SAMPLE;
	}
	
	abstract public void learn(int foodKind, ArrayList<Point> food);
	abstract public Point[] getSample(int foodKind);	
}
