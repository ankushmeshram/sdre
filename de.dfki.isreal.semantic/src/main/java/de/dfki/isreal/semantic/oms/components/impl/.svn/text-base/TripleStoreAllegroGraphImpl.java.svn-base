package de.dfki.isreal.semantic.oms.components.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openrdf.model.URI;

import com.franz.agbase.AllegroGraph;
import com.franz.agbase.AllegroGraphConnection;
import com.franz.agbase.AllegroGraphException;
import com.franz.agbase.Triple;
import com.franz.agbase.TriplesIterator;
import com.franz.agbase.ValueSetIterator;

import de.dfki.isreal.helpers.Profiler;
import de.dfki.isreal.semantic.oms.components.TripleStorePlugin;
import de.dfki.isreal.data.State;
import de.dfki.isreal.data.Statement;
import de.dfki.isreal.data.impl.SetOfStatementsStateImpl;
import de.dfki.isreal.data.impl.SetOfStatementsTriplesIteratorImpl;
import de.dfki.isreal.data.impl.StatementImpl;
import de.dfki.isreal.data.impl.VariableBindingValueSetImpl;
import de.dfki.isreal.helpers.SparqlQueryHelper;
import eu.larkc.core.data.BooleanInformationSet;
import eu.larkc.core.data.BooleanInformationSetImpl;
import eu.larkc.core.data.SetOfStatements;
import eu.larkc.core.data.VariableBinding;
import eu.larkc.core.qos.QoSInformation;
import eu.larkc.core.query.SPARQLQuery;
import eu.larkc.plugin.Context;
import eu.larkc.plugin.Contract;

public class TripleStoreAllegroGraphImpl implements TripleStorePlugin {

	private static Logger logger = Logger
			.getLogger(TripleStoreAllegroGraphImpl.class);

	private static String system_name = "allegro";
	private int port = 4126;
	private String reps_loc = System.getProperty("user.dir") + "/test_reps/";

	private AllegroGraph ts = null;
	private AllegroGraphConnection ags = null;

	public TripleStoreAllegroGraphImpl(String loc) throws AllegroGraphException {
		logger.info("init TripleStoreAllegroGraphImpl...");
		init(loc);
		logger.info("-----------------------------------------------------");
		logger.info("- TripleStoreAllegroGraphImpl waits for requests... -");
		logger.info("-----------------------------------------------------");

	}

	private void init(String loc) throws AllegroGraphException {
		ags = new AllegroGraphConnection();
		try {
			ags.setPort(port);
			ags.enable();
		} catch (Exception e) {
			logger.error("Server connection problem", e);
			throw new AllegroGraphException("Server connection problem", e);
		}

		// Estimate the number of unique resources in the Lubm 50 data
		// Simple heuristic: a little more than 1/3 of the total triples
		ags.setDefaultExpectedResources(2500000);

		// Create fresh triple-store for this example.
		ts = ags.access(loc, reps_loc + system_name + File.separator);

		logger.info("Number of triples in ts: " + ts.numberOfTriples());
	}

	private void readFilesToTS(String[] onts) {
		try {
			for (String f : onts) {
				logger.info("Load file: " + f);
				ts.loadRDFXML(f);
			}
			ts.indexAllTriples();
		} catch (AllegroGraphException e) {
			logger.error("Could not load ontologies", e);
			e.printStackTrace();
		}
	}

	private void deleteTS() {
		try {
			ts.clear();
			logger.info("Number of remaining triples: " + ts.numberOfTriples());
		} catch (AllegroGraphException e) {
			logger.error("Could not clear triple store.", e);
			e.printStackTrace();
		}
	}

	@Override
	public BooleanInformationSet sparqlAsk(SPARQLQuery arg0,
			SetOfStatements arg1, Contract arg2, Context arg3) {
		Profiler.startMonitor(this.getClass().getName(), "sparqlAsk");
		BooleanInformationSet b = null;
		if (arg0.isAsk()) {
			try {
				String query = arg0.toString();
				logger.debug("SPARQL Ask: " + query);
				// Query the store and show the result
				com.franz.agbase.SPARQLQuery sq = new com.franz.agbase.SPARQLQuery();
				sq.setIncludeInferred(true);
				sq.setEngine(com.franz.agbase.SPARQLQuery.ENGINE.ALGEBRA);
				sq.setPlanner(com.franz.agbase.SPARQLQuery.PLANNER.IDENTITY);
				boolean result = sq.ask(ts, query);
				b = new BooleanInformationSetImpl(result);
			} catch (Exception e) {
				logger.error("Error while processing SPARQL Query!", e);
				e.printStackTrace();
			}
		} else {
			logger.error("Wrong SPARQL query format: Ask query expected!");
		}
		Profiler.stopMonitor(this.getClass().getName(), "sparqlAsk");
		return b;
	}

	@Override
	public SetOfStatements sparqlConstruct(SPARQLQuery arg0,
			SetOfStatements arg1, Contract arg2, Context arg3) {
		Profiler.startMonitor(this.getClass().getName(), "sparqlConstruct");
		SetOfStatements st = null;
		if (arg0.isConstruct()) {
			try {
				String query = arg0.toString();
				logger.debug("SPARQL Construct: " + query);
				com.franz.agbase.SPARQLQuery sq = new com.franz.agbase.SPARQLQuery();
				sq.setIncludeInferred(true);
				sq.setEngine(com.franz.agbase.SPARQLQuery.ENGINE.ALGEBRA);
				sq.setPlanner(com.franz.agbase.SPARQLQuery.PLANNER.IDENTITY);
				TriplesIterator cc = sq.construct(ts,query);
				st = new SetOfStatementsTriplesIteratorImpl(cc);
			} catch (Exception e) {
				logger.error("Error while processing SPARQL Query!", e);
				e.printStackTrace();
			}
		} else {
			logger.error("Wrong SPARQL query format: Construct query expected!");
		}
		Profiler.stopMonitor(this.getClass().getName(), "sparqlConstruct");
		return st;
	}

	@Override
	public SetOfStatements sparqlDescribe(SPARQLQuery arg0,
			SetOfStatements arg1, Contract arg2, Context arg3) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableBinding sparqlSelect(SPARQLQuery arg0, SetOfStatements arg1,
			Contract arg2, Context arg3) {
		Profiler.startMonitor(this.getClass().getName(), "sparqlSelect");
		VariableBinding bnd = null;
		if (arg0.isSelect()) {
			// Set up the SPARQLQuery object
			com.franz.agbase.SPARQLQuery sq = new com.franz.agbase.SPARQLQuery();
			logger.debug("SPARQL Select: " + arg0.toString());
			sq.setEngine(com.franz.agbase.SPARQLQuery.ENGINE.ALGEBRA);
			sq.setPlanner(com.franz.agbase.SPARQLQuery.PLANNER.IDENTITY);
			sq.setTripleStore(ts);
			sq.setIncludeInferred(true);
			sq.setQuery(arg0.toString());
			ValueSetIterator it = null;
			try {
				it = sq.select();
				bnd = new VariableBindingValueSetImpl(it);
			} catch (AllegroGraphException e) {
				logger.error("Could not process query.", e);
				e.printStackTrace();
			}
		} else {
			logger.error("No valid SPARQL Select query!");
		}
		Profiler.stopMonitor(this.getClass().getName(), "sparqlSelect");
		return bnd;
	}

	@Override
	public URI getIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QoSInformation getQoSInformation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initFromFiles(List<File> ontologies) {
		Profiler.startMonitor(this.getClass().getName(), "initPluginFromFiles");
		String[] onts = new String[ontologies.size()];
		for (int i = 0; i < ontologies.size(); i++) {
			onts[i] = ontologies.get(i).getAbsolutePath();
		}
		deleteTS();
		readFilesToTS(onts);
		try {
			logger.info("Number of remaining triples: " + ts.numberOfTriples());
		} catch (AllegroGraphException e) {
			e.printStackTrace();
		}
		
		
		Profiler.stopMonitor(this.getClass().getName(), "initPluginFromFiles");
	}

	@Override
	public void insert(List<Statement> a) {
		Profiler.startMonitor(this.getClass().getName(), "insert");
		try {
			for (Statement s : a) {
				ts
						.addStatement(SparqlQueryHelper.getSparqlTripleElement(s.getSubjectString()),
								SparqlQueryHelper.getSparqlTripleElement(s.getPredicateString()),
								SparqlQueryHelper.getSparqlTripleElement(s.getObjectString()));
				ts.indexNewTriples();
			}
			logger.info("Number of remaining triples: " + ts.numberOfTriples());
		} catch (AllegroGraphException e) {
			logger.error("Could not insert triples.", e);
			e.printStackTrace();
		}
		Profiler.stopMonitor(this.getClass().getName(), "insert");
	}

	@Override
	public void remove(List<Statement> a) {
		Profiler.startMonitor(this.getClass().getName(), "remove");
		try {
			for (Statement s : a) {
				ts.removeStatements(SparqlQueryHelper.getSparqlTripleElement(s.getSubjectString()),
									SparqlQueryHelper.getSparqlTripleElement(s.getPredicateString()),
									SparqlQueryHelper.getSparqlTripleElement(s.getObjectString()));
			}
			logger.info("Number of remaining triples: " + ts.numberOfTriples());
		} catch (AllegroGraphException e) {
			logger.error("Could not insert triples.", e);
			e.printStackTrace();
		}
		Profiler.stopMonitor(this.getClass().getName(), "remove");
	}

	@Override
	public void update(List<Statement> al) {
		Profiler.startMonitor(this.getClass().getName(), "update");
		
		if (al.size() > 0){
		de.dfki.isreal.data.Statement a = al.get(0);
		
		try {
			ts.removeStatements(SparqlQueryHelper.getSparqlTripleElement(a.getSubjectString()),null,null);
		} catch (AllegroGraphException e) {
			logger.error("Could not remove statements!", e);
			e.printStackTrace();
		}
		
		insert(al);
		}
		Profiler.stopMonitor(this.getClass().getName(), "update");
	}

	@Override
	public State getInitialStateFromKB() {
		Profiler.startMonitor(this.getClass().getName(), "getInitialStateFromKB");
		TriplesIterator cc = null;
		try {
			cc = ts.getStatements(false,null,null,null);
		} catch (AllegroGraphException e) {
			logger.error("Could not retrieve statements.", e);
			e.printStackTrace();
		}
		SetOfStatements set = new SetOfStatementsTriplesIteratorImpl(cc);
		State state = new SetOfStatementsStateImpl(set);
		Profiler.stopMonitor(this.getClass().getName(), "getInitialStateFromKB");
		return state;
	}
	
	public static void main(String[] args) {
		logger.info(System.getProperty("user.dir"));
		Profiler.init();

		TripleStoreAllegroGraphImpl plugin = null;
		try {
			plugin = new TripleStoreAllegroGraphImpl("TEST");
		} catch (AllegroGraphException e) {
			e.printStackTrace();
		}

		String path = "/var/www/isreal/";
		List<File> ontologies = new ArrayList<File>();
		ontologies.add(new File(path + "spatial_ontology.owl"));
		ontologies.add(new File(path + "abstract_concepts.owl"));
		ontologies.add(new File(path + "room.owl"));
		ontologies.add(new File(path + "agent.owl"));
		ontologies.add(new File(path + "messemodul.owl"));
		ontologies.add(new File(path + "messemodul_abox.owl"));
		ontologies.add(new File(path + "room_abox.owl"));

		plugin.initFromFiles(ontologies);
		
		Statement st = new StatementImpl("http://www.dfki.de/isreal/room_abox.owl#doorEF",
				"http://www.w3.org/1999/02/22-rdf-syntax-ns#type",
				"http://www.dfki.de/isreal/abstract_concepts.owl#Locked");
		
		List<Statement> sts = new ArrayList<Statement>();
		sts.add(st);
		
		plugin.remove(sts);
		
		plugin.insert(sts);
		plugin.remove(sts);
		
		plugin.update(sts);

	}


}
