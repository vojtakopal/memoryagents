package memagents.memory.quad;

public class QuadNode {
	
	protected QuadNode[] children = null;
	
	protected long positive;
	
	protected long negative;
	
	public static double ALPHA = 2;
	
	public QuadNode() {
		
	}
	
	public void incPositive() {
		positive++;
	}
	
	public void incNegative() {
		negative++;
	}

	public long getPositive() {
		return positive;
	}
	
	public long getNegative() {
		return negative;
	}
	
	public long containsFood() {
		long result = 0;
		
		result = (long)(ALPHA * getPositive() - getNegative());
		
//		if (getPositive() > 0 && getNegative() < getPositive()) {
//			long p = getPositive();
//			if (p > 255) p = 255;
//			return p;
//		}

		return result;
	}
	
}
