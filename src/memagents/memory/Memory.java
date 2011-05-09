package memagents.memory;

import java.awt.Point;
import java.util.ArrayList;

import memagents.food.FoodGenerator;

abstract public class Memory {

	static public final int NUM_SAMPLES = 5;
	
	protected int width;
	public int getWidth() { return height; }
	
	protected int height;
	public int getHeight() { return height; }
	
	public Memory(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	abstract public void learn(int foodKind, ArrayList<Point> food);
	abstract public Point[] getSample(int foodKind);	
}
