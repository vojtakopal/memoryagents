package memagents.food;

import java.awt.Color;
import java.util.Random;

import memagents.Simulation;
import memagents.environment.Environment;

public class FoodGenerator {
	static private int idCounter = 0;
	static public int getSize() { return idCounter; }
		
	protected int id;
	public int getKind() { return id; }
	
	protected int x;
	
	protected int y;
	
	protected int range = 10;
	
	protected Simulation simulation = null;
	
	/**
	 * Amount of food generated at one time.
	 * 
	 */
	protected int deltaAmount = 1;
	
	public FoodGenerator(int centerX, int centerY, Simulation simulation) {
		this.id = idCounter++;
		this.x = centerX;
		this.y = centerY;
		this.simulation = simulation;
	}
	
	public void setRange(int value) {
		this.range = value;
	}
		
	public void seed(Environment environment) {		
		int newX = 0;
		int newY = 0;
		
		Random rand = simulation.getRandom();

		newX = (int)(x + range*rand.nextGaussian());
		newY = (int)(y + range*rand.nextGaussian());
		
		environment.seedFood(newX, newY, id, deltaAmount);
	}
	
	public boolean isOverThreshold(double value) {
		if (value > 0.5) return true;
		else return false;
	}
	
	public Color getColor() {
		switch (id) {
		case 0: return Color.RED;
		case 1: return Color.BLUE;
		case 2: return Color.CYAN;
		case 3: return Color.GREEN;
		case 4: return Color.MAGENTA;
		case 5: return Color.ORANGE;
		case 6: return Color.PINK;
		case 7: return Color.YELLOW;
		default: return Color.DARK_GRAY;
		}
	}
}
