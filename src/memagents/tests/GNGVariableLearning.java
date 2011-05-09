package memagents.tests;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.FileHandler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class GNGVariableLearning {

	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, InterruptedException {
		
			int numSteps = 100000;
			int maxThreads = 30;
			String xmlPath = "settings.xml";
			
			if (args.length > 0) {
				numSteps = Integer.parseInt(args[0]);
			}
			if (args.length > 1) {
				maxThreads = Integer.parseInt(args[1]);
			}
			if (args.length > 2) {
				xmlPath = args[2];
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			String logname = "log_"+sdf.format(Calendar.getInstance().getTime())+".txt";
			FileOutputStream stream = new FileOutputStream(logname, true);
	
			// parse xml with settings
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
	
			Document dom = db.parse(xmlPath);
			NamedNodeMap appAttributes = dom.getAttributes();
			NodeList alphas = dom.getElementsByTagName("alpha");
			NodeList betas = dom.getElementsByTagName("beta");
			NodeList epsilons = dom.getElementsByTagName("epsilon");
			NodeList epsilon2s = dom.getElementsByTagName("epsilon2");
			NodeList numNewNodes = dom.getElementsByTagName("numNewNode");
			NodeList maxEdgeAges = dom.getElementsByTagName("maxEdgeAge");
			NodeList maxNodess = dom.getElementsByTagName("maxNodes");
			
			ArrayList<String[]> permutations = new ArrayList<String[]>();
			
			for (int ia = 0; ia < alphas.getLength(); ia++) {
				Node na = alphas.item(ia);
				for (int ib = 0; ib < betas.getLength(); ib++) {
					Node nb = betas.item(ib);
					for (int ie = 0; ie < epsilons.getLength(); ie++) {
						Node ne = epsilons.item(ie);
						for (int ie2 = 0; ie2 < epsilon2s.getLength(); ie2++) {
							Node ne2 = epsilon2s.item(ie2);
							for (int in = 0; in < numNewNodes.getLength(); in++) {
								Node nn = numNewNodes.item(in);
								for (int im = 0; im < maxEdgeAges.getLength(); im++) {
									Node nm = maxEdgeAges.item(im);
									for (int ima = 0; ima < maxNodess.getLength(); ima++) {
										Node nma = maxNodess.item(ima);
										
										String[] item = {
												na.getFirstChild().getTextContent(),
												nb.getFirstChild().getTextContent(),
												ne.getFirstChild().getTextContent(),
												ne2.getFirstChild().getTextContent(),
												nn.getFirstChild().getTextContent(),
												nm.getFirstChild().getTextContent(),
												nma.getFirstChild().getTextContent(),
										};
										permutations.add(item);									
									}
								}
							}
						}
					}
				}
			}
					
			// get information about following computation
			System.out.println("Started with "+permutations.size()+" computation and "+numSteps+" steps each to go. Using "+maxThreads+" threads. Log into "+logname+".");
			SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss:SSS yyyy/MM/dd");
			Date now = Calendar.getInstance().getTime();
			long finishTime = now.getTime() + (long)(permutations.size()*numSteps/maxThreads);
			System.out.println("Estimated end: "+format.format(new Date(finishTime)));
			
			System.setOut(new PrintStream(stream));
			
			for (String[] item : permutations) {
	
				Thread.sleep(250);
				
				float alpha = Float.parseFloat(item[0]);
				float beta = Float.parseFloat(item[1]);
				float epsilon = Float.parseFloat(item[2]);
				float epsilon2 = Float.parseFloat(item[3]);
				int numNewNode = Integer.parseInt(item[4]);
				int maxEdgeAge = Integer.parseInt(item[5]);
				int maxNodes = Integer.parseInt(item[6]);
				
				DynamicComputeGNG gng = new DynamicComputeGNG(true);
				gng.setParams(alpha, beta, epsilon, epsilon2, numNewNode, maxEdgeAge, maxNodes);
				
				while (DynamicComputeGNG.threadCounter > maxThreads) {
					
					Thread.sleep(1000);
					
				}
				
				gng.start(numSteps);	
							
			}
		
				
	}
	
}
