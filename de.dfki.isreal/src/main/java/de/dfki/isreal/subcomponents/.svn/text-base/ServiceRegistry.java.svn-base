package de.dfki.isreal.subcomponents;

import java.util.List;

import de.dfki.isreal.data.Parameter;
import de.dfki.isreal.data.ServiceWrapper;
import de.dfki.isreal.data.impl.ConditionalEffect;


public interface ServiceRegistry{
	
	/**
	 * store all services found in the ontology in the semantic environment.
	 */
	public void addServices(String ont_uri);
	
	/**
	 * remove a service from the registry
	 */
	public void removeService(String uri);

	
	/**
	 * returns a list of URI.
	 */
	public List<String> getInteractionServices();
	
	/**
	 * returns a list of URI.
	 */
	public List<String> getSEServices();
	
	/**
	 * returns a list of URI.
	 */
	public List<String> getAgentServices();
	
	/**
	 * returns a list of URI.
	 */
	public List<String> getAllServices();
		
	/**
	 * Returns the human readable description of the Service as a String.
	 */
	public String getServiceDescription(String service_uri);
	
	/**
	 * Returns the input parameter URIs of the service as list.
	 */
	public List<String> getServiceInputs(String service_uri);
}
