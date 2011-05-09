package memagents.memory.gng;
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

import java.util.*;

/**
 * A class representing a node for all neural algorithms.
 *
 */
public class NodeGNG {
  /**
   * The maximum number of neighbors of a node
   */
  protected final int MAX_NEIGHBORS = 10;
  /**
   * The flag for mouse-selection
   */
  protected boolean fixed = false;
  /**
   * The flag for the winner.
   *  This node is nearest to the input signal.
   */
  protected boolean winner = false;
  /**
   * The flag for the second winner.
   *  This node is second nearest to the input signal.
   */
  protected boolean second = false;
  /**
   * The flag for movement.
   *  This node moved from the last position (GNG, LBG).
   */
  protected boolean moved = false;
  /**
   * The flag for insertion.
   *  This node was inserted last (GNG).
   */
  protected boolean inserted = false;

  /**
   * The x index in the grid (GG, SOM).
   */
  protected int x_grid = -1;
  /**
   * The y index in the grid (GG, SOM).
   */
  protected int y_grid = -1;
  /**
   * The x-position of the node.
   */
  protected float x = 0.0f;
  /**
   * The old x-position of the moved node.
   */
  protected float x_old = 0.0f;
  /**
   * The y-position of the node.
   */
  protected float y = 0.0f;
  /**
   * The old y-position of the moved node.
   */
  protected float y_old = 0.0f;
  /**
   * The error of the node.
   */
  protected float error = 0.0f;
  /**
   * The distance from the input signal.
   */
  protected float dist = Float.MAX_VALUE;
  /**
   * The utility for GNG-U and LBG-U
   */
  protected float utility = 0.0f;
  /**
   * The number of neighbors.
   */
  protected int nNeighbor = 0;
  /**
   * The list of neighbors.
   */
  protected int neighbor[] = new int[MAX_NEIGHBORS];
  /**
   * The list of neighbors.
   */
  protected Vector signals = new Vector();

  /**
   * Return number of signals.
   *
   * @return		number of signals
   */
  protected int numSignals() {
    return(signals.size());
  }

  /**
   * Add a signal index.
   *
   * @param sig		The index of the signal
   */
  protected void addSignal(int sig) {
    signals.addElement( new Integer(sig) );
  }

  /**
   * Remove a signal index and return the index.
   *
   * @return		The index of the signal or -1.
   */
  protected int removeSignal() {
	int size = signals.size();
    if (size < 1) 
      return(-1);

	// remove last element from the vector and return it
	Integer lastSignal = (Integer)signals.lastElement();
	signals.removeElementAt(size-1);
    return(lastSignal.intValue());
  }

  /**
   * Return the number of neighbors.
   *
   * @return	Number of neighbors
   */
  protected int numNeighbors() {
    return nNeighbor;
  }

  /**
   * Is there space for more neighbors?
   *
   * @return	Space enough?
   */
  protected boolean moreNeighbors() {
    return (MAX_NEIGHBORS != nNeighbor);
  }

  /**
   * Returns the i-th neighbor.
   *
   * @param i	The index of a neighbor
   * @return	The index of a node
   */
  protected int neighbor(int i) {
    return neighbor[i];
  }

  /**
   * Deletes the node from the list of neighbors.
   *
   * @param node	The index of a node
   */
  protected void deleteNeighbor(int node) {
    for (int i = 0; i < nNeighbor; i++) {
      if (node == neighbor[i]) {
		nNeighbor--;
		neighbor[i] = neighbor[nNeighbor];
		neighbor[nNeighbor] = -1;
		return;
      }
    }
  }

  /**
   * Replaces the old node with a new node.
   *
   * @param old		The index of a node
   * @param newN	The index of a node
   * @see ComputeGNG#deleteNode
   */
  protected void replaceNeighbor(int old, int newN) {
    for (int i = 0; i < nNeighbor; i++) {
      if (old == neighbor[i]) {
		neighbor[i] = newN;
		return;
      }
    }
  }

  /**
   * Is the node a neighbor?
   *
   * @param n		The index of a node
   * @return		Neighbor?
   */
  protected boolean isNeighbor(int n) {
    for (int i = 0; i < nNeighbor; i++)
      if (n == neighbor[i])
		return true;
    return false;
  }

  /**
   * Add a node to the neighborhood.
   *
   * @param node	The index of a node
   */
  protected void addNeighbor(int node) {
    if (nNeighbor == MAX_NEIGHBORS) 
      return;

    neighbor[nNeighbor] = node;
    nNeighbor++;
  }
  
  public float getX() { return x; }
  public float getY() { return y; }
}
