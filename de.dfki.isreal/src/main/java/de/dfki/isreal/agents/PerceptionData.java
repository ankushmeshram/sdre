package de.dfki.isreal.agents;

import java.util.List;

import de.dfki.isreal.network.protos.ExchangeDataProtos.MetadataRecord;

/**
 * The PerceptionData interface is used by the perception 
 * queue to deliver the perceptions to the agents.
 * 
 * @author warwas
 *
 */
public interface PerceptionData {
	
	public String getAgentName();
	
	public List<MetadataRecord> getObjects();
}
