package de.dfki.isreal.network.socket;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import net.iharder.Base64;

import org.apache.log4j.Logger;

import de.dfki.isreal.main.Config;
import de.dfki.isreal.network.protos.ExchangeDataProtos.ISRealMessage;

/**
 * This class is the server side instance of an bidirectional socket connection
 * thread. It is instantiated by the GlobalSESocketServer for every client
 * connection. The constructor stores the Socket object internally and
 * registers the new connection in the SocketRegistry (without an identifying
 * name).
 * 
 * 
 * @author stenes
 *
 */
public class ISRealSocketThread extends Thread implements ISRealSocket {
	private Logger logger = Logger.getLogger(ISRealSocketThread.class);
	
	private Socket socket = null;
	private PrintWriter out;
	private BufferedReader in;
	private int sid;

	/**
	 * The constructor stores the instance by its sid in the SocketRegistry.
	 * @param socket
	 */
	public ISRealSocketThread(Socket socket) {
		super("SocketServerThread");
		try{
		this.socket = socket;
		out = new PrintWriter(socket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(socket
				.getInputStream()));
		sid = SocketRegistry.register(this);
		} catch(IOException e) {
			logger.error("Could not initialize ISRealSocketThread: " + e);
		}
	}

	/**
	 * Live cycle of this class. As long as there are incomming messages they
	 * are handled by 'onMessage'. If the connection is closed, it is removed
	 * from the SocketRegistry. 
	 */
	public void run() {
		try {
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				onMessage(inputLine);
				Thread.yield();
			}
			logger.error("Closing everything!!!");
			out.close();
			in.close();
			socket.close();
			SocketRegistry.remove(sid);

		} catch (IOException e) {
			logger.error("Error in running thread: " + this.getName() +" - " + e);
		}
	}
	
	/**
	 * This class handles incoming messages. The message is decoded from Base64
	 * into a byte array and further into an ISRealMessage using Google protobufs
	 * from de.dfki.isreal.network.protos.
	 * The ISRealMessage is stored in an MessageQueue instance, that has to be
	 * registered at the static Config class.
	 * 
	 * @param data
	 */
	private void onMessage(String data) {
		try {
			byte[] decodedBase64 = Base64.decode(data);
			ISRealMessage in_msg = ISRealMessage.parseFrom(decodedBase64);
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
			out.println(Base64.encodeBytes(output.toByteArray()));
		} catch (IOException e) {
			logger.error("Error in encoding and sending the data: " + e);
		}
	}
}
