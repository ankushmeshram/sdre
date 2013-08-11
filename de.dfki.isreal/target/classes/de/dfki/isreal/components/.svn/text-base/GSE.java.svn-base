package de.dfki.isreal.components;


import java.util.List;
import java.util.HashMap;

import de.dfki.isreal.data.Statement;
import de.dfki.isreal.subcomponents.FactChangeEventListener;
import de.dfki.isreal.subcomponents.OMSDecider;
import de.dfki.isreal.subcomponents.ServiceExecution;
import de.dfki.isreal.subcomponents.ServiceRegistry;

public interface GSE extends ServiceRegistry, ServiceExecution, OMSDecider, FactChangeEventListener {
		
	/**
	 * 
	 * Specialized query for agent perception. It returns a list of statements
	 * that have the give uri as subject.
	 * 
	 * @param instance_uri
	 * @return
	 */
	public List<Statement> getObjectFacts(String instance_uri);
	
	/**
	 * 
	 * Specialized query for agent perception. Handles a list of instances with
	 * the same effect as calling getObjectFacts n times.
	 * 
	 * @param instance_uris
	 * @return
	 */
	public HashMap<String, List<Statement>> getObjectFacts(List<String> instance_uris);
	
	/**
	 * Return a profile report, that is a report about the measured times in
	 * the GSE.
	 * @return
	 */
	public String getProfileReport();
}
