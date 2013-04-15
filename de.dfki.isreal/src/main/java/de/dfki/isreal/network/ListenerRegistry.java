package de.dfki.isreal.network;

import java.util.HashMap;

import org.apache.log4j.Logger;

import de.dfki.isreal.main.ComponentInitializer;
import de.dfki.isreal.network.protos.ExchangeDataProtos.ISRealAnswer;

/**
 * This class is a static registry for CallerThreads in case they have to wait
 * for an answer.
 * 
 * @author stenes
 *
 */
public class ListenerRegistry {
	private static Logger logger = Logger.getLogger(ListenerRegistry.class);
	
	private static HashMap<Integer, CallerThread> threads;

	/**
	 * Initializes the registry.
	 */
	public static void init(){
		threads = new HashMap<Integer, CallerThread>();
	}
	
	/**
	 * Registers a caller thread object for a message id.
	 * @param msg_id
	 * @param t
	 */
	public static void register(int msg_id, CallerThread t){
		threads.put(msg_id,	t);
	}
	
	/**
	 * Wakes up the thread object that is registered with the query id of the
	 * given ISRealAnswer.
	 * @param a
	 */
	public static void wakeCallerThread(ISRealAnswer a){
		int id = a.getQueryId();
		if (threads.containsKey(id)){
			CallerThread t = threads.remove(id);
			t.setAnswer(a);
		} else{
			logger.error("No thread registered for this msg id.");
		}
	}
}
