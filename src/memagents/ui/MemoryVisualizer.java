package memagents.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.management.timer.TimerMBean;
import javax.swing.JFrame;

import memagents.Simulation;
import memagents.agents.Agent;
import memagents.agents.MemoryAgent;
import memagents.agents.TestAgent;
import memagents.agents.memory.Chunk;
import memagents.agents.memory.MemoryAgentMemories.BeliefType;
import memagents.environment.Matrix;

public class MemoryVisualizer extends JFrame implements MouseListener {
	private Simulation simulation = null;	
	private MemoryAgent agent = null;
	
	private ArrayList<Agent> agents = null;
	
	private Timer timer = new Timer();
	
	private MVPanel panel = null;
	
	public MemoryVisualizer(MemoryAgent agent, Simulation simulation) {
        super("MemoryVisualizer: #"+agent.getId());  
        
        this.agent = agent;
        this.agents = simulation.getAgents();
        this.simulation = simulation;
        
        setSize(300, 300);  
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        setVisible(true);  
        
        Container pane = getContentPane();
        pane.setLayout(new BorderLayout());
        
        panel = new MVPanel(this);
        panel.addMouseListener(this);
        pane.add(panel, BorderLayout.CENTER);
        
        final MemoryVisualizer self = this;
        timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				self.run();
			}
		}, 0, 50);
        
        // set current situation
//        for (int i = 0; i < TestAgent.MEMORY_SIZE; i++) {
//        	for (int j = 0; j < TestAgent.MEMORY_SIZE; j++) {
//        		if (i % 5 == 0 && j % 5 == 0) {
//        			situation.set(i, j, 1.0);
//        		}
//        	}
//        }
        
        // set random values
		for (int i = 0; i < MemoryAgent.MEMORY_SIZE; i++) {
			for (int j = 0; j < MemoryAgent.MEMORY_SIZE; j++) {
				Chunk chunk = agent.getMemoryChunkAt(BeliefType.MRKEV, i, j);
				chunk.amount = simulation.getEnvironment().get(i,j).size();
				chunk.belief = Math.random();
			}
 		}
		
		// set random position to agent
		agent.setX((int)(Math.random() * MemoryAgent.MEMORY_SIZE));
		agent.setY((int)(Math.random() * MemoryAgent.MEMORY_SIZE));
	}
	
	public void run() {
		
		
	}
	
	public int getValueAt(int x, int y) {
		boolean agentThere = false;
		boolean meThere = false;
		
		for (Agent otheragent : agents) {
			if (otheragent instanceof MemoryAgent) {
				MemoryAgent otherMemoryAgent = (MemoryAgent)otheragent;
				if (otherMemoryAgent.getX() == x && otherMemoryAgent.getY() == y) {
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
			Chunk chunk = agent.getMemoryChunkAt(BeliefType.MRKEV, x, y);
			if (chunk == null) return 0xffffff;
			
			double alpha = chunk.belief;
			
			int R = (int)(alpha * 255 * chunk.amount);
			int G = 0 + (int)(255 * (1 - alpha));
			int B = 125 + (int)(130 * (1 - alpha));
			
			int finalcolor = (R << 16) + (G << 8) + (B);
			
			return finalcolor;
		}
	}
	
	public void touch(int x, int y) {
		int effectiveDistance = 10;
		for (int i = 0; i < effectiveDistance*2; i++) {
			for (int j = 0; j < effectiveDistance*2; j++) {
				int matrixX = x + i - effectiveDistance;
				int matrixY = y + j - effectiveDistance;
				
				if (matrixX < 0 || matrixX >= TestAgent.MEMORY_SIZE) continue;
				if (matrixY < 0 || matrixY >= TestAgent.MEMORY_SIZE) continue;
				
				double distance = Math.sqrt((i-effectiveDistance)*(i-effectiveDistance)+(j-effectiveDistance)*(j-effectiveDistance));
				
				if (distance < effectiveDistance) {
					//memory[x+i-effectiveDistance][y+j-effectiveDistance] += 0.02;
					Chunk chunk = agent.getMemoryChunkAt(BeliefType.MRKEV, matrixX, matrixY);
					chunk.amount = simulation.getEnvironment().get(matrixX,	matrixY).size();
					chunk.belief = 1;
				}
			}
		}
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