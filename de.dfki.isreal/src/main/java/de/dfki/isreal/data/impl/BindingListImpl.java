package de.dfki.isreal.data.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

import de.dfki.isreal.data.BindingList;

/**
 * Implementation of the BindingList. It is used for the service invokation and 
 * the planner output.
 * 
 * @author stenes
 *
 */
public class BindingListImpl implements BindingList, Serializable {

	private HashMap<String, String> bnd_list;

	public BindingListImpl() {
		bnd_list = new HashMap<String, String>();
	}

	@Override
	public String substituteVariable(String var) {
		if (var.trim().startsWith("?")) {
			String v = var.substring(var.indexOf("?") + 1);
			String res = bnd_list.get(v);
			if (res == null) {
				return var;
			} else {
				return res;
			}
		}
		return var;
	}

	public void addPair(String var, String ind) {
		bnd_list.put(var, ind);
	}

	@Override
	public String getInstance(String var) {
		return bnd_list.get(var);
	}

	@Override
	public void print() {
		for (String var : getVariableList()){
			System.out.println(var + " -> " + bnd_list.get(var));
		}
	}
	
	@Override
	public String toStringValue() {
		String res = "";
		for (String var : getVariableList()){
			res = res + var + " -> " + bnd_list.get(var) + "\n";
		}
		return res;
	}

	@Override
	public Set<String> getVariableList() {
		return bnd_list.keySet();
	}

}
