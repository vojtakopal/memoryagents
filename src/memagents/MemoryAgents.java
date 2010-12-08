package memagents;

import memagents.utils.Log;

//TODO: Pridat zradlo (jeden typ), pamatovani zradla, kde je. Zobrazovatko z logu.

public class MemoryAgents 
{
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Log.intoFile("log.log");
		
		Simulation simulation = new Simulation();
		simulation.run();
	}
}