package de.dfki.isreal.agents.impl;

import de.dfki.isreal.agents.ISRealAgent;
import de.dfki.isreal.agents.PerceptionListenerInterface;
import de.dfki.isreal.network.protos.ExchangeDataProtos.MetadataRecord;

/**
 * This class is the default perception listener for an object URI. The check method checks 
 * whether the object URI passed to the constructor matches the object URI in the passed 
 * MetadataRecord in the check method.
 * 
 * 
 * @author warwas
 *
 */
public abstract class ObjectPerceptionListener implements PerceptionListenerInterface {

	protected String objUri = null;
	protected MetadataRecord ao = null;
	protected ISRealAgent agent = null;
	
	/**
	 * Initializes the listener with an object URI, and the agent that registered this
	 * listener.
	 * 
	 * @param agent
	 * @param objUri
	 */
	public ObjectPerceptionListener(ISRealAgent agent, String objUri) {
		this.objUri = objUri;
		this.agent = agent;
	}
	
	/**
	 * 
	 * @param ao
	 * @return
	 */
	@Override
	public boolean check(MetadataRecord ao) {
		this.ao = ao;
		String objUri = ao.getObject();
		
		if(objUri != null && objUri.length() > 0) {
			if(objUri.equalsIgnoreCase(objUri)) return true;
		}
		
		return false;
	}
	
}
