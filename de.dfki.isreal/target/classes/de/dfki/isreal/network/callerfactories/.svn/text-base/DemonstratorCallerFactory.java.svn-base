package de.dfki.isreal.network.callerfactories;

import org.apache.log4j.Logger;

import de.dfki.isreal.components.Logging;
import de.dfki.isreal.network.CallerThread;
import de.dfki.isreal.network.ISRealMessageFactory;
import de.dfki.isreal.network.callerfactories.CallerFactory;
import de.dfki.isreal.network.protos.ExchangeDataProtos.ISRealMessage;
import de.dfki.isreal.network.socket.SocketRegistry;

/**
 * CallerFactory implementing the Logging Interface.
 * @author stenes
 *
 */
public class DemonstratorCallerFactory implements Logging {
	private static Logger logger = Logger.getLogger(CallerFactory.class);
	private static String dem_role = "Logging";
	
	public void sendMessage(String msg, int comp, int t, boolean s) {
		ISRealMessage m = ISRealMessageFactory.createDemonstratorMsg(msg, comp, t, s);
		int sid = SocketRegistry.getSocketIdFromRole(dem_role);
		CallerThread ct = new CallerThread(sid, m, false);
		ct.start();
	}
	
	public void sendCommunicationMessage(String msg, String from, String to, boolean s) {
		ISRealMessage m = ISRealMessageFactory.createDemonstratorCommunicationMsg(msg, from, to, s);
		int sid = SocketRegistry.getSocketIdFromRole(dem_role);
		CallerThread ct = new CallerThread(sid, m, false);
		ct.start();
	}
	
	public void sendAgentMessage(String msg, int comp, int t, String from, boolean s) {
		ISRealMessage m = ISRealMessageFactory.createDemonstratorAgentMsg(msg, comp, t, from, s);
		int sid = SocketRegistry.getSocketIdFromRole(dem_role);
		CallerThread ct = new CallerThread(sid, m, false);
		ct.start();
	}
	
}
