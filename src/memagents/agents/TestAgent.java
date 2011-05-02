package memagents.agents;

public class TestAgent {
	public static int MEMORY_SIZE = 100;
	private double[][] memory = new double[MEMORY_SIZE][MEMORY_SIZE];
	public double getMemory(int x, int y) { return memory[x][y]; }
	public void setMemory(int x, int y, double value) { memory[x][y] = value; }
	
	private int _x;
	public int getX() { return _x; }
	public void setX(int value) { _x = value; }
	
	private int _y;
	public int getY() { return _y; }
	public void setY(int value) { _y = value; }
}
