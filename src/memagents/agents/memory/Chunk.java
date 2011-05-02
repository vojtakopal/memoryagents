package memagents.agents.memory;

import memagents.agents.memory.MemoryAgentMemories.BeliefType;

public class Chunk {
	
	public BeliefType type;
	public double belief;
	public int amount;
	
	public Chunk(BeliefType type, double belief, int amount) {
		this.type = type;
		this.belief = belief;
		this.amount = amount;
	}
	
	public Chunk() {
		this.type = BeliefType.MRKEV;
	}
	
}
