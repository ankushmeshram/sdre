package de.dfki.isreal.network.workers;


import org.apache.log4j.Logger;

import de.dfki.isreal.network.ListenerRegistry;
import de.dfki.isreal.network.protos.ExchangeDataProtos.ISRealMessage;
import de.dfki.isreal.network.protos.ExchangeDataProtos.MetadataRecord;

/**
 * Thread to handle all kind of ISRealMessages. The class is instantiated in
 * every Java ISReal component (agent environment, GSE).
 * 
 * It is assumed that this class has the respective component interface to
 * interact with.
 * 
 * The following messages are handled:
 * 
 * Sensor_Event with type SCRIPT, ZONE_IN, ZONE_OUT are assumed to be handled
 * from the GSE and therefore updates to the triple store are made.
 * 
 * Sensor_Event with type PERCEPTION are assumed to be handled by an agent and
 * therefore the metadata record is written to the perception queue.
 * 
 * 
 * @author stenes
 * 
 */
public class WorkerThreadImpl extends WorkerThread {
	private static Logger logger = Logger.getLogger(WorkerThreadImpl.class);
	
	public WorkerThreadImpl(int sid, ISRealMessage msg) {
		super(sid, msg);
	}

	
	private static void handleAgentPerception(MetadataRecord r) {
		// TODO implement after speaking to Stefan Warwas.

	}

	@Override
	protected void handleMessage(int sid, ISRealMessage msg) {
		ISRealMessage.MSGType m_type = msg.getMsgType();

		if (m_type == ISRealMessage.MSGType.ANSWER) {
			// wake CallerThread
			ListenerRegistry.wakeCallerThread(msg.getAnswer());
			// QUERY
		} else {
			logger.error("Incomming message type " + m_type
					+ " could not be handled!");
		}
		
	}

}
