package de.dfki.isreal.network.server;

import java.io.IOException;
import java.net.ServerSocket;

import org.apache.log4j.Logger;

import de.dfki.isreal.network.socket.ISRealSocketThread;

/**
 * This class is the server side of an bidirectional socket connection. It has
 * to be invoked one time for every server and can handle (theoretically) any
 * number of socket connections coming from ISRealSocketClient. Once the
 * server is established for every incoming ISRealSocketClient an
 * ISRealSocketThread is created and started.
 *   
 * @author stenes
 *
 */
public class ISRealSocketServer extends Thread {

	private Logger logger = Logger.getLogger(ISRealSocketServer.class);
	private int port = -1;
	
	/**
	 * @param args
	 */
	public ISRealSocketServer(int port){
		this.port = port;
	}


	@Override
	public void run() {
		ServerSocket serverSocket = null;
		boolean listening = true;

		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.err.println("Could not listen on port: " + port);
			System.exit(-1);
		}
		
		logger.info("----------------------------------------------");
		logger.info("---    Server awaits socket requests...    ---");
		logger.info("----------------------------------------------");

		while (listening)
			try {
				new ISRealSocketThread(serverSocket.accept()).start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		try {
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
