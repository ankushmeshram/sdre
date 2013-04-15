package de.dfki.isreal.semantic.services;

import java.util.Hashtable;
import java.util.List;

/**
 * This class stores WaiterThreads that wait for the completion of the complete
 * service invokation. If a ServiceExecutor adds output to the this Class it
 * is checked if a WaiterThread is already registered to wait for that output.
 * If not a new WaiterThread is created.  
 * 
 * 
 * 
 * @author stenes
 *
 */
public class OutputQueue {

	private Hashtable<Integer, WaiterThread> registry = null;

	public OutputQueue() {
		registry = new Hashtable<Integer, WaiterThread>();
	}

	public synchronized WaiterThread getOutput(int id) {
		WaiterThread w = registry.get(id);
		if (w!=null){
			return w;
		} else{
			w = new WaiterThread(id);
			registry.put(id, w);
			return w;
		}
	}

	public synchronized void addOutput(int id, List<String> out) {
		WaiterThread w = registry.get(id);
		if (w != null){
			w.setOutput(out);
		} else{
			w = new WaiterThread(id, out);
			registry.put(id, w);
		}
	}
}
