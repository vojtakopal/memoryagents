package memagents.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import memagents.agents.Agent;

public class MVPanel extends JPanel {
	
	private MemoryVisualizer _viz;
	
	private Agent _agent;
	
	private Timer _timer;
	
	public MVPanel(MemoryVisualizer viz, Agent agent) {
		super.setDoubleBuffered(true);
		this._viz = viz;
		this._timer = new Timer("Timer (MemoryVisualizer)");
		
		final MVPanel self = this;
		this._timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				self.repaint();
			}
		}, 0, 10);
		
		this._agent = agent;
	}
		
	public void paintComponent(Graphics g) {
		
		g.setColor(Color.BLUE);
		g.drawRect(0, 0, getWidth(), getHeight());
		
		int cellWidth = (int)Math.ceil((double)this.getWidth() / _viz.getCols());
		int cellHeight = (int)Math.ceil((double)this.getHeight() / _viz.getRows());
				
		for (int i = 0; i < _viz.getCols(); i++) {
			for (int j = 0; j < _viz.getRows(); j++) {
				int value = _viz.getValueAt(i, j);
				g.setColor(new Color(value));
				g.fillRect(i*cellWidth, j*cellHeight, cellWidth, cellHeight);
			}
		}
		
		_agent.draw(g, getWidth(), getHeight());
		
		g.dispose();
		
		
		
		super.paintComponent(g);
	}
}
