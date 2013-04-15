package de.dfki.isreal.agents.impl;

import java.util.List;

import de.dfki.isreal.agents.ISRealAgent;
import de.dfki.isreal.agents.PerceptionListenerInterface;
import de.dfki.isreal.network.protos.ExchangeDataProtos.MetadataRecord;

public abstract class ConceptPerceptionListener implements PerceptionListenerInterface {
	
	protected ISRealAgent agent = null;
	protected String conceptUri = null;
	//protected String objUri = null;
	protected MetadataRecord ao = null;
	
	public ConceptPerceptionListener(ISRealAgent agent, String conceptUri) {
		this.agent = agent;
		this.conceptUri = conceptUri;
	}
	
	public boolean check(MetadataRecord ao) {
		//ao = agent.getObjectMap().get(objUri);
		//ao = agent.getObjectMap().get(ao.getObject());
		this.ao = ao;
		
		//if(ao == null) return false;
		
		List<String> concepts =  ao.getConceptsList();
		
		if(concepts.size() > 0) {
			for(String concept : concepts) {
				if(concept.equalsIgnoreCase(conceptUri)) {
					//agent.debug(conceptUris[i] + " == " + conceptUri);
					return true;
				}
			}
		}
		return false;
	}

}
