package de.dfki.isreal.data;

import java.io.Serializable;

/**
 * This is the most used internal object in the semantic components of the
 * ISReal platform. It represents a statement (triple). The format is very low
 * level and all the triple parts are represented as String. So it can easily be
 * transformed to any other Statement format that is used for a number of plugins.
 * 
 * @author stenes
 *
 */
public interface Statement extends Serializable{

	/**
	 * Returns the subject of the statement as URI String.
	 * @return
	 */
	public String getSubjectString();
	
	/**
	 * Returns the predicate of the statement as URI String.
	 * @return
	 */
	public String getPredicateString();
	
	/**
	 * Returns the object of the statement as URI String.
	 * @return
	 */
	public String getObjectString();

	/**
	 * Returns the statement as a human readable string to output it in a logger.
	 * @return
	 */
	public String stringValue();
	
	/**
	 * Compares the statement with another.
	 * @return
	 */
	public boolean equals(Object s);
}
