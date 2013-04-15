package de.dfki.isreal.network.socket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import de.dfki.isreal.helpers.ConcurrentCounter;

/**
 * This static class stores all socket connections on that component. I.e. it
 * stores the sid (socket id) and the running thread object. For identification
 * from other components a static string role_name can be stored too. The sid is
 * created in the registry in a unique way and given back to the component that
 * registered the socket object for further identification (e.g. to send
 * messages over that object).
 * 
 * @author stenes
 * 
 */
public class SocketRegistry {
	private static Logger logger = Logger.getLogger(SocketRegistry.class);

	private static HashMap<Integer, ISRealSocket> sockets;
	private static HashMap<String, Integer> names;
	private static HashMap<String, String> role2name;
	private static ConcurrentCounter id;

	/**
	 * Initiates the registry.
	 */
	public static void init() {
		sockets = new HashMap<Integer, ISRealSocket>();
		names = new HashMap<String, Integer>();
		role2name = new HashMap<String, String>();
		id = new ConcurrentCounter();
	}

	/**
	 * Registers a String name for an already stored sid.
	 * 
	 * TODO Check for duplicate names, unknown sids.
	 * 
	 * @param role
	 *            - Name to store...
	 * @param sid
	 *            - ...for this sid.
	 */
	public static void register(String name, int sid, List<String> roles) {
		synchronized (names) {
			names.put(name, sid);
		}
		synchronized (role2name) {
			for (String role : roles) {
				role2name.put(role, name);
			}
		}
	}

	/**
	 * Checks if a Socket leading to the given name is registered.
	 * 
	 * @param name
	 * @return true iff there is any Socket with this name registered
	 */
	public static boolean isRegistered(String name) {
		boolean registered = false;

		if (names.containsKey(name)) {
			int sid = names.get(name);
			registered = sockets.containsKey(sid);
		}

		return registered;
	}

	/**
	 * Checks if a Socket leading that fullfills the given role is registered.
	 * 
	 * @param role
	 * @return true iff there is any Socket with this role registered
	 */
	public static boolean isRoleRegistered(String role) {
		boolean registered = false;

		String name = role2name.get(role);
		if (name != null) {
			if (names.containsKey(name)) {
				int sid = names.get(name);
				registered = sockets.containsKey(sid);
			}
		}

		return registered;
	}

	/**
	 * Registers an ISRealSocket and returns a new sid for this object.
	 * 
	 * @param thread
	 *            - Object to store.
	 * @return Created sid of the object.
	 */
	public static int register(ISRealSocket s) {
		synchronized (sockets) {
			int count = id.getCount();
			sockets.put(count, s);
			id.increase();
			return count;
		}
	}

	/**
	 * Returns the ISRealSocket with the given sid.
	 * 
	 * @param id
	 * @return
	 */
	public static ISRealSocket getISRealSocket(int id) {
		return sockets.get(id);
	}

	/**
	 * Returns the sid for the given name. This name has to be registered first
	 * for that sid.
	 * 
	 * @param name
	 * @return
	 */
	public static int getSocketId(String name) {
		int sid = names.get(name);
		return sid;
	}

	/**
	 * Returns the SocketId that is registered under the given role. Note that
	 * one socket id can have more than one role.
	 * 
	 * @param role
	 * @return
	 */
	public static int getSocketIdFromRole(String role) {
			String name = role2name.get(role);

			int sid = names.get(name);
			return sid;
	}

	/**
	 * Returns the socket name of the given Role if the socket is registered.
	 * Returns null if the socket is not registered.
	 * 
	 * @param role
	 */
	public static String getSocketNameFromRole(String role) {
		return role2name.get(role);
	}

	/**
	 * Removes the socket object and the name of the sid.
	 * 
	 * @param sid
	 */
	public static void remove(int sid) {
		List<String> ns = new ArrayList<String>();
		if (sockets.containsKey(sid)) {
			synchronized (sockets) {
				sockets.remove(sid);
			}
		}
		// delete names
		String name_to_delete = null;
		if (names.containsValue(sid)) {
			for (String n : names.keySet()) {
				if (names.get(n) == sid)
					name_to_delete = n;
				ns.add(n);
			}
		}
		if(name_to_delete != null) {
			synchronized (names) {
				names.remove(name_to_delete);
			}
		}
		// delete roles
		List<String> roles_to_delete = new ArrayList<String>();
		for (String n : ns) {
			if (role2name.containsValue(n)) {
				for (String r : role2name.keySet()) {
					if (role2name.get(r).equals(n)) {
						roles_to_delete.add(r);
					}
				}
			}
		}
		synchronized (role2name) {
			for (String r : roles_to_delete) {
				role2name.remove(r);
			}
		}
	}

	/**
	 * Returns the registered role name for the given socket id. If there is nor
	 * role registered, null is returned.
	 * 
	 * @param sid
	 * @return
	 */
	public static String getSocketRole(int sid) {
		for (String n : names.keySet()) {
			if (names.get(n) == sid) {
				return n;
			}
		}
		return null;
	}

	public static ISRealSocket getSocket(String n) {
		int id = names.get(n);
		if (sockets.containsKey(id)) {
			return sockets.get(id);
		} else {
			logger.error("No socket with role name: " + n
					+ " registered.");
			return null;
		}
	}
}
