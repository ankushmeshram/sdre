package de.dfki.isreal.semantic.services;

import java.net.URISyntaxException;
import java.util.HashMap;

import de.dfki.isreal.data.ServiceWrapper;
import de.dfki.isreal.data.impl.ServiceWrapperImpl;

/**
 *  Shared service pool where service registries actually get service objects from.
 *  This class has been added mainly for optimization. Every agent in the agent
 *  environment maintains a separate service registry but object services are
 *  domain-dependent (instead of agent-dependent).
 *  
 * @author Patrick Kapahnke
 */
public class ServicePool {

	private HashMap<String, ServiceWrapper>		services = new HashMap<String, ServiceWrapper>();
	
	public ServiceWrapper getService(String uri) {
		return services.get(uri);
	}
	
	public synchronized ServiceWrapper addService(String uri, String physUri) throws URISyntaxException {
		ServiceWrapper sWrapper =new ServiceWrapperImpl(uri, physUri); 
		services.put(uri, sWrapper);
		return sWrapper;
	}
	
	public boolean contains(String uri) {
		return services.containsKey(uri);
	}
}