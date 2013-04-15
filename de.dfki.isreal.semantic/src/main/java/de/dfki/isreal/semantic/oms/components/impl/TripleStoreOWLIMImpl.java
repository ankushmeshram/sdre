package de.dfki.isreal.semantic.oms.components.impl;

import info.aduna.iteration.CloseableIteration;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Semaphore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.openrdf.model.Graph;
import org.openrdf.model.Literal;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.GraphImpl;
import org.openrdf.model.impl.LiteralImpl;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.openrdf.model.util.GraphUtil;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.query.Binding;
import org.openrdf.query.BindingSet;
import org.openrdf.query.GraphQueryResult;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.query.algebra.evaluation.impl.EvaluationStrategyImpl;
import org.openrdf.query.impl.EmptyBindingSet;
import org.openrdf.query.parser.QueryParser;
import org.openrdf.query.parser.QueryParserUtil;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.repository.config.RepositoryConfig;
import org.openrdf.repository.config.RepositoryConfigSchema;
import org.openrdf.repository.config.RepositoryConfigUtil;
import org.openrdf.repository.manager.LocalRepositoryManager;
import org.openrdf.repository.manager.RepositoryManager;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.Rio;
import org.openrdf.rio.helpers.StatementCollector;
import org.openrdf.rio.rdfxml.util.RDFXMLPrettyWriter;

import com.ontotext.ordi.tripleset.impl.TSourceImpl;

import de.dfki.isreal.helpers.Profiler;
import de.dfki.isreal.semantic.oms.components.TripleStorePlugin;
import de.dfki.isreal.data.State;
import de.dfki.isreal.data.impl.SetOfStatementsQueryResultImpl;
import de.dfki.isreal.data.impl.SetOfStatementsStateImpl;
import de.dfki.isreal.data.impl.StatementImpl;
import de.dfki.isreal.data.impl.VariableBindingIterationImpl;
import de.dfki.isreal.data.impl.VariableBindingIteratorImpl;
import de.dfki.isreal.helpers.StatementHelpers;

import eu.larkc.core.data.BooleanInformationSet;
import eu.larkc.core.data.BooleanInformationSetImpl;
import eu.larkc.core.data.SetOfStatements;
import eu.larkc.core.data.VariableBinding;
import eu.larkc.core.qos.QoSInformation;
import eu.larkc.core.query.SPARQLQuery;
import eu.larkc.plugin.Context;
import eu.larkc.plugin.Contract;

public class TripleStoreOWLIMImpl implements TripleStorePlugin {

	private Logger logger = Logger.getLogger(TripleStoreOWLIMImpl.class);

	private static String system_name = "owlim";
	private RepositoryManager man;
	private Repository repository;
	private RepositoryConnection repositoryConn;
	protected RepositoryConfig repConfig;
	private QueryParser MyQueryParser = null;
	
	// Jetty HTTP server to provide snapshots of the RDF store
	private static Server jetty = null;
	private static Integer jettyPort = null;
	private static HandlerList	jettyHandlers = null;
	
	// Binary semaphore used to synchronize read/write access (which seems to cause problems in agent plan execution)
	private Semaphore semaphore = new Semaphore(1, true);

	public TripleStoreOWLIMImpl(String loc) {
		logger.info("init TripleStoreOWLIMImpl...");
		init(loc);
		logger.info("----------------------------------------------");
		logger.info("- TripleStoreOWLIMImpl waits for requests... -");
		logger.info("----------------------------------------------");
	}
	
	public TripleStoreOWLIMImpl(String loc, Integer jettyPort) {
		logger.info("init TripleStoreOWLIMImpl...");
		this.jettyPort = jettyPort;
		init(loc);
		logger.info("----------------------------------------------");
		logger.info("- TripleStoreOWLIMImpl waits for requests... -");
		logger.info("----------------------------------------------");
		logger.info("Snapshots available via HTTP at port " + jettyPort + "...");
	}

	public BooleanInformationSet sparqlAsk(SPARQLQuery theQuery,
			SetOfStatements theSetOfStatements, Contract contract,
			Context context) {
		// TODO before or while monitoring!?
		semaphore.acquireUninterruptibly();
		
		Profiler.startMonitor(this.getClass().getName(), "sparqlAsk");
		BooleanInformationSet b = null;
		if (theQuery.isAsk()) {
			try {
				String query = theQuery.toString();
				logger.debug("SPARQL Ask: " + query);
//				CloseableIteration<BindingSet, QueryEvaluationException> result = Evaluator
//						.evaluate(
//								MyQueryParser
//										.parseQuery(query,
//												"http://www.w3.org/2006/03/wn/wn20/schema/")
//										.getTupleExpr(), new EmptyBindingSet());
//				if (result.hasNext()) {
//					b = new BooleanInformationSetImpl(true);
//				} else {
//					b = new BooleanInformationSetImpl(false);
//				}
						
				b = new BooleanInformationSetImpl(repositoryConn.prepareBooleanQuery(QueryLanguage.SPARQL, query).evaluate());			
				
			} catch (Exception e) {
				logger.error("Error while processing SPARQL Query!", e);
				e.printStackTrace();
			}
		} else {
			logger.error("Wrong SPARQL query format: Ask query expected!");
		}
		Profiler.stopMonitor(this.getClass().getName(), "sparqlAsk");
		
		semaphore.release();
		
		return b;
	}

	public SetOfStatements sparqlConstruct(SPARQLQuery theQuery,
			SetOfStatements theSetOfStatements, Contract contract,
			Context context) {
		// TODO before or while monitoring!?
		semaphore.acquireUninterruptibly();
		
		Profiler.startMonitor(this.getClass().getName(), "sparqlConstruct");
		SetOfStatements st = null;
		if (theQuery.isConstruct()) {
			try {
				String query = theQuery.toString();
				logger.debug("SPARQL Construct: " + query);
				GraphQueryResult result = repositoryConn.prepareGraphQuery(
						QueryLanguage.SPARQL, query).evaluate();
				st = new SetOfStatementsQueryResultImpl(result);
			} catch (Exception e) {
				logger.error("Error while processing SPARQL Query!", e);
				e.printStackTrace();
			}
		} else {
			logger.error("Wrong SPARQL query format: Construct query expected!");
		}
		Profiler.stopMonitor(this.getClass().getName(), "sparqlConstruct");
		
		semaphore.release();
		
		return st;
	}

	public SetOfStatements sparqlDescribe(SPARQLQuery theQuery,
			SetOfStatements theSetOfStatements, Contract contract,
			Context context) {
		// TODO Auto-generated method stub
		return null;
	}

	public VariableBinding sparqlSelect(SPARQLQuery theQuery,
			SetOfStatements theSetOfStatements, Contract contract,
			Context context) {
		// TODO before or while monitoring!?
		semaphore.acquireUninterruptibly();
		
		Profiler.startMonitor(this.getClass().getName(), "sparqlSelect");
		VariableBinding bnd = null;
		if (theQuery.isSelect()) {
			try {
				String query = theQuery.toString();
				logger.debug("SPARQL Select: " + query);
//				CloseableIteration<BindingSet, QueryEvaluationException> result = Evaluator
//						.evaluate(
//								MyQueryParser
//										.parseQuery(query,
//												"http://www.w3.org/2006/03/wn/wn20/schema/")
//										.getTupleExpr(), new EmptyBindingSet());
//				// printResult(result);
//				bnd = new VariableBindingIterationImpl(result);
				
				
				
				TupleQueryResult result = repositoryConn.prepareTupleQuery(QueryLanguage.SPARQL, query).evaluate();
				bnd = new VariableBindingIterationImpl(result);
				
				
			} catch (Exception e) {
				logger.error("Error while processing SPARQL Query!", e);
				e.printStackTrace();
			}
		} else {
			logger.error("Wrong SPARQL query format: Select query expected!");
		}
		Profiler.stopMonitor(this.getClass().getName(), "sparqlSelect");
		
		semaphore.release();
		
		return bnd;
	}

	private void printResult(
			CloseableIteration<BindingSet, QueryEvaluationException> result) {
		try {
			while (result.hasNext()) {// while
				BindingSet tuple = (BindingSet) result.next();
				logger.info(tuple.toString());
				for (Iterator<Binding> iter = tuple.iterator(); iter.hasNext();) {
					Binding b = iter.next();
					System.out.print(b.getName() + "=" + b.getValue());
				}
				logger.info("");
			}
			logger.info("");

		} catch (QueryEvaluationException ex) {
			throw new RuntimeException(ex);
		}
	}

	public URI getIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}

	public QoSInformation getQoSInformation() {
		// TODO Auto-generated method stub
		return null;
	}

	public void initFromFiles(List<File> ontologies) {
		// TODO before or while monitoring!?
		semaphore.acquireUninterruptibly();
		
		Profiler.startMonitor(this.getClass().getName(), "initPluginFromFiles");
		File[] onts = new File[ontologies.size()];
		for (int i = 0; i < ontologies.size(); i++) {
			onts[i] = ontologies.get(i);
		}
		deleteTS();
		readFilesToTS(onts);
		Profiler.stopMonitor(this.getClass().getName(), "initPluginFromFiles");
		
		semaphore.release();
	}

	public void insert(List<de.dfki.isreal.data.Statement> al) {
		// TODO before or while monitoring!?
		semaphore.acquireUninterruptibly();
		
		Profiler.startMonitor(this.getClass().getName(), "insert");
		ValueFactory fac = repositoryConn.getValueFactory();

		for (de.dfki.isreal.data.Statement a : al) {

			logger.info("Inserting " + a.stringValue());
			try {
				repositoryConn.add((Statement) a);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Error while processing insert!", e);
			}
		}
		try {
			repositoryConn.commit();
		} catch (RepositoryException e) {
			e.printStackTrace();
			logger.error("Error while processing insert!", e);
		}
		Profiler.stopMonitor(this.getClass().getName(), "insert");
		
		semaphore.release();
	}

	public void remove(List<de.dfki.isreal.data.Statement> al) {
		// TODO before or while monitoring!?
		semaphore.acquireUninterruptibly();
		
		Profiler.startMonitor(this.getClass().getName(), "remove");
		ValueFactory fac = repositoryConn.getValueFactory();

		for (de.dfki.isreal.data.Statement a : al) {

			logger.info("Removing " + a.stringValue());
			try {
				repositoryConn.remove((Statement) a);
				// special handling for symmetric properties here!
				if(repositoryConn.hasStatement(fac.createURI(a.getPredicateString()), fac.createURI("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"), fac.createURI("http://www.w3.org/2002/07/owl#SymmetricProperty"), false)) {
					//if a.pred = symmProperty also remove (a.obj a.pred a.subj)
					de.dfki.isreal.data.Statement b = new StatementImpl(a.getObjectString(), a.getPredicateString(), a.getSubjectString());
					logger.info("  SymmetricProperty --> also remove " + b.stringValue());
					repositoryConn.remove((Statement) b);
				}			
				
			} catch (Exception e) {
				logger.error("Error while processing remove!", e);
				e.printStackTrace();
			}
		}
		try {
			repositoryConn.commit();
		} catch (RepositoryException e) {
			logger.error("Error while processing remove!", e);
			e.printStackTrace();
		}
		Profiler.stopMonitor(this.getClass().getName(), "remove");
		
		semaphore.release();
	}

	/**
	 * Assuming that an update is always an update on the object of the triple.
	 * Therefore this method deletes all triples, where subject of the first
	 * given Axiom corresponds and adds the new axiom with different object.
	 * 
	 */
	public void update(List<de.dfki.isreal.data.Statement> al) {
		// TODO before or while monitoring!?
		semaphore.acquireUninterruptibly();
		
		Profiler.startMonitor(this.getClass().getName(), "update");
		ValueFactory fac = repositoryConn.getValueFactory();

		if (al.size() > 0) {
			de.dfki.isreal.data.Statement a = al.get(0);

			URI subj = fac.createURI(a.getSubjectString());

			try {
				RepositoryResult<Statement> iter = repositoryConn
						.getStatements(subj, null, null, true);
				while (iter.hasNext()) {
					Statement r = iter.next();
					repositoryConn.remove(r);
					// special handling for symmetric properties here!
					if(repositoryConn.hasStatement(r.getPredicate(), fac.createURI("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"), fac.createURI("http://www.w3.org/2002/07/owl#SymmetricProperty"), false)) {
						//if r.pred = symmProperty also remove (r.obj r.pred r.subj)
						Statement b = fac.createStatement(fac.createURI(r.getObject().stringValue()), r.getPredicate(), r.getSubject());
						logger.info("  SymmetricProperty --> also remove " + b.toString());
						repositoryConn.remove((Statement) b);
					}		
				}

				for (de.dfki.isreal.data.Statement t : al) {
					repositoryConn.add((Statement) t);
				}
				repositoryConn.commit();
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}
		Profiler.stopMonitor(this.getClass().getName(), "update");
		
		semaphore.release();
	}

	public State getInitialStateFromKB() {
		// TODO before or while monitoring!?
		semaphore.acquireUninterruptibly();
		
		Profiler.startMonitor(this.getClass().getName(),"getInitialStateFromKB");
		String query = "CONSTRUCT" + " { ?x ?y ?z } " + "WHERE { ?x ?y ?z }";
		GraphQueryResult result = null;
		try {
			result = repositoryConn.prepareGraphQuery(QueryLanguage.SPARQL,
					query).evaluate();
		} catch (QueryEvaluationException e) {
			logger.error("Error while computing initial state!", e);
			e.printStackTrace();
		} catch (RepositoryException e) {
			logger.error("Error while computing initial state!", e);
			e.printStackTrace();
		} catch (MalformedQueryException e) {
			logger.error("Error while computing initial state!", e);
			e.printStackTrace();
		}
		SetOfStatements st = new SetOfStatementsQueryResultImpl(result);
		State s = new SetOfStatementsStateImpl(st);
		Profiler.stopMonitor(this.getClass().getName(), "getInitialStateFromKB");
		
		semaphore.release();
		
		return s;
	}

	private void init(String loc) {
		// String config_file_str = "config/owlim.ttl";
		String config_file_str = OMSConfig.getTripleStoreConfigPath();

		logger.info("\n===== Initialize TripleStoreSystem (OWLIM) =========\n");

		// access the config file(XML)
		File config_file = new File(config_file_str);
		logger.info("using " + config_file.getAbsolutePath());

		MyQueryParser = QueryParserUtil.createParser(QueryLanguage.SPARQL);

		try {
			man = new LocalRepositoryManager(new File("./test_reps/"
					+ system_name + File.separator + loc));
			man.initialize();
			Repository systemRepo = man.getSystemRepository();
			ValueFactory vf = systemRepo.getValueFactory();

			Graph graph = new GraphImpl(vf);

			RDFParser rdfParser = Rio.createParser(RDFFormat.TURTLE, vf);

			rdfParser.setRDFHandler(new StatementCollector(graph));

			rdfParser.parse(new FileReader(config_file),
					RepositoryConfigSchema.NAMESPACE);
			Resource repositoryNode = GraphUtil.getUniqueSubject(graph,
					RDF.TYPE, RepositoryConfigSchema.REPOSITORY);
			GraphUtil.setUniqueObject(graph, repositoryNode, new URIImpl(
					"http://www.openrdf.org/config/repository#repositoryID"),
					new LiteralImpl(loc));

			RepositoryConfig repConfig = RepositoryConfig.create(graph,
					repositoryNode);

			repConfig.validate();

			RepositoryConfigUtil.updateRepositoryConfigs(systemRepo, repConfig);
			Literal _id = GraphUtil.getUniqueObjectLiteral(graph,
					repositoryNode, RepositoryConfigSchema.REPOSITORYID);
			repository = man.getRepository(_id.getLabel());
			repositoryConn = repository.getConnection();
			repositoryConn.setAutoCommit(false);

		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		
		// Jetty Web server providing the current state of the triple store content via HTTP		
		// init Jetty if not already running
		if(jettyPort != null) {
			if(jetty == null) {
				// TODO Port is fixed, but should be configurable. What about Agents and GSE running on the same machine!?
				jetty = new Server(jettyPort);
				jettyHandlers = new HandlerList();
				jetty.setHandler(jettyHandlers);
				jettyHandlers.addHandler(DefaultHTTPHandler.getInstance());
			}	
			else {
				try {
					jetty.stop();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// remove default, add specific, add default (to keep order)
			jettyHandlers.removeHandler(DefaultHTTPHandler.getInstance());
			jettyHandlers.addHandler(new OWLIMSnapshotHTTPHandler(loc, repositoryConn));
			jettyHandlers.addHandler(DefaultHTTPHandler.getInstance());
			
			DefaultHTTPHandler.addLocation(loc);
			
			try {
				jetty.start();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void deleteTS() {
		try {
			for (String i : man.getRepositoryIDs()) {
				logger.info("ID: " + i);
			}

			Long clearBegin = System.currentTimeMillis();
			repositoryConn.clear();
			repositoryConn.commit();
			Long clearEnd = System.currentTimeMillis();
			logger.info("----------------------------------------------");
			logger.info("- Repository cleared in " + (clearEnd - clearBegin)
					+ " ms.");
			logger.info("----------------------------------------------");

		} catch (Exception e) {
			logger.error("Error while deleting triple store!", e);
			e.printStackTrace();
		}

	}

	private void readFilesToTS(File[] ontologies) {
		RDFFormat format = RDFFormat.RDFXML;

		if (ontologies != null) {
			// Either dir does not exist or is not a directory

			File f;
			for (int i = 0; i < ontologies.length; i++) {
				// Get filename of file or directory
				f = ontologies[i];
				if (ontologies[i].isDirectory()) {
					continue;
				}
				logger.info("Parsing file: " + f.getAbsolutePath());
				try {
					repositoryConn.add(f, "http://example.org/owlim#", format,
							new URIImpl(f.toURI().toString()));
					repositoryConn.commit();
				} catch (Exception e) {
					logger.error("Error while reading files to triple store!",
							e);
					e.printStackTrace();
				}
				logger.info("----------------------------------------------");
				try {
					logger.info("- files loaded... " + repositoryConn.size());
				} catch (RepositoryException e) {
					logger.error("Error while reading files to triple store!",
							e);
					e.printStackTrace();
				}
				logger.info("----------------------------------------------");

			}
		}

	}

}
