package de.dfki.isreal.data.impl;

import java.util.List;

import de.dfki.isreal.data.BindingList;
import de.dfki.isreal.data.PlannerOutput;

/**
 * Implements a PlannerOutput.
 * @author stenes
 *
 */
public class PlannerOutputImpl implements PlannerOutput {

	List<String> plan;
	List<BindingList> bnds;
	
	public PlannerOutputImpl(List<String> p, List<BindingList> bs){
		if (p.size() == bs.size()){
			plan = p;
			bnds = bs;
		}else{
			System.out.println("Error in planner output: plan size does not match number of bindings.");
		}
	}
	
	
	@Override
	public int getPlanSize() {
		return plan.size();
	}

	@Override
	public String getService(int step) {
		return plan.get(step);
	}

	@Override
	public BindingList getServiceBinding(int step) {
		return bnds.get(step);
	}


	@Override
	public boolean isEmpty() {
		return plan.isEmpty();
	}


	@Override
	public void print() {
		int i = 0;
		for (String str : plan){
			System.out.println("--- " + str + " ---");
			bnds.get(i).print();
			i++;
		}
	}

}
