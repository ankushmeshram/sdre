package de.dfki.isreal.data.impl;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.openrdf.model.Statement;
import org.openrdf.query.GraphQueryResult;
import org.openrdf.query.QueryEvaluationException;

import de.dfki.isreal.data.impl.TransCloseableIterator;


import eu.larkc.core.data.CloseableIterator;
import eu.larkc.core.data.SetOfStatements;

/**
 * Since ISReal uses the Larkc Reasoner interface, a SetOfStatement
 * implementation is needed to connect the Reasoner Implementation with the
 * Plugin specific interface.
 * 
 * This class implements SetOfStatements using the GraphQueryResult from OWLIM.
 *   
 * @author stenes
 *
 */
public class SetOfStatementsQueryResultImpl implements SetOfStatements, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	Set<de.dfki.isreal.data.Statement> set;

	public SetOfStatementsQueryResultImpl(GraphQueryResult result) {
//		//debug
//		int i = 1;
		
		set = new HashSet<de.dfki.isreal.data.Statement>();
		try {
			
//			//debug
//			System.out.println("Start processing results...");
			
			while (result.hasNext()) {
//			if(result.hasNext()) {
				//debug
//				System.out.println("Inserting Triple " + i + " : ");
				
				Statement s = result.next();
				de.dfki.isreal.data.Statement st = new de.dfki.isreal.data.impl.StatementImpl(
						s.getSubject().stringValue(), s.getPredicate()
								.stringValue(), s.getObject().stringValue());
				// debug
//				System.out.println(s.getSubject().stringValue() + ", "  + s.getPredicate().stringValue() + ", " + s.getObject().stringValue());
				set.add(st);
				
//				//debug
//				i++;
			}
		} catch (QueryEvaluationException e) {
			e.printStackTrace();
		}
	}

	public CloseableIterator<Statement> getStatements() {
		CloseableIterator<Statement> res = new TransCloseableIterator(set.iterator());
		return res;
	}

}
