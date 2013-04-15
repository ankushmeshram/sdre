package de.dfki.isreal.agents.impl;

import de.dfki.isreal.agents.ISRealAgent;
import de.dfki.isreal.agents.PerceptionListenerInterface;
import de.dfki.isreal.network.protos.ExchangeDataProtos.MetadataRecord;

public abstract class SPARQLConceptPerceptionListener implements PerceptionListenerInterface {
	
	protected ISRealAgent agent = null;
	protected String conceptUri = null;
	
	public SPARQLConceptPerceptionListener(ISRealAgent agent, String conceptUri) {
		this.agent = agent;
		this.conceptUri = conceptUri;
	}
	
	public boolean check(MetadataRecord ao) {
		return false;
		/*this.ao = ao;
		String objUri = ao.getObject();
		
		for(String objUri : objUris) {
			if(helper.hasType(objUri, conceptUri)) return true;
		}
		
		return false;*/
	}

}
