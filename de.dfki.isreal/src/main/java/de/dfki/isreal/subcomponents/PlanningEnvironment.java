package de.dfki.isreal.subcomponents;

import java.util.List;

import de.dfki.isreal.data.PlannerOutput;
import de.dfki.isreal.data.State;

public interface PlanningEnvironment {
	
	public PlannerOutput invokePlanner(State init, State goal);
	
	public PlannerOutput invokePlanner(State init, State goal, List<String> services);
	

}
