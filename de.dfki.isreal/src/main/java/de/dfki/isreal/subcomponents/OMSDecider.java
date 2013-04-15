package de.dfki.isreal.subcomponents;


import java.io.File;
import java.net.URI;
import java.util.List;

import de.dfki.isreal.data.SPARQLDLResult;
import de.dfki.isreal.data.State;
import de.dfki.isreal.data.Statement;
import eu.larkc.plugin.decide.Decider;

public interface OMSDecider extends Decider, Maintenance, SemanticReasoner {
	
	public State getInitialStateFromKB();
	
}
