package memagents.schedule;

/**
 * Once this event is done, it reschedules itself.
 * @author Vojtech Kopal
 *
 */
public class RepeatedEvent extends Event 
{
	protected Scheduler owner;
	
	public RepeatedEvent(IEventer eventer, int time, Scheduler owner) {
		super(eventer, time);
		
		this.owner = owner;
	}
	
	public void process()
	{
		super.process();
		owner.scheduleRepeatedEvent(this.callback, this.time);
	}
}
