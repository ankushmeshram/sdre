package de.dfki.isreal.semantic.oms.components.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

import naga.steinertrees.queries.Entity;
import naga.steinertrees.queries.Fact;
import naga.steinertrees.queries.Relation;
import naga.steinertrees.queries.ResultGraph;
import naga.steinertrees.queries.STARfromMM;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.ClassExpressionType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

import uk.ac.manchester.cs.factplusplus.owlapiv3.FaCTPlusPlusReasonerFactory;
import de.derivo.sparqldlapi.Query;
import de.derivo.sparqldlapi.QueryEngine;
import de.derivo.sparqldlapi.QueryResult;
import de.derivo.sparqldlapi.exceptions.QueryEngineException;
import de.derivo.sparqldlapi.exceptions.QueryParserException;
import de.dfki.isreal.data.SPARQLDLResult;
import de.dfki.isreal.data.State;
import de.dfki.isreal.data.impl.OWLStateImpl;
import de.dfki.isreal.data.impl.SPARQLDLResultImpl;
import de.dfki.isreal.data.impl.StatementImpl;
import de.dfki.isreal.helpers.Profiler;
import de.dfki.isreal.semantic.oms.components.SemanticReasonerPlugin;
import eu.larkc.core.data.VariableBinding;

public class SemanticReasonerFactPlusPlusImpl implements SemanticReasonerPlugin {

	private Logger logger = Logger.getLogger(SemanticReasonerFactPlusPlusImpl.class);

	OWLOntologyManager manager = null;
	OWLDataFactory factory = null;
	OWLOntology ontology = null;
	
	OWLReasonerFactory reasonerFactory = null;
	ConsoleProgressMonitor progressMonitor = null;
	OWLReasonerConfiguration config = null;
	OWLReasoner reasoner = null;
	
	private QueryEngine engine;
	
	Set<Fact> graph = new TreeSet<Fact>();
	
	public SemanticReasonerFactPlusPlusImpl() {
		manager =  OWLManager.createOWLOntologyManager();
		factory = manager.getOWLDataFactory();
		
		reasonerFactory = new FaCTPlusPlusReasonerFactory();
		progressMonitor = new ConsoleProgressMonitor();
		config = new SimpleConfiguration(progressMonitor);
	}
	

	public boolean checkClassConsistency(URI c) {
//		Profiler.startMonitor(this.getClass().getName(),
//				"checkClassConsistency");
//		logger.info("Class consistency (Satisfiability)");
//
//		OntClass cl1 = model.getOntClass(c.toString());
//		OntClass nothing = model
//				.getOntClass("http://www.w3.org/2002/07/owl#Nothing");
//		if (cl1 == null || nothing == null) {
//			logger.error("Input URI's not correct!");
//			Profiler.stopMonitor(this.getClass().getName(),
//					"checkClassConsistency");
//			return false;
//		}
//		boolean res = !(cl1.hasEquivalentClass(nothing));
//		Profiler.stopMonitor(this.getClass().getName(), "checkClassConsistency");
//		return res;
		return false;
	}

	public boolean checkClassDisjunction(URI c1, URI c2) {
//		Profiler.startMonitor(this.getClass().getName(),
//				"checkClassDisjunction");
//		logger.info("Class Disjunction");
//
//		OntClass cl1 = model.createClass(c1.getPath());
//		OntClass cl2 = model.createClass(c2.getPath());
//		if (cl1 == null || cl2 == null) {
//			logger.error("Input URI's not correct!");
//			Profiler.stopMonitor(this.getClass().getName(),
//					"checkClassDisjunction");
//			return false;
//		}
//		boolean res = cl1.isDisjointWith(cl2);
//		Profiler.stopMonitor(this.getClass().getName(), "checkClassDisjunction");
//		return res;
		return false;
	}

	public boolean checkClassEquivalence(URI c1, URI c2) {
//		Profiler.startMonitor(this.getClass().getName(),
//				"checkClassEquivalence");
//		logger.info("Class Equivalence");
//
//		OntClass cl1 = model.getOntClass(c1.toString());
//		OntClass cl2 = model.getOntClass(c2.toString());
//		if (cl1 == null || cl2 == null) {
//			logger.error("Input URI's not correct!");
//			Profiler.stopMonitor(this.getClass().getName(),
//					"checkClassEquivalence");
//			return false;
//		}
//		boolean res = cl1.hasEquivalentClass(cl2);
//		Profiler.stopMonitor(this.getClass().getName(), "checkClassEquivalence");
//		return res;
		return false;
	}

	public boolean checkClassSubsumption(URI c1, URI c2) {
//		Profiler.startMonitor(this.getClass().getName(),
//				"checkClassSubsumption");
//		logger.info("Class Subsumption");
//
//		OntClass cl1 = model.getOntClass(c1.toString());
//		OntClass cl2 = model.getOntClass(c2.toString());
//		if (cl1 == null || cl2 == null) {
//			logger.error("Input URI's not correct!");
//			Profiler.stopMonitor(this.getClass().getName(),
//					"checkClassSubsumption");
//			return false;
//		}
//
//		boolean res = cl2.hasSubClass(cl1);
//		Profiler.stopMonitor(this.getClass().getName(), "checkClassSubsumption");
//		return res;
		return false;
	}

	public boolean checkKBConsistency() {
//		Profiler.startMonitor(this.getClass().getName(), "checkKBConsistency");
//		logger.info("Knowledgebase consistency");
//
//		boolean res = model.validate().isValid();
//		Profiler.stopMonitor(this.getClass().getName(), "checkKBConsistency");
//		return res;
		return false;
	}

	public void classify() {
		Profiler.startMonitor(this.getClass().getName(), "classify");
		logger.info("Knowledgebase classification");

//		((PelletInfGraph) model.getGraph()).classify();

		Profiler.stopMonitor(this.getClass().getName(), "classify");
	}

	public void realize() {
		Profiler.startMonitor(this.getClass().getName(), "realize");
		logger.info("Knowledgebase realization");

//		((PelletInfGraph) model.getGraph()).realize();

		Profiler.stopMonitor(this.getClass().getName(), "realize");
	}

	public boolean instanceChecking(URI i, URI c) {
		Profiler.startMonitor(this.getClass().getName(), "instanceChecking");
		logger.info("Instance Checking");
		logger.debug("Instance: " + i.toString());
		logger.debug("Class: " + c.toString());

		// model.write(System.out);

//		Individual in = model.getIndividual(i.toString());
//		try {
//			in.isIndividual();
//		} catch (Exception e) {
//			logger.error("e1 is not a individual!");
//			Profiler.stopMonitor(this.getClass().getName(), "instanceChecking");
//			return false;
//		}
//		ExtendedIterator<Resource> tl = in.listRDFTypes(true);
		boolean res = false;
//		while (tl.hasNext()) {
//			Resource t = tl.next();
//			OntClass cl = model.getOntClass(c.toString());
//			res = cl.hasSubClass(t);
//			if (res) {
//				Profiler.stopMonitor(this.getClass().getName(),
//						"instanceChecking");
//				return res;
//			}
//		}
		Profiler.stopMonitor(this.getClass().getName(), "instanceChecking");
		return res;
	}

	public SPARQLDLResult sparqldlProcessing(String query) {
		Profiler.startMonitor(this.getClass().getName(), "sparqldlSelect");
		SPARQLDLResult dlresult = null;
		
		System.out.println("SPARQL-DL QUERY: " + query);
	
		try {
			boolean b = false;
			
			Query sparqlquery = Query.create(query);
			QueryResult result = engine.execute(sparqlquery);
			
			//debug
			System.out.println(sparqlquery.isAsk());
			
			if(result.ask())
				b = result.ask();
						
			if(sparqlquery.isAsk()) {
				System.out.println("Answer is: " + b);
				dlresult = new SPARQLDLResultImpl(b);
			} else {			
				VariableBinding bindings = new VariableBindingQueryResultImpl(result);
				dlresult = new SPARQLDLResultImpl(bindings);
			}
			
		} catch (QueryEngineException e) {
//			e.printStackTrace();
		} catch (QueryParserException e) {
//			e.printStackTrace();
		}
		
		logger.debug("SPARQL-DL query: " + query.toString());
		Profiler.stopMonitor(this.getClass().getName(), "sparqldlSelect");
		return dlresult;
						
	}

	public void initFromFiles(List<File> ontologies) {
		Profiler.startMonitor(this.getClass().getName(), "initPluginFromFiles");
		Profiler.startMonitor(this.getClass().getName(), "loadFiles");
		
		IRI ontoURI = null;
		IRI docURI = null;
				
		Map<String, String> mapping = OMSConfig.getURIMappings();
		for(Map.Entry<String, String> entry : mapping.entrySet()) {
			ontoURI = IRI.create(entry.getKey().toString());
			docURI = IRI.create(entry.getValue().toString());
			
			manager.addIRIMapper(new SimpleIRIMapper(ontoURI, docURI));
		}

		for (File ont : ontologies) {
			try {
				ontology = manager.loadOntology(IRI.create(ont));
			} catch (OWLOntologyCreationException e) {
				e.printStackTrace();
			}
		}
		
		Profiler.stopMonitor(this.getClass().getName(), "loadFiles");
		
		initPluginReasoner();
		
		Profiler.stopMonitor(this.getClass().getName(), "initPluginFromFiles");
	}
	
	private void initPluginReasoner() {
		reasoner = reasonerFactory.createReasoner(ontology, config);
		reasoner.precomputeInferences(InferenceType.CLASS_ASSERTIONS, InferenceType.OBJECT_PROPERTY_ASSERTIONS, InferenceType.DATA_PROPERTY_ASSERTIONS,
								InferenceType.CLASS_HIERARCHY, InferenceType.OBJECT_PROPERTY_HIERARCHY);
		
		engine = QueryEngine.create(manager, reasoner, true);
		
		createGraph();
	}
	
	private void createGraph() {
		Queue<OWLClass> to_check = new LinkedList<OWLClass>();
		ClassExpressionType someValuesFrom = ClassExpressionType.OBJECT_SOME_VALUES_FROM;
		ClassExpressionType exactCardinality = ClassExpressionType.OBJECT_EXACT_CARDINALITY;

		OWLClass thing = factory.getOWLThing();
		to_check.add(thing);
				
		// construct graph by his implicit class hierarchy.
		while (!to_check.isEmpty()) {
			OWLClass clazz = to_check.poll();
			System.out.println();
			logger.info("Class to check: " + clazz.getIRI());
			
			for(OWLSubClassOfAxiom ax : ontology.getSubClassAxiomsForSubClass(clazz)) {
				OWLObjectProperty restrictedProperty = null;
				OWLClass restrictedClass = null; 
				
				OWLClassExpression superCls = ax.getSuperClass();
				ClassExpressionType type = superCls.getClassExpressionType();
				
				if(type.compareTo(someValuesFrom) == 0) {
					OWLObjectSomeValuesFrom desc = (OWLObjectSomeValuesFrom) superCls;
					restrictedProperty = desc.getProperty().asOWLObjectProperty();
//					System.out.print(desc.getProperty().asOWLObjectProperty().getIRI().toString() + "--");
					if(!desc.getFiller().isAnonymous()) {
						restrictedClass = desc.getFiller().asOWLClass();
//						System.out.println(desc.getFiller().asOWLClass().getIRI().toString());	
					}
					
				} else if(type.compareTo(exactCardinality) == 0) {
					OWLObjectExactCardinality desc = (OWLObjectExactCardinality) superCls;
					restrictedProperty = desc.getProperty().asOWLObjectProperty();
//					System.out.print(desc.getProperty().asOWLObjectProperty().getIRI().toString() + "--");
					if(!desc.getFiller().isAnonymous()) {
						restrictedClass = desc.getFiller().asOWLClass();
//						System.out.println(desc.getFiller().asOWLClass().getIRI().toString());	
					}
				}

//				System.out.println(superCls.toString());
				if(restrictedProperty != null && restrictedClass != null) {
				
				// Add Class-ObjectProperty-Class relations
				Fact f = new Fact(
						     new Entity(clazz.getIRI().getFragment()),
						     new Entity(restrictedClass.getIRI().getFragment()),
						     new Relation(restrictedProperty.getIRI().getFragment()),
						     0.0F);
				
				try {
					graph.add(f);
				} catch (Exception e) {
					logger.error("Could not insert fact:\n" + clazz.getIRI().getFragment() + "--" + restrictedProperty.getIRI().getFragment() + "--" + restrictedClass.getIRI().getFragment());
				} 
				}
				
			}
			
			NodeSet<OWLClass> classes = reasoner.getSubClasses(clazz, true);
			Set<OWLClass> subclasses = classes.getFlattened();
			for(OWLClass subclass : subclasses) {
				if(!subclass.equals(factory.getOWLNothing())) {
					to_check.add(subclass);
					
					// Add Direct Subclasses			
					// Add Class-SubClassOf-Class relation
					Fact f = new Fact(
						     new Entity(subclass.getIRI().getFragment()),
						     new Entity(clazz.getIRI().getFragment()),
						     new Relation("subClassOf"), //"http://www.w3.org/2000/01/rdf-schema#subClassOf"),
						     0.0F);
					
					try {
						graph.add(f);
					} catch (Exception e) {
						logger.error("Could not insert fact:\n" + clazz.getIRI().getFragment() + "--subClassOf--" + subclass.getIRI().getFragment());
					}
				}
			}
			

			NodeSet<OWLNamedIndividual> instances = reasoner.getInstances(clazz, true);
			Set<OWLNamedIndividual> individuals = instances.getFlattened();
			for(OWLNamedIndividual individual : individuals) {
				
				// Add Individual-Type-Class relation
				Fact f = new Fact(
						     new Entity(individual.getIRI().getFragment()),
						     new Entity(clazz.getIRI().getFragment()),
						     new Relation("type"), //"http://www.w3.org/1999/02/22-rdf-syntax-ns#type"),
						     0.0F);
				
				try {
					graph.add(f);
				} catch (Exception e) {
					logger.error("Could not insert fact:\n" + individual.getIRI().getFragment() + "--type--"+ clazz.getIRI().getFragment());
				
				}
				
				for(OWLObjectPropertyAssertionAxiom opax : ontology.getObjectPropertyAssertionAxioms(individual)) {
					OWLObjectProperty restrictedProperty = opax.getProperty().asOWLObjectProperty();
					OWLNamedIndividual restrictedIndividual = opax.getObject().asOWLNamedIndividual();
					
					// Add Individual-ObjectProperty-Individual relation
					Fact f2 = new Fact(
							     new Entity(individual.getIRI().getFragment()),
							     new Entity(restrictedIndividual.getIRI().getFragment()),
							     new Relation(restrictedProperty.getIRI().getFragment()),
							     0.0F);
					
					try {
						graph.add(f2);
					} catch (Exception e) {
						logger.error("Could not insert fact:\n" + individual.getIRI().getFragment() + "--" + restrictedProperty.getIRI().getFragment() + "--" + restrictedIndividual.getIRI().getFragment());
					
					}
				}
							
			}
		}
		
		logger.info("Graph created.");

	}

	public void insert(List<de.dfki.isreal.data.Statement> al) {
		Profiler.startMonitor(this.getClass().getName(), "insert");
//		for (de.dfki.isreal.data.Statement a : al) {
//			Resource s = model.createResource(a.getSubjectString());
//			Property p = model.createProperty(a.getPredicateString());
//			RDFNode o = null;
//			String obj = a.getObjectString();
//			if (obj.startsWith("_:")) {
//				o = model.createLiteral(obj);
//			} else if (obj.startsWith("http:") || obj.startsWith("file:")) {
//				o = model.createResource(obj);
//			} else {
//				o = model.createLiteral(obj);
//			}
//
//			model.add(s, p, o);
//		}
		Profiler.stopMonitor(this.getClass().getName(), "insert");
	}

	public void remove(List<de.dfki.isreal.data.Statement> al) {
		Profiler.startMonitor(this.getClass().getName(), "remove");
//		for (de.dfki.isreal.data.Statement a : al) {
//			// create a statement to remove
//			Resource s = model.createResource(a.getSubjectString());
//			Property p = model.createProperty(a.getPredicateString());
//			RDFNode o = null;
//			String obj = a.getObjectString();
//			if (obj.startsWith("_:")) {
//				o = model.createLiteral(obj);
//			} else if (obj.startsWith("http:") || obj.startsWith("file:")) {
//				o = model.createResource(obj);
//			} else {
//				o = model.createLiteral(obj);
//			}
//			Statement st = model.createStatement(s, p, o);
//
//			// model.remove(s, p, o);
//			model.remove(st);
//		}
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
//			de.dfki.isreal.data.Statement tmp = al.get(0);
//			String subj = tmp.getSubjectString();
//			Resource r = model.getResource(subj);
//			model.removeAll(r, null, null);

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
//		StmtIterator it = model.listStatements();
		logger.debug("Start iterating staments...");
		List<de.dfki.isreal.data.Statement> list = new ArrayList<de.dfki.isreal.data.Statement>();
//		while (it.hasNext()) {
//			Statement s = it.next();
//			list.add(new StatementImpl(s.getSubject().getURI(), s
//					.getPredicate().getURI(), s.getObject().toString()));
//		}
		Profiler.stopMonitor(this.getClass().getName(), "listStatements");
		return list;
	}

	public List<List<de.dfki.isreal.data.Statement>> computeTopRelationalTrees(
			List<String> entities, int num, boolean props) {
		Profiler.startMonitor(this.getClass().getName(),
				"computeTopRelationalTrees");
		List<List<de.dfki.isreal.data.Statement>> result = new ArrayList<List<de.dfki.isreal.data.Statement>>();

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

//	private static String getResultString(ResultGraph poll) {
//		String r = "Score: " + poll.getScore() + "\n";
//		for (Fact f : poll.getEdges()) {
//			r = r + f.factToString() + "\n";
//		}
//		return r;
//	}

	public State getInitialStateFromKB() {
		Profiler.startMonitor(this.getClass().getName(),
				"getInitialStateFromKB");
		ByteArrayOutputStream os = new ByteArrayOutputStream();
//		model.write(os);
		State state = new OWLStateImpl(os);
		Profiler.stopMonitor(this.getClass().getName(), "getInitialStateFromKB");
		return state;
	}

}
