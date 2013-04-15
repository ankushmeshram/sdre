package de.dfki.isreal.agents.impl;

import java.util.List;

import de.dfki.isreal.agents.PerceptionData;
import de.dfki.isreal.network.protos.ExchangeDataProtos.MetadataRecord;

public class PerceptionDataImpl implements PerceptionData {

	private String agentName = null;
	private List<MetadataRecord> objects = null;
	
	public PerceptionDataImpl(String agentName, List<MetadataRecord> objects) {
		this.agentName = agentName;
		this.objects = objects;
	}
	
	public String getAgentName() {
		return agentName;
	}
	
	public List<MetadataRecord> getObjects() {
		return objects;
	}
}
