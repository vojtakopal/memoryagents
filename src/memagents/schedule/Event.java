package memagents.schedule;

public class Event {
	protected IEventer callback;
	protected int time;
	protected Event next;
	
	public Event(IEventer callback, int time)
	{
		this.callback = callback;
		this.time = time;
		this.next = null;
	}
	
	public void process()
	{
		this.callback.callback();
	}
	
	public void chainEvent(Event event)
	{
		if (this.next == null)
		{
			this.next = event;
		}
		else
		{
			this.next.chainEvent(event);
		}
	}
	
	public Event getNext()
	{
		return this.next;
	}
}
