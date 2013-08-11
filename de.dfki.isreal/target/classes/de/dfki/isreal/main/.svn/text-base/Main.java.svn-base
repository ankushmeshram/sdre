package de.dfki.isreal.main;

import java.io.File;

import org.apache.log4j.PropertyConfigurator;

public class Main {

	/**
	 * 
	 * Starts the ISReal SetupInitializer_Gui.
	 * @param args
	 */
	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");
		System.setProperty("isreal.basedir", System.getProperty("user.dir") + File.separator);
		
		String file = System.getProperty("isreal.basedir") + File.separator + "config" + File.separator + args[0];
		
		ISRealSetupInitializer i = new ISRealSetupInitializer(file);
		SetupInitializer_GUI gui = new SetupInitializer_GUI(i);
		
		if (args.length > 0){
			gui.setDefaultConfig(file);
		}
	}

}
