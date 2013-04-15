package de.dfki.isreal.data.impl;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;


import eu.larkc.core.data.CloseableIterator;
import eu.larkc.core.data.VariableBinding.Binding;


/**
 * CloseableIterator implementation used in LarKC. It is needed for the
 * SetOfStatements implementation.
 * 
 * @author stenes
 */
public class CloseableIteratorImpl implements CloseableIterator<Binding>,
		Serializable {

	int actual_item;
	List<Binding> iterator;
	boolean closed = false;

	public CloseableIteratorImpl(List<Binding> b_list) {
		iterator = b_list;
		actual_item = -1;
	}

	
	public void close() {
		closed = true;
	}

	
	public boolean hasNext() {
		return (actual_item + 1) < iterator.size();
	}

	
	public boolean isClosed() {
		return closed;
	}

	
	public Binding next() {
		actual_item++;
		Binding b = iterator.get(actual_item);
		return b;
	}

	
	public void remove() {
		iterator.remove(actual_item);
		actual_item--;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeObject(iterator);
		out.writeBoolean(closed);
		out.writeInt(actual_item);
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		iterator = (List<Binding>) in.readObject();
		closed = in.readBoolean();
		actual_item = in.readInt();
	}

}
