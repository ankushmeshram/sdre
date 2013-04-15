package de.dfki.isreal.agents;

import java.util.List;

import de.dfki.isreal.network.protos.ExchangeDataProtos.MetadataRecord;

/**
 * This interface defines the methods that have to be implemented in order to enable
 * an agent platform to handle ISReal messages. For each message type there is one
 * according method that is responsible for handling it.
 */
public interface AgentWorker {

	/**
	 * Called to pass a list of perceived objects to the agent.
	 * 
	 * @param agentID agent name in the platform
	 * @param perceptions list of perceived objects
	 */
	public void handlePerception(String agentID, List<MetadataRecord> perceptions);
	
	/**
	 * Create a new agent.
	 * 
	 * @param agentID
	 */
	
	public void createAgent(String agentID);
	
	/**
	 * 
	 */
	public void shutDownAgent(String agentID);
	
	public void assignQuery(String agentID, String query);
	
	public void pausePlatform();
	
	public void resumePlatform();
	
	public void pauseAgent();
	
	public void resumeAgent();
	
}
