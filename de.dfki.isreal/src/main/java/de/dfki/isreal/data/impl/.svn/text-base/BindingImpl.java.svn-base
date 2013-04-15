package de.dfki.isreal.data.impl;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.openrdf.model.Value;

import eu.larkc.core.data.VariableBinding.Binding;


/**
 * ISReal implementation for a Larkc Binding. It is needed as part of the data
 * transformation to use the Larkc Reasoner interface. 
 * 
 * @author stenes
 *
 */
public class BindingImpl implements Binding, Serializable {

	List<Value> vals;

	public BindingImpl(List<Value> l) {
		vals = l;
	}
	
	
	public List<Value> getValues() {
		return vals;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeObject(vals);
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		vals = (List<Value>) in.readObject();
	}

}
