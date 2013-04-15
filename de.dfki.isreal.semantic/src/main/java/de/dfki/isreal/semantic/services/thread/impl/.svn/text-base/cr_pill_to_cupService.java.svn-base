package de.dfki.isreal.semantic.services.thread.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openrdf.model.Value;

import de.dfki.isreal.components.GSE;
import de.dfki.isreal.data.BindingList;
import de.dfki.isreal.data.ServiceWrapper;
import de.dfki.isreal.data.Statement;
import de.dfki.isreal.data.impl.StatementImpl;
import de.dfki.isreal.helpers.SparqlQueryHelper;
import de.dfki.isreal.main.Config;
import de.dfki.isreal.network.callerfactories.GraphicsCallerFactory;
import de.dfki.isreal.semantic.services.SEServiceImplementationThread;

import eu.larkc.core.data.CloseableIterator;
import eu.larkc.core.data.VariableBinding;
import eu.larkc.core.data.VariableBinding.Binding;
import eu.larkc.core.query.SPARQLQuery;
import eu.larkc.core.query.SPARQLQueryImpl;

public class cr_pill_to_cupService extends SEServiceImplementationThread {
	private static Logger logger = Logger.getLogger(cr_pill_to_cupService.class);
	

	private static String type = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
	private static String located = "http://www.dfki.de/isreal/spatial_ontology.owl#isLocatedIn";
	private static String hasColor = "http://www.dfki.de/isreal/messemodul.owl#hasColor";
	
	public cr_pill_to_cupService(ServiceWrapper service, BindingList b) {
		super(service, b);
	}

	@Override
	protected List<String> serviceImplementation() {
		if (Config.isRoleRegistered("GSE")){
			
			// Trigger animation with the correct color.
			List<Statement> patterns = new ArrayList<Statement>();
			List<String> vars = new ArrayList<String>();
			Statement st = new StatementImpl(bnd.getInstance("http://www.dfki.de/isreal/cr_pill_to_cup.owl#self"), hasColor, "?col");
			patterns.add(st);
			vars.add("?col");
			
			SPARQLQuery q = SparqlQueryHelper.createSELECTQuery(vars, patterns);
			VariableBinding b = ((GSE) Config.getComponent(Config.getComponentNameForRole("GSE"))).sparqlSelect(q, null);
			CloseableIterator<Binding> it = b.iterator();
			String col = null;
			if (it == null) {
				logger.error("Iterator is null!!!!");
			} else {
				while (it.hasNext()) {
					Binding bg = it.next();
					List<Value> vals = bg.getValues();
					col = vals.get(b.getVariables().indexOf("col"))
								.stringValue();
				}
			}
			if (col == null) logger.error("Could not determine color.");
			if (col.equalsIgnoreCase("http://www.dfki.de/isreal/messemodul_abox.owl#BLUE")){
				graphics.sendAnimationCall("blue_pill_to_cup", null);
			}
			if (col.equalsIgnoreCase("http://www.dfki.de/isreal/messemodul_abox.owl#WHITE")){
				graphics.sendAnimationCall("white_pill_to_cup", null);
			}
			if (col.equalsIgnoreCase("http://www.dfki.de/isreal/messemodul_abox.owl#RED")){
				graphics.sendAnimationCall("red_pill_to_cup", null);
			}

			// wait for animation
			waitMillis(1000);
			
			// effects
			Statement in = new StatementImpl(bnd.getInstance("http://www.dfki.de/isreal/cr_pill_to_cup.owl#self"), located, bnd.getInstance("http://www.dfki.de/isreal/cr_pill_to_cup.owl#cuparea"));
			List<Statement> ins = new ArrayList<Statement>();
			ins.add(in);
			
			Statement out = new StatementImpl(bnd.getInstance("http://www.dfki.de/isreal/cr_pill_to_cup.owl#self"), located, bnd.getInstance("http://www.dfki.de/isreal/cr_pill_to_cup.owl#sliderarea"));
			List<Statement> outs = new ArrayList<Statement>();
			outs.add(out);
			
			((GSE) Config.getComponent(Config.getComponentNameForRole("GSE"))).insert(ins);
			((GSE) Config.getComponent(Config.getComponentNameForRole("GSE"))).remove(outs);
			
			// no further checks
			
			return new ArrayList<String>();
		} else {
			logger.error("No GlobalSE available in thread: " + cr_pill_to_cupService.class.getName());
		}
		return new ArrayList<String>();
	}
	
	/**
	 * This methods waits for the given numer of milliseconds.
	 * 
	 * @param i
	 *            - Milliseconds to wait.
	 */
	private static void waitMillis(int i) {
		long start = System.currentTimeMillis();
		while ((System.currentTimeMillis() - start) < i) {
		}
	}

}
