package memagents.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

import memagents.Simulation;
import memagents.agents.Agent;
import memagents.agents.GNGAgent;

public class MemoryVisualizer extends JFrame implements MouseListener {
	private Simulation simulation = null;	
	private Agent agent = null;
	
	private ArrayList<Agent> agents = null;
	
	private MVPanel panel = null;
	
	public MemoryVisualizer(Agent agent, Simulation simulation) {
        super("MemoryVisualizer: #"+agent.getId());  
        
        this.agent = agent;
        this.agents = simulation.getAgents();
        this.simulation = simulation;
        
        setSize(250, 250);  
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        
        Container pane = getContentPane();
        pane.setLayout(new BorderLayout());
        
        panel = new MVPanel(this, agent);
        panel.addMouseListener(this);
        pane.add(panel, BorderLayout.CENTER);
                
        setVisible(true);  
        
        Point currLoc = getLocation();
        int xLoc = agent.getId() % 5;
        int yLoc = agent.getId() / 5;
        setLocation(currLoc.x + 250*xLoc, currLoc.y + 250*yLoc);
        
        // set current situation
//        for (int i = 0; i < TestAgent.MEMORY_SIZE; i++) {
//        	for (int j = 0; j < TestAgent.MEMORY_SIZE; j++) {
//        		if (i % 5 == 0 && j % 5 == 0) {
//        			situation.set(i, j, 1.0);
//        		}
//        	}
//        }
        
        // set random values
//		for (int i = 0; i < MemoryAgent.MEMORY_SIZE; i++) {
//			for (int j = 0; j < MemoryAgent.MEMORY_SIZE; j++) {
//				Chunk chunk = agent.getMemoryChunkAt(BeliefType.MRKEV, i, j);
//				chunk.amount = simulation.getEnvironment().get(i,j).size();
//				chunk.belief = Math.random();
//			}
// 		}
		
		// set random position to agent
//		agent.setX((int)(Math.random() * MemoryAgent.MEMORY_SIZE));
//		agent.setY((int)(Math.random() * MemoryAgent.MEMORY_SIZE));
	}
		
	public int getValueAt(int x, int y) {
		boolean agentThere = false;
		boolean meThere = false;
		
		for (Agent otheragent : agents) {
			if (otheragent instanceof Agent) {
				Agent otherMemoryAgent = otheragent;
				if (otherMemoryAgent.getPosition().x == x && otherMemoryAgent.getPosition().y == y) {
					agentThere = true;
					if (otheragent == agent) {
						meThere = true;
					}
				}
			}
		}
		
		if (agentThere) {
			if (meThere) {
				return 0x00ffff;
			} else {
				return 0xffff00;
			}
		} else {
//			return 0xffffff;
			double alpha = 1;
			int amount = 0;
						
			return 0xffffff;
			
//			int R = (int)(alpha * 255 * amount);
//			int G = 0 + (int)(255 * (1 - alpha));
//			int B = 125 + (int)(130 * (1 - alpha));
//			
//			int finalcolor = (R << 16) + (G << 8) + (B);
//			return finalcolor; 
//			Chunk chunk = agent.getMemoryChunkAt(BeliefType.MRKEV, x, y);
//			if (chunk == null) return 0xffffff;
//			
//			double alpha = chunk.belief;
//			
//			int R = (int)(alpha * 255 * chunk.amount);
//			int G = 0 + (int)(255 * (1 - alpha));
//			int B = 125 + (int)(130 * (1 - alpha));
//			
//			int finalcolor = (R << 16) + (G << 8) + (B);
//			
//			return finalcolor;
		}
	}
	
	public void touch(int x, int y) {
//		int effectiveDistance = 10;
//		for (int i = 0; i < effectiveDistance*2; i++) {
//			for (int j = 0; j < effectiveDistance*2; j++) {
//				int matrixX = x + i - effectiveDistance;
//				int matrixY = y + j - effectiveDistance;
//				
//				if (matrixX < 0 || matrixX >= Agent.MEMORY_SIZE) continue;
//				if (matrixY < 0 || matrixY >= Agent.MEMORY_SIZE) continue;
//				
//				double distance = Math.sqrt((i-effectiveDistance)*(i-effectiveDistance)+(j-effectiveDistance)*(j-effectiveDistance));
//				
//				if (distance < effectiveDistance) {
//					//memory[x+i-effectiveDistance][y+j-effectiveDistance] += 0.02;
//					Chunk chunk = agent.getMemoryChunkAt(BeliefType.MRKEV, matrixX, matrixY);
//					chunk.amount = simulation.getEnvironment().get(matrixX,	matrixY).size();
//					chunk.belief = 1;
//				}
//			}
//		}
	}
	
	public int getCols() {
		return 100;
	}
	
	public int getRows() {
		return 100;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println(Integer.toString(e.getX()) + ':' + Integer.toString(e.getY()));
		System.out.println(Integer.toString(panel.getWidth()) + ':' + Integer.toString(panel.getHeight()));

		int x = getCols() * e.getX() / panel.getWidth();
		int y = getRows() * e.getY() / panel.getHeight();
		
		touch(x, y);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public Agent getAgent() {
		return agent;
	}
}