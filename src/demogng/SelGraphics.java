package demogng;
// ========================================================================== ;
//                                                                            ;
//     Copyright (1996-1998) Hartmut S. Loos                                  ;
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
// This class was originally written by                                       ;
// Christian Kurzke (cnkurzke@cip.informatik.uni-erlangen.de) and             ;
// Ningsui Chen (nichen@cip.informatik.uni-erlangen.de).                      ;
// The original version of the SelGraphics class is included in a Java        ;
// applet at http://www2.informatik.uni-erlangen.de                           ;
//         /IMMD-II/Persons/jacob/Evolvica/Java/CharacterEvolution/index.html ;
// The applet shows an Evolutionary Algorithm (very impressive!).             ;
//                                                                            ;
// I have only made minor changes to this class. Many thanks to Christian     ;
// Kurzke and Ningsui Chen for this nice graph class.                         ;
//                                                                            ;
// ========================================================================== ;
//                                                                            ;
// Copyright 1996-1998  Hartmut S. Loos                                       ;
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
import java.util.*;

/**
 * Generates a graph.
 * It scales its axis automatically, so the window is always
 * filled at its maximum.
 *
 */
public class SelGraphics extends Canvas {
  /**
   * The initial maximum value for the x-axis.
   */
  final int INIT_MAX_X = 50;
  /**
   * The initial maximum value for the y-axis
   */
  final int INIT_MAX_Y = 20;

  Graphics g;
  Vector  data;

  int ScaleMaxX = INIT_MAX_X;
  int ScaleMaxY = INIT_MAX_Y;

  /**
   * The constructor.
   * 
   */
  public SelGraphics() {
    super();

	// The initial size of the frame
	resize(400,200);
    data = new Vector(1,1);
    data.addElement( new Vector(100,20) );
    show();
  }
  
  public void update(Graphics g) {
    paint(g);
  }

  public void paintComponents(Graphics g) {
    paint(this.getGraphics());
  }

  /**
   * Draws the chart on the screen.
   *
   * @param g	The drawing area
   */
  public void paint(Graphics g) {
	int i;
    int lx = 30;
    int ly = 30;
    double sx,sy;
    Graphics da;
    Rectangle size;
    Rectangle Diagram;
    Enumeration Traces;
    
    // get bounds
    da = this.getGraphics();
    size = this.bounds();
    Diagram = new Rectangle(40, 5, size.width - 45, size.height - 25);

    da.setColor(Color.gray);
    da.draw3DRect(0, 0, size.width - 1, size.height - 1, false);
    da.setColor(Color.white);
    da.fillRect(1, 1, size.width - 2, size.height - 2);

    // paint drawing aera
    da.translate(Diagram.x, Diagram.y);
    Color paleyellow = new Color(255, 250, 230);
    da.setColor(paleyellow);
    da.fillRect(0, 0, Diagram.width, Diagram.height);
    da.setColor(Color.red);
    da.drawLine(0, 0, 0, Diagram.height);
    da.drawLine(0, Diagram.height, Diagram.width, Diagram.height);

    sx = ((double) Diagram.width) / ScaleMaxX;
    sy = ((double) Diagram.height) / ScaleMaxY;

    // draw grids
    da.setFont( new Font("Dialog", Font.PLAIN, 12) );
    
    da.setColor( new Color(255, 200, 100) );

	int x;
    for (i = 1; i < (ScaleMaxX/5); i++) {
	  x = (int)(sx * i * 5);

	  if (x > lx)	{
	    da.setColor(Color.orange);
	    da.drawLine(x, 0, x, Diagram.height);
	    da.setColor(Color.red);
	    da.drawString(Integer.toString(i * 5), x - 10, Diagram.height + 15);
	    lx = x + 30;
	  }
	}

	int y;
    for (i = 1; i < (ScaleMaxY/10); i++) {
	  y = (int)(sy * i * 10);

	  if ( y > ly ) {
	    int ry = Diagram.height - y;

	    da.setColor(Color.orange);
	    da.drawLine(0, ry, Diagram.width, ry);
	    da.setColor(Color.red);
	    da.drawString(Integer.toString(i * 10), -35, ry + 5);
	    ly = y + 30;
	  }
	} 

    // paint traces
    Traces = data.elements();
	Double cur;
    while ( Traces.hasMoreElements() ) {
	  Enumeration Current_Trace = null;

	  try {
	    Current_Trace=( (Vector) Traces.nextElement()).elements();
	  }	catch( NoSuchElementException e ) {}

	  if ( Traces.hasMoreElements() )	
		da.setColor(Color.magenta);
	  else
		da.setColor(Color.black);

	  if (Current_Trace != null) {
	    int nx, ny, ox = -1, oy = -1;
	   
	    for (i = 0; Current_Trace.hasMoreElements(); i++) {
		  cur = (Double)(Current_Trace.nextElement());
		  nx = (int)(sx * i);
		  ny = Diagram.height - (int)(cur.doubleValue() * sy);
		  if (oy > 0)
			da.drawLine(ox, oy, nx, ny);
		  ox = nx;
		  oy = ny;
		}
	  }
	}
    
  }
    
  /**
   * Add the data to the graph.
   *
   * @param value            The new value for the graph
   */
  public void add(double value) {
    // add the value to the storage vector
    ((Vector)data.lastElement()).addElement( new Double(value) );

    if ( value > ScaleMaxY) {
	  ScaleMaxY = (int)(1.05 * value);
	  paint(this.getGraphics());
	}

    if (((Vector)data.lastElement()).size() > ScaleMaxX) {
	  ScaleMaxX = ((Vector)data.lastElement()).size() + 20;
	  paint(this.getGraphics());
	}

    // paint its value in the diagram

    int anz = ((Vector)data.lastElement()).size() - 1;
    if (anz > 0) {
	  Graphics da = this.getGraphics();
	  Rectangle size = this.bounds();
	  Rectangle Diagram = new Rectangle(40, 5, size.width - 45,
										size.height - 25);
	  da.translate(Diagram.x, Diagram.y);
	  da.setColor(Color.black);
	  double sx = ((double) Diagram.width) / ScaleMaxX;
	  double sy = ((double) Diagram.height) / ScaleMaxY;
	  Double lastValue = (Double)
		(((Vector)data.lastElement()).elementAt(anz - 1));
	  int ox = (int)(sx * (anz - 1));
	  int oy = Diagram.height - (int)(sy * lastValue.doubleValue());
	  int nx = (int)(sx * anz);
	  int ny = Diagram.height - (int)(sy * value);
	
	  da.drawLine(ox, oy, nx, ny);
	}
  }  

  /**
   * Initialize the graph.
   */
  public void startNewTrace() {
    data.addElement(new Vector(100, 20));
    paint(this.getGraphics());
  }

  /*
   * Clear the graph.
   */
  public void clear() {
    ScaleMaxX = INIT_MAX_X;
    ScaleMaxY = INIT_MAX_Y;
    data = new Vector(1, 1);
    data.addElement( new Vector(100, 20) );
    paint(this.getGraphics());
  }

}
