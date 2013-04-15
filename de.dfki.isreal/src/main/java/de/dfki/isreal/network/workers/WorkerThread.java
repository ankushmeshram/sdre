package de.dfki.isreal.network.workers;

import de.dfki.isreal.network.protos.ExchangeDataProtos.ISRealMessage;
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
public abstract class WorkerThread extends Thread {

	private int sid = -1; // source socket id of the message
	private ISRealMessage msg = null;

	public WorkerThread(int sid, ISRealMessage msg) {
		this.sid = sid;
		this.msg = msg;
	}

	public void run() {
		handleMessage(sid, msg);
	}

	protected abstract void handleMessage(int sid, ISRealMessage msg);
}
