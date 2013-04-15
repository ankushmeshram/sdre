package de.dfki.isreal.agents.impl;

import java.util.HashMap;

public class NameURIMap {
	
	private HashMap<String, String> nameUriMap = new HashMap<String, String>();
	private HashMap<String, String> uriNameMap = new HashMap<String, String>();
	
	public NameURIMap() {
		
	}
	
	public void put(String uri, String name) {
		nameUriMap.put(name, uri);
		uriNameMap.put(uri, name);
	}

	public String getName(String uri) {
		return uriNameMap.get(uri);
	}
	
	public String getUri(String name) {
		return nameUriMap.get(name);
	}
	
	public String get(String text) {
		if(getName(text) != null) {
			return getName(text);
		}
		
		return getUri(text);
	}
}
