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
 * A class implementing the error graph.
 *
 */
public class GraphGNG extends Frame {
  DemoGNG demo;
  SelGraphics graph;
  TextField error;

  /**
   * The name of the clear button.
   */
  protected final static String CLEAR = "Clear";
  /**
   * The name of the close button.
   */
  protected final static String CLOSE = "Close";

  GraphGNG(DemoGNG demo) {
	this.demo = demo;
	graph = new SelGraphics();
	setTitle("ErrorGraph");

	setLayout(new BorderLayout());
	add("North",new Label("  Error Graph"));
	add("Center",graph);
	Panel pSouth = new Panel();
	pSouth.add(new Button(CLEAR));
	pSouth.add(new Button(CLOSE));
	add("South", pSouth);
	pack();
  }

  public boolean handleEvent(Event evt) {
	if (CLEAR.equals(evt.arg)) {
	  graph.clear();
	  return true;
	} else if (CLOSE.equals(evt.arg)) {
	  demo.graphClose();
	  return true;
	}
	return super.handleEvent(evt);
  }

}
