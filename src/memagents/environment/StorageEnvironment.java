package memagents.environment;

import java.util.ArrayList;

/**
 * 
 * @author Vojtech Kopal
 * 
 */
public class StorageEnvironment extends BasicEnvironment 
{
	protected ArrayList<Resource> resources;
		
	public StorageEnvironment(int width, int height)
	{
		super(width, height);
		
		resources = new ArrayList<Resource>();
	}

	public void addResource(Resource resource)
	{
		resources.add(resource);
	}
	
	public void initNextDay()
	{
		super.initNextDay();
		
		for ( Resource resource : resources )
		{
			resource.process(this);
		}
	}
}
