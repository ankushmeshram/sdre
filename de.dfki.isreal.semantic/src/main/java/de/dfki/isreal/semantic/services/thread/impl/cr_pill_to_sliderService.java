package de.dfki.isreal.semantic.services.thread.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.dfki.isreal.components.GSE;
import de.dfki.isreal.data.BindingList;
import de.dfki.isreal.data.ServiceWrapper;
import de.dfki.isreal.data.Statement;
import de.dfki.isreal.data.impl.StatementImpl;
import de.dfki.isreal.main.Config;
import de.dfki.isreal.network.callerfactories.GraphicsCallerFactory;
import de.dfki.isreal.semantic.services.SEServiceImplementationThread;

public class cr_pill_to_sliderService extends SEServiceImplementationThread {
	private static Logger logger = Logger.getLogger(cr_pill_to_sliderService.class);
	

	private static String type = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
	private static String located = "http://www.dfki.de/isreal/spatial_ontology.owl#isLocatedIn";
	private static String hasColor = "http://www.dfki.de/isreal/messemodul.owl#hasColor";
	
	public cr_pill_to_sliderService(ServiceWrapper service, BindingList b) {
		super(service, b);
	}

	@Override
	protected List<String> serviceImplementation() {
		if (Config.isRoleRegistered("GSE")){
			// Trigger animation with the correct color.
			String col = bnd.getInstance("http://www.dfki.de/isreal/cr_pill_to_slider.owl#col");
			if (col == null) logger.error("Could not determine color.");
			if (col.equalsIgnoreCase("http://www.dfki.de/isreal/messemodul_abox.owl#BLUE")){
				graphics.sendAnimationCall("blue_pill_to_slider", null);
			}
			if (col.equalsIgnoreCase("http://www.dfki.de/isreal/messemodul_abox.owl#WHITE")){
				graphics.sendAnimationCall("white_pill_to_slider", null);
			}
			if (col.equalsIgnoreCase("http://www.dfki.de/isreal/messemodul_abox.owl#RED")){
				graphics.sendAnimationCall("red_pill_to_slider", null);
			}

			// wait for animation
			waitMillis(4000);
			
			// effects
			Statement in = new StatementImpl(bnd.getInstance("http://www.dfki.de/isreal/cr_pill_to_slider.owl#self"), located, bnd.getInstance("http://www.dfki.de/isreal/cr_pill_to_slider.owl#sliderarea"));
			List<Statement> ins = new ArrayList<Statement>();
			ins.add(in);
			
			Statement out = new StatementImpl(bnd.getInstance("http://www.dfki.de/isreal/cr_pill_to_slider.owl#self"), located, bnd.getInstance("http://www.dfki.de/isreal/cr_pill_to_slider.owl#magarea"));
			List<Statement> outs = new ArrayList<Statement>();
			outs.add(out);
			
			((GSE) Config.getComponent(Config.getComponentNameForRole("GSE"))).insert(ins);
			((GSE) Config.getComponent(Config.getComponentNameForRole("GSE"))).remove(outs);
			
			services_to_check.add("http://www.dfki.de/isreal/cr_magazine_empty.owl#cr_magazine_emptyService");
			
			
			return new ArrayList<String>();
		} else {
			logger.error("No GlobalSE available in thread: " + cr_pill_to_sliderService.class.getName());
		}
		return new ArrayList<String>();
	}
	
	/**
	 * This methods waits for the given numer of milliseconds.
	 * 
	 * @param i
	 *            - Milliseconds to wait.
	 */
	private static void waitMillis(int i) {
		long start = System.currentTimeMillis();
		while ((System.currentTimeMillis() - start) < i) {
		}
	}

}
