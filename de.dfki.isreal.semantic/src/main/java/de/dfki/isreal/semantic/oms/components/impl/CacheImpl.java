package de.dfki.isreal.semantic.oms.components.impl;

import java.util.Hashtable;
import java.util.List;

import de.dfki.isreal.semantic.oms.components.Cache;
import de.dfki.isreal.data.Statement;

public class CacheImpl implements Cache {
	
	Hashtable<String, List<Statement>> cache = null;
	
	public CacheImpl(){
		cache = new Hashtable<String, List<Statement>>();
	}

	@Override
	public void deleteFromCache(String uri) {
		if (cache.containsKey(uri)){
			cache.remove(uri);
		}

	}

	@Override
	public List<Statement> getFromCache(String uri) {
		return cache.get(uri);
	}

	@Override
	public boolean isInCache(String uri) {
		return cache.containsKey(uri);
	}

	@Override
	public void writeToCache(String uri, List<Statement> sts) {
		cache.put(uri, sts);
	}

}
