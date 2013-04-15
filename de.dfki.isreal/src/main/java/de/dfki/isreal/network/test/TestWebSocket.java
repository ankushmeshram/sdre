package de.dfki.isreal.network.test;

import org.apache.log4j.Logger;
import org.eclipse.jetty.websocket.WebSocket;

public class TestWebSocket implements WebSocket {

	private Logger logger = Logger.getLogger(TestWebSocket.class);
	
	public Outbound out;
	public byte frame = 0;
	
	@Override
	public void onConnect(Outbound arg0) {
		out = arg0;
		logger.info("Connection established.");
	}

	@Override
	public void onDisconnect() {
		logger.info("Connection closed.");
	}

	@Override
	public void onMessage(byte arg0, String arg1) {
		logger.info("Message retrieved, but reception not implemented!!!");
	}

	@Override
	public void onMessage(byte arg0, byte[] arg1, int arg2, int arg3) {
		logger.info("Message retrieved, but reception not implemented!!!");
	}

}
