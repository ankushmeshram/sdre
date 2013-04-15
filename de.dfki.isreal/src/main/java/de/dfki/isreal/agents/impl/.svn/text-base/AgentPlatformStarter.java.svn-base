package de.dfki.isreal.agents.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * The ISReal AgentPlatformStarter is responsible for launching an agent platform.
 * The user has to implement the start method for his platform.
 * 
 * @author stefanwarwas
 *
 */
public abstract class AgentPlatformStarter {
	
	/**
	 * The default platform starter takes a properties file as parameter and adds
	 * its entries to the system properties.
	 */
	public AgentPlatformStarter(String param) {
		try {
			// add custom properties to system properties
			Properties system = System.getProperties();
			Properties properties = new Properties();
			properties.load(new FileInputStream(param));
			properties.putAll(system);
			System.setProperties(properties);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		start();
	}
	
	/**
	 * This method starts the agent platform.
	 */
	public abstract void start();
}
