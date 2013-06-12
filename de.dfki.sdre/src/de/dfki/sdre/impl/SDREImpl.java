package de.dfki.sdre.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.openrdf.model.Value;

import de.dfki.isreal.data.SPARQLDLResult;
import de.dfki.isreal.data.State;
import de.dfki.isreal.data.Statement;
import de.dfki.isreal.data.impl.StatementImpl;
import de.dfki.isreal.helpers.Profiler;
import de.dfki.isreal.semantic.oms.components.Cache;
import de.dfki.isreal.semantic.oms.components.impl.CacheImpl;
import de.dfki.isreal.semantic.oms.components.impl.OMSConfig;
import de.dfki.isreal.semantic.oms.components.impl.OMSDeciderOWLAPIImpl;
import de.dfki.isreal.subcomponents.OMSDecider;
import eu.larkc.core.data.BooleanInformationSet;
import eu.larkc.core.data.CloseableIterator;
import eu.larkc.core.data.SetOfStatements;
import eu.larkc.core.data.VariableBinding;
import eu.larkc.core.data.VariableBinding.Binding;
import eu.larkc.core.qos.QoSParameters;
import eu.larkc.core.query.SPARQLQuery;
import eu.larkc.core.query.SPARQLQueryImpl;

public class SDREImpl {
	private Logger logger = Logger.getLogger(SDREImpl.class);
	OMSDecider OMS;
	Cache cache;

	public SDREImpl(String config) {
		logger.info("Init SDREImpl...");
		
		OMSConfig.init(config);
		
		Integer httpPort = OMSConfig.getHttpPort();
		if(httpPort != null && httpPort != 0)
			OMS = new OMSDeciderOWLAPIImpl(OMSConfig.getOWLIMRepository(), httpPort);
		else
			OMS = new OMSDeciderOWLAPIImpl(OMSConfig.getOWLIMRepository());
		
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
		
		cache = new CacheImpl();
	}
	
	public void initFromFiles(List<File> ontologies) {
		OMS.initFromFiles(ontologies);
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
		System.out.println(theQuery.toString());
		return OMS.sparqlSelect(theQuery, theQoSParameters);
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
	
	public synchronized void insert(List<Statement> a) {
		if (OMSConfig.isCacheEnabled()) {
			for (Statement s : a) {
				if (cache.isInCache(s.getSubjectString())) {
					cache.deleteFromCache(s.getSubjectString());
				}
			}
		}
		
		OMS.insert(a);
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
	}

	public boolean instanceChecking(java.net.URI i, java.net.URI c) {
		return OMS.instanceChecking(i, c);
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
	
	public String getProfileReport() {
		return Profiler.getReport();
	}

	public SPARQLDLResult sparqldlProcessing(String query) {
		return OMS.sparqldlProcessing(query);
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

	public HashMap<String, List<Statement>> getObjectFacts(
			List<String> instanceUris) {
		// TODO implement as SPARQL UNION
		HashMap<String, List<Statement>> map = new HashMap<String, List<Statement>>();
		for(String instanceUri : instanceUris) {
			map.put(instanceUri, getObjectFacts(instanceUri));
		}
		
		return map;
	}
	
	public void closeTS() {
		OMS.closeTS();	
	}
}
