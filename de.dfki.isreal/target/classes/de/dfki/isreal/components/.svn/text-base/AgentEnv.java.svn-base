package de.dfki.isreal.components;

import java.io.File;
import java.net.URI;
import java.util.List;

import de.dfki.isreal.data.SPARQLDLResult;
import de.dfki.isreal.data.State;
import de.dfki.isreal.data.Statement;
import de.dfki.isreal.network.protos.ExchangeDataProtos.ISRealAnswer;
import de.dfki.isreal.network.protos.ExchangeDataProtos.ISRealQuery;
import de.dfki.isreal.network.protos.ExchangeDataProtos.MetadataRecord;
import de.dfki.isreal.subcomponents.LSE;
import de.dfki.isreal.subcomponents.OMSDecider;
import eu.larkc.core.data.BooleanInformationSet;
import eu.larkc.core.data.SetOfStatements;
import eu.larkc.core.data.VariableBinding;
import eu.larkc.core.qos.QoSParameters;
import eu.larkc.core.query.SPARQLQuery;

/**
 * Public interface that defines the methods, any agent environment has to
 * implement to be included as component in the ISReal platform.
 * 
 * Here it is perception handling, query answering and goal setting.
 * 
 * @todo This interface should extend LSE (or at least the Reasoner and
 * SemanticReasoner Interfaces), so a User could ask any query to an Agent.
 * A direct extension is not possible since the user have to give also the agent
 * id as input. 
 * 
 * 
 * @author stenes, swarwas
 *
 */
public interface AgentEnv {
	
	/**
	 * Called to pass a list of perceived objects to the agent.
	 * 
	 * @param agentID agent instance URI in the platform
	 * @param perceptions list of perceived objects
	 */
	public void handlePerception(String agentID, List<MetadataRecord> perceptions);

	public State getInitialStateFromKB(String agentID);
	
	/**
	 * Initializes the data of globalSES by loading ontology files
	 * into the internal KB's of the Plugins.  
	 *  
	 * @param agentID agent instance URI in the platform
	 */
	public void initFromFiles(String agentID, List<File> ontologies);
		
	/**
	 * removes Axiom a from all internal KB's in all Plugins
	 *  
	 * @param agentID agent instance URI in the platform
	 */
	public void remove(String agentID, List<Statement> a);
	
	/**
	 * inserts Axiom a into all KB's of all Plugins.
	 *  
	 * @param agentID agent instance URI in the platform
	 */
	public void insert(String agentID, List<Statement> a);

	/**
	 * Takes a list a of statements. Deletes all the statements s with
	 * a[0].subject = s.subject and inserts a into the triple store.
	 *  
	 *   
	 * @param agentID agent instance URI in the platform
	 * @param a
	 * @return
	 */
	public void update(String agentID, List<Statement> a);
	
	/**
	 * Checks the consistency of the reasoner knowledge base.
	 *  
	 * @param agentID agent instance URI in the platform
	 * @return
	 */
	public boolean checkKBConsistency(String agentID);
	
	/**
	 * Checks the consistency of class c in the reasoner knowledge base.
	 *  
	 * @param agentID agent instance URI in the platform
	 * @param c
	 * @return
	 */
	public boolean checkClassConsistency(String agentID, URI c);
	
	/**
	 * Check if c1 subsumes c2.
	 *  
	 * @param agentID agent instance URI in the platform
	 * @param c1
	 * @param c2
	 * @return
	 */
	public boolean checkClassSubsumption(String agentID, URI c1, URI c2);
	
	/**
	 * Checks if c1 is an equivalent class of c2.
	 *  
	 * @param agentID agent instance URI in the platform
	 * @param c1
	 * @param c2
	 * @return
	 */
	public boolean checkClassEquivalence(String agentID, URI c1, URI c2);
	
	/**
	 * Checks if c1 is disjunct to c2.
	 *  
	 * @param agentID agent instance URI in the platform
	 * @param c1
	 * @param c2
	 * @return
	 */
	public boolean checkClassDisjunction(String agentID, URI c1, URI c2);
	
	
	/**
	 * Checks if i is an instance of c.
	 *  
	 * @param agentID agent instance URI in the platform
	 * @param i
	 * @param c
	 * @return
	 */
	public boolean instanceChecking(String agentID, URI i, URI c);
	
	/**
	 * Given a sparql or sparqldl query this method returns the result of that
	 * query. 
	 *  
	 * @param agentID agent instance URI in the platform
	 * @param query
	 * @return
	 */
	public SPARQLDLResult sparqldlProcessing(String agentID, String query);
	
	/**
	 * Forces the reasoner plugin to classify his knowledge base.
	 *  
	 * @param agentID agent instance URI in the platform
	 * @return
	 */
	public void classify(String agentID);
	
	/**
	 * Forces the reasoner plugin to realize his knowledge base.
	 *  
	 * @param agentID agent instance URI in the platform
	 * @return
	 */
	public void realize(String agentID);
	
	/**
	 * Returns a list of all statement that are in the knowledge base of the
	 * reasoner plugin. This method is only used for the gui.
	 *  
	 * @param agentID agent instance URI in the platform
	 * @return
	 */
	public List<Statement> listStatements(String agentID);
	
	/**
	 * Triggers the STAR algorithm, given a set of entities and a number i the
	 * method returns the i-top minimal trees that connects all the entities
	 * in the list.
	 *  
	 * @param agentID agent instance URI in the platform
	 * @param entities
	 * @param i
	 * @param props
	 * @return
	 */
	public List<List<Statement>> computeTopRelationalTrees(String agentID, List<String> entities, int i, boolean props);
	
	/**
	 * 
	 * @param agentID
	 * @param theQuery
	 * @param theQoSParameters
	 * @return
	 */
	public VariableBinding sparqlSelect(String agentID, SPARQLQuery theQuery, QoSParameters theQoSParameters);
	
	/**
	 * 
	 * @param agentID
	 * @param theQuery
	 * @param theQoSParameters
	 * @return
	 */
	public SetOfStatements sparqlConstruct(String agentID, SPARQLQuery theQuery, QoSParameters theQoSParameters);
	
	/**
	 * 
	 * @param agentID
	 * @param theQuery
	 * @param theQoSParameters
	 * @return
	 */
	public SetOfStatements sparqlDescribe(String agentID, SPARQLQuery theQuery, QoSParameters theQoSParameters);
	
	/**
	 * 
	 * @param agentID
	 * @param theQuery
	 * @param theQoSParameters
	 * @return
	 */
	public BooleanInformationSet sparqlAsk(String agentID, SPARQLQuery theQuery, QoSParameters theQoSParameters);
	
	public void handleAgentQuery(String agent, ISRealQuery query);
}
