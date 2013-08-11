package de.dfki.isreal.network.socket;

import de.dfki.isreal.network.protos.ExchangeDataProtos.ISRealMessage;

/**
 * Simple interface for an ISReal Socket. Several implementing classes differ
 * according side (server or client) or the implementation WebSocket. 
 * 
 * @author stenes
 *
 */
public interface ISRealSocket {
	
	/**
	 * Sends the given ISRealMessage 'im' over the Socket Channel.
	 * @param im
	 */
	public void send(ISRealMessage im);
}
