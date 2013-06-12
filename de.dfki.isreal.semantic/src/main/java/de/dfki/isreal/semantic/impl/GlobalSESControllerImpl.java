package de.dfki.isreal.semantic.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.openrdf.model.URI;
import org.openrdf.model.Value;

import de.dfki.isreal.components.GSE;
import de.dfki.isreal.data.BindingList;
import de.dfki.isreal.data.SPARQLDLResult;
import de.dfki.isreal.data.State;
import de.dfki.isreal.data.Statement;
import de.dfki.isreal.data.impl.StatementImpl;
import de.dfki.isreal.helpers.Profiler;
import de.dfki.isreal.semantic.oms.components.Cache;
import de.dfki.isreal.semantic.oms.components.impl.OMSConfig;
import de.dfki.isreal.semantic.oms.components.impl.OMSDeciderImpl;
import de.dfki.isreal.subcomponents.FactChangeEventHandler;
import de.dfki.isreal.subcomponents.OMSDecider;
import eu.larkc.core.data.BooleanInformationSet;
import eu.larkc.core.data.CloseableIterator;
import eu.larkc.core.data.SetOfStatements;
import eu.larkc.core.data.VariableBinding;
import eu.larkc.core.data.VariableBinding.Binding;
import eu.larkc.core.qos.QoSInformation;
import eu.larkc.core.qos.QoSParameters;
import eu.larkc.core.query.SPARQLQuery;
import eu.larkc.core.query.SPARQLQueryImpl;

/**
 * GlobalSESController implementation using a config file in order to initialize
 * different Plugins and a Cache for efficient agent perception queries.
 * 
 * @author stenes
 * 
 */
public class GlobalSESControllerImpl implements GSE {

	private Logger logger = Logger.getLogger(GlobalSESControllerImpl.class);
//	
//	private boolean logging = false;
//	
//	private DemonstratorCallerFactory dem = new DemonstratorCallerFactory();

	OMSDecider OMS;
//		
//	DetailedServiceRegistry SReg;
//	ServiceExecution SEx;
//	ContinousQueryRegistry QReg;

	Cache cache;
	
//	
//	/**
//	 * Currently registered fact change event handlers.
//	 */
//	Map<Integer, FactChangeEventHandler>	factChangeHandlers;

	public GlobalSESControllerImpl(String config) {
		logger.info("init GlobalSESControllerImpl...");

		OMSConfig.init(config);
		
//		
//		if (OMSConfig.isContQueriesEnabled()) {
//			QReg = new ContinousQueryRegistry();
//		}
		
//
//		if (logging){
//		dem.sendMessage("Initializing GlobalSE...",
//				DemonstratorMessage.Component.GSE.getNumber(), DemonstratorMessage.DMsgType.INIT.getNumber(), false);
//		dem.sendMessage("Initializing OMS...",
//				DemonstratorMessage.Component.GSE.getNumber(), DemonstratorMessage.DMsgType.INIT.getNumber(), false);
//		}
		
		Integer httpPort = OMSConfig.getHttpPort();
		if(httpPort != null && httpPort != 0)
			OMS = new OMSDeciderImpl(OMSConfig.getOWLIMRepository(), httpPort);
		else
			OMS = new OMSDeciderImpl(OMSConfig.getOWLIMRepository());

//		
//		if (logging){
//		dem.sendMessage(
//				"Initializing ServiceRepository...", DemonstratorMessage.Component.GSE.getNumber(),
//				DemonstratorMessage.DMsgType.INIT.getNumber(), false);
//		}
		
//		
//		if (OMSConfig.isContQueriesEnabled()){
//			SReg = new ServiceRegistryImpl(QReg);
//		} else{
//			SReg = new ServiceRegistryImpl();
//		}
		
//		
//		SEx = new ServiceExecutionThreadImpl(SReg);
//		if (OMSConfig.isCacheEnabled()) {
//			cache = new CacheImpl();
//		}

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

		
		for (String rule : OMSConfig.getRuleURIStrings()) {
			addServices(OMSConfig.getURIMappings().get(rule));
		}

		for (String service : OMSConfig.getServiceURIStrings()) {
			addServices(OMSConfig.getURIMappings().get(service));
		}

//		
//		printServices();

//		
//		if (logging){
//		dem.sendMessage("Initialized GlobalSE.",
//				DemonstratorMessage.Component.GSE.getNumber(), DemonstratorMessage.DMsgType.INIT.getNumber(), false);
//		}

//		
//		factChangeHandlers = new HashMap<Integer, FactChangeEventHandler>();
	}

	public List<Statement> getObjectFacts(String instance_uri) {
		if (OMSConfig.isCacheEnabled()) {
			// check if instance_uri in cache
			if (cache.isInCache(instance_uri)) {
				return cache.getFromCache(instance_uri);
			}

		}
		// get set of statements from triple store
		String q = "SELECT ?y ?z WHERE { <" + instance_uri + "> ?y ?z ." + " }";
		SPARQLQuery query = new SPARQLQueryImpl(q);
		VariableBinding bnd = null;
		bnd = sparqlSelect(query, null);

		// extract all statements from globalSE
		List<Statement> al = new ArrayList<Statement>();
		CloseableIterator<Binding> it = bnd.iterator();
		while (it.hasNext()) {
			try {
				Binding b = (Binding) it.next();
				List<Value> vals = b.getValues();
				String y_val = ((Value) vals.get(bnd.getVariables()
						.indexOf("y"))).stringValue();
				String z_val = ((Value) vals.get(bnd.getVariables()
						.indexOf("z"))).stringValue();
				Statement a = new StatementImpl(instance_uri, y_val, z_val);
				al.add(a);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		it.close();
		if (OMSConfig.isCacheEnabled()) {
			cache.writeToCache(instance_uri, al);
		}
		return al;
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

	public void realize() {
		OMS.realize();
	}

	public void initFromFiles(List<File> ontologies) {
		OMS.initFromFiles(ontologies);
		// inti service reg
	}

	public synchronized void insert(List<Statement> a) {
		if (OMSConfig.isCacheEnabled()) {
			for (Statement s : a) {
				if (cache.isInCache(s.getSubjectString())) {
					cache.deleteFromCache(s.getSubjectString());
				}
			}
		}
		
//		
//		if (OMSConfig.isContQueriesEnabled()) {
//			for (Statement s : a) {
//				QReg.checkStatement(s);
//			}
//		}
		
		OMS.insert(a);
		
//		if(logging) {
//			String msg = "Facts INSERTED:";
//			for(Statement s : a) {
//				String subj = s.getSubjectString();
//				String pred = s.getPredicateString();
//				String obj = s.getObjectString();
//				msg += " (" +  subj.substring(subj.indexOf('#') + 1) + " " + pred.substring(pred.indexOf('#') + 1) + " " + obj.substring(obj.indexOf('#') + 1) + ")";				
//			}
//			dem.sendMessage(msg, DemonstratorMessage.Component.GSE.getNumber(), DemonstratorMessage.DMsgType.EXECUTION.getNumber(), false);
//		}
	
//		
//		// fact change event handling
//		for(FactChangeEventHandler handler : factChangeHandlers.values()) {
//			if(handler.check(a))
//				handler.handle();
//		}
	}

	public synchronized void remove(List<Statement> a) {
		if (OMSConfig.isCacheEnabled()) {
			for (Statement s : a) {
				if (cache.isInCache(s.getSubjectString())) {
					cache.deleteFromCache(s.getSubjectString());
				}
			}
		}
		OMS.remove(a);
		
//		if(logging) {
//			String msg = "Facts REMOVED:";
//			for(Statement s : a) {
//				String subj = s.getSubjectString();
//				String pred = s.getPredicateString();
//				String obj = s.getObjectString();
//				msg += " (" +  subj.substring(subj.indexOf('#') + 1) + " " + pred.substring(pred.indexOf('#') + 1) + " " + obj.substring(obj.indexOf('#') + 1) + ")";				
//			}
//			dem.sendMessage(msg, DemonstratorMessage.Component.GSE.getNumber(), DemonstratorMessage.DMsgType.EXECUTION.getNumber(), false);
//		}
	
//		
//		// fact change event handling
//		for(FactChangeEventHandler handler : factChangeHandlers.values()) {
//			if(handler.check(a))
//				handler.handle();
//		}
	}

	public synchronized void update(List<Statement> al) {
		if (OMSConfig.isCacheEnabled()) {
			for (Statement s : al) {
				if (cache.isInCache(s.getSubjectString())) {
					cache.deleteFromCache(s.getSubjectString());
				}
			}
		}
		OMS.update(al);
		
//		if(logging) {
//			String msg = "Facts UPDATED:";
//			for(Statement s : al) {
//				String subj = s.getSubjectString();
//				String pred = s.getPredicateString();
//				String obj = s.getObjectString();
//				msg += " (" +  subj.substring(subj.indexOf('#') + 1) + " " + pred.substring(pred.indexOf('#') + 1) + " " + obj.substring(obj.indexOf('#') + 1) + ")";				
//			}
//			dem.sendMessage(msg, DemonstratorMessage.Component.GSE.getNumber(), DemonstratorMessage.DMsgType.EXECUTION.getNumber(), false);
//		}	
		
//		
//		// fact change event handling
//		for(FactChangeEventHandler handler : factChangeHandlers.values()) {
//			if(handler.check(al))
//				handler.handle();
//		}
	}

	public boolean instanceChecking(java.net.URI i, java.net.URI c) {
		return OMS.instanceChecking(i, c);
	}

	
	public void addService(String uri) {
//		
//		SReg.addService(uri);
	}

	public List<String> getAllServices() {
//
//		return SReg.getAllServices();
		return Collections.emptyList();
	}
//
//	public boolean printServices() {
//		return SReg.printServices();
//	}

	public void removeService(String uri) {
//		SReg.removeService(uri);
	}

	public void applyContextRules() {
	}

	public List<Statement> listStatements() {
		return OMS.listStatements();
	}

	public List<List<Statement>> computeTopRelationalTrees(
			List<String> entities, int num, boolean props) {
		return OMS.computeTopRelationalTrees(entities, num, props);
	}

	public State getInitialStateFromKB() {
		return OMS.getInitialStateFromKB();
	}

	
	public void addServices(String ont_uri) {
//	
//		SReg.addServices(ont_uri);
	}

	public String getServiceDescription(String service_uri) {
//	
//		return SReg.getServiceDescription(service_uri);
		return null;
	}
//
//	public List<ConditionalEffect> getServiceEffects(String service_uri) {
//		return SReg.getServiceEffects(service_uri);
//	}

	public List<String> getServiceInputs(String service_uri) {
//	
//		return SReg.getServiceInputs(service_uri);
		return Collections.emptyList();
	}
//
//	public List<String> getServiceLocals(String service_uri) {
//		return SReg.getServiceLocals(service_uri);
//	}
//
//	public List<String> getServiceOutputs(String service_uri) {
//		return SReg.getServiceOutputs(service_uri);
//	}
//
//	public List<String> getServicePreconditionExpression(String service_uri) {
//		return SReg.getServicePreconditionExpression(service_uri);
//	}

	public List<String> invokeSemanticService(String service_uri, BindingList inp_bnd) {
//	
//		return SEx.invokeSemanticService(service_uri, inp_bnd);
		return Collections.emptyList();
	}
//
//	public List<Parameter> getServiceParameters(String s_uri) {
//		return SReg.getServiceParameters(s_uri);
//	}

	@Override
	public String getProfileReport() {
		return Profiler.getReport();
	}

	@Override
	public SPARQLDLResult sparqldlProcessing(String query) {
		return OMS.sparqldlProcessing(query);
	}

	
	@Override
	public List<String> getAgentServices() {
//
//		return SReg.getAgentServices();
		return Collections.emptyList();
	}

	@Override
	public List<String> getInteractionServices() {
//		
//		return SReg.getInteractionServices();
		return Collections.emptyList();
	}

	@Override
	public List<String> getSEServices() {
//		
//		return SReg.getSEServices();
		return Collections.emptyList();
	}
//
//	public String getSelfName(String r_uri) {
//		return SReg.getSelfName(r_uri);
//	}
//
//	public String getSelfType(String r_uri) {
//		return SReg.getSelfType(r_uri);
//	}
//
//	public int getServiceType(String s_uri) {
//		return SReg.getServiceType(s_uri);
//	}
//
//	
//	public ContinousQueryRegistry getQReg(){
//		return QReg;
//	}
//
//	public ServiceWrapper getServiceInRegistry(String uri) {
//		return SReg.getServiceInRegistry(uri);
//	}

	@Override
	public HashMap<String, List<Statement>> getObjectFacts(
			List<String> instanceUris) {
		// TODO implement as SPARQL UNION
		HashMap<String, List<Statement>> map = new HashMap<String, List<Statement>>();
		for(String instanceUri : instanceUris) {
			map.put(instanceUri, getObjectFacts(instanceUri));
		}
		
		return map;
	}
	
	@Override
	public synchronized void registerFactChangeEventHandler(FactChangeEventHandler handler) {
//		
//		factChangeHandlers.put(handler.getId(), handler);
//		dem.sendMessage("Fact change perception listener ADDED:", DemonstratorMessage.Component.GSE.getNumber(), DemonstratorMessage.DMsgType.PERC.getNumber(), false);
//		dem.sendMessage(handler.toString(), DemonstratorMessage.Component.GSE.getNumber(), DemonstratorMessage.DMsgType.PERC.getNumber(), false);
	}
	
	@Override
	public synchronized void unregisterFactChangeEventHandler(int handlerId) {
		try {
//			
//			FactChangeEventHandler handler = factChangeHandlers.remove(handlerId);
//			dem.sendMessage("Fact change perception listener REMOVED:", DemonstratorMessage.Component.GSE.getNumber(), DemonstratorMessage.DMsgType.PERC.getNumber(), false);
//			dem.sendMessage(handler.toString(), DemonstratorMessage.Component.GSE.getNumber(), DemonstratorMessage.DMsgType.PERC.getNumber(), false);
		}
		catch(NullPointerException e) {
			logger.error("Handler ID " + handlerId + " not registered at GSE.", e);
//			
//			dem.sendMessage("Handler ID " + handlerId + " not registered at GSE.", DemonstratorMessage.Component.GSE.getNumber(), DemonstratorMessage.DMsgType.EXECUTION.getNumber(), false);
		}
	}

	@Override
	public void closeTS() {
		// TODO Auto-generated method stub
		
	}
}
