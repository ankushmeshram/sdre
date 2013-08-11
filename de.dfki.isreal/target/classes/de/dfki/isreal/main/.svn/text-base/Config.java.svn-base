package de.dfki.isreal.main;

import java.util.HashMap;
import java.util.List;

import de.dfki.isreal.network.MessageQueue;


/**
 * 
 * This class is a static reference for a process to refer to all components
 * running. For every process (initialized by the SetupInitializer), the
 * ComponentInitializer inits one Config. It is used as static class to get
 * the instance of the component, the MessageQueue and the WorkerThread class.
 * 
 * The methods in this class are to check whether there is an instance of an 
 * component and to get it.
 * 
 * Furthermore this class can be used to store every path on the local machine
 * or read in config files in order to store path variables how to invoke and
 * where files are located (e.g. the ontologies for the semantic environment).
 * But for this I would suggest a local, component specific config class.
 * 
 * 
 * @author stenes
 *
 */
public class Config {
		
	private static HashMap<String, Object> components = new HashMap<String, Object>();
	private static String workerClass = null;
	private static MessageQueue msg_queue = null;
	private static HashMap<String, String> role2name = new HashMap<String, String>();
	

	/**
	 * Registeres an instance 'comp' of an ISReal component with its role name.
	 * @param name
	 * @param comp
	 */
	public static void register(String name, Object comp, List<String> roles){
		components.put(name, comp);
		for (String r : roles){
			role2name.put(r, name);
		}
	}
	
	/**
	 * Checks whether for the given name a component is registered. 
	 * @param name
	 * @return
	 */
	public static boolean isRegistered(String name){
		return components.containsKey(name);
	}
	
	/**
	 * Checks whether for the given role a component is registered. 
	 * @param name
	 * @return
	 */
	public static boolean isRoleRegistered(String role){
		return role2name.containsKey(role);
	}
	
	/**
	 * Returns the component name for the given role or null if this role is
	 * not registered.
	 * @param role
	 * @return
	 */
	public static String getComponentNameForRole(String role){
		return role2name.get(role);
	}
	
	/**
	 * Returns the component for the given name. This instance can be casted to
	 * the Interface object according to the role.
	 * @param name
	 * @return
	 */
	public static Object getComponent(String name){
		return components.get(name);
	}
	
	/**
	 * Registers the MessageQueue for this component.
	 * @param mq
	 */
	public static void register(MessageQueue mq){
		msg_queue = mq;
	}
	
	/**
	 * Checks if a MessageQueue is registered.
	 * @return
	 */
	public static boolean hasMessageQueue(){
		return msg_queue != null;
	}
	
	/**
	 * Returns the MessageQueue object.
	 * @return
	 */
	public static MessageQueue getMessageQueue(){
		return msg_queue;
	}
	
	/**
	 * Sets the worker class name.
	 * @param w_class
	 */
	public static void setWorkerClass(String w_class){
		workerClass = w_class;
	}
	
	/**
	 * Returns the worker class name.
	 * @return
	 */
	public static String usesWorkerThread() {
		return workerClass;
	}
}
