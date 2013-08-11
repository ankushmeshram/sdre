package de.dfki.isreal.data.impl;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openrdf.model.Statement;

import de.dfki.isreal.data.impl.TransCloseableIterator;

import eu.larkc.core.data.CloseableIterator;
import eu.larkc.core.data.SetOfStatements;

/**
 *
 * Since ISReal uses the Larkc Reasoner interface, a SetOfStatement
 * implementation is needed to connect the Reasoner Implementation with the
 * Plugin specific interface.
 * 
 * Implements the Larkc SetOfStatements interface using a List of ISReal
 * Statements.
 * 
 * @author stenes
 *
 */
public class SetOfStatementsStatementListImpl implements SetOfStatements, Serializable {

	Set<de.dfki.isreal.data.Statement> set;
	
	public SetOfStatementsStatementListImpl(List<de.dfki.isreal.data.Statement> sts){
		set = new HashSet<de.dfki.isreal.data.Statement>();
		for (de.dfki.isreal.data.Statement s : sts){
			set.add(s);
		}
	}

	public CloseableIterator<Statement> getStatements() {
		CloseableIterator<Statement> res = new TransCloseableIterator(set
				.iterator());
		return res;
	}

}
