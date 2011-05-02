package memagents.agents.memory;

import java.util.ArrayList;
import java.util.HashMap;

import memagents.agents.MemoryAgent;
import memagents.agents.memory.MemoryAgentMemories.BeliefType;

/**
 * 	Belief.	
 * 
 *  "Do you believe in the Hogfather, Hex?"
 */
public class MemoryAgentMemories {

	public enum BeliefType {
		MRKEV,
		REDKEV
	}
	
	/**
	 *	
	 */
	protected HashMap<BeliefType, Chunk[][]> knowledges = new HashMap<BeliefType, Chunk[][]>();
	
	protected HashMap<BeliefType, Influence> influences = new HashMap<MemoryAgentMemories.BeliefType, Influence>();
	
	//protected ArrayList<Belief>
	
	public MemoryAgentMemories() {
		for (BeliefType type : BeliefType.values()) {
			Chunk[][] knowledge = new Chunk[MemoryAgent.MEMORY_SIZE][MemoryAgent.MEMORY_SIZE];
			
			for (int i = 0; i < MemoryAgent.MEMORY_SIZE; i++) {
				for (int j = 0; j < MemoryAgent.MEMORY_SIZE; j++) {
					knowledge[i][j] = new Chunk();
				}	
			}
			
			knowledges.put(type, knowledge);
		}
	}
	
	public void beInfluenced(MemoryAgentMemories memories) {
		for (BeliefType type : BeliefType.values()) {
			Influence influence = getInfluence(type);
			influence.add(memories.knowledges.get(type));
		}
	} 
	
	public void computeKnowledge() {
		for (BeliefType type : BeliefType.values()) {
			Influence influence = getInfluence(type);
			Chunk[][] currentKnowledge = knowledges.get(type);
			knowledges.put(type, influence.computeKnowledge(currentKnowledge));
		}
		
		influences = new HashMap<MemoryAgentMemories.BeliefType, Influence>();
	}
	
	private Influence getInfluence(BeliefType type) {
		if (influences.containsKey(type) == true) return influences.get(type);
		
		Influence influence = new Influence();
		influences.put(type, influence);
		
		return influence;
	}
	
	public Chunk getBeliefChunkAt(BeliefType type, int x, int y) {
		return getKnowledge(type)[x][y];
	}
	
	private Chunk[][] getKnowledge(BeliefType type) {
		if (knowledges.containsKey(type) == true) return knowledges.get(type);
		
		Chunk[][] knowledge = new Chunk[MemoryAgent.MEMORY_SIZE][MemoryAgent.MEMORY_SIZE];
		return knowledge;
	}
	
	/**
	 * 	Those are facts I belief 100%.
	 * 
	 */
	public void doLearn(BeliefType type, int x, int y, int amount) {
		knowledges.get(type)[x][y] = new Chunk(type, 1, amount); 
	}
	
	/**
	 * Default value.
	 * 
	 */
	public void doAge() {
		doAge(.99);
	}
	
	/**
	 * Lower certainty of our believes.
	 * 
	 * @param coef
	 */
	public void doAge(double coef) {
		for (BeliefType type : BeliefType.values()) {
			Chunk[][] knowledge = knowledges.get(type);
			for (int i = 0; i < MemoryAgent.MEMORY_SIZE; i++) {
				for (int j = 0; j < MemoryAgent.MEMORY_SIZE; j++) {
					knowledge[i][j].belief *= coef;
				}
			}
		}
	}
}

class Influence {
	
	protected ArrayList<Chunk[][]> chunks = new ArrayList<Chunk[][]>();
	
	public Influence() {
		
	}
	
	public void add(Chunk[][] chunk) {
		chunks.add(chunk);
	}

	public Chunk[][] computeKnowledge(Chunk[][] currentKnowledge) {
		int numInfluences = chunks.size();
		if (numInfluences == 0) return currentKnowledge;
		
		for (int i = 0; i < MemoryAgent.MEMORY_SIZE; i++) {
			for (int j = 0; j < MemoryAgent.MEMORY_SIZE; j++) {
				HashMap<Integer, double[]> scoredAmounts = new HashMap<Integer, double[]>();
				Chunk myChunk = currentKnowledge[i][j];
				
				double sumAmount = 0;
				double sumBelief = 0;
				for (Chunk[][] opinions : chunks) {
					Chunk chunk = opinions[i][j];
					sumAmount += chunk.amount * chunk.belief;
					sumBelief += chunk.belief;
				}
				
				double propAmount = sumAmount / chunks.size();
				double realBelief = sumBelief / chunks.size();

				double leastDistance = Double.MAX_VALUE;
				int realAmount = 0;
				for (Chunk[][] opinions : chunks) {
					Chunk chunk = opinions[i][j];
					if (Math.abs(chunk.amount - propAmount) < leastDistance) {
						leastDistance = Math.abs(chunk.amount - propAmount);
						realAmount = chunk.amount;
					}
				}
				
				if (realAmount != currentKnowledge[i][j].amount) {
					 if (realBelief > myChunk.belief) {
						 // changing my opinion
						 myChunk.amount = realAmount;
						 myChunk.belief = realBelief;
					 } else {
						 // lowering my belief
						 myChunk.belief -= Math.abs(realBelief - myChunk.belief) / 10;
					 }
				} else {
					 // merging my belief
					 myChunk.belief += (realBelief - myChunk.belief) / 10;
				}
			}
		}
		
		return currentKnowledge;
	}
	
}
