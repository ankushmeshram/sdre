package de.dfki.isreal.semantic.oms.components.impl;

import java.io.File;
import java.io.FileFilter;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.openrdf.model.URI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import com.franz.agbase.AllegroGraphException;

import de.dfki.isreal.helpers.Profiler;
import de.dfki.isreal.semantic.oms.components.SemanticReasonerPlugin;
import de.dfki.isreal.semantic.oms.components.TripleStorePlugin;
import de.dfki.isreal.subcomponents.OMSDecider;
import de.dfki.isreal.data.SPARQLDLResult;
import de.dfki.isreal.data.State;
import de.dfki.isreal.data.Statement;

import eu.larkc.core.data.BooleanInformationSet;
import eu.larkc.core.data.SetOfStatements;
import eu.larkc.core.data.VariableBinding;
import eu.larkc.core.qos.QoSInformation;
import eu.larkc.core.qos.QoSParameters;
import eu.larkc.core.query.SPARQLQuery;

public class OMSDeciderOWLAPIImpl implements OMSDecider {

	private Logger logger = Logger.getLogger(OMSDeciderOWLAPIImpl.class);

	boolean sem_r = false;
	
	TripleStorePlugin OTS = null;
	SemanticReasonerPlugin PSR = null;

	public OMSDeciderOWLAPIImpl(String loc) {
		logger.info("init OMSDeciderImpl...");
		sem_r = OMSConfig.isSemReasoningEnabled();
		if (OMSConfig.getTripleStoreSystemName().equals("owlim")) {
			OTS = new TripleStoreOWLIMImpl(loc);
		} else if (OMSConfig.getTripleStoreSystemName().equals("allegro")) {
			try {
				OTS = new TripleStoreAllegroGraphImpl(loc);
			} catch (AllegroGraphException e) {
				logger
						.error(
								"Could not instanciate TripleStoreAllegroGraphImpl.",
								e);
				e.printStackTrace();
			}
		}

		if (sem_r) {
			PSR = new SemanticReasonerFactPlusPlusImpl();
		}
	}
	
	public OMSDeciderOWLAPIImpl(String loc, int jettyPort) {
		logger.info("init OMSDeciderImpl...");
		sem_r = OMSConfig.isSemReasoningEnabled();
		if (OMSConfig.getTripleStoreSystemName().equals("owlim")) {
			OTS = new TripleStoreOWLIMImpl(loc, jettyPort);
		} else if (OMSConfig.getTripleStoreSystemName().equals("allegro")) {
			try {
				OTS = new TripleStoreAllegroGraphImpl(loc);
			} catch (AllegroGraphException e) {
				logger
						.error(
								"Could not instanciate TripleStoreAllegroGraphImpl.",
								e);
				e.printStackTrace();
			}
		}

		if (sem_r) {
			PSR = new SemanticReasonerFactPlusPlusImpl();
		}
	}

	public BooleanInformationSet sparqlAsk(SPARQLQuery theQuery,
			QoSParameters theQoSParameters) {
		return OTS.sparqlAsk(theQuery, null, null, null);
	}

	public SetOfStatements sparqlConstruct(SPARQLQuery theQuery,
			QoSParameters theQoSParameters) {
		return OTS.sparqlConstruct(theQuery, null, null, null);
	}

	public SetOfStatements sparqlDescribe(SPARQLQuery theQuery,
			QoSParameters theQoSParameters) {
		return OTS.sparqlDescribe(theQuery, null, null, null);
	}

	public VariableBinding sparqlSelect(SPARQLQuery theQuery,
			QoSParameters theQoSParameters) {
		return OTS.sparqlSelect(theQuery, null, null, null);
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
		if (sem_r) {
			return PSR.checkClassConsistency(c);
		} else {
			logger.error("OMSDeciderImpl: Semantic Reasoner not enabled.");
			return false;
		}
	}

	public boolean checkClassDisjunction(java.net.URI c1, java.net.URI c2) {
		if (sem_r) {
			return PSR.checkClassDisjunction(c1, c2);
		} else {
			logger.error("OMSDeciderImpl: Semantic Reasoner not enabled.");
			return false;
		}
	}

	public boolean checkClassEquivalence(java.net.URI c1, java.net.URI c2) {
		if (sem_r) {
			return PSR.checkClassEquivalence(c1, c2);
		} else {
			logger.error("OMSDeciderImpl: Semantic Reasoner not enabled.");
			return false;
		}
	}

	public boolean checkClassSubsumption(java.net.URI c1, java.net.URI c2) {
		if (sem_r) {
			return PSR.checkClassSubsumption(c1, c2);
		} else {
			logger.error("OMSDeciderImpl: Semantic Reasoner not enabled.");
			return false;
		}
	}

	public boolean checkKBConsistency() {
		if (sem_r) {
			return PSR.checkKBConsistency();
		} else {
			logger.error("OMSDeciderImpl: Semantic Reasoner not enabled.");
			return false;
		}
	}

	public SPARQLDLResult sparqldlProcessing(String query) {
		if (sem_r) {
			return PSR.sparqldlProcessing(query);
		} else {
			logger.error("OMSDeciderImpl: Semantic Reasoner not enabled.");
			return null;
		}
	}

	public void classify() {
		if (sem_r) {
			PSR.classify();
		} else {
			logger.error("OMSDeciderImpl: Semantic Reasoner not enabled.");
		}

	}

	public void realize() {
		if (sem_r) {
			PSR.realize();
		} else {
			logger.error("OMSDeciderImpl: Semantic Reasoner not enabled.");
		}

	}

	public void initFromFiles(List<File> ontologies) {

		OTS.initFromFiles(ontologies);
		if (sem_r) {
			PSR.initFromFiles(ontologies);
			
		}
	}

	public void insert(List<Statement> a) {
		Profiler.startMonitor(this.getClass().getName(), "insert");
		OTS.insert(a);
		if (sem_r) {
			PSR.insert(a);
		}
		Profiler.stopMonitor(this.getClass().getName(), "insert");
	}

	public void remove(List<Statement> a) {
		Profiler.startMonitor(this.getClass().getName(), "remove");
		OTS.remove(a);
		if (sem_r) {
			PSR.remove(a);
		}
		Profiler.stopMonitor(this.getClass().getName(), "remove");
	}

	public boolean synchronizePlugins() {
		// TODO
		return Boolean.TRUE;
	}

	public void update(List<Statement> al) {
		Profiler.startMonitor(this.getClass().getName(), "update");
		OTS.update(al);
		if (sem_r) {
			PSR.update(al);
		}
		Profiler.stopMonitor(this.getClass().getName(), "update");
	}

	public boolean instanceChecking(java.net.URI i, java.net.URI c) {
		if (sem_r) {
			return PSR.instanceChecking(i, c);
		} else {
			logger.error("OMSDeciderImpl: Semantic Reasoner not enabled.");
			return false;
		}
	}

	public List<Statement> listStatements() {
		if (sem_r) {
			return PSR.listStatements();
		} else {
			logger.error("OMSDeciderImpl: Semantic Reasoner not enabled.");
			return null;
		}
	}

	public List<List<Statement>> computeTopRelationalTrees(
			List<String> entities, int num, boolean props) {
		if (sem_r) {
			return PSR.computeTopRelationalTrees(entities, num, props);
		} else {
			logger.error("OMSDeciderImpl: Semantic Reasoner not enabled.");
			return null;
		}
	}

	public State getInitialStateFromKB() {
		return OTS.getInitialStateFromKB();
	}
	
	public void closeTS() {
		OTS.closeTS();
	}

	
	@Override
	public Set<OWLClass> dlEquivalentClasses(String classExp) {
		return PSR.dlEquivalentClasses(classExp);
	}

	@Override
	public Set<OWLClass> dlSubClasses(String classExp, boolean direct) {
		return PSR.dlSubClasses(classExp, direct);
	}

	@Override
	public Set<OWLClass> dlEquivalentClasses(OWLClass clas) {
		return PSR.dlEquivalentClasses(clas);
	}

	@Override
	public OWLClass getSensorForProperty(OWLClass propClass) {
		return PSR.getSensorForProperty(propClass);
	}

	@Override
	public Set<OWLClass> dlSubClasses(OWLClass clas, boolean direct) {
		return PSR.dlSubClasses(clas, direct);
	}
	
	
}
