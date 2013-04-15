package de.dfki.isreal.semantic.oms.components.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.dfki.isreal.helpers.Profiler;
import de.dfki.isreal.data.PlannerOutput;
import de.dfki.isreal.data.State;
import de.dfki.isreal.helpers.StatementHelpers;
import de.dfki.isreal.main.Config;
import de.dfki.isreal.network.callerfactories.DemonstratorCallerFactory;
import de.dfki.isreal.network.protos.ExchangeDataProtos.DemonstratorMessage;
import de.dfki.isreal.semantic.planner.OWLSXPlanWrapperImpl;
import de.dfki.isreal.semantic.planner.PlannerWrapper;
import de.dfki.isreal.subcomponents.PlanningEnvironment;

public class PlanningEnvironmentImpl implements PlanningEnvironment {

	private Logger logger = Logger.getLogger(PlanningEnvironmentImpl.class);
	
	private DemonstratorCallerFactory fac = new DemonstratorCallerFactory();
	
	private PlannerWrapper converter;
	private String name = "plan";
	private String serv_path = OMSConfig.getResourcePath() + "services" + File.separator;
	private String ex_path = OMSConfig.getPlannerExecutionPath() + File.separator;;
	private String tmp_path = OMSConfig.getTmpPath() + File.separator;;
	
	
	public PlanningEnvironmentImpl(){
		converter = new OWLSXPlanWrapperImpl(name, tmp_path);
	}
	
	/**
	 * Deprecated should not be used.
	 * 
	 * TODO do not read services from a txt file.
	 * @deprecated
	 */
	public PlannerOutput invokePlanner(State init, State goal) {
		
		ArrayList<String> services = new ArrayList<String>();
		File serv = new File(serv_path + "services.txt");
		
		String line;
		int count = 0;
		try {
			FileReader inputstream = new FileReader(serv);
			logger.info("ServicePath: " + serv);
			BufferedReader in = new BufferedReader(inputstream);
			while ((line = in.readLine()) != null) {
				count++;
				// URI uri = new URI(line);
				logger.info("Adding Service: " + line);// + uri);
				services.add(line);// uri);
			}
			inputstream.close();
			in.close();
		} catch (Exception ee) {
			logger.error("unable to load service-file.", ee);
			ee.printStackTrace();
			return null;
		}
		
		return invokePlanner(init, goal, services);
	}
	
	public PlannerOutput invokePlanner(State init, State goal, List<String> services) {
		fac.sendMessage("Invoke Service Composition Planner", DemonstratorMessage.Component.LSE.getNumber(), DemonstratorMessage.DMsgType.PLANNING.getNumber(), false);
		Profiler.startMonitor(this.getClass().getName(), "invokePlanner");
				
		converter.addToInitialState(init.getOntology());
		fac.sendMessage("Add initial state...", DemonstratorMessage.Component.LSE.getNumber(), DemonstratorMessage.DMsgType.PLANNING.getNumber(), false);
		converter.addToGoalState(goal.getOntology());
		fac.sendMessage("Add goal state...", DemonstratorMessage.Component.LSE.getNumber(), DemonstratorMessage.DMsgType.PLANNING.getNumber(), false);
		
		for (String s : services){
			converter.addServices(s);
		}
		fac.sendMessage("Add services...", DemonstratorMessage.Component.LSE.getNumber(), DemonstratorMessage.DMsgType.PLANNING.getNumber(), false);
		
		
		String os = System.getProperty("os.name");
		if(os.equals("Windows XP")){
			logger.info("Trying to start XPlan in "+ os + " Version...");
			fac.sendMessage("Process...", DemonstratorMessage.Component.LSE.getNumber(), DemonstratorMessage.DMsgType.PLANNING.getNumber(), false);
			PlannerOutput out = converter.invokePlanner(ex_path);
			Profiler.stopMonitor(this.getClass().getName(), "invokePlanner");
			String msg = "Plan produced:\n[";
			for (int i=0; i<out.getPlanSize(); i++){
				msg = msg + " " + StatementHelpers.getLocalnameFromURI(out.getService(i)) + " ";
			}
			msg = msg + "]";
			fac.sendMessage(msg, DemonstratorMessage.Component.LSE.getNumber(), DemonstratorMessage.DMsgType.PLANNING.getNumber(), false);
			return out;
		}else if (os.equals("Linux")){
			logger.info("No planner version available for " + os + ".");
			Profiler.stopMonitor(this.getClass().getName(), "invokePlanner");
			fac.sendMessage("No plan received...", DemonstratorMessage.Component.LSE.getNumber(), DemonstratorMessage.DMsgType.PLANNING.getNumber(), false);
			return null;
		}
		Profiler.stopMonitor(this.getClass().getName(), "invokePlanner");
		fac.sendMessage("No plan received...", DemonstratorMessage.Component.LSE.getNumber(), DemonstratorMessage.DMsgType.PLANNING.getNumber(), false);
		return null;
		
	}
	
}
