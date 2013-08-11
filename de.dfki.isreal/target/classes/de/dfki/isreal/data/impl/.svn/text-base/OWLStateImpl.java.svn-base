package de.dfki.isreal.data.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

import de.dfki.isreal.data.State;

/**
 * Implements State. It is initialized with an OutputStream of an String
 * OWLOntology.
 * 
 * @author stenes
 *
 */
public class OWLStateImpl implements State, Serializable {

	private String ont;

	public OWLStateImpl(ByteArrayOutputStream in) {
		ont = in.toString();
	}

	
	public String getOntology() {
		return ont;
	}
	
	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeObject(ont);
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		ont = (String) in.readObject();
	}

}