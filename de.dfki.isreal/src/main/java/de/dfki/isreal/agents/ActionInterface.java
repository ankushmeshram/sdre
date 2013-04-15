package de.dfki.isreal.agents;
 
import java.io.Serializable;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.ListenableUndirectedWeightedGraph;

public interface ActionInterface extends Serializable {

	public String getName();
	
	@SuppressWarnings("rawtypes")
	public Class getType();
	
	public Class [] getParameterTypes();
	
	public String [] getParameterNames();
	
	
}
