package de.dfki.isreal.helpers;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.dfki.isreal.data.Statement;
import eu.larkc.core.query.SPARQLQuery;
import eu.larkc.core.query.SPARQLQueryImpl;


/**
 * FactoryClass for SPARQLQueries.
 * @author stenes
 *
 */
public class SparqlQueryHelper {
	private static Logger logger = Logger.getLogger(SparqlQueryHelper.class);
	
	public static int ASK = 1;
	public static int SELECT = 2;
	public static int CONSTRUCT = 3;
	public static int DESCRIBE = 4;

	public static String getSparqlTripleElement(String elem) {
		String res = "";

		if (elem.startsWith("_:")) {
			res = elem;
		} else if (elem.startsWith("http://")) {
			res = "<" + elem + ">";
		} else if (elem.startsWith("?")) {
			if (elem.substring(1).startsWith("http://")){
				res = "?" + StatementHelpers.getLocalnameFromURI(elem).substring(1);
				// without '#'
			} else {
				res = elem;
			}
		} else {
			res = "\"" + elem + "\"";
		}

		return res;
	}
	

	
	public static SPARQLQuery createSparqlQuery(String query){
		SPARQLQueryImpl q = new SPARQLQueryImpl(query);
		SparqlQMVisitor visitor = new SparqlQMVisitor();
		int type = 0;
		if(q.isAsk()) type = ASK;
		if(q.isSelect()) type = SELECT;
		if(q.isConstruct()) type = CONSTRUCT;
		if(q.isDescribe()) type = DESCRIBE;
		
		try {
			q.getParsedQuery().getTupleExpr().visit(visitor);
			return createSparqlQuery(type,
						visitor.getVariables(),
						visitor.getStatementPatterns(),
						visitor.getConstructPatterns());
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return q;
	}

	/**
	 * This method parses the String q and creates a LARKC SPARQLQuery.
	 * 
	 * @param q
	 * @return
	 */
	public static SPARQLQuery createSparqlQuery(int type, List<String> vars,
			List<Statement> patterns, List<Statement> construct) {
		SPARQLQuery query = null;
		if (type == ASK) {
			query = createASKQuery(patterns);
		} else if (type == SELECT) {
			query = createSELECTQuery(vars, patterns);
		} else if (type == CONSTRUCT) {
			query = createCONSTRUCTQuery(patterns, construct);
		} else if (type == DESCRIBE) {
			logger.error("DESCRIBE Queries are not supported...");
		} else {
			logger.error("Error: Wrong query type...");
		}
		return query;
	}

	public static SPARQLQuery createCONSTRUCTQuery(List<Statement> patterns, List<Statement> construct) {
		String res =  "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
			+ " PREFIX owl: <http://www.w3.org/2002/07/owl#>"
			+ "CONSTRUCT { \n" + getTriples(construct) + "} WHERE { \n"
						+ getTriples(patterns) + " }";
		return new SPARQLQueryImpl(res);
	}

	public static SPARQLQuery createSELECTQuery(List<String> vars,
			List<Statement> patterns) {

		String res = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ " PREFIX owl: <http://www.w3.org/2002/07/owl#>"
				+ "SELECT "
				+ getVariableString(vars)
				+ " WHERE { \n"
				+ getTriples(patterns) + " }";
		return new SPARQLQueryImpl(res);
	}
	
	public static SPARQLQuery createSPARQLQuery(List<Statement> patterns) {

		List<String> vars = getVariableList(patterns);
		
		if (vars.size() == 0){
			// ask query
			return createASKQuery(patterns);
		}else {
			// select query
			return createSELECTQuery(vars, patterns);
		}
	}

	private static List<String> getVariableList(List<Statement> patterns) {
		List<String> vars = new ArrayList<String>();
		for (Statement s : patterns){
			if (s.getSubjectString().startsWith("?")){
				String var = getSparqlTripleElement(s.getSubjectString());
				if (!vars.contains(var)){
					vars.add(var);
				}
			}
			if (s.getPredicateString().startsWith("?")){
				String var = getSparqlTripleElement(s.getPredicateString());
				if (!vars.contains(var)){
					vars.add(var);
				}
			}
			if (s.getObjectString().startsWith("?")){
				String var = getSparqlTripleElement(s.getObjectString());
				if (!vars.contains(var)){
					vars.add(var);
				}
			}
		}
		return vars;
	}



	public static SPARQLQuery createASKQuery(List<Statement> patterns) {

		String res = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ " PREFIX owl: <http://www.w3.org/2002/07/owl#>"
				+ " ASK { \n"
				+ getTriples(patterns) + " }";
		return new SPARQLQueryImpl(res);
	}

	private static String getTriples(List<Statement> sts) {
		String res = "";

		// sort the triples in a decreasing number of variables
		List<Statement> none_var = new ArrayList<Statement>();
		List<Statement> one_var = new ArrayList<Statement>();
		List<Statement> two_vars = new ArrayList<Statement>();
		List<Statement> three_vars = new ArrayList<Statement>();

		for (Statement s : sts) {
			int vars_count = 0;
			if (s.getSubjectString().startsWith("?")) {
				vars_count++;
			}
			if (s.getPredicateString().startsWith("?")) {
				vars_count++;
			}
			if (s.getObjectString().startsWith("?")) {
				vars_count++;
			}
			if (vars_count == 0)
				none_var.add(s);
			else if (vars_count == 1)
				one_var.add(s);
			else if (vars_count == 2)
				two_vars.add(s);
			else if (vars_count == 3)
				three_vars.add(s);
		}

		List<Statement> sorted = new ArrayList<Statement>();
		for (Statement s : three_vars) {
			sorted.add(s);
		}
		for (Statement s : two_vars) {
			sorted.add(s);
		}
		for (Statement s : one_var) {
			sorted.add(s);
		}
		for (Statement s : none_var) {
			sorted.add(s);
		}

		for (Statement s : sorted) {
			res = res + getSparqlTripleElement(s.getSubjectString()) + " "
					+ getSparqlTripleElement(s.getPredicateString()) + " "
					+ getSparqlTripleElement(s.getObjectString()) + " .\n";
		}
		return res;
	}

	private static String getVariableString(List<String> vars) {
		String res = "";
		for (String v : vars) {
			res = res + getSparqlTripleElement(v) + " ";
		}
		return res;
	}

}
