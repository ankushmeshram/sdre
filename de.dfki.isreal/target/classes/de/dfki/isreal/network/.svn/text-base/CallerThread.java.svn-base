package de.dfki.isreal.network;

import org.apache.log4j.Logger;

import de.dfki.isreal.network.protos.ExchangeDataProtos.ISRealAnswer;
import de.dfki.isreal.network.protos.ExchangeDataProtos.ISRealMessage;
import de.dfki.isreal.network.socket.SocketRegistry;

/**
 * ISReal components can use a CallerFactory in order to create CallerThreads.
 * CallerThreads are initialized with an Message and an destination. Mostly they
 * send the message to their destination and die. If an answer is expected to
 * that message, they register as a listener in the ListenerRegistry, where they
 * wait for the WorkerThread who is responsible for the incoming answer and wakes
 * them up. After that they return the answer.
 * 
 * @author stenes
 * 
 */
public class CallerThread extends Thread {

	private static Logger logger = Logger.getLogger(CallerThread.class);

	private int sid = -1; // source socket id of the message
	private ISRealMessage msg = null;
	private boolean wait = false;
	private ISRealAnswer answer = null;

	public CallerThread(int sid, ISRealMessage msg, boolean isAnswerExpected) {
		this.sid = sid;
		this.msg = msg;
		this.wait = isAnswerExpected;
	}

	public void run() {
		// send msg to destination id
		if (SocketRegistry.isRegistered(SocketRegistry.getSocketRole(sid))) {
			SocketRegistry.getISRealSocket(sid).send(msg);
		} 
		if (wait && msg.getMsgType() == ISRealMessage.MSGType.QUERY) {
			int msg_id = msg.getQuery().getQueryId();
			ListenerRegistry.register(msg_id, this);
			while (true) {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					break;
				}
			}

			if (answer == null) {
				logger.error("Thread woke up with no answer!");
			}
		}
		if (wait && msg.getMsgType() == ISRealMessage.MSGType.ANIMATION_CALL) {
			if (msg.getAnimationcall().getAck()){
			int msg_id = msg.getAnimationcall().getAnimationId();
			ListenerRegistry.register(msg_id, this);
			while (true) {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					break;
				}
			}

			if (answer == null) {
				logger.error("Thread woke up with no answer!");
			}
			}
		}
	}

	public void setAnswer(ISRealAnswer a) {
		answer = a;
		interrupt();
	}

	public ISRealAnswer getAnswer() {
		return answer;
	}

}
