package de.dfki.isreal.agents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Semaphore;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.ListenableUndirectedWeightedGraph;

import de.dfki.isreal.components.GSE;
import de.dfki.isreal.data.BindingList;
import de.dfki.isreal.network.callerfactories.DemonstratorCallerFactory;
import de.dfki.isreal.network.protos.ExchangeDataProtos.DemonstratorMessage;
import de.dfki.isreal.network.protos.ExchangeDataProtos.ISRealQuery;
import de.dfki.isreal.network.protos.ExchangeDataProtos.MetadataRecord;
import de.dfki.isreal.subcomponents.LSE;

public interface ISRealAgent extends ExecutionComponent {
	
	public static final int APPEAR = 0;
	public static final int DISAPPEAR = 1;
	public static final int MODIFY = 2;
	
	/**
	 * Returns the agent's plain name (without portal, etc.).
	 */
	public String getAgentName();
	
	/**
	 * Returns the agent's object URI with which it is identified in the ISReal platform.
	 */
	public String getSelfURI();
	
	/**
	 * Returns the agent's local semantic environment.
	 * 
	 * @return the agent's LSE
	 */
	public LSE getLocalSE();
	
	/**
	 * Returns the agent's global semantic environment client.
	 * 
	 * @return the agent's GSE client
	 */
	public GSE getGlobalSE();
	
	
	/**
	 * Delivers a perception coming from the ISReal platform to the agent.
	 * 
	 * @param objects list of perceived objects
	 * @return
	 */
	public int handlePerception(List<MetadataRecord> objects);
	
	/**
	 * Returns the agent's object map which contains all metadata records of all perceived
	 * objects.
	 * 
	 * @return
	 */
	public HashMap<String, MetadataRecord> getObjectMap();
	
	/**
	 * Returns the agent's knowledge about spatial regions and their connections as graph.
	 * 
	 * @return
	 */
	public ListenableUndirectedWeightedGraph<String, DefaultWeightedEdge> getTopology();
	
	/**
	 * Passes a query to the agent.
	 * 
	 * @param id query ID
	 * @param query the query objects
	 */
	//public void handleQuery(int id, Vector<Object> query);
	
	public void handleQuery(ISRealQuery query);

	public Object getAgentObject();
	
	public DemonstratorCallerFactory getDemo();
	
	public void registerPerceptionListener(PerceptionListenerInterface listener);
	
	public void unregisterPerceptionListener(PerceptionListenerInterface listener);
}
