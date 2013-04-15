package de.dfki.isreal.helpers;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

/**
 * This class is a simple profile tool. It can be used to set flags that
 * are identified with a strings in order to monitor the execution time between
 * the start and the end flag.
 * 
 * For more details see jamonapi.Monitor at
 * http://jamonapi.sourceforge.net/
 *  
 * @author stenes
 *
 */
public class Profiler {
	private static Logger logger = Logger.getLogger(Profiler.class);

	private static HashMap<String, Monitor> monitors;

	public static void init() {
		monitors = new HashMap<String, Monitor>();
	}

	public static void startMonitor(String classname, String methodname) {
		Monitor mon = MonitorFactory.start(classname + "." + methodname);
		monitors.put(classname + "." + methodname, mon);
	}

	public static void stopMonitor(String classname, String methodname) {
		monitors.get(classname + "." + methodname).stop();
	}

	public static void printReport() {
		for (String key : monitors.keySet()) {
			logger.info(monitors.get(key));
		}
	}

	public static String getReport() {
		if (monitors != null) {
			String res = "";
			for (String key : monitors.keySet()) {
				res = res + monitors.get(key) + "\n";
			}
			return res;
		} else {
			logger.error("Error: Profiler not initialized.");
			return "";
		}
	}
}
