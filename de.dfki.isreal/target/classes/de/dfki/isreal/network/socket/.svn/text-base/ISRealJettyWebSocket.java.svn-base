package de.dfki.isreal.network.socket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.iharder.Base64;

import org.apache.log4j.Logger;
import org.eclipse.jetty.websocket.WebSocket;

import de.dfki.isreal.main.Config;
import de.dfki.isreal.network.KeepAlive;
import de.dfki.isreal.network.protos.ExchangeDataProtos.ISRealMessage;

/**
 * 
 * WebSocket for the communication to one browser handling the graphic
 * environment. It is assumed that every message from the browser is handled 
 * by the ISRealMessageHandler and the respective component implementation.
 * Every outgoing message to the browser is created by the ISRealMessageFactory. 
 * 
 * NOTE: Since Chrome changed the WebSocket Client implementation from draft 75
 * to draft 76, the Jetty WebSocket Server is not working anymore. Also using
 * the newest (8.0.1) version of Jetty could not solve the problem.
 * Therefore a new Implementation is used. 
 * 
 * @author stenes
 * 
 */
public class ISRealJettyWebSocket implements WebSocket, ISRealSocket {

	private Logger logger = Logger.getLogger(ISRealJettyWebSocket.class);

	public Outbound out;
	public byte frame = 0;
	public int sid;
	public KeepAlive alive;

	/**
	 * The constructor registers the socket with its sid and a static name for
	 * the RTSGE (has to be changed if we have more than one connection to
	 * browsers) in the SocketRegistry. 
	 */
	public ISRealJettyWebSocket() {
		super();
		sid = SocketRegistry.register(this);
	}

	/**
	 * On connect a KeepAlive thread is started in order to send message in a
	 * predefined interval. This prevents the WebSocket Connection from timeout.
	 */
	@Override
	public void onConnect(Outbound arg0) {
		out = arg0;
		// just for testing
		//SendRandomMessageThread t = new SendRandomMessageThread();
		//t.start();
		//
		
		
		alive = new KeepAlive(this);
		alive.start();
		logger.info("Connection established.");
	}

	/**
	 * On disconnect the KeepAlive thread is closed.
	 */
	@Override
	public void onDisconnect() {
		alive.setClosed();
		logger.info("Connection closed.");
	}

	/**
	 * This class handles incoming messages. The message is decoded from Base64
	 * into a byte array and further into an ISRealMessage using Google protobufs
	 * from de.dfki.isreal.network.protos.
	 * The ISRealMessage is stored in an MessageQueue instance, that has to be
	 * registered at the static Config class.
	 * 
	 * @param frame - (length of msg??) read specification in WebSocket implementation.
	 * @param data
	 */
	@Override
	public void onMessage(byte frame, String data) {
		try {
			byte[] decodedBase64 = Base64.decode(data);
			ISRealMessage in_msg = ISRealMessage.parseFrom(decodedBase64);
			Config.getMessageQueue().addMessage(sid, in_msg);
		} catch (IOException e) {
			logger.error("Error in decoding and handling a message: " + e);
		}
	}

	/**
	 * This method is not used yet.
	 */
	@Override
	public void onMessage(byte frame, byte[] data, int arg2, int arg3) {
		logger.info("Message retrieved, but reception not implemented!!!");
		logger.info("frame: " + frame);
		logger.info("data: " + data);
		logger.info("arg2: " + arg2);
		logger.info("arg3: " + arg3);
		try {
			byte[] decodedBase64 = Base64.decode(data);
			ISRealMessage in_msg = ISRealMessage.parseFrom(decodedBase64);
			logger.info("Msg: " + in_msg);
			Config.getMessageQueue().addMessage(sid, in_msg);
		} catch (IOException e) {
			logger.error("Error in decoding and handling a message: " + e);
		}
	}
	
	/**
	 * Sends an ISRealMessage to the other end of the connection. The
	 * ISRealMessage is constructed using Google protobufs from
	 * de.dfki.isreal.network.protos.
	 * Further it is encoded into an Base64 output format before sending.
	 * 
	 * @param im
	 */
	public void send(ISRealMessage im){
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try {
			im.writeTo(output);
			out.sendMessage((byte) 0, Base64.encodeBytes(output.toByteArray()));
		} catch (IOException e) {
			logger.error("Error in encoding and sending the data: " + e);
		}
	}

}
