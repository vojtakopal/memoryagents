package memagents.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import memagents.Simulation;
import memagents.agents.Agent;
import memagents.food.FoodGenerator;

public class NeedsMonitor implements Monitor {
	protected FileWriter writer;
	protected Simulation simulation;
	public NeedsMonitor(Simulation simulation) {
		this.simulation = simulation;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String logname = "NM-"+sdf.format(Calendar.getInstance().getTime())+".txt";
		
		try {
			writer = new FileWriter(logname);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			writer.append(simulation.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	synchronized public void monitor(Agent agent) {
		try {
			writer.append(String.valueOf(agent.getId()));
			writer.append('#');
			
			for (int foodKind = 0; foodKind < FoodGenerator.getSize(); foodKind++) {
				float value = agent.getNeed(foodKind);
				writer.append(String.valueOf(value));
				writer.append(";");
			}
			
			writer.append("\n");
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
