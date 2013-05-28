package de.dfki.sdre.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jdom.Document;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

import uk.ac.manchester.cs.factplusplus.owlapiv3.FaCTPlusPlusReasonerFactory;

import de.derivo.sparqldlapi.Query;
import de.derivo.sparqldlapi.QueryArgument;
import de.derivo.sparqldlapi.QueryBinding;
import de.derivo.sparqldlapi.QueryEngine;
import de.derivo.sparqldlapi.QueryResult;
import de.derivo.sparqldlapi.exceptions.QueryEngineException;
import de.derivo.sparqldlapi.exceptions.QueryParserException;
import de.derivo.sparqldlapi.types.QueryArgumentType;
import de.dfki.isreal.data.SPARQLDLResult;
import de.dfki.isreal.data.impl.BindingImpl;
import de.dfki.isreal.data.impl.CloseableIteratorImpl;
import de.dfki.isreal.data.impl.SPARQLDLResultImpl;
import de.dfki.isreal.semantic.oms.components.impl.VariableBindingQueryResultImpl;
import eu.larkc.core.data.CloseableIterator;
import eu.larkc.core.data.VariableBinding;
import eu.larkc.core.data.VariableBinding.Binding;


public class SPARQLDLImpl {
	
//	static IRI ontoURI = IRI.create("http://www.icmwind.com/icmwindontology.owl");
//	static IRI docURI = IRI.create("file:C:/workspace_sdre/de.dfki.sdre/res/gse_config/ontologies/icmwindontology.owl");
	
	static IRI ontoURI = IRI.create("http://www.icmwind.com/instances/iwo-abox-04.09.2008.owl");
	static IRI docURI = IRI.create("file:C:/workspace_sdre/de.dfki.sdre/res/gse_config/ontologies/iwo-abox-04.09.2008.owl");
	
	static OWLOntologyManager man =  OWLManager.createOWLOntologyManager();
	OWLDataFactory fact = man.getOWLDataFactory();
	static OWLOntology ontology = null;
	
//	static StructuralReasonerFactory reasonerFactory = null;
	static OWLReasonerFactory reasonerFactory = null;
	static ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();
	static OWLReasonerConfiguration config = new SimpleConfiguration(progressMonitor);
	static OWLReasoner reasoner = null;
	
	private static QueryEngine engine;
	
	static CloseableIterator<Binding> binding;
	static List<String> variables =  new ArrayList<String>();

	
	public static void main(String[] args) {
		man.addIRIMapper(new SimpleIRIMapper(ontoURI, docURI));
		
		try {
			ontology = man.loadOntology(ontoURI);
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
		
//		reasonerFactory = new StructuralReasonerFactory();
		reasonerFactory = new FaCTPlusPlusReasonerFactory();
		reasoner = reasonerFactory.createReasoner(ontology, config);
		reasoner.precomputeInferences(InferenceType.CLASS_ASSERTIONS,InferenceType.OBJECT_PROPERTY_ASSERTIONS);
		
//		System.out.println(reasoner.isConsistent());
		engine = QueryEngine.create(man, reasoner, true);
		
//		String q =
//				"PREFIX iwo: <http://www.icmwind.com/icmwindontology.owl#>" + "\n" +
//				"SELECT ?x WHERE { " +
//				"Class(?x) " +
//				"}";
		
		String q = "PREFIX iwo: <http://www.icmwind.com/icmwindontology.owl#>\n" +
		"SELECT ?x WHERE {\n" +
			"StrictSubClassOf(?x, iwo:Sensor)" +
		"}";
		
		String q2 = 
				"PREFIX wtd: <http://www.icmwind.com/icmwindontology.owl#>\n" +
				"SELECT DISTINCT ?s ?qs WHERE {\n" +
					"SubClassOf(?qs, wtd:Sensor), \n" +
					"DirectSubClassOf(?qs, ?s)" +
				"}";
		
		String q3 = 
				"PREFIX wtd: <http://www.icmwind.com/icmwindontology.owl#>\n" +
				"ASK WHERE {\n" +
					"Property(wtd:hasPosition)" +
				"}";

		boolean b = false;
		QueryResult result = null;
		Query sparqlquery = null;
		
		SPARQLDLResult dlresult = null;

		try {
			sparqlquery = Query.create(q3);
			result = engine.execute(sparqlquery);
			
			if(result.ask())
				b = result.ask();
				
		} catch (QueryParserException | QueryEngineException e) {
//			e.printStackTrace();
		}
		
		if(sparqlquery.isAsk()) {
			System.out.println("Answer is: " + b);
			dlresult = new SPARQLDLResultImpl(b);
		} else {
			System.out.println(result.toString());
			VariableBinding bindings = new VariableBindingQueryResultImpl(result);
			dlresult = new SPARQLDLResultImpl(bindings);
		}
		


	}
	
	

}
