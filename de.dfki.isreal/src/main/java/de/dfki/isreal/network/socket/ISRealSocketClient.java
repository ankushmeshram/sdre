package de.dfki.isreal.network.socket;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import net.iharder.Base64;

import org.apache.log4j.Logger;

import de.dfki.isreal.main.Config;
import de.dfki.isreal.network.protos.ExchangeDataProtos.ISRealMessage;


/**
 * ISRealSocketClient is used to connect to the ISRealSocketServer at the
 * 'host' location over the given 'port'.
 * The connection is established at once in the constructor that gets a String
 * 'name' to register the socket thread in the SocketRegistry. The class is an 
 * extension to the class Thread, so every instance of ISRealSocketClient is
 * a running thread handling the in- and output stream to the GlobalSEServer.
 * 'onMessage' implements the reaction of incoming messages from the
 * ISRealServer and 'send' puts ISRealMessages to the ISRealServer.
 * To handle incoming messages the static Config class is asked for an instance
 * of an MessageQueue. This instance must be registered in the Config before
 * using ISRealSocketClient.
 *  
 *  
 * @author stenes
 *
 */
public class ISRealSocketClient extends Thread implements ISRealSocket {
	private static Logger logger = Logger.getLogger(ISRealSocketClient.class);

	private Socket socket = null;
	private PrintWriter out = null;
	private BufferedReader in = null;

	private String host = "134.96.184.229";
	private int port = 4444;
	private String name = "default";
	private List<String> roles;
	
	private int sid;

	/**
	 * 
	 * This class uses the given 'post' and 'host' to connect to a 
	 * ISRealSocketServer. If a connection can be established it is written
	 * into the SocketRegistry and the life cycle of this class is started, i.e.
	 * the socket connection to the ISRealSocketServer.  
	 * 
	 * @param name - Unique name for this socket, to identify it in the SocketRegistry.
	 * @throws IOException
	 */
	public ISRealSocketClient(String n, List<String> rs) throws IOException {
		super("SocketClientThread");
		name = n;
		roles = rs;
		init();
	}
	
	/**
	 * 
	 * @param n - name of socket
	 * @param h - host to connect
	 * @param p - port to connect
	 * @throws IOException
	 */
	public ISRealSocketClient(String n, List<String> rs, String h, int p) throws IOException {
		super("SocketClientThread");
		name = n;
		host = h;
		port = p;
		roles = rs;
		init();
	}
	
	private void init() throws IOException {
		try {
			socket = new Socket(host, port);
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket
					.getInputStream()));
			sid = SocketRegistry.register(this);
			SocketRegistry.register(name, sid, roles);
			logger.debug("ISRealSocketClient - init : Registered " + name + " with sid: " + sid);
			this.start();
		} catch (UnknownHostException e) {
			logger.error("Don't know about host: " + host + " - " + e);
		}
	}

	/**
	 * Live cycle of this class. As long as there are incomming messages they
	 * are handled by 'onMessage'. If the connection is closed, it is removed
	 * from the SocketRegistry. 
	 */
	public void run() {
		try{
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			onMessage(inputLine);
			Thread.yield();
		}

		out.close();
		in.close();
		socket.close();
		SocketRegistry.remove(sid);
		}catch(IOException e){
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
	public void send(ISRealMessage im) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try {
			im.writeTo(output);
			out.println(Base64.encodeBytes(output.toByteArray()));
		} catch (IOException e) {
			logger.error("Error in encoding and sending the data: " + e);
		}
	}
}
