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
 * Implementation of the http://www.dfki.de/isreal/produce_blue_pill_on_workbench.owl#ProduceBluePillOnWorkbenchService
 * service. It removes the three required ingredients from the agent performing the creation process, converts
 * one of the capsules carried by the workbench to a blue pill and lets the agent carry it.
 * 
 * For simulation purpose, it waits EXEC_TIME milliseconds before changing the GSE facts.
 * 
 * @author paka
 *
 */
public class CreateBluePillOnWorkbenchService extends
		ServiceImplementationThread {
	
	private static Logger logger = Logger.getLogger(CreateBluePillOnWorkbenchService.class);
			
	private static String	CARRIES_OBJECT = "http://www.dfki.de/isreal/messemodul.owl#carriesObject";
	private static String	RDF_TYPE = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
	private static String	BLUE_PILL = "http://www.dfki.de/isreal/messemodul.owl#BluePill";
	private static String	HAS_COLOR = "http://www.dfki.de/isreal/messemodul.owl#hasColor";
	private static String	BLUE = "http://www.dfki.de/isreal/messemodul_abox.owl#BLUE";
	
	private static long		EXEC_TIME = 2000;
	
	private GSE				gse;

	public CreateBluePillOnWorkbenchService(ServiceWrapper service, BindingList b) {
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
			
			// remove ingredients from agent performing the creation process
			removes.add(new StatementImpl(
					bnd.getInstance("http://www.dfki.de/isreal/create_blue_pill_on_workbench.owl#Producer"),
					CARRIES_OBJECT, 
					bnd.getInstance("http://www.dfki.de/isreal/create_blue_pill_on_workbench.owl#Ingredient1")));
			
			removes.add(new StatementImpl(
					bnd.getInstance("http://www.dfki.de/isreal/create_blue_pill_on_workbench.owl#Producer"),
					CARRIES_OBJECT, 
					bnd.getInstance("http://www.dfki.de/isreal/create_blue_pill_on_workbench.owl#Ingredient2")));
			
			removes.add(new StatementImpl(
					bnd.getInstance("http://www.dfki.de/isreal/create_blue_pill_on_workbench.owl#Producer"),
					CARRIES_OBJECT, 
					bnd.getInstance("http://www.dfki.de/isreal/create_blue_pill_on_workbench.owl#Ingredient3")));
			
			removes.add(new StatementImpl(
					bnd.getInstance("http://www.dfki.de/isreal/create_blue_pill_on_workbench.owl#Producer"),
					CARRIES_OBJECT, 
					bnd.getInstance("http://www.dfki.de/isreal/create_blue_pill_on_workbench.owl#Ingredient4")));
			
			// remove capsule (OWL-S local) from workbench
			removes.add(new StatementImpl(
					bnd.getInstance("http://www.dfki.de/isreal/create_blue_pill_on_workbench.owl#self"),
					CARRIES_OBJECT, 
					bnd.getInstance("http://www.dfki.de/isreal/create_blue_pill_on_workbench.owl#capsule")));
			
			gse.remove(removes);
			
			List<Statement> inserts = new ArrayList<Statement>();
			
			// capsule + incredients = blue pill
			inserts.add(new StatementImpl(
					bnd.getInstance("http://www.dfki.de/isreal/create_blue_pill_on_workbench.owl#capsule"),
					RDF_TYPE, 
					BLUE_PILL));
			
			inserts.add(new StatementImpl(
					bnd.getInstance("http://www.dfki.de/isreal/create_blue_pill_on_workbench.owl#capsule"),
					HAS_COLOR, 
					BLUE));
			
			// hand over the pill to the agent
			inserts.add(new StatementImpl(
					bnd.getInstance("http://www.dfki.de/isreal/create_blue_pill_on_workbench.owl#Producer"),
					CARRIES_OBJECT, 
					bnd.getInstance("http://www.dfki.de/isreal/create_blue_pill_on_workbench.owl#capsule")));			
			
			gse.insert(inserts);
			
			return new ArrayList<String>();
		} else {
			logger.error("No GlobalSE available in thread: "
					+ CreateBluePillOnWorkbenchService.class.getName());
		}
		return new ArrayList<String>();
	}
}
