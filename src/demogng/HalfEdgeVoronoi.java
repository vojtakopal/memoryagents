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

/**
 * A class representing a halfedge in the Voronoi diagram
 *
 */
class HalfEdgeVoronoi {
  public HalfEdgeVoronoi ELleft;
  public HalfEdgeVoronoi ELright;
  public EdgeVoronoi ELedge = null;
  public SiteVoronoi vertex = null;
  public int ELpm = -1;
  public int ELrefcnt = -1;
  public float ystar = -1.0f;

  public HalfEdgeVoronoi() {
    ELedge = new EdgeVoronoi();
    vertex = new SiteVoronoi();
    ELpm = 0;
    ystar = 0.0f;
  }

  public HalfEdgeVoronoi(EdgeVoronoi e, int pm) {
    ELedge = e;
    ELpm = pm;
    vertex = null;
    ELrefcnt = 0;
  }

  /**
   * Returns whether this edge is greater than the passed edge.
   *
   * @param HalfEdgeVoronoi he	 	The edge to compare this edge to.
   */
  public boolean greaterThan(HalfEdgeVoronoi he) {
    return ystar > he.ystar;
  }
  
  /**
   * Returns whether this edge is equal to the passed edge.
   *
   * @param HalfEdgeVoronoi he	 	The edge to compare this edge to.
   */
  public boolean equal(HalfEdgeVoronoi he) {
    return ystar == he.ystar;
  }
  
  /**
   * Prints this edge.
   */
  public void print() {
    System.out.println("HE: ystar = " + ystar + ", ELpm = " + ELpm);
  }

}
