/**
 * 
 */
package de.dfki.isreal.semantic.oms.components.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;

import de.derivo.sparqldlapi.QueryArgument;
import de.derivo.sparqldlapi.QueryBinding;
import de.derivo.sparqldlapi.QueryResult;
import de.derivo.sparqldlapi.types.QueryArgumentType;
import de.dfki.isreal.data.impl.BindingImpl;
import de.dfki.isreal.data.impl.CloseableIteratorImpl;
import eu.larkc.core.data.CloseableIterator;
import eu.larkc.core.data.VariableBinding;

/**
 * @author anme05
 *
 */
public class VariableBindingQueryResultImpl implements VariableBinding {

	CloseableIterator<Binding> binding;
	List<String> variables =  new ArrayList<String>();
	
	public VariableBindingQueryResultImpl(QueryResult result) {
		Set<QueryArgument> qas = result.get(0).getBoundArgs();
		
		for(QueryArgument qa : qas) {
			variables.add(qa.getValue());
		}
		
		List<Binding> b_list = new ArrayList<Binding>();
					
		Iterator<QueryBinding> it = result.iterator();
		while(it.hasNext()) {
			QueryBinding binding = it.next();
			List<Value> vals = new ArrayList<Value>();
			
			for(String var : variables) {
				QueryArgument qa =  new QueryArgument(QueryArgumentType.VAR, var);
				binding.get(qa).getValue();
				
				vals.add(getOpenRDFValue(binding.get(qa).getValue()));
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

}
