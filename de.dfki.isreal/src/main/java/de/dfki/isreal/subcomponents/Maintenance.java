package de.dfki.isreal.subcomponents;

import java.io.File;
import java.util.List;

import de.dfki.isreal.data.Statement;

public interface Maintenance {

	/**
	 * Initializes the data of globalSES by loading ontology files
	 * into the internal KB's of the Plugins.  
	 */
	public void initFromFiles(List<File> ontologies);
		
	/**
	 * removes Axiom a from all internal KB's in all Plugins
	 */
	public void remove(List<Statement> a);
	
	/**
	 * inserts Axiom a into all KB's of all Plugins.
	 */
	public void insert(List<Statement> a);

	/**
	 * Takes a list a of statements. Deletes all the statements s with
	 * a[0].subject = s.subject and inserts a into the triple store.
	 *  
	 * @param a
	 * @return
	 */
	public void update(List<Statement> a);
}
