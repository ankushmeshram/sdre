package de.dfki.isreal.semantic.oms.components;

import java.util.List;

import de.dfki.isreal.data.Statement;

public interface Cache {

	public boolean isInCache(String uri);
	
	public List<Statement> getFromCache(String uri);
	
	public void writeToCache(String uri, List<Statement> sts);
	
	public void deleteFromCache(String uri);
	
}
