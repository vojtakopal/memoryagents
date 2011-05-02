package memagents.tests;

public class GNGVariableLearning {

	public static void main(String[] args) {
		
		for (int i = 1; i < 5; i++) {
			DynamicComputeGNG gng = new DynamicComputeGNG(true);
			gng.setParams(0, 0, 0, 0, 600, 88, 16);
			gng.start(i * 1000);
		}
		
	}
	
}
