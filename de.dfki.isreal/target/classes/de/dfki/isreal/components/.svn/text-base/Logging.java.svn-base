package de.dfki.isreal.components;

public interface Logging {
	
	/**
	 * This is a normal logging message, that is shown to the user via a
	 * Logging component.
	 * 
	 * @param msg - Msg shown to the user.
	 * @param comp - Component this message is from.
	 * @param t - Type of the message.
	 * @param s - Should this message be spoken. (not used)
	 */
	public void sendMessage(String msg, int comp, int t, boolean s);
	
	/**
	 * In case of agent communication, from defines the agent sender and to
	 * defines the agent receiver.
	 * @param msg - Msg shown to the user.
	 * @param from - Sender of the message.
	 * @param to - Receiver of the message. 
	 * @param s - Should this message be spoken. (not used)
	 */
	public void sendCommunicationMessage(String msg, String from, String to, boolean s);
	
	/**
	 * In case of a multi agent scenario the field from defines the agent,
	 * that has send the logging message.
	 * 
	 * @param msg - Msg shown to the user.
	 * @param comp - Component this message is from.
	 * @param t - Type of the message.
	 * @param from - Sender of the message.
	 * @param s - Should this message be spoken. (not used)
	 */
	public void sendAgentMessage(String msg, int comp, int t, String from, boolean s);

}
