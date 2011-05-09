package memagents.tests;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import demogng.ComputeGNG;

import memagents.ui.MVPanel;
import memagents.ui.MemoryVisualizer;

public class GNGView extends JFrame implements MouseListener  {

	protected GNGViewPanel panel;
	
	protected JLabel label;
	
	protected Timer timer;
	
	protected int steps = 0;
	
	public GNGView() {
		super("Growing Neural Gas Test");  

        setSize(600, 600);  
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        setResizable(false);

        Container pane = getContentPane();
        pane.setLayout(new BorderLayout());
        
        DynamicComputeGNG compute = new DynamicComputeGNG();
//        #	alpha = 0.8
//        #	beta = 1.0E-4
//        #	epsilon = 0.0050
//        #	epsilon2 = 6.0E-4
//        #	numNewNode = 600
//        #	maxNodes = 8
		compute.setParams(0.8f, 1.0E-5f, 0.005f, 6.0E-4f, 600, 88, 8);
        
        pane.add(compute);
        
        setVisible(true);  
        
        compute.start();
	} 
		
	public GNGView(GNG gng) {
		super("Growing Neural Gas Test");  
                
        setSize(600, 600);  
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        setVisible(true);  
        
        Container pane = getContentPane();
        pane.setLayout(new BorderLayout());

        panel = new GNGViewPanel(gng);
        panel.addMouseListener(this);
        pane.add(panel, BorderLayout.CENTER);    
                       
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				panel.repaint();
			}
		}, 0, 10);
	}
	
	public static void main(String[] args) {
		
//		int[][] nodes = {{100,200},{300,200},{120,300}};
//		int[][] edges = {{0,1},{1,2},{0,2}};
//		GNG gng = new GNG(nodes, edges);
//		GNGView view = new GNGView(gng);
		
		GNGView view = new GNGView();
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
