package de.dfki.isreal.network.server;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketHandler;

import de.dfki.isreal.network.socket.ISRealJettyWebSocket;

/**
 * This class is the server side of an bidirectional websocket connection. It
 * has to be invoked one time for every server and can handle (theoretically)
 * any number of websocket connections coming from javascript. Once the
 * server is established for every incoming socket announcement an
 * ISRealWebSocket is created and started.
 * 
 * TODO make port changeable
 * 
 * NOTE: Since Chrome changed the WebSocket Client implementation from draft 75
 * to draft 76, the Jetty WebSocket Server is not working anymore. Also using
 * the newest (8.0.1) version of Jetty could not solve the problem.
 * Therefore a new Implementation is used. 
 *   
 * @author stenes
 *
 */
public class ISRealJettyWebSocketServer {

	public ISRealJettyWebSocketServer() {

		Handler handler = new WebSocketHandler() {

			@Override
			protected WebSocket doWebSocketConnect(HttpServletRequest request,
					String protocol) {
				return new ISRealJettyWebSocket();
			}

		};

		Server server = new Server(8080);
		server.setHandler(handler);
		try {
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
