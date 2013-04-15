package de.dfki.isreal.agents;

import java.util.List;

import de.dfki.isreal.network.protos.ExchangeDataProtos.MetadataRecord;

/**
 * The perception queue is used to deliver perceptions (Metadata Records) coming from the ISReal 
 * platform (agent sensors) to the agents.
 * 
 * @author stefanwarwas
 *
 */
public interface PerceptionQueue extends Runnable, ExecutionComponent {

	/**
	 * Add a new MetadataRecord to the perception queue.
	 * 
	 * @param agent
	 * @param objects
	 */
	public void add(String agent, List<MetadataRecord> objects);
	
	/**
	 * Start the perception queue.
	 */
	@Override
	public void activate();
	
	/**
	 * Stop the perception queue (cannot be resumed).
	 */
	@Override
	public void shutdown();
	
	/**
	 * Pauses the perception queue. No more perceptions will be delivered until start() is called
	 * again.
	 */
	@Override
	public void pause();
	
}
