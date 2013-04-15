package de.dfki.isreal.main;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import de.dfki.isreal.helpers.KillProcessThread;
import de.dfki.isreal.helpers.RestartComponentsThread;
import de.dfki.isreal.helpers.StreamGobbler;
import de.dfki.isreal.isrealsetup.Component;
import de.dfki.isreal.isrealsetup.ISRealSetupPackage;
import de.dfki.isreal.isrealsetup.Setup;

/**
 * This class reads in a model of an ISRealSetup and calls ComponentInitializers
 * for every component in it. All the ComponentInitializer are invoked in a new
 * java process, their output stream are redirected by the StreamGobbler class
 * and a hook (KillProcessThread) is established in order to kill all the
 * different processes in case of ending the process where the
 * ISRealSetupInitializer is running in.
 * 
 * This class is extended by an gui (SetupInitializer_GUI), showing the running
 * processes and for loading the setup file.
 * 
 * @author stenes
 * 
 */
public class ISRealSetupInitializer {
	private static Logger logger = Logger.getLogger(ISRealSetupInitializer.class);

	private String setup_file = "";
	private Setup setup = null;
	private HashMap<String, Process> processes = new HashMap<String, Process>();
	private static String host = null;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ISRealSetupInitializer initializer = new ISRealSetupInitializer();
		initializer.setSetupFile("/home/stenes/workspace/Isreal/setups/logger_gse.isrealsetup");
		initializer.initializeSetup();
	}

	public ISRealSetupInitializer() {
		host = getCurrentEnvironmentNetworkIp();
	}

	public ISRealSetupInitializer(String file) {
		this();
		setSetupFile(file);
	}

	/**
	 * This method changes or sets the setup file and loads the model from that
	 * file.
	 * @param file
	 */
	public void setSetupFile(String file) {
		setup_file = file;
		ResourceSet rs = new ResourceSetImpl();
		rs.getResourceFactoryRegistry().getExtensionToFactoryMap().put(
				Resource.Factory.Registry.DEFAULT_EXTENSION,
				new XMIResourceFactoryImpl());
		rs.getPackageRegistry().put(ISRealSetupPackage.eNS_URI,
				ISRealSetupPackage.eINSTANCE);

		URI setupURI = URI.createFileURI(file);
		//URI setupURI = URI.createURI("file://" + file);
		Resource res = rs.getResource(setupURI, true);
		setup = (Setup) EcoreUtil.getObjectByType(res.getContents(),
				ISRealSetupPackage.eINSTANCE.getSetup());
		printSetup();
	}

	private void printSetup() {
		logger.info("========================================");
		logger.info("Setup file: " + setup_file);
		logger.info("========================================");
		for (Component c : setup.getComponents()){
			printComponent(c);
		}
	}

	private void printComponent(Component c) {
		logger.info("========================================");
		logger.info("Component name: " + c.getName());
		logger.info("Servers:");
		for(Component s : c.getServers()){
			logger.info(" - " + s.getName());
		}
		logger.info("Clients:");
		for(Component s : c.getClients()){
			logger.info(" - " + s.getName());
		}
	}

	/**
	 * Returns the setup file as an object.
	 * @return
	 */
	public Setup getSetupFile() {
		return setup;
	}

	/**
	 * For every component that is supposed to run on that machine (equality
	 * by host address) this method starts a new ComponentInitilizer process.
	 * 
	 * For this process a Gobbler takes care of the error and output stream.
	 * 
	 * Also a hook is installed that if the process of the SetupInitializer
	 * closes, all the child processes are also destroyed.
	 */
	public void initializeSetup() {
		if (setup != null) {
			// for every Component, start component in new process
			// - only if the host is the one of this machine!!!
			// - only if the side is not client ???
			for (Component c : setup.getComponents()) {
				if ((c.getHost().equals(host) || c.getHost().equals("127.0.0.1")) && c.getSide().getLiteral().equals("Server")) {
					initializeComponent(c);
				} else {
					System.out.println("Do not initialize " + c.getName()
							+ " with host " + c.getHost()
							+ " and side " + c.getSide().getLiteral()
							+ " because this machine is host " + host
							+ " and only initializes \"Server\" side." );
				}
			}
			// start thread for component restart
			if(setup.isReset()) {
				RestartComponentsThread restartThread = new RestartComponentsThread(this);
				restartThread.start();
			}
		} else {
			System.out.println("ERROR: No setup file given...");
		}
	}

	private void initializeComponent(Component c) {
		String cp = System.getProperty("java.class.path");
		cp = "\"" + cp + "\"";

		String baseDir = "-Disreal.basedir=" + System.getProperty("isreal.basedir");

		Process p;
		Vector<String> cmd = new Vector<String>();
		cmd.add("java");
		cmd.add(baseDir);
		
		String jvmoptions = c.getJvmoptions();
		if(jvmoptions != null) {
			for(String opt : jvmoptions.split(" "))
				cmd.add(opt);
		}
		
		cmd.add("-cp");
		logger.info("CP: " + cp);
		cmd.add(cp);
		cmd.add("de.dfki.isreal.main.ComponentInitializer");
		cmd.add(setup_file);
		cmd.add(c.getName());
		
		try {
			p = Runtime.getRuntime().exec(cmd.toArray(new String[0]));
			// any error message?
			StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(),
					"ERROR");

			// any output?
			StreamGobbler outputGobbler = new StreamGobbler(p.getInputStream(),
					"OUTPUT");

			// kick them off
			errorGobbler.start();
			outputGobbler.start();
			Runtime.getRuntime().addShutdownHook(new KillProcessThread(p));
			processes.put(c.getName(), p);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns a set of the running processes.
	 * 
	 * @return
	 */
	public Set<String> getProcesses() {
		return processes.keySet();
	}
	
	public Process getProcess(String process) {
		return processes.get(process);
	}

	/**
	 * Kills the process with the given name.
	 * 
	 * @param sel
	 */
	public void killProcess(String sel) {
		Process p = processes.get(sel);
		p.destroy();
		processes.remove(sel);
	}

	/**
	 * Returns a String with the ip address of this machine.
	 * 
	 * @return
	 */
	public static String getCurrentEnvironmentNetworkIp() {
		Enumeration<NetworkInterface> netInterfaces = null;
		try {
			netInterfaces = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			System.out.println("Somehow we have a socket error...");
		}

		while (netInterfaces.hasMoreElements()) {
			NetworkInterface ni = netInterfaces.nextElement();
			Enumeration<InetAddress> address = ni.getInetAddresses();
			while (address.hasMoreElements()) {
				InetAddress addr = address.nextElement();
				if (!addr.isLoopbackAddress() && !addr.isSiteLocalAddress()
						&& !(addr.getHostAddress().indexOf(":") > -1)) {
					return addr.getHostAddress();
				}
			}
		}
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			return "127.0.0.1";
		}
	}

}
