package de.dfki.isreal.data.impl;


import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;



import eu.larkc.core.data.CloseableIterator;
import eu.larkc.core.data.VariableBinding;

/**
 * VariableBinding implementation. Since ISReal is using the Larkc Reasoner
 * interface it needs to implements the VariableBinding as result of the SPARQL
 * Select query. For the different triple store plugins specific
 * implementations are used to create the VariableBinding.
 * 
 * This implementation uses the ResultSet from Jena plugin.
 * @author stenes
 *
 */
public class VariableBindingResultSetImpl implements VariableBinding, Serializable {

	CloseableIterator<Binding> binding;
	List<String> variables;

	public VariableBindingResultSetImpl(ResultSet rs) {
		variables = rs.getResultVars();
		List<Binding> b_list = new ArrayList<Binding>();
		while(rs.hasNext()){
			List<Value> vals = new ArrayList<Value>();
			QuerySolution sol = rs.next();
			for (String var : variables){
				vals.add(getOpenRDFValue(sol.get(var).toString()));
			}
			Binding bs_larkc = new BindingImpl(vals);
			b_list.add(bs_larkc);
		}
		binding = new CloseableIteratorImpl(b_list);
	}

	private Value getOpenRDFValue(String string) {
		ValueFactory v_fac = new ValueFactoryImpl();
		Value v = null;
		if (string.startsWith("_:")){
			v = v_fac.createBNode(string);
		}else if (string.startsWith("http:") || string.startsWith("file:")){
			v = v_fac.createURI(string);
		}else{
			v = v_fac.createLiteral(string);
		}
		return v;
	}

	
	public List<String> getVariables() {
		return variables;
	}

	
	public CloseableIterator<Binding> iterator() {

		return binding;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeObject(variables);
		out.writeObject(binding);
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		variables = (List<String>) in.readObject();
		binding = (CloseableIterator<Binding>) in.readObject();
	}

}
