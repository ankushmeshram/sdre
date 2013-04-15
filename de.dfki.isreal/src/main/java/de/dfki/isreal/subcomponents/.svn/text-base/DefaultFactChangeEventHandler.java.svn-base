package de.dfki.isreal.subcomponents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.dfki.isreal.data.Statement;
import de.dfki.isreal.network.callerfactories.AgentEnvCallerFactory;
import de.dfki.isreal.network.callerfactories.DemonstratorCallerFactory;
import de.dfki.isreal.network.protos.ExchangeDataProtos.DemonstratorMessage;
import de.dfki.isreal.network.protos.ExchangeDataProtos.MetadataRecord;
import de.dfki.isreal.network.protos.ExchangeDataProtos.MetadataRecord.Builder;

/**
 * Informs the agent environment about fact changes to supplement
 * the sensor scripts.
 * 
 * @author paka
 *
 */
public class DefaultFactChangeEventHandler extends
		FactChangeEventHandler {

	protected String agent;
		
	protected static AgentEnvCallerFactory agentCaller = new AgentEnvCallerFactory();
	
	protected static DemonstratorCallerFactory demoCaller = new DemonstratorCallerFactory();
	
	public DefaultFactChangeEventHandler(Integer id, MetadataRecord object, String agent) {
		super(id, object);
		this.agent = agent;
	}
		
	@Override
	public void handle() {
		Builder mdrBuilder = MetadataRecord.newBuilder();
		List<MetadataRecord> perceptions = new ArrayList<MetadataRecord>();
		for(String object : objectsToHandle) {
			// set node ID to that of the object to monitor (even for dependent objects)
			mdrBuilder.setNodeId(objectToMonitor.getNodeId());
			mdrBuilder.setObject(object);
			// generate new perception and clear builder
			perceptions.add(mdrBuilder.build());
			mdrBuilder.clear();
		}
				
		// handle perception in specified agent
		agentCaller.handlePerception(agent, perceptions);
		
		// send log message
		//demoCaller.sendMessage("Fact change perception listener triggered: " + object.substring(object.indexOf('#')) + " -> " + agent.substring(agent.indexOf('#')), DemonstratorMessage.Component.GSE.getNumber(), DemonstratorMessage.DMsgType.PERC.getNumber(), false);		
	}

	@Override
	public String toString() {
		return "Id = " + id + ", " + objectToMonitor.getObject().substring(objectToMonitor.getObject().indexOf('#')) + " -> " + agent.substring(agent.indexOf('#'));
	}
}
