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
 * A class representing a float point in the plane.
 *
 */
public class FPoint {
  /**
   * The x coordinate
   */
  public float x;
  /**
   * The y coordinate
   */
  public float y;

  /**
   * Constructor.
   * 
   */
  public FPoint() {
    this.x = -1.0f;
    this.y = -1.0f;
  }

  /**
   * Constructor, allows setting the coordinates.
   * 
   * @param x        The x coordinate
   * @param y        The y coordinate
   */
  public FPoint(float x, float y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Set the member variables.
   * 
   * @param x        The x coordinate
   * @param y        The y coordinate
   */
  public void set(float x, float y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Set the member variables.
   * 
   * @param p        The coordinates
   */
  public void set(FPoint p) {
    x = p.x;
    y = p.y;
  }

  /**
   * Test the member variables.
   * 
   * @param x        The x coordinate
   * @param y        The y coordinate
   * @return	     Equal?
   */
  public boolean equal(float x, float y) {
    if ( (this.x == x) && (this.y == y) )
      return(true);
    else
      return(false);
  }

}
