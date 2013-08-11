package de.dfki.isreal.data.impl;

import de.dfki.isreal.data.BindingList;

/**
 * A invokation task is consists of a service uri and its according BindingList.
 * The tasks can be identified by an id.
 * 
 * @author stenes
 *
 */
public class InvokationTask {
	
	private int id = -1;
	private String uri = null;
	private BindingList bnd = null;

	public InvokationTask(String service_uri, int i, BindingList inp_bnd) {
		uri = service_uri;
		id = i;
		bnd = inp_bnd;
	}

	public String getServiceUri() {
		return uri;
	}

	public int getId() {
		return id;
	}

	public BindingList getBinding() {
		return bnd;
	}

	public void setId(int i) {
		id = i;
	}

	public void setBinding(BindingList b) {
		bnd = b;
	}

}
