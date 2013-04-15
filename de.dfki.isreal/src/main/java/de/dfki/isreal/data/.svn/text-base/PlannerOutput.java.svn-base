package de.dfki.isreal.data;

import java.io.Serializable;

/**
 * This class represents the result of the SCP planner. It consists of a
 * sequence of service Uris and with their according BindingLists to instantiate
 * the services input variables.
 * 
 * @author stenes
 *
 */
public interface PlannerOutput extends Serializable {
	
	/**
	 * Returns the size of the plan, that are the number of services in the
	 * plan.
	 * @return
	 */
	public int getPlanSize();
	
	/**
	 * Returns the service uri of the given step.
	 * @param step
	 * @return
	 */
	public String getService(int step);
	
	/**
	 * Returns the BindingList of the given step.
	 * @param step
	 * @return
	 */
	public BindingList getServiceBinding(int step);
	
	/**
	 * Checks if the plan is empty, i.e. the number of services is 0.
	 * @return
	 */
	public boolean isEmpty();
	
	/**
	 * Prints the plan to the standard output stream.
	 */
	public void print();
}
