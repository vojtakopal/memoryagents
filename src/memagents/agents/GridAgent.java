package memagents.agents;

import java.awt.Color;
import java.awt.Graphics;

import memagents.Simulation;
import memagents.environment.Matrix;
import memagents.food.FoodGenerator;
import memagents.memory.Memory;
import memagents.memory.GridMemory;
import memagents.memory.quad.GridCell;

public class GridAgent extends Agent {

	protected GridMemory memory;
	
	public GridAgent(Simulation simulation) {
		super(simulation);
		
		memory = new GridMemory(Simulation.SIZE, Simulation.SIZE, simulation, this);
		position.x = Simulation.SIZE / 2; //simulation.getRandom().nextInt(Simulation.SIZE);
		position.y = Simulation.SIZE / 2; //simulation.getRandom().nextInt(Simulation.SIZE);
	}
	
	public String getName() {
		return "grid";
	}

	public Memory getMemory() {
		return memory;
	}

	/**
	 * 	Draws current agent's perception of the environment into the graphic canvas. 
	 * 	Also it draws the state of agent's memory.
	 * 
	 */
	synchronized public void draw(Graphics g, int width, int height) {
		// draw memory representation
		int cellSize = 16;
		double xV = width / (double)getMemory().getWidth();
		double yV = height / (double)getMemory().getHeight();
		
		for (int foodKind = 0; foodKind < FoodGenerator.getSize(); foodKind++) {
			Matrix<GridCell> matrix = memory.getMatrix(foodKind); 
			int deltaX = 0; // 0 - 4
			int deltaY = 0; // 0 - 4
			float maxAlpha = 0.5f;
			
			// compute the shift in cell
			deltaX = foodKind % 4;
			deltaY = foodKind / 4;
			
			for (int i = 0; i < memory.getCols(); i++) {
				for (int j = 0; j < memory.getRows(); j++) {
					long containsFood = memory.getCellAt(matrix, i, j).containsFood();
					if (containsFood > 0) {
						Color color = simulation.getGenerator(foodKind).getColor();
						int alpha = (int)(containsFood*maxAlpha);
						if (alpha < 0) alpha = 0;
						if (alpha > 250) alpha = 250;
						Color alphaColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
						g.setColor(alphaColor);
						g.fillRect((int)((i*16+deltaX*4)*xV), (int)((j*16+deltaY*4)*yV), (int)(4*xV), (int)(4*yV));
					}
				}
			}
		}
		
		for (int i = 0; i < memory.getCols(); i++) {
			g.setColor(Color.GRAY.brighter());
			g.drawLine((int)(i*cellSize*xV), 0, (int)(i*cellSize*xV), (int)(yV*width));
		}

		for (int j = 0; j < memory.getRows(); j++) {
			g.setColor(Color.GRAY.brighter());
			g.drawLine(0, (int)(j*cellSize*yV), (int)(xV*height), (int)(j*cellSize*yV));
		}
		
		super.draw(g, width, height);
	}
	
}
