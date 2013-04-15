package de.dfki.isreal.data.impl;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.openrdf.model.Statement;

import com.franz.agbase.Triple;
import com.franz.agbase.TriplesIterator;

import de.dfki.isreal.data.impl.TransCloseableIterator;


import eu.larkc.core.data.CloseableIterator;
import eu.larkc.core.data.SetOfStatements;


/**
 * 
 *  *
 * Since ISReal uses the Larkc Reasoner interface, a SetOfStatement
 * implementation is needed to connect the Reasoner Implementation with the
 * Plugin specific interface.
 * 
 * SetOfStatements implementation used in the AllegroGraph Plugin.
 * 
 * @author stenes
 *
 */
public class SetOfStatementsTriplesIteratorImpl implements SetOfStatements, Serializable {

	Set<de.dfki.isreal.data.Statement> set;

	
	public SetOfStatementsTriplesIteratorImpl(TriplesIterator cc) {
		set = new HashSet<de.dfki.isreal.data.Statement>();
		while(cc.hasNext()){
			Triple t = cc.next();
			String s = t.getSubject().toString();
			String p = t.getPredicate().getURI().toString();
			String o = t.getObject().toString();
			de.dfki.isreal.data.Statement st = new de.dfki.isreal.data.impl.StatementImpl(
					s, p, o);
			set.add(st);
		}
	}
	

	public CloseableIterator<Statement> getStatements() {
		CloseableIterator<Statement> res = new TransCloseableIterator(set
				.iterator());
		return res;
	}

}
