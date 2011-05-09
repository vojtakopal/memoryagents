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


/**
 * A class representing an edge. 
 *  For example:
 * <pre>
 *    EdgeGNG e = new EdgeGNG();
 *    e.from(Node1);
 *    e.to(Node2);
 * </pre>
 *
 */
public class EdgeGNG {
  /**
   * The starting point of the edge
   * @see EdgeGNG
   */
  protected int from = -1;
  /**
   * The end point of the edge
   * @see EdgeGNG
   */
  protected int to = -1;
  /**
   * The age of this edge.
   * @see EdgeGNG
   */
  protected int age = 0;

  /**
   * Replace a node with a new one. This is neccessary after deleting a node
   *  in the static array. In most cases the deleted node will be replaced by
   *  the last node in the static array.
   * 
   * @param old        The index of a node
   * @param newN       The index of a node
   * @see ComputeGNG#deleteNode
   */
  protected void replace(int old, int newN) {
    if (from == old)
      from = newN;
    if (to == old)
      to = newN;
  }
}
