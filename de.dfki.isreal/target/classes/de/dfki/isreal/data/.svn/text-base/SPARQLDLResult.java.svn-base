package de.dfki.isreal.data;

import java.io.Serializable;

import eu.larkc.core.data.VariableBinding;

/**
 * Represents a SPARQLDL query result. A such a result is either a boolean
 * value, for queries without variables or a VariableBinding that provides
 * possible solutions for the variables in the query.
 * @author stenes
 *
 */
public interface SPARQLDLResult extends Serializable {
	
	/**
	 * Checks if the result is a boolean value.
	 * @return
	 */
	public boolean isBoolean();
	
	/**
	 * Returns the boolean value of the result.
	 * @return
	 */
	public boolean getBoolean();
	
	/**
	 * Checks if the result is a VariableBinding.
	 * @return
	 */
	public boolean isBinding();
	
	/**
	 * Returns the VariableBinding of the result.
	 * @return
	 */
	public VariableBinding getBinding();

}
