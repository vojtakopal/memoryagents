package memagents.memory.quad;

public class QuadNode {
	
	protected QuadNode[] children = null;
	
	protected long positive;
	
	protected long negative;
	
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
		if (getPositive() > 0 && getNegative() < getPositive()) {
			long p = getPositive();
			if (p > 255) p = 255;
			return p;
		}
		
		return 0;
	}
	
}
