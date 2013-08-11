package de.dfki.isreal.semantic.services;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyCreationException;
import org.semanticweb.owl.model.OWLOntologyManager;
import org.xml.sax.InputSource;

import com.sun.org.apache.xerces.internal.parsers.SAXParser;

import de.dfki.isreal.helpers.Profiler;
import de.dfki.isreal.semantic.oms.components.impl.ContinousQueryRegistry;
import de.dfki.isreal.semantic.oms.components.impl.OMSConfig;
import de.dfki.isreal.data.Statement;
import de.dfki.isreal.data.impl.StatementImpl;
import de.dfki.isreal.data.Parameter;
import de.dfki.isreal.data.ServiceWrapper;
import de.dfki.isreal.data.impl.ConditionalEffect;
import de.dfki.isreal.semantic.services.helpers.PDDXMLHandler;

/**
 * This Class implements a service registry. It would be sufficient to maintain
 * a list of service uris, but since we need the service description on several
 * other positions (service execution, gui) it is more efficient to maintain a
 * service object.
 */
public class ServiceRegistryImpl implements ServiceRegistryCaching {

	private static Logger logger = Logger.getLogger(ServiceRegistryImpl.class);

	private HashMap<String, ServiceWrapper> interaction_service_reg;
	private HashMap<String, ServiceWrapper> se_service_reg;
	private HashMap<String, ServiceWrapper> agent_action_service_reg;
	private ContinousQueryRegistry QReg;

	private static String rdf_type = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
	
	private static ServicePool servicePool = new ServicePool();
	private  OWLOntologyManager owlManager = OWLManager.createOWLOntologyManager();

	public ServiceRegistryImpl() {
		interaction_service_reg = new HashMap<String, ServiceWrapper>();
		se_service_reg = new HashMap<String, ServiceWrapper>();
		agent_action_service_reg = new HashMap<String, ServiceWrapper>();
		
		owlManager.setSilentMissingImportsHandling(true);
	}
	
	public ServiceRegistryImpl(ContinousQueryRegistry reg) {
		QReg = reg;
		interaction_service_reg = new HashMap<String, ServiceWrapper>();
		se_service_reg = new HashMap<String, ServiceWrapper>();
		agent_action_service_reg = new HashMap<String, ServiceWrapper>();
	}

	public boolean addService(String uri) {
		if (!interaction_service_reg.containsKey(uri)
				&& !se_service_reg.containsKey(uri)
				&& !agent_action_service_reg.containsKey(uri)) {
			Profiler.startMonitor(this.getClass().getName(), "addService");
			ServiceWrapper s = null;
			try {
				String abs = uri.substring(0, uri.lastIndexOf("#"));
				String phys = OMSConfig.getURIMappings().get(abs);
				
				s = servicePool.getService(uri);
				if(s == null)
					s = servicePool.addService(uri, phys);				
				// s = new ServiceWrapperImpl(uri, phys);
				
				if (s.getServiceType() == ServiceWrapper.INTERACTION_SERVICE) {
					interaction_service_reg.put(uri, s);
					Profiler.stopMonitor(this.getClass().getName(),
							"addService");
					return Boolean.TRUE;
				} else if (s.getServiceType() == ServiceWrapper.SE_SERVICE) {
					se_service_reg.put(uri, s);

					if (OMSConfig.isContQueriesEnabled()) {
						// register precondition at ContinousQueryRegistry
						QReg.registerQuery(
								getStatementsFromPrecondition(uri), uri);
					}

					Profiler.stopMonitor(this.getClass().getName(),
							"addService");
					return Boolean.TRUE;
				} else if (s.getServiceType() == ServiceWrapper.AGENT_ACTION_SERVICE) {
					agent_action_service_reg.put(uri, s);
					Profiler.stopMonitor(this.getClass().getName(),
							"addService");
					return Boolean.TRUE;
				} else {
					logger.info("Not a ISReal service, therefore not stored.");
				}
			} catch (URISyntaxException e) {
				logger.error("Not a valid URI!", e);
				Profiler.stopMonitor(this.getClass().getName(), "addService");
				return Boolean.FALSE;
			}
		} else {
			logger.debug("Service already registered.");
		}
		return Boolean.FALSE;
	}

	public List<String> getInteractionServices() {
		return new ArrayList<String>(interaction_service_reg.keySet());
	}

	public List<String> getSEServices() {
		return new ArrayList<String>(se_service_reg.keySet());
	}

	public List<String> getAgentServices() {
		return new ArrayList<String>(agent_action_service_reg.keySet());
	}

	public List<String> getAllServices() {
		List<String> services = new ArrayList<String>();
		for (String s : interaction_service_reg.keySet()) {
			services.add(s);
		}
		for (String s : se_service_reg.keySet()) {
			services.add(s);
		}
		for (String s : agent_action_service_reg.keySet()) {
			services.add(s);
		}
		return services;
	}

	public boolean printServices() {
		logger.info("The services:");
		for (String u : getAllServices()) {
			logger.info("printServices-URIs: " + u);
		}
		return Boolean.TRUE;
	}

	public void removeService(String uri) {
		if (interaction_service_reg.containsKey(uri)) {
			interaction_service_reg.remove(uri);
		} else if (se_service_reg.containsKey(uri)) {
			se_service_reg.remove(uri);
		} else if (agent_action_service_reg.containsKey(uri)) {
			agent_action_service_reg.remove(uri);
		}
		logger.error("Service not in registry.");
	}

	public void addServices(String ont_file_name) {
		Profiler.startMonitor(this.getClass().getName(), "addServices");

		Set<OWLIndividual> inds = getServicesInFile(ont_file_name);
		if(inds == null) {
			Profiler.stopMonitor(this.getClass().getName(), "addServices");
			return;
		}
		
		for (OWLIndividual service : inds) {
			addService(service.getURI().toString());
		}
		
		Profiler.stopMonitor(this.getClass().getName(), "addServices");
	}
	
	private Set<OWLIndividual> getServicesInFile(String ont_file_name) {
		File f = new File(ont_file_name);
		// TODO check service description language

		// for owls 1.1

		//man.addURIMapper(mapper);
		try {
			URI ont_uri = f.toURI();
			OWLOntology ont;
			if(!owlManager.contains(ont_uri))
				ont = owlManager.loadOntologyFromPhysicalURI(ont_uri);
			else
				ont = owlManager.getOntology(ont_uri);
			OWLClass s_class = owlManager
					.getOWLDataFactory()
					.getOWLClass(
							URI
									.create("http://www.daml.org/services/owl-s/1.1/Service.owl#Service"));
			return s_class.getIndividuals(ont);
		} catch (OWLOntologyCreationException e1) {
			e1.printStackTrace();
			return null;
		}
	}
	
	public void prepareServices(String ont_file_name) {
		Set<OWLIndividual> inds = getServicesInFile(ont_file_name);
		if(inds == null)
			return;
		
		for(OWLIndividual service : inds) {
			String uri = service.getURI().toString();
			String abs = uri.substring(0, uri.lastIndexOf("#"));
			String phys = OMSConfig.getURIMappings().get(abs);

			try {
				if(!servicePool.contains(uri))
					servicePool.addService(uri, phys);
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
	}

	public ServiceWrapper getServiceInRegistry(String service_uri) {
		if (interaction_service_reg.containsKey(service_uri)) {
			return interaction_service_reg.get(service_uri);
		} else if (se_service_reg.containsKey(service_uri)) {
			return se_service_reg.get(service_uri);
		} else if (agent_action_service_reg.containsKey(service_uri)) {
			return agent_action_service_reg.get(service_uri);
		} else {
			return null;
		}
	}

	public String getServiceDescription(String service_uri) {
		return getServiceInRegistry(service_uri).getDescription();
	}

	public List<ConditionalEffect> getServiceEffects(String service_uri) {
		if(getServiceInRegistry(service_uri) == null) return null;
		return getServiceInRegistry(service_uri).getEffects();
	}

	public List<String> getServiceInputs(String service_uri) {
		if(getServiceInRegistry(service_uri) == null) return null;
		return getServiceInRegistry(service_uri).getInputs();
	}

	public List<String> getServiceLocals(String service_uri) {
		return getServiceInRegistry(service_uri).getLocals();
	}

	public List<String> getServiceOutputs(String service_uri) {
		return getServiceInRegistry(service_uri).getOutputs();
	}

	public List<String> getServicePreconditionExpression(String service_uri) {
		return getServiceInRegistry(service_uri).getPreconditionExpression();
	}

	public String getSelfType(String s_uri) {
		return getServiceInRegistry(s_uri).getSelfType();
	}

	public String getSelfName(String s_uri) {
		return getServiceInRegistry(s_uri).getSelfName();
	}

	public List<Parameter> getServiceParameters(String s_uri) {
		return getServiceInRegistry(s_uri).getParameterList();
	}

	@Override
	public int getServiceType(String s_uri) {
		return getServiceInRegistry(s_uri).getServiceType();
	}

	private List<Statement> getStatementsFromPrecondition(String uri) {
		List<String> conds = null;
		List<Parameter> pars = null;
		conds = getServicePreconditionExpression(uri);
		pars = getServiceParameters(uri);

		List<Statement> sts = new ArrayList<Statement>();
		// get Statement list for every cond
		// since we transform a set of precodnitions to only one sparql query
		// we have to merge every conjunction of predicates in the conditions.
		for (String cond : conds) {
			try {
				PDDXMLHandler t = new PDDXMLHandler();
				SAXParser p = new SAXParser();
				p.setContentHandler(t);
				ByteArrayInputStream is = new ByteArrayInputStream(cond
						.getBytes());
				InputSource isource = new InputSource(is);
				p.parse(isource);

				for (Statement st : t.getConjList()) {
					sts.add(st);
				}
			} catch (Exception e) {
				logger.error(e);
				e.printStackTrace();
			}
		}

		// add statements from parameters
		// to ensure that every input is of the right type
		List<Statement> p_sts = getStatementsFromParameters(pars);
		for (Statement s : p_sts) {
			sts.add(s);
		}
		return sts;
	}

	private List<Statement> getStatementsFromParameters(List<Parameter> pars) {
		List<Statement> sts = new ArrayList<Statement>();
		for (Parameter p : pars) {
			for (String t : p.getTypes()) {
				Statement s = new StatementImpl("?" + p.getName(), rdf_type, t);
				sts.add(s);
			}
		}
		return sts;
	}
}
