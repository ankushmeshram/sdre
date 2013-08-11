package de.dfki.isreal.subcomponents;

import java.net.URI;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;


import de.dfki.isreal.data.SPARQLDLResult;
import de.dfki.isreal.data.Statement;

public interface SemanticReasoner {

	/**
	 * Checks the consistency of the reasoner knowledge base.
	 * @return
	 */
	public boolean checkKBConsistency();
	
	/**
	 * Checks the consistency of class c in the reasoner knowledge base.
	 * @param c
	 * @return
	 */
	public boolean checkClassConsistency(URI c);
	
	/**
	 * Check if c1 subsumes c2.
	 * @param c1
	 * @param c2
	 * @return
	 */
	public boolean checkClassSubsumption(URI c1, URI c2);
	
	/**
	 * Checks if c1 is an equivalent class of c2.
	 * @param c1
	 * @param c2
	 * @return
	 */
	public boolean checkClassEquivalence(URI c1, URI c2);
	
	/**
	 * Checks if c1 is disjunct to c2.
	 * @param c1
	 * @param c2
	 * @return
	 */
	public boolean checkClassDisjunction(URI c1, URI c2);
	
	
	/**
	 * Checks if i is an instance of c.
	 * @param i
	 * @param c
	 * @return
	 */
	public boolean instanceChecking(URI i, URI c);
	
	/**
	 * Given a sparql or sparqldl query this method returns the result of that
	 * query. 
	 * @param query
	 * @return
	 */
	public SPARQLDLResult sparqldlProcessing(String query);
	
	public Set<OWLClass> dlEquivalentClasses(String classExp);
	public Set<OWLClass> dlEquivalentClasses(OWLClass clas);
	public Set<OWLClass> dlSubClasses(String classExp, boolean direct);
	public Set<OWLClass> dlSubClasses(OWLClass clas, boolean direct);
	public OWLClass getSensorForProperty(OWLClass propClass);
	
	
	/**
	 * Forces the reasoner plugin to classify his knowledge base.
	 * @return
	 */
	public void classify();
	
	/**
	 * Forces the reasoner plugin to realize his knowledge base.
	 * @return
	 */
	public void realize();
	
	/**
	 * Returns a list of all statement that are in the knowledge base of the
	 * reasoner plugin. This method is only used for the gui.
	 * @return
	 */
	public List<Statement> listStatements();
	
	/**
	 * Triggers the STAR algorithm, given a set of entities and a number i the
	 * method returns the i-top minimal trees that connects all the entities
	 * in the list.
	 * @param entities
	 * @param i
	 * @param props
	 * @return
	 */
	public List<List<Statement>> computeTopRelationalTrees(List<String> entities, int i, boolean props);
}
