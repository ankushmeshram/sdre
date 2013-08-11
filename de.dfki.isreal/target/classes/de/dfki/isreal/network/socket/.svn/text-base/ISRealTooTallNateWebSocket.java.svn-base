package de.dfki.isreal.network.socket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

import net.iharder.Base64;
import net.tootallnate.websocket.WebSocket;
import de.dfki.isreal.network.KeepAlive;
import de.dfki.isreal.network.protos.ExchangeDataProtos.ISRealMessage;
import de.dfki.isreal.network.server.ISRealTooTallNateWebSocketServer;

/**
 * SubClass of the ISRealSocket, but without any specific Socket implementation.
 * This implementation is directly stored in the ISRealTooTallNateWebSocketServer.
 * So this class only maintains the KeepAliveThread and the sid, but the
 * functionality is implemented completely in the ISRealTooTallNateWebSocketServer.
 * 
 * @author stenes
 *
 */
public class ISRealTooTallNateWebSocket implements ISRealSocket {
	private static Logger logger = Logger.getLogger(ISRealTooTallNateWebSocket.class);
	
	public int sid;
	private ISRealTooTallNateWebSocketServer server;
	
	private KeepAlive alive;
	
	
	public ISRealTooTallNateWebSocket(ISRealTooTallNateWebSocketServer socketServer){
		server = socketServer;
		
		alive = new KeepAlive(this);
		alive.start();
		
		// register this socket
		sid = SocketRegistry.register(this);
	}
	
	@Override
	public void send(ISRealMessage im) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try {
			im.writeTo(output);
			String msg = Base64.encodeBytes(output.toByteArray());
			server.send(msg, sid);
		} catch (IOException e) {
			logger.error("Error in encoding and sending the data: " + e);
		}
		
	}
	
	public int getId(){
		return sid;
	}

	public void close() {
		alive.setClosed();
		
		// remove from registry
		SocketRegistry.remove(sid);
	}

}
