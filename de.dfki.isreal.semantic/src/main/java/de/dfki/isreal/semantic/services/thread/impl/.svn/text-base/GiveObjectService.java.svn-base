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
import de.dfki.isreal.semantic.services.ServiceImplementationThread;

/**
 * Implementation of the http://www.dfki.de/isreal/give_object.owl#GiveObjectService
 * service. It removes the specified object from the agent (not carriesObject) and adds it to the target
 * agent.
 * 
 * TODO: Please note: currently, the target agent can not reject this process. Only the facts in the GSE are
 * changed. This requires concepts for services provided by agents.
 * 
 * For simulation purpose, it waits EXEC_TIME milliseconds before changing the GSE facts.
 * 
 * @author paka
 *
 */
public class GiveObjectService extends
		ServiceImplementationThread {
	
	private static Logger logger = Logger.getLogger(GiveObjectService.class);
			
	private static String	CARRIES_OBJECT = "http://www.dfki.de/isreal/messemodul.owl#carriesObject";
	
	private static long		EXEC_TIME = 1000;
	
	private GSE				gse;

	public GiveObjectService(ServiceWrapper service, BindingList b) {
		super(service, b);
		
		gse = (GSE) Config.getComponent(Config.getComponentNameForRole("GSE"));
	}
	
	@Override
	protected List<String> serviceImplementation() {
		if (Config.isRoleRegistered("GSE")) {
			// wait...
			try {
				Thread.sleep(EXEC_TIME);
			}
			catch(InterruptedException e) {
				logger.error(e);
			}
			
			// Change GSE as Effect
			List<Statement> removes = new ArrayList<Statement>();
			
			// remove object from agent
			removes.add(new StatementImpl(
					bnd.getInstance("http://www.dfki.de/isreal/give_object.owl#self"),
					CARRIES_OBJECT, 
					bnd.getInstance("http://www.dfki.de/isreal/give_object.owl#Object")));
						
			gse.remove(removes);
			
			List<Statement> inserts = new ArrayList<Statement>();
						
			// add object to target agent inventory
			inserts.add(new StatementImpl(
					bnd.getInstance("http://www.dfki.de/isreal/give_object.owl#Target"),
					CARRIES_OBJECT, 
					bnd.getInstance("http://www.dfki.de/isreal/give_object.owl#Object")));			
			
			gse.insert(inserts);
			
			return new ArrayList<String>();
		} else {
			logger.error("No GlobalSE available in thread: "
					+ GiveObjectService.class.getName());
		}
		return new ArrayList<String>();
	}
}
