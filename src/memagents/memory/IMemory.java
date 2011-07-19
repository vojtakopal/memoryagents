package memagents.memory;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

public interface IMemory extends Runnable {
	int getWidth();
	int getHeight();
	void learn(int foodKind, ArrayList<Point> food);
	Point[] getSample(int foodKind);	
	HashMap<Integer, ExpectedGauss> getExpectedGausses();
	ExpectedGauss getExpectedGauss(int foodKind);
}
