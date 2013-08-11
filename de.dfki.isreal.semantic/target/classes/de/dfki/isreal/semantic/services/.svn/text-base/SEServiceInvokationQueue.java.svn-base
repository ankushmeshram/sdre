package de.dfki.isreal.semantic.services;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openrdf.model.Value;
import org.xml.sax.InputSource;

import com.sun.org.apache.xerces.internal.parsers.SAXParser;

import de.dfki.isreal.helpers.Profiler;
import de.dfki.isreal.main.Config;
import de.dfki.isreal.components.GSE;
import de.dfki.isreal.data.BindingList;
import de.dfki.isreal.data.Statement;
import de.dfki.isreal.data.impl.BindingListImpl;
import de.dfki.isreal.data.impl.StatementImpl;
import de.dfki.isreal.helpers.SparqlQueryHelper;
import de.dfki.isreal.helpers.StatementHelpers;
import de.dfki.isreal.data.Parameter;
import de.dfki.isreal.data.ServiceWrapper;
import de.dfki.isreal.data.impl.InvokationTask;
import de.dfki.isreal.semantic.oms.components.DetailedServiceRegistry;
import de.dfki.isreal.semantic.services.helpers.PDDXMLHandler;
import eu.larkc.core.data.BooleanInformationSet;
import eu.larkc.core.data.CloseableIterator;
import eu.larkc.core.data.VariableBinding;
import eu.larkc.core.data.VariableBinding.Binding;
import eu.larkc.core.query.SPARQLQuery;

public class SEServiceInvokationQueue implements Runnable {
	private static Logger logger = Logger
			.getLogger(SEServiceInvokationQueue.class);

	static String rdf_type = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";

	private DetailedServiceRegistry s_reg = null;

	private List<InvokationTask> check_to_exec = null;
	private List<InvokationTask> applicants_for_check_to_exec = null;

	private boolean stop = false;
	private boolean running = false;
	private Thread thread = null;

	public SEServiceInvokationQueue(DetailedServiceRegistry sreg) {
		s_reg = sreg;
		applicants_for_check_to_exec = new ArrayList<InvokationTask>();
		check_to_exec = new ArrayList<InvokationTask>();
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		while (!stop) {
			synchronized (check_to_exec) {
				if (check_to_exec.size() == 0) {
					try {
						logger.info("Wait for SEServices to check");
						check_to_exec.wait();
					} catch (InterruptedException e) {
					}
				}

				running = true;
				logger.info("SEServices running...");
				
				// create Implementation Threads
				List<SEServiceImplementationThread> candidates = new ArrayList<SEServiceImplementationThread>();
				for (InvokationTask s : check_to_exec) {
					String service_uri = s.getServiceUri();
					ServiceWrapper sw = s_reg.getServiceInRegistry(service_uri);

					String class_name = service_uri.substring(service_uri
							.lastIndexOf("#") + 1);
					class_name = "de.dfki.isreal.semantic.services.thread.impl."
							+ class_name;
					logger
							.debug("Try to initialize service thread implementation: "
									+ class_name);
					SEServiceImplementationThread service_instance = null;
					Class thread_class;
					try {
						thread_class = Class.forName(class_name);
						Constructor cons = thread_class.getConstructor(
								ServiceWrapper.class, BindingList.class);
						// need to have standard constructor. Else put this code
						// in every
						// if clause beneath.
						service_instance = (SEServiceImplementationThread) cons
								.newInstance(sw, s.getBinding());
						candidates.add(service_instance);
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
				}

				// check preconditions
				List<SEServiceImplementationThread> to_exec = new ArrayList<SEServiceImplementationThread>();
				for (SEServiceImplementationThread s : candidates) {
					BindingList bnd = s.checkPreconditionInFront();
					if (bnd != null) {
						// precondition true -> add to_exec
						to_exec.add(s);
					}
				}

				// execute implementation
				for (SEServiceImplementationThread s : to_exec) {
					s.start();
				}

				// wait for all to finish and catch all new SEServices to check
				for (SEServiceImplementationThread s : to_exec) {
					try {
						s.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					List<InvokationTask> check_next = getSEServiceApplicants(s
							.getServicesToCheck());
					addSEServicesToCheckList(check_next);
				}
				check_to_exec.clear();
				synchronized (applicants_for_check_to_exec){
					for (InvokationTask t : applicants_for_check_to_exec){
						check_to_exec.add(t);
					}
					applicants_for_check_to_exec.clear();
				}
				running = false;
				logger.info("SEServices stopped...");
			}
		}

	}

	public void addSEServicesToCheckList(List<InvokationTask> to_check) {
		synchronized (applicants_for_check_to_exec) {
			logger.info("Add applicants " + to_check.size());
			for (InvokationTask s : to_check) {
				applicants_for_check_to_exec.add(s);
			}
			if (!running) {
				synchronized (check_to_exec) {
					logger.info("SEServices not running...notifying...");
					for (InvokationTask s : applicants_for_check_to_exec){
						check_to_exec.add(s);
					}
					applicants_for_check_to_exec.clear();
					logger.info("Size: " + check_to_exec.size());
					check_to_exec.notify();
				}
			}
		}
	}

	private BindingList checkPrecondition(ServiceWrapper sw, BindingList bnd) {
		Profiler.startMonitor(this.getClass().getName(), "checkPrecondition");
		Profiler.startMonitor(this.getClass().getName(),
				"transformPDDLPrecondition");
		List<String> conds = null;
		List<Parameter> pars = null;
		conds = sw.getPreconditionExpression();
		pars = sw.getParameterList();

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

		// get substitute inp_bnd
		// since the input parameters from the service are given by the binding
		// they have to be substituted
		sts = substituteVariables(sts, bnd);

		// get remaining variables
		// the remaining variables (locals or outputs) has to be extracted for
		// the query
		List<String> vars = catchVariables(sts);

		// end of transformation
		Profiler.stopMonitor(this.getClass().getName(),
				"transformPDDLPrecondition");
		if (vars.size() > 0) {

			// create SPARQL query
			SPARQLQuery theQuery = SparqlQueryHelper.createSparqlQuery(
					SparqlQueryHelper.SELECT, vars, sts, null);

			VariableBinding vb;
			vb = ((GSE) Config.getComponent(Config.getComponentNameForRole("GSE"))).sparqlSelect(theQuery, null);

			// add variable binding to inp_bnd
			CloseableIterator<Binding> it = vb.iterator();
			if (it == null || !it.hasNext()) {
				logger
						.info(sw.getServiceURI()
								+ " checked but not applicable.");
				Profiler.stopMonitor(this.getClass().getName(),
						"checkPrecondition");
				return null;
			} else {
				while (it.hasNext()) {
					Binding b = it.next();
					List<Value> vals = b.getValues();
					for (String var : vars) {
						if (var.startsWith("?")) {
							var = var.substring(1);
						}
						// var_name is the local name without leading ? and no
						// '#'
						// this name was constructed in SparqlQueryHelper
						// since ':' and '#' are not allowed in var names for
						// sparql queries.
						String var_name = StatementHelpers.getLocalnameFromURI(
								var).substring(1);
						int var_index = vb.getVariables().indexOf(var_name);
						String val = vals.get(var_index).stringValue();
						bnd.addPair(var, val);
					}
				}
			}
			it.close();
			Profiler
					.stopMonitor(this.getClass().getName(), "checkPrecondition");
			return bnd;
		} else {
			// create SPARQL ask
			SPARQLQuery theQuery = SparqlQueryHelper.createSparqlQuery(
					SparqlQueryHelper.ASK, null, sts, null);

			BooleanInformationSet b;
			b = ((GSE) Config.getComponent("GSE")).sparqlAsk(theQuery, null);

			if (b.getValue()) {
				// the precondition holds and no bnd were added
				Profiler.stopMonitor(this.getClass().getName(),
						"checkPrecondition");
				return bnd;
			} else {
				// precondition does not hold;
				Profiler.stopMonitor(this.getClass().getName(),
						"checkPrecondition");
				return null;
			}
		}
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

	private List<Statement> substituteVariables(List<Statement> sts,
			BindingList input_bnd) {
		ArrayList<Statement> res = new ArrayList<Statement>();
		for (Statement st : sts) {
			String s = input_bnd.substituteVariable(st.getSubjectString());
			String o = input_bnd.substituteVariable(st.getObjectString());

			res.add(new StatementImpl(s, st.getPredicateString(), o));
		}
		return res;
	}

	private List<String> catchVariables(List<Statement> sts) {
		List<String> vars = new ArrayList<String>();
		for (Statement st : sts) {
			String s = st.getSubjectString();
			if (isVariable(s) && !vars.contains(s)) {
				vars.add(s);
			}
			String o = st.getObjectString();
			if (isVariable(o) && !vars.contains(o)) {
				vars.add(o);
			}
		}
		return vars;
	}

	private boolean isVariable(String s) {
		return s.trim().startsWith("?");
	}

	private List<InvokationTask> getSEServiceApplicants(List<String> services) {
		List<InvokationTask> applicants = new ArrayList<InvokationTask>();
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
						applicants.add(new InvokationTask(serv, -1, inp_bnd));
					}
				}
			}

		}
		return applicants;
	}

}
