package memagents.schedule;

import java.util.ArrayList;;

public class Scheduler {
	private boolean DEBUG_MODE = false;
	protected ArrayList<Event> events;
	protected ArrayList<Integer> delays;
	
	public Scheduler() 
	{
		events = new ArrayList<Event>();
		delays = new ArrayList<Integer>();
	}
	
	public void scheduleRepeatedEvent(IEventer eventer, int time)
	{
		schedule(new RepeatedEvent(eventer, time, this));
	}
	
	public void scheduleEvent(IEventer eventer, int time)
	{
		schedule(new Event(eventer, time));
	}
	
	public Event dequeue()
	{
		if (delays.get(0) > 0)
		{
			delays.set(0, delays.get(0) - 1);
			
			return null;
		}
		else
		{
			Event event = events.get(0);
			delays.remove(0);
			events.remove(0);
			
			return event;
		}
	}
	
	public boolean empty()
	{
		return events.size() == 0;
	}
	
	public void schedule(Event event)
	{
		int timePosition = 0;
		
		boolean added = false;
		
		for (int i = 0; i < delays.size(); i++)
		{
			timePosition += delays.get(i);
			
			if (timePosition >= event.time)
			{
				if (timePosition == event.time)
				{
					if (DEBUG_MODE) System.out.println("Scheduling at existing " + i);
					Event scheduledEvent = events.get(i);
					scheduledEvent.chainEvent(event);
				}
				else
				{
					//TODO: log4j
					if (DEBUG_MODE) System.out.println("Scheduling at new " + i);
					events.add(i, event);
					delays.add(i, event.time - timePosition + delays.get(i));
					delays.set(i+1, delays.get(i+1) - delays.get(i));
				}
				added = true;
				break;
			}
		}
		
		if (!added)
		{
			if (DEBUG_MODE) System.out.println("Scheduling at the end");
			events.add(event);
			delays.add(event.time - timePosition);
		}
	}
}
