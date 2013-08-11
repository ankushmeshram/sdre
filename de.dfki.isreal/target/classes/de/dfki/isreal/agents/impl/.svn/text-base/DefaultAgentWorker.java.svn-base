package de.dfki.isreal.agents.impl;

import java.util.List;

import de.dfki.isreal.agents.AgentWorker;
import de.dfki.isreal.components.GSE;
import de.dfki.isreal.main.Config;
import de.dfki.isreal.network.protos.ExchangeDataProtos.ISRealMessage;
import de.dfki.isreal.network.protos.ExchangeDataProtos.MetadataRecord;
import de.dfki.isreal.network.protos.ExchangeDataProtos.SensorEvent;
import de.dfki.isreal.network.workers.WorkerThread;

/** 
 * This class implements an abstract worker thread for handling incoming ISReal 
 * messages. The worker identifies the message types and invokes according 
 * methods that are declared in the IAgentWorker interface. The developer of an
 * ISReal agent platform just has to implement the methods. 
 * 
 * SINCE THE DEVELOPER OF AN ISREAL AGENT PLATFORM HAS TO IMPLEMENT THE 
 * AGENTENV INTERFACE, THE IMPLEMENTATION OF THE AGENTWORKER IS NOT PLATFORM
 * SPECIFIC. IT JUST HAVE TO DECODE THE MESSAGES AND USES THE METHODS DEFINED
 * IN AGENTENV TO PROCESS THEM.
 * 
 * SO SIMPLY WRITE ONE AGENTWORKER AND USE 
 * (AgentEnv) Config.getComponent("AgentEnv")) TO GET THE AGENT PLATFORM OBJECT,
 * REGARDLESS THEIR IMPLEMENTATION.
 * 
 * TAKE A LOOK IN DUMMYAGENTWORKER.
 * 
 */
public abstract class DefaultAgentWorker extends WorkerThread implements AgentWorker{

	public DefaultAgentWorker(int sid, ISRealMessage msg) {
		super(sid, msg);
	}

	@Override
	protected void handleMessage(int sid, ISRealMessage msg) {
		if(msg.getMsgType().getNumber() == ISRealMessage.MSGType.SENSOR_EVENT.getNumber()) {
			// TODO remove fixed agent name
			String agent = "NANCY";
			
			// Handle perception messages
			if(msg.getSensorevent().getType().getNumber() == SensorEvent.SensorType.PERCEPTION.getNumber()) {
				List<MetadataRecord> objects = msg.getSensorevent().getRecordList();
				handlePerception(agent,  objects);
			} else {
				//throw new Exception("Unknown ISReal agent message!");
			}
		}
	}
}
