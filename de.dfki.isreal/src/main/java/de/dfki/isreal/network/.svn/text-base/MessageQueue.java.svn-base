package de.dfki.isreal.network;

import java.lang.reflect.Constructor;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import de.dfki.isreal.main.Config;
import de.dfki.isreal.network.protos.ExchangeDataProtos.ISRealMessage;
import de.dfki.isreal.network.workers.WorkerThread;

/**
 * 
 * This class is a thread implementation of a synchronized MessageQueue.
 * Messages can be stored in an thread-save way and the message queue itself
 * invokes the handlers (WorkerThread) of the incoming messages. The
 * MessageQueue object is started and stored in the Config class. Other classes
 * can get the instance of MessageQueue by the Config.getMessageQueue method.
 * 
 * At startup, the components send INIT_ACK messages. They are stored in the
 * MessageQueue and can be read out to check what server is already initialized.
 * 
 * @author stenes
 * 
 */
public class MessageQueue implements Runnable {
	private static Logger logger = Logger.getLogger(MessageQueue.class);
	
	private Vector<Message> messageQueue = null;
	//private Vector<Thread> listeners = null;
	private Vector<Integer> init_acks = null;
	private Hashtable<String, Integer> registered_sids = null;

	private boolean stop = false;
	private Thread thread = null;
	//private final Lock lock = new ReentrantLock();

	/**
	 * Initializes the MessageQueue
	 */
	public MessageQueue() {
		messageQueue = new Vector<Message>();
		init_acks = new Vector<Integer>();
		registered_sids = new Hashtable<String, Integer>();
		thread = new Thread(this);
		thread.start();
	}

	/**
	 * The MessageQueue waits for inputs (addMessage) and in case of such an
	 * input it wakes up and creates and starts a WorkerThread for every Message
	 * objects stored. If no message is in the queue, the threads sleeps.
	 * 
	 */
	@Override
	public void run() {

		while (!stop) {
			synchronized (messageQueue) {
				if (messageQueue.size() == 0) {
					try {
						messageQueue.wait();
					} catch (InterruptedException e) {
					}
				}

				for (Message data : messageQueue) {
					String workerclass = Config.usesWorkerThread();
					WorkerThread t;
					try {
						Class worker_class = Class.forName(workerclass);
						Constructor cons = worker_class.getConstructor(
								int.class, ISRealMessage.class);
						Object worker_instance = cons.newInstance(data.getId(),
								data.getISRealMessage());
						t = (WorkerThread) worker_instance;
						t.start();
					} catch (Exception e) {
						e.printStackTrace();
					}
					// WorkerThread t = new WorkerThreadImpl(data.getId(),
					// data.getISRealMessage());

				}
				messageQueue.clear();
			}
		}
	}

	/**
	 * This method adds a new ISRealMessage that was sent from the given sid to
	 * the queue and notifies the running MessageQueue thread to handle the
	 * input (see run()).
	 * If the message is an INIT_ACK, the sid is stored in the init_acks vector.
	 * 
	 * @param socket_id
	 * @param msg
	 */
	public void addMessage(int socket_id, ISRealMessage msg) {
		logger.debug("Message received from: " + socket_id + " with content: " + msg);
		if (msg.getMsgType() == ISRealMessage.MSGType.INIT_ACK) {
			synchronized (init_acks) {
				init_acks.add(socket_id);
			}
		} else if (msg.getMsgType() == ISRealMessage.MSGType.INIT_REGISTER_CLIENT) {
			synchronized (registered_sids) {
				registered_sids.put(msg.getClientName(), socket_id);
			}
		} else if (msg.getMsgType() == ISRealMessage.MSGType.RESET) {
			// terminate using exit code 2 as reset signal
			System.exit(2);
		}
		else {
			synchronized (messageQueue) {
				messageQueue.add(new Message(socket_id, msg));
				messageQueue.notify();
			}
		}
	}

	/**
	 * Stops and kills the MessageQueue. All stored unhandled messages are lost.
	 */
	public void stop() {
		stop = true;
	}
	
	/**
	 * Returns a list of SocketIds from which an INIT_ACK message was received.
	 * 
	 * @return
	 */
	public List<Integer> getAcknowledgedSocketIds(){
		synchronized (init_acks){
			return (List<Integer>) init_acks.clone();
		}
	}

	/**
	 * Returns map of names and socket ids for which an INIT_ROLE message was received.
	 * @return
	 */
	public Hashtable<String, Integer> getRegisteredClientNames() {
		synchronized (registered_sids){
			return (Hashtable<String, Integer>) registered_sids.clone();
		}
	}

}
