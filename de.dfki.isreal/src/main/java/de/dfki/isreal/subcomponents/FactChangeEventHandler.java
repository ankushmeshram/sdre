package de.dfki.isreal.subcomponents;

import java.util.ArrayList;
import java.util.List;

import de.dfki.isreal.data.Statement;
import de.dfki.isreal.network.protos.ExchangeDataProtos.MetadataRecord;

/**
 * Fact change event handler prototype that compares URI strings of fact to monitor and subjects/objects
 * of RDF triples to change. The actual event handling code has to be implemented.
 * 
 * @author paka
 *
 */
public abstract class FactChangeEventHandler {

	protected MetadataRecord objectToMonitor;
	
	protected int id;
	
	protected List<String>	objectsToHandle = new ArrayList<String>();
	
	public FactChangeEventHandler(int id, MetadataRecord objectToMonitor) {
		this.id = id;
		this.objectToMonitor = objectToMonitor;
	}
	
	/**
	 * Returns <code>true</code> iff the object itself or any of the dependent objects has changed.
	 * 
	 * @param a List of statements to check.
	 * @return <code>true</code> iff object or dependent object changed.
	 */
	public boolean check(List<Statement> a) {
		objectsToHandle.clear();
		
		for(Statement triple : a) {		
			String object = objectToMonitor.getObject();
			if(triple.getSubjectString().equals(object))
				objectsToHandle.add(object);
			else if(triple.getObjectString().equals(object))
				objectsToHandle.add(object);
			
			// also check dependent objects
			for(String dependentObj : objectToMonitor.getDependentObjectsList()) {
				if(triple.getSubjectString().equals(dependentObj))
					objectsToHandle.add(dependentObj);
				else if(triple.getObjectString().equals(dependentObj))
					objectsToHandle.add(dependentObj);
			}
		}
		
		return !objectsToHandle.isEmpty();
	}

	/**
	 * Must be implemented by the subclasses. The <code>objectsToHandle</code> list contains
	 * all objects that should trigger the intended behaviour.
	 */
	public abstract void handle();
	
	public int getId() {
		return id;
	}
}
