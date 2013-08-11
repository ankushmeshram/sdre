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

public class cr_flip_slider_downService extends SEServiceImplementationThread {
	private static Logger logger = Logger.getLogger(cr_flip_slider_downService.class);
	

	private static String type = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
	private static String up = "http://www.dfki.de/isreal/abstract_concepts.owl#Up";
	private static String down = "http://www.dfki.de/isreal/abstract_concepts.owl#Down";
	
	public cr_flip_slider_downService(ServiceWrapper service, BindingList b) {
		super(service, b);
	}

	@Override
	protected List<String> serviceImplementation() {
		if (Config.isRoleRegistered("GSE")){
			
			// Trigger animation with the correct color.
			graphics.sendAnimationCall("slider_down", null);

			// wait for animation
			waitMillis(1000);
			
			// effects
			Statement in = new StatementImpl(bnd.getInstance("http://www.dfki.de/isreal/cr_flip_slider_down.owl#self"), type, down);
			List<Statement> ins = new ArrayList<Statement>();
			ins.add(in);
			
			Statement out = new StatementImpl(bnd.getInstance("http://www.dfki.de/isreal/cr_flip_slider_down.owl#self"), type, up);
			List<Statement> outs = new ArrayList<Statement>();
			outs.add(out);
			
			((GSE) Config.getComponent(Config.getComponentNameForRole("GSE"))).insert(ins);
			((GSE) Config.getComponent(Config.getComponentNameForRole("GSE"))).remove(outs);
			
			// further checks
			services_to_check.add("http://www.dfki.de/isreal/cr_flip_slider_up.owl#cr_flip_slider_upService");
			services_to_check.add("http://www.dfki.de/isreal/cr_carriage_to_writer.owl#cr_carriage_to_writerService");
			services_to_check.add("http://www.dfki.de/isreal/cr_pill_to_writer.owl#cr_pill_to_writerService");
			
			return new ArrayList<String>();
		} else {
			logger.error("No GlobalSE available in thread: " + cr_flip_slider_downService.class.getName());
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
