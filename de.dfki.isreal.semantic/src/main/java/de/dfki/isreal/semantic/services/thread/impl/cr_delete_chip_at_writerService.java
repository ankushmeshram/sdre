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

public class cr_delete_chip_at_writerService extends SEServiceImplementationThread {
	private static Logger logger = Logger.getLogger(cr_delete_chip_at_writerService.class);
	

	private static String type = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
	private static String stores = "http://www.dfki.de/isreal/messemodul.owl#stores";
	
	public cr_delete_chip_at_writerService(ServiceWrapper service, BindingList b) {
		super(service, b);
	}

	@Override
	protected List<String> serviceImplementation() {
		if (Config.isRoleRegistered("GSE")){
			
			Statement out = new StatementImpl(bnd.getInstance("http://www.dfki.de/isreal/cr_delete_chip_at_writer.owl#self"), stores, bnd.getInstance("http://www.dfki.de/isreal/cr_delete_chip_at_writer.owl#data"));
			List<Statement> outs = new ArrayList<Statement>();
			outs.add(out);
			
			((GSE) Config.getComponent(Config.getComponentNameForRole("GSE"))).remove(outs);
			
			// no further checks
			
			
			return new ArrayList<String>();
		} else {
			logger.error("No GlobalSE available in thread: " + cr_delete_chip_at_writerService.class.getName());
		}
		return new ArrayList<String>();
	}

}
