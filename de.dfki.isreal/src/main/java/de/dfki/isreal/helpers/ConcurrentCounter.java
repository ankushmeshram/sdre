package de.dfki.isreal.helpers;


/**
 * This class is a thread save counter.
 * @author stenes
 *
 */
public class ConcurrentCounter {
	private int count = 0; // count starts at zero

	/**
	 * Sets the counter to the given 'amount' value.
	 * @param amount
	 */
	public synchronized void setCount(int amount) {
		count = amount;
	}
	
	/**
	 * Increases the counter by one.
	 */
	public synchronized void increase(){
		count = count + 1;
	}

	/**
	 * Returns the actual count.
	 * @return
	 */
	public synchronized int getCount() {
		return count;
	}
}
