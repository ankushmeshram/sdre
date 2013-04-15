package de.dfki.isreal.data.impl;

import java.util.List;

/**
 * This class represent a List of output URIs.
 * Since this list may be null (what causes an exception) this class manages
 * also a NULL boolean.
 * 
 * It is used in the ServiceExecution.
 * 
 * @author stenes
 *
 */
public class Output {
	
	private boolean is_null = true;
	private List<String> outs = null;
	
	public Output(List<String> o){
		if (o != null){
			outs = o;
			is_null = false;
		}
	}

	public Output() {}

	public boolean isNull(){
		return is_null;
	}

	public List<String> getList() {
		return outs;
	}

	public void setList(List<String> out) {
		outs = out;		
	}
}
