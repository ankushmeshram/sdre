package de.dfki.isreal.network;

import de.dfki.isreal.network.protos.ExchangeDataProtos.ISRealMessage;

/**
 * This class is an wrapper for the ISRealMessages stored in the MessageQueue.
 * The object holds the ISRealMessage and the Socket ID from which the message
 * was received.
 * 
 * @author stenes
 *
 */
public class Message {
	
	int sid = -1;
	ISRealMessage msg = null;
	
	public Message(int socket_id, ISRealMessage message){
		msg = message;
		sid = socket_id;
	}
	
	/**
	 * Returns the socket id the message was received from. 
	 * @return
	 */
	public int getId(){
		return sid;
	}
	
	/**
	 * Returns the ISRealMessage.
	 * @return
	 */
	public ISRealMessage getISRealMessage(){
		return msg;
	}
}

