package memagents.agents.memory;

import memagents.environment.Product;

public class MemoryItem
{
	protected Product product;
	protected int[] position;
	protected float age;
	
	public MemoryItem(int[] position, Product product)
	{
		this.product = product;
		this.position = position;
		this.age = 0;
	}
}
