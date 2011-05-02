package memagents.tests;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

public class GNGViewPanel extends JPanel {

	protected GNG gng;
	
	public GNGViewPanel(GNG dataProvider) {
		this.gng = dataProvider;
	}
	
	public void paintComponent(Graphics g) {
		
		// get data from gng
//		g.setColor(Color.WHITE);
//		g.fillRect(0, 0, getWidth(), getHeight());
//		
//		g.setColor(Color.RED);
//		for (Edge edge : gng.getEdges()) {
//			g.drawLine(edge.first.x, edge.first.y, edge.second.x, edge.second.y);
//		}
//		
//		g.setColor(Color.GREEN);
//		for (Node node : gng.getNodes()) {
//			g.fillOval(node.x-5, node.y-5, 10, 10);
//		}
//		
//		g.dispose();
		
		super.paintComponent(g);
		
	}
	
}
