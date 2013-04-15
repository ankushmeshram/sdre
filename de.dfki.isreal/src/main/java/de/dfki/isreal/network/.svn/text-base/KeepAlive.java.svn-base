package de.dfki.isreal.network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.iharder.Base64;

import org.apache.log4j.Logger;
import org.eclipse.jetty.websocket.WebSocket.Outbound;

import de.dfki.isreal.network.protos.ExchangeDataProtos.ISRealMessage;
import de.dfki.isreal.network.server.ISRealTooTallNateWebSocketServer;
import de.dfki.isreal.network.socket.ISRealSocket;
import de.dfki.isreal.network.socket.ISRealTooTallNateWebSocket;


/**
 * This class is a thread that gets a jetty web socket connection and send
 * KeepAlive messages over that connection in order to prevent the connection
 * from a timeout.
 * 
 * The problem is described at
 * http://java.dzone.com/articles/websocket-chat
 * 
 * @author stenes
 * 
 */
public class KeepAlive extends Thread {
	private Logger logger = Logger.getLogger(KeepAlive.class);

	private ISRealSocket socket;
	private boolean closed = false;

	public KeepAlive(ISRealSocket is) {
		socket = is;
	}

	public void run() {
		while (!closed) {
			waitMillis(20000);
			ISRealMessage im = ISRealMessageFactory.createKeepAlive();

			if (!closed) {
				socket.send(im);
			}
		}
	}

	/**
	 * This methods waits for the given numer of milliseconds.
	 * 
	 * @param i
	 *            - Milliseconds to wait.
	 */
	private static void waitMillis(int i) {
//		long start = System.currentTimeMillis();
//		while ((System.currentTimeMillis() - start) < i) {
//		}
		try {
			Thread.sleep(i);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}

	public void setClosed() {
		closed = true;
	}

}
