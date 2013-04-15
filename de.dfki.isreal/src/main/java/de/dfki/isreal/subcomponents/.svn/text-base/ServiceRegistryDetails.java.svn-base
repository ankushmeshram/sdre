package de.dfki.isreal.subcomponents;

import java.util.List;

import de.dfki.isreal.data.Parameter;
import de.dfki.isreal.data.ServiceWrapper;
import de.dfki.isreal.data.impl.ConditionalEffect;

/**
 * This interface provides more methods for the service registry.
 * @author stenes
 *
 */
public interface ServiceRegistryDetails {
	
	/**
	 * Stores a service uri (uri of the service instance) in the semantic environment.
	 */
	public boolean addService(String uri);
	
	/**
	 * Returns the input parameter URIs of the service as list.
	 */
	public List<String> getServiceInputs(String service_uri);
	
	/**
	 * Returns the output parameter URIs of the service as list.
	 */
	public List<String> getServiceOutputs(String service_uri);
	
	/**
	 * Returns the local parameter URIs of the service as list.
	 */
	public List<String> getServiceLocals(String service_uri);
	
	/**
	 * Returns a String for every expression of the precondition of the service.
	 * We Assume this to be in PDDXML syntax.
	 */
	public List<String> getServicePreconditionExpression(String service_uri);
	
	/**
	 * Return all (Conditional) Effects constructs of the service.
	 */
	public List<ConditionalEffect> getServiceEffects(String service_uri);
	
	/**
	 * see ServiceWrapper
	 */
	public String getSelfType(String r_uri);

	/**
	 * see ServiceWrapper
	 */
	public String getSelfName(String r_uri);
	
	/**
	 * returns a list of all parameters of the given service.
	 */
	public List<Parameter> getServiceParameters(String s_uri);
	
	/**
	 * Returns the type of the given service.
	 * 
	 * @param s_uri URI of the servive 
	 * @return type of the service given by the s_uri
	 */
	public int getServiceType(String s_uri);
	
	/**
	 * print to std out.
	 */
	public boolean printServices();
	
	/**
	 * Returns the service wrapper object of the specified uri iff it is registered.
	 * @param uri
	 * @return
	 */
	public ServiceWrapper getServiceInRegistry(String uri);
	
}
