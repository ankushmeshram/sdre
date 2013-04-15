package de.dfki.isreal.data.impl;

import java.io.Serializable;

import org.openrdf.model.Resource;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.model.impl.ValueFactoryImpl;

import de.dfki.isreal.data.Statement;

/**
 * ISReal Statement implementation. It also implements the sesame openrdf
 * Statement interface.
 * 
 * @author stenes
 *
 */
public class StatementImpl implements Statement, Serializable, org.openrdf.model.Statement  {

	String subj;
	String pred;
	String obj;

	public StatementImpl(String s, String p, String o) {
		subj = s;
		pred = p;
		obj = o;
	}

	
	public String getObjectString() {
		return obj;
	}

	
	public String getPredicateString() {
		return pred;
	}

	
	public String getSubjectString() {
		return subj;
	}

	
	public String stringValue() {
		return subj + " -- " + pred + " -- " + obj;
	}

	public boolean equals(Object s) {
		Statement st = (Statement) s;
		return st.getSubjectString().equals(subj) && st.getPredicateString().equals(pred)
				&& st.getObjectString().equals(obj);
	}
	
	public int hashCode(){
		int hash = (0xFF0000 & subj.hashCode()) | 
			(0x00FF00 & pred.hashCode()) |
			(0x0000FF & obj.hashCode());
		return hash;
	}

	@Override
	public Value getObject() {
		Value o;
		ValueFactory v_fac = new ValueFactoryImpl();
		if (obj.startsWith("_:")){
			o = v_fac.createBNode(obj);
		}else if (obj.startsWith("http:") || obj.startsWith("file:")){
			o = v_fac.createURI(obj);
		}else{
			o = v_fac.createLiteral(obj);
		}
		return o;
	}


	@Override
	public URI getPredicate() {
		return new URIImpl(pred);
	}


	@Override
	public Resource getSubject() {
		Resource s;
		ValueFactory v_fac = new ValueFactoryImpl();
		if (subj.startsWith("_:")){
			s = v_fac.createBNode(subj);
		}else{
			s = v_fac.createURI(subj);
		}
		return s;
	}


	@Override
	public Resource getContext() {
		return null;
	}

}
