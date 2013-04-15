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

public class cr_redlamp_off2Service extends SEServiceImplementationThread {
	private static Logger logger = Logger.getLogger(cr_redlamp_off2Service.class);
	
	private static String type = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
	private static String on = "http://www.dfki.de/isreal/abstract_concepts.owl#On";
	private static String off = "http://www.dfki.de/isreal/abstract_concepts.owl#Off";
	
	public cr_redlamp_off2Service(ServiceWrapper service, BindingList b) {
		super(service, b);
	}

	@Override
	protected List<String> serviceImplementation() {
		if (Config.isRoleRegistered("GSE")){
			// Trigger animation.
			graphics.sendAnimationCall("switch_red_lamp_off", null);
			
			// TODO Change GSE as Effect since no graphical sensor script yet
			Statement in = new StatementImpl(bnd.getInstance("http://www.dfki.de/isreal/cr_redlamp_off2.owl#self"), type, off);
			List<Statement> ins = new ArrayList<Statement>();
			ins.add(in);
			
			Statement out = new StatementImpl(bnd.getInstance("http://www.dfki.de/isreal/cr_redlamp_off2.owl#self"), type, on);
			List<Statement> outs = new ArrayList<Statement>();
			outs.add(out);
			
			((GSE) Config.getComponent(Config.getComponentNameForRole("GSE"))).insert(ins);
			((GSE) Config.getComponent(Config.getComponentNameForRole("GSE"))).remove(outs);
			
			services_to_check
				.add("http://www.dfki.de/isreal/cr_greenlamp_on.owl#cr_greenlamp_onService");
			
			return new ArrayList<String>();
		} else {
			logger.error("No GlobalSE available in thread: " + cr_redlamp_off2Service.class.getName());
		}
		return new ArrayList<String>();
	}

}
