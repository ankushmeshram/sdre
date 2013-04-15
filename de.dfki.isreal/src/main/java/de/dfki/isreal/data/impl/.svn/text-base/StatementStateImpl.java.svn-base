package de.dfki.isreal.data.impl;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.mindswap.pellet.jena.PelletReasonerFactory;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;

import de.dfki.isreal.data.State;
import de.dfki.isreal.data.Statement;

/**
 * State implementation using a List of Statement.
 * 
 * @author stenes
 *
 */
public class StatementStateImpl implements State {

	public static String	RDF_TYPE = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
	
	List<Statement> statements;

	public StatementStateImpl(List<Statement> sts) {
		statements = sts;
	}

	
	public String getOntology() {
		OntModel model = ModelFactory
				.createOntologyModel(PelletReasonerFactory.THE_SPEC);
		for (Statement a : statements) {
			// Class membership -> individual of specific type
			if(a.getPredicateString().equals(RDF_TYPE)) {
				String indName = a.getSubjectString();
				Resource clazz = model.createResource(a.getObjectString());
				model.createIndividual(indName, clazz);			
			}
			// Object property -> statement
			else {
				Resource s = model.createResource(a.getSubjectString());
				Property p = model.createProperty(a.getPredicateString());
				Resource o = model.createResource(a.getObjectString());
				
				model.add(s, p, o);
			}
		}
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		model.write(os); // RDF/XML syntax
		
		String ont = os.toString();
		
		return ont;
	}

}
