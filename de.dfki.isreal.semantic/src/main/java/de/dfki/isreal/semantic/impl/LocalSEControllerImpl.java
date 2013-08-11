package de.dfki.isreal.semantic.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.openrdf.model.URI;
import org.semanticweb.owlapi.model.OWLClass;

import de.dfki.isreal.subcomponents.LSE;
import de.dfki.isreal.subcomponents.OMSDecider;
import de.dfki.isreal.data.Parameter;
import de.dfki.isreal.data.PlannerOutput;
import de.dfki.isreal.data.SPARQLDLResult;
import de.dfki.isreal.data.ServiceWrapper;
import de.dfki.isreal.data.State;
import de.dfki.isreal.data.Statement;
import de.dfki.isreal.data.impl.ConditionalEffect;
import de.dfki.isreal.helpers.Profiler;
import de.dfki.isreal.network.callerfactories.DemonstratorCallerFactory;
import de.dfki.isreal.network.protos.ExchangeDataProtos.DemonstratorMessage;
import de.dfki.isreal.network.socket.SocketRegistry;
import de.dfki.isreal.semantic.oms.components.impl.OMSConfig;
import de.dfki.isreal.semantic.oms.components.impl.OMSDeciderImpl;
import de.dfki.isreal.semantic.oms.components.impl.PlanningEnvironmentImpl;
import de.dfki.isreal.semantic.services.ServiceRegistryCaching;
import de.dfki.isreal.semantic.services.ServiceRegistryImpl;
import de.dfki.isreal.subcomponents.PlanningEnvironment;
import eu.larkc.core.data.BooleanInformationSet;
import eu.larkc.core.data.SetOfStatements;
import eu.larkc.core.data.VariableBinding;
import eu.larkc.core.qos.QoSInformation;
import eu.larkc.core.qos.QoSParameters;
import eu.larkc.core.query.SPARQLQuery;

public class LocalSEControllerImpl implements LSE {
	
	private final Logger logger = Logger.getLogger(LocalSEControllerImpl.class);
	private DemonstratorCallerFactory fac = new DemonstratorCallerFactory();
	private String instance;
	
	private OMSDecider OMS;
	private ServiceRegistryCaching SReg;
	private PlanningEnvironment PEnv;
	
	public LocalSEControllerImpl(String agent_id, String inst, String path){	
		Profiler.init();
		OMSConfig.init(path);
		
		instance = inst;
		
		if(OMSConfig.isLogging()){
			fac.sendAgentMessage("Initializing LocalSE...", DemonstratorMessage.Component.LSE.getNumber(), DemonstratorMessage.DMsgType.INIT.getNumber(), instance , false);
			fac.sendAgentMessage("Initializing OMS...", DemonstratorMessage.Component.LSE.getNumber(), DemonstratorMessage.DMsgType.INIT.getNumber(), instance, false);
		}
		
		String jettyPort = System.getProperty("agents.lse.httpport");
		if(jettyPort == null)
			OMS = new OMSDeciderImpl(agent_id);
		else
			OMS = new OMSDeciderImpl(agent_id, Integer.parseInt(jettyPort));
		if(OMSConfig.isLogging()){
			fac.sendAgentMessage("Initializing ServiceRepository...", DemonstratorMessage.Component.LSE.getNumber(), DemonstratorMessage.DMsgType.INIT.getNumber(), instance, false);
		}
		SReg = new ServiceRegistryImpl();
		// TODO planning environment should use URI mappings instead of abstract URI's -> OMSConfig.getURIMappings() should be used inside the planner implementation
		PEnv = new PlanningEnvironmentImpl();

		ArrayList<File> onts = new ArrayList<File>();
		for (String ont : OMSConfig.getImportURIStrings()){
			File f = new File(OMSConfig.getURIMappings().get(ont));
			onts.add(f);
		}
		for (String ont : OMSConfig.getOntologyURIStrings()){
			File f = new File(OMSConfig.getURIMappings().get(ont));
			onts.add(f);
		}
		initFromFiles(onts);

		// prepare (cache) services that are contained in mappings first
		for(String sFiles : OMSConfig.getURIMappings().values()) {
			SReg.prepareServices(sFiles);
		}
		
		for (String rule : OMSConfig.getRuleURIStrings()) {
			addServices(OMSConfig.getURIMappings().get(rule));
		}

		for (String service : OMSConfig.getServiceURIStrings()) {
			addServices(OMSConfig.getURIMappings().get(service));
		}

		printServices();
		
		if(OMSConfig.isLogging()){
			fac.sendAgentMessage("Initialized LocalSE.", DemonstratorMessage.Component.LSE.getNumber(), DemonstratorMessage.DMsgType.INIT.getNumber(), instance, false);
		}
	}


	public BooleanInformationSet sparqlAsk(SPARQLQuery theQuery,
			QoSParameters theQoSParameters) {
		return OMS.sparqlAsk(theQuery, theQoSParameters);
	}

	 
	public SetOfStatements sparqlConstruct(SPARQLQuery theQuery,
			QoSParameters theQoSParameters) {
		return OMS.sparqlConstruct(theQuery, theQoSParameters);
	}

	 
	public SetOfStatements sparqlDescribe(SPARQLQuery theQuery,
			QoSParameters theQoSParameters) {
		return OMS.sparqlDescribe(theQuery, theQoSParameters);
	}

	 
	public VariableBinding sparqlSelect(SPARQLQuery theQuery,
			QoSParameters theQoSParameters) {
		return OMS.sparqlSelect(theQuery, theQoSParameters);
	}

	 
	public URI getIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}

	 
	public QoSInformation getQoSInformation() {
		// TODO Auto-generated method stub
		return null;
	}

	 
	public boolean checkClassConsistency(java.net.URI c) {
		return OMS.checkClassConsistency(c);
	}

	 
	public boolean checkClassDisjunction(java.net.URI c1, java.net.URI c2) {
		return OMS.checkClassDisjunction(c1, c2);
	}

	 
	public boolean checkClassEquivalence(java.net.URI c1, java.net.URI c2) {
		return OMS.checkClassEquivalence(c1, c2);
	}

	 
	public boolean checkClassSubsumption(java.net.URI c1, java.net.URI c2) {
		return OMS.checkClassSubsumption(c1, c2);
	}

	 
	public boolean checkKBConsistency() {
		return OMS.checkKBConsistency();
	}

	 
	public void classify() {
		OMS.classify();
		
	}

	 
	public boolean instanceChecking(java.net.URI i, java.net.URI c) {
		return OMS.instanceChecking(i, c);
	}

	 
	public void realize() {
		OMS.realize();
		
	}

	 
	public void initFromFiles(List<File> ontologies) {
		OMS.initFromFiles(ontologies);
	}

	 
	public synchronized void insert(List<Statement> a) {
		OMS.insert(a);
		
	}

	 
	public synchronized void remove(List<Statement> a) {
		OMS.remove(a);
		
	}

	 
	public synchronized void update(List<Statement> al) {
		OMS.update(al);
	}

	 
	public boolean addService(String uri) {
		return SReg.addService(uri);
		
	}

	 
	public List<String> getAllServices() {
		return SReg.getAllServices();
	}

	 
	public boolean printServices() {
		return SReg.printServices();
		
	}

	 
	public void removeService(String uri) {
		SReg.removeService(uri);		
	}

	 
	public List<Statement> listStatements() {
		return OMS.listStatements();
	}

	 
	public List<List<Statement>> computeTopRelationalTrees(List<String> entities, int num, boolean props) {
		return OMS.computeTopRelationalTrees(entities, num, props);
	}

	 
	public PlannerOutput invokePlanner(State init, State goal) {
		return PEnv.invokePlanner(init, goal);		
	}

	 
	public State getInitialStateFromKB() {
		return OMS.getInitialStateFromKB();
	}
	 
	public void addServices(String ont_uri) {
		SReg.addServices(ont_uri);
	}
	
	 
	public String getServiceDescription(String service_uri) {
		return SReg.getServiceDescription(service_uri);
	}

	 
	public List<ConditionalEffect> getServiceEffects(String service_uri) {
		return SReg.getServiceEffects(service_uri);
	}

	 
	public List<String> getServiceInputs(String service_uri) {
		return SReg.getServiceInputs(service_uri);
	}

	 
	public List<String> getServiceLocals(String service_uri) {
		return SReg.getServiceLocals(service_uri);
	}

	 
	public List<String> getServiceOutputs(String service_uri) {
		return SReg.getServiceOutputs(service_uri);
	}

	 
	public List<String> getServicePreconditionExpression(String service_uri) {
		return SReg.getServicePreconditionExpression(service_uri);
	}


	public String getSelfType(String r_uri) {
		return SReg.getSelfType(r_uri);
	}
	
	public String getSelfName(String r_uri) {
		return SReg.getSelfName(r_uri);
	}


	public List<Parameter> getServiceParameters(String s_uri) {
		return SReg.getServiceParameters(s_uri);
	}

	public String getLocalSEProfileReport(){
		return Profiler.getReport();
	}
	
	@Override
	public SPARQLDLResult sparqldlProcessing(String query) {
		return OMS.sparqldlProcessing(query);
	}


	@Override
	public List<String> getAgentServices() {
		return SReg.getAgentServices();
	}


	@Override
	public List<String> getInteractionServices() {
		return SReg.getInteractionServices();
	}


	@Override
	public List<String> getSEServices() {
		return SReg.getSEServices();
	}


	public int getServiceType(String s_uri) {
		return SReg.getServiceType(s_uri);
	}


	@Override
	public PlannerOutput invokePlanner(State init, State goal,
			List<String> services) {
		return PEnv.invokePlanner(init, goal, services);
	}


	public ServiceWrapper getServiceInRegistry(String uri) {
		
		return SReg.getServiceInRegistry(uri);
	}


	@Override
	public String getProfileReport() {
		return Profiler.getReport();
	}
	
	/**
	 * For testing...
	 * @param args
	 */
	public static void main(String[] args) {
		
		SocketRegistry.init();
		
		String path = "/home/stenes/workspace/de.dfki.isreal.main/setups/mm_room_ontologies.isrealomsconfig";
		new LocalSEControllerImpl("NANCY", "http://www.dfki.de/isreal/room_abox.owl#NANCY" , path);
	}


	@Override
	public void closeTS() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public Set<OWLClass> dlEquivalentClasses(String classExp) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Set<OWLClass> dlSubClasses(String classExp, boolean direct) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Set<OWLClass> dlEquivalentClasses(OWLClass clas) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Set<OWLClass> dlSubClasses(OWLClass clas, boolean direct) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public OWLClass getSensorForProperty(OWLClass propClass) {
		// TODO Auto-generated method stub
		return null;
	}

}
