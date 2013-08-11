package de.dfki.isreal.data.impl;

import java.io.IOException;
import java.util.List;

import com.hp.hpl.jena.query.ResultSet;

import de.dfki.isreal.data.SPARQLDLResult;
import eu.larkc.core.data.CloseableIterator;
import eu.larkc.core.data.VariableBinding;
import eu.larkc.core.data.VariableBinding.Binding;

/**
 * SPARQLDLResult implementation.
 * 
 * @author stenes
 *
 */
public class SPARQLDLResultImpl implements SPARQLDLResult {

	private boolean isAskRes;
	private boolean askRes;
	private VariableBinding bnd = null;
	
	
	public SPARQLDLResultImpl(boolean b){
		isAskRes = true;
		askRes = b;
	}
	
	public SPARQLDLResultImpl(ResultSet rs){
		isAskRes = false;
		askRes = false;
		bnd = new VariableBindingResultSetImpl(rs);
	}
	
	public SPARQLDLResultImpl(VariableBinding vb){
		isAskRes = false;
		askRes = false;
		bnd = vb;
	}
	
	@Override
	public VariableBinding getBinding() {
		return bnd;
	}

	@Override
	public boolean getBoolean() {
		return askRes;
	}

	@Override
	public boolean isBinding() {
		return !isAskRes;
	}

	@Override
	public boolean isBoolean() {
		return isAskRes;
	}
	
	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeBoolean(isAskRes);
		out.writeBoolean(askRes);
		out.writeObject(bnd);
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		isAskRes = in.readBoolean();
		askRes = in.readBoolean();
		bnd = (VariableBinding) in.readObject();
	}

}
