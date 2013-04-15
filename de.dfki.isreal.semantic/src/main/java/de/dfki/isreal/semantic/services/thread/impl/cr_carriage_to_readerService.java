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

public class cr_carriage_to_readerService extends SEServiceImplementationThread {
	private static Logger logger = Logger.getLogger(cr_carriage_to_readerService.class);
	
	private static String type = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
	private static String located = "http://www.dfki.de/isreal/spatial_ontology.owl#isLocatedIn";
	
	public cr_carriage_to_readerService(ServiceWrapper service, BindingList b) {
		super(service, b);
	}

	@Override
	protected List<String> serviceImplementation() {
		if (Config.isRoleRegistered("GSE")){
			// Trigger animation.
			graphics.sendAnimationCall("car_to_reader", null);
			
			// wait for animation
			waitMillis(8000);
			
			// TODO Change GSE as Effect since no graphical sensor script yet
			Statement in = new StatementImpl(bnd.getInstance("http://www.dfki.de/isreal/cr_carriage_to_reader.owl#self"), located, bnd.getInstance("http://www.dfki.de/isreal/cr_carriage_to_reader.owl#readerpos"));
			List<Statement> ins = new ArrayList<Statement>();
			ins.add(in);
			
			Statement out = new StatementImpl(bnd.getInstance("http://www.dfki.de/isreal/cr_carriage_to_reader.owl#self"), located, bnd.getInstance("http://www.dfki.de/isreal/cr_carriage_to_reader.owl#endpos"));
			List<Statement> outs = new ArrayList<Statement>();
			outs.add(out);
			
			((GSE) Config.getComponent(Config.getComponentNameForRole("GSE"))).insert(ins);
			((GSE) Config.getComponent(Config.getComponentNameForRole("GSE"))).remove(outs);
			
			services_to_check.add("http://www.dfki.de/isreal/cr_redlamp_on.owl#CR_REDLAMP_ONService");
			services_to_check.add("http://www.dfki.de/isreal/cr_pill_to_slider.owl#cr_pill_to_sliderService");
			services_to_check.add("http://www.dfki.de/isreal/cr_pill_to_fill.owl#cr_pill_to_fillService");
			services_to_check.add("http://www.dfki.de/isreal/cr_carriage_to_fill.owl#cr_carriage_to_fillService");
			
			return new ArrayList<String>();
		} else {
			logger.error("No GlobalSE available in thread: " + cr_carriage_to_readerService.class.getName());
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
