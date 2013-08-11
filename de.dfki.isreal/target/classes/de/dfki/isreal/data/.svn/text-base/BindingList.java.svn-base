package de.dfki.isreal.data;

import java.io.Serializable;
import java.util.Set;

/**
 * A BindingList is a set of variable bindings. For a list of variables it
 * provides instances this variable can be substituted with.
 * 
 * @author stenes
 *
 */
public interface BindingList extends Serializable{
	
	/**
	 * This method returns for a given variable (with leading "?") string the
	 * instance this variable can be substituted with. If there is no such
	 * substitution in the binding, the variable string is returned.
	 *  
	 * @param var
	 * @return
	 */
	public String substituteVariable(String var);
	
	/**
	 * This methods add a pair var -> ind to the binding, that substitutes the
	 * variable var with the instance ind.
	 *  
	 * @param var
	 * @param ind
	 */
	public void addPair(String var, String ind);
	
	/**
	 * Prints the binding to the standard output stream.
	 */
	public void print();
	
	/**
	 * Returns the instance string for the given variable or null if it is not
	 * present.
	 * 
	 * @param var
	 * @return
	 */
	public String getInstance(String var);
	
	/**
	 * Returns the list of variables for that there exists a binding.
	 * @return
	 */
	public Set<String> getVariableList();

	/**
	 * Creates a human readable String for debugging.
	 * @return
	 */
	public String toStringValue();
}
