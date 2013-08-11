package de.dfki.isreal.data.impl;

import info.aduna.iteration.CloseableIteration;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.openrdf.query.BindingSet;
import org.openrdf.query.QueryEvaluationException;

import eu.larkc.core.data.CloseableIterator;
import eu.larkc.core.data.VariableBinding;

/**
 * VariableBinding implementation. Since ISReal is using the Larkc Reasoner
 * interface it needs to implements the VariableBinding as result of the SPARQL
 * Select query. For the different triple store plugins specific
 * implementations are used to create the VariableBinding.
 * 
 * This implementation uses the ClosableIteration from the OWLIM plugin.
 *   
 * @author stenes
 *
 */
public class VariableBindingIterationImpl implements VariableBinding, Serializable {

	CloseableIterator<Binding> binding;
	List<String> variables;
	
	public VariableBindingIterationImpl(
			CloseableIteration<BindingSet, QueryEvaluationException> bnd) {
		List<Binding> b_list = new ArrayList<Binding>();
		Set<String> vars = null;
		try {
			int i=0;
			while (bnd.hasNext()) {
				List<Value> vl = new ArrayList<Value>();
				BindingSet bs_owlim = bnd.next();
				if (i==0){
					vars = bs_owlim.getBindingNames();				
				}
				Iterator<org.openrdf.query.Binding> it = bs_owlim.iterator();
				while (it.hasNext()){
					org.openrdf.query.Binding b = it.next();
					vl.add(getOpenRDFValue(b.getValue().stringValue()));
				}
				Binding bs_larkc = new BindingImpl(vl);
				b_list.add(bs_larkc);
				i++;
			}
		} catch (QueryEvaluationException e1) {
			e1.printStackTrace();
		}

		binding = new CloseableIteratorImpl(b_list);
		if (vars != null) {
			variables = new LinkedList<String>(vars);
		} else {
			variables = new LinkedList<String>();
		}

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
