package de.dfki.isreal.data.impl;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.mindswap.pellet.jena.PelletReasonerFactory;
import org.openrdf.model.Statement;
import org.openrdf.model.Value;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

import de.dfki.isreal.data.State;

import eu.larkc.core.data.CloseableIterator;
import eu.larkc.core.data.SetOfStatements;

/**
 * State implementation for a SetOfStatements. This is used to extract the
 * State from the TripleStore Plugin.
 * 
 * @author stenes
 *
 */
public class SetOfStatementsStateImpl implements State {

	SetOfStatements statements;

	public SetOfStatementsStateImpl(SetOfStatements sts) {
		statements = sts;
	}

	
	public String getOntology() {
		OntModel model = ModelFactory
				.createOntologyModel(PelletReasonerFactory.THE_SPEC);
		CloseableIterator<Statement> it = statements.getStatements();
		while (it.hasNext()) {
			Statement a = it.next();
			if (valid(a.getSubject()) && valid(a.getPredicate()) && valid(a.getObject())){
			Resource s = model.createResource(a.getSubject().stringValue());
			Property p = model.createProperty(a.getPredicate().stringValue());
			Resource o = model.createResource(a.getObject().stringValue());
			
			model.add(s, p, o);
			}
		}
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		model.write(os); // RDF/XML syntax
		
		String ont = os.toString();
		
		return ont;
	}


	/**
	 * @param object
	 * @return
	 */
	private boolean valid(Value object) {
		if (object.stringValue().startsWith("http")){
			return true;
		}
		return false;
	}

}
