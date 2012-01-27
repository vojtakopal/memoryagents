package memagents.memory.quad;

public class GridCell {
	
	protected GridCell[] children = null;
	
	protected long positive;
	
	protected long negative;
	
	public static double ALPHA = 2;
	
	public GridCell() {
		
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

		return result;
	}
	

	public double getValueOrZero() {
		double result = 0;
		
		result = containsFood() / ALPHA;
		
		if (result < 0) result = 0;
		
		return result;
	}
	
	public String toString() {
		return "+"+positive+" -"+negative;
	}
	
}
