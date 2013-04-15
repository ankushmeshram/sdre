package de.dfki.isreal.data.impl;


import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;

import com.franz.agbase.BlankNode;
import com.franz.agbase.LiteralNode;
import com.franz.agbase.URINode;
import com.franz.agbase.ValueNode;
import com.franz.agbase.ValueObject;
import com.franz.agbase.ValueSetIterator;

import eu.larkc.core.data.CloseableIterator;
import eu.larkc.core.data.VariableBinding;

/**
 * VariableBinding implementation. Since ISReal is using the Larkc Reasoner
 * interface it needs to implements the VariableBinding as result of the SPARQL
 * Select query. For the different triple store plugins specific
 * implementations are used to create the VariableBinding.
 * 
 * This implementation uses the ValueSetIterator from the AllegroGraph plugin.
 * 
 * @author stenes
 *
 */
public class VariableBindingValueSetImpl implements VariableBinding, Serializable {

	CloseableIterator<Binding> binding;
	List<String> variables;

	
	public VariableBindingValueSetImpl(ValueSetIterator it){
		variables = new ArrayList<String>();
		for (String var : it.getNames()){
			variables.add(var);
		}
		
		List<Binding> b_list = new ArrayList<Binding>();
		while(it.hasNext()){
			List<Value> vals = new ArrayList<Value>();
			ValueObject[] objects = it.next();
			for (ValueObject ob : objects){
				vals.add(getOpenRDFValue(ob));
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

	private Value getOpenRDFValue(ValueObject ob) {
		ValueFactory v_fac = new ValueFactoryImpl();
		Value vl = null;
		if (ob == null) {
			return null;
		} else if (ob instanceof LiteralNode) {
			LiteralNode l = (LiteralNode)ob;
			vl = v_fac.createLiteral(l.getLabel());
		} else if (ob instanceof BlankNode) {
			BlankNode b = (BlankNode)ob;
			vl = v_fac.createBNode(b.getID());
		} else if (ob instanceof URINode) {
			URINode n = (URINode)ob;
			vl = v_fac.createURI(n.toString());
		} else {
			ValueNode n = (ValueNode)ob;
			vl = v_fac.createURI(n.toString());
		}
		return vl;
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
