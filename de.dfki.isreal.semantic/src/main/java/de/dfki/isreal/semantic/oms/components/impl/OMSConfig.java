package de.dfki.isreal.semantic.oms.components.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import de.dfki.isreal.omsconfig.ISRealOMSConfigPackage;
import de.dfki.isreal.omsconfig.OntologyFile;



/**
 * This class is the config for the OntologyManagementSystem.
 * 
 * @author stenes
 *
 */
public class OMSConfig {

	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(OMSConfig.class);
	
	private static de.dfki.isreal.omsconfig.OMSConfig config = null;
	
	private static HashMap<String, String> uri_mappers;
	private static List<String> ontologies;
	private static List<String> services;
	private static List<String> rules;
	private static List<String> imports;
	private static String r_path;
	
	public static void init(String conf_file){
		
		ResourceSet rs = new ResourceSetImpl();
		rs.getResourceFactoryRegistry().getExtensionToFactoryMap().put(
				Resource.Factory.Registry.DEFAULT_EXTENSION,
				new XMIResourceFactoryImpl());
		rs.getPackageRegistry().put(ISRealOMSConfigPackage.eNS_URI,
				ISRealOMSConfigPackage.eINSTANCE);

		URI setupURI = URI.createURI("file://" + conf_file);
		Resource res = rs.getResource(setupURI, true);
		config = (de.dfki.isreal.omsconfig.OMSConfig) EcoreUtil.getObjectByType(res.getContents(),
				ISRealOMSConfigPackage.eINSTANCE.getOMSConfig());
		
		
		// Note all physical path are: resourcePath + services/rules/ontologies + name
		// where services/rules/ontologies are fixed.
		// For the mappers we assume that the name contains the whole path.
		uri_mappers = new HashMap<String, String>();
		r_path = config.getResourcePath();
		if (!r_path.endsWith(File.separator)){
			r_path = r_path + File.separator;
		}
		
		for (OntologyFile of : config.getMappings()){
			String phys = checkURI(r_path, of.getPhysicalURIString());
			uri_mappers.put(of.getAbstractURIString(), phys);
		}
		
		ontologies = new ArrayList<String>();
		for (OntologyFile of : config.getOntologies()){
			String phys = r_path + "ontologies" + File.separator + of.getPhysicalURIString();
				uri_mappers.put(of.getAbstractURIString(), phys);
				ontologies.add(of.getAbstractURIString());
		}
		
		services = new ArrayList<String>();
		for (OntologyFile of : config.getServices()){
				String phys = r_path + "services" + File.separator + of.getPhysicalURIString();
				uri_mappers.put(of.getAbstractURIString(), phys);
				services.add(of.getAbstractURIString());
		}
		
		rules = new ArrayList<String>();
		for (OntologyFile of : config.getRules()){
				String phys = r_path + "rules" + File.separator + of.getPhysicalURIString();
				uri_mappers.put(of.getAbstractURIString(), phys);
				rules.add(of.getAbstractURIString());
		}
		
		imports = new ArrayList<String>();
		for (OntologyFile of : config.getImports()){
				String phys = r_path + "imports" + File.separator + of.getPhysicalURIString();
				uri_mappers.put(of.getAbstractURIString(), phys);
				imports.add(of.getAbstractURIString());
		}
		
	}

	/**
	 * This method checks whether the File(<root>+<path>) exists. If so, the combined
	 * path is returned, otherwise the user specified <path> is used. In the last case,
	 * <path> could be anything.
	 * 
	 * @param root
	 * @param path
	 * @return
	 */
	private static String checkURI(String root, String path) {
		String full = root + path;
		try {
			if(!new File(full).exists()) {
				return path;
			}
		} catch(Exception e) {
			return path;
		}
		return full;
	}
	
	public static String getOWLIMRepository(){
		return config.getRepositoryName();
	}
	
	public static boolean isSemReasoningEnabled(){
		return config.isSemReasoningEnabled();
	}
	
	public static String getResourcePath() {
		return r_path;
	}

	public static String getTripleStoreSystemName() {
		return config.getTripleStoreSystemName();
	}
	
	public static boolean isCacheEnabled() {
		return config.isCacheEnabled();
	}
	
	public static boolean isContQueriesEnabled() {
		return config.isContQueriesEnabled();
	}
	
	public static List<String> getOntologyURIStrings(){
		return ontologies;
	}
	
	public static List<String> getRuleURIStrings(){
		return rules;
	}
	
	public static List<String> getServiceURIStrings(){
		return services;
	}
	
	public static List<String> getImportURIStrings(){
		return imports;
	}
	
	public static HashMap<String, String> getURIMappings(){
		return uri_mappers;
	}

	public static String getTripleStoreConfigPath() {
		String c = checkURI(r_path, config.getTripleStoreConfigPath());
		return c;
	}

	public static String getPlannerExecutionPath() {
		return config.getPlannerExecutionPath();
	}

	public static String getTmpPath() {
		return config.getPlannerTmpPath();
	}
	
	public static boolean isLogging() {
		return config.isLogging();
	}
	
	public static int getHttpPort() {
		return config.getHttpPort();
	}
}
