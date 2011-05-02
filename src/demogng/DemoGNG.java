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
//     Version 1.5 (19.10.1998) ==> DGNG_VERSION (computeGNG.java)            ;
//                                                                            ;
// ========================================================================== ;
//                                                                            ;
//        Possible values for the <param>-tag:                                ;
//         algorithm:                                                         ;
//            - nothing                        => start applet with GNG       ;
//            - name=algorithm value="GG"     \                               ;
//            - name=algorithm value="GNG"     \                              ;
//            - name=algorithm value="GNG-U"    \                             ;
//            - name=algorithm value="HCL"       \  Start applet with         ;
//            - name=algorithm value="NG"         > algorithm named           ;
//            - name=algorithm value="NGwCHL"    /  in value.                 ;
//            - name=algorithm value="CHL"      /                             ;
//            - name=algorithm value="LBG"     /                              ;
//            - name=algorithm value="LBG-U"  /                               ;
//            - name=algorithm value="SOM"   /                                ;
//         distribution:                                                      ;
//            - nothing                                  => Rectangle         ;
//            - name=distribution value="Discrete"      \                     ;
//            - name=distribution value="Rectangle"      \                    ;
//            - name=distribution value="Circle"          \                   ;
//            - name=distribution value="Ring"             \                  ;
//            - name=distribution value="UNI"               \   Start with    ;
//            - name=distribution value="Small Spirals"      \  distribution  ;
//            - name=distribution value="Large Spirals"      /  named in      ;
//            - name=distribution value="HiLo Density"      /   value.        ;
//            - name=distribution value="UNIT"             /                  ;
//            - name=distribution value="Move & Jump"     /                   ;
//            - name=distribution value="Move"           /                    ;
//            - name=distribution value="Jump"          /                     ;
//            - name=distribution value="Right MouseB" /                      ;
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

import java.applet.*;
import java.awt.*;

/**
 * A class drawing the GUI and interact with the user.
 *
 */
public class DemoGNG extends Applet {

  final static int GNG_C    = 5;
  final static int HCL_C    = 1;
  final static int NG_C     = 2;
  final static int NGwCHL_C = 4;
  final static int CHL_C    = 3;
  final static int LBG_C    = 0;
  final static int GG_C     = 7;
  final static int SOM_C    = 6;

  /**
   * The name of the first algorithm.
   */
  protected final static String ALGO_0 = "Growing Neural Gas / GNG-U";
  /**
   * The abbreviation of the first algorithm.
   */
  protected final static String ALGO_ABBREV_0 = "GNG";
  /**
   * The abbreviation of the utility version of the first algorithm.
   */
  protected final static String ALGO_ABBREV_0_U = "GNG-U";
  /**
   * The name of the second algorithm.
   */
  protected final static String ALGO_1 = "Hard Competitive Learning";
  /**
   * The abbreviation of the second algorithm.
   */
  protected final static String ALGO_ABBREV_1 = "HCL";
  /**
   * The name of the third algorithm.
   */
  protected final static String ALGO_2 = "Neural Gas";
  /**
   * The abbreviation of the third algorithm.
   */
  protected final static String ALGO_ABBREV_2 = "NG";
  /**
   * The name of the fourth algorithm.
   */
  protected final static String ALGO_3 = "Neural Gas with CHL";
  /**
   * The abbreviation of the fourth algorithm.
   */
  protected final static String ALGO_ABBREV_3 = "NGwCHL";
  /**
   * The name of the fifth algorithm.
   */
  protected final static String ALGO_4 = "Competitive Hebbian Learning";
  /**
   * The abbreviation of the fifth algorithm.
   */
  protected final static String ALGO_ABBREV_4 = "CHL";
  /**
   * The name of the sixth algorithm.
   */
  protected final static String ALGO_5 = "LBG / LBG-U";
  /**
   * The abbreviation of the sixth algorithm.
   */
  protected final static String ALGO_ABBREV_5 = "LBG";
  /**
   * The abbreviation of the utility version of the sixth algorithm.
   */
  protected final static String ALGO_ABBREV_5_U = "LBG-U";
  /**
   * The name of the seventh algorithm.
   */
  protected final static String ALGO_6 = "Growing Grid";
  /**
   * The abbreviation of the seventh algorithm.
   */
  protected final static String ALGO_ABBREV_6 = "GG";
  /**
   * The name of the eighth algorithm.
   */
  protected final static String ALGO_7 = "Self-Organizing Map";
  /**
   * The abbreviation of the eighth algorithm.
   */
  protected final static String ALGO_ABBREV_7 = "SOM";
  /**
   * The name of the first distribution.
   */
  protected final static String DISTRIB_0 = "Rectangle";
  /**
   * The name of the second distribution.
   */
  protected final static String DISTRIB_1 = "Circle";
  /**
   * The name of the third distribution.
   */
  protected final static String DISTRIB_2 = "Ring";
  /**
   * The name of the fourth distribution.
   */
  protected final static String DISTRIB_3 = "UNI";
  /**
   * The name of the fifth distribution.
   */
  protected final static String DISTRIB_4 = "Small Spirals";
  /**
   * The name of the sixth distribution.
   */
  protected final static String DISTRIB_5 = "Large Spirals";
  /**
   * The name of the seventh distribution.
   */
  protected final static String DISTRIB_6 = "HiLo Density";
  /**
   * The name of the eighth distribution.
   */
  protected final static String DISTRIB_7 = "Discrete";
  /**
   * The name of the ninth distribution.
   */
  protected final static String DISTRIB_8 = "UNIT";
  /**
   * The name of the tenth distribution.
   */
  protected final static String DISTRIB_9 = "Move & Jump";
  /**
   * The name of the eleventh distribution.
   */
  protected final static String DISTRIB_10 = "Move";
  /**
   * The name of the twelfth distribution.
   */
  protected final static String DISTRIB_11 = "Jump";
  /**
   * The name of the 13th distribution.
   */
  protected final static String DISTRIB_12 = "Right MouseB";
  /**
   * The name of the start button.
   */
  protected final static String BUTTON_0 = "Start";
  /**
   * The name of the stop button.
   */
  protected final static String BUTTON_1 = "Stop";
  /**
   * The name of the reset button.
   */
  protected final static String BUTTON_3 = "Reset";
  /**
   * The name of the signal checkbox.
   */
  protected final static String SIGNALS    	= " Signals";
  /**
   * The name of the no_new_nodes checkbox.
   */
  protected final static String NO_NEW_NODES 	= " No new Nodes";
  /**
   * The name of the utility checkbox.
   */
  protected final static String UTILITY_GNG 	= " Utility =";
  /**
   * The name of the LBG-U checkbox.
   */
  protected final static String LBG_U 	= " LBG-U";
  /**
   * The name of the sound checkbox.
   */
  protected final static String SOUND      	= " Sound";
  /**
   * The name of the hardcopy checkbox.
   */
  protected final static String WHITE      	= " White";
  /**
   * The name of the random-init checkbox.
   */
  protected final static String RNDINIT    	= " Random Init";
  /**
   * The name of the teach checkbox.
   */
  protected final static String TEACH      	= " Teach";
  /**
   * The name of the variable checkbox (HCL).
   */
  protected final static String VARIABLE   	= " Variable";
  /**
   * The name of the edges checkbox.
   */
  protected final static String EDGES   	= " Edges";
  /**
   * The name of the nodes checkbox.
   */
  protected final static String NODES   	= " Nodes";
  /**
   * The name of the error graph checkbox.
   */
  protected final static String ERRORGRAPH   = " Error Graph";
  /**
   * The name of the Voronoi checkbox.
   */
  protected final static String VORONOI   	= " Voronoi";
  /**
   * The name of the Delaunay checkbox.
   */
  protected final static String DELAUNAY   	= " Delaunay";
  ComputeGNG compute;
  Panel cards;
  Panel p11;
  Panel p71;
  Label epsilonHCL_lbl;
  Label epsiloniHCL_lbl;
  Label epsilonfHCL_lbl;
  Label tmaxHCL_lbl;
  Label nodes_lbl;
  Checkbox noNodes_cb;
  Checkbox utilityGNG_cb;
  Checkbox LBG_U_cb;
  Checkbox variable_cb;
  Checkbox errorGraph_cb;
  Choice algo_choice;
  Choice distrib_choice;
  Choice stepSize_choice;
  Choice speed_choice;
  Choice nodes_choice;
  Choice newNodeGNG_choice;
  Choice delEdgeGNG_choice;
  Choice epsilonGNG1_choice;
  Choice epsilonGNG2_choice;
  Choice alphaGNG_choice;
  Choice betaGNG_choice;
  Choice utilityGNG_choice;
  Choice epsilonHCL_choice;
  Choice epsiloniHCL_choice;
  Choice epsilonfHCL_choice;
  Choice tmaxHCL_choice;
  Choice lambdaiNG_choice;
  Choice lambdafNG_choice;
  Choice epsiloniNG_choice;
  Choice epsilonfNG_choice;
  Choice tmaxNG_choice;
  Choice lambdaiCHL_choice;
  Choice lambdafCHL_choice;
  Choice epsiloniCHL_choice;
  Choice epsilonfCHL_choice;
  Choice tmaxCHL_choice;
  Choice edgeiCHL_choice;
  Choice edgefCHL_choice;
  Choice discreteNumSignalsLBG_choice;
  Choice lambdagGG_choice;
  Choice lambdafGG_choice;
  Choice epsiloniGG_choice;
  Choice epsilonfGG_choice;
  Choice sigmaGG_choice;
  Choice epsiloniSOM_choice;
  Choice epsilonfSOM_choice;
  Choice sigmaiSOM_choice;
  Choice sigmafSOM_choice;
  Choice tmaxSOM_choice;
  Choice sizeSOM_choice;
  int sizeSOM_index = 0;
  Button start_b;
  Button stop_b;
  Button restart_b;
  /**
   * The array for the stepsize.
   *  To add or delete values to the choice, only this array must be changed.
   */
  protected int stepSize_Ai[] = {50, 1, 2, 5, 10, 20, 40,
								 80, 100, 150, compute.MAX_STEPSIZE};
  /**
   * The array for the machine speed.
   *  To add or delete values to the choice, change this array and speed_As[].
   */
  protected int speed_Ai[] = {150, 10, 20, 50, 100, 200, 300, 400};
  /**
   * The array for the machine speed names.
   *  To add or delete values to the choice, change this array and speed_Ai.
   */
  protected String speed_As[] = {"Normal", "Lightning", "Very fast", "Fast",
								 "Medium fast", "Medium slow", "Slow",
								 "Very slow"};
  /**
   * The array for the maximum number of nodes.
   *  To add or delete values to the choice, only this array must be changed.
   */
  protected int nodes_Ai[] = {100, 1, 2, 3, 4, 5, 10, 20, 50, 150,
							  200, compute.MAX_NODES};
  /**
   * The array for the maximum age of an edge.
   *  To add or delete values to the choice, only this array must be changed.
   */
  protected int delEdgeGNG_Ai[] = {88, 10, 20, 50, 100, 200, 400, 800};
  /**
   * The array for the number of runs to insert a new node.
   *  To add or delete values to the choice, only this array must be changed.
   */
  protected int newNodeGNG_Ai[] = {600, 10, 50, 100, 200, 300,
								   400, 500, 800, 1000, 2000};
  /**
   * The array for the value epsiolon initial of the HCL algorithms.
   *  To add or delete values to the choice, only this array must be changed.
   */
  protected float epsiloniHCL_Af[] = {0.1f, 0.05f, 0.2f, 0.3f, 0.4f, 0.5f,
									  0.6f, 0.7f, 0.8f, 0.9f, 1.0f};
  /**
   * The array for the value epsiolon final of the HCL algorithms.
   *  To add or delete values to the choice, only this array must be changed.
   */
  protected float epsilonfHCL_Af[] = {0.005f, 0.0001f, 0.001f, 0.008f, 0.01f,
									  0.05f, 0.1f, 0.2f, 0.5f, 1.0f};
  /**
   * The array for the value t_max of the HCL algorithms.
   *  To add or delete values to the choice, only this array must be changed.
   */
  protected float tmaxHCL_Af[] = {20000, 1000, 5000, 10000, 30000, 40000};
  /**
   * The array for the value epsilon of the HCL algorithm.
   *  To add or delete values to the choice, only this array must be changed.
   */
  protected float epsilonHCL_Af[] = {0.1f, 0.05f, 0.2f, 0.3f, 0.4f, 0.5f,
								 0.6f, 0.7f, 0.8f, 0.9f, 1.0f};
  /**
   * The array for the value epsilon of the GNG algorithm (winner).
   *  To add or delete values to the choice, only this array must be changed.
   */
  protected float epsilonGNG1_Af[] = {0.05f, 0.0f, 0.001f, 0.005f, 0.01f,
								  0.1f, 0.2f, 0.5f, 1.0f};
  /**
   * The array for the value epsilon of the GNG algorithm (second).
   *  To add or delete values to the choice, only this array must be changed.
   */
  protected float epsilonGNG2_Af[] = {0.0006f, 0.0f, 0.0001f, 0.001f, 0.005f,
									  0.01f, 0.05f, 0.1f, 0.15f, 0.2f};
  /**
   * The array for the value alpha of the GNG algorithm.
   *  To add or delete values to the choice, only this array must be changed.
   */
  protected float alphaGNG_Af[] = {0.5f, 0.0f, 0.2f, 0.4f, 0.6f, 0.8f, 1.0f};
  /**
   * The array for the value beta of the GNG algorithm.
   *  To add or delete values to the choice, only this array must be changed.
   */
  protected float betaGNG_Af[] = {0.0005f, 0.0f, 0.00001f, 0.00005f, 0.0001f,
								  0.001f, 0.005f, 0.01f, 0.05f, 0.1f, 0.5f,
								  1.0f};
  /**
   * The array for the utility factor of the GNG-U algorithm.
   *  To add or delete values to the choice, only this array must be changed.
   */
  protected float utilityGNG_Af[] =  {3.0f, 1.0f, 1.5f, 2.0f, 2.5f,
									  3.5f, 4.0f, 4.5f, 5.0f, 5.5f, 6.0f, 6.5f,
									  7.0f, 7.5f, 8.0f};
  /**
   * The array for the value lambda initial of the NG algorithms.
   *  To add or delete values to the choice, only this array must be changed.
   */
  protected float lambdaiNG_Af[] = {30, 10, 20, 40, 60, 80, 100};
  /**
   * The array for the value lambda final of the NG algorithms.
   *  To add or delete values to the choice, only this array must be changed.
   */
  protected float lambdafNG_Af[] = {0.01f, 0.0f, 0.001f, 0.005f, 0.05f,
									0.1f, 0.2f, 0.5f, 1.0f};
  /**
   * The array for the value epsiolon initial of the NG algorithms.
   *  To add or delete values to the choice, only this array must be changed.
   */
  protected float epsiloniNG_Af[] = {0.3f, 0.1f, 0.2f, 0.4f, 0.5f, 0.6f,
									 0.7f, 0.8f, 0.9f, 1.0f};
  /**
   * The array for the value epsiolon final of the NG algorithms.
   *  To add or delete values to the choice, only this array must be changed.
   */
  protected float epsilonfNG_Af[] = {0.05f, 0.0f, 0.001f, 0.005f, 0.01f,
									 0.05f, 0.1f, 0.2f, 0.5f, 1.0f};
  /**
   * The array for the value t_max of the NG algorithms.
   *  To add or delete values to the choice, only this array must be changed.
   */
  protected float tmaxNG_Af[] = {40000, 1000, 5000, 10000, 20000, 30000};
  /**
   * The array for the value lambda initial of the NGwCHL algorithms.
   *  To add or delete values to the choice, only this array must be changed.
   */
  protected float lambdaiCHL_Af[] = {30, 10, 20, 40, 60, 80, 100};
  /**
   * The array for the value lambda final of the NGwCHL algorithms.
   *  To add or delete values to the choice, only this array must be changed.
   */
  protected float lambdafCHL_Af[] = {0.01f, 0.0f, 0.001f, 0.005f, 0.05f,
									 0.1f, 0.2f, 0.5f, 1.0f};
  /**
   * The array for the value epsiolon initial of the NGwCHL algorithms.
   *  To add or delete values to the choice, only this array must be changed.
   */
  protected float epsiloniCHL_Af[] ={0.3f, 0.1f, 0.2f, 0.4f, 0.5f, 0.6f, 0.7f,
									 0.8f, 0.9f, 1.0f};
  /**
   * The array for the value epsiolon final of the NGwCHL algorithms.
   *  To add or delete values to the choice, only this array must be changed.
   */
  protected float epsilonfCHL_Af[] = {0.05f, 0.0f, 0.001f, 0.005f, 0.01f,
									  0.05f, 0.1f, 0.2f, 0.5f, 1.0f};
  /**
   * The array for the value t_max of the NGwCHL algorithms.
   *  To add or delete values to the choice, only this array must be changed.
   */
  protected float tmaxCHL_Af[] = {40000, 1000, 5000, 10000, 20000, 30000};
  /**
   * The array for the value delete edge initial of the NGwCHL algorithm.
   *  To add or delete values to the choice, only this array must be changed.
   */
  protected int edgeiCHL_Ai[] = {20, 10, 30, 40, 50, 60, 70, 80, 90, 100};
  /**
   * The array for the value delete edge final of the NGwCHL algorithm.
   *  To add or delete values to the choice, only this array must be changed.
   */
  protected int edgefCHL_Ai[] = {200, 100, 120, 140, 180, 250, 300, 400, 500};
  /**
   * The array for the number of discrete signals.
   *  To add or delete values to the choice, only this array must be changed.
   */
  protected int discreteNumSignalsLBG_Ai[] = {500, 3, 6, 9, 12, 15, 18,
											  30, 60, 90, 100,
											  120, 150, 180, 200, 210,
											  240, 270, 300, 330, 360,
											  390, 400, 420, 450, 480};
  /**
   * The array for the value lambda growing of the GG algorithms.
   *  To add or delete values to the choice, only this array must be changed.
   */
  protected float lambdagGG_Af[] = {30, 10, 20, 40, 60, 80, 100};
  /**
   * The array for the value lambda fine tuning of the GG algorithms.
   *  To add or delete values to the choice, only this array must be changed.
   */
  protected float lambdafGG_Af[] = {100, 60, 120, 150, 200};
  /**
   * The array for the value epsiolon initial of the GG algorithms.
   *  To add or delete values to the choice, only this array must be changed.
   */
  protected float epsiloniGG_Af[] = {0.1f, 0.05f, 0.2f, 0.3f, 0.4f, 0.5f,
									 0.6f, 0.7f, 0.8f, 0.9f, 1.0f};
  /**
   * The array for the value epsiolon final of the GG algorithms.
   *  To add or delete values to the choice, only this array must be changed.
   */
  protected float epsilonfGG_Af[] = {0.005f, 0.0001f, 0.001f, 0.008f, 0.01f,
									 0.05f, 0.1f, 0.2f, 0.5f, 1.0f};
  /**
   * The array for the value sigma of the GG algorithms.
   *  To add or delete values to the choice, only this array must be changed.
   */
  protected float sigmaGG_Af[] = {0.9f, 0.1f, 0.2f, 0.3f, 0.4f, 0.5f,
								  0.6f, 0.7f, 0.8f, 1.0f, 1.2f, 1.4f,
								  1.6f, 1.8f, 2.0f};
  /**
   * The array for the value epsiolon initial of the SOM algorithms.
   *  To add or delete values to the choice, only this array must be changed.
   */
  protected float epsiloniSOM_Af[] = {0.1f, 0.05f, 0.2f, 0.3f, 0.4f, 0.5f,
									  0.6f, 0.7f, 0.8f, 0.9f, 1.0f};
  /**
   * The array for the value epsiolon final of the SOM algorithms.
   *  To add or delete values to the choice, only this array must be changed.
   */
  protected float epsilonfSOM_Af[] = {0.005f, 0.0001f, 0.001f, 0.008f, 0.01f,
									  0.05f, 0.1f, 0.2f, 0.5f, 1.0f};
  /**
   * The array for the value sigma of the SOM algorithms.
   *  To add or delete values to the choice, only this array must be changed.
   */
  protected float sigmaiSOM_Af[] = {5.0f, 1.0f, 1.5f, 2.0f, 2.5f, 3.0f,
									3.5f, 4.0f, 4.5f, 5.5f, 6.0f, 6.5f,
									7.0f, 7.5f, 8.0f};
  /**
   * The array for the value sigma of the SOM algorithms.
   *  To add or delete values to the choice, only this array must be changed.
   */
  protected float sigmafSOM_Af[] = {0.2f, 0.01f, 0.05f, 0.1f, 0.3f, 0.4f,
									0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f};
  /**
   * The array for the value t_max of the SOM algorithm.
   *  To add or delete values to the choice, only this array must be changed.
   */
  protected float tmaxSOM_Af[] = {40000, 1000, 5000, 10000, 20000, 30000};
  /**
   * The array for the grid size of the SOM algorithm.
   *  To add or delete values to the choice, change this array and
   *  sizeSOM_As[].
   */
  protected int sizeSOM_Ai[][] = {{10, 10}, {5, 5}, {30, 1}, {10, 5}, {5, 10},
								  {30, 2}, {30, 3}, {30, 4},
								  {15, 10}, {10, 15}, {20, 10}, {10, 20},
								  {15, 15}, {19, 13}, {13, 19}};
  /**
   * The array for the grid size of the SOM algorithm.
   *  To add or delete values to the choice, change this array and sizeSOM_Ai.
   */
  protected String sizeSOM_As[] = {"10x10", "5x5", "1x30", "5x10", "10x5",
								   "2x30", "3x30", "4x30",
								   "10x15", "15x10", "10x20", "20x10",
								   "15x15", "13x19", "19x13"};

  public void init() {
	int i;
    Font font = getFont();
    Font boldFont = new Font(font.getName(), Font.BOLD, font.getSize());

    // Set the layout-style
    setLayout(new BorderLayout());

    // Create the GNG-Panel and center it
    compute = new ComputeGNG(this);
    add("Center", compute);

    // Create a Panel for the Buttons

    // Put the Choice in a Panel to get a nicer look.
    Panel cp_distrib = new Panel();
    Panel cp_stepSize = new Panel();
    Panel cp_speed = new Panel();
    Panel cp_nodes = new Panel();

    Panel cp_algo = new Panel();
    // Create a menu of algorithms and add it to the Panel.
    algo_choice = new Choice();
    algo_choice.setFont(boldFont);
    algo_choice.addItem(ALGO_5);
    algo_choice.addItem(ALGO_1);
    algo_choice.addItem(ALGO_2);
    algo_choice.addItem(ALGO_4);
    algo_choice.addItem(ALGO_3);
    algo_choice.addItem(ALGO_0);
    algo_choice.addItem(ALGO_7);
    algo_choice.addItem(ALGO_6);
    cp_algo.add(new Label("Network Model:", Label.RIGHT));
    cp_algo.add(algo_choice);
    add("North", cp_algo);

    // Create new panel, set the layout-style and add it to the Panel
    Panel pSouth = new Panel();
    pSouth.setLayout(new BorderLayout());
    add("South", pSouth);

    Panel pAll = new Panel();
	pAll.setLayout(new BorderLayout());
    Panel pAllN = new Panel();
    Panel pAllS = new Panel();
    start_b = new Button(BUTTON_0);
    stop_b = new Button(BUTTON_1);
    restart_b = new Button(BUTTON_3);
    start_b.disable();
    restart_b.disable();

    pAllN.add(start_b);
    pAllN.add(stop_b);
    pAllN.add(restart_b);
    pAllN.add(new Checkbox(TEACH));
    pAllN.add(new Checkbox(SIGNALS));
    pAllN.add(new Checkbox(VORONOI));
    pAllN.add(new Checkbox(DELAUNAY));

	errorGraph_cb = new Checkbox(ERRORGRAPH, null, compute.errorGraphB);

    pAllS.add(errorGraph_cb);
    pAllS.add(new Checkbox(NODES, null, compute.nodesB));
    pAllS.add(new Checkbox(EDGES, null, compute.edgesB));
    pAllS.add(new Checkbox(RNDINIT, null, compute.rndInitB));
    pAllS.add(new Checkbox(WHITE, null, compute.whiteB));
    pAllS.add(new Checkbox(SOUND, null, compute.soundB));
    pAll.add("North", pAllN);
    pAll.add("South", pAllS);
    pSouth.add("North", pAll);

    Panel pDS = new Panel();

    // Create a menu of distributions and add it to the Panel.
    distrib_choice = new Choice();
    distrib_choice.addItem(DISTRIB_0);
    distrib_choice.addItem(DISTRIB_1);
    distrib_choice.addItem(DISTRIB_2);
    distrib_choice.addItem(DISTRIB_3);
    distrib_choice.addItem(DISTRIB_4);
    distrib_choice.addItem(DISTRIB_5);
    distrib_choice.addItem(DISTRIB_6);
    distrib_choice.addItem(DISTRIB_7);
    distrib_choice.addItem(DISTRIB_8);
    distrib_choice.addItem(DISTRIB_9);
    distrib_choice.addItem(DISTRIB_10);
    distrib_choice.addItem(DISTRIB_11);
    distrib_choice.addItem(DISTRIB_12);
    cp_distrib.add(new Label("prob. Distrib.:", Label.RIGHT));
    cp_distrib.add(distrib_choice);
    pDS.add(cp_distrib);

    // Create a menu of node-numbers and add it to the Panel.
    nodes_choice = new Choice();
    for (i = 0; i < nodes_Ai.length; i++)
      nodes_choice.addItem(String.valueOf(nodes_Ai[i]));

    nodes_lbl = new Label("max. Nodes:", Label.RIGHT);
    cp_nodes.add(nodes_lbl);
    cp_nodes.add(nodes_choice);
    pDS.add(cp_nodes);

    // Create a menu of step sizes and add it to the Panel.
    stepSize_choice = new Choice();
    for (i = 0; i < stepSize_Ai.length; i++)
      stepSize_choice.addItem(String.valueOf(stepSize_Ai[i]));

    cp_stepSize.add(new Label("Display:", Label.RIGHT));
    cp_stepSize.add(stepSize_choice);
    pDS.add(cp_stepSize);
    pSouth.add("Center", pDS);

    // Create a menu of machine speeds and add it to the Panel.
    speed_choice = new Choice();
    for (i = 0; i < speed_As.length; i++)
      speed_choice.addItem(speed_As[i]);

    cp_speed.add(new Label("Speed:", Label.RIGHT));
    cp_speed.add(speed_choice);
    pDS.add(cp_speed);
    pSouth.add("Center", pDS);

    cards = new Panel();
    cards.setLayout(new CardLayout());
        
    Panel p1 = new Panel();
    p1.setLayout(new BorderLayout());

    Panel p10 = new Panel();
    noNodes_cb = new Checkbox(NO_NEW_NODES);
    p10.add(noNodes_cb);
    utilityGNG_cb = new Checkbox(UTILITY_GNG, null, compute.utilityGNGB);
    p10.add(utilityGNG_cb);
    utilityGNG_choice = new Choice();
    for (i = 0; i < utilityGNG_Af.length; i++)
      utilityGNG_choice.addItem(String.valueOf(utilityGNG_Af[i]));
    p10.add(utilityGNG_choice);

    p11 = new Panel();
    p11.setLayout(new GridLayout(2,6));
    newNodeGNG_choice = new Choice();
    for (i = 0; i < newNodeGNG_Ai.length; i++)
      newNodeGNG_choice.addItem(String.valueOf(newNodeGNG_Ai[i]));

    delEdgeGNG_choice = new Choice();
    for (i = 0; i < delEdgeGNG_Ai.length; i++)
      delEdgeGNG_choice.addItem(String.valueOf(delEdgeGNG_Ai[i]));

    epsilonGNG1_choice = new Choice();
    for (i = 0; i < epsilonGNG1_Af.length; i++)
      epsilonGNG1_choice.addItem(String.valueOf(epsilonGNG1_Af[i]));

    epsilonGNG2_choice = new Choice();
    for (i = 0; i < epsilonGNG2_Af.length; i++)
      epsilonGNG2_choice.addItem(String.valueOf(epsilonGNG2_Af[i]));

    alphaGNG_choice = new Choice();
    for (i = 0; i < alphaGNG_Af.length; i++)
      alphaGNG_choice.addItem(String.valueOf(alphaGNG_Af[i]));

    betaGNG_choice = new Choice();
    for (i = 0; i < betaGNG_Af.length; i++)
      betaGNG_choice.addItem(String.valueOf(betaGNG_Af[i]));

    p11.add(new Label("Lambda"));
    p11.add(new Label("max. Edge Age"));
    p11.add(new Label("Epsilon winner"));
    p11.add(new Label("Epsilon neighbor"));
    p11.add(new Label("   alpha"));
    p11.add(new Label("beta"));
    p11.add(newNodeGNG_choice);
    p11.add(delEdgeGNG_choice);
    p11.add(epsilonGNG1_choice);
    p11.add(epsilonGNG2_choice);
    p11.add(alphaGNG_choice);
    p11.add(betaGNG_choice);

    p1.add("North", p10);
    p1.add("Center", p11);
    p1.add("East", new Panel().add(new Label("     ")));
    p1.add("West", new Panel().add(new Label("     ")));


    Panel p2 = new Panel();
    // Create a menu of epsilon sizes
    epsilonHCL_choice = new Choice();
    for (i = 0; i < epsilonHCL_Af.length; i++)
      epsilonHCL_choice.addItem(String.valueOf(epsilonHCL_Af[i]));
    epsiloniHCL_choice = new Choice();
    for (i = 0; i < epsiloniHCL_Af.length; i++)
      epsiloniHCL_choice.addItem(String.valueOf(epsiloniHCL_Af[i]));
    epsilonfHCL_choice = new Choice();
    for (i = 0; i < epsilonfHCL_Af.length; i++)
      epsilonfHCL_choice.addItem(String.valueOf(epsilonfHCL_Af[i]));
    tmaxHCL_choice = new Choice();
    for (i = 0; i < tmaxHCL_Af.length; i++)
      tmaxHCL_choice.addItem(String.valueOf(tmaxHCL_Af[i]));

    // Create two labels
    epsilonHCL_lbl = new Label("epsilon =", Label.RIGHT);
    epsiloniHCL_lbl = new Label("epsilon_i =", Label.RIGHT);
    epsilonfHCL_lbl = new Label("epsilon_f =", Label.RIGHT);
    tmaxHCL_lbl = new Label("t_max =", Label.RIGHT);

    p2.add(new Checkbox(VARIABLE, null, compute.variableB));
    p2.add(epsilonHCL_lbl);
    p2.add(epsilonHCL_choice);

    p2.add(epsiloniHCL_lbl);
    p2.add(epsiloniHCL_choice);
    p2.add(epsilonfHCL_lbl);
    p2.add(epsilonfHCL_choice);
    p2.add(tmaxHCL_lbl);
    p2.add(tmaxHCL_choice);

    if (!compute.variableB) {
	  epsiloniHCL_lbl.disable();
	  epsilonfHCL_lbl.disable();
	  tmaxHCL_lbl.disable();

	  epsiloniHCL_choice.disable();
	  epsilonfHCL_choice.disable();
	  tmaxHCL_choice.disable();
    }

    Panel p3 = new Panel();
    p3.setLayout(new BorderLayout());

    Panel p30 = new Panel();
    p30.add(new Panel().add(new Label("     ")));
    Panel p31 = new Panel();
    p31.setLayout(new GridLayout(2,5));
    lambdaiNG_choice = new Choice();
    for (i = 0; i < lambdaiNG_Af.length; i++)
      lambdaiNG_choice.addItem(String.valueOf(lambdaiNG_Af[i]));
    lambdafNG_choice = new Choice();
    for (i = 0; i < lambdafNG_Af.length; i++)
      lambdafNG_choice.addItem(String.valueOf(lambdafNG_Af[i]));
    epsiloniNG_choice = new Choice();
    for (i = 0; i < epsiloniNG_Af.length; i++)
      epsiloniNG_choice.addItem(String.valueOf(epsiloniNG_Af[i]));
    epsilonfNG_choice = new Choice();
    for (i = 0; i < epsilonfNG_Af.length; i++)
      epsilonfNG_choice.addItem(String.valueOf(epsilonfNG_Af[i]));
    tmaxNG_choice = new Choice();
    for (i = 0; i < tmaxNG_Af.length; i++)
      tmaxNG_choice.addItem(String.valueOf(tmaxNG_Af[i]));

    p31.add(new Label("lambda_i"));
    p31.add(new Label("lambda_f"));
    p31.add(new Label("epsilon_i"));
    p31.add(new Label("epsilon_f"));
    p31.add(new Label("t_max"));
    p31.add(lambdaiNG_choice);
    p31.add(lambdafNG_choice);
    p31.add(epsiloniNG_choice);
    p31.add(epsilonfNG_choice);
    p31.add(tmaxNG_choice);

    p3.add("Center", p31);
    p3.add("East", new Panel().add(new Label("     ")));
    p3.add("West", new Panel().add(new Label("     ")));
    p3.add("South", p30);


    Panel p4 = new Panel();

    p4.setLayout(new BorderLayout());

    Panel p40 = new Panel();
    p40.add(new Panel().add(new Label("     ")));
    Panel p41 = new Panel();
    p41.setLayout(new GridLayout(2,7));
    lambdaiCHL_choice = new Choice();
    for (i = 0; i < lambdaiCHL_Af.length; i++)
      lambdaiCHL_choice.addItem(String.valueOf(lambdaiCHL_Af[i]));
    lambdafCHL_choice = new Choice();
    for (i = 0; i < lambdafCHL_Af.length; i++)
      lambdafCHL_choice.addItem(String.valueOf(lambdafCHL_Af[i]));
    epsiloniCHL_choice = new Choice();
    for (i = 0; i < epsiloniCHL_Af.length; i++)
      epsiloniCHL_choice.addItem(String.valueOf(epsiloniCHL_Af[i]));
    epsilonfCHL_choice = new Choice();
    for (i = 0; i < epsilonfCHL_Af.length; i++)
      epsilonfCHL_choice.addItem(String.valueOf(epsilonfCHL_Af[i]));
    tmaxCHL_choice = new Choice();
    for (i = 0; i < tmaxCHL_Af.length; i++)
      tmaxCHL_choice.addItem(String.valueOf(tmaxCHL_Af[i]));
    edgeiCHL_choice = new Choice();
    for (i = 0; i < edgeiCHL_Ai.length; i++)
      edgeiCHL_choice.addItem(String.valueOf(edgeiCHL_Ai[i]));
    edgefCHL_choice = new Choice();
    for (i = 0; i < edgefCHL_Ai.length; i++)
      edgefCHL_choice.addItem(String.valueOf(edgefCHL_Ai[i]));

    p41.add(new Label("lambda_i"));
    p41.add(new Label("lambda_f"));
    p41.add(new Label("epsilon_i"));
    p41.add(new Label("epsilon_f"));
    p41.add(new Label("t_max"));
    p41.add(new Label("edge_i"));
    p41.add(new Label("edge_f"));
    p41.add(lambdaiCHL_choice);
    p41.add(lambdafCHL_choice);
    p41.add(epsiloniCHL_choice);
    p41.add(epsilonfCHL_choice);
    p41.add(tmaxCHL_choice);
    p41.add(edgeiCHL_choice);
    p41.add(edgefCHL_choice);

    p4.add("Center", p41);
    p4.add("East", new Panel().add(new Label("     ")));
    p4.add("West", new Panel().add(new Label("     ")));
    p4.add("South", p40);

    Panel p5 = new Panel();

    p5.setLayout(new BorderLayout());

    Panel p6 = new Panel();

    p6.setLayout(new BorderLayout());

    Panel p60 = new Panel();
    p60.add(new Panel().add(new Label("     ")));
    LBG_U_cb = new Checkbox(LBG_U, null, compute.LBG_U_B);
    p60.add(LBG_U_cb);
    Panel p61 = new Panel();
    p61.setLayout(new GridLayout(2,1));
    discreteNumSignalsLBG_choice = new Choice();
    for (i = 0; i < discreteNumSignalsLBG_Ai.length; i++)
      discreteNumSignalsLBG_choice.addItem(String.valueOf(discreteNumSignalsLBG_Ai[i]));

    p61.add(new Label("Number of Signals"));
    p61.add(discreteNumSignalsLBG_choice);

    p6.add("North", p60);
    p6.add("Center", p61);
    p6.add("East", new Panel().add(new Label("                     ")));
    p6.add("West", new Panel().add(new Label("                     ")));

    Panel p7 = new Panel();
    p7.setLayout(new BorderLayout());

    Panel p70 = new Panel();
    noNodes_cb = new Checkbox(NO_NEW_NODES);
    p70.add(noNodes_cb);

    Panel p71 = new Panel();
    p71.setLayout(new GridLayout(2,5));
    lambdagGG_choice = new Choice();
    for (i = 0; i < lambdagGG_Af.length; i++)
      lambdagGG_choice.addItem(String.valueOf(lambdagGG_Af[i]));
    lambdafGG_choice = new Choice();
    for (i = 0; i < lambdafGG_Af.length; i++)
      lambdafGG_choice.addItem(String.valueOf(lambdafGG_Af[i]));
    epsiloniGG_choice = new Choice();
    for (i = 0; i < epsiloniGG_Af.length; i++)
      epsiloniGG_choice.addItem(String.valueOf(epsiloniGG_Af[i]));
    epsilonfGG_choice = new Choice();
    for (i = 0; i < epsilonfGG_Af.length; i++)
      epsilonfGG_choice.addItem(String.valueOf(epsilonfGG_Af[i]));
    sigmaGG_choice = new Choice();
    for (i = 0; i < sigmaGG_Af.length; i++)
      sigmaGG_choice.addItem(String.valueOf(sigmaGG_Af[i]));

    p71.add(new Label("lambda_g"));
    p71.add(new Label("lambda_f"));
    p71.add(new Label("epsilon_i"));
    p71.add(new Label("epsilon_f"));
    p71.add(new Label("sigma"));
    p71.add(lambdagGG_choice);
    p71.add(lambdafGG_choice);
    p71.add(epsiloniGG_choice);
    p71.add(epsilonfGG_choice);
    p71.add(sigmaGG_choice);

    p7.add("North", p70);
    p7.add("Center", p71);
    p7.add("East", new Panel().add(new Label("     ")));
    p7.add("West", new Panel().add(new Label("     ")));

    Panel p8 = new Panel();
    p8.setLayout(new BorderLayout());

    Panel p80 = new Panel();
    p80.add(new Panel().add(new Label("     ")));

    Panel p81 = new Panel();
    p81.setLayout(new GridLayout(2,6));
    sizeSOM_choice = new Choice();
    for (i = 0; i < sizeSOM_As.length; i++)
      sizeSOM_choice.addItem(sizeSOM_As[i]);
    epsiloniSOM_choice = new Choice();
    for (i = 0; i < epsiloniSOM_Af.length; i++)
      epsiloniSOM_choice.addItem(String.valueOf(epsiloniSOM_Af[i]));
    epsilonfSOM_choice = new Choice();
    for (i = 0; i < epsilonfSOM_Af.length; i++)
      epsilonfSOM_choice.addItem(String.valueOf(epsilonfSOM_Af[i]));
    sigmaiSOM_choice = new Choice();
    for (i = 0; i < sigmaiSOM_Af.length; i++)
      sigmaiSOM_choice.addItem(String.valueOf(sigmaiSOM_Af[i]));
    sigmafSOM_choice = new Choice();
    for (i = 0; i < sigmafSOM_Af.length; i++)
      sigmafSOM_choice.addItem(String.valueOf(sigmafSOM_Af[i]));
    tmaxSOM_choice = new Choice();
    for (i = 0; i < tmaxSOM_Af.length; i++)
      tmaxSOM_choice.addItem(String.valueOf(tmaxSOM_Af[i]));

    p81.add(new Label("Grid size"));
    p81.add(new Label("epsilon_i"));
    p81.add(new Label("epsilon_f"));
    p81.add(new Label("sigma_i"));
    p81.add(new Label("sigma_f"));
    p81.add(new Label("t_max"));
    p81.add(sizeSOM_choice);
    p81.add(epsiloniSOM_choice);
    p81.add(epsilonfSOM_choice);
    p81.add(sigmaiSOM_choice);
    p81.add(sigmafSOM_choice);
    p81.add(tmaxSOM_choice);

    p8.add("North", p80);
    p8.add("Center", p81);
    p8.add("East", new Panel().add(new Label("     ")));
    p8.add("West", new Panel().add(new Label("     ")));


    cards.add(ALGO_0, p1);
    cards.add(ALGO_1, p2);
    cards.add(ALGO_2, p3);
    cards.add(ALGO_3, p4);
    cards.add(ALGO_4, p5);
    cards.add(ALGO_5, p6);
    cards.add(ALGO_6, p7);
    cards.add(ALGO_7, p8);
    pSouth.add("South", cards);

	// Default algorithm is GNG
	compute.algo = 0;
	algo_choice.select(GNG_C);
	algo_choice.show();
	((CardLayout)cards.getLayout()).show(cards, ALGO_0);

    // Get the parameter from the html-page
    String algorithm = getParameter("algorithm");
    String distrib = getParameter("distribution");

    if (distrib != null) {
      if (distrib.equals(DISTRIB_1)) {
		compute.distribution = 1;
		distrib_choice.select(1);
		distrib_choice.show();
      } else if (distrib.equals(DISTRIB_2)) {
		compute.distribution = 2;
		distrib_choice.select(2);
		distrib_choice.show();
      } else if (distrib.equals(DISTRIB_3)) {
		compute.distribution = 3;
		distrib_choice.select(3);
		distrib_choice.show();
      } else if (distrib.equals(DISTRIB_4)) {
		compute.distribution = 4;
		distrib_choice.select(4);
		distrib_choice.show();
      } else if (distrib.equals(DISTRIB_5)) {
		compute.distribution = 5;
		distrib_choice.select(5);
		distrib_choice.show();
      } else if (distrib.equals(DISTRIB_6)) {
		compute.distribution = 6;
		distrib_choice.select(6);
		distrib_choice.show();
      } else if (distrib.equals(DISTRIB_7)) {
		compute.distribution = 7;
		distrib_choice.select(7);
		distrib_choice.show();
      } else if (distrib.equals(DISTRIB_8)) {
		compute.distribution = 8;
		distrib_choice.select(8);
		distrib_choice.show();
      } else if (distrib.equals(DISTRIB_9)) {
		compute.distribution = 9;
		distrib_choice.select(9);
		distrib_choice.show();
      } else if (distrib.equals(DISTRIB_10)) {
		compute.distribution = 10;
		distrib_choice.select(10);
		distrib_choice.show();
      } else if (distrib.equals(DISTRIB_11)) {
		compute.distribution = 11;
		distrib_choice.select(11);
		distrib_choice.show();
      } else if (distrib.equals(DISTRIB_12)) {
		compute.distribution = 12;
		distrib_choice.select(12);
		distrib_choice.show();
      } else {
		compute.distribution = 0;
		distrib_choice.select(0);
		distrib_choice.show();
      }
    }

    if (algorithm != null) {
      // Init for Hard Competitive Learning (HCL)
      if (algorithm.equals(ALGO_ABBREV_1)) {
		compute.algo = 1;
		algo_choice.select(HCL_C);
		algo_choice.show();
		((CardLayout)cards.getLayout()).show(cards, ALGO_1);
		compute.epsilon = epsilonHCL_Af[0];
		nodes_lbl.setText("     Nodes:");
		compute.e_i = epsiloniHCL_Af[0];
		compute.e_f = epsilonfHCL_Af[0];
		compute.t_max = tmaxHCL_Af[0];

		// Gernerate some nodes
		for (i = 0; i < compute.maxNodes; i++)
		  compute.addNode(new Dimension(compute.INIT_WIDTH, compute.INIT_HEIGHT));
      }
      // Init for Neural Gas (NG)
      else if (algorithm.equals(ALGO_ABBREV_2)) {
		compute.algo = 2;
		algo_choice.select(NG_C);
		algo_choice.show();
		((CardLayout)cards.getLayout()).show(cards, ALGO_2);
		compute.stepSize = stepSize_Ai[3];
		stepSize_choice.select(3);
		stepSize_choice.show();
		nodes_lbl.setText("     Nodes:");
		compute.l_i = lambdaiNG_Af[0];
		compute.l_f = lambdafNG_Af[0];
		compute.e_i = epsiloniNG_Af[0];
		compute.e_f = epsilonfNG_Af[0];
		compute.t_max = tmaxNG_Af[0];

		// Gernerate some nodes
		for (i = 0; i < compute.maxNodes; i++)
		  compute.addNode(new Dimension(compute.INIT_WIDTH, compute.INIT_HEIGHT));
      }
      // Init for Neural Gas with Competitive Hebian Learning (NGwCHL)
      else if (algorithm.equals(ALGO_ABBREV_3)) {
		compute.algo = 3;
		algo_choice.select(NGwCHL_C);
		algo_choice.show();
		compute.stepSize = stepSize_Ai[3];
		stepSize_choice.select(3);
		stepSize_choice.show();
		((CardLayout)cards.getLayout()).show(cards, ALGO_3);
		nodes_lbl.setText("     Nodes:");
		compute.l_i = lambdaiCHL_Af[0];
		compute.l_f = lambdafCHL_Af[0];
		compute.e_i = epsiloniCHL_Af[0];
		compute.e_f = epsilonfCHL_Af[0];
		compute.t_max = tmaxCHL_Af[0];
		compute.delEdge_i = edgeiCHL_Ai[0];
		compute.delEdge_f = edgefCHL_Ai[0];

		// Gernerate some nodes
		for (i = 0; i < compute.maxNodes; i++)
		  compute.addNode(new Dimension(compute.INIT_WIDTH, compute.INIT_HEIGHT));
      }
      // Init for Competitive Hebian Learning (CHL)
      else if (algorithm.equals(ALGO_ABBREV_4)) {
		compute.algo = 4;
		algo_choice.select(CHL_C);
		algo_choice.show();
		compute.stepSize = stepSize_Ai[0];
		stepSize_choice.select(0);
		stepSize_choice.show();
		((CardLayout)cards.getLayout()).show(cards, ALGO_4);
		nodes_lbl.setText("     Nodes:");

		// Gernerate some nodes
		for (i = 0; i < compute.maxNodes; i++)
		  compute.addNode(new Dimension(compute.INIT_WIDTH, compute.INIT_HEIGHT));
      }
      // Init for LBG (LBG)
      else if (algorithm.equals(ALGO_ABBREV_5) ||
			   algorithm.equals(ALGO_ABBREV_5_U)) {
		if (algorithm.equals(ALGO_ABBREV_5_U)) {
		  compute.LBG_U_B = true;
		  LBG_U_cb.setState(compute.LBG_U_B);
		}
		compute.algo = 5;
		algo_choice.select(LBG_C);
		algo_choice.show();
		compute.stepSize = stepSize_Ai[1];
		stepSize_choice.select(1);
		stepSize_choice.show();
		compute.maxNodes = nodes_Ai[5];
		nodes_choice.select(5);
		nodes_choice.show();
		compute.numDiscreteSignals = discreteNumSignalsLBG_Ai[0];
		discreteNumSignalsLBG_choice.select(0);
		discreteNumSignalsLBG_choice.show();
		((CardLayout)cards.getLayout()).show(cards, ALGO_5);
		nodes_lbl.setText("     Nodes:");
		compute.errorBestLBG_U = Float.MAX_VALUE;		  
      }
      // Init for Growing Grid (GG)
      else if (algorithm.equals(ALGO_ABBREV_6)) {
		compute.algo = 6;
		algo_choice.select(GG_C);
		algo_choice.show();
		((CardLayout)cards.getLayout()).show(cards, ALGO_6);
		compute.stepSize = stepSize_Ai[0];
		compute.l_i = lambdagGG_Af[0];
		compute.l_f = lambdafGG_Af[0];
		compute.e_i = epsiloniGG_Af[0];
		compute.e_f = epsilonfGG_Af[0];
		compute.sigma = sigmaGG_Af[0];

		// Gernerate some nodes
		compute.initGrid(2, 2,
					   new Dimension(compute.INIT_WIDTH, compute.INIT_HEIGHT));
      }
      // Init for Self-Organizing Map (SOM)
      else if (algorithm.equals(ALGO_ABBREV_7)) {
		compute.algo = 7;
		algo_choice.select(SOM_C);
		algo_choice.show();
		((CardLayout)cards.getLayout()).show(cards, ALGO_7);
		compute.stepSize = stepSize_Ai[0];
		compute.e_i = epsiloniSOM_Af[0];
		compute.e_f = epsilonfSOM_Af[0];
		compute.sigma_i = sigmaiSOM_Af[0];
		compute.sigma_f = sigmafSOM_Af[0];
		compute.t_max = tmaxSOM_Af[0];
		nodes_lbl.disable();
		nodes_choice.disable();

		// Gernerate some nodes
		compute.initGrid(sizeSOM_Ai[0][0], sizeSOM_Ai[0][1],
					   new Dimension(compute.INIT_WIDTH, compute.INIT_HEIGHT));
      }
      // Init for Growing Neural Gas (GNG) and GNG-U
      else {
		if (algorithm.equals(ALGO_ABBREV_0_U)) {
		  compute.utilityGNGB = true;
		  utilityGNG_cb.setState(compute.utilityGNGB);
		}
		compute.addNode(new Dimension(compute.INIT_WIDTH, compute.INIT_HEIGHT));
		if (compute.maxNodes != 1)
		  compute.addNode(new Dimension(compute.INIT_WIDTH, compute.INIT_HEIGHT));
      }
    }
	compute.errorGraph = new GraphGNG(this);
	compute.errorGraph.graph.startNewTrace();
  }

  public void start() {
    compute.start();
  }

  public void stop() {
    compute.stop();
  }

  public void destroy() {
	compute.destroy();
  }

  public void graphClose() {
	compute.errorGraphB = false;
	errorGraph_cb.setState(false);
	compute.graphClose();
  }


  public boolean action(Event evt, Object arg) {
	int i;
    Dimension d = compute.size();

    // A Checkbox event?
    if (arg instanceof Boolean) {

      // Get the label of the changed checkbox
      String cb = ((Checkbox)evt.target).getLabel();

      if (cb.equals(SIGNALS)) {
		compute.signalsB = ((Boolean)arg).booleanValue();
      } else if (cb.equals(NO_NEW_NODES)) {
		compute.noNodesB = ((Boolean)arg).booleanValue();
      } else if (cb.equals(UTILITY_GNG)) {
		compute.utilityGNGB = ((Boolean)arg).booleanValue();
      } else if (cb.equals(LBG_U)) {
		compute.LBG_U_B = ((Boolean)arg).booleanValue();
      } else if (cb.equals(SOUND)) {
		compute.soundB = ((Boolean)arg).booleanValue();
      } else if (cb.equals(WHITE)) {
		compute.whiteB = ((Boolean)arg).booleanValue();
      } else if (cb.equals(RNDINIT)) {
		compute.rndInitB = ((Boolean)arg).booleanValue();
      } else if (cb.equals(NODES)) {
		compute.nodesB = ((Boolean)arg).booleanValue();
      } else if (cb.equals(EDGES)) {
		compute.edgesB = ((Boolean)arg).booleanValue();
      } else if (cb.equals(ERRORGRAPH)) {
		compute.errorGraphB = ((Boolean)arg).booleanValue();
      } else if (cb.equals(VORONOI)) {
		compute.voronoiB = ((Boolean)arg).booleanValue();
		compute.nodesMovedB = true;
      } else if (cb.equals(DELAUNAY)) {
		compute.delaunayB = ((Boolean)arg).booleanValue();
		compute.nodesMovedB = true;
      } else if (cb.equals(TEACH)) {
		compute.teachB = ((Boolean)arg).booleanValue();
      } else if (cb.equals(VARIABLE)) {
		compute.variableB = ((Boolean)arg).booleanValue();
		if (compute.variableB) {
		  epsilonHCL_lbl.disable();
		  epsilonHCL_choice.disable();

		  epsiloniHCL_lbl.enable();
		  epsilonfHCL_lbl.enable();
		  tmaxHCL_lbl.enable();
		  
		  epsiloniHCL_choice.enable();
		  epsilonfHCL_choice.enable();
		  tmaxHCL_choice.enable();
		} else {
		  epsilonHCL_lbl.enable();
		  epsilonHCL_choice.enable();

		  epsiloniHCL_lbl.disable();
		  epsilonfHCL_lbl.disable();
		  tmaxHCL_lbl.disable();
		  
		  epsiloniHCL_choice.disable();
		  epsilonfHCL_choice.disable();
		  tmaxHCL_choice.disable();
		}
      }
      return true;
    }

    // A Button event?
    else if (evt.target instanceof Button) {

      // Start
      if (BUTTON_0.equals(arg)) {
		start_b.disable();
		stop_b.enable();
		restart_b.disable();
		compute.stopB = false;
		compute.readyLBG_B = false;
      }
      // Stop
      else if (BUTTON_1.equals(arg)) {
		start_b.enable();
		stop_b.disable();
		restart_b.enable();
		compute.stopB = true;
      }
      // Reset
      else if (BUTTON_3.equals(arg)) {

		// Reset values
		compute.numRun = 0;
		compute.nnodes = 0;
		compute.nedges = 0;
		compute.noNodesB = noNodes_cb.getState();
		compute.utilityGNGB = utilityGNG_cb.getState();
		compute.nodesMovedB = true;

        // Set specific algorithm parameters
        if (compute.algo == 0) {
		  compute.addNode(d);
		  if (compute.maxNodes != 1)
			compute.addNode(d);
        } else if (compute.algo == 5) {
		  // Initialize discrete signals
		  // compute.initDiscreteSignals(compute.distribution);
		  compute.readyLBG_B = false;
		  compute.errorBestLBG_U = Float.MAX_VALUE;		  
		  compute.LBG_U_B = LBG_U_cb.getState();

		  // Gernerate some nodes
		  int z = (int) (compute.numDiscreteSignals * Math.random());
		  for (i = 0; i < compute.maxNodes; i++)
			compute.addNode(compute.discreteSignalsX[(z+i)%compute.numDiscreteSignals],
							compute.discreteSignalsY[(z+i)%compute.numDiscreteSignals]);
        } else if (compute.algo == 6) {
		  // Set default values
		  compute.fineTuningB = false;
		  if (compute.maxNodes < 4) {
			compute.maxNodes = nodes_Ai[0];
			nodes_choice.select(0);
			nodes_choice.show();
		  }

		  // Gernerate some nodes
		  compute.initGrid(2, 2, d);

        } else if (compute.algo == 7) {
		  // Set default values

		  // Gernerate some nodes
		  compute.initGrid(sizeSOM_Ai[sizeSOM_index][0],
						   sizeSOM_Ai[sizeSOM_index][1], d);

		} else {
          // Gernerate some nodes
          for (i = 0; i < compute.maxNodes; i++)
            compute.addNode(d);
        }
      }

      return true;
    }

    // A Choice event?
    else if (evt.target instanceof Choice) {

      // algorithm
      if (algo_choice.equals(evt.target)) {

		// Reset values
		compute.numRun = 0;
		compute.nnodes = 0;
		compute.nedges = 0;
		compute.noNodesB = noNodes_cb.getState();
		compute.utilityGNGB = utilityGNG_cb.getState();
		compute.nodesMovedB = true;
		nodes_lbl.enable();
		nodes_choice.enable();

        ((CardLayout)cards.getLayout()).show(cards,(String)arg);

        // Set specific algorithm parameters
		if (arg.equals(ALGO_0)) {
		  compute.algo = 0;

		  // Set default values
		  compute.addNode(d);
		  if (compute.maxNodes != 1)
			compute.addNode(d);
		  nodes_lbl.setText("max. Nodes:");
          compute.epsilonGNG = epsilonGNG1_Af[0];
		  epsilonGNG1_choice.select(0);
		  epsilonGNG1_choice.show();
          compute.epsilonGNG2 = epsilonGNG2_Af[0];
		  epsilonGNG2_choice.select(0);
		  epsilonGNG2_choice.show();
          compute.alphaGNG = alphaGNG_Af[0];
		  alphaGNG_choice.select(0);
		  alphaGNG_choice.show();
          compute.forgetFactor = 1.0f - betaGNG_Af[0];
          compute.forgetFactorUtility = 1.0f - betaGNG_Af[0];
		  betaGNG_choice.select(0);
		  betaGNG_choice.show();
          compute.utilityGNG = utilityGNG_Af[0];
		  utilityGNG_choice.select(0);
		  utilityGNG_choice.show();
          compute.MAX_EDGE_AGE = delEdgeGNG_Ai[0];
		  delEdgeGNG_choice.select(0);
		  delEdgeGNG_choice.show();
          compute.NUM_NEW_NODE = newNodeGNG_Ai[0];
		  newNodeGNG_choice.select(0);
		  newNodeGNG_choice.show();
		} else if (arg.equals(ALGO_1)) {
		  compute.algo = 1;

		  // Set default values
		  nodes_lbl.setText("Nodes:");
          compute.epsilon = epsilonHCL_Af[0];
		  epsilonHCL_choice.select(0);
		  epsilonHCL_choice.show();
		  compute.e_i = epsiloniHCL_Af[0];
		  epsiloniHCL_choice.select(0);
		  epsiloniHCL_choice.show();
		  compute.e_f = epsilonfHCL_Af[0];
		  epsilonfHCL_choice.select(0);
		  epsilonfHCL_choice.show();
		  compute.t_max = tmaxHCL_Af[0];
		  tmaxHCL_choice.select(0);
		  tmaxHCL_choice.show();

          // Gernerate some nodes
          for (i = 0; i < compute.maxNodes; i++)
            compute.addNode(d);
		} else if (arg.equals(ALGO_2)) {
		  compute.algo = 2;

		  // Set default values
		  nodes_lbl.setText("Nodes:");
          compute.l_i = lambdaiNG_Af[0];
		  lambdaiNG_choice.select(0);
		  lambdaiNG_choice.show();
          compute.l_f = lambdafNG_Af[0];
		  lambdafNG_choice.select(0);
		  lambdafNG_choice.show();
          compute.e_i = epsiloniNG_Af[0];
		  epsiloniNG_choice.select(0);
		  epsiloniNG_choice.show();
          compute.e_f = epsilonfNG_Af[0];
		  epsilonfNG_choice.select(0);
		  epsilonfNG_choice.show();
          compute.t_max = tmaxNG_Af[0];
		  tmaxNG_choice.select(0);
		  tmaxNG_choice.show();

          // Gernerate some nodes
          for (i = 0; i < compute.maxNodes; i++)
            compute.addNode(d);
		} else if (arg.equals(ALGO_3)) {
		  compute.algo = 3;

		  // Set default values
		  nodes_lbl.setText("Nodes:");
          compute.l_i = lambdaiCHL_Af[0];
		  lambdaiCHL_choice.select(0);
		  lambdaiCHL_choice.show();
          compute.l_f = lambdafCHL_Af[0];
		  lambdafCHL_choice.select(0);
		  lambdafCHL_choice.show();
          compute.e_i = epsiloniCHL_Af[0];
		  epsiloniCHL_choice.select(0);
		  epsiloniCHL_choice.show();
          compute.e_f = epsilonfCHL_Af[0];
		  epsilonfCHL_choice.select(0);
		  epsilonfCHL_choice.show();
          compute.t_max = tmaxCHL_Af[0];
		  tmaxCHL_choice.select(0);
		  tmaxCHL_choice.show();
          compute.delEdge_i = edgeiCHL_Ai[0];
		  edgeiCHL_choice.select(0);
		  edgeiCHL_choice.show();
          compute.delEdge_f = edgefCHL_Ai[0];
		  edgefCHL_choice.select(0);
		  edgefCHL_choice.show();

          // Gernerate some nodes
          for (i = 0; i < compute.maxNodes; i++)
            compute.addNode(d);
		} else if (arg.equals(ALGO_4)) {
		  compute.algo = 4;

		  // Set default values
		  nodes_lbl.setText("Nodes:");

          // Gernerate some nodes
          for (i = 0; i < compute.maxNodes; i++)
            compute.addNode(d);
		} else if (arg.equals(ALGO_5)) {
		  compute.algo = 5;

		  // Set default values
		  compute.stepSize = stepSize_Ai[1];
		  stepSize_choice.select(1);
		  stepSize_choice.show();
		  compute.maxNodes = nodes_Ai[5];
		  nodes_choice.select(5);
		  nodes_choice.show();
		  compute.numDiscreteSignals = discreteNumSignalsLBG_Ai[0];
		  discreteNumSignalsLBG_choice.select(0);
		  discreteNumSignalsLBG_choice.show();
		  compute.readyLBG_B = false;
		  compute.LBG_U_B = LBG_U_cb.getState();
		  nodes_lbl.setText("Nodes:");
		  compute.errorBestLBG_U = Float.MAX_VALUE;		  

		  // Initialize discrete signals
		  compute.initDiscreteSignals(compute.distribution);

		  // Gernerate some nodes
		  int z = (int) (compute.numDiscreteSignals * Math.random());
		  for (i = 0; i < compute.maxNodes; i++)
			compute.addNode(compute.discreteSignalsX[(z+i)%compute.numDiscreteSignals],
							compute.discreteSignalsY[(z+i)%compute.numDiscreteSignals]);
		} else if (arg.equals(ALGO_6)) {
		  compute.algo = 6;

		  // Set default values
		  compute.fineTuningB = false;
		  compute.initGrid(2, 2, d);
		  compute.maxNodes = nodes_Ai[0];
		  nodes_choice.select(0);
		  nodes_choice.show();
		  nodes_lbl.setText("max. Nodes:");
          compute.l_i = lambdagGG_Af[0];
		  lambdagGG_choice.select(0);
		  lambdagGG_choice.show();
          compute.l_f = lambdafGG_Af[0];
		  lambdafGG_choice.select(0);
		  lambdafGG_choice.show();
          compute.e_i = epsiloniGG_Af[0];
		  epsiloniGG_choice.select(0);
		  epsiloniGG_choice.show();
          compute.e_f = epsilonfGG_Af[0];
		  epsilonfGG_choice.select(0);
		  epsilonfGG_choice.show();
          compute.sigma = sigmaGG_Af[0];
		  sigmaGG_choice.select(0);
		  sigmaGG_choice.show();
		} else if (arg.equals(ALGO_7)) {
		  compute.algo = 7;

		  // Set default values
		  compute.initGrid(sizeSOM_Ai[0][0],
						   sizeSOM_Ai[0][1], d);
		  nodes_lbl.disable();
		  nodes_choice.disable();
          compute.e_i = epsiloniSOM_Af[0];
		  epsiloniSOM_choice.select(0);
		  epsiloniSOM_choice.show();
          compute.e_f = epsilonfSOM_Af[0];
		  epsilonfSOM_choice.select(0);
		  epsilonfSOM_choice.show();
          compute.sigma_i = sigmaiSOM_Af[0];
		  sigmaiSOM_choice.select(0);
		  sigmaiSOM_choice.show();
          compute.sigma_f = sigmafSOM_Af[0];
		  sigmafSOM_choice.select(0);
		  sigmafSOM_choice.show();
          compute.t_max = tmaxSOM_Af[0];
		  tmaxSOM_choice.select(0);
		  tmaxSOM_choice.show();
		}
      } 
      // distribution
      else if (distrib_choice.equals(evt.target)) {
		if (compute.soundB) {
          play(getCodeBase(), "audio/drummer.au");
		}
        compute.distribution = distrib_choice.getSelectedIndex();
		// Initialize discrete signals
		compute.initDiscreteSignals(compute.distribution);
		compute.errorBestLBG_U = Float.MAX_VALUE;		  
      } 
      // stepsize
      else if (stepSize_choice.equals(evt.target)) {
        compute.stepSize = stepSize_Ai[stepSize_choice.getSelectedIndex()];
      } 
      // machine speed
      else if (speed_choice.equals(evt.target)) {
        compute.speed = speed_Ai[speed_choice.getSelectedIndex()];
      } 
      // max. nodes
      else if (nodes_choice.equals(evt.target)) {
        compute.maxNodes = nodes_Ai[nodes_choice.getSelectedIndex()];
		compute.fineTuningB = false;
      } 
      // insert new node
      else if (newNodeGNG_choice.equals(evt.target)) {
        compute.NUM_NEW_NODE = newNodeGNG_Ai[newNodeGNG_choice.getSelectedIndex()];
      } 
      // max. edges
      else if (delEdgeGNG_choice.equals(evt.target)) {
        compute.MAX_EDGE_AGE = delEdgeGNG_Ai[delEdgeGNG_choice.getSelectedIndex()];
      } 
      // epsilon HCL
      else if (epsilonHCL_choice.equals(evt.target)) {
        compute.epsilon = epsilonHCL_Af[epsilonHCL_choice.getSelectedIndex()];
      } 
      // epsilon_i HCL
      else if (epsiloniHCL_choice.equals(evt.target)) {
        compute.e_i = epsiloniHCL_Af[epsiloniHCL_choice.getSelectedIndex()];
      } 
      // epsilon_f HCL
      else if (epsilonfHCL_choice.equals(evt.target)) {
        compute.e_f = epsilonfHCL_Af[epsilonfHCL_choice.getSelectedIndex()];
      } 
      // t_max HCL
      else if (tmaxHCL_choice.equals(evt.target)) {
        compute.t_max = tmaxHCL_Af[tmaxHCL_choice.getSelectedIndex()];
      } 
      // epsilon winner GNG
      else if (epsilonGNG1_choice.equals(evt.target)) {
        compute.epsilonGNG = epsilonGNG1_Af[epsilonGNG1_choice.getSelectedIndex()];
      } 
      // epsilon neighbors GNG
      else if (epsilonGNG2_choice.equals(evt.target)) {
        compute.epsilonGNG2 = epsilonGNG2_Af[epsilonGNG2_choice.getSelectedIndex()];
      } 
      // alpha GNG
      else if (alphaGNG_choice.equals(evt.target)) {
        compute.alphaGNG = alphaGNG_Af[alphaGNG_choice.getSelectedIndex()];
      } 
      // beta GNG
      else if (betaGNG_choice.equals(evt.target)) {
        compute.forgetFactor = 1.0f - betaGNG_Af[betaGNG_choice.getSelectedIndex()];
        compute.forgetFactorUtility = 1.0f - betaGNG_Af[betaGNG_choice.getSelectedIndex()];
      } 
      // utility GNG
      else if (utilityGNG_choice.equals(evt.target)) {
        compute.utilityGNG = utilityGNG_Af[utilityGNG_choice.getSelectedIndex()];
      } 
      // lambda_i NG
      else if (lambdaiNG_choice.equals(evt.target)) {
        compute.l_i = lambdaiNG_Af[lambdaiNG_choice.getSelectedIndex()];
      } 
      // lambda_f NG
      else if (lambdafNG_choice.equals(evt.target)) {
        compute.l_f = lambdafNG_Af[lambdafNG_choice.getSelectedIndex()];
      } 
      // epsilon_i NG
      else if (epsiloniNG_choice.equals(evt.target)) {
        compute.e_i = epsiloniNG_Af[epsiloniNG_choice.getSelectedIndex()];
      } 
      // epsilon_f NG
      else if (epsilonfNG_choice.equals(evt.target)) {
        compute.e_f = epsilonfNG_Af[epsilonfNG_choice.getSelectedIndex()];
      } 
      // t_max NG
      else if (tmaxNG_choice.equals(evt.target)) {
        compute.t_max = tmaxNG_Af[tmaxNG_choice.getSelectedIndex()];
      } 
      // lambda_g GG
      else if (lambdagGG_choice.equals(evt.target)) {
        compute.l_i = lambdagGG_Af[lambdagGG_choice.getSelectedIndex()];
      } 
      // lambda_f GG
      else if (lambdafGG_choice.equals(evt.target)) {
        compute.l_f = lambdafGG_Af[lambdafGG_choice.getSelectedIndex()];
      } 
      // epsilon_i GG
      else if (epsiloniGG_choice.equals(evt.target)) {
        compute.e_i = epsiloniGG_Af[epsiloniGG_choice.getSelectedIndex()];
      } 
      // epsilon_f GG
      else if (epsilonfGG_choice.equals(evt.target)) {
        compute.e_f = epsilonfGG_Af[epsilonfGG_choice.getSelectedIndex()];
      } 
      // sigma GG
      else if (sigmaGG_choice.equals(evt.target)) {
        compute.sigma = sigmaGG_Af[sigmaGG_choice.getSelectedIndex()];
      } 
      // grid size SOM
      else if (sizeSOM_choice.equals(evt.target)) {
		sizeSOM_index = sizeSOM_choice.getSelectedIndex();
      } 
      // epsilon_i SOM
      else if (epsiloniSOM_choice.equals(evt.target)) {
        compute.e_i = epsiloniSOM_Af[epsiloniSOM_choice.getSelectedIndex()];
      } 
      // epsilon_f SOM
      else if (epsilonfSOM_choice.equals(evt.target)) {
        compute.e_f = epsilonfSOM_Af[epsilonfSOM_choice.getSelectedIndex()];
      } 
      // sigma_i SOM
      else if (sigmaiSOM_choice.equals(evt.target)) {
        compute.sigma_i = sigmaiSOM_Af[sigmaiSOM_choice.getSelectedIndex()];
      } 
      // sigma_f SOM
      else if (sigmafSOM_choice.equals(evt.target)) {
        compute.sigma_f = sigmafSOM_Af[sigmafSOM_choice.getSelectedIndex()];
      } 
      // t_max SOM
      else if (tmaxSOM_choice.equals(evt.target)) {
        compute.t_max = tmaxSOM_Af[tmaxSOM_choice.getSelectedIndex()];
      } 
      // lambda_i CHL
      else if (lambdaiCHL_choice.equals(evt.target)) {
        compute.l_i = lambdaiCHL_Af[lambdaiCHL_choice.getSelectedIndex()];
      } 
      // lambda_f CHL
      else if (lambdafCHL_choice.equals(evt.target)) {
        compute.l_f = lambdafCHL_Af[lambdafCHL_choice.getSelectedIndex()];
      } 
      // epsilon_i CHL
      else if (epsiloniCHL_choice.equals(evt.target)) {
        compute.e_i = epsiloniCHL_Af[epsiloniCHL_choice.getSelectedIndex()];
      } 
      // epsilon_f CHL
      else if (epsilonfCHL_choice.equals(evt.target)) {
        compute.e_f = epsilonfCHL_Af[epsilonfCHL_choice.getSelectedIndex()];
      } 
      // t_max CHL
      else if (tmaxCHL_choice.equals(evt.target)) {
        compute.t_max = tmaxCHL_Af[tmaxCHL_choice.getSelectedIndex()];
      } 
      // edge_i CHL
      else if (edgeiCHL_choice.equals(evt.target)) {
        compute.delEdge_i = edgeiCHL_Ai[edgeiCHL_choice.getSelectedIndex()];
      } 
      // edge_f CHL
      else if (edgefCHL_choice.equals(evt.target)) {
        compute.delEdge_f = edgefCHL_Ai[edgefCHL_choice.getSelectedIndex()];
      } 
      // numDiscreteSignals LBG
      else if (discreteNumSignalsLBG_choice.equals(evt.target)) {
		// Initialize discrete signals
		compute.initDiscreteSignals(compute.distribution);
		// Set number of discrete signals
        compute.numDiscreteSignals = discreteNumSignalsLBG_Ai[discreteNumSignalsLBG_choice.getSelectedIndex()];
		compute.errorBestLBG_U = Float.MAX_VALUE;		  
      } 

      return true;
    }
    return false;
  }

  public String[][] getParameterInfo() {
    String[][] info = {
      // Arrays of arrays of strings describing each parameter.
      {"algorithm\t\t",
       "The abbreviation of an algorithm (GNG, GNG-U, HCL, NG, NGwCHL, " +
	   "CHL, LBG, LBG-U, GG, SOM)",
       "The starting algorithm"},
      {"distribution\t",
       "The name of a distribution (Rectangle, Ring, Circle, UNI, " +
	   "Small Spirals, Large Spirals, HiLo Density, Discrete, UNIT, " +
	   "Move & Jump, Move, Jump, Right MouseB)",
       "The initial distribution"}
    };
    return info;
  }

  /**
   * Where to find beta releases and additional information.
   */
  static final String myHomepage =
            "http://www.neuroinformatik.ruhr-uni-bochum.de/ini/PEOPLE/loos";



  public String getAppletInfo() {
    String versionInfo = "DemoGNG " + compute.DGNG_VERSION +
	  ". Written by Hartmut S. Loos\n\nCopyright 1996-1998" +
	  " under the terms of the GNU General Public License." +
	  "\n\nFor updates look at " + myHomepage;
    return versionInfo;
  }
  
}

