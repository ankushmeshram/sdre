package de.dfki.isreal.agents.impl;

import java.util.List;

import org.apache.log4j.Logger;

import de.dfki.isreal.agents.AgentWorker;
import de.dfki.isreal.components.AgentEnv;
import de.dfki.isreal.components.GSE;
import de.dfki.isreal.main.Config;
import de.dfki.isreal.network.ISRealMessageFactory;
import de.dfki.isreal.network.ListenerRegistry;
import de.dfki.isreal.network.protos.ExchangeDataProtos.AgentQuery;
import de.dfki.isreal.network.protos.ExchangeDataProtos.ISRealAnswer;
import de.dfki.isreal.network.protos.ExchangeDataProtos.ISRealMessage;
import de.dfki.isreal.network.protos.ExchangeDataProtos.ISRealQuery;
import de.dfki.isreal.network.protos.ExchangeDataProtos.MetadataRecord;
import de.dfki.isreal.network.protos.ExchangeDataProtos.SensorEvent;
import de.dfki.isreal.network.protos.ExchangeDataProtos.Statement;
import de.dfki.isreal.network.socket.SocketRegistry;
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
public class AgentWorkerImpl extends WorkerThread implements AgentWorker{

	private static Logger logger = Logger.getLogger(AgentWorkerImpl.class);
	
	public AgentWorkerImpl(int sid, ISRealMessage msg) {
		super(sid, msg);
	}

	private AgentEnv getAgentEnvironment() {
		return (AgentEnv)Config.getComponent(Config.getComponentNameForRole("AgentEnv"));
	}
	
	@Override
	protected void handleMessage(int sid, ISRealMessage msg) {
		ISRealMessage.MSGType m_type = msg.getMsgType();
		if (!Config.isRoleRegistered("AgentEnv")) {
			logger.error("No AgentEnv registered in Config...");
		} else {
			AgentEnv aenv = getAgentEnvironment();
			
			// SENSOR_EVENT
			if (m_type == ISRealMessage.MSGType.SENSOR_EVENT) {
				SensorEvent sev = msg.getSensorevent();
				String agent = sev.getAgent();
				List<MetadataRecord> mdrs = sev.getRecordList();
				
				aenv.handlePerception(agent, mdrs);
			} else if (m_type == ISRealMessage.MSGType.AGENT_QUERY) {
				AgentQuery aq = msg.getAQuery();
				String agent = aq.getAgent();
				ISRealQuery iq = aq.getQuery();
				
				aenv.handleAgentQuery(agent, iq);
			} else if (m_type == ISRealMessage.MSGType.ANSWER) {
					// wake CallerThread
					ListenerRegistry.wakeCallerThread(msg.getAnswer());
			} 		
			else {
				logger.error("Incomming message type " + m_type
						+ " could not be handled!");
			}
		}
	}

	@Override
	public void handlePerception(String agentID,
			List<MetadataRecord> perceptions) {
		 AbstractAgentPlatform p = (AbstractAgentPlatform)Config.getComponent("AgentEnv");
		 p.handlePerception(agentID, perceptions);
	}

	@Override
	public void createAgent(String agentID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shutDownAgent(String agentID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void assignQuery(String agentID, String query) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pausePlatform() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resumePlatform() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pauseAgent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resumeAgent() {
		// TODO Auto-generated method stub
		
	}
	
	private ISRealAnswer handleQuery(AgentQuery query) {
		String agent = query.getAgent();
		ISRealQuery q = query.getQuery();
		// ISRealAnswer a = ((AgentEnv) Config.getComponent("AgentEnv")).assignQuery(agent, q);
		
		return null; //a;
	}

	private void handleLSEUpdate(String agent, List<Statement>[] changes) {
		System.out.println("");
	}
}
