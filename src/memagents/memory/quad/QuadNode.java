package memagents.memory.quad;

import java.awt.Point;

public class QuadNode {
	
	protected QuadNode[] children = null;
	
	protected long positive;
	
	protected long negative;
	
	public QuadNode(int x, int y, int width, int height) {
		
	}
	
	public void learn(Point[] points) {
		positive += points.length;
		
		if (children != null) {
			for (QuadNode node : children) {
				
			}
		} else {
			
		}
	}
	
}
