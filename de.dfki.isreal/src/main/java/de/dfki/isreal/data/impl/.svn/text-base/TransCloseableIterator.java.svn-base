package de.dfki.isreal.data.impl;

import java.util.Iterator;
import java.util.NoSuchElementException;

import eu.larkc.core.data.CloseableIterator;

/**
 * CloseableIterator implementation used in LarKC. It is needed for the
 * SetOfStatements implementation.
 * 
 * @author stenes
 */
public class TransCloseableIterator<E> implements CloseableIterator<E> {
	private Iterator<E> iter;
	private boolean isClosed;

	public TransCloseableIterator(
			Iterator<E> iter) {
		if (iter == null) {
			throw new IllegalArgumentException();
		}
		this.iter = iter;
		isClosed = false;
	}

	public boolean hasNext(){
		if (isClosed) {
			throw new IllegalStateException("Iterator is closed!");
		}
		
		boolean b = false;
		try {
			b = iter.hasNext();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	public E next() {
		if (hasNext() == false) {
			throw new NoSuchElementException();
		}
		return iter.next();
	}

	public void remove() {
		try {
			iter.remove();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void close() {
		iter = null;
		isClosed = true;
	}

	public boolean isClosed() {
		return isClosed;
	}
}
