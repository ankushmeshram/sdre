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

public class cr_flip_slider_upService extends SEServiceImplementationThread {
	private static Logger logger = Logger.getLogger(cr_flip_slider_upService.class);
	

	private static String type = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
	private static String up = "http://www.dfki.de/isreal/abstract_concepts.owl#Up";
	private static String down = "http://www.dfki.de/isreal/abstract_concepts.owl#Down";
	
	public cr_flip_slider_upService(ServiceWrapper service, BindingList b) {
		super(service, b);
	}

	@Override
	protected List<String> serviceImplementation() {
		if (Config.isRoleRegistered("GSE")){
			
			// Trigger animation with the correct color.
			graphics.sendAnimationCall("slider_up", null);

			// wait for animation
			waitMillis(1000);
			
			// effects
			Statement in = new StatementImpl(bnd.getInstance("http://www.dfki.de/isreal/cr_flip_slider_up.owl#self"), type, up);
			List<Statement> ins = new ArrayList<Statement>();
			ins.add(in);
			
			Statement out = new StatementImpl(bnd.getInstance("http://www.dfki.de/isreal/cr_flip_slider_up.owl#self"), type, down);
			List<Statement> outs = new ArrayList<Statement>();
			outs.add(out);
			
			((GSE) Config.getComponent(Config.getComponentNameForRole("GSE"))).insert(ins);
			((GSE) Config.getComponent(Config.getComponentNameForRole("GSE"))).remove(outs);
			
			// further checks
			
			return new ArrayList<String>();
		} else {
			logger.error("No GlobalSE available in thread: " + cr_flip_slider_upService.class.getName());
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
