package de.dfki.isreal.semantic.oms.helpers;

import java.net.URI;
import java.util.HashMap;

import org.semanticweb.owl.model.OWLOntologyURIMapper;

public class ISRealOntologyURIMapper implements OWLOntologyURIMapper {

	private HashMap<URI, URI> mapper;
	
	public ISRealOntologyURIMapper(){
		mapper = new HashMap<URI, URI>();
	}
	
	public void addMapping(URI abs, URI phys){
		mapper.put(abs, phys);
	}
	
	@Override
	public URI getPhysicalURI(URI abs) {
		return mapper.get(abs);
	}

}
