package de.dfki.isreal.network.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.google.protobuf.Message.Builder;

import de.dfki.isreal.main.Config;
import de.dfki.isreal.network.ISRealMessageFactory;
import de.dfki.isreal.network.protos.ExchangeDataProtos.ISRealMessage;
import de.dfki.isreal.network.socket.ISRealTooTallNateWebSocket;
import net.iharder.Base64;
import net.tootallnate.websocket.WebSocket;
import net.tootallnate.websocket.WebSocketServer;

/**
 * This class is the server side of an bidirectional websocket connection. It
 * has to be invoked one time for every server and can handle (theoretically)
 * any number of websocket connections coming from javascript. Once the
 * server is established for every incoming socket announcement an
 * ISRealWebSocket is created and started.
 * 
 * For this implementation using the TooTallNate WebSockets it was a little
 * bit tricky, since the WebSocket class there is final and it is not possible
 * to write a specific subclass of ISRealWebSocket and TooTallNate's WebSocket.
 * 
 * Therefore this Server keeps an internal list of TooTallNate's WebSocket
 * (int_sockets) and a fake list of ISRealWebSockets, that handle the KeepAlive
 * and map the send Method to the server.
 * 
 * 
 * @author stenes
 *
 */
public class ISRealTooTallNateWebSocketServer extends WebSocketServer {
	private static Logger logger = Logger.getLogger(ISRealTooTallNateWebSocketServer.class);
	
	private HashMap<WebSocket, Integer> int_sockets;
	private HashMap<Integer, ISRealTooTallNateWebSocket> ex_sockets;
	
	private boolean resetOnClientClose = false;
	
	public ISRealTooTallNateWebSocketServer(){
		super(8088, Draft.AUTO);
		int_sockets = new HashMap<WebSocket, Integer>();
		ex_sockets = new HashMap<Integer, ISRealTooTallNateWebSocket>();
	}
	
	public ISRealTooTallNateWebSocketServer(int port){
		super(port, Draft.AUTO);
		int_sockets = new HashMap<WebSocket, Integer>();
		ex_sockets = new HashMap<Integer, ISRealTooTallNateWebSocket>();
	}
	
	public ISRealTooTallNateWebSocketServer(int port, boolean resetOnClientClose){
		super(port, Draft.AUTO);
		int_sockets = new HashMap<WebSocket, Integer>();
		ex_sockets = new HashMap<Integer, ISRealTooTallNateWebSocket>();
		this.resetOnClientClose = resetOnClientClose;
	}
	
	
	@Override
	public void onClientClose(WebSocket arg0) {
		int id = int_sockets.get(arg0);
		ISRealTooTallNateWebSocket s = ex_sockets.remove(id);
		s.close();
		int sid = int_sockets.remove(arg0);
		
		logger.info("Connection closed.");
		
		if(resetOnClientClose)
			Config.getMessageQueue().addMessage(sid, ISRealMessageFactory.createReset());
	}

	@Override
	public void onClientMessage(WebSocket arg0, String arg1) {
		try {
			byte[] decodedBase64 = Base64.decode(arg1);
			ISRealMessage in_msg = ISRealMessage.parseFrom(decodedBase64);
			Config.getMessageQueue().addMessage(int_sockets.get(arg0), in_msg);
		} catch (IOException e) {
			logger.error("Error in decoding and handling a message: " + e);
		}
	}

	@Override
	public void onClientOpen(WebSocket arg0) {
		// only allow one connected Web client.
		// TODO Should be changed in e.g. multi-user scenarios
		if(int_sockets.size() > 0) {
			logger.warn("A second client tried to connect to this server component. Only one client allowed.");
			// send error message
			try {
				ISRealMessage isrealMsg = ISRealMessageFactory.createError("Server already in use. Only one client allowed.");
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				try {
					isrealMsg.writeTo(output);
					String msg = Base64.encodeBytes(output.toByteArray());
					arg0.send(msg);
				} catch (IOException e) {
					logger.error("Error in encoding and sending the data: " + e);
				}
				arg0.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error("Error while sending msg: Server already in use. Only one client allowed.");
			}
			return;
		}
		
		ISRealTooTallNateWebSocket socket = new ISRealTooTallNateWebSocket(this);
		
		int_sockets.put(arg0, socket.getId());
		ex_sockets.put(socket.getId(), socket); 
		
		logger.info("Connection established.");
	}

	public void send(String msg, int sid) {
		Set<WebSocket> except = new HashSet<WebSocket>();
		for(WebSocket in : int_sockets.keySet()){
			if (sid != int_sockets.get(in)){
				except.add(in);
			}
		}
		try {
			this.sendToAllExcept(except, msg);
		} catch (IOException e) {
			logger.error("Error while sending msg: " + msg + "; to sid: " + sid);
		}		
	}
	
	@Override
	public void start(){
		super.start();
		logger.info("----------------------------------------------");
		logger.info("---   WebSocket Server awaits requests...  ---");
		logger.info("----------------------------------------------");
	}

}
