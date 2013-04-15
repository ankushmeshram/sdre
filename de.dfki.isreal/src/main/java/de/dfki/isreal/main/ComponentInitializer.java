package de.dfki.isreal.main;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import de.dfki.isreal.helpers.Profiler;
import de.dfki.isreal.isrealsetup.Component;
import de.dfki.isreal.isrealsetup.ISRealSetupPackage;
import de.dfki.isreal.isrealsetup.Role;
import de.dfki.isreal.isrealsetup.Setup;
import de.dfki.isreal.network.CallerThread;
import de.dfki.isreal.network.ISRealMessageFactory;
import de.dfki.isreal.network.ListenerRegistry;
import de.dfki.isreal.network.MessageQueue;
import de.dfki.isreal.network.protos.ExchangeDataProtos.ISRealMessage;
import de.dfki.isreal.network.server.ISRealSocketServer;
import de.dfki.isreal.network.server.ISRealTooTallNateWebSocketServer;
import de.dfki.isreal.network.socket.ISRealSocketClient;
import de.dfki.isreal.network.socket.SocketRegistry;

/**
 * The ComponentInitializer starts any ISReal component by their specification
 * given as an ecore model instance.
 * 
 * During startup there are several stages, where this component have to wait
 * for their depending other (server, client) components. Therefore during
 * startup several waiting loops are passed.
 * 
 * Overall the startup takes care of the whole communication establishing and
 * registration process.
 * 
 * @author stenes
 * 
 */
public class ComponentInitializer  {
	private static Logger logger = Logger.getLogger(ComponentInitializer.class);

	private static Setup setup = null;
	private static Component comp = null;

	private static int wait_threshold = 500;
	
	/**
	 * The main needs two arguments, the first is the path leading to the setup
	 * model file, the second is the name of the component, that is specified in
	 * this file and that should be initialized.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("Wrong arguments!!!!");
		} else {
			ResourceSet rs = new ResourceSetImpl();
			rs.getResourceFactoryRegistry().getExtensionToFactoryMap().put(
					Resource.Factory.Registry.DEFAULT_EXTENSION,
					new XMIResourceFactoryImpl());
			rs.getPackageRegistry().put(ISRealSetupPackage.eNS_URI,
					ISRealSetupPackage.eINSTANCE);

			URI setupURI = URI.createURI("file://" + args[0]);
			Resource res = rs.getResource(setupURI, true);
			setup = (Setup) EcoreUtil.getObjectByType(res.getContents(),
					ISRealSetupPackage.eINSTANCE.getSetup());

			for (Component c : setup.getComponents()) {
				if (c.getName().equals(args[1])) {
					comp = c;
					initialize();
				}
			}
		}

	}

	/**
	 * The initialization is divided in several stages: 1 - Setup of the basic
	 * communication components. 2 - Start of the socket servers. 3 - Connect to
	 * the servers of this component. - Since this could not be established yet
	 * there is a waiting loop. 4 - Register to connected servers 5 - Get all
	 * client registrations, there is a waiting loop. 6 - If the component has a
	 * server it waits for their acknowledgment, that it is fully initialized. 7
	 * - Initialize Component implementation. 8 - Send acknowledgment to all
	 * clients (see step 6).
	 * 
	 * These stages ensure a waterfall-like, top-down, parallel initialization
	 * of all components. If one component is missing the initialization process
	 * of the other components stops in the according waiting loops.
	 */
	private static void initialize() {
		PropertyConfigurator.configure("log4j.properties");
		Profiler.init();
		

		logger.info(comp.getName() + " 1 - setup communication unit");
		MessageQueue mq = new MessageQueue();
		Config.register(mq);
		Config.setWorkerClass(comp.getWorkerThread());
		SocketRegistry.init();
		ListenerRegistry.init();

		logger.info(comp.getName() + " 2 - start own socket servers");
		if (comp.getClients().size() > 0) {
			EList<Component> clients = comp.getClients();
			// Todo Check for Programming Environment of Clients.
			boolean hasJavaClient = false;
			boolean hasWebClient = false;
			for (Component client : clients) {
				if (client.getProgramEnv().getLiteral().equals("Java")) {
					hasJavaClient = true;
				} else if (client.getProgramEnv().getLiteral().equals(
						"JavaScript")) {
					hasWebClient = true;
				}
			}

			if (hasJavaClient) {
				ISRealSocketServer java_server = new ISRealSocketServer(comp
						.getPort());
				java_server.start();
			}
			if (hasWebClient) {
				//ISRealJettyWebSocketServer web_server = new ISRealJettyWebSocketServer();
				ISRealTooTallNateWebSocketServer web_server = new ISRealTooTallNateWebSocketServer(comp.getWebsocketport(), setup.isReset());
				web_server.start();
			}
		}

		logger.info(comp.getName() + " 3 - connect to servers");
		if (comp.getServers().size() > 0) {
			for (Component server : comp.getServers()) {
				// TODO Check for Programming Environment of Servers.
				boolean connected = false;
				while (!connected) {
					try {
						ISRealSocketClient sc = new ISRealSocketClient(server
								.getName(), getRoleNames(server.getRole()), server.getHost(),
								server.getPort());
						connected = true;
						logger.info(comp.getName() + " Connected to " + server.getHost() + ":"
								+ server.getPort() + ":" + server.getName());
					} catch (IOException e) {
						logger.info(comp.getName() + " Could not connect yet to "
								+ server.getHost() + ":" + server.getPort()
								+ ":" + server.getName() + ", wait some time!");
						waitMillis(wait_threshold); // wait 3 sec hand hope the component
						// will established the server.
					}
				}

				
				ISRealMessage m = ISRealMessageFactory
						.createInitRegisterClient(comp.getName());
				int sid = SocketRegistry.getSocketId(server.getName());
				logger.info(comp.getName() + " 4 - send init role_messages to " + server.getName() + " with sid: " + sid);
				CallerThread ct = new CallerThread(sid, m, true);
				ct.start();
			}
		}

		logger.info(comp.getName() + " 5 - wait for init_role messages from possible clients that are on server side");
		if (comp.getClients().size() > 0) {
			boolean all_clients_ready = false;
			boolean checked_clients_ready = true;
			while (!all_clients_ready) {
				Hashtable<String, Integer> reg = Config.getMessageQueue()
						.getRegisteredClientNames();
				for (Component c : comp.getClients()) {
					if (!reg.containsKey(c.getName()) || !checked_clients_ready) {
						checked_clients_ready = false;
					} else {
						// hack for more than one role
						// get this from model after change
						SocketRegistry.register(c.getName(), reg.get(c
								.getName()), getRoleNames(c.getRole()));
					}
				}
				all_clients_ready = checked_clients_ready;
				if (!all_clients_ready) {
					// some clients not connected yet...
					waitMillis(wait_threshold);
					logger
							.info(comp.getName() + " Waiting for all client platforms to register...");
					for (String name : reg.keySet()){
						logger.debug(comp.getName() + " has registered client: " + name);
					}
					checked_clients_ready = true;
				}
			}
		}

		logger.info(comp.getName() + " 6 - wait for acknowledgements of the server startups.");
		if (comp.getServers().size() > 0) {
			boolean all_servers_ready = false;
			boolean checked_servers_ready = true;
			while (!all_servers_ready) {
				List<Integer> sids = Config.getMessageQueue()
						.getAcknowledgedSocketIds();
				for (Component c : comp.getServers()) {
					if (!sids.contains(SocketRegistry.getSocketId(c.getName()))
							|| !checked_servers_ready) {
						checked_servers_ready = false;
					}
				}
				all_servers_ready = checked_servers_ready;
				if (!all_servers_ready) {
					// some servers not ready yet...
					waitMillis(wait_threshold);
					logger
							.info(comp.getName() + " Waiting for all server platforms to acknowledge startup...");
					for (int id : sids){
						logger.debug(comp.getName() + " has acks from: " + id);
					}
					checked_servers_ready = true;
				}
			}
		}

		logger.info(comp.getName() + " 7 - init component");
		initComponent(comp);

		logger.info(comp.getName() + " 8 - send INIT_ACKs to all Clients on Server side.");
		if (Config.isRegistered(comp.getName())){
		for (Component c : comp.getClients()) {
			ISRealMessage m = ISRealMessageFactory.createInitAcknowledgement();
			int sid = SocketRegistry.getSocketId(c.getName());
			logger.info(comp.getName() + " 8 - send INIT_ACKs to: " + c.getName() + " with sid: " + sid);
			CallerThread ct = new CallerThread(sid, m, true);
			ct.start();
		}
		} else{
			logger.error(comp.getName() + " - Could not initialize component correctly therefore no acks sent.");
		}

	}

	/**
	 * Returns the role names as String of a List of roles.
	 * @param role
	 * @return
	 */
	private static List<String> getRoleNames(EList<Role> role) {
		List<String> res = new ArrayList<String>();
		for (Role r : role){
			res.add(r.getLiteral());
		}
		return res;
	}

	/**
	 * Initializes the component and registers it in the Config. At this version
	 * it has to be one of several predefined implementations (Demo_GUI,
	 * GlobalSESController, etc.). A clean design would be to store an class
	 * object in the Config and dynamically cast it to invoke methods or at
	 * least to ensure that every role in ISReal has a predefined interface
	 * (GlobalSESController for GSE) so that the numbers of different
	 * implementations that can be registered in the Config are limited.
	 * 
	 * @param c
	 */
	private static void initComponent(Component c) {
		try {
			String class_name = c.getProcessClass();
			Object comp_instance = null;
			Class worker_class = Class.forName(class_name);
			Constructor cons = worker_class.getConstructor(String.class);
			// need to have standard constructor with parameter string for config path.
			// Else put this code in every if clause beneath.
			comp_instance = cons.newInstance(c.getProcessConfigLocation());
			// register in local Config.
			Config.register(c.getName(), comp_instance, getRoleNames(c.getRole()));
			
		} catch (Exception e) {
			logger.error("Error initializing Component " + e);
			e.printStackTrace();
		}

	}

	/**
	 * This methods waits for the given numer of milliseconds.
	 * 
	 * @param i
	 *            - Milliseconds to wait.
	 */
	private static void waitMillis(int i) {
		try {
			Thread.sleep(i);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
//		long start = System.currentTimeMillis();
//		while ((System.currentTimeMillis() - start) < i) {
//		}
	}

}
