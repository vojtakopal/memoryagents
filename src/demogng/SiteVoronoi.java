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
 * A class representing a site in the Voronoi diagram
 *
 */
class SiteVoronoi {
  /**
   * The coordinate
   */
  public FPoint coord = null;
  /**
   * The number of the site
   */
  public int sitenbr = 0;
  /**
   * The reference counter of the site
   */
  public int refcnt = 0;

  /**
   * The constructor of the SiteVoronoi class.
   * 
   */
  public SiteVoronoi() {
    coord = new FPoint();
    sitenbr = -1;
    refcnt = 0;
  }
  /**
   * The constructor of the SiteVoronoi class.
   * 
   * @param p        The coordinates
   * @param sitenbr  The identifier of the site
   */
  public SiteVoronoi(FPoint p, int sitenbr) {
    coord = p;
    this.sitenbr = sitenbr;
    refcnt = 0;
  }

  public void print() {
    System.out.println("Site: (Nr. " + sitenbr + "):" + 
		       coord.x + ", " + coord.y +
		       " (refcnt = " + refcnt + ")");
  }

}
