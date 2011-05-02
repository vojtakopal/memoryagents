package demogng;
// ========================================================================== ;
//                                                                            ;
//     Copyright (1996-1998)  Hartmut S. Loos                                 ;
//                                                                            ;
//     Institut f"ur Neuroinformatik   ND 03                                  ;
//     Ruhr-Universit"at Bochum                                               ;
//     44780 Bochum                                                           ;
//                                                                            ;
//     Tel  : +49 234 7007845                                                 ;
//     Email: Hartmut.Loos@neuroinformatik.ruhr-uni-bochum.de                 ;
//                                                                            ;
//     For version information and parameter explanation have a look at       ;
//     the file 'DemoGNG.java'.                                               ;
//                                                                            ;
// ========================================================================== ;
//                                                                            ;
// Copyright 1996-1998 Hartmut S. Loos                                        ;
//                                                                            ;
// This program is free software; you can redistribute it and/or modify       ;
// it under the terms of the GNU General Public License as published by       ;
// the Free Software Foundation; either version 1, or (at your option)        ;
// any later version.                                                         ;
//                                                                            ;
// This program is distributed in the hope that it will be useful,            ;
// but WITHOUT ANY WARRANTY; without even the implied warranty of             ;
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the              ;
// GNU General Public License for more details.                               ;
//                                                                            ;
// You should have received a copy of the GNU General Public License          ;
// along with this program; if not, write to the Free Software                ;
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.                  ;
//                                                                            ;
// ========================================================================== ;

import java.awt.*;

/**
 * A class which implements the algorithms.
 * This class is an overkill. It implements many functions/algorithms.
 * The most important method is 'learn()' which implements the different
 * methods.
 * 
 */
public class ComputeGNG extends Panel implements Runnable {
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
  protected final float RING_FACTOR = 0.4f;	// Factor < 1 
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
   * The initial width of the drawing area.
   * This value can only be changed by resizing the appletviewer.
   */
  protected int INIT_WIDTH = 550;
  /**
   * The initial height of the drawing area.
   * This value can only be changed by resizing the appletviewer.
   */
  protected int INIT_HEIGHT = 310;

  protected DemoGNG graph;
  /**
   * The actual number of nodes.
   */
  protected int nnodes = 0;
  /**
   * The array of the actual used nodes.
   */
  protected NodeGNG nodes[] = new NodeGNG[MAX_NODES];
  /**
   * The sorted array of indices of nodes.
   * The indices of the nodes are sorted by their distance from the actual
   * signal. snodes[1] is the index of the nearest node.
   */
  protected int snodes[] = new int[MAX_NODES + 1];
  /**
   * This array of sites is sorted by y-coordinate (2nd y-coordinate).
   * vsites[1] is the index of the bottom node.
   */
  protected SiteVoronoi vsites[] = new SiteVoronoi[MAX_NODES + 1];
  /**
   * The array of the nodes in the grid.
   */
  protected GridNodeGNG grid[][] = new GridNodeGNG[MAX_GRID_X][MAX_GRID_Y];
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
  /**
   * The actual number of Voronoi lines.
   */
  protected int nlines = 0;
  /**
   * The array of the actual used lines.
   */
  protected LineGNG lines[] = new LineGNG[MAX_V_LINES];
  /**
   * The array of boolean to distinguish between Voronoi and Delaunay lines.
   */
  protected boolean vd[] = new boolean[MAX_V_LINES];

  Thread relaxer;
  GraphGNG errorGraph;

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
   *  distribution or not.
   */
  protected boolean rndInitB = false;
  /**
   * The flag for entering the fine-tuning phase (GG).
   */
  protected boolean fineTuningB = false;
  /**
   * The flag for showing the signal.
   *  This variable can be set by the user and shows the last input signals.
   */
  protected boolean signalsB = false;
  /**
   * The flag for inserting new nodes.
   *  This variable can be set by the user. If true no new nodes are
   *  inserted (GNG, GG).
   */
  protected boolean noNodesB = false;
  /**
   * The flag for stopping the demo.
   *  This variable can be set by the user. If true no calculation is done.
   */
  protected boolean stopB = false;
  /**
   * The flag for the sound.
   *  This variable can be set by the user. If false no sound is played.
   */
  protected boolean soundB = false;
  /**
   * The flag for the teach-mode.
   *  This variable can be set by the user. If true a legend is displayed
   *  which discribe the new form and color of some nodes. Furthermore
   *  all calculation is very slow.
   */
  protected boolean teachB = false;
  /**
   * The flag for variable movement (HCL).
   *  This variable can be set by the user. 
   */
  protected boolean variableB = false;
  /**
   * The flag for displaying the edges.
   *  This variable can be set by the user. 
   */
  protected boolean edgesB = true;
  /**
   * The flag for displaying the nodes.
   *  This variable can be set by the user. 
   */
  protected boolean nodesB = true;
  /**
   * The flag for displaying the error graph.
   *  This variable can be set by the user. 
   */
  protected boolean errorGraphB = false;
  /**
   * The flag for the global error (more signals).
   *  This variable can be set by the user. 
   */
  protected boolean globalGraphB = true;
  /**
   * The flag for displaying the Voronoi diagram.
   *  This variable can be set by the user. 
   */
  protected boolean voronoiB = false;
  /**
   * The flag for displaying the Delaunay triangulation.
   *  This variable can be set by the user. 
   */
  protected boolean delaunayB = false;
  /**
   * The flag for any moved nodes (to compute the Voronoi diagram/Delaunay
   *  triangulation).
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
   * The selected distribution.
   *  This variable can be set by the user. 
   */
  protected int distribution = 0;
  /**
   * The selected algorithm.
   *  This variable can be set by the user. 
   */
  protected int algo = 0;
  /**
   * The current maximum number to delete an old edge (GNG,NGwCHL).
   *  This variable can be set by the user. 
   */
  protected int MAX_EDGE_AGE = 88;
  /**
   * The current number of calculations done in one step. 
   *  This variable can be set by the user. After <TT> stepSize </TT>
   *  calculations the result is displayed.
   */
  protected int stepSize = 50;
  /**
   * This vaiable determines how long the compute thread sleeps. In this time
   *  the user can interact with the program. Slow machines and/or slow
   *  WWW-browsers need more time than fast machines and/or browsers.
   *  This variable can be set by the user.
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
   * The direction factor for the x axis (-1 or 1) used for the
   * 'Moving Rectangle' distribution
   */
  protected int bounceX = -1;
  /**
   * The direction factor for the y axis (-1 or 1) used for the
   * 'Moving Rectangle' distribution
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
   * The value epsilon for the HCL algorithm.
   *  This variable can be set by the user.
   */
  protected float epsilon = 0.1f;
  /**
   * The value epsilon for the GNG algorithm (winner).
   *  This variable can be set by the user.
   */
  protected float epsilonGNG = 0.1f;
  /**
   * The value epsilon for the GNG algorithm (second).
   *  This variable can be set by the user.
   */
  protected float epsilonGNG2 = 0.001f;
  /**
   * The value alpha for the GNG algorithm.
   *  This variable can be set by the user.
   */
  protected float alphaGNG = 0.5f;
  /**
   * The value beta for the GNG algorithm.
   *  This variable can be set by the user.
   */
  protected float betaGNG = 0.0005f;
  /**
   * The utility factor for the GNG-U algorithm.
   *  This variable can be set by the user.
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
   * The value lambda initial for the NG,NGwCHL,GG algorithms.
   *  This variable can be set by the user.
   */
  protected float l_i = 30.0f;
  /**
   * The value lambda final for the NG,NGwCHL,GG algorithms.
   *  This variable can be set by the user.
   */
  protected float l_f = 0.01f;
  /**
   * The value epsiolon(t) for the NG,NGwCHL algorithms.
   */
  protected float e_t = 0.0f;
  /**
   * The value epsiolon initial for the NG,NGwCHL,GG algorithms.
   *  This variable can be set by the user.
   */
  protected float e_i = 0.3f;
  /**
   * The value epsiolon final for the NG,NGwCHL,GG algorithms.
   *  This variable can be set by the user.
   */
  protected float e_f = 0.05f;
  /**
   * The value t_max for the NG,NGwCHL,SOM algorithms.
   *  This variable can be set by the user.
   */
  protected float t_max = 40000.0f;
  /**
   * The value delete edge initial for the NGwCHL algorithm.
   *  This variable can be set by the user.
   */
  protected float delEdge_i = 20.0f;
  /**
   * The value delete edge final for the NGwCHL algorithm.
   *  This variable can be set by the user.
   */
  protected float delEdge_f = 200.0f;
  /**
   * The value sigma for the GG algorithm.
   *  This variable can be set by the user.
   */
  protected float sigma = 0.9f;
  /**
   * The value sigma_i for the SOM algorithm.
   *  This variable can be set by the user.
   */
  protected float sigma_i = 5.0f;
  /**
   * The value sigma_f for the SOM algorithm.
   *  This variable can be set by the user.
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
   * @param graph	The drawing area
   */
  protected ComputeGNG(DemoGNG graph) {
    this.graph = graph;
  }

  /**
   * Add a node. The new node will be randomly placed within the 
   *  given dimension.
   * 
   * @param d          The dimension of the initial drawing area
   * @return           The index of the new node
   */
  protected int addNode(Dimension d) {
    if ( (nnodes == MAX_NODES) || (nnodes >= maxNodes) )
      return -1;

    NodeGNG n = new NodeGNG();

	if (rndInitB) {
	  n.x = (float) (10 + (d.width-20) * Math.random());
	  n.y = (float) (10 + (d.height-20) * Math.random());
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
   * Add a node. The new node will be placed at the
   *  given coordinates.
   * 
   * @param x          The x-coordinate of the new node
   * @param y          The y-coordinate of the new node
   * @return           The index of the new node
   */
  protected int addNode(int x, int y) {
    if ( (nnodes == MAX_NODES) || (nnodes >= maxNodes) )
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
   * Add a node. The new node will be placed between the
   *  given nodes which must be connected. The existing edge is splitted.
   *  The new node gets the average of the interesting values of
   *  the two given nodes.
   * 
   * @param n1         The index of a node
   * @param n2         The index of a node
   * @return           The index of the new node
   */
  protected int insertNode(int n1, int n2) {
    if ( (nnodes == MAX_NODES) || (nnodes >= maxNodes) )
      return -1;
    if ( (n1 < 0) || (n2 < 0) )
      return -1;
    NodeGNG n = new NodeGNG();
    float dx = (nodes[n1].x - nodes[n2].x) / 2.0f;
    float dy = (nodes[n1].y - nodes[n2].y) / 2.0f;
    nodes[n1].error *= (1.0f - alphaGNG);
    nodes[n2].error *= (1.0f - alphaGNG);
    n.error = (nodes[n1].error + nodes[n2].error)/2.0f;
    n.utility = (nodes[n1].utility + nodes[n2].utility)/2.0f;
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
   * Add a node into the grid. The new node will be randomly placed within the 
   *  given dimension.
   * 
   * @param x          The x coordinate of the grid array
   * @param y          The y coordinate of the grid array
   * @param d          The dimension of the initial drawing area
   * @return           The index of the new node
   */
  protected int addGridNode(int x, int y, Dimension d) {
    if ( (x > MAX_GRID_X) || (y > MAX_GRID_Y) )
      return -1;

	int n = addNode(d);
	nodes[n].x_grid = x;
	nodes[n].y_grid = y;

	grid[x][y] = new GridNodeGNG(n, nodes[n]);

    return n;
  }

  /**
   * Initialize the grid. The new nodes will be randomly placed within the 
   *  given dimension.
   * 
   * @param x          The x size of the grid array
   * @param y          The y size of the grid array
   * @param d          The dimension of the initial drawing area
   */
  protected void initGrid(int x, int y, Dimension d) {
    if ( (x > MAX_GRID_X) || (y > MAX_GRID_Y) )
      return;
	int i, j;

	if (algo == 7)
	  maxNodes = x * y;

	for (i = 0; i < x; i++)
	  for (j = 0; j < y; j++)
		addGridNode(i, j, d);

	grid_x = x;
	grid_y = y;

	for (i = 0; i < grid_x; i++) {
	  for (j = 0; j < grid_y; j++) {
		if ( i < (grid_x - 1) )
		  addEdge(grid[i][j].index, grid[i+1][j].index);
		if ( j < (grid_y - 1) )
		  addEdge(grid[i][j].index, grid[i][j+1].index);
	  }
	}
  }

  /**
   * Prepare to insert a row or column into the grid.
   * 
   * @return		The index of the last inserted node or -1
   */
  protected int insertGrid() {
	int tau = 0;
	int x = 0;
	int y = 0;
	int n = -1;
	float d1 = 0, d2 = 0, d3 = 0, d4 = 0, max = 0;

	for (int i = 0; i < grid_x; i++) {
	  for (int j = 0; j < grid_y; j++) {
		// Find maximum resource value tau
		if ( grid[i][j].tau > tau ) {
		  tau = grid[i][j].tau;
		  x = i;
		  y = j;
		}
		grid[i][j].tau = 0;
		grid[i][j].node.inserted = false;
	  }
	}

	// Identify the neighbor with the most different reference vector
	// left neighbor
	if (x > 0)
	  d1 = (grid[x-1][y].node.x - grid[x][y].node.x) *
	       (grid[x-1][y].node.x - grid[x][y].node.x) +
	       (grid[x-1][y].node.y - grid[x][y].node.y) *
	       (grid[x-1][y].node.y - grid[x][y].node.y);

	// upper neighbor
	if (y > 0)
	  d2 = (grid[x][y-1].node.x - grid[x][y].node.x) *
	       (grid[x][y-1].node.x - grid[x][y].node.x) +
	       (grid[x][y-1].node.y - grid[x][y].node.y) *
	       (grid[x][y-1].node.y - grid[x][y].node.y);

	// right neighbor
	if (x < grid_x - 1)
	  d3 = (grid[x+1][y].node.x - grid[x][y].node.x) *
	       (grid[x+1][y].node.x - grid[x][y].node.x) +
	       (grid[x+1][y].node.y - grid[x][y].node.y) *
	       (grid[x+1][y].node.y - grid[x][y].node.y);

	// lower neighbor
	if (y < grid_y - 1)
	  d4 = (grid[x][y+1].node.x - grid[x][y].node.x) *
	       (grid[x][y+1].node.x - grid[x][y].node.x) +
	       (grid[x][y+1].node.y - grid[x][y].node.y) *
	       (grid[x][y+1].node.y - grid[x][y].node.y);
	
	max = Math.max(d1, Math.max(d2, Math.max(d3, d4)));

	if (max == d1)
	  n = insertColumn(x - 1);
	else if (max == d2)
	  n = insertRow(y - 1);
	else if (max == d3)
	  n = insertColumn(x);
	else
	  n = insertRow(y);

	return n;
  }

  /**
   * Add a row into the grid. The new row will be placed after index y.
   * 
   * @param y          The row index
   * @return           The index of the last inserted node or -1
   */
  protected int insertRow(int y) {
    if ( (grid_y == MAX_GRID_Y) || (nnodes + grid_x > maxNodes) )
      return -1;

	int n = -1;
	int i, j;

	// Insert nodes for the new row
	for (i = 0; i < grid_x; i++) {
	  n = addNode(0,0);
	  nodes[n].x_grid = i;
	  nodes[n].y_grid = grid_y;
	  grid[i][grid_y] = new GridNodeGNG(n, nodes[n]);

	  // Add Edges
	  if (i != 0)
		addEdge(grid[i][grid_y].index, grid[i-1][grid_y].index);
	  addEdge(grid[i][grid_y].index, grid[i][grid_y-1].index);
	}

	// Now change the parameters (position)
	for (j = grid_y; j > y+1; j--) {
	  for (i = 0; i < grid_x; i++) {
		grid[i][j].node.x = grid[i][j-1].node.x;
		grid[i][j].node.y = grid[i][j-1].node.y;
	  }
	}

	for (i = 0; i < grid_x; i++) {
	  grid[i][y+1].node.x = (grid[i][y].node.x + grid[i][y+1].node.x)*0.5f;
	  grid[i][y+1].node.y = (grid[i][y].node.y + grid[i][y+1].node.y)*0.5f;
	  grid[i][y+1].node.inserted = true;
	}

	// Make the new row official
	grid_y++;

    return n;
  }

  /**
   * Add a column into the grid. The new column will be placed after index x.
   * 
   * @param x          The column index
   * @return           The index of the last inserted node or -1
   */
  protected int insertColumn(int x) {
    if ( (grid_x == MAX_GRID_X) || (nnodes + grid_y > maxNodes) )
      return -1;

	int n = -1;
	int i, j;

	// Insert nodes for the new Column
	for (j = 0; j < grid_y; j++) {
	  n = addNode(0,0);
	  nodes[n].x_grid = grid_x;
	  nodes[n].y_grid = j;
	  grid[grid_x][j] = new GridNodeGNG(n, nodes[n]);

	  // Add Edges
	  if (j != 0)
		addEdge(grid[grid_x][j].index, grid[grid_x][j-1].index);
	  addEdge(grid[grid_x][j].index, grid[grid_x-1][j].index);
	}

	// Now change the parameters (position)
	for (i = grid_x; i > x+1; i--) {
	  for (j = 0; j < grid_y; j++) {
		grid[i][j].node.x = grid[i-1][j].node.x;
		grid[i][j].node.y = grid[i-1][j].node.y;
	  }
	}

	for (j = 0; j < grid_y; j++) {
	  grid[x+1][j].node.x = (grid[x][j].node.x + grid[x+1][j].node.x)*0.5f;
	  grid[x+1][j].node.y = (grid[x][j].node.y + grid[x+1][j].node.y)*0.5f;
	  grid[x+1][j].node.inserted = true;
	}

	// Make the new row official
	grid_x++;

    return n;
  }

  /**
   * Sort the array of nodes. The result is in the <TT> vsite</TT>-array.
   *  The implemented sorting algorithm is heapsort.
   * 
   * @param n          The number of nodes to be sorted
   * @see ComputeGNG#vsites
   * @see ComputeGNG#reheapVoronoi
   */
  protected void sortSites(int n) {
    SiteVoronoi exchange;
	int i;

    // Initialize the sorted site array
    for (i = 1; i <= n; i++) {
      NodeGNG nd = nodes[i-1];
      SiteVoronoi sv = new SiteVoronoi();
      sv.coord.x = nd.x;
      sv.coord.y = nd.y;
      sv.sitenbr = i-1;
      sv.refcnt = 0;
      vsites[i] = sv;
    }
    nNodesChangedB = false;

    // Build a maximum heap
    for (i = n/2; i > 0; i--)
      reheapVoronoi(i, n);

    // Switch elements 1 and i then reheap
    for (i = n; i > 1; i--) {
      exchange = vsites[1];
      vsites[1] = vsites[i];
      vsites[i] = exchange;
      reheapVoronoi(1, i-1);
    }
  }

  /**
   * Build a maximum-heap. The result is in the <TT> vsite</TT>-array.
   * 
   * @param i          The start of the intervall
   * @param k          The end of the intervall
   * @see ComputeGNG#vsites
   * @see ComputeGNG#sortSites
   */
  protected void reheapVoronoi(int i, int k) {
    int j = i;
    int son;
    
    while (2*j <= k) {
      if (2*j+1 <= k)
		if ( (vsites[2*j].coord.y > vsites[2*j+1].coord.y) ||
			 (vsites[2*j].coord.y == vsites[2*j+1].coord.y &&
			  vsites[2*j].coord.x > vsites[2*j+1].coord.x) )
		  son = 2*j;
		else
		  son = 2*j + 1;
      else
		son = 2*j;

      if ( (vsites[j].coord.y < vsites[son].coord.y) ||
		   (vsites[j].coord.y == vsites[son].coord.y &&
			vsites[j].coord.x < vsites[son].coord.x) ) {
		SiteVoronoi exchange = vsites[j];
		vsites[j] = vsites[son];
		vsites[son] = exchange;
		j = son;
      } else
		return;
    }
  }

  /**
   * Build a minimum-heap.
   * 
   * @param i          The start of the intervall
   * @param k          The end of the intervall
   */
  protected void reheap_min(int i, int k) {
    int j = i;
    int son;
    
    while (2*j <= k) {
      if (2*j+1 <= k)
		if (nodes[snodes[2*j]].dist < nodes[snodes[2*j+1]].dist)
		  son = 2*j;
		else
		  son = 2*j + 1;
	  else
		son = 2*j;

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
   * @param n          The index of a node
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
    for (i = 0 ; i < nnodes ; i++)
      nodes[i].replaceNeighbor(nnodes, n);
    for (i = 0 ; i < nedges ; i++)
      edges[i].replace(nnodes, n);

    return;
  }

  /**
   * Connect two nodes or reset the age of their edge. 
   * 
   * @param from          The index of the first node
   * @param to            The index of the second node
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

    if ( (nodes[from].moreNeighbors()) && (nodes[to].moreNeighbors()) ) {
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
   * @param from          The index of the first node
   * @param to            The index of the second node
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
   * @param from          The index of the edge
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
   * @param from          The index of the first node
   * @param to            The index of the second node
   * @return              The index of the found edge or -1
   */
  protected int findEdge(int from, int to) {
    
    for (int i = 0; i < nedges; i++)
      if (( (edges[i].from == from) && (edges[i].to == to) ) || 
		  ( (edges[i].from == to) && (edges[i].to == from) ) )
		return i;
    return -1;
  }

  /**
   * Age an edge. All edges starting from the given node are aged.
   *  Too old edges are deleted.
   * 
   * @param node          The index of a node
   * @see ComputeGNG#MAX_EDGE_AGE
   */
  protected void ageEdge(int node) {
    
    for (int i = nedges - 1; i > -1; i--) {
      if ( (edges[i].from == node) || (edges[i].to == node) )
		edges[i].age++;
      if (edges[i].age > MAX_EDGE_AGE)
		deleteEdge(i);
    }
  }

  /**
   * Find neighbor with the highest error.
   * 
   * @param master          The index of a node
   * @return                The index of a node
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
   * Generate discrete signals for the given distribution.
   *  The result goes into the global arrays <TT> discreteSignalsX </TT>
   *  and <TT> discreteSignalsY </TT>.
   * 
   * @param distrib          The specified distribution
   * @param w          		 The width of the drawing area
   * @param h          		 The height of the drawing area
   */
  protected void initDiscreteSignals(int distrib) {
    Dimension d = size();
	int w = d.width;
	int h = d.height;
	int kx = 1;
	int ky = 1;
	int l = 0;
	int dSX[] = discreteSignalsX;
	int dSY[] = discreteSignalsY;

    if (distrib != 7) {
      for (int i = 0; i < MAX_DISCRETE_SIGNALS; i++) {
		getSignal(distrib);
		discreteSignalsX[i] = SignalX;
		discreteSignalsY[i] = SignalY;
      }
    } else {
	  if (w > h) {
		kx = (int)(w/4);
		l = h;
	  } else {
		ky = (int)(h/4);
		l = w;
	  }
		dSX[0]=(int)(kx+l*0.13814); dSY[0]=(int)(ky+l*(1.0-0.29675));
		dSX[1]=(int)(kx+l*0.19548); dSY[1]=(int)(ky+l*(1.0-0.09674));
		dSX[2]=(int)(kx+l*0.73576); dSY[2]=(int)(ky+l*(1.0-0.86994));
		dSX[3]=(int)(kx+l*0.73065); dSY[3]=(int)(ky+l*(1.0-0.19024));
		dSX[4]=(int)(kx+l*0.83479); dSY[4]=(int)(ky+l*(1.0-0.34258));
		dSX[5]=(int)(kx+l*0.13184); dSY[5]=(int)(ky+l*(1.0-0.56509));
		dSX[6]=(int)(kx+l*0.15959); dSY[6]=(int)(ky+l*(1.0-0.59065));
		dSX[7]=(int)(kx+l*0.21696); dSY[7]=(int)(ky+l*(1.0-0.1402));
		dSX[8]=(int)(kx+l*0.61592); dSY[8]=(int)(ky+l*(1.0-0.16657));
		dSX[9]=(int)(kx+l*0.10513); dSY[9]=(int)(ky+l*(1.0-0.21708));
		dSX[10]=(int)(kx+l*0.1864); dSY[10]=(int)(ky+l*(1.0-0.1454));
		dSX[11]=(int)(kx+l*0.36696); dSY[11]=(int)(ky+l*(1.0-0.74924));
		dSX[12]=(int)(kx+l*0.18345); dSY[12]=(int)(ky+l*(1.0-0.80946));
		dSX[13]=(int)(kx+l*0.8509); dSY[13]=(int)(ky+l*(1.0-0.38268));
		dSX[14]=(int)(kx+l*0.19476); dSY[14]=(int)(ky+l*(1.0-0.74262));
		dSX[15]=(int)(kx+l*0.49164); dSY[15]=(int)(ky+l*(1.0-0.65776));
		dSX[16]=(int)(kx+l*0.86552); dSY[16]=(int)(ky+l*(1.0-0.38373));
		dSX[17]=(int)(kx+l*0.73176); dSY[17]=(int)(ky+l*(1.0-0.84414));
		dSX[18]=(int)(kx+l*0.71978); dSY[18]=(int)(ky+l*(1.0-0.86979));
		dSX[19]=(int)(kx+l*0.83034); dSY[19]=(int)(ky+l*(1.0-0.36613));
		dSX[20]=(int)(kx+l*0.39886); dSY[20]=(int)(ky+l*(1.0-0.71479));
		dSX[21]=(int)(kx+l*0.09955); dSY[21]=(int)(ky+l*(1.0-0.20342));
		dSX[22]=(int)(kx+l*0.07091); dSY[22]=(int)(ky+l*(1.0-0.17197));
		dSX[23]=(int)(kx+l*0.21896); dSY[23]=(int)(ky+l*(1.0-0.10398));
		dSX[24]=(int)(kx+l*0.72465); dSY[24]=(int)(ky+l*(1.0-0.13984));
		dSX[25]=(int)(kx+l*0.71034); dSY[25]=(int)(ky+l*(1.0-0.87981));
		dSX[26]=(int)(kx+l*0.83547); dSY[26]=(int)(ky+l*(1.0-0.36065));
		dSX[27]=(int)(kx+l*0.13907); dSY[27]=(int)(ky+l*(1.0-0.56451));
		dSX[28]=(int)(kx+l*0.62124); dSY[28]=(int)(ky+l*(1.0-0.20175));
		dSX[29]=(int)(kx+l*0.65543); dSY[29]=(int)(ky+l*(1.0-0.17331));
		dSX[30]=(int)(kx+l*0.72349); dSY[30]=(int)(ky+l*(1.0-0.14375));
		dSX[31]=(int)(kx+l*0.82495); dSY[31]=(int)(ky+l*(1.0-0.40116));
		dSX[32]=(int)(kx+l*0.76586); dSY[32]=(int)(ky+l*(1.0-0.82376));
		dSX[33]=(int)(kx+l*0.24648); dSY[33]=(int)(ky+l*(1.0-0.11987));
		dSX[34]=(int)(kx+l*0.14817); dSY[34]=(int)(ky+l*(1.0-0.59985));
		dSX[35]=(int)(kx+l*0.82663); dSY[35]=(int)(ky+l*(1.0-0.38964));
		dSX[36]=(int)(kx+l*0.37131); dSY[36]=(int)(ky+l*(1.0-0.72726));
		dSX[37]=(int)(kx+l*0.12176); dSY[37]=(int)(ky+l*(1.0-0.60139));
		dSX[38]=(int)(kx+l*0.73587); dSY[38]=(int)(ky+l*(1.0-0.86952));
		dSX[39]=(int)(kx+l*0.59645); dSY[39]=(int)(ky+l*(1.0-0.21302));
		dSX[40]=(int)(kx+l*0.39489); dSY[40]=(int)(ky+l*(1.0-0.63452));
		dSX[41]=(int)(kx+l*0.234); dSY[41]=(int)(ky+l*(1.0-0.10385));
		dSX[42]=(int)(kx+l*0.51314); dSY[42]=(int)(ky+l*(1.0-0.67151));
		dSX[43]=(int)(kx+l*0.13499); dSY[43]=(int)(ky+l*(1.0-0.56896));
		dSX[44]=(int)(kx+l*0.10815); dSY[44]=(int)(ky+l*(1.0-0.62515));
		dSX[45]=(int)(kx+l*0.35487); dSY[45]=(int)(ky+l*(1.0-0.65635));
		dSX[46]=(int)(kx+l*0.13939); dSY[46]=(int)(ky+l*(1.0-0.24579));
		dSX[47]=(int)(kx+l*0.22087); dSY[47]=(int)(ky+l*(1.0-0.20651));
		dSX[48]=(int)(kx+l*0.12274); dSY[48]=(int)(ky+l*(1.0-0.61131));
		dSX[49]=(int)(kx+l*0.47888); dSY[49]=(int)(ky+l*(1.0-0.65166));
		dSX[50]=(int)(kx+l*0.18836); dSY[50]=(int)(ky+l*(1.0-0.6895));
		dSX[51]=(int)(kx+l*0.2511); dSY[51]=(int)(ky+l*(1.0-0.12476));
		dSX[52]=(int)(kx+l*0.84242); dSY[52]=(int)(ky+l*(1.0-0.3685));
		dSX[53]=(int)(kx+l*0.70824); dSY[53]=(int)(ky+l*(1.0-0.18571));
		dSX[54]=(int)(kx+l*0.2548); dSY[54]=(int)(ky+l*(1.0-0.77552));
		dSX[55]=(int)(kx+l*0.3659); dSY[55]=(int)(ky+l*(1.0-0.64852));
		dSX[56]=(int)(kx+l*0.78094); dSY[56]=(int)(ky+l*(1.0-0.37826));
		dSX[57]=(int)(kx+l*0.34205); dSY[57]=(int)(ky+l*(1.0-0.7295));
		dSX[58]=(int)(kx+l*0.83349); dSY[58]=(int)(ky+l*(1.0-0.37511));
		dSX[59]=(int)(kx+l*0.35477); dSY[59]=(int)(ky+l*(1.0-0.68483));
		dSX[60]=(int)(kx+l*0.13761); dSY[60]=(int)(ky+l*(1.0-0.17267));
		dSX[61]=(int)(kx+l*0.46041); dSY[61]=(int)(ky+l*(1.0-0.72594));
		dSX[62]=(int)(kx+l*0.12945); dSY[62]=(int)(ky+l*(1.0-0.58863));
		dSX[63]=(int)(kx+l*0.27379); dSY[63]=(int)(ky+l*(1.0-0.14071));
		dSX[64]=(int)(kx+l*0.4097); dSY[64]=(int)(ky+l*(1.0-0.77705));
		dSX[65]=(int)(kx+l*0.7175); dSY[65]=(int)(ky+l*(1.0-0.87696));
		dSX[66]=(int)(kx+l*0.43969); dSY[66]=(int)(ky+l*(1.0-0.66972));
		dSX[67]=(int)(kx+l*0.48588); dSY[67]=(int)(ky+l*(1.0-0.63899));
		dSX[68]=(int)(kx+l*0.69263); dSY[68]=(int)(ky+l*(1.0-0.20386));
		dSX[69]=(int)(kx+l*0.7374); dSY[69]=(int)(ky+l*(1.0-0.8667));
		dSX[70]=(int)(kx+l*0.67306); dSY[70]=(int)(ky+l*(1.0-0.18347));
		dSX[71]=(int)(kx+l*0.21203); dSY[71]=(int)(ky+l*(1.0-0.12508));
		dSX[72]=(int)(kx+l*0.48821); dSY[72]=(int)(ky+l*(1.0-0.67574));
		dSX[73]=(int)(kx+l*0.45742); dSY[73]=(int)(ky+l*(1.0-0.67679));
		dSX[74]=(int)(kx+l*0.67982); dSY[74]=(int)(ky+l*(1.0-0.1421));
		dSX[75]=(int)(kx+l*0.13429); dSY[75]=(int)(ky+l*(1.0-0.56728));
		dSX[76]=(int)(kx+l*0.2402); dSY[76]=(int)(ky+l*(1.0-0.76521));
		dSX[77]=(int)(kx+l*0.15482); dSY[77]=(int)(ky+l*(1.0-0.178));
		dSX[78]=(int)(kx+l*0.71594); dSY[78]=(int)(ky+l*(1.0-0.15844));
		dSX[79]=(int)(kx+l*0.10534); dSY[79]=(int)(ky+l*(1.0-0.59961));
		dSX[80]=(int)(kx+l*0.44167); dSY[80]=(int)(ky+l*(1.0-0.69823));
		dSX[81]=(int)(kx+l*0.46529); dSY[81]=(int)(ky+l*(1.0-0.70682));
		dSX[82]=(int)(kx+l*0.13842); dSY[82]=(int)(ky+l*(1.0-0.56618));
		dSX[83]=(int)(kx+l*0.09876); dSY[83]=(int)(ky+l*(1.0-0.5795));
		dSX[84]=(int)(kx+l*0.12101); dSY[84]=(int)(ky+l*(1.0-0.57408));
		dSX[85]=(int)(kx+l*0.44963); dSY[85]=(int)(ky+l*(1.0-0.74847));
		dSX[86]=(int)(kx+l*0.12532); dSY[86]=(int)(ky+l*(1.0-0.56478));
		dSX[87]=(int)(kx+l*0.18264); dSY[87]=(int)(ky+l*(1.0-0.77186));
		dSX[88]=(int)(kx+l*0.80443); dSY[88]=(int)(ky+l*(1.0-0.35896));
		dSX[89]=(int)(kx+l*0.72038); dSY[89]=(int)(ky+l*(1.0-0.90205));
		dSX[90]=(int)(kx+l*0.24934); dSY[90]=(int)(ky+l*(1.0-0.77047));
		dSX[91]=(int)(kx+l*0.35552); dSY[91]=(int)(ky+l*(1.0-0.70131));
		dSX[92]=(int)(kx+l*0.49591); dSY[92]=(int)(ky+l*(1.0-0.71126));
		dSX[93]=(int)(kx+l*0.36426); dSY[93]=(int)(ky+l*(1.0-0.72803));
		dSX[94]=(int)(kx+l*0.21113); dSY[94]=(int)(ky+l*(1.0-0.08745));
		dSX[95]=(int)(kx+l*0.33412); dSY[95]=(int)(ky+l*(1.0-0.68345));
		dSX[96]=(int)(kx+l*0.17158); dSY[96]=(int)(ky+l*(1.0-0.226));
		dSX[97]=(int)(kx+l*0.69135); dSY[97]=(int)(ky+l*(1.0-0.26172));
		dSX[98]=(int)(kx+l*0.80362); dSY[98]=(int)(ky+l*(1.0-0.34908));
		dSX[99]=(int)(kx+l*0.49367); dSY[99]=(int)(ky+l*(1.0-0.61372));
		dSX[100]=(int)(kx+l*0.67809); dSY[100]=(int)(ky+l*(1.0-0.16071));
		dSX[101]=(int)(kx+l*0.42288); dSY[101]=(int)(ky+l*(1.0-0.7547));
		dSX[102]=(int)(kx+l*0.21535); dSY[102]=(int)(ky+l*(1.0-0.71766));
		dSX[103]=(int)(kx+l*0.26248); dSY[103]=(int)(ky+l*(1.0-0.0794));
		dSX[104]=(int)(kx+l*0.65766); dSY[104]=(int)(ky+l*(1.0-0.11433));
		dSX[105]=(int)(kx+l*0.81799); dSY[105]=(int)(ky+l*(1.0-0.36416));
		dSX[106]=(int)(kx+l*0.80867); dSY[106]=(int)(ky+l*(1.0-0.39382));
		dSX[107]=(int)(kx+l*0.2401); dSY[107]=(int)(ky+l*(1.0-0.83207));
		dSX[108]=(int)(kx+l*0.83016); dSY[108]=(int)(ky+l*(1.0-0.37551));
		dSX[109]=(int)(kx+l*0.30746); dSY[109]=(int)(ky+l*(1.0-0.78597));
		dSX[110]=(int)(kx+l*0.22122); dSY[110]=(int)(ky+l*(1.0-0.19961));
		dSX[111]=(int)(kx+l*0.81422); dSY[111]=(int)(ky+l*(1.0-0.39008));
		dSX[112]=(int)(kx+l*0.28025); dSY[112]=(int)(ky+l*(1.0-0.16485));
		dSX[113]=(int)(kx+l*0.42936); dSY[113]=(int)(ky+l*(1.0-0.70449));
		dSX[114]=(int)(kx+l*0.20721); dSY[114]=(int)(ky+l*(1.0-0.79412));
		dSX[115]=(int)(kx+l*0.1023); dSY[115]=(int)(ky+l*(1.0-0.59687));
		dSX[116]=(int)(kx+l*0.49873); dSY[116]=(int)(ky+l*(1.0-0.68088));
		dSX[117]=(int)(kx+l*0.44373); dSY[117]=(int)(ky+l*(1.0-0.60472));
		dSX[118]=(int)(kx+l*0.12955); dSY[118]=(int)(ky+l*(1.0-0.58045));
		dSX[119]=(int)(kx+l*0.40319); dSY[119]=(int)(ky+l*(1.0-0.64087));
		dSX[120]=(int)(kx+l*0.39597); dSY[120]=(int)(ky+l*(1.0-0.74223));
		dSX[121]=(int)(kx+l*0.37318); dSY[121]=(int)(ky+l*(1.0-0.74561));
		dSX[122]=(int)(kx+l*0.48026); dSY[122]=(int)(ky+l*(1.0-0.65002));
		dSX[123]=(int)(kx+l*0.09824); dSY[123]=(int)(ky+l*(1.0-0.15969));
		dSX[124]=(int)(kx+l*0.68454); dSY[124]=(int)(ky+l*(1.0-0.17986));
		dSX[125]=(int)(kx+l*0.11659); dSY[125]=(int)(ky+l*(1.0-0.30008));
		dSX[126]=(int)(kx+l*0.73836); dSY[126]=(int)(ky+l*(1.0-0.87819));
		dSX[127]=(int)(kx+l*0.37924); dSY[127]=(int)(ky+l*(1.0-0.72885));
		dSX[128]=(int)(kx+l*0.07252); dSY[128]=(int)(ky+l*(1.0-0.21803));
		dSX[129]=(int)(kx+l*0.22104); dSY[129]=(int)(ky+l*(1.0-0.81961));
		dSX[130]=(int)(kx+l*0.23872); dSY[130]=(int)(ky+l*(1.0-0.06629));
		dSX[131]=(int)(kx+l*0.27114); dSY[131]=(int)(ky+l*(1.0-0.77851));
		dSX[132]=(int)(kx+l*0.84307); dSY[132]=(int)(ky+l*(1.0-0.35729));
		dSX[133]=(int)(kx+l*0.83856); dSY[133]=(int)(ky+l*(1.0-0.38892));
		dSX[134]=(int)(kx+l*0.84041); dSY[134]=(int)(ky+l*(1.0-0.33806));
		dSX[135]=(int)(kx+l*0.72441); dSY[135]=(int)(ky+l*(1.0-0.84423));
		dSX[136]=(int)(kx+l*0.45169); dSY[136]=(int)(ky+l*(1.0-0.66888));
		dSX[137]=(int)(kx+l*0.7291); dSY[137]=(int)(ky+l*(1.0-0.85748));
		dSX[138]=(int)(kx+l*0.38792); dSY[138]=(int)(ky+l*(1.0-0.74045));
		dSX[139]=(int)(kx+l*0.69006); dSY[139]=(int)(ky+l*(1.0-0.88995));
		dSX[140]=(int)(kx+l*0.09004); dSY[140]=(int)(ky+l*(1.0-0.57847));
		dSX[141]=(int)(kx+l*0.20986); dSY[141]=(int)(ky+l*(1.0-0.21552));
		dSX[142]=(int)(kx+l*0.22969); dSY[142]=(int)(ky+l*(1.0-0.79372));
		dSX[143]=(int)(kx+l*0.2407); dSY[143]=(int)(ky+l*(1.0-0.78147));
		dSX[144]=(int)(kx+l*0.83483); dSY[144]=(int)(ky+l*(1.0-0.35725));
		dSX[145]=(int)(kx+l*0.74069); dSY[145]=(int)(ky+l*(1.0-0.87034));
		dSX[146]=(int)(kx+l*0.53127); dSY[146]=(int)(ky+l*(1.0-0.69099));
		dSX[147]=(int)(kx+l*0.73562); dSY[147]=(int)(ky+l*(1.0-0.89203));
		dSX[148]=(int)(kx+l*0.22449); dSY[148]=(int)(ky+l*(1.0-0.14296));
		dSX[149]=(int)(kx+l*0.74473); dSY[149]=(int)(ky+l*(1.0-0.85085));
		dSX[150]=(int)(kx+l*0.80492); dSY[150]=(int)(ky+l*(1.0-0.40119));
		dSX[151]=(int)(kx+l*0.66545); dSY[151]=(int)(ky+l*(1.0-0.14658));
		dSX[152]=(int)(kx+l*0.74401); dSY[152]=(int)(ky+l*(1.0-0.88545));
		dSX[153]=(int)(kx+l*0.16486); dSY[153]=(int)(ky+l*(1.0-0.81768));
		dSX[154]=(int)(kx+l*0.10909); dSY[154]=(int)(ky+l*(1.0-0.58963));
		dSX[155]=(int)(kx+l*0.36812); dSY[155]=(int)(ky+l*(1.0-0.71451));
		dSX[156]=(int)(kx+l*0.77083); dSY[156]=(int)(ky+l*(1.0-0.86754));
		dSX[157]=(int)(kx+l*0.19709); dSY[157]=(int)(ky+l*(1.0-0.16813));
		dSX[158]=(int)(kx+l*0.08257); dSY[158]=(int)(ky+l*(1.0-0.57901));
		dSX[159]=(int)(kx+l*0.81561); dSY[159]=(int)(ky+l*(1.0-0.38789));
		dSX[160]=(int)(kx+l*0.11613); dSY[160]=(int)(ky+l*(1.0-0.61403));
		dSX[161]=(int)(kx+l*0.16391); dSY[161]=(int)(ky+l*(1.0-0.10041));
		dSX[162]=(int)(kx+l*0.36024); dSY[162]=(int)(ky+l*(1.0-0.75178));
		dSX[163]=(int)(kx+l*0.73822); dSY[163]=(int)(ky+l*(1.0-0.84884));
		dSX[164]=(int)(kx+l*0.22963); dSY[164]=(int)(ky+l*(1.0-0.11442));
		dSX[165]=(int)(kx+l*0.01152); dSY[165]=(int)(ky+l*(1.0-0.27939));
		dSX[166]=(int)(kx+l*0.74314); dSY[166]=(int)(ky+l*(1.0-0.87522));
		dSX[167]=(int)(kx+l*0.22871); dSY[167]=(int)(ky+l*(1.0-0.134));
		dSX[168]=(int)(kx+l*0.14996); dSY[168]=(int)(ky+l*(1.0-0.54459));
		dSX[169]=(int)(kx+l*0.14354); dSY[169]=(int)(ky+l*(1.0-0.25589));
		dSX[170]=(int)(kx+l*0.0779); dSY[170]=(int)(ky+l*(1.0-0.2636));
		dSX[171]=(int)(kx+l*0.13208); dSY[171]=(int)(ky+l*(1.0-0.28005));
		dSX[172]=(int)(kx+l*0.2498); dSY[172]=(int)(ky+l*(1.0-0.75765));
		dSX[173]=(int)(kx+l*0.30859); dSY[173]=(int)(ky+l*(1.0-0.08592));
		dSX[174]=(int)(kx+l*0.03277); dSY[174]=(int)(ky+l*(1.0-0.25141));
		dSX[175]=(int)(kx+l*0.69026); dSY[175]=(int)(ky+l*(1.0-0.11579));
		dSX[176]=(int)(kx+l*0.70569); dSY[176]=(int)(ky+l*(1.0-0.20655));
		dSX[177]=(int)(kx+l*0.19796); dSY[177]=(int)(ky+l*(1.0-0.1327));
		dSX[178]=(int)(kx+l*0.10402); dSY[178]=(int)(ky+l*(1.0-0.18623));
		dSX[179]=(int)(kx+l*0.20623); dSY[179]=(int)(ky+l*(1.0-0.17315));
		dSX[180]=(int)(kx+l*0.14383); dSY[180]=(int)(ky+l*(1.0-0.16819));
		dSX[181]=(int)(kx+l*0.43416); dSY[181]=(int)(ky+l*(1.0-0.81161));
		dSX[182]=(int)(kx+l*0.21801); dSY[182]=(int)(ky+l*(1.0-0.1926));
		dSX[183]=(int)(kx+l*0.80582); dSY[183]=(int)(ky+l*(1.0-0.40684));
		dSX[184]=(int)(kx+l*0.47273); dSY[184]=(int)(ky+l*(1.0-0.66746));
		dSX[185]=(int)(kx+l*0.72923); dSY[185]=(int)(ky+l*(1.0-0.91807));
		dSX[186]=(int)(kx+l*0.21609); dSY[186]=(int)(ky+l*(1.0-0.14719));
		dSX[187]=(int)(kx+l*0.61592); dSY[187]=(int)(ky+l*(1.0-0.17603));
		dSX[188]=(int)(kx+l*0.25956); dSY[188]=(int)(ky+l*(1.0-0.74824));
		dSX[189]=(int)(kx+l*0.10157); dSY[189]=(int)(ky+l*(1.0-0.25437));
		dSX[190]=(int)(kx+l*0.34822); dSY[190]=(int)(ky+l*(1.0-0.74119));
		dSX[191]=(int)(kx+l*0.37535); dSY[191]=(int)(ky+l*(1.0-0.68263));
		dSX[192]=(int)(kx+l*0.11609); dSY[192]=(int)(ky+l*(1.0-0.25491));
		dSX[193]=(int)(kx+l*0.84751); dSY[193]=(int)(ky+l*(1.0-0.36326));
		dSX[194]=(int)(kx+l*0.48434); dSY[194]=(int)(ky+l*(1.0-0.71852));
		dSX[195]=(int)(kx+l*0.82894); dSY[195]=(int)(ky+l*(1.0-0.38072));
		dSX[196]=(int)(kx+l*0.23618); dSY[196]=(int)(ky+l*(1.0-0.78797));
		dSX[197]=(int)(kx+l*0.70894); dSY[197]=(int)(ky+l*(1.0-0.84481));
		dSX[198]=(int)(kx+l*0.21377); dSY[198]=(int)(ky+l*(1.0-0.08697));
		dSX[199]=(int)(kx+l*0.08777); dSY[199]=(int)(ky+l*(1.0-0.23077));
		dSX[200]=(int)(kx+l*0.4627); dSY[200]=(int)(ky+l*(1.0-0.68689));
		dSX[201]=(int)(kx+l*0.1064); dSY[201]=(int)(ky+l*(1.0-0.13423));
		dSX[202]=(int)(kx+l*0.34044); dSY[202]=(int)(ky+l*(1.0-0.71728));
		dSX[203]=(int)(kx+l*0.14377); dSY[203]=(int)(ky+l*(1.0-0.10488));
		dSX[204]=(int)(kx+l*0.83586); dSY[204]=(int)(ky+l*(1.0-0.39654));
		dSX[205]=(int)(kx+l*0.23719); dSY[205]=(int)(ky+l*(1.0-0.75877));
		dSX[206]=(int)(kx+l*0.72909); dSY[206]=(int)(ky+l*(1.0-0.83794));
		dSX[207]=(int)(kx+l*0.11163); dSY[207]=(int)(ky+l*(1.0-0.57717));
		dSX[208]=(int)(kx+l*0.82082); dSY[208]=(int)(ky+l*(1.0-0.38887));
		dSX[209]=(int)(kx+l*0.23973); dSY[209]=(int)(ky+l*(1.0-0.09762));
		dSX[210]=(int)(kx+l*0.18049); dSY[210]=(int)(ky+l*(1.0-0.7213));
		dSX[211]=(int)(kx+l*0.17251); dSY[211]=(int)(ky+l*(1.0-0.06261));
		dSX[212]=(int)(kx+l*0.73943); dSY[212]=(int)(ky+l*(1.0-0.1515));
		dSX[213]=(int)(kx+l*0.12257); dSY[213]=(int)(ky+l*(1.0-0.21737));
		dSX[214]=(int)(kx+l*0.72598); dSY[214]=(int)(ky+l*(1.0-0.87021));
		dSX[215]=(int)(kx+l*0.7244); dSY[215]=(int)(ky+l*(1.0-0.88142));
		dSX[216]=(int)(kx+l*0.21058); dSY[216]=(int)(ky+l*(1.0-0.83842));
		dSX[217]=(int)(kx+l*0.34401); dSY[217]=(int)(ky+l*(1.0-0.72108));
		dSX[218]=(int)(kx+l*0.65233); dSY[218]=(int)(ky+l*(1.0-0.15241));
		dSX[219]=(int)(kx+l*0.1184); dSY[219]=(int)(ky+l*(1.0-0.59815));
		dSX[220]=(int)(kx+l*0.20673); dSY[220]=(int)(ky+l*(1.0-0.09814));
		dSX[221]=(int)(kx+l*0.65673); dSY[221]=(int)(ky+l*(1.0-0.19377));
		dSX[222]=(int)(kx+l*0.46674); dSY[222]=(int)(ky+l*(1.0-0.7408));
		dSX[223]=(int)(kx+l*0.14444); dSY[223]=(int)(ky+l*(1.0-0.18892));
		dSX[224]=(int)(kx+l*0.44631); dSY[224]=(int)(ky+l*(1.0-0.72023));
		dSX[225]=(int)(kx+l*0.18501); dSY[225]=(int)(ky+l*(1.0-0.81523));
		dSX[226]=(int)(kx+l*0.67013); dSY[226]=(int)(ky+l*(1.0-0.17383));
		dSX[227]=(int)(kx+l*0.21007); dSY[227]=(int)(ky+l*(1.0-0.11003));
		dSX[228]=(int)(kx+l*0.28895); dSY[228]=(int)(ky+l*(1.0-0.79667));
		dSX[229]=(int)(kx+l*0.355); dSY[229]=(int)(ky+l*(1.0-0.77679));
		dSX[230]=(int)(kx+l*0.8031); dSY[230]=(int)(ky+l*(1.0-0.40707));
		dSX[231]=(int)(kx+l*0.20507); dSY[231]=(int)(ky+l*(1.0-0.23746));
		dSX[232]=(int)(kx+l*0.2091); dSY[232]=(int)(ky+l*(1.0-0.1445));
		dSX[233]=(int)(kx+l*0.69395); dSY[233]=(int)(ky+l*(1.0-0.87292));
		dSX[234]=(int)(kx+l*0.26225); dSY[234]=(int)(ky+l*(1.0-0.83517));
		dSX[235]=(int)(kx+l*0.46057); dSY[235]=(int)(ky+l*(1.0-0.66066));
		dSX[236]=(int)(kx+l*0.46715); dSY[236]=(int)(ky+l*(1.0-0.62083));
		dSX[237]=(int)(kx+l*0.15991); dSY[237]=(int)(ky+l*(1.0-0.16164));
		dSX[238]=(int)(kx+l*0.66818); dSY[238]=(int)(ky+l*(1.0-0.18336));
		dSX[239]=(int)(kx+l*0.1206); dSY[239]=(int)(ky+l*(1.0-0.20415));
		dSX[240]=(int)(kx+l*0.11134); dSY[240]=(int)(ky+l*(1.0-0.17899));
		dSX[241]=(int)(kx+l*0.81705); dSY[241]=(int)(ky+l*(1.0-0.3876));
		dSX[242]=(int)(kx+l*0.16158); dSY[242]=(int)(ky+l*(1.0-0.58197));
		dSX[243]=(int)(kx+l*0.71638); dSY[243]=(int)(ky+l*(1.0-0.92072));
		dSX[244]=(int)(kx+l*0.70824); dSY[244]=(int)(ky+l*(1.0-0.86881));
		dSX[245]=(int)(kx+l*0.21828); dSY[245]=(int)(ky+l*(1.0-0.07292));
		dSX[246]=(int)(kx+l*0.23573); dSY[246]=(int)(ky+l*(1.0-0.05748));
		dSX[247]=(int)(kx+l*0.11584); dSY[247]=(int)(ky+l*(1.0-0.70939));
		dSX[248]=(int)(kx+l*0.1235); dSY[248]=(int)(ky+l*(1.0-0.23567));
		dSX[249]=(int)(kx+l*0.18015); dSY[249]=(int)(ky+l*(1.0-0.73396));
		dSX[250]=(int)(kx+l*0.04505); dSY[250]=(int)(ky+l*(1.0-0.22103));
		dSX[251]=(int)(kx+l*0.75712); dSY[251]=(int)(ky+l*(1.0-0.89093));
		dSX[252]=(int)(kx+l*0.51855); dSY[252]=(int)(ky+l*(1.0-0.68719));
		dSX[253]=(int)(kx+l*0.20628); dSY[253]=(int)(ky+l*(1.0-0.78227));
		dSX[254]=(int)(kx+l*0.3027); dSY[254]=(int)(ky+l*(1.0-0.74326));
		dSX[255]=(int)(kx+l*0.72344); dSY[255]=(int)(ky+l*(1.0-0.86115));
		dSX[256]=(int)(kx+l*0.7823); dSY[256]=(int)(ky+l*(1.0-0.40698));
		dSX[257]=(int)(kx+l*0.80725); dSY[257]=(int)(ky+l*(1.0-0.36859));
		dSX[258]=(int)(kx+l*0.22767); dSY[258]=(int)(ky+l*(1.0-0.72875));
		dSX[259]=(int)(kx+l*0.8389); dSY[259]=(int)(ky+l*(1.0-0.36071));
		dSX[260]=(int)(kx+l*0.15643); dSY[260]=(int)(ky+l*(1.0-0.1861));
		dSX[261]=(int)(kx+l*0.70301); dSY[261]=(int)(ky+l*(1.0-0.13106));
		dSX[262]=(int)(kx+l*0.27509); dSY[262]=(int)(ky+l*(1.0-0.07504));
		dSX[263]=(int)(kx+l*0.26088); dSY[263]=(int)(ky+l*(1.0-0.7257));
		dSX[264]=(int)(kx+l*0.21206); dSY[264]=(int)(ky+l*(1.0-0.75771));
		dSX[265]=(int)(kx+l*0.13805); dSY[265]=(int)(ky+l*(1.0-0.58384));
		dSX[266]=(int)(kx+l*0.77447); dSY[266]=(int)(ky+l*(1.0-0.88097));
		dSX[267]=(int)(kx+l*0.24243); dSY[267]=(int)(ky+l*(1.0-0.76475));
		dSX[268]=(int)(kx+l*0.22454); dSY[268]=(int)(ky+l); // -0.01632
		dSX[269]=(int)(kx+l*0.46329); dSY[269]=(int)(ky+l*(1.0-0.66386));
		dSX[270]=(int)(kx+l*0.42856); dSY[270]=(int)(ky+l*(1.0-0.77059));
		dSX[271]=(int)(kx+l*0.09912); dSY[271]=(int)(ky+l*(1.0-0.23061));
		dSX[272]=(int)(kx+l*0.40031); dSY[272]=(int)(ky+l*(1.0-0.7123));
		dSX[273]=(int)(kx+l*0.41085); dSY[273]=(int)(ky+l*(1.0-0.68234));
		dSX[274]=(int)(kx+l*0.16843); dSY[274]=(int)(ky+l*(1.0-0.21484));
		dSX[275]=(int)(kx+l*0.19902); dSY[275]=(int)(ky+l*(1.0-0.74001));
		dSX[276]=(int)(kx+l*0.23913); dSY[276]=(int)(ky+l*(1.0-0.83891));
		dSX[277]=(int)(kx+l*0.52281); dSY[277]=(int)(ky+l*(1.0-0.68538));
		dSX[278]=(int)(kx+l*0.77193); dSY[278]=(int)(ky+l*(1.0-0.85292));
		dSX[279]=(int)(kx+l*0.62099); dSY[279]=(int)(ky+l*(1.0-0.1954));
		dSX[280]=(int)(kx+l*0.76508); dSY[280]=(int)(ky+l*(1.0-0.88685));
		dSX[281]=(int)(kx+l*0.17655); dSY[281]=(int)(ky+l*(1.0-0.19809));
		dSX[282]=(int)(kx+l*0.25752); dSY[282]=(int)(ky+l*(1.0-0.14649));
		dSX[283]=(int)(kx+l*0.41938); dSY[283]=(int)(ky+l*(1.0-0.66025));
		dSX[284]=(int)(kx+l*0.64649); dSY[284]=(int)(ky+l*(1.0-0.16984));
		dSX[285]=(int)(kx+l*0.83039); dSY[285]=(int)(ky+l*(1.0-0.36848));
		dSX[286]=(int)(kx+l*0.47482); dSY[286]=(int)(ky+l*(1.0-0.75725));
		dSX[287]=(int)(kx+l*0.36224); dSY[287]=(int)(ky+l*(1.0-0.72407));
		dSX[288]=(int)(kx+l*0.41691); dSY[288]=(int)(ky+l*(1.0-0.76211));
		dSX[289]=(int)(kx+l*0.71197); dSY[289]=(int)(ky+l*(1.0-0.88301));
		dSX[290]=(int)(kx+l*0.38875); dSY[290]=(int)(ky+l*(1.0-0.66923));
		dSX[291]=(int)(kx+l*0.65122); dSY[291]=(int)(ky+l*(1.0-0.2006));
		dSX[292]=(int)(kx+l*0.14963); dSY[292]=(int)(ky+l*(1.0-0.58176));
		dSX[293]=(int)(kx+l*0.12702); dSY[293]=(int)(ky+l*(1.0-0.20527));
		dSX[294]=(int)(kx+l*0.21471); dSY[294]=(int)(ky+l*(1.0-0.80336));
		dSX[295]=(int)(kx+l*0.1117); dSY[295]=(int)(ky+l*(1.0-0.58565));
		dSX[296]=(int)(kx+l*0.21964); dSY[296]=(int)(ky+l*(1.0-0.05787));
		dSX[297]=(int)(kx+l*0.52603); dSY[297]=(int)(ky+l*(1.0-0.68563));
		dSX[298]=(int)(kx+l*0.2513); dSY[298]=(int)(ky+l*(1.0-0.11038));
		dSX[299]=(int)(kx+l*0.23048); dSY[299]=(int)(ky+l*(1.0-0.81628));
		dSX[300]=(int)(kx+l*0.07918); dSY[300]=(int)(ky+l*(1.0-0.59745));
		dSX[301]=(int)(kx+l*0.39107); dSY[301]=(int)(ky+l*(1.0-0.70391));
		dSX[302]=(int)(kx+l*0.1921); dSY[302]=(int)(ky+l*(1.0-0.75531));
		dSX[303]=(int)(kx+l*0.69606); dSY[303]=(int)(ky+l*(1.0-0.21785));
		dSX[304]=(int)(kx+l*0.14619); dSY[304]=(int)(ky+l*(1.0-0.57905));
		dSX[305]=(int)(kx+l*0.79628); dSY[305]=(int)(ky+l*(1.0-0.3461));
		dSX[306]=(int)(kx+l*0.26276); dSY[306]=(int)(ky+l*(1.0-0.17608));
		dSX[307]=(int)(kx+l*0.32383); dSY[307]=(int)(ky+l*(1.0-0.74517));
		dSX[308]=(int)(kx+l*0.71259); dSY[308]=(int)(ky+l*(1.0-0.85462));
		dSX[309]=(int)(kx+l*0.11917); dSY[309]=(int)(ky+l*(1.0-0.25115));
		dSX[310]=(int)(kx+l*0.15771); dSY[310]=(int)(ky+l*(1.0-0.5723));
		dSX[311]=(int)(kx+l*0.74207); dSY[311]=(int)(ky+l*(1.0-0.86498));
		dSX[312]=(int)(kx+l*0.30246); dSY[312]=(int)(ky+l*(1.0-0.66994));
		dSX[313]=(int)(kx+l*0.20864); dSY[313]=(int)(ky+l*(1.0-0.16323));
		dSX[314]=(int)(kx+l*0.1412); dSY[314]=(int)(ky+l*(1.0-0.56028));
		dSX[315]=(int)(kx+l*0.82053); dSY[315]=(int)(ky+l*(1.0-0.35693));
		dSX[316]=(int)(kx+l*0.22989); dSY[316]=(int)(ky+l*(1.0-0.81021));
		dSX[317]=(int)(kx+l*0.10676); dSY[317]=(int)(ky+l*(1.0-0.20945));
		dSX[318]=(int)(kx+l*0.24867); dSY[318]=(int)(ky+l*(1.0-0.13241));
		dSX[319]=(int)(kx+l*0.18081); dSY[319]=(int)(ky+l*(1.0-0.77961));
		dSX[320]=(int)(kx+l*0.8051); dSY[320]=(int)(ky+l*(1.0-0.37642));
		dSX[321]=(int)(kx+l*0.71303); dSY[321]=(int)(ky+l*(1.0-0.87868));
		dSX[322]=(int)(kx+l*0.20502); dSY[322]=(int)(ky+l*(1.0-0.20587));
		dSX[323]=(int)(kx+l*0.47605); dSY[323]=(int)(ky+l*(1.0-0.68292));
		dSX[324]=(int)(kx+l*0.20975); dSY[324]=(int)(ky+l*(1.0-0.13444));
		dSX[325]=(int)(kx+l*0.7098); dSY[325]=(int)(ky+l*(1.0-0.85967));
		dSX[326]=(int)(kx+l*0.19912); dSY[326]=(int)(ky+l*(1.0-0.11887));
		dSX[327]=(int)(kx+l*0.21338); dSY[327]=(int)(ky+l*(1.0-0.15242));
		dSX[328]=(int)(kx+l*0.0816); dSY[328]=(int)(ky+l*(1.0-0.20505));
		dSX[329]=(int)(kx+l*0.81617); dSY[329]=(int)(ky+l*(1.0-0.37632));
		dSX[330]=(int)(kx+l*0.11072); dSY[330]=(int)(ky+l*(1.0-0.1742));
		dSX[331]=(int)(kx+l*0.44663); dSY[331]=(int)(ky+l*(1.0-0.7283));
		dSX[332]=(int)(kx+l*0.43758); dSY[332]=(int)(ky+l*(1.0-0.7116));
		dSX[333]=(int)(kx+l*0.11169); dSY[333]=(int)(ky+l*(1.0-0.58286));
		dSX[334]=(int)(kx+l*0.21739); dSY[334]=(int)(ky+l*(1.0-0.808));
		dSX[335]=(int)(kx+l*0.11504); dSY[335]=(int)(ky+l*(1.0-0.58542));
		dSX[336]=(int)(kx+l*0.22232); dSY[336]=(int)(ky+l*(1.0-0.10244));
		dSX[337]=(int)(kx+l*0.13277); dSY[337]=(int)(ky+l*(1.0-0.5679));
		dSX[338]=(int)(kx+l*0.41598); dSY[338]=(int)(ky+l*(1.0-0.73469));
		dSX[339]=(int)(kx+l*0.23372); dSY[339]=(int)(ky+l*(1.0-0.76431));
		dSX[340]=(int)(kx+l*0.32057); dSY[340]=(int)(ky+l*(1.0-0.75133));
		dSX[341]=(int)(kx+l*0.82525); dSY[341]=(int)(ky+l*(1.0-0.39564));
		dSX[342]=(int)(kx+l*0.15967); dSY[342]=(int)(ky+l*(1.0-0.17686));
		dSX[343]=(int)(kx+l*0.65594); dSY[343]=(int)(ky+l*(1.0-0.90155));
		dSX[344]=(int)(kx+l*0.71754); dSY[344]=(int)(ky+l*(1.0-0.87787));
		dSX[345]=(int)(kx+l*0.11191); dSY[345]=(int)(ky+l*(1.0-0.59932));
		dSX[346]=(int)(kx+l*0.2125); dSY[346]=(int)(ky+l*(1.0-0.05011));
		dSX[347]=(int)(kx+l*0.21381); dSY[347]=(int)(ky+l*(1.0-0.13874));
		dSX[348]=(int)(kx+l*0.32597); dSY[348]=(int)(ky+l*(1.0-0.702));
		dSX[349]=(int)(kx+l*0.84447); dSY[349]=(int)(ky+l*(1.0-0.377));
		dSX[350]=(int)(kx+l*0.23257); dSY[350]=(int)(ky+l*(1.0-0.0836));
		dSX[351]=(int)(kx+l*0.09849); dSY[351]=(int)(ky+l*(1.0-0.15117));
		dSX[352]=(int)(kx+l*0.25526); dSY[352]=(int)(ky+l*(1.0-0.156));
		dSX[353]=(int)(kx+l*0.46334); dSY[353]=(int)(ky+l*(1.0-0.69123));
		dSX[354]=(int)(kx+l*0.48943); dSY[354]=(int)(ky+l*(1.0-0.75123));
		dSX[355]=(int)(kx+l*0.7088); dSY[355]=(int)(ky+l*(1.0-0.8525));
		dSX[356]=(int)(kx+l*0.29138); dSY[356]=(int)(ky+l*(1.0-0.73165));
		dSX[357]=(int)(kx+l*0.15562); dSY[357]=(int)(ky+l*(1.0-0.80957));
		dSX[358]=(int)(kx+l*0.45633); dSY[358]=(int)(ky+l*(1.0-0.62115));
		dSX[359]=(int)(kx+l*0.22247); dSY[359]=(int)(ky+l*(1.0-0.73574));
		dSX[360]=(int)(kx+l*0.20278); dSY[360]=(int)(ky+l*(1.0-0.02718));
		dSX[361]=(int)(kx+l*0.1757); dSY[361]=(int)(ky+l*(1.0-0.77329));
		dSX[362]=(int)(kx+l*0.81154); dSY[362]=(int)(ky+l*(1.0-0.34851));
		dSX[363]=(int)(kx+l*0.63127); dSY[363]=(int)(ky+l*(1.0-0.19212));
		dSX[364]=(int)(kx+l*0.80712); dSY[364]=(int)(ky+l*(1.0-0.3727));
		dSX[365]=(int)(kx+l*0.79678); dSY[365]=(int)(ky+l*(1.0-0.37069));
		dSX[366]=(int)(kx+l*0.65493); dSY[366]=(int)(ky+l*(1.0-0.17201));
		dSX[367]=(int)(kx+l*0.11119); dSY[367]=(int)(ky+l*(1.0-0.55032));
		dSX[368]=(int)(kx+l*0.35914); dSY[368]=(int)(ky+l*(1.0-0.69928));
		dSX[369]=(int)(kx+l*0.84783); dSY[369]=(int)(ky+l*(1.0-0.38467));
		dSX[370]=(int)(kx+l*0.25637); dSY[370]=(int)(ky+l*(1.0-0.16449));
		dSX[371]=(int)(kx+l*0.4251); dSY[371]=(int)(ky+l*(1.0-0.75901));
		dSX[372]=(int)(kx+l*0.19824); dSY[372]=(int)(ky+l*(1.0-0.85476));
		dSX[373]=(int)(kx+l*0.49887); dSY[373]=(int)(ky+l*(1.0-0.69768));
		dSX[374]=(int)(kx+l*0.86102); dSY[374]=(int)(ky+l*(1.0-0.37142));
		dSX[375]=(int)(kx+l*0.19372); dSY[375]=(int)(ky+l*(1.0-0.80485));
		dSX[376]=(int)(kx+l*0.11601); dSY[376]=(int)(ky+l*(1.0-0.55327));
		dSX[377]=(int)(kx+l*0.72774); dSY[377]=(int)(ky+l*(1.0-0.87631));
		dSX[378]=(int)(kx+l*0.24923); dSY[378]=(int)(ky+l*(1.0-0.79912));
		dSX[379]=(int)(kx+l*0.4765); dSY[379]=(int)(ky+l*(1.0-0.68893));
		dSX[380]=(int)(kx+l*0.82476); dSY[380]=(int)(ky+l*(1.0-0.35662));
		dSX[381]=(int)(kx+l*0.73111); dSY[381]=(int)(ky+l*(1.0-0.17849));
		dSX[382]=(int)(kx+l*0.23645); dSY[382]=(int)(ky+l*(1.0-0.8192));
		dSX[383]=(int)(kx+l*0.24282); dSY[383]=(int)(ky+l*(1.0-0.79375));
		dSX[384]=(int)(kx+l*0.32193); dSY[384]=(int)(ky+l*(1.0-0.73014));
		dSX[385]=(int)(kx+l*0.18991); dSY[385]=(int)(ky+l*(1.0-0.76666));
		dSX[386]=(int)(kx+l*0.4943); dSY[386]=(int)(ky+l*(1.0-0.64545));
		dSX[387]=(int)(kx+l*0.45752); dSY[387]=(int)(ky+l*(1.0-0.68871));
		dSX[388]=(int)(kx+l*0.27258); dSY[388]=(int)(ky+l*(1.0-0.75787));
		dSX[389]=(int)(kx+l*0.48832); dSY[389]=(int)(ky+l*(1.0-0.66738));
		dSX[390]=(int)(kx+l*0.70802); dSY[390]=(int)(ky+l*(1.0-0.84396));
		dSX[391]=(int)(kx+l*0.36794); dSY[391]=(int)(ky+l*(1.0-0.63548));
		dSX[392]=(int)(kx+l*0.37738); dSY[392]=(int)(ky+l*(1.0-0.73531));
		dSX[393]=(int)(kx+l*0.23611); dSY[393]=(int)(ky+l*(1.0-0.10068));
		dSX[394]=(int)(kx+l*0.2211); dSY[394]=(int)(ky+l*(1.0-0.77825));
		dSX[395]=(int)(kx+l*0.21163); dSY[395]=(int)(ky+l*(1.0-0.12681));
		dSX[396]=(int)(kx+l*0.18089); dSY[396]=(int)(ky+l*(1.0-0.73562));
		dSX[397]=(int)(kx+l*0.66299); dSY[397]=(int)(ky+l*(1.0-0.22315));
		dSX[398]=(int)(kx+l*0.21315); dSY[398]=(int)(ky+l*(1.0-0.13477));
		dSX[399]=(int)(kx+l*0.71559); dSY[399]=(int)(ky+l*(1.0-0.89859));
		dSX[400]=(int)(kx+l*0.43222); dSY[400]=(int)(ky+l*(1.0-0.77103));
		dSX[401]=(int)(kx+l*0.87618); dSY[401]=(int)(ky+l*(1.0-0.37878));
		dSX[402]=(int)(kx+l*0.26296); dSY[402]=(int)(ky+l*(1.0-0.15748));
		dSX[403]=(int)(kx+l*0.1686); dSY[403]=(int)(ky+l*(1.0-0.20094));
		dSX[404]=(int)(kx+l*0.6815); dSY[404]=(int)(ky+l*(1.0-0.10764));
		dSX[405]=(int)(kx+l*0.37811); dSY[405]=(int)(ky+l*(1.0-0.70541));
		dSX[406]=(int)(kx+l*0.36094); dSY[406]=(int)(ky+l*(1.0-0.67579));
		dSX[407]=(int)(kx+l*0.82511); dSY[407]=(int)(ky+l*(1.0-0.41853));
		dSX[408]=(int)(kx+l*0.14138); dSY[408]=(int)(ky+l*(1.0-0.23299));
		dSX[409]=(int)(kx+l*0.67249); dSY[409]=(int)(ky+l*(1.0-0.20002));
		dSX[410]=(int)(kx+l*0.23894); dSY[410]=(int)(ky+l*(1.0-0.17142));
		dSX[411]=(int)(kx+l*0.75744); dSY[411]=(int)(ky+l*(1.0-0.14058));
		dSX[412]=(int)(kx+l*0.17161); dSY[412]=(int)(ky+l*(1.0-0.10035));
		dSX[413]=(int)(kx+l*0.48828); dSY[413]=(int)(ky+l*(1.0-0.66026));
		dSX[414]=(int)(kx+l*0.09221); dSY[414]=(int)(ky+l*(1.0-0.24637));
		dSX[415]=(int)(kx+l*0.16063); dSY[415]=(int)(ky+l*(1.0-0.59428));
		dSX[416]=(int)(kx+l*0.12893); dSY[416]=(int)(ky+l*(1.0-0.59674));
		dSX[417]=(int)(kx+l*0.35694); dSY[417]=(int)(ky+l*(1.0-0.78796));
		dSX[418]=(int)(kx+l*0.41546); dSY[418]=(int)(ky+l*(1.0-0.76092));
		dSX[419]=(int)(kx+l*0.16968); dSY[419]=(int)(ky+l*(1.0-0.83991));
		dSX[420]=(int)(kx+l*0.10334); dSY[420]=(int)(ky+l*(1.0-0.13985));
		dSX[421]=(int)(kx+l*0.16873); dSY[421]=(int)(ky+l*(1.0-0.03174));
		dSX[422]=(int)(kx+l*0.09976); dSY[422]=(int)(ky+l*(1.0-0.57833));
		dSX[423]=(int)(kx+l*0.73443); dSY[423]=(int)(ky+l*(1.0-0.86841));
		dSX[424]=(int)(kx+l*0.2138); dSY[424]=(int)(ky+l*(1.0-0.14457));
		dSX[425]=(int)(kx+l*0.18475); dSY[425]=(int)(ky+l*(1.0-0.73202));
		dSX[426]=(int)(kx+l*0.48298); dSY[426]=(int)(ky+l*(1.0-0.70441));
		dSX[427]=(int)(kx+l*0.18751); dSY[427]=(int)(ky+l*(1.0-0.17179));
		dSX[428]=(int)(kx+l*0.15242); dSY[428]=(int)(ky+l*(1.0-0.56863));
		dSX[429]=(int)(kx+l*0.47199); dSY[429]=(int)(ky+l*(1.0-0.60514));
		dSX[430]=(int)(kx+l*0.08912); dSY[430]=(int)(ky+l*(1.0-0.59353));
		dSX[431]=(int)(kx+l*0.14872); dSY[431]=(int)(ky+l*(1.0-0.63872));
		dSX[432]=(int)(kx+l*0.79864); dSY[432]=(int)(ky+l*(1.0-0.35493));
		dSX[433]=(int)(kx+l*0.35112); dSY[433]=(int)(ky+l*(1.0-0.78383));
		dSX[434]=(int)(kx+l*0.69891); dSY[434]=(int)(ky+l*(1.0-0.84894));
		dSX[435]=(int)(kx+l*0.80731); dSY[435]=(int)(ky+l*(1.0-0.39325));
		dSX[436]=(int)(kx+l*0.82968); dSY[436]=(int)(ky+l*(1.0-0.3552));
		dSX[437]=(int)(kx+l*0.72571); dSY[437]=(int)(ky+l*(1.0-0.19687));
		dSX[438]=(int)(kx+l*0.69843); dSY[438]=(int)(ky+l*(1.0-0.84846));
		dSX[439]=(int)(kx+l*0.84693); dSY[439]=(int)(ky+l*(1.0-0.40964));
		dSX[440]=(int)(kx+l*0.20669); dSY[440]=(int)(ky+l*(1.0-0.77071));
		dSX[441]=(int)(kx+l*0.12141); dSY[441]=(int)(ky+l*(1.0-0.58855));
		dSX[442]=(int)(kx+l*0.2279); dSY[442]=(int)(ky+l*(1.0-0.12276));
		dSX[443]=(int)(kx+l*0.83297); dSY[443]=(int)(ky+l*(1.0-0.39735));
		dSX[444]=(int)(kx+l*0.14542); dSY[444]=(int)(ky+l*(1.0-0.56013));
		dSX[445]=(int)(kx+l*0.12433); dSY[445]=(int)(ky+l*(1.0-0.20911));
		dSX[446]=(int)(kx+l*0.72573); dSY[446]=(int)(ky+l*(1.0-0.8408));
		dSX[447]=(int)(kx+l*0.09379); dSY[447]=(int)(ky+l*(1.0-0.55713));
		dSX[448]=(int)(kx+l*0.14829); dSY[448]=(int)(ky+l*(1.0-0.23154));
		dSX[449]=(int)(kx+l*0.4523); dSY[449]=(int)(ky+l*(1.0-0.67249));
		dSX[450]=(int)(kx+l*0.11726); dSY[450]=(int)(ky+l*(1.0-0.19693));
		dSX[451]=(int)(kx+l*0.11815); dSY[451]=(int)(ky+l*(1.0-0.25814));
		dSX[452]=(int)(kx+l*0.67506); dSY[452]=(int)(ky+l*(1.0-0.17122));
		dSX[453]=(int)(kx+l*0.83483); dSY[453]=(int)(ky+l*(1.0-0.40775));
		dSX[454]=(int)(kx+l*0.07239); dSY[454]=(int)(ky+l*(1.0-0.18731));
		dSX[455]=(int)(kx+l*0.3272); dSY[455]=(int)(ky+l*(1.0-0.7225));
		dSX[456]=(int)(kx+l*0.16136); dSY[456]=(int)(ky+l*(1.0-0.61121));
		dSX[457]=(int)(kx+l*0.21065); dSY[457]=(int)(ky+l*(1.0-0.13483));
		dSX[458]=(int)(kx+l*0.71889); dSY[458]=(int)(ky+l*(1.0-0.20099));
		dSX[459]=(int)(kx+l*0.36902); dSY[459]=(int)(ky+l*(1.0-0.7864));
		dSX[460]=(int)(kx+l*0.84165); dSY[460]=(int)(ky+l*(1.0-0.36644));
		dSX[461]=(int)(kx+l*0.68612); dSY[461]=(int)(ky+l*(1.0-0.12094));
		dSX[462]=(int)(kx+l*0.22926); dSY[462]=(int)(ky+l*(1.0-0.16182));
		dSX[463]=(int)(kx+l*0.18717); dSY[463]=(int)(ky+l*(1.0-0.11579));
		dSX[464]=(int)(kx+l*0.80286); dSY[464]=(int)(ky+l*(1.0-0.32103));
		dSX[465]=(int)(kx+l*0.25034); dSY[465]=(int)(ky+l*(1.0-0.04969));
		dSX[466]=(int)(kx+l*0.25102); dSY[466]=(int)(ky+l*(1.0-0.81178));
		dSX[467]=(int)(kx+l*0.40104); dSY[467]=(int)(ky+l*(1.0-0.70706));
		dSX[468]=(int)(kx+l*0.47589); dSY[468]=(int)(ky+l*(1.0-0.62965));
		dSX[469]=(int)(kx+l*0.4878); dSY[469]=(int)(ky+l*(1.0-0.77431));
		dSX[470]=(int)(kx+l*0.44168); dSY[470]=(int)(ky+l*(1.0-0.69073));
		dSX[471]=(int)(kx+l*0.10281); dSY[471]=(int)(ky+l*(1.0-0.2403));
		dSX[472]=(int)(kx+l*0.82296); dSY[472]=(int)(ky+l*(1.0-0.3797));
		dSX[473]=(int)(kx+l*0.48731); dSY[473]=(int)(ky+l*(1.0-0.69098));
		dSX[474]=(int)(kx+l*0.63263); dSY[474]=(int)(ky+l*(1.0-0.22947));
		dSX[475]=(int)(kx+l*0.67528); dSY[475]=(int)(ky+l*(1.0-0.20604));
		dSX[476]=(int)(kx+l*0.19472); dSY[476]=(int)(ky+l*(1.0-0.11076));
		dSX[477]=(int)(kx+l*0.84223); dSY[477]=(int)(ky+l*(1.0-0.34943));
		dSX[478]=(int)(kx+l*0.66502); dSY[478]=(int)(ky+l*(1.0-0.2078));
		dSX[479]=(int)(kx+l*0.5253); dSY[479]=(int)(ky+l*(1.0-0.70939));
		dSX[480]=(int)(kx+l*0.26947); dSY[480]=(int)(ky+l*(1.0-0.10746));
		dSX[481]=(int)(kx+l*0.22708); dSY[481]=(int)(ky+l*(1.0-0.7998));
		dSX[482]=(int)(kx+l*0.39279); dSY[482]=(int)(ky+l*(1.0-0.6481));
		dSX[483]=(int)(kx+l*0.36533); dSY[483]=(int)(ky+l*(1.0-0.74434));
		dSX[484]=(int)(kx+l*0.21924); dSY[484]=(int)(ky+l*(1.0-0.76278));
		dSX[485]=(int)(kx+l*0.17686); dSY[485]=(int)(ky+l*(1.0-0.18335));
		dSX[486]=(int)(kx+l*0.51587); dSY[486]=(int)(ky+l*(1.0-0.5951));
		dSX[487]=(int)(kx+l*0.8566); dSY[487]=(int)(ky+l*(1.0-0.40405));
		dSX[488]=(int)(kx+l*0.12652); dSY[488]=(int)(ky+l*(1.0-0.57607));
		dSX[489]=(int)(kx+l*0.22685); dSY[489]=(int)(ky+l*(1.0-0.79786));
		dSX[490]=(int)(kx+l*0.68578); dSY[490]=(int)(ky+l*(1.0-0.8548));
		dSX[491]=(int)(kx+l*0.38968); dSY[491]=(int)(ky+l*(1.0-0.73713));
		dSX[492]=(int)(kx+l*0.70811); dSY[492]=(int)(ky+l*(1.0-0.19062));
		dSX[493]=(int)(kx+l*0.46795); dSY[493]=(int)(ky+l*(1.0-0.68742));
		dSX[494]=(int)(kx+l*0.70386); dSY[494]=(int)(ky+l*(1.0-0.13081));
		dSX[495]=(int)(kx+l*0.12347); dSY[495]=(int)(ky+l*(1.0-0.57067));
		dSX[496]=(int)(kx+l*0.07499); dSY[496]=(int)(ky+l*(1.0-0.58753));
		dSX[497]=(int)(kx+l*0.26596); dSY[497]=(int)(ky+l*(1.0-0.75632));
		dSX[498]=(int)(kx+l*0.2196); dSY[498]=(int)(ky+l*(1.0-0.81844));
		dSX[499]=(int)(kx+l*0.22364); dSY[499]=(int)(ky+l*(1.0-0.11876));
    } // if (distrib != 7) {...} else
  }


  /**
   * Generate a signal for the given distribution.
   *  The result goes into the global variables <TT> SignalX </TT>
   *  and <TT> SignalY </TT>.
   * 
   * @param distrib          The specified distribution
   */
  protected void getSignal(int distrib) {
	int wi = INIT_WIDTH;
	int hi = INIT_HEIGHT;

    int r1 = (int) wi/4;
    int l1 = (int) hi/4;
    int r2 = (int) wi/2;
    int l2 = (int) hi/2;
    int cx = (int) (wi/2);
    int cy = (int) (hi/2);
    int xA[] = new int[MAX_COMPLEX];
    int yA[] = new int[MAX_COMPLEX];
    int ringRadius;
    int w;
    int h;
    int z;
	double remainderX = 0;
	double remainderY = 0;
    float rdist;

    switch (distrib) {
    case 0: // Rectangle
      r1 = (int) wi/4;
      l1 = (int) hi/4;
      r2 = (int) wi/2;
      l2 = (int) hi/2;
      SignalX = (int) (r1 + (r2 * Math.random()));
      SignalY = (int) (l1 + (l2 * Math.random()));
      break;
    case 1: // Circle

      l2 = (int) ((cx < cy) ? cx : cy); // Diameter

      r1 = (int) wi/2 -l2/2;
      l1 = (int) hi/2 -l2/2;

      do {
		SignalX = (int) (r1 + (l2 * Math.random()));
		SignalY = (int) (l1 + (l2 * Math.random()));
		rdist = (float) Math.sqrt(((cx - SignalX) *
								   (cx - SignalX) +
								   (cy - SignalY) *
								   (cy - SignalY)));
      } while ( rdist > l2/2 );

      break;
    case 2: // Ring

      l2 = (int) ((cx < cy) ? cx : cy); // Diameter

      r1 = (int) cx - l2;
      l1 = (int) cy - l2;
      ringRadius = (int) (l2 * RING_FACTOR);

      do {
		SignalX = (int) (r1 + (2*l2 * Math.random()));
		SignalY = (int) (l1 + (2*l2 * Math.random()));
		rdist = (float) Math.sqrt(((cx - SignalX) *
								   (cx - SignalX) +
								   (cy - SignalY) *
								   (cy - SignalY)));
      } while ( (rdist > l2) || (rdist < (l2 - ringRadius)) );
      break;
    case 3: // Complex (1)
      w = (int) wi/9;
      h = (int) hi/5;
      xA[0] = w;
      yA[0] = h;
      xA[1] = w;
      yA[1] = 2*h;
      xA[2] = w;
      yA[2] = 3*h;
      xA[3] = 2*w;
      yA[3] = 3*h;
      xA[4] = 3*w;
      yA[4] = 3*h;
      xA[5] = 3*w;
      yA[5] = 2*h;
      xA[6] = 3*w;
      yA[6] = h;
      xA[7] = 4*w;
      yA[7] = h;
      xA[8] = 5*w;
      yA[8] = h;
      xA[9] = 5*w;
      yA[9] = 2*h;
      xA[10] = 5*w;
      yA[10] = 3*h;
      xA[11] = 7*w;
      yA[11] = h;
      xA[12] = 7*w;
      yA[12] = 2*h;
      xA[13] = 7*w;
      yA[13] = 3*h;

      z = (int) (14 * Math.random());
      SignalX = (int) (xA[z] + (w * Math.random()));
      SignalY = (int) (yA[z] + (h * Math.random()));

      break;
    case 4: // Complex (2)
      w = (int) wi/9;
      h = (int) hi/7;
      xA[0] = w;
      yA[0] = 5*h;
      xA[1] = w;
      yA[1] = 4*h;
      xA[2] = w;
      yA[2] = 3*h;
      xA[3] = w;
      yA[3] = 2*h;
      xA[4] = 1*w;
      yA[4] = h;
      xA[5] = 2*w;
      yA[5] = h;
      xA[6] = 3*w;
      yA[6] = h;
      xA[7] = 4*w;
      yA[7] = h;
      xA[8] = 5*w;
      yA[8] = 1*h;
      xA[9] = 5*w;
      yA[9] = 2*h;
      xA[10] = 5*w;
      yA[10] = 3*h;
      xA[11] = 3*w;
      yA[11] = 3*h;
      xA[12] = 3*w;
      yA[12] = 4*h;
      xA[13] = 3*w;
      yA[13] = 5*h;
      xA[14] = 4*w;
      yA[14] = 5*h;
      xA[15] = 5*w;
      yA[15] = 5*h;
      xA[16] = 6*w;
      yA[16] = 5*h;
      xA[17] = 7*w;
      yA[17] = 5*h;
      xA[18] = 7*w;
      yA[18] = 4*h;
      xA[19] = 7*w;
      yA[19] = 3*h;
      xA[20] = 7*w;
      yA[20] = 2*h;
      xA[21] = 7*w;
      yA[21] = 1*h;

      z = (int) (22 * Math.random());
      SignalX = (int) (xA[z] + (w * Math.random()));
      SignalY = (int) (yA[z] + (h * Math.random()));

      break;
    case 5: // Complex (3)
      w = (int) wi/13;
      h = (int) hi/11;
      xA[0] = w;
      yA[0] = h;
      xA[1] = w;
      yA[1] = 2*h;
      xA[2] = w;
      yA[2] = 3*h;
      xA[3] = w;
      yA[3] = 4*h;
      xA[4] = 1*w;
      yA[4] = 5*h;
      xA[5] = 1*w;
      yA[5] = 6*h;
      xA[6] = 1*w;
      yA[6] = 7*h;
      xA[7] = 1*w;
      yA[7] = 8*h;
      xA[8] = 1*w;
      yA[8] = 9*h;
      xA[9] = 2*w;
      yA[9] = 1*h;
      xA[10] = 3*w;
      yA[10] = 1*h;
      xA[11] = 4*w;
      yA[11] = 1*h;
      xA[12] = 5*w;
      yA[12] = 1*h;
      xA[13] = 6*w;
      yA[13] = 1*h;
      xA[14] = 7*w;
      yA[14] = 1*h;
      xA[15] = 8*w;
      yA[15] = 1*h;
      xA[16] = 9*w;
      yA[16] = 1*h;
      xA[17] = 9*w;
      yA[17] = 2*h;
      xA[18] = 9*w;
      yA[18] = 3*h;
      xA[19] = 9*w;
      yA[19] = 4*h;
      xA[20] = 9*w;
      yA[20] = 5*h;
      xA[21] = 9*w;
      yA[21] = 6*h;
      xA[22] = 9*w;
      yA[22] = 7*h;
      xA[23] = 8*w;
      yA[23] = 7*h;
      xA[24] = 7*w;
      yA[24] = 7*h;
      xA[25] = 6*w;
      yA[25] = 7*h;
      xA[26] = 5*w;
      yA[26] = 7*h;
      xA[27] = 5*w;
      yA[27] = 6*h;
      xA[28] = 5*w;
      yA[28] = 5*h;
      xA[29] = 3*w;
      yA[29] = 3*h;
      xA[30] = 3*w;
      yA[30] = 4*h;
      xA[31] = 3*w;
      yA[31] = 5*h;
      xA[32] = 3*w;
      yA[32] = 6*h;
      xA[33] = 3*w;
      yA[33] = 7*h;
      xA[34] = 3*w;
      yA[34] = 8*h;
      xA[35] = 3*w;
      yA[35] = 9*h;
      xA[36] = 4*w;
      yA[36] = 3*h;
      xA[37] = 5*w;
      yA[37] = 3*h;
      xA[38] = 6*w;
      yA[38] = 3*h;
      xA[39] = 7*w;
      yA[39] = 3*h;
      xA[40] = 7*w;
      yA[40] = 4*h;
      xA[41] = 7*w;
      yA[41] = 5*h;
      xA[42] = 4*w;
      yA[42] = 9*h;
      xA[43] = 5*w;
      yA[43] = 9*h;
      xA[44] = 6*w;
      yA[44] = 9*h;
      xA[45] = 7*w;
      yA[45] = 9*h;
      xA[46] = 8*w;
      yA[46] = 9*h;
      xA[47] = 9*w;
      yA[47] = 9*h;
      xA[48] =10*w;
      yA[48] = 9*h;
      xA[49] =11*w;
      yA[49] = 9*h;
      xA[50] =11*w;
      yA[50] = 8*h;
      xA[51] =11*w;
      yA[51] = 7*h;
      xA[52] =11*w;
      yA[52] = 6*h;
      xA[53] =11*w;
      yA[53] = 5*h;
      xA[54] =11*w;
      yA[54] = 4*h;
      xA[55] =11*w;
      yA[55] = 3*h;
      xA[56] =11*w;
      yA[56] = 2*h;
      xA[57] =11*w;
      yA[57] = 1*h;

      z = (int) (58 * Math.random());
      SignalX = (int) (xA[z] + (w * Math.random()));
      SignalY = (int) (yA[z] + (h * Math.random()));

      break;

    case 6: // HiLo-Density
      w = (int) wi/10;
      h = (int) hi/10;
      xA[0] = 2 * w;
      yA[0] = 4 * h;
      xA[1] = 5 * w;
      yA[1] = 1 * h;

      z = (int) (2 * Math.random());
      if (z == 0) {
		SignalX = (int) (xA[z] + (w * Math.random()));
		SignalY = (int) (yA[z] + (h * Math.random()));
      } else {
		SignalX = (int) (xA[z] + (4 * w * Math.random()));
		SignalY = (int) (yA[z] + (8 * h * Math.random()));
      }

      break;

    case 7: // Discrete
      z = (int) (MAX_DISCRETE_SIGNALS * Math.random());
	  SignalX = discreteSignalsX[z];
	  SignalY = discreteSignalsY[z];

      break;

    case 8: // Complex (4)
      w = (int) wi/17;
      h = (int) hi/8;
      xA[0] = w;
      yA[0] = 2*h;
      xA[1] = w;
      yA[1] = 3*h;
      xA[2] = w;
      yA[2] = 4*h;
      xA[3] = w;
      yA[3] = 5*h;
      xA[4] = 2*w;
      yA[4] = 5*h;
      xA[5] = 3*w;
      yA[5] = 5*h;
      xA[6] = 3*w;
      yA[6] = 4*h;
      xA[7] = 3*w;
      yA[7] = 3*h;
      xA[8] = 3*w;
      yA[8] = 2*h;
      xA[9] = 4*w;
      yA[9] = 2*h;
      xA[10] = 5*w;
      yA[10] = 2*h;
      xA[11] = 6*w;
      yA[11] = 2*h;
      xA[12] = 7*w;
      yA[12] = 2*h;
      xA[13] = 7*w;
      yA[13] = 3*h;
      xA[14] = 7*w;
      yA[14] = 4*h;
      xA[15] = 7*w;
      yA[15] = 5*h;
      xA[16] = 8*w;
      yA[16] = 5*h;
      xA[17] = 9*w;
      yA[17] = 5*h;
      xA[18] = 10*w;
      yA[18] = 5*h;
      xA[19] = 11*w;
      yA[19] = 5*h;
      xA[20] = 11*w;
      yA[20] = 4*h;
      xA[21] = 11*w;
      yA[21] = 3*h;
      xA[22] = 11*w;
      yA[22] = 2*h;
      xA[23] = 14*w;
      yA[23] = 2*h;
      xA[24] = 15*w;
      yA[24] = 2*h;
      xA[25] = 15*w;
      yA[25] = 3*h;
      xA[26] = 15*w;
      yA[26] = 4*h;
      xA[27] = 15*w;
      yA[27] = 5*h;

      z = (int) (28 * Math.random());
      SignalX = (int) (xA[z] + (w * Math.random()));
      SignalY = (int) (yA[z] + (h * Math.random()));

      break;
    case 9: // Moving and Jumping Rectangle
      r2 = (int) wi/4;
      l2 = (int) hi/4;
      r1 = (int) (0.75 * (wi/2 +
				  Math.IEEEremainder((double)0.2 * numRun,(double)(wi))));
      l1 = (int) (0.75 * (hi/2 +
				  Math.IEEEremainder((double)0.2 * numRun,(double)(hi))));
	  if (DEBUG) 
		System.out.println("signal x = " + r1);

      SignalX = (int) (r1 + (r2 * Math.random()));
      SignalY = (int) (l1 + (l2 * Math.random()));
      break;
    case 10: // Moving Rectangle
      r2 = (int) wi/4;
      l2 = (int) hi/4;

	  remainderX = Math.IEEEremainder((double)0.2 * numRun,(double)(wi));
	  remainderY = Math.IEEEremainder((double)0.2 * numRun,(double)(hi));

	  if ( (bounceX_old > 0) && (remainderX < 0) )
		bounceX = bounceX * (-1);
	  if ( (bounceY_old > 0) && (remainderY < 0) )
		bounceY = bounceY * (-1);

      r1 = (int) (0.75 * (wi/2 + bounceX * remainderX));
      l1 = (int) (0.75 * (hi/2 + bounceY * remainderY));

	  bounceX_old = remainderX;
	  bounceY_old = remainderY;

	  if (DEBUG) 
		System.out.println("signal x = " + r1);

      SignalX = (int) (r1 + (r2 * Math.random()));
      SignalY = (int) (l1 + (l2 * Math.random()));
      break;

    case 11: // Jumping Rectangle
      r2 = (int) wi/4;
      l2 = (int) hi/4;

	  if (Math.ceil(Math.IEEEremainder((double)numRun, 1000.0)) == 0) {
		jumpX = (int) ((wi - r2) * Math.random());
		jumpY = (int) ((hi - l2) * Math.random());
	  }

      SignalX = (int) (jumpX + (r2 * Math.random()));
      SignalY = (int) (jumpY + (l2 * Math.random()));
      break;

    case 12: // R.Mouse Rectangle
      r2 = (int) wi/4;
      l2 = (int) hi/4;

      SignalX = (int) (jumpX + (r2 * Math.random()));
      SignalY = (int) (jumpY + (l2 * Math.random()));
      break;
    }
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
      if ( (d.width != INIT_WIDTH) || (d.height != INIT_HEIGHT) ) {
		NodeGNG n;
		for (i = 0 ; i < nnodes ; i++) {
		  n = nodes[i];
	  
		  n.x = n.x * d.width / INIT_WIDTH;
		  n.y = n.y * d.height / INIT_HEIGHT;
		}

		INIT_WIDTH = d.width;
		INIT_HEIGHT = d.height;
		initDiscreteSignals(distribution);
		if ( ( nnodes == 0) && (algo == 5) ) {
		  // Gernerate some nodes
		  int z = (int) (numDiscreteSignals * Math.random());
		  int mod = 0;
		  for (i = 0; i < maxNodes; i++) {
			mod = (z+i)%numDiscreteSignals;
			addNode(discreteSignalsX[mod],
							discreteSignalsY[mod]);
		  }
		}
	  }

      // Calculate the new positions
      if (!stopB) {
        learn();
		nodesMovedB = true;
      }

      if ((delaunayB || voronoiB) && nodesMovedB) {
		nlines = 0;
		nodesMovedB = computeVoronoi();
      }

	  if (errorGraphB && !stopB)
		errorGraph.graph.add(valueGraph);

      repaint();

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
   *  and forwarded to the switched algorithm.
   *  Available Algorithms (abbrev, case):
   *   Growing Neural Gas (GNG, 0),
   *   Hard Competitive Learning (HCL, 1),
   *   Neural Gas (NG, 2),
   *   Neural Gas with Competitive Hebbian Learning (NGwCHL, 3) and
   *   Competitive Hebbian Learning (CHL, 4).
   *   LBG (LBG, 5: but not in the switch).
   *   Growing Grid (GG, 6).
   *   Self-Organizing Map (SOM, 7).
   * 
   */
  protected synchronized void learn() {
    Dimension d = size();
    int nr1, nr2;
	int i, j, k, l, m;
	int x, y;
    int numError, lowUtilityNode;
    int numNb;
    int toDelete;
	float dx, dy;
	float dstSgmExp;
    float bestDist, nextBestDist;
    float h_l = 0.0f;
    float l_t = 0.0f;
	float worstError, lowUtility;
    NodeGNG pick, pick2, n, node;

    SignalX = (int) d.width/2;
    SignalY = (int) d.height/2;

	valueGraph = 0.0f;

    for (k = 0; k < stepSize; k++) {

      numRun++;

      if (algo != 5) {
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
		for (i = 0 ; i < nnodes ; i++) {
		  n = nodes[i];
		  n.winner = n.second = n.moved = false;

		  if ( ((numRun % NUM_NEW_NODE) == 0) && (!noNodesB) && (algo == 0) )
			n.inserted = false;
	 
		  // Mark Node without neighbors (one each run is enough)
		  if (n.numNeighbors() == 0)
			toDelete = i;

		  // Calculate distance
		  n.dist = (n.x - SignalX) * (n.x - SignalX) +
			       (n.y - SignalY) * (n.y - SignalY);
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

		switch (algo) {
		  //
		  // Growing Neural Gas
		  //
		case 0:
		  // Find second node (continued)
		  if (nr1 == nr2) {
			nr2++;
			nextBestDist = Float.MAX_VALUE;
			pick2 = nodes[nr2];
		  }
		  for (i = nr1 + 1 ; i < nnodes ; i++) {
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
		  if ( (numRun % NUM_NEW_NODE) == 0 ) 
			if (!noNodesB)
			  insertedSoundB =
				( -1 != insertNode(numError, worstErrorNeighbor(numError)) );

		  // Delete Node without Neighbors (not GNG-U)
		  if ((toDelete != -1) && (nnodes > 2) && !utilityGNGB )
			deleteNode(toDelete);
		  // Delete Node with very low utility
		  else {
			if ( worstError > lowUtility * utilityGNG) {
			  if (utilityGNGB && (nnodes > 2)) {
				deleteNode(lowUtilityNode);
			  }
			  if (DEBUG) 
				System.out.println("Utlity-delete ("
									+ lowUtilityNode + "): "
									+ worstError + " > "
									+ lowUtility + " * " + utilityGNG);
			  //utilityNode = lowUtility;
			  
			} else if ( utilityGNGB && (nnodes > 2) && (nnodes > maxNodes) ) {
			  deleteNode(lowUtilityNode);
			}
		  }

		  break;

		  //
		  // Hard Competitive Learning
		  //
		case 1:
		  if ((numRun >= t_max) && variableB)
			break;

		  // Adapt picked node
		  if (variableB) {
			e_t = (float)(e_i * Math.pow(e_f/e_i, numRun/t_max));
			pick.x += e_t * (SignalX - pick.x);
			pick.y += e_t * (SignalY - pick.y);
		  } else {
			pick.x += epsilon * (SignalX - pick.x);
			pick.y += epsilon * (SignalY - pick.y);
		  }
		  break;

		  //
		  // Neural Gas
		  //
		case 2:
		  if (numRun >= t_max)
			break;

		  // Initialize the sorted node array, if necessary
		  if (nNodesChangedB) {
			for (i = 1; i <= nnodes; i++)
			  snodes[i] = i-1;
			nNodesChangedB = false;
		  }

		  l_t = (float)(l_i * Math.pow(l_f/l_i, numRun/t_max));
		  e_t = (float)(e_i * Math.pow(e_f/e_i, numRun/t_max));

		  // Build a minimum heap
		  for (i = nnodes/2; i > 0; i--)
			reheap_min(i, nnodes);

		  {
			int decrNnodes = nnodes - 1;
			int minimum;

			// Fetch minimum, calculate new position and reheap
			for (i = nnodes; i > 0; i--) {

			  minimum = snodes[1];
			  snodes[1] = snodes[i];
			  snodes[i] = minimum;
			  n = nodes[minimum];

			  // Mark second for teach-mode
			  if (i == decrNnodes)
				nodes[minimum].second = true;

			  h_l = (float)(Math.exp(-(nnodes - i)/l_t));

			  // Adapt nodes
			  dx = e_t * h_l * (SignalX - n.x);
			  dy = e_t * h_l * (SignalY - n.y);
			  n.x += dx;
			  n.y += dy;

			  if ( (Math.abs(dx) < 1.0) &&
				   (Math.abs(dy) < 1.0) &&
				   (i < decrNnodes) )
				break;

			  reheap_min(1, i-1);
			}
		  }
		  break;

		  //
		  // Neural Gas with Competitive Hebbian Learning
		  //
		case 3:
		  if (numRun >= t_max)
			break;

		  // Initialize the sorted node array, if necessary
		  if (nNodesChangedB) {
			for (i = 1; i <= nnodes; i++)
			  snodes[i] = i-1;
			nNodesChangedB = false;
		  }

		  l_t = (float)(l_i * Math.pow(l_f/l_i, numRun/t_max));
		  e_t = (float)(e_i * Math.pow(e_f/e_i, numRun/t_max));

		  // Calculate the new edge-deleting term
		  MAX_EDGE_AGE = (int) (delEdge_i *
								Math.pow(delEdge_f/delEdge_i, numRun/t_max));

		  // Build a minimum heap
		  for (i = nnodes/2; i > 0; i--)
			reheap_min(i, nnodes);

		  {
			int decrNnodes = nnodes - 1;
			int minimum;

			// Fetch minimum, calculate new position and reheap
			for (i = nnodes; i > 0; i--) {

			  minimum = snodes[1];
			  snodes[1] = snodes[i];
			  snodes[i] = minimum;
			  n = nodes[minimum];

			  // Mark second for teach-mode
			  if (i == decrNnodes) {
				nodes[minimum].second = true;
				// This is the only difference between NG and NGwCHL:
				// 	- Connect the first and the second node
				addEdge(snodes[nnodes],snodes[nnodes - 1]);
				// 	- Calculate the age of the connected edges and
				//	  delete too old edges
				ageEdge(snodes[nnodes]);      
			  }

			  h_l = (float)(Math.exp(-(nnodes - i)/l_t));

			  // Adapt nodes
			  dx = e_t * h_l * (SignalX - n.x);
			  dy = e_t * h_l * (SignalY - n.y);
			  n.x += dx;
			  n.y += dy;

			  if ( (Math.abs(dx) < 1.0) &&
				   (Math.abs(dy) < 1.0) &&
				   (i < decrNnodes) )
				break;

			  reheap_min(1, i-1);
			}
		  }
		  break;

		  //
		  // Competitive Hebbian Learning
		  //
		case 4:
		  // Find second node (continued)
		  if (nr1 == nr2) {
			nr2++;
			nextBestDist = Float.MAX_VALUE;
			pick2 = nodes[nr2];
		  }
		  for (i = nr1 + 1 ; i < nnodes ; i++) {
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

		  // Connect the nodes
		  addEdge(nr1, nr2);

		  break;

		  //
		  // Growing Grid
		  //
		case 6:
		  // Adapt nodes
		  x = pick.x_grid;
		  y = pick.y_grid;
		  grid[x][y].tau++;

		  if (fineTuningB) {
			int percent;
			float tmax = grid_x * grid_y * l_f;
			e_t = (float)(e_i * Math.pow(e_f/e_i, (numRun-numRunTmp)/tmax));
			percent = (int) (((numRun-numRunTmp)*100)/tmax);
			if (percent > 100) {
			  fineTuningS = "Fine-tuning (100%)";
			  break;
			}
			fineTuningS = "Fine-tuning (" + String.valueOf(percent) + "%)";
		  } else
			e_t = e_i;

		  int dst;
		  for (i = 0; i < grid_x; i++) {
			for (j = 0; j < grid_y; j++) {
			  // Distance
			  dst = Math.abs(x - i) + Math.abs(y - j);
			  node = grid[i][j].node;

			  dstSgmExp = (float) (Math.exp(-(dst*dst)/(2.0 * sigma * sigma)));
			  node.x += e_t * dstSgmExp * (SignalX - node.x);
			  node.y += e_t * dstSgmExp * (SignalY - node.y);

			  if ( dstSgmExp > 0.5f )
				node.second = true;
			}
		  }

		  // Prove inserting nodes and insert if neccessary
		  if ( (numRun % (grid_x * grid_y * l_i) == 0) && (!fineTuningB) ) {
			if (!noNodesB) {
			  insertedSoundB = ( -1 != insertGrid() );
			  fineTuningB = !insertedSoundB;
			  numRunTmp = numRun;
			}
		  }

		  break;

		  //
		  // Self-Organizing Map
		  //
		case 7:
		  if (numRun >= t_max)
			break;

		  // Adapt nodes
		  x = pick.x_grid;
		  y = pick.y_grid;

		  e_t = (float)(e_i * Math.pow(e_f/e_i, numRun/t_max));
		  sigma = (float)(sigma_i * Math.pow(sigma_f, numRun/t_max));

		  for (i = 0; i < grid_x; i++) {
			for (j = 0; j < grid_y; j++) {
			  // Distance
			  dst = Math.abs(x - i) + Math.abs(y - j);
			  node = grid[i][j].node;

			  dstSgmExp = (float) (Math.exp(-(dst*dst)/(2.0 * sigma * sigma)));
			  node.x += e_t * dstSgmExp * (SignalX - node.x);
			  node.y += e_t * dstSgmExp * (SignalY - node.y);

			  if ( dstSgmExp > 0.5f )
				node.second = true;
			}
		  }

		  break;

		} // switch
      } // if (algo != 5)
      else {

		//
		// LBG
		//
		readyLBG_B = true;
		int sumSignal, sig;
		int wa = 0, wb = 0;
		int pickNo = 0, pick2No = 0;
		NodeGNG pickLBG, pick2LBG;
		float bestDistLBG, nextBestDistLBG;
		float utility = 0.0f;
		float minUtility = Float.MAX_VALUE;
		float error = 0.0f;
		float maxError = 0.0f;
		float errorAct = 0.0f;
		FPoint tmpFP;

		for (j = 0; j < numDiscreteSignals; j++) {
		  pickLBG = nodes[0];
		  pick2LBG = nodes[0];
		  bestDistLBG = Float.MAX_VALUE;
		  nextBestDistLBG = Float.MAX_VALUE;

		  // Locate the nearest node and prepare for the second
		  for (i = 0; i < nnodes; i++) {
			n = nodes[i];
	 
			// Calculate distance
			n.dist = (n.x - discreteSignalsX[j]) *
			  (n.x - discreteSignalsX[j]) +
			  (n.y - discreteSignalsY[j]) *
			  (n.y - discreteSignalsY[j]);

			// Calculate node with best distance and prepare for second
			if (n.dist < bestDistLBG) {
			  pick2No = pickNo;
			  pick2LBG = pickLBG;
			  pickNo = i;
			  pickLBG = n;
			  nextBestDistLBG = bestDistLBG;
			  bestDistLBG = n.dist;
			}
		  }

		  // Store distance to the nearest codebook vector (LBG-U)
		  discreteSignalsD1[j] = bestDistLBG;
		  errorAct += bestDistLBG;

		  // Add signal index to the winning codebook vector
		  pickLBG.addSignal(j);

		  // Find node with second best distance
		  if (pickLBG == pick2LBG)
			nextBestDistLBG = Float.MAX_VALUE;

		  for (i = pickNo + 1; i < nnodes; i++) {
			n = nodes[i];

			if (n.dist < nextBestDistLBG) {
			  pick2No = i;
			  pick2LBG = n;
			  nextBestDistLBG = n.dist;
			}
		  }

		  // Store distance to the second nearest codebook vector (LBG-U)
		  discreteSignalsD2[j] = nextBestDistLBG;


		  valueGraph += bestDistLBG;
		}

		minUtility = Float.MAX_VALUE;
		maxError = 0.0f;
		// Adapt selected nodes
		for (l = 0; l < nnodes; l++) {
		  n = nodes[l];
		  tmpFP = new FPoint(n.x, n.y);
		  utility = 0.0f;
		  error = 0.0f;

		  sumSignal = n.numSignals();
	 
		  if (sumSignal > 0) {
			for (m = 0; m < sumSignal; m++) {
			  sig = n.removeSignal();
			  n.x += discreteSignalsX[sig];
			  n.y += discreteSignalsY[sig];

			  // calculate utility
			  utility += (discreteSignalsD2[sig] -
						  discreteSignalsD1[sig]);
			  // calculate error
			  error += discreteSignalsD1[sig];

			}
			n.x /= (sumSignal + 1.0f);
			n.y /= (sumSignal + 1.0f);

			// nodes moved?
			if (!tmpFP.equal(n.x, n.y)) {
			  n.moved = true;
			  readyLBG_B = false;
			} else {
			  n.moved = false;
			}

			// determine minimum utility
			if (utility < minUtility) {
			  wa = l;
			  minUtility = utility;
			}
			// determine maximum error
			if (error > maxError) {
			  wb = l;
			  maxError = error;
			}
		  }
		}

		if (LBG_U_B) {
		  if (DEBUG)
			System.out.println("Act: " + errorAct
							   + ", Best: " + errorBestLBG_U);
		  if (readyLBG_B && (errorAct < errorBestLBG_U) ) {
			// Save old positions
			for (i = 0; i < nnodes; i++) {
			  Cbest[i] = new FPoint(nodes[i].x,
									nodes[i].y);
			}
			readyLBG_B = false;
			errorBestLBG_U = errorAct;
			// move node from wa to wb with a small offset (LBG-U)
			// IMPORTANT: This works only for image data space!!!
			nodes[wa].x = nodes[wb].x;
			nodes[wa].y = nodes[wb].y + 1;
		  } else if (readyLBG_B && (errorAct > errorBestLBG_U) ) {
			// Restore old positions
			for (i = 0; i < nnodes; i++) {
			  nodes[i].x = Cbest[i].x;
			  nodes[i].y = Cbest[i].y;
			}
		  }
		}

      } // if (algo != 5)
    } // for

	if (errorGraphB) {
	  // Calculate mean square error
	  if (algo == 5)
		valueGraph = (float) valueGraph / numDiscreteSignals;

	  valueGraph = (float) Math.sqrt( valueGraph / ((float) stepSize) );
	}
  }

  // Vars for Voronoi diagram (start).
  int xmin, ymin, xmax, ymax;
  int deltax, deltay;
  int siteidx, nsites, sqrt_nsites;
  int nvertices, nvedges;
  int PQcount, PQmin;
  SiteVoronoi bottomsite;
  final int LE = 0;
  final int RE = 1;
  final int DELETED = -2;
  ListGNG list, pq;
  boolean debug = true;
  HalfEdgeVoronoi ELleftend, ELrightend;
  float pxmin, pymin, pxmax, pymax;
  // Vars for Voronoi diagram (end).

  /**
   * Compute Voronoi diagram.
   * A sweepline algorithm is implemented (Steven Fortune, 1987).
   * It computes the Voronoi diagram/Delaunay triangulation of n sites
   * in time O(n log n) and space usage O(n).
   * Input: nodes[], Output: lines[] (global).
   * 
   */
  public boolean computeVoronoi() {
    Dimension d = size();
	int i;
    xmin = 0;
    ymin = 0;
    xmax = deltax = d.width;
    ymax = deltay = d.height;
    siteidx = 0;
    nsites = nnodes;
    nvertices = 0;
    nvedges = 0;
    sqrt_nsites = (int) Math.sqrt(nsites + 4);
    PQcount = 0;
    PQmin = 0;

    // Copy nodes[] to vsites[] and sort them
    sortSites(nnodes);

	if ( (nnodes == 0) ||
		 ( (nnodes != maxNodes) && (algo != 0) && (algo != 6) ) )
	  return true;

    xmin = (int) vsites[1].coord.x; 
    xmax = (int) vsites[1].coord.x;
    for(i = 2; i <= nsites; i++) {
      if (vsites[i].coord.x < xmin)
		xmin = (int) vsites[i].coord.x;
      if (vsites[i].coord.x > xmax)
		xmax = (int) vsites[i].coord.x;
    }
    ymin = (int) vsites[1].coord.y;
    ymax = (int) vsites[nsites].coord.y;
    
    voronoi();
	return false;
  }

  /**
   * Compute Voronoi diagram (2).
   * A sweepline algorithm is implemented (Steven Fortune, 1987).
   * Input: nodes[], Output: lines[] (global).
   * 
   * @see ComputeGNG#computeVoronoi
   */
  public void voronoi() {
    SiteVoronoi newsite, bot, top, temp, p, v;
    FPoint newintstar = new FPoint();
    int pm;
    HalfEdgeVoronoi lbnd, rbnd, llbnd, rrbnd, bisector;
    EdgeVoronoi e;

	pq = new ListGNG();
    bottomsite = nextsite();
    ELinitialize();
    newsite = nextsite();

    while(true) {
      if (!pq.empty())
		newintstar = pq.PQ_min();

      if ( (newsite != null) &&
		   ( pq.empty() ||
			 (newsite.coord.y < newintstar.y) ||
			 ( (newsite.coord.y == newintstar.y) &&
			   (newsite.coord.x < newintstar.x) )
			 ) ) {
		lbnd = ELleftbnd(newsite.coord);
		rbnd = lbnd.ELright;
		bot = rightreg(lbnd);
		e = bisect(bot, newsite);
		bisector = new HalfEdgeVoronoi(e, LE);
		ELinsert(lbnd, bisector);
		if ( (p = intersect(lbnd, bisector)) != null ) {
		  PQdelete(lbnd);
		  PQinsert(lbnd, p, dist(p,newsite));
		}
		lbnd = bisector;
		bisector = new HalfEdgeVoronoi(e, RE);
		ELinsert(lbnd, bisector);
		if ( (p = intersect(bisector, rbnd)) != null ) {
		  PQinsert(bisector, p, dist(p,newsite));	
		}

		newsite = nextsite();

      } else if ( !pq.empty() ) {
		// intersection is smallest
		PQcount--;
		lbnd = pq.PQextractmin();
		llbnd = lbnd.ELleft;
		rbnd = lbnd.ELright;
		rrbnd = rbnd.ELright;
		bot = leftreg(lbnd);
		top = rightreg(rbnd);
		v = lbnd.vertex;
		makevertex(v);
		endpoint(lbnd.ELedge, lbnd.ELpm, v);
		endpoint(rbnd.ELedge, rbnd.ELpm, v);
		ELdelete(lbnd); 
		PQdelete(rbnd);
		ELdelete(rbnd); 
		pm = LE;
		if (bot.coord.y > top.coord.y) {
		  temp = bot;
		  bot = top;
		  top = temp;
		  pm = RE;
		}
		e = bisect(bot, top);
		bisector = new HalfEdgeVoronoi(e, pm);
		ELinsert(llbnd, bisector);
		endpoint(e, RE-pm, v);
		deref(v);
		if ( (p = intersect(llbnd, bisector)) != null ) {
		  PQdelete(llbnd);
		  PQinsert(llbnd, p, dist(p, bot) );
		}
		if ( (p = intersect(bisector, rrbnd)) != null )
		  PQinsert(bisector, p, dist(p, bot) );
      } else
		break;
    }

    if (voronoiB) {
      for(lbnd = ELleftend.ELright;
		  lbnd != ELrightend;
		  lbnd = lbnd.ELright) {
		e = lbnd.ELedge;
		out_ep(e);
      }
    }
  }

  public void out_bisector(EdgeVoronoi e) {
    line(e.reg[0].coord.x, e.reg[0].coord.y, 
		 e.reg[1].coord.x, e.reg[1].coord.y, false);

  }

  public void out_ep(EdgeVoronoi e) {
    SiteVoronoi s1, s2;
    float x1, x2, y1, y2;
    float dx,dy,d;
    Dimension dim = size();

    pxmin = 0.0f;
    pymin = 0.0f;
    pxmax = dim.width;
    pymax = dim.height;

    if ( (e.a == 1.0f) && (e.b >= 0.0f) ) {
      s1 = e.ep[1];
      s2 = e.ep[0];
    } else {
      s1 = e.ep[0];
      s2 = e.ep[1];
    }

    if (e.a == 1.0) {
      y1 = pymin;
      if ( (s1 != null) && (s1.coord.y > pymin) )
		y1 = s1.coord.y;
      if (y1 > pymax)
		return;
      x1 = e.c - e.b * y1;
      y2 = pymax;
      if ( (s2 != null) && (s2.coord.y < pymax) ) 
		y2 = s2.coord.y;
      if (y2 < pymin)
		return;
      x2 = e.c - e.b * y2;
      if ( (x1 > pxmax & x2 > pxmax) | (x1 < pxmin & x2 < pxmin) )
		return;
      if (x1 > pxmax) {
		x1 = pxmax;
		y1 = (e.c - x1)/e.b;
      }
      if (x1 < pxmin) {
		x1 = pxmin;
		y1 = (e.c - x1)/e.b;
      }
      if (x2 > pxmax) {
		x2 = pxmax;
		y2 = (e.c - x2)/e.b;
      }
      if (x2 < pxmin) {
		x2 = pxmin;
		y2 = (e.c - x2)/e.b;
      }
    } else {
      x1 = pxmin;
      if ( (s1 != null) && (s1.coord.x > pxmin) ) 
		x1 = s1.coord.x;
      if (x1 > pxmax)
		return;
      y1 = e.c - e.a * x1;
      x2 = pxmax;
      if ( (s2 != null) && (s2.coord.x < pxmax) )
		x2 = s2.coord.x;
      if (x2 < pxmin)
		return;
      y2 = e.c - e.a * x2;
      if ( (y1 > pymax & y2 > pymax) | ( y1 < pymin & y2 < pymin) )
		return;
      if (y1 > pymax) {
		y1 = pymax;
		x1 = (e.c - y1)/e.a;
      }
      if(y1 < pymin) {
		y1 = pymin;
		x1 = (e.c - y1)/e.a;
      }
      if(y2 > pymax) {
		y2 = pymax;
		x2 = (e.c - y2)/e.a;
      }
      if(y2 < pymin) {
		y2 = pymin;
		x2 = (e.c - y2)/e.a;
      }
    }
    line(x1, y1, x2, y2, true);
  }

  public void line(float x1, float y1, float x2, float y2, boolean vdB) {
    LineGNG l = new LineGNG((int) x1, (int) y1, (int) x2, (int) y2);
    lines[nlines] = l;
    vd[nlines] = vdB;
    nlines++;
  }

  public void PQinsert(HalfEdgeVoronoi he, SiteVoronoi v, float offset) {
    HalfEdgeVoronoi last, next;

    he.vertex = v;
    v.refcnt++;
    he.ystar = v.coord.y + offset;

    pq.PQinsert(he);
    PQcount++;
  }

  public void PQdelete(HalfEdgeVoronoi he) {
    HalfEdgeVoronoi last;

    if(he.vertex != null) {
      pq.PQdelete(he);
      PQcount--;
      deref(he.vertex);
      he.vertex = null;
    }
  }

  public float dist(SiteVoronoi s, SiteVoronoi t)
  {
    float dx,dy;
    dx = s.coord.x - t.coord.x;
    dy = s.coord.y - t.coord.y;
    return( (float) Math.sqrt(dx*dx + dy*dy) );
  }

  public SiteVoronoi intersect(HalfEdgeVoronoi el1, HalfEdgeVoronoi el2) {
    EdgeVoronoi e1, e2, e;
    HalfEdgeVoronoi el;
    float d, xint, yint;
    boolean right_of_site;
    SiteVoronoi v;

    e1 = el1.ELedge;
    e2 = el2.ELedge;
    if ( (e1 == null) || (e2 == null) ) 
      return(null);
    if (e1.reg[1] == e2.reg[1])
      return(null);

    d = e1.a * e2.b - e1.b * e2.a;
    if ( (-1.0e-10 < d) && (d < 1.0e-10) )
      return(null);

    xint = (e1.c * e2.b - e2.c * e1.b)/d;
    yint = (e2.c * e1.a - e1.c * e2.a)/d;

    if ( (e1.reg[1].coord.y < e2.reg[1].coord.y) ||
		 ( (e1.reg[1].coord.y == e2.reg[1].coord.y) &&
		   (e1.reg[1].coord.x < e2.reg[1].coord.x) ) ) {
      el = el1;
      e = e1;
    } else {
      el = el2;
      e = e2;
    }
    right_of_site = (xint >= e.reg[1].coord.x);
    if ( (right_of_site && el.ELpm == LE) ||
		 (!right_of_site && el.ELpm == RE) )
      return(null);

    v = new SiteVoronoi();
    v.refcnt = 0;
    v.coord.x = xint;
    v.coord.y = yint;
    return(v);
  }

  public void ELinsert(HalfEdgeVoronoi lb, HalfEdgeVoronoi henew) {
    henew.ELleft = lb;
    henew.ELright = lb.ELright;
    (lb.ELright).ELleft = henew;
    lb.ELright = henew;
  }

  public void deref(SiteVoronoi v) {
    v.refcnt--;
    if (v.refcnt == 0 )
      v = null;
  }

  public EdgeVoronoi bisect(SiteVoronoi s1, SiteVoronoi s2) {
    float dx, dy, adx, ady;
    EdgeVoronoi newedge;

    newedge = new EdgeVoronoi();

    newedge.reg[0] = s1;
    newedge.reg[1] = s2;
    s1.refcnt++; 
    s2.refcnt++;
    newedge.ep[0] = null;
    newedge.ep[1] = null;

    dx = s2.coord.x - s1.coord.x;
    dy = s2.coord.y - s1.coord.y;
    adx = (dx > 0) ? dx : -dx;
    ady = (dy > 0) ? dy : -dy;
    newedge.c = s1.coord.x * dx + s1.coord.y * dy + (dx*dx + dy*dy) * 0.5f;
    if (adx > ady) {
      newedge.a = 1.0f;
      newedge.b = dy/dx;
      newedge.c /= dx;
    } else {
      newedge.b = 1.0f;
      newedge.a = dx/dy;
      newedge.c /= dy;
    }

    newedge.edgenbr = nvedges;
    if (delaunayB)
      out_bisector(newedge);
    nvedges++;
    return(newedge);
  }

  public void endpoint(EdgeVoronoi e, int lr, SiteVoronoi s) {
    e.ep[lr] = s;
    s.refcnt++;;
    if (e.ep[RE-lr] == null)
      return;
    if (voronoiB)
      out_ep(e);
    deref(e.reg[LE]);
    deref(e.reg[RE]);
    e = null;
  }

  public void makevertex(SiteVoronoi v) {
    v.sitenbr = nvertices;
    nvertices++;
  }

  public void ELdelete(HalfEdgeVoronoi he) {
    (he.ELleft).ELright = he.ELright;
    (he.ELright).ELleft = he.ELleft;
    he.ELedge = null;
  }

  public SiteVoronoi rightreg(HalfEdgeVoronoi he) {
    if(he.ELedge == null)
      return(bottomsite);
    return( he.ELpm == LE ? 
			he.ELedge.reg[RE] :
			he.ELedge.reg[LE] );
  }

  public SiteVoronoi leftreg(HalfEdgeVoronoi he) {
    if (he.ELedge == null)
      return(bottomsite);
    return( he.ELpm == LE ? 
			he.ELedge.reg[LE] :
			he.ELedge.reg[RE] );
  }

  public void ELinitialize() {
    list = new ListGNG();
    ELleftend = new HalfEdgeVoronoi(null, 0);
    ELrightend = new HalfEdgeVoronoi(null, 0);
    ELleftend.ELleft = null;
    ELleftend.ELright = ELrightend;
    ELrightend.ELleft = ELleftend;
    ELrightend.ELright = null;
    list.insert(ELleftend, list.head);
    list.insert(ELrightend, list.last());
  }

  public HalfEdgeVoronoi ELleftbnd(FPoint p) {
    HalfEdgeVoronoi he;
    he = (list.first()).elem;
    // Now search linear list of halfedges for the corect one
    if ( he == ELleftend  || (he != ELrightend && right_of(he,p)) ) {
      do {
		he = he.ELright;
      } while ( (he != ELrightend) && right_of(he,p) );
      he = he.ELleft;
    } else { 
      do {
		he = he.ELleft;
      } while ( he != ELleftend && !right_of(he,p) );
    }
    return(he);
  }

  // returns true if p is to right of halfedge e
  public boolean right_of(HalfEdgeVoronoi el, FPoint p) {
    EdgeVoronoi e;
    SiteVoronoi topsite;
    boolean right_of_site, above, fast;
    float dxp, dyp, dxs, t1, t2, t3, yl;

    e = el.ELedge;
    topsite = e.reg[1];
    right_of_site = p.x > topsite.coord.x;
    if(right_of_site && (el.ELpm == LE) )
      return(true);
    if(!right_of_site && (el.ELpm == RE) )
      return (false);

    if (e.a == 1.0) {
      dyp = p.y - topsite.coord.y;
      dxp = p.x - topsite.coord.x;
      fast = false;
      if ( (!right_of_site & e.b < 0.0) | (right_of_site & e.b >= 0.0) ) {
		above = (dyp >= e.b * dxp);
		fast = above;
      }
      else {
		above = (p.x + p.y * e.b) > e.c;
		if(e.b < 0.0)
		  above = !above;
		if (!above)
		  fast = true;
      }
      if (!fast) {
		dxs = topsite.coord.x - (e.reg[0]).coord.x;
		above = (e.b * (dxp*dxp - dyp*dyp)) <
		  (dxs * dyp * (1.0 + 2.0 * dxp/dxs + e.b * e.b));
		if(e.b < 0.0)
		  above = !above;
      }
    } else {  // e.b == 1.0
      yl = e.c - e.a * p.x;
      t1 = p.y - yl;
      t2 = p.x - topsite.coord.x;
      t3 = yl - topsite.coord.y;
      above = t1*t1 > t2*t2 + t3*t3;
    }
    return (el.ELpm == LE ? above : !above);
  }

  public SiteVoronoi nextsite() {
    siteidx++;
    if (siteidx > nsites)
      return(null);
    return(vsites[siteidx]);
  }

  /**
   * The mouse-selected node.
   */
  protected NodeGNG pick;
  /**
   * The flag for mouse-selected node.
   */
  protected boolean pickfixed;
  Image offscreen;
  Dimension offscreensize;
  Graphics offgraphics;
    
  /**
   * The color of the winner node (teach-mode).
   */
  protected final Color winnerColor = Color.red;
  /**
   * The color of the second node (teach-mode).
   */
  protected final Color secondColor = Color.orange;
  /**
   * The color of the last moved nodes (teach-mode).
   */
  protected final Color movedColor = Color.yellow;
  /**
   * The color of the last inserted node (teach-mode).
   */
  protected final Color insertedColor = Color.blue;
  /**
   * The color of the shown signal (teach-mode).
   */
  protected final Color signalColor = Color.black;
  /**
   * The color of the mouse-selected node.
   */
  protected final Color selectColor = Color.pink;
  /**
   * The color of the edges.
   */
  protected final Color edgeColor = Color.black;
  /**
   * The color of the Voronoi diagram.
   */
  protected final Color voronoiColor = Color.red;
  /**
   * The color of the Delaunay diagram.
   */
  protected final Color delaunayColor = Color.orange;
  /**
   * The color of the nodes.
   */
  protected final Color nodeColor = Color.green;
  /**
   * The color for debug information.
   */
  protected final Color debugColor = Color.gray;
  /**
   * The color of the distribution.
   */
  protected final Color distribColor = new Color(203, 205, 252);
  /**
   * The color of the low density distribution.
   */
  protected final Color lowDistribColor = new Color(203, 205, 252);
  /**
   * The color of the high density distribution.
   */
  protected final Color highDistribColor = new Color(152, 161, 250);
  
  /**
   * Paint a node.
   * 
   * @param g            The graphic context
   * @param n            The node
   */
  public void paintNode(Graphics g, NodeGNG n) {
    int RADIUS = 10;
    Color col = nodeColor;

    if (teachB && (algo != 5) ) {
      if (n.winner) {
		RADIUS += 5;
		col = winnerColor;
      } else if (n.second) {
		RADIUS += 4;
		col = secondColor;
      } else if (n.moved) {
		RADIUS += 2;
		col = movedColor;
      }
    }

    if (n.inserted)  {
      RADIUS += 2;
      col = insertedColor;
    }

	if ( (algo == 5) && (!n.moved) ) {
      RADIUS += 4;
      col = movedColor;
	}

    g.setColor(col);
    g.fillOval((int)n.x - (RADIUS/2), (int)n.y - (RADIUS/2), RADIUS, RADIUS);
    g.setColor(Color.black);
    g.drawOval((int)n.x - (RADIUS/2), (int)n.y - (RADIUS/2), RADIUS, RADIUS);

  }


  /**
   * Update the drawing area.
   * 
   * @param g          The graphic context
   */
  public synchronized void update(Graphics g) {
    Dimension d = size();
    int r1 = (int) d.width/4;
    int l1 = (int) d.height/4;
    int r2 = (int) d.width/2;
    int l2 = (int) d.height/2;
    int cx = (int) (d.width/2);
    int cy = (int) (d.height/2);
    int xA[] = new int[MAX_COMPLEX];
    int yA[] = new int[MAX_COMPLEX];
    int w;
    int h;
    int ringRadius;
	int i, x, y;

    if ((offscreen == null) ||
		(d.width != offscreensize.width) ||
		(d.height != offscreensize.height)) {
      offscreen = createImage(d.width, d.height);
      offscreensize = d;
      offgraphics = offscreen.getGraphics();
      offgraphics.setFont(getFont());
    }

	if (whiteB)
	  offgraphics.setColor(Color.white);
	else
	  offgraphics.setColor(getBackground());

    offgraphics.fillRect(0, 0, d.width, d.height);

	// Set color for distribution
	if (algo != 5)
	  offgraphics.setColor(distribColor);

    switch (distribution) {
    case 0: // Rectangle
      r1 = (int) d.width/4;
      l1 = (int) d.height/4;
      r2 = (int) d.width/2;
      l2 = (int) d.height/2;
      offgraphics.fillRect(r1, l1, r2, l2);
      break;
    case 1: // Circle

      l2 = (int) ((cx < cy) ? cx : cy); // Diameter

      r1 = (int) d.width/2 -l2/2;
      l1 = (int) d.height/2 -l2/2;

      offgraphics.fillOval(r1, l1, l2, l2);
      break;
    case 2: // Ring
      l2 = (int) ((cx < cy) ? cx : cy); // Diameter

      r1 = (int) cx - l2;
      l1 = (int) cy - l2;
      ringRadius = (int) (l2 * RING_FACTOR);

      offgraphics.fillOval(r1, l1, 2*l2, 2*l2);
	  if (whiteB)
		offgraphics.setColor(Color.white);
	  else
		offgraphics.setColor(getBackground());
      offgraphics.fillOval(r1 + ringRadius,
						   l1+ringRadius,
						   2*l2-2*ringRadius,
						   2*l2-2*ringRadius);
      break;
    case 3: // Complex (1)
      w = (int) d.width/9;
      h = (int) d.height/5;
      xA[0] = w;
      yA[0] = h;
      xA[1] = w;
      yA[1] = 2*h;
      xA[2] = w;
      yA[2] = 3*h;
      xA[3] = 2*w;
      yA[3] = 3*h;
      xA[4] = 3*w;
      yA[4] = 3*h;
      xA[5] = 3*w;
      yA[5] = 2*h;
      xA[6] = 3*w;
      yA[6] = h;
      xA[7] = 4*w;
      yA[7] = h;
      xA[8] = 5*w;
      yA[8] = h;
      xA[9] = 5*w;
      yA[9] = 2*h;
      xA[10] = 5*w;
      yA[10] = 3*h;
      xA[11] = 7*w;
      yA[11] = h;
      xA[12] = 7*w;
      yA[12] = 2*h;
      xA[13] = 7*w;
      yA[13] = 3*h;

      for (i = 0; i < 14; i++)
		offgraphics.fillRect(xA[i], yA[i], w, h);
      break;
    case 4: // Complex (2)
      w = (int) d.width/9;
      h = (int) d.height/7;
      xA[0] = w;
      yA[0] = 5*h;
      xA[1] = w;
      yA[1] = 4*h;
      xA[2] = w;
      yA[2] = 3*h;
      xA[3] = w;
      yA[3] = 2*h;
      xA[4] = 1*w;
      yA[4] = h;
      xA[5] = 2*w;
      yA[5] = h;
      xA[6] = 3*w;
      yA[6] = h;
      xA[7] = 4*w;
      yA[7] = h;
      xA[8] = 5*w;
      yA[8] = 1*h;
      xA[9] = 5*w;
      yA[9] = 2*h;
      xA[10] = 5*w;
      yA[10] = 3*h;
      xA[11] = 3*w;
      yA[11] = 3*h;
      xA[12] = 3*w;
      yA[12] = 4*h;
      xA[13] = 3*w;
      yA[13] = 5*h;
      xA[14] = 4*w;
      yA[14] = 5*h;
      xA[15] = 5*w;
      yA[15] = 5*h;
      xA[16] = 6*w;
      yA[16] = 5*h;
      xA[17] = 7*w;
      yA[17] = 5*h;
      xA[18] = 7*w;
      yA[18] = 4*h;
      xA[19] = 7*w;
      yA[19] = 3*h;
      xA[20] = 7*w;
      yA[20] = 2*h;
      xA[21] = 7*w;
      yA[21] = 1*h;

      for (i = 0; i < 22; i++)
		offgraphics.fillRect(xA[i], yA[i], w, h);
      break;
    case 5: // Complex (3)
      w = (int) d.width/13;
      h = (int) d.height/11;
      xA[0] = w;
      yA[0] = h;
      xA[1] = w;
      yA[1] = 2*h;
      xA[2] = w;
      yA[2] = 3*h;
      xA[3] = w;
      yA[3] = 4*h;
      xA[4] = 1*w;
      yA[4] = 5*h;
      xA[5] = 1*w;
      yA[5] = 6*h;
      xA[6] = 1*w;
      yA[6] = 7*h;
      xA[7] = 1*w;
      yA[7] = 8*h;
      xA[8] = 1*w;
      yA[8] = 9*h;
      xA[9] = 2*w;
      yA[9] = 1*h;
      xA[10] = 3*w;
      yA[10] = 1*h;
      xA[11] = 4*w;
      yA[11] = 1*h;
      xA[12] = 5*w;
      yA[12] = 1*h;
      xA[13] = 6*w;
      yA[13] = 1*h;
      xA[14] = 7*w;
      yA[14] = 1*h;
      xA[15] = 8*w;
      yA[15] = 1*h;
      xA[16] = 9*w;
      yA[16] = 1*h;
      xA[17] = 9*w;
      yA[17] = 2*h;
      xA[18] = 9*w;
      yA[18] = 3*h;
      xA[19] = 9*w;
      yA[19] = 4*h;
      xA[20] = 9*w;
      yA[20] = 5*h;
      xA[21] = 9*w;
      yA[21] = 6*h;
      xA[22] = 9*w;
      yA[22] = 7*h;
      xA[23] = 8*w;
      yA[23] = 7*h;
      xA[24] = 7*w;
      yA[24] = 7*h;
      xA[25] = 6*w;
      yA[25] = 7*h;
      xA[26] = 5*w;
      yA[26] = 7*h;
      xA[27] = 5*w;
      yA[27] = 6*h;
      xA[28] = 5*w;
      yA[28] = 5*h;
      xA[29] = 3*w;
      yA[29] = 3*h;
      xA[30] = 3*w;
      yA[30] = 4*h;
      xA[31] = 3*w;
      yA[31] = 5*h;
      xA[32] = 3*w;
      yA[32] = 6*h;
      xA[33] = 3*w;
      yA[33] = 7*h;
      xA[34] = 3*w;
      yA[34] = 8*h;
      xA[35] = 3*w;
      yA[35] = 9*h;
      xA[36] = 4*w;
      yA[36] = 3*h;
      xA[37] = 5*w;
      yA[37] = 3*h;
      xA[38] = 6*w;
      yA[38] = 3*h;
      xA[39] = 7*w;
      yA[39] = 3*h;
      xA[40] = 7*w;
      yA[40] = 4*h;
      xA[41] = 7*w;
      yA[41] = 5*h;
      xA[42] = 4*w;
      yA[42] = 9*h;
      xA[43] = 5*w;
      yA[43] = 9*h;
      xA[44] = 6*w;
      yA[44] = 9*h;
      xA[45] = 7*w;
      yA[45] = 9*h;
      xA[46] = 8*w;
      yA[46] = 9*h;
      xA[47] = 9*w;
      yA[47] = 9*h;
      xA[48] =10*w;
      yA[48] = 9*h;
      xA[49] =11*w;
      yA[49] = 9*h;
      xA[50] =11*w;
      yA[50] = 8*h;
      xA[51] =11*w;
      yA[51] = 7*h;
      xA[52] =11*w;
      yA[52] = 6*h;
      xA[53] =11*w;
      yA[53] = 5*h;
      xA[54] =11*w;
      yA[54] = 4*h;
      xA[55] =11*w;
      yA[55] = 3*h;
      xA[56] =11*w;
      yA[56] = 2*h;
      xA[57] =11*w;
      yA[57] = 1*h;

      for (i = 0; i < 58; i++)
		offgraphics.fillRect(xA[i], yA[i], w, h);
      break;

    case 6: // HiLo-Density
      w = (int) d.width/10;
      h = (int) d.height/10;
      xA[0] = 2 * w;
      yA[0] = 4 * h;
      xA[1] = 5 * w;
      yA[1] = 1 * h;

	  if (algo != 5)
		offgraphics.setColor(highDistribColor);
      offgraphics.fillRect(xA[0], yA[0], w, h);
	  if (algo != 5)
		offgraphics.setColor(lowDistribColor);
      offgraphics.fillRect(xA[1], yA[1], 4 * w, 8 * h);
      break;

	case 7: // discrete
	  int RADIUS = 2;
      for (i = 0; i < numDiscreteSignals; i++) {
		x = (int) (discreteSignalsX[i]);
		y = (int) (discreteSignalsY[i]);
		
		offgraphics.setColor(distribColor);
		offgraphics.fillOval(x - 1, y - 1, 2, 2);
		offgraphics.setColor(Color.black);
		offgraphics.drawOval(x - 1, y - 1, 2, 2);
      }
	  break;

    case 8: // Complex (4)
      w = (int) d.width/17;
      h = (int) d.height/8;
      xA[0] = w;
      yA[0] = 2*h;
      xA[1] = w;
      yA[1] = 3*h;
      xA[2] = w;
      yA[2] = 4*h;
      xA[3] = w;
      yA[3] = 5*h;
      xA[4] = 2*w;
      yA[4] = 5*h;
      xA[5] = 3*w;
      yA[5] = 5*h;
      xA[6] = 3*w;
      yA[6] = 4*h;
      xA[7] = 3*w;
      yA[7] = 3*h;
      xA[8] = 3*w;
      yA[8] = 2*h;
      xA[9] = 4*w;
      yA[9] = 2*h;
      xA[10] = 5*w;
      yA[10] = 2*h;
      xA[11] = 6*w;
      yA[11] = 2*h;
      xA[12] = 7*w;
      yA[12] = 2*h;
      xA[13] = 7*w;
      yA[13] = 3*h;
      xA[14] = 7*w;
      yA[14] = 4*h;
      xA[15] = 7*w;
      yA[15] = 5*h;
      xA[16] = 8*w;
      yA[16] = 5*h;
      xA[17] = 9*w;
      yA[17] = 5*h;
      xA[18] = 10*w;
      yA[18] = 5*h;
      xA[19] = 11*w;
      yA[19] = 5*h;
      xA[20] = 11*w;
      yA[20] = 4*h;
      xA[21] = 11*w;
      yA[21] = 3*h;
      xA[22] = 11*w;
      yA[22] = 2*h;
      xA[23] = 14*w;
      yA[23] = 2*h;
      xA[24] = 15*w;
      yA[24] = 2*h;
      xA[25] = 15*w;
      yA[25] = 3*h;
      xA[26] = 15*w;
      yA[26] = 4*h;
      xA[27] = 15*w;
      yA[27] = 5*h;

      for (i = 0; i < 28; i++)
		offgraphics.fillRect(xA[i], yA[i], w, h);
      break;
    case 9: // Moving and Jumping Rectangle
      r2 = (int) d.width/4;
      l2 = (int) d.height/4;
      r1 = (int) (0.75 * (d.width/2 +
		Math.IEEEremainder((double)0.2 * numRun,(double)(d.width))));
      l1 = (int) (0.75 * (d.height/2 +
		Math.IEEEremainder((double)0.2 * numRun,(double)(d.height))));
	  if (DEBUG) 
		System.out.println("Rectangle x = " + r1);

      offgraphics.fillRect(r1, l1, r2, l2);
      break;
    case 10: // Moving Rectangle
      r2 = (int) d.width/4;
      l2 = (int) d.height/4;
      r1 = (int) (0.75 * (d.width/2 +
		bounceX * Math.IEEEremainder((double)0.2 * numRun,
									 (double)(d.width))));
      l1 = (int) (0.75 * (d.height/2 +
		bounceY * Math.IEEEremainder((double)0.2 * numRun,
									 (double)(d.height))));
	  if (DEBUG) 
		System.out.println("Rectangle x = " + r1 +
						   "\nremainder x = " +
						   Math.IEEEremainder((double)0.2 * numRun,
											  (double)(d.width)) +
						   "\nremainder y = " +
						   Math.IEEEremainder((double)0.2 * numRun,
											  (double)(d.height)));

      offgraphics.fillRect(r1, l1, r2, l2);
      break;

    case 11: // Jumping Rectangle
      r2 = (int) d.width/4;
      l2 = (int) d.height/4;

      offgraphics.fillRect(jumpX, jumpY, r2, l2);
      break;

    case 12: // R.Mouse Rectangle
      r2 = (int) d.width/4;
      l2 = (int) d.height/4;

      offgraphics.fillRect(jumpX, jumpY, r2, l2);
      break;
    }      

	// Draw the edges
	if (edgesB) {
	  int x1, y1, x2, y2;
	  EdgeGNG e;
	  for (i = 0 ; i < nedges ; i++) {
		e = edges[i];
		x1 = (int)nodes[e.from].x;
		y1 = (int)nodes[e.from].y;
		x2 = (int)nodes[e.to].x;
		y2 = (int)nodes[e.to].y;
		offgraphics.setColor(edgeColor);
		offgraphics.drawLine(x1, y1, x2, y2);
	  }
	}

    // Draw the Voronoi diagram
    if (voronoiB || delaunayB) {
	  LineGNG l;
      for (i = 0; i < nlines; i++) {
		l = lines[i];
		if (vd[i])
		  offgraphics.setColor(voronoiColor);
		else
		  offgraphics.setColor(delaunayColor);
		offgraphics.drawLine(l.x1, l.y1, l.x2, l.y2);
      }
    }

	// Draw the nodes
	if (nodesB)
	  for (i = 0; i < nnodes; i++)
		paintNode(offgraphics, nodes[i]);

    if ( teachB ) {
	  int r = 6;
	  int offset_x = 12;
	  int offset2_x = offset_x + 5;
	  int offset_y = (int) d.height/4;

	  if (algo == 5) {
		// Draw legend
		offgraphics.setColor(Color.black);
		offgraphics.drawString("Legend:", 	2, offset_y); offset_y += 15;

		offgraphics.setColor(movedColor);
		offgraphics.fillOval(offset_x - r, offset_y - r, r, r);
		offgraphics.setColor(Color.black);
		offgraphics.drawString("Not moved", offset2_x,
							   offset_y); offset_y += 15;
	  } else {
		offgraphics.setColor(signalColor);
		offgraphics.fillOval(SignalX - r/2, SignalY - r/2, r, r);

		// Draw legend
		offgraphics.setColor(Color.black);
		offgraphics.drawString("Legend:", 	2, offset_y); offset_y += 15;

		offgraphics.setColor(winnerColor);
		offgraphics.fillOval(offset_x - r, offset_y - r, r, r);
		offgraphics.setColor(Color.black);
		offgraphics.drawString("Winner", offset2_x, offset_y); offset_y += 15;

		if (algo != 1) {
		  offgraphics.setColor(secondColor);
		  offgraphics.fillOval(offset_x - r, offset_y - r, r, r);
		  offgraphics.setColor(Color.black);
		  offgraphics.drawString("Second", offset2_x, offset_y); offset_y += 15;
		}

		if (algo == 0) {
		  offgraphics.setColor(movedColor);
		  offgraphics.fillOval(offset_x - r, offset_y - r, r, r);
		  offgraphics.setColor(Color.black);
		  offgraphics.drawString("Neighbors", offset2_x, offset_y); offset_y += 15;

		  offgraphics.setColor(insertedColor);
		  offgraphics.fillOval(offset_x - r, offset_y - r, r, r);
		  offgraphics.setColor(Color.black);
		  offgraphics.drawString("Last inserted", offset2_x, offset_y); offset_y += 15;
		}
	  
		offgraphics.setColor(signalColor);
		offgraphics.fillOval(offset_x - r, offset_y - r, r, r);
		offgraphics.setColor(Color.black);
		offgraphics.drawString("Signal", offset2_x, offset_y); offset_y += 15;
	  }
	}

	offgraphics.setColor(Color.black);
    offgraphics.drawString(String.valueOf(numRun), 10, 10);
    if (maxNodes == 1)
      offgraphics.drawString(String.valueOf(nnodes) + " Node",
							 10, d.height - 10);
    else
      offgraphics.drawString(String.valueOf(nnodes) + " Nodes",
							 10, d.height - 10);

    offgraphics.drawString(DGNG_VERSION, d.width - 40, 10);
    if ( readyLBG_B && (algo == 5) )
      offgraphics.drawString("READY!", d.width-50, d.height-10);
    if ( fineTuningB && (algo == 6) )
      offgraphics.drawString(fineTuningS, d.width-130, d.height-10);

    if ( signalsB && (algo != 5) ) {
      for (i = 0; i < stepSize; i++) {
		x = (int) (lastSignalsX[i]);
		y = (int) (lastSignalsY[i]);

		offgraphics.setColor(Color.green);
		offgraphics.fillOval(x - 1, y - 1, 2, 2);
		offgraphics.setColor(Color.black);
		offgraphics.drawOval(x - 1, y - 1, 2, 2);
      }
    }


    if ( insertedSoundB && soundB ) {
      graph.play(graph.getCodeBase(), "audio/drip.au");
      insertedSoundB = false;
    }

    if (algo == 5) {
      for (i = 0; i < numDiscreteSignals; i++) {
		x = (int) (discreteSignalsX[i]);
		y = (int) (discreteSignalsY[i]);
		
		offgraphics.setColor(distribColor);
		offgraphics.fillOval(x - 1, y - 1, 2, 2);
		offgraphics.setColor(Color.black);
		offgraphics.drawOval(x - 1, y - 1, 2, 2);
      }
    }

	// Show error graph or not
	if (errorGraph != null) {
	  if (errorGraphB)
		errorGraph.show();
	  else
		errorGraph.hide();
	}

    g.drawImage(offscreen, 0, 0, null);
  }

  public synchronized boolean mouseDown(Event evt, int x, int y) {
	if (evt.metaDown() && (distribution == 12) ) {
	  Dimension d = size();
	  //System.out.println("RIGHT Mousebutton pressed!");

	  jumpX = x;
	  jumpY = y;
	  // Draw distribution only inside the visible region
	  if (jumpX > (0.75 * d.width))
		jumpX = (int) (0.75 * d.width);

	  if (jumpY > (0.75 * d.height))
		jumpY = (int) (0.75 * d.height);
	  
	  repaint();
	  return true;
	}

    float bestDist = Float.MAX_VALUE;
	NodeGNG n;
	float dist;

	//	if (evt.modifiers == Event.ALT_MASK)
	//	  System.out.println("MIDDLE Mousebutton pressed!");

    for (int i = 0 ; i < nnodes ; i++) {
      n = nodes[i];
      dist = (n.x - x) * (n.x - x) + (n.y - y) * (n.y - y);
      if (dist < bestDist) {
		pick = n;
		bestDist = dist;
      }
    }
    pickfixed = pick.fixed;
    pick.fixed = true;
    pick.x = x;
    pick.y = y;

	if (algo == 5)
	  pick.moved = true;

    nodesMovedB = true;
    repaint();
    return true;
  }

  public synchronized boolean mouseDrag(Event evt, int x, int y) {
    Dimension d = size();

	if (evt.metaDown() && (distribution == 12) ) {
	  //System.out.println("RIGHT Mousebutton dragged!");
	  jumpX = x;
	  jumpY = y;

	  // Draw distribution only inside the visible region
	  if (jumpX < 0)
		jumpX = 0;
	  else if (jumpX > (0.75 * d.width))
		jumpX = (int) (0.75 * d.width);

	  if (jumpY < 0)
		jumpY = 0;
	  else if (jumpY > (0.75 * d.height))
		jumpY = (int) (0.75 * d.height);

	  repaint();
	  return true;
	}

    pick.x = x;
    pick.y = y;

    // Draw nodes only inside the visible region
    if (pick.x < 0)
      pick.x = 0;
	else if (pick.x > d.width)
      pick.x = d.width;

    if (pick.y < 0)
      pick.y = 0;
	else if (pick.y > d.height)
      pick.y = d.height;

    nodesMovedB = true;
    repaint();
    return true;
  }

  public synchronized boolean mouseUp(Event evt, int x, int y) {
    Dimension d = size();

	if (evt.metaDown() && (distribution == 12) ) {
	  //System.out.println("RIGHT Mousebutton released!");
	  jumpX = x;
	  jumpY = y;

	  // Draw distribution only inside the visible region
	  if (jumpX < 0)
		jumpX = 0;
	  else if (jumpX > (0.75 * d.width))
		jumpX = (int) (0.75 * d.width);

	  if (jumpY < 0)
		jumpY = 0;
	  else if (jumpY > (0.75 * d.height))
		jumpY = (int) (0.75 * d.height);

	  repaint();
	  return true;
	}

    pick.x = x;
    pick.y = y;
    pick.fixed = pickfixed;

    // Draw nodes only inside the visible region
    if (pick.x < 0)
      pick.x = 0;
    else if (pick.x > d.width)
      pick.x = d.width;

    if (pick.y < 0)
      pick.y = 0;
    else if (pick.y > d.height)
      pick.y = d.height;

    pick = null;
  
    nodesMovedB = true;
    repaint();
    return true;
  }

  public void start() {
    relaxer = new Thread(this);
    relaxer.start();

    if ( errorGraphB  && (errorGraph != null) )
	  errorGraph.show();
  }

  public void stop() {
    relaxer.stop();
    if ( errorGraphB  && (errorGraph != null) )
	  errorGraph.hide();
  }

  public void destroy() {
	if ( errorGraphB  && (errorGraph != null) ) {
	  errorGraph.dispose();
	  errorGraph = null;
	}
  }

  public void graphClose() {
    if ( errorGraphB  && (errorGraph != null) ) {
	  errorGraph.dispose();
	  errorGraph = null;
	}
  }

}
