package de.dfki.isreal.network.callerfactories;

import java.util.List;

import org.apache.log4j.Logger;

import de.dfki.isreal.components.Graphics;
import de.dfki.isreal.network.CallerThread;
import de.dfki.isreal.network.ISRealMessageFactory;
import de.dfki.isreal.network.protos.ExchangeDataProtos.ISRealMessage;
import de.dfki.isreal.network.socket.SocketRegistry;

/**
 * CallerFactory implementing the Graphics Interface.
 * @author stenes
 *
 */
public class GraphicsCallerFactory implements Graphics {
		private static Logger logger = Logger.getLogger(CallerFactory.class);
		private static String graphicsRoleName = "Graphics";
		
		public void sendAnimationCall(String method, List<String> params){
			if (SocketRegistry.isRoleRegistered(graphicsRoleName)){
				ISRealMessage m = ISRealMessageFactory.createAnimationCall(method, params);
				int sid = SocketRegistry.getSocketIdFromRole(graphicsRoleName);
				CallerThread t = new CallerThread(sid, m, true);
				t.start();
			} else {
				logger.error("Not connected to component with role: " + graphicsRoleName);
			}
		}
		
}
