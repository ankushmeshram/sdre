package de.dfki.isreal.semantic.oms.components.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

import naga.steinertrees.queries.Entity;
import naga.steinertrees.queries.Fact;
import naga.steinertrees.queries.Relation;
import naga.steinertrees.queries.ResultGraph;
import naga.steinertrees.queries.STARfromMM;

import org.apache.log4j.Logger;
import org.mindswap.pellet.PelletOptions;
import org.mindswap.pellet.jena.PelletInfGraph;
import org.mindswap.pellet.jena.PelletReasonerFactory;
import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyCreationException;
import org.semanticweb.owl.model.OWLOntologyManager;

import com.clarkparsia.pellet.sparqldl.jena.SparqlDLExecutionFactory;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntDocumentManager;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

import de.dfki.isreal.helpers.Profiler;
import de.dfki.isreal.semantic.oms.components.SemanticReasonerPlugin;
import de.dfki.isreal.data.SPARQLDLResult;
import de.dfki.isreal.data.State;
import de.dfki.isreal.data.impl.OWLStateImpl;
import de.dfki.isreal.data.impl.SPARQLDLResultImpl;
import de.dfki.isreal.data.impl.StatementImpl;
import de.dfki.isreal.helpers.SparqlQueryHelper;
import de.dfki.isreal.semantic.oms.helpers.URIFilter;
import de.dfki.isreal.semantic.sparqldl.SPARQL_DL_Parser;
import de.dfki.isreal.semantic.sparqldl.SPARQL_DL_Parser_Impl;
import de.dfki.isreal.semantic.sparqldl.SPARQL_DL_Transformation;
import de.dfki.isreal.semantic.sparqldl.SPARQL_DL_Transformation_Impl;
import de.dfki.isreal.semantic.sparqldl.Token;
import eu.larkc.core.query.SPARQLQuery;

public class SemanticReasonerPelletImpl implements SemanticReasonerPlugin {

	private Logger logger = Logger.getLogger(SemanticReasonerPelletImpl.class);

	public OntModel model = null;
	public URIFilter filter = null;

	public SemanticReasonerPelletImpl() {
		model = ModelFactory.createOntologyModel();
		PelletOptions.SILENT_UNDEFINED_ENTITY_HANDLING = false;
		filter = new URIFilter("http://www.dfki.de/isreal");

	}

	public boolean checkClassConsistency(URI c) {
		Profiler.startMonitor(this.getClass().getName(),
				"checkClassConsistency");
		logger.info("Class consistency (Satisfiability)");

		OntClass cl1 = model.getOntClass(c.toString());
		OntClass nothing = model
				.getOntClass("http://www.w3.org/2002/07/owl#Nothing");
		if (cl1 == null || nothing == null) {
			logger.error("Input URI's not correct!");
			Profiler.stopMonitor(this.getClass().getName(),
					"checkClassConsistency");
			return false;
		}
		boolean res = !(cl1.hasEquivalentClass(nothing));
		Profiler.stopMonitor(this.getClass().getName(), "checkClassConsistency");
		return res;
	}

	public boolean checkClassDisjunction(URI c1, URI c2) {
		Profiler.startMonitor(this.getClass().getName(),
				"checkClassDisjunction");
		logger.info("Class Disjunction");

		OntClass cl1 = model.createClass(c1.getPath());
		OntClass cl2 = model.createClass(c2.getPath());
		if (cl1 == null || cl2 == null) {
			logger.error("Input URI's not correct!");
			Profiler.stopMonitor(this.getClass().getName(),
					"checkClassDisjunction");
			return false;
		}
		boolean res = cl1.isDisjointWith(cl2);
		Profiler.stopMonitor(this.getClass().getName(), "checkClassDisjunction");
		return res;
	}

	public boolean checkClassEquivalence(URI c1, URI c2) {
		Profiler.startMonitor(this.getClass().getName(),
				"checkClassEquivalence");
		logger.info("Class Equivalence");

		OntClass cl1 = model.getOntClass(c1.toString());
		OntClass cl2 = model.getOntClass(c2.toString());
		if (cl1 == null || cl2 == null) {
			logger.error("Input URI's not correct!");
			Profiler.stopMonitor(this.getClass().getName(),
					"checkClassEquivalence");
			return false;
		}
		boolean res = cl1.hasEquivalentClass(cl2);
		Profiler.stopMonitor(this.getClass().getName(), "checkClassEquivalence");
		return res;
	}

	public boolean checkClassSubsumption(URI c1, URI c2) {
		Profiler.startMonitor(this.getClass().getName(),
				"checkClassSubsumption");
		logger.info("Class Subsumption");

		OntClass cl1 = model.getOntClass(c1.toString());
		OntClass cl2 = model.getOntClass(c2.toString());
		if (cl1 == null || cl2 == null) {
			logger.error("Input URI's not correct!");
			Profiler.stopMonitor(this.getClass().getName(),
					"checkClassSubsumption");
			return false;
		}

		boolean res = cl2.hasSubClass(cl1);
		Profiler.stopMonitor(this.getClass().getName(), "checkClassSubsumption");
		return res;

	}

	public boolean checkKBConsistency() {
		Profiler.startMonitor(this.getClass().getName(), "checkKBConsistency");
		logger.info("Knowledgebase consistency");

		boolean res = model.validate().isValid();
		Profiler.stopMonitor(this.getClass().getName(), "checkKBConsistency");
		return res;
	}

	public void classify() {
		Profiler.startMonitor(this.getClass().getName(), "classify");
		logger.info("Knowledgebase classification");

		((PelletInfGraph) model.getGraph()).classify();

		Profiler.stopMonitor(this.getClass().getName(), "classify");
	}

	public void realize() {
		Profiler.startMonitor(this.getClass().getName(), "realize");
		logger.info("Knowledgebase realization");

		((PelletInfGraph) model.getGraph()).realize();

		Profiler.stopMonitor(this.getClass().getName(), "realize");
	}

	public boolean instanceChecking(URI i, URI c) {
		Profiler.startMonitor(this.getClass().getName(), "instanceChecking");
		logger.info("Instance Checking");
		logger.debug("Instance: " + i.toString());
		logger.debug("Class: " + c.toString());

		// model.write(System.out);

		Individual in = model.getIndividual(i.toString());
		try {
			in.isIndividual();
		} catch (Exception e) {
			logger.error("e1 is not a individual!");
			Profiler.stopMonitor(this.getClass().getName(), "instanceChecking");
			return false;
		}
		ExtendedIterator<Resource> tl = in.listRDFTypes(true);
		boolean res = false;
		while (tl.hasNext()) {
			Resource t = tl.next();
			OntClass cl = model.getOntClass(c.toString());
			res = cl.hasSubClass(t);
			if (res) {
				Profiler.stopMonitor(this.getClass().getName(),
						"instanceChecking");
				return res;
			}
		}
		Profiler.stopMonitor(this.getClass().getName(), "instanceChecking");
		return res;
	}

	public SPARQLDLResult sparqldlProcessing(String query) {
		Profiler.startMonitor(this.getClass().getName(), "sparqldlSelect");

		String sparql_query = "";
		try {
			SPARQLQuery sq = SparqlQueryHelper.createSparqlQuery(query);
			sparql_query = query;
		} catch (Exception e) {
			logger.info("Not a SPARQL query, try to parse SPARQL-DL...");
			SPARQL_DL_Parser sp = new SPARQL_DL_Parser_Impl();
			SPARQL_DL_Transformation trans = new SPARQL_DL_Transformation_Impl();

			List<de.dfki.isreal.data.Statement> sts = null;
			try {
				List<Token> toks = sp.parse(query);
				sts = trans.transformToStatements(toks);
			} catch (Exception e1) {
				logger.info("Not a valid SPARQL-DL query!", e1);
				Profiler.stopMonitor(this.getClass().getName(),
						"sparqldlSelect");
				return null;
			}

			sparql_query = SparqlQueryHelper.createSPARQLQuery(sts).toString();
			logger.debug("Transformed Query: " + sparql_query);
		}

		Query q = QueryFactory.create(sparql_query);

		QueryExecution qe = SparqlDLExecutionFactory.create(q, model);

		SPARQLDLResult res = null;
		if (q.isAskType()) {
			boolean b = qe.execAsk();
			logger.info("Answer is: " + b);
			res = new SPARQLDLResultImpl(b);
		} else {
			ResultSet rs = qe.execSelect();
			res = new SPARQLDLResultImpl(rs);
		}

		logger.debug("SPARQL-DL query: " + q.toString());
		Profiler.stopMonitor(this.getClass().getName(), "sparqldlSelect");
		return res;
	}

	public void initFromFiles(List<File> ontologies) {
		Profiler.startMonitor(this.getClass().getName(), "initPluginFromFiles");
		model.removeAll();
		Profiler.startMonitor(this.getClass().getName(), "loadFiles");
		OntModel m = ModelFactory
				.createOntologyModel(PelletReasonerFactory.THE_SPEC);
		OntDocumentManager manager = m.getDocumentManager();

		HashMap<String, String> mapping = OMSConfig.getURIMappings();
		for (String abs : mapping.keySet()) {
				manager.addAltEntry(abs, mapping.get(abs));
		}

		for (File ont : ontologies) {
			m.read(ont.toURI().toString());
		}
		Profiler.stopMonitor(this.getClass().getName(), "loadFiles");
		// m.rebind();
		// m.prepare();
		model.add(m);
		Profiler.stopMonitor(this.getClass().getName(), "initPluginFromFiles");
	}

	public void insert(List<de.dfki.isreal.data.Statement> al) {
		Profiler.startMonitor(this.getClass().getName(), "insert");
		for (de.dfki.isreal.data.Statement a : al) {
			Resource s = model.createResource(a.getSubjectString());
			Property p = model.createProperty(a.getPredicateString());
			RDFNode o = null;
			String obj = a.getObjectString();
			if (obj.startsWith("_:")) {
				o = model.createLiteral(obj);
			} else if (obj.startsWith("http:") || obj.startsWith("file:")) {
				o = model.createResource(obj);
			} else {
				o = model.createLiteral(obj);
			}

			model.add(s, p, o);
		}
		Profiler.stopMonitor(this.getClass().getName(), "insert");
	}

	public void remove(List<de.dfki.isreal.data.Statement> al) {
		Profiler.startMonitor(this.getClass().getName(), "remove");
		for (de.dfki.isreal.data.Statement a : al) {
			// create a statement to remove
			Resource s = model.createResource(a.getSubjectString());
			Property p = model.createProperty(a.getPredicateString());
			RDFNode o = null;
			String obj = a.getObjectString();
			if (obj.startsWith("_:")) {
				o = model.createLiteral(obj);
			} else if (obj.startsWith("http:") || obj.startsWith("file:")) {
				o = model.createResource(obj);
			} else {
				o = model.createLiteral(obj);
			}
			Statement st = model.createStatement(s, p, o);

			// model.remove(s, p, o);
			model.remove(st);
		}
		Profiler.stopMonitor(this.getClass().getName(), "remove");
	}

	public boolean shutdownPluginToFile() {
		logger.error("NOT IMPLEMENTED");
		return Boolean.TRUE;

	}

	public boolean shutdownPluginToStore() {
		// TODO Auto-generated method stub
		logger.error("NOT IMPLEMENTED");
		return Boolean.TRUE;
	}

	public void update(List<de.dfki.isreal.data.Statement> al) {
		Profiler.startMonitor(this.getClass().getName(), "update");
		if (al.size() > 0) {
			de.dfki.isreal.data.Statement tmp = al.get(0);
			String subj = tmp.getSubjectString();
			Resource r = model.getResource(subj);
			model.removeAll(r, null, null);

			this.insert(al);
		}
		Profiler.stopMonitor(this.getClass().getName(), "update");
	}

	/**
	 * This method lists all statements in the KB of the reasoner. Since the
	 * reasoner materializes all inferences it takes time to create all triples.
	 * This method is not recommended for ontologies over 1000 instances.
	 */
	public List<de.dfki.isreal.data.Statement> listStatements() {
		Profiler.startMonitor(this.getClass().getName(), "listStatements");
		logger.debug("Start listing staments...");
		StmtIterator it = model.listStatements();
		logger.debug("Start iterating staments...");
		List<de.dfki.isreal.data.Statement> list = new ArrayList<de.dfki.isreal.data.Statement>();
		while (it.hasNext()) {
			Statement s = it.next();
			list.add(new StatementImpl(s.getSubject().getURI(), s
					.getPredicate().getURI(), s.getObject().toString()));
		}
		Profiler.stopMonitor(this.getClass().getName(), "listStatements");
		return list;
	}

	public List<List<de.dfki.isreal.data.Statement>> computeTopRelationalTrees(
			List<String> entities, int num, boolean props) {
		Profiler.startMonitor(this.getClass().getName(),
				"computeTopRelationalTrees");
		List<List<de.dfki.isreal.data.Statement>> result = new ArrayList<List<de.dfki.isreal.data.Statement>>();

		Set<Fact> graph = new TreeSet<Fact>();
		Queue<OntClass> to_check = new LinkedList<OntClass>();
		OntClass thing = model
				.getOntClass("http://www.w3.org/2002/07/owl#Thing");
		to_check.add(thing);
		// construct graph by his implicit class hierarchy.
		while (!to_check.isEmpty()) {
			OntClass ocl = to_check.poll();
			logger.info("Class to check: " + ocl.getURI());
			ExtendedIterator<OntClass> eit = ocl.listSubClasses(true);
			while (eit.hasNext()) {
				OntClass scl = eit.next();
				if (!scl.getURI().equals(
						"http://www.w3.org/2002/07/owl#Nothing")
						&& filter.accept(scl.getURI())) {
					to_check.add(scl);
					Fact f = new Fact(new Entity(scl.getURI()), new Entity(
							ocl.getURI()), new Relation(
							"http://www.w3.org/2000/01/rdf-schema#subClassOf"),
							0.0F);
					try {
						graph.add(f);
					} catch (Exception e) {
						logger.error("Could not insert fact:\n"
								+ scl.getURI().toString() + " subClassOf "
								+ ocl.getURI().toString());
					}
				}
			}
			// ExtendedIterator<OntResource> e2it =
			// (ExtendedIterator<OntResource>) ocl
			// .listInstances(true);
			ExtendedIterator<? extends OntResource> e2it = ocl
					.listInstances(true);
			List<? extends OntResource> inds = e2it.toList();
			logger.info("Number of direct individuals: " + inds.size());
			// add instances into class hierarchy
			// while (e2it.hasNext()) {
			for (OntResource ind : inds) {
				// OntResource ind = e2it.next();
				if (filter.accept(ind.getURI())) {
					Fact f = new Fact(new Entity(ind.getURI()), new Entity(
							ocl.getURI()), new Relation(
							"http://www.w3.org/1999/02/22-rdf-syntax-ns#type"),
							0.0F);
					try {
						graph.add(f);
					} catch (Exception e) {
						logger.error("Could not insert fact.");
						// + ind.getURI().toString() + " type " );
					}

					if (props) {
						ExtendedIterator<ObjectProperty> e3it = model
								.listObjectProperties();
						// add object properties between instances
						while (e3it.hasNext()) {
							ObjectProperty prop = e3it.next();
							if (filter.accept(prop.getURI())) {
								NodeIterator nit = ind.listPropertyValues(prop);
								while (nit.hasNext()) {
									RDFNode node = nit.next();
									// System.out.println(ind.getLocalName() +
									// " -> " +
									// prop.getLocalName() + " -> " +
									// node.toString());
									Fact pf = new Fact(
											new Entity(ind.getURI()),
											new Entity(node.toString()),
											new Relation(prop.getURI()), 0.0F);
									logger.debug(">>>" + ind.getURI() + " : "
											+ node.toString() + " : "
											+ prop.getURI());
									try {
										graph.add(pf);
									} catch (Exception e) {
										logger.error("Could not insert fact:\n"
												+ ind.toString() + " "
												+ prop.getURI().toString()
												+ " " + node.toString());
									}
								}
							}
						}
					}
				}
			}
		}

		try {
			String[] ents = (String[]) entities.toArray(new String[0]);
			STARfromMM steiner = new STARfromMM(ents, graph);
			Queue<ResultGraph> rg = steiner.getTopKTrees(num);
			while (!rg.isEmpty()) {
				result.add(getResultStatements(rg.poll()));
			}
		} catch (Exception e) {
			logger.error("Could not compute Steiner Tree.", e);
			e.printStackTrace();
		}
		Profiler.stopMonitor(this.getClass().getName(),
				"computeTopRelationalTrees");
		return result;
	}

	private List<de.dfki.isreal.data.Statement> getResultStatements(
			ResultGraph poll) {
		List<de.dfki.isreal.data.Statement> st_list = new ArrayList<de.dfki.isreal.data.Statement>();
		for (Fact f : poll.getEdges()) {
			de.dfki.isreal.data.Statement st = new StatementImpl(f.getN1()
					.name(), f.label().name(), f.getN2().name());
			st_list.add(st);
		}
		return st_list;
	}

	private static String getResultString(ResultGraph poll) {
		String r = "Score: " + poll.getScore() + "\n";
		for (Fact f : poll.getEdges()) {
			r = r + f.factToString() + "\n";
		}
		return r;
	}

	public State getInitialStateFromKB() {
		Profiler.startMonitor(this.getClass().getName(),
				"getInitialStateFromKB");
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		model.write(os);
		State state = new OWLStateImpl(os);
		Profiler.stopMonitor(this.getClass().getName(), "getInitialStateFromKB");
		return state;
	}

}
