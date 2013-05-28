package de.dfki.sdre.impl;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.ClassExpressionType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
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
import org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

import de.dfki.isreal.data.Statement;
import de.dfki.isreal.data.impl.StatementImpl;
import de.dfki.isreal.helpers.Profiler;


import uk.ac.manchester.cs.factplusplus.owlapiv3.FaCTPlusPlusReasonerFactory;


public class STAROWLAPIImpl {

	private Logger logger = Logger.getLogger(STARImpl.class);
	
	IRI ontoURI = IRI.create("http://www.icmwind.com/instances/iwo-abox-04.09.2008.owl");
	IRI docURI = IRI.create("file:C:/workspace_sdre/de.dfki.sdre/res/gse_config/ontologies/iwo-abox-04.09.2008.owl");
	
	OWLOntologyManager man =  OWLManager.createOWLOntologyManager();
	OWLDataFactory fact = man.getOWLDataFactory();
	OWLOntology ontology = null;
	
	OWLReasonerFactory reasonerFactory = null;
	ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();
	OWLReasonerConfiguration config = new SimpleConfiguration(progressMonitor);
	OWLReasoner reasoner = null;
	
	Set<Fact> graph = new TreeSet<Fact>();
	ClassExpressionType someValuesFrom = ClassExpressionType.OBJECT_SOME_VALUES_FROM;
	ClassExpressionType exactCardinality = ClassExpressionType.OBJECT_EXACT_CARDINALITY;
		
	public STAROWLAPIImpl() {
		man.addIRIMapper(new SimpleIRIMapper(ontoURI, docURI));
		
		try {
			ontology = man.loadOntology(ontoURI);
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
		
		reasonerFactory = new FaCTPlusPlusReasonerFactory();
		reasoner = reasonerFactory.createReasoner(ontology, config);
		reasoner.precomputeInferences(InferenceType.CLASS_ASSERTIONS, InferenceType.OBJECT_PROPERTY_ASSERTIONS, InferenceType.DATA_PROPERTY_ASSERTIONS,
				InferenceType.DISJOINT_CLASSES , InferenceType.SAME_INDIVIDUAL, InferenceType.SAME_INDIVIDUAL,
				InferenceType.CLASS_HIERARCHY, InferenceType.OBJECT_PROPERTY_HIERARCHY, InferenceType.DATA_PROPERTY_HIERARCHY);
		
		createMap();
	}
		
	
	public static void main(String args[]) {
		STAROWLAPIImpl star = new STAROWLAPIImpl();
		
		List<String> entities = new ArrayList<String>();
		entities.add("http://www.icmwind.com/icmwindontology.owl#cs_marpI");
		entities.add("http://www.icmwind.com/icmwindontology.owl#Connection");
		entities.add("http://www.icmwind.com/icmwindontology.owl#gb_marpI");
		
		List<List<Statement>> answer = star.computeTopRelationalTrees(entities, 3);
		System.out.println("**----------------Test.checkSensor2ComponentRelation() : Paths found...");
		for(List<Statement> paths : answer ) {
			System.out.println("PATH : ");
			for(Statement s : paths) {
				System.out.println(s.getSubjectString() + "--" + s.getPredicateString() + "--" + s.getObjectString());
			}
			
		}
	}
	
	public void createMap() {
		Queue<OWLClass> to_check = new LinkedList<OWLClass>();

		OWLClass thing = fact.getOWLThing();
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
						     new Entity(clazz.getIRI().toString()),
						     new Entity(restrictedClass.getIRI().toString()),
						     new Relation(restrictedProperty.getIRI().toString()),
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
				if(!subclass.equals(fact.getOWLNothing())) {
					to_check.add(subclass);
					
					// Add Direct Subclasses			
					// Add Class-SubClassOf-Class relation
					Fact f = new Fact(
						     new Entity(subclass.getIRI().toString()),
						     new Entity(clazz.getIRI().toString()),
						     new Relation("http://www.w3.org/2000/01/rdf-schema#subClassOf"),
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
						     new Entity(individual.getIRI().toString()),
						     new Entity(clazz.getIRI().toString()),
						     new Relation("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"),
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
							     new Entity(individual.getIRI().toString()),
							     new Entity(restrictedIndividual.getIRI().toString()),
							     new Relation(restrictedProperty.getIRI().toString()),
							     0.0F);
					
					try {
						graph.add(f2);
					} catch (Exception e) {
						logger.error("Could not insert fact:\n" + individual.getIRI().getFragment() + "--" + restrictedProperty.getIRI().getFragment() + "--" + restrictedIndividual.getIRI().getFragment());
					
					}
				}
							
			}
		}
	}
	
	public List<List<de.dfki.isreal.data.Statement>> computeTopRelationalTrees(List<String> entities, int numOfTress) {
		List<List<de.dfki.isreal.data.Statement>> result = new ArrayList<List<de.dfki.isreal.data.Statement>>(); 
		
		try {
			String[] ents = (String[]) entities.toArray(new String[0]);
			STARfromMM steiner = new STARfromMM(ents, graph);
			Queue<ResultGraph> rg = steiner.getTopKTrees(numOfTress);
			while (!rg.isEmpty()) {
				result.add(getResultStatements(rg.poll()));
			}
		} catch (Exception e) {
			logger.error("Could not compute Steiner Tree.", e);
			e.printStackTrace();
		}
		
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
	
	
	public void test() {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("marp.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		OWLNamedIndividual marp = fact.getOWLNamedIndividual(IRI.create("http://www.icmwind.com/icmwindontology.owl#cs_marpI"));
		for(OWLObjectPropertyAssertionAxiom opax : ontology.getObjectPropertyAssertionAxioms(marp)) {
			writer.println(opax.getSubject().asOWLNamedIndividual().getIRI().toString() + "--" +
						   opax.getProperty().asOWLObjectProperty().getIRI().toString() + "--" +
						   opax.getObject().asOWLNamedIndividual().getIRI().toString());
		}
		writer.close();
	}
}




