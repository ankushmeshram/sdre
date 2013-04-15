package de.dfki.isreal.agents;

import de.dfki.isreal.network.protos.ExchangeDataProtos.MetadataRecord;

/**
 * This interface specifies the methods required by all perception listeners.
 * 
 * @author warwas
 *
 */
public interface PerceptionListenerInterface {
	
	/**
	 * This method is used to check whether the listener is triggered.
	 * 
	 * @param ao
	 * @return
	 */
	public boolean check(MetadataRecord ao);
	
	/**
	 * This method is used to perform the user code when the listener is triggered.
	 */
	public void execute();
	
}
