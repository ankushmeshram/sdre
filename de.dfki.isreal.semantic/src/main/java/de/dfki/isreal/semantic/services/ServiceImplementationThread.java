package de.dfki.isreal.semantic.services;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openrdf.model.Value;
import org.xml.sax.InputSource;

import com.sun.org.apache.xerces.internal.parsers.SAXParser;

import de.dfki.isreal.helpers.Profiler;
import de.dfki.isreal.main.Config;
import de.dfki.isreal.network.callerfactories.DemonstratorCallerFactory;
import de.dfki.isreal.network.callerfactories.GraphicsCallerFactory;
import de.dfki.isreal.network.protos.ExchangeDataProtos.DemonstratorMessage;
import de.dfki.isreal.components.GSE;
import de.dfki.isreal.data.BindingList;
import de.dfki.isreal.data.Statement;
import de.dfki.isreal.data.impl.StatementImpl;
import de.dfki.isreal.helpers.SparqlQueryHelper;
import de.dfki.isreal.helpers.StatementHelpers;
import de.dfki.isreal.data.Parameter;
import de.dfki.isreal.data.ServiceWrapper;
import de.dfki.isreal.semantic.services.helpers.PDDXMLHandler;
import eu.larkc.core.data.BooleanInformationSet;
import eu.larkc.core.data.CloseableIterator;
import eu.larkc.core.data.VariableBinding;
import eu.larkc.core.data.VariableBinding.Binding;
import eu.larkc.core.query.SPARQLQuery;


/**
 * This abstract class represents the implementation of an ISReal service.
 * For every semantic service in ISReal there is an implementation of this class.
 * When invoking a semantic service via the ServiceExecution in ISReal, this
 * implementation is initialized and started. This abstract class ensures, that
 * the first thing to check is the semantic precondition by sending a query to
 * the GSE. After that the abstract serviceImplementation method is called. This
 * method is then service dependant and can have any effect on the running ISReal
 * simulation. It could trigger animations, changes the GSE, invokes further
 * services (like SEServices) and much more. 
 *   
 * @author stenes
 *
 */
public abstract class ServiceImplementationThread extends Thread {
	
	private Logger logger = Logger.getLogger(ServiceImplementationThread.class);
	
	// used for demonstrator messages in the GUI
	private boolean logging = true;
	private DemonstratorCallerFactory dem = new DemonstratorCallerFactory();
	
	protected GraphicsCallerFactory graphics = new GraphicsCallerFactory();
	
	protected BindingList bnd;
	protected ServiceWrapper sw;
	
	protected List<String> output;
	protected List<String> services_to_check;
	
	static String rdf_type = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
	
	public ServiceImplementationThread(ServiceWrapper service, BindingList b) {
		bnd = b;
		sw = service;
		services_to_check = new ArrayList<String>();
	}
	
	public void run(){
		logger.info("SERVICE execution of " + sw.getServiceURI()+ " started at " + System.nanoTime());
		bnd = checkPrecondition();
		if (bnd != null){
			output = serviceImplementation();
			if (logging)
				dem.sendMessage(this.getClass().getSimpleName() + ": EXECUTED.",
						DemonstratorMessage.Component.GSE.getNumber(), DemonstratorMessage.DMsgType.EXECUTION.getNumber(), false);
		} else {
			if (logging)
				dem.sendMessage(this.getClass().getSimpleName() + ": precondition check FAILED.",
						DemonstratorMessage.Component.GSE.getNumber(), DemonstratorMessage.DMsgType.EXECUTION.getNumber(), false);
			
			output = null;
		}
		logger.info("SERVICE execution of " + sw.getServiceURI()+ " finished at " + System.nanoTime());
	}
	
	public List<String> getOutput(){
		return output;
	}
	
	public List<String> getServicesToCheck(){
		return services_to_check;
	}
	
	
	protected BindingList checkPrecondition() {
		Profiler.startMonitor(this.getClass().getName(), "checkPrecondition");
		
		// at this point it is checked if all the input variables
		// are substituted with a binding.
		for(String input : sw.getInputs()) {
			if(bnd.getInstance(input) == null) {
				// no instance for input variable in binding
				logger.warn(sw.getServiceURI() + " checked but not applicable: input binding is not complete.");
				Profiler.stopMonitor(this.getClass().getName(), "checkPrecondition");
				return null;
			}
		}
		
		Profiler.startMonitor(this.getClass().getName(), "transformPDDLPrecondition");

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
		List<Statement> p_sts= getStatementsFromParameters(pars);
		for (Statement s : p_sts){
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
		Profiler.stopMonitor(this.getClass().getName(), "transformPDDLPrecondition");
		if (vars.size() > 0) {

			// create SPARQL query
			SPARQLQuery theQuery = SparqlQueryHelper.createSparqlQuery(SparqlQueryHelper.SELECT,
					vars, sts, null);

			VariableBinding vb;
			vb = ((GSE) Config.getComponent(Config.getComponentNameForRole("GSE"))).sparqlSelect(theQuery, null);

			
			
			// add variable binding to inp_bnd
			CloseableIterator<Binding> it = vb.iterator();
			if (it == null || !it.hasNext()) {
				logger.info(sw.getServiceURI() + " checked but not applicable.");
				Profiler.stopMonitor(this.getClass().getName(), "checkPrecondition");
				return null;
			} else {
				// TODO what does happen if there are more than one possible bindings
				// for a local variable? 
				// Here this variable binding is overwritten with the last one.
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
			Profiler.stopMonitor(this.getClass().getName(), "checkPrecondition");
			return bnd;
		} else {
			// create SPARQL ask
			SPARQLQuery theQuery = SparqlQueryHelper.createSparqlQuery(SparqlQueryHelper.ASK, null, sts, null);

			BooleanInformationSet b;
			b = ((GSE) Config.getComponent(Config.getComponentNameForRole("GSE"))).sparqlAsk(theQuery, null);

			if (b.getValue()) {
				// the precondition holds and no bnd were added
				Profiler.stopMonitor(this.getClass().getName(), "checkPrecondition");
				return bnd;
			} else {
				// precondition does not hold;
				Profiler.stopMonitor(this.getClass().getName(), "checkPrecondition");
				return null;
			}
		}
	}
	
	protected List<Statement> getStatementsFromParameters(List<Parameter> pars) {
		List<Statement> sts = new ArrayList<Statement>();
		for(Parameter p : pars){
			for (String t : p.getTypes()){
				Statement s = new StatementImpl("?" + p.getName(), rdf_type, t);
				sts.add(s);
			}
		}
		return sts;
	}
	
	protected List<Statement> substituteVariables(List<Statement> sts,
			BindingList input_bnd) {
		ArrayList<Statement> res = new ArrayList<Statement>();
		for (Statement st : sts) {
			String s = input_bnd.substituteVariable(st.getSubjectString());
			String o = input_bnd.substituteVariable(st.getObjectString());

			res.add(new StatementImpl(s, st.getPredicateString(), o));
		}
		return res;
	}
	
	protected List<String> catchVariables(List<Statement> sts) {
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

	protected abstract List<String> serviceImplementation();
}
