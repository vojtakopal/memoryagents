package memagents.tests;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GNG extends TimerTask {
		
	//protected ArrayList<NodeGNG> nodes = new ArrayList<Node>();
	//protected ArrayList<EdgeGNG> edges = new ArrayList<Edge>();
	
	protected Timer timer;
	
	public GNG() {
		timer = new Timer();
		timer.scheduleAtFixedRate(this, 0, 10);
	}
	
	public void run() {
//		Random rand = new Random();
//		for (Node node : nodes) {
//			node.x += rand.nextInt(10) - 5;
//			node.y += rand.nextInt(10) - 5;
//		}
	}
	
	public GNG(int[][] arrNodes, int[][] arrEdges) {
		this();
		
//		for (int[] node : arrNodes) {
//			Node n = new Node(node[0], node[1]);
//			nodes.add(n);
//		}
//		
//		for (int[] edge : arrEdges) {
//			Edge e = new Edge(nodes.get(edge[0]), nodes.get(edge[1]));
//			edges.add(e);
//		}
	}
	
//	final public ArrayList<Node> getNodes() {
//		return nodes;
//	}
//	
//	final public ArrayList<Edge> getEdges() {
//		return edges;
//	}
	
	static public void main(String[] args) {
		
		GNG gng = new GNG();
				
	}
}
