package memagents.environment;


import java.util.ArrayList;

import memagents.utils.Log;

/**
 * Only add placebos.
 * @author Vojtech
 *
 */
public class Resource 
{
	protected int interval;
	protected int counter;
	protected ArrayList<int[]> destinations;
	
	public Resource(ArrayList<int[]> destinations)
	{
		init(destinations, 50);
	}
	
	public Resource(ArrayList<int[]> destinations, int interval)
	{
		init(destinations, interval);
	}
	
	private void init(ArrayList<int[]> destinations, int interval)
	{
		this.destinations = destinations;
		this.interval = interval;
		this.counter = 0;
	}
	
	public void process(StorageEnvironment storageEnvironment) 
	{
		if ((counter % interval) == 0)
		{
			for ( int[] d : destinations )
			{
				if (Math.random() < 0.1) {
					Log.println("product 0 "+d[0]+" "+d[1]);
					storageEnvironment.add(d[0], d[1], new Product());
				}
			}
		}
		counter++;
	}
	
}
