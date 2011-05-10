package memagents.memory.gng;

//========================================================================== ;
//                                                                         ;
//  Copyright (1996-1998)  Hartmut S. Loos                                 ;
//                                                                         ;
//  Institut fur Neuroinformatik   ND 03                                  ;
//  Ruhr-Universit"at Bochum                                               ;
//  44780 Bochum                                                           ;
//                                                                         ;
//  Tel  : +49 234 7007845                                                 ;
//  Email: Hartmut.Loos@neuroinformatik.ruhr-uni-bochum.de                 ;
//                                                                         ;
//  For version information and parameter explanation have a look at       ;
//  the file 'DemoGNG.java'.                                               ;
//                                                                         ;
//========================================================================== ;
//                                                                         ;
//Copyright 1996-1998 Hartmut S. Loos                                        ;
//                                                                         ;
//This program is free software; you can redistribute it and/or modify       ;
//it under the terms of the GNU General Public License as published by       ;
//the Free Software Foundation; either version 1, or (at your option)        ;
//any later version.                                                         ;
//                                                                         ;
//This program is distributed in the hope that it will be useful,            ;
//but WITHOUT ANY WARRANTY; without even the implied warranty of             ;
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the              ;
//GNU General Public License for more details.                               ;
//                                                                         ;
//You should have received a copy of the GNU General Public License          ;
//along with this program; if not, write to the Free Software                ;
//Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.                  ;
//                                                                         ;
//========================================================================== ;
// 
//  Rewritten by Vojtech Kopal for purpose of Bachelor Thesis ;
// ;
//=====;

import java.awt.*;

/**
 * A class which implements the algorithms. This class is an overkill. It
 * implements many functions/algorithms. The most important method is 'learn()'
 * which implements the different methods.
 * 
 */
public class ComputeGNG implements Runnable {
	/**
	 * The flag for debugging.
	 */
	protected final boolean DEBUG = false;
	/**
	 * The maximum number of elements to draw/calculate for the distributions.
	 */
	protected final int MAX_COMPLEX = 58;
	/**
	 * The maximum number of nodes.
	 */
	protected final int MAX_NODES = 250;
	/**
	 * The maximum number of edges (3 * maximum number of nodes).
	 */
	protected final int MAX_EDGES = 3 * MAX_NODES;
	/**
	 * The maximum number of Voronoi lines (5 * maximum number of nodes).
	 */
	protected final int MAX_V_LINES = 6 * MAX_NODES;
	/**
	 * The maximum stepsize.
	 */
	protected final int MAX_STEPSIZE = 200;
	/**
	 * The maximum number of discrete signals.
	 */
	protected final int MAX_DISCRETE_SIGNALS = 500;
	/**
	 * The maximum x size of the grid array.
	 */
	protected final int MAX_GRID_X = 30;
	/**
	 * The maximum y size of the grid array.
	 */
	protected final int MAX_GRID_Y = 15;
	/**
	 * The number of errors added for the mean square error.
	 */
	protected final int NUM_GLOBAL_GRAPH = 500;
	/**
	 * The factor for the ring-thickness (distribution).
	 */
	protected final float RING_FACTOR = 0.4f; // Factor < 1
	/**
	 * The version of the Growing Neural Gas Demo.
	 */
	protected final String DGNG_VERSION = "v1.5";
	/**
	 * The current maximum number of nodes.
	 */
	protected int maxNodes = 100;
	/**
	 * The current number of runs to insert a new node (GNG).
	 */
	protected int NUM_NEW_NODE = 600;
	/**
	 * The current number of runs.
	 */
	protected int numRun = 0;
	/**
	 * The temporal backup of a run.
	 */
	protected int numRunTmp = 0;
	/**
	 * The x-position of the actual signal.
	 */
	protected int SignalX = 0;
	/**
	 * The y-position of the actual signal.
	 */
	protected int SignalY = 0;
	/**
	 * The initial width of the drawing area. This value can only be changed by
	 * resizing the appletviewer.
	 */
	protected int width = 550;
	/**
	 * The initial height of the drawing area. This value can only be changed by
	 * resizing the appletviewer.
	 */
	protected int height = 310;

	/**
	 * The actual number of nodes.
	 */
	protected int nnodes = 0;
	/**
	 * The array of the actual used nodes.
	 */
	protected NodeGNG nodes[] = new NodeGNG[MAX_NODES];
	/**
	 * The sorted array of indices of nodes. The indices of the nodes are sorted
	 * by their distance from the actual signal. snodes[1] is the index of the
	 * nearest node.
	 */
	protected int snodes[] = new int[MAX_NODES + 1];
	/**
	 * The array of the last computed signals (x-coordinate).
	 */
	protected float lastSignalsX[] = new float[MAX_STEPSIZE];
	/**
	 * The array of the last computed signals (y-coordinate).
	 */
	protected float lastSignalsY[] = new float[MAX_STEPSIZE];
	/**
	 * The array of the discrete signals (x-coordinate).
	 */
	protected int discreteSignalsX[] = new int[MAX_DISCRETE_SIGNALS];
	/**
	 * The array of the discrete signals (y-coordinate).
	 */
	protected int discreteSignalsY[] = new int[MAX_DISCRETE_SIGNALS];
	/**
	 * The array of the best distance (discrete signals).
	 */
	protected float discreteSignalsD1[] = new float[MAX_DISCRETE_SIGNALS];
	/**
	 * The array of the second best distance (discrete signals).
	 */
	protected float discreteSignalsD2[] = new float[MAX_DISCRETE_SIGNALS];
	/**
	 * The array of the second best distance (discrete signals).
	 */
	protected FPoint Cbest[] = new FPoint[MAX_NODES];

	/**
	 * The current number of discrete signals.
	 */
	protected int numDiscreteSignals = 500;
	/**
	 * The actual number of edges.
	 */
	protected int nedges = 0;
	/**
	 * The array of the actual used edges.
	 */
	protected EdgeGNG edges[] = new EdgeGNG[MAX_EDGES];

	//protected Thread relaxer;

	/**
	 * The flag for playing the sound for a new inserted node.
	 */
	protected boolean insertedSoundB = false;
	/**
	 * The flag for a white background. Useful for making hardcopies
	 */
	protected boolean whiteB = false;
	/**
	 * The flag for random init. The nodes will be placed only in the specified
	 * distribution or not.
	 */
	protected boolean rndInitB = false;
	/**
	 * The flag for entering the fine-tuning phase (GG).
	 */
	protected boolean fineTuningB = false;
	/**
	 * The flag for showing the signal. This variable can be set by the user and
	 * shows the last input signals.
	 */
	protected boolean signalsB = false;
	/**
	 * The flag for inserting new nodes. This variable can be set by the user.
	 * If true no new nodes are inserted (GNG, GG).
	 */
	protected boolean noNodesB = false;
	/**
	 * The flag for stopping the demo. This variable can be set by the user. If
	 * true no calculation is done.
	 */
	protected boolean stopB = false;
	/**
	 * The flag for the sound. This variable can be set by the user. If false no
	 * sound is played.
	 */
	protected boolean soundB = false;
	/**
	 * The flag for the teach-mode. This variable can be set by the user. If
	 * true a legend is displayed which discribe the new form and color of some
	 * nodes. Furthermore all calculation is very slow.
	 */
	protected boolean teachB = false;
	/**
	 * The flag for variable movement (HCL). This variable can be set by the
	 * user.
	 */
	protected boolean variableB = false;
	/**
	 * The flag for displaying the edges. This variable can be set by the user.
	 */
	protected boolean edgesB = true;
	/**
	 * The flag for displaying the nodes. This variable can be set by the user.
	 */
	protected boolean nodesB = true;
	/**
	 * The flag for displaying the error graph. This variable can be set by the
	 * user.
	 */
	protected boolean errorGraphB = false;
	/**
	 * The flag for the global error (more signals). This variable can be set by
	 * the user.
	 */
	protected boolean globalGraphB = true;
	/**
	 * The flag for displaying the Voronoi diagram. This variable can be set by
	 * the user.
	 */
	protected boolean voronoiB = false;
	/**
	 * The flag for displaying the Delaunay triangulation. This variable can be
	 * set by the user.
	 */
	protected boolean delaunayB = false;
	/**
	 * The flag for any moved nodes (to compute the Voronoi diagram/Delaunay
	 * triangulation).
	 */
	protected boolean nodesMovedB = true;
	/**
	 * The flag for using utility (GNG-U).
	 */
	protected boolean utilityGNGB = false;
	/**
	 * The flag for changed number of nodes.
	 */
	protected boolean nNodesChangedB = true;
	/**
	 * The flag for only discrete signals
	 */
	protected boolean discreteSigB = true;
	/**
	 * The flag for LBG-U method
	 */
	protected boolean LBG_U_B = false;
	/**
	 * The flag for end of calculation (LBG)
	 */
	protected boolean readyLBG_B = false;
	/**
	 * The selected distribution. This variable can be set by the user.
	 */
	protected int distribution = 0;
	/**
	 * The selected algorithm. This variable can be set by the user.
	 */
	protected int algo = 0;
	/**
	 * The current maximum number to delete an old edge (GNG,NGwCHL). This
	 * variable can be set by the user.
	 */
	protected int MAX_EDGE_AGE = 88;
	/**
	 * The current number of calculations done in one step. This variable can be
	 * set by the user. After <TT> stepSize </TT> calculations the result is
	 * displayed.
	 */
	protected int stepSize = 50;
	/**
	 * This vaiable determines how long the compute thread sleeps. In this time
	 * the user can interact with the program. Slow machines and/or slow
	 * WWW-browsers need more time than fast machines and/or browsers. This
	 * variable can be set by the user.
	 */
	protected int speed = 400;
	/**
	 * The actual x size of the grid array.
	 */
	protected int grid_x = 0;
	/**
	 * The actual y size of the grid array.
	 */
	protected int grid_y = 0;
	/**
	 * The direction factor for the x axis (-1 or 1) used for the 'Moving
	 * Rectangle' distribution
	 */
	protected int bounceX = -1;
	/**
	 * The direction factor for the y axis (-1 or 1) used for the 'Moving
	 * Rectangle' distribution
	 */
	protected int bounceY = -1;
	/**
	 * The x coordinate for the 'Jumping Rectangle' and 'R.Mouse' distribution
	 */
	protected int jumpX = 250;
	/**
	 * The y coordinate for the 'Jumping Rectangle' and 'R.Mouse' distribution
	 */
	protected int jumpY = 250;
	/**
	 * Stores the old x value of the remainder in order to detect the bounce
	 * (used for the 'Moving Rectangle' distribution)
	 */
	protected double bounceX_old = 1;
	/**
	 * Stores the old y value of the remainder in order to detect the bounce
	 * (used for the 'Moving Rectangle' distribution)
	 */
	protected double bounceY_old = 1;

	/**
	 * The value epsilon for the HCL algorithm. This variable can be set by the
	 * user.
	 */
	protected float epsilon = 0.1f;
	/**
	 * The value epsilon for the GNG algorithm (winner). This variable can be
	 * set by the user.
	 */
	protected float epsilonGNG = 0.1f;
	/**
	 * The value epsilon for the GNG algorithm (second). This variable can be
	 * set by the user.
	 */
	protected float epsilonGNG2 = 0.001f;
	/**
	 * The value alpha for the GNG algorithm. This variable can be set by the
	 * user.
	 */
	protected float alphaGNG = 0.5f;
	/**
	 * The value beta for the GNG algorithm. This variable can be set by the
	 * user.
	 */
	protected float betaGNG = 0.0005f;
	/**
	 * The utility factor for the GNG-U algorithm. This variable can be set by
	 * the user.
	 */
	protected float utilityGNG = 3.0f;
	/**
	 * The decay factor for utility
	 */
	protected float forgetFactorUtility = 1.0f - betaGNG;
	/**
	 * The factor to forget old values.
	 */
	protected float forgetFactor = 1.0f - betaGNG;
	/**
	 * The value lambda initial for the NG,NGwCHL,GG algorithms. This variable
	 * can be set by the user.
	 */
	protected float l_i = 30.0f;
	/**
	 * The value lambda final for the NG,NGwCHL,GG algorithms. This variable can
	 * be set by the user.
	 */
	protected float l_f = 0.01f;
	/**
	 * The value epsiolon(t) for the NG,NGwCHL algorithms.
	 */
	protected float e_t = 0.0f;
	/**
	 * The value epsiolon initial for the NG,NGwCHL,GG algorithms. This variable
	 * can be set by the user.
	 */
	protected float e_i = 0.3f;
	/**
	 * The value epsiolon final for the NG,NGwCHL,GG algorithms. This variable
	 * can be set by the user.
	 */
	protected float e_f = 0.05f;
	/**
	 * The value t_max for the NG,NGwCHL,SOM algorithms. This variable can be
	 * set by the user.
	 */
	protected float t_max = 40000.0f;
	/**
	 * The value delete edge initial for the NGwCHL algorithm. This variable can
	 * be set by the user.
	 */
	protected float delEdge_i = 20.0f;
	/**
	 * The value delete edge final for the NGwCHL algorithm. This variable can
	 * be set by the user.
	 */
	protected float delEdge_f = 200.0f;
	/**
	 * The value sigma for the GG algorithm. This variable can be set by the
	 * user.
	 */
	protected float sigma = 0.9f;
	/**
	 * The value sigma_i for the SOM algorithm. This variable can be set by the
	 * user.
	 */
	protected float sigma_i = 5.0f;
	/**
	 * The value sigma_f for the SOM algorithm. This variable can be set by the
	 * user.
	 */
	protected float sigma_f = 5.0f;
	/**
	 * This value is displayed in the error graph.
	 */
	protected float valueGraph = 0.0f;
	/**
	 * This value contains the best error value for LBG-U up to now.
	 */
	protected float errorBestLBG_U = Float.MAX_VALUE;
	/**
	 * The string shown in the fine-tuning phase of the method GG.
	 */
	protected String fineTuningS = "";

	/**
	 * The constructor.
	 * 
	 * @param graph
	 *            The drawing area
	 */
	protected ComputeGNG() {
	}

	/**
	 * Add a node. The new node will be randomly placed within the given
	 * dimension.
	 * 
	 * @param d
	 *            The dimension of the initial drawing area
	 * @return The index of the new node
	 */
	protected int addNode(Dimension d) {
		if ((nnodes == MAX_NODES) || (nnodes >= maxNodes))
			return -1;

		NodeGNG n = new NodeGNG();

		if (rndInitB) {
			n.x = (float) (10 + (d.width - 20) * Math.random());
			n.y = (float) (10 + (d.height - 20) * Math.random());
		} else {
			getSignal(distribution);
			n.x = SignalX;
			n.y = SignalY;
		}

		n.nNeighbor = 0;
		if (algo == 5)
			n.moved = true;
		nodes[nnodes] = n;
		nNodesChangedB = true;
		return nnodes++;
	}

	/**
	 * Add a node. The new node will be placed at the given coordinates.
	 * 
	 * @param x
	 *            The x-coordinate of the new node
	 * @param y
	 *            The y-coordinate of the new node
	 * @return The index of the new node
	 */
	protected int addNode(int x, int y) {
		if ((nnodes == MAX_NODES) || (nnodes >= maxNodes))
			return -1;
		NodeGNG n = new NodeGNG();
		n.x = x;
		n.y = y;
		if (algo == 5)
			n.moved = true;
		nodes[nnodes] = n;
		nNodesChangedB = true;
		return nnodes++;
	}

	/**
	 * Add a node. The new node will be placed between the given nodes which
	 * must be connected. The existing edge is splitted. The new node gets the
	 * average of the interesting values of the two given nodes.
	 * 
	 * @param n1
	 *            The index of a node
	 * @param n2
	 *            The index of a node
	 * @return The index of the new node
	 */
	protected int insertNode(int n1, int n2) {
		if ((nnodes == MAX_NODES) || (nnodes >= maxNodes))
			return -1;
		if ((n1 < 0) || (n2 < 0))
			return -1;
		NodeGNG n = new NodeGNG();
		float dx = (nodes[n1].x - nodes[n2].x) / 2.0f;
		float dy = (nodes[n1].y - nodes[n2].y) / 2.0f;
		nodes[n1].error *= (1.0f - alphaGNG);
		nodes[n2].error *= (1.0f - alphaGNG);
		n.error = (nodes[n1].error + nodes[n2].error) / 2.0f;
		n.utility = (nodes[n1].utility + nodes[n2].utility) / 2.0f;
		n.x = nodes[n1].x - dx;
		n.y = nodes[n1].y - dy;
		n.inserted = true;
		nodes[nnodes] = n;
		deleteEdge(n1, n2);
		addEdge(n1, nnodes);
		addEdge(n2, nnodes);
		nNodesChangedB = true;

		return nnodes++;
	}

	/**
	 * Build a minimum-heap.
	 * 
	 * @param i
	 *            The start of the intervall
	 * @param k
	 *            The end of the intervall
	 */
	protected void reheap_min(int i, int k) {
		int j = i;
		int son;

		while (2 * j <= k) {
			if (2 * j + 1 <= k)
				if (nodes[snodes[2 * j]].dist < nodes[snodes[2 * j + 1]].dist)
					son = 2 * j;
				else
					son = 2 * j + 1;
			else
				son = 2 * j;

			if (nodes[snodes[j]].dist > nodes[snodes[son]].dist) {
				int exchange = snodes[j];
				snodes[j] = snodes[son];
				snodes[son] = exchange;
				j = son;
			} else
				return;
		}
	}

	/**
	 * Delete the given node.
	 * 
	 * @param n
	 *            The index of a node
	 */
	protected void deleteNode(int n) {
		NodeGNG node = nodes[n];
		int num = node.numNeighbors();
		int i;

		for (i = 0; i < num; i++)
			deleteEdge(n, node.neighbor(0));

		nNodesChangedB = true;
		nnodes--;
		nodes[n] = nodes[nnodes];
		nodes[nnodes] = null;

		// Now rename all occurances of nodes[nnodes] to nodes[n]
		for (i = 0; i < nnodes; i++)
			nodes[i].replaceNeighbor(nnodes, n);

		for (i = 0; i < nedges; i++)
			edges[i].replace(nnodes, n);

		return;
	}

	/**
	 * Connect two nodes or reset the age of their edge.
	 * 
	 * @param from
	 *            The index of the first node
	 * @param to
	 *            The index of the second node
	 */
	protected void addEdge(int from, int to) {
		if (nnodes < 2)
			return;

		if (nodes[from].isNeighbor(to)) {
			// Find edge(from,to) and reset age
			int i = findEdge(from, to);

			if (i != -1)
				edges[i].age = 0;

			return;
		}

		if (nedges == MAX_EDGES)
			return;

		if ((nodes[from].moreNeighbors()) && (nodes[to].moreNeighbors())) {
			nodes[to].addNeighbor(from);
			nodes[from].addNeighbor(to);
		} else
			return;

		// Add new edge
		EdgeGNG e = new EdgeGNG();
		e.from = from;
		e.to = to;
		edges[nedges] = e;
		nedges++;
	}

	/**
	 * Disconnect two nodes.
	 * 
	 * @param from
	 *            The index of the first node
	 * @param to
	 *            The index of the second node
	 */
	protected void deleteEdge(int from, int to) {
		int i = findEdge(from, to);
		if (i != -1) {
			nodes[edges[i].from].deleteNeighbor(edges[i].to);
			nodes[edges[i].to].deleteNeighbor(edges[i].from);
			nedges--;
			edges[i] = edges[nedges];
			edges[nedges] = null;
		}
		return;
	}

	/**
	 * Delete an edge.
	 * 
	 * @param from
	 *            The index of the edge
	 */
	protected void deleteEdge(int edgeNr) {
		nodes[edges[edgeNr].from].deleteNeighbor(edges[edgeNr].to);
		nodes[edges[edgeNr].to].deleteNeighbor(edges[edgeNr].from);
		nedges--;
		edges[edgeNr] = edges[nedges];
		edges[nedges] = null;
	}

	/**
	 * Find an edge. Find the edge between the two given nodes.
	 * 
	 * @param from
	 *            The index of the first node
	 * @param to
	 *            The index of the second node
	 * @return The index of the found edge or -1
	 */
	protected int findEdge(int from, int to) {
		for (int i = 0; i < nedges; i++)
			if (((edges[i].from == from) && (edges[i].to == to))
					|| ((edges[i].from == to) && (edges[i].to == from)))
				return i;
		return -1;
	}

	/**
	 * Age an edge. All edges starting from the given node are aged. Too old
	 * edges are deleted.
	 * 
	 * @param node
	 *            The index of a node
	 * @see ComputeGNG#MAX_EDGE_AGE
	 */
	protected void ageEdge(int node) {

		for (int i = nedges - 1; i > -1; i--) {
			if ((edges[i].from == node) || (edges[i].to == node))
				edges[i].age++;
			if (edges[i].age > MAX_EDGE_AGE)
				deleteEdge(i);
		}
	}

	/**
	 * Find neighbor with the highest error.
	 * 
	 * @param master
	 *            The index of a node
	 * @return The index of a node
	 */
	protected int worstErrorNeighbor(int master) {
		float ws = Float.MIN_VALUE;
		int wn = -1;
		int n = -1;
		int num = nodes[master].numNeighbors();
		for (int i = 0; i < num; i++) {
			n = nodes[master].neighbor(i);
			if (ws < nodes[n].error) {
				ws = nodes[n].error;
				wn = n;
			}
		}
		return wn;
	}

	/**
	 * Generate discrete signals for the given distribution. The result goes
	 * into the global arrays <TT> discreteSignalsX </TT> and
	 * <TT> discreteSignalsY </TT>.
	 * 
	 * @param distrib
	 *            The specified distribution
	 * @param w
	 *            The width of the drawing area
	 * @param h
	 *            The height of the drawing area
	 */
	protected void initDiscreteSignals(int distrib) {

	}

	/**
	 * Generate a signal for the given distribution. The result goes into the
	 * global variables <TT> SignalX </TT> and <TT> SignalY </TT>.
	 * 
	 * @param distrib
	 *            The specified distribution
	 */
	protected void getSignal(int distrib) {

	}

	/**
	 * Do resize calculations, start the learning method.
	 * 
	 */
	public void run() {
		int i;

		while (true) {

			// Relativate Positions
			Dimension d = size();
			if ((d.width != width) || (d.height != height)) {
				NodeGNG n;
				for (i = 0; i < nnodes; i++) {
					n = nodes[i];

					n.x = n.x * d.width / width;
					n.y = n.y * d.height / height;
				}

				width = d.width;
				height = d.height;
				initDiscreteSignals(distribution);
			}

			// Calculate the new positions
			if (!stopB) {
				learn();
				nodesMovedB = true;
			}

			if (teachB) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					break;
				}
			} else {

				try {
					Thread.sleep(speed);
				} catch (InterruptedException e) {
					break;
				}
			}
		}
	}

	/**
	 * Do the learning. An input signal is generated for the given distribution
	 * and forwarded to the switched algorithm. Available Algorithms (abbrev,
	 * case): Growing Neural Gas (GNG, 0), Hard Competitive Learning (HCL, 1),
	 * Neural Gas (NG, 2), Neural Gas with Competitive Hebbian Learning (NGwCHL,
	 * 3) and Competitive Hebbian Learning (CHL, 4). LBG (LBG, 5: but not in the
	 * switch). Growing Grid (GG, 6). Self-Organizing Map (SOM, 7).
	 * 
	 */
	protected synchronized void learn() {
		Dimension d = size();
		int nr1, nr2;
		int i, k;
		int numError, lowUtilityNode;
		int numNb;
		int toDelete;

		float bestDist, nextBestDist;
		float worstError, lowUtility;

		NodeGNG pick, pick2, n;

		SignalX = (int) d.width / 2;
		SignalY = (int) d.height / 2;

		valueGraph = 0.0f;

		for (k = 0; k < stepSize; k++) {

			numRun++;

			pick = nodes[0];
			pick2 = nodes[0];
			nr1 = 0;
			nr2 = 0;
			numError = 0;
			lowUtilityNode = 0;
			worstError = 0.0f;
			lowUtility = Float.MAX_VALUE;
			bestDist = Float.MAX_VALUE;
			nextBestDist = Float.MAX_VALUE;
			toDelete = -1;

			// Get a random point out of the selected distribution
			getSignal(distribution);

			// Save the signals
			lastSignalsX[k] = SignalX;
			lastSignalsY[k] = SignalY;

			// Locate the nearest node and prepare for the second
			for (i = 0; i < nnodes; i++) {
				n = nodes[i];
				n.winner = n.second = n.moved = false;

				if (((numRun % NUM_NEW_NODE) == 0) && (!noNodesB)
						&& (algo == 0)) {
					n.inserted = false;
				}

				// Mark Node without neighbors (one each run is enough)
				if (n.numNeighbors() == 0) {
					toDelete = i;
				}

				// Calculate distance
				n.dist = (n.x - SignalX) * (n.x - SignalX) + (n.y - SignalY)
						* (n.y - SignalY);
				// Reduce error and utility
				n.error *= forgetFactor;
				n.utility *= forgetFactorUtility;

				// Calculate node with best distance
				if (n.dist < bestDist) {
					pick2 = pick;
					nr2 = nr1;
					pick = n;
					nr1 = i;
					nextBestDist = bestDist;
					bestDist = n.dist;
				}

				// Calculate node with worst Error
				if (n.error > worstError) {
					worstError = n.error;
					numError = i;
				}
				// Calculate most useless node (GNG-U)
				if (n.utility < lowUtility) {
					lowUtility = n.utility;
					lowUtilityNode = i;
				}
			}

			valueGraph += bestDist;

			// Mark winner for teach-mode
			pick.winner = true;
			pick.x_old = pick.x;
			pick.y_old = pick.y;

			// Find second node (continued)
			if (nr1 == nr2) {
				nr2++;
				nextBestDist = Float.MAX_VALUE;
				pick2 = nodes[nr2];
			}

			for (i = nr1 + 1; i < nnodes; i++) {
				n = nodes[i];
				if (n.dist < nextBestDist) {
					pick2 = n;
					nr2 = i;
					nextBestDist = n.dist;
				}
			}

			// Mark second for teach-mode
			pick2.second = true;
			pick2.x_old = pick2.x;
			pick2.y_old = pick2.y;

			// Adapt picked nodes
			// Winner:
			pick.x += epsilonGNG * (SignalX - pick.x);
			pick.y += epsilonGNG * (SignalY - pick.y);
			numNb = pick.numNeighbors();

			// Neighbors:
			int nn;
			for (i = 0; i < numNb; i++) {
				nn = pick.neighbor(i);

				nodes[nn].moved = true;
				pick2.x_old = pick2.x;
				pick2.y_old = pick2.y;

				nodes[nn].x += epsilonGNG2 * (SignalX - nodes[nn].x);
				nodes[nn].y += epsilonGNG2 * (SignalY - nodes[nn].y);
			}

			// Calculate error
			pick.error += bestDist;

			// Calculate utility for GNG-U
			pick.utility += (nextBestDist - bestDist);

			// Connect the nodes
			addEdge(nr1, nr2);

			// Calculate the age of the connected edges and delete too old edges
			ageEdge(nr1);

			// Prove inserting node and insert if neccessary
			if ((numRun % NUM_NEW_NODE) == 0) {
				if (!noNodesB) {
					insertedSoundB = (-1 != insertNode(numError,
							worstErrorNeighbor(numError)));
				}
			}

			// Delete Node without Neighbors (not GNG-U)
			if ((toDelete != -1) && (nnodes > 2) && !utilityGNGB)
				deleteNode(toDelete);
			// Delete Node with very low utility
			else {
				if (worstError > lowUtility * utilityGNG) {
					if (utilityGNGB && (nnodes > 2)) {
						deleteNode(lowUtilityNode);
					}
					if (DEBUG)
						System.out.println("Utlity-delete (" + lowUtilityNode
								+ "): " + worstError + " > " + lowUtility
								+ " * " + utilityGNG);
					// utilityNode = lowUtility;

				} else if (utilityGNGB && (nnodes > 2) && (nnodes > maxNodes)) {
					deleteNode(lowUtilityNode);
				}
			}

		} // for

	}

	public Dimension size() {
		return new Dimension(100, 100);
	}
}
