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
 * A class which represents the elements of the double linked list class
 *  ListGNG.
 *
 */
class ListElem {

  /**
   * The left neighbour.
   */
  ListElem		left;
  /**
   * The right neighbour.
   */
  ListElem		right;
  /**
   * The element is a halfedge.
   */
  HalfEdgeVoronoi	elem;

  /**
   * Constructor, initializes member data.
   */
  ListElem() {
    left = null;
    right = null;
    elem = null;
  }

  /**
   * Constructor, allows setting data.
   *
   * @param HalfEdgeVoronoi elem 	The data elem
   */
  ListElem(HalfEdgeVoronoi elem) {
    left = right = null;
    this.elem = elem;
  }

  /**
   * Constructor, allows setting neighbors and data.
   *
   * @param l 		Left neighbor
   * @param r 		Right neighbor
   * @param elem 	The data elem
   */
  ListElem(ListElem l, ListElem r, HalfEdgeVoronoi elem) {
    left = l;
    right = r;
    this.elem = elem;
  }

  /**
   * Print the element.
   */
  public void print() {
    if(elem != null)
      elem.print();
  }
}
