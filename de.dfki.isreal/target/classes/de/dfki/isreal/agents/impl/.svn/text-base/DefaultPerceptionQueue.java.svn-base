package de.dfki.isreal.agents.impl;

import java.util.List;
import java.util.Vector;

import de.dfki.isreal.agents.ExecutionComponent;
import de.dfki.isreal.agents.PerceptionData;
import de.dfki.isreal.agents.PerceptionQueue;
import de.dfki.isreal.network.protos.ExchangeDataProtos.MetadataRecord;

public abstract class DefaultPerceptionQueue implements PerceptionQueue {

	private Vector<PerceptionData> perceptionQueue = null;
	private boolean stop = false;
	private Thread thread = null;
	private int status = ExecutionComponent.NOT_ACTIVATED;
	
	public DefaultPerceptionQueue() {
		perceptionQueue = new Vector<PerceptionData>();
		status = ExecutionComponent.NOT_ACTIVATED;
		thread = new Thread(this);
	}
	
	@Override
	public void run() {
		while(!stop) {
			synchronized(perceptionQueue) {
				if(perceptionQueue.size() == 0) {
					try {
						perceptionQueue.wait();
					} catch (InterruptedException e) {
						//System.out.println("Interrupted");
					}
				}
					
				for(PerceptionData data : perceptionQueue) {
					deliver(data);
				}
				
				perceptionQueue.clear();
			}
		}
	}
	
	@Override
	public void add(String agent, List<MetadataRecord> objects) {
		synchronized(perceptionQueue) {
			// add the new perception data to an existing entry for the agent
			// in the perception queue (if there is one) => batch process
			for(PerceptionData data : perceptionQueue) {
				if(data.getAgentName().equalsIgnoreCase(agent)) {
					data.getObjects().addAll(objects);
					perceptionQueue.notify();
					return;
				}
			}
			
			// there was no entry, so we create one
			perceptionQueue.add(new PerceptionDataImpl(agent, objects));
			perceptionQueue.notify();
		}
	}
	
	/**
	 * This method is called to deliver the perception data to the agent.
	 */
	protected abstract void deliver(PerceptionData data);
	
	@Override
	public void shutdown() {
		stop = true;
		status = ExecutionComponent.SHUTDOWN;
	}
	
	@Override
	public void activate() {
		stop = false;
		thread.start();
		status = ExecutionComponent.ACTIVATED;
	}
	
	@Override
	public void pause() {
		status = ExecutionComponent.PAUSED;
	}
	
	@Override
	public int getStatus() {
		return status;
	}

}
