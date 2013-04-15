package de.dfki.isreal.semantic.services;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openrdf.model.Value;

import de.dfki.isreal.main.Config;
import de.dfki.isreal.components.GSE;
import de.dfki.isreal.data.BindingList;
import de.dfki.isreal.data.impl.BindingListImpl;
import de.dfki.isreal.helpers.SparqlQueryHelper;
import de.dfki.isreal.semantic.oms.components.DetailedServiceRegistry;
import de.dfki.isreal.data.ServiceWrapper;
import de.dfki.isreal.data.impl.InvokationTask;
import eu.larkc.core.data.CloseableIterator;
import eu.larkc.core.data.VariableBinding;
import eu.larkc.core.data.VariableBinding.Binding;
import eu.larkc.core.query.SPARQLQuery;


/**
 * A ServiceExecutor gets a ServiceTask in the constructor and creates an
 * instance of the ServiceImplementationThread, i.e. the actual service
 * implementation.
 * 
 * After the ServiceImplementationThread is dead, the Executor collects
 * the outputs and the SEServices to check and puts them in the according
 * queues.
 *  
 * @author stenes
 *
 */
public class ServiceExecutor extends Thread {
	private static Logger logger = Logger.getLogger(ServiceExecutor.class);

	private InvokationTask task = null;
	private DetailedServiceRegistry s_reg = null;
	private SEServiceInvokationQueue se_queue = null;
	private OutputQueue output_queue = null;
	private List<InvokationTask> tasks_to_exec = null;
	
	public ServiceExecutor(InvokationTask t, DetailedServiceRegistry reg, OutputQueue queue, SEServiceInvokationQueue se){
		task = t;
		s_reg = reg;
		se_queue = se;
		output_queue = queue;
		tasks_to_exec = new ArrayList<InvokationTask>();
	}
	
	@Override
	public void run() {
		String service_uri = task.getServiceUri();
		ServiceWrapper sw = s_reg.getServiceInRegistry(service_uri);

		String class_name = service_uri
				.substring(service_uri.lastIndexOf("#") + 1);
		class_name = "de.dfki.isreal.semantic.services.thread.impl." + class_name;
		logger.debug("Try to initialize service thread implementation: "
				+ class_name);
		ServiceImplementationThread service_instance = null;
		Class thread_class;
		try {
			thread_class = Class.forName(class_name);
			Constructor cons = thread_class.getConstructor(
					ServiceWrapper.class, BindingList.class);
			// need to have standard constructor. Else put this code in every
			// if clause beneath.
			service_instance = (ServiceImplementationThread) cons.newInstance(
					sw, task.getBinding());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		try {
			logger.info("Start ServiceImplThread.");
			service_instance.start();
			service_instance.join();
			logger.info("ServiceImplThread joined.");
		} catch (InterruptedException e) {
			System.out
					.println("Thread interrupted, this should not happen or not hurt.");
			e.printStackTrace();
		}

		List<String> out = service_instance.getOutput();
		output_queue.addOutput(task.getId(), out);
		List<String> services = service_instance.getServicesToCheck();
		logger.info("ToCheck: " + services.size());
		applySEServices(services);
		se_queue.addSEServicesToCheckList(tasks_to_exec);
		logger.info("Executor finished.");
	}
	
	private void applySEServices(List<String> services) {
		for (String serv : services) {
			List<String> inps = s_reg.getServiceInputs(serv);
			if (inps.size() == 1) {
				String self_name = s_reg.getSelfName(serv);
				String self_type = s_reg.getSelfType(serv);

				String q = "SELECT ?x WHERE { "
						+ "?x <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> "
						+ "<" + self_type + "> . } ";

				SPARQLQuery theQuery = SparqlQueryHelper.createSparqlQuery(q);
				VariableBinding bnd;
				bnd = ((GSE) Config.getComponent(Config.getComponentNameForRole("GSE"))).sparqlSelect(theQuery, null);

				CloseableIterator<Binding> it = bnd.iterator();
				if (it == null) {
					logger.info(serv + " checked but not applicable.");
				} else {
					while (it.hasNext()) {
						Binding b = it.next();
						List<Value> vals = b.getValues();
						String s = vals.get(bnd.getVariables().indexOf("x"))
								.stringValue();

						BindingList inp_bnd = new BindingListImpl();
						inp_bnd.addPair(self_name, s);
						logger.info("Task found...");
						tasks_to_exec.add(new InvokationTask(serv, -1, inp_bnd));
					}
				}
			}

		}
	}

	public List<InvokationTask> getTasksToExecute() {
		return tasks_to_exec;
	}
	

}
