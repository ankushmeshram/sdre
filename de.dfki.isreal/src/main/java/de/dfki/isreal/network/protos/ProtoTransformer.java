package de.dfki.isreal.network.protos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;

import de.dfki.isreal.data.SPARQLDLResult;
import de.dfki.isreal.data.Statement;
import de.dfki.isreal.data.impl.BindingImpl;
import de.dfki.isreal.data.impl.SPARQLDLResultImpl;
import de.dfki.isreal.data.impl.SetOfStatementsStatementListImpl;
import de.dfki.isreal.data.impl.StatementImpl;
import de.dfki.isreal.data.impl.TransCloseableIterator;
import de.dfki.isreal.data.impl.VariableBindingIteratorImpl;
import de.dfki.isreal.network.protos.ExchangeDataProtos.ISRealAnswer;
import de.dfki.isreal.network.protos.ExchangeDataProtos.StatementList;
import de.dfki.isreal.network.protos.ExchangeDataProtos.Substitution;
import eu.larkc.core.data.BooleanInformationSet;
import eu.larkc.core.data.BooleanInformationSetImpl;
import eu.larkc.core.data.CloseableIterator;
import eu.larkc.core.data.SetOfStatements;
import eu.larkc.core.data.VariableBinding;
import eu.larkc.core.data.VariableBinding.Binding;
import eu.larkc.core.query.SPARQLQuery;
import eu.larkc.core.query.SPARQLQueryImpl;

/**
 * This static class transforms datatypes needed in the messages into protobufs
 * and back into java objects.
 * 
 * 
 * //Todo Hack!!!, search for better solutions. Maybe use proto objects also inside
 * ISReal, where we use our own implementations anyway. 
 * 
 * @author stenes
 *
 */
public class ProtoTransformer {

		
	public static List<Statement> getListOfStatements(List<ExchangeDataProtos.Statement> proto_list) {
		List<Statement> transformed = new ArrayList<Statement>();
		for (ExchangeDataProtos.Statement p_st : proto_list) {
			transformed.add(getISRealStatement(p_st));
		}
		return transformed;
	}

	public static Statement getISRealStatement(
			de.dfki.isreal.network.protos.ExchangeDataProtos.Statement p_st) {
		StatementImpl transformed = new StatementImpl(p_st.getSubject(), p_st
				.getPredicate(), p_st.getObject());
		return transformed;
	}

	public static SPARQLQuery getSPARQLQuery(String queryString) {
		return new SPARQLQueryImpl(queryString);
	}

	public static boolean getBoolean(BooleanInformationSet sparqlAsk) {
		return sparqlAsk.getValue();
	}

	public static List<ExchangeDataProtos.Statement> getStatementList(SetOfStatements sparqlConstruct) {
		List<ExchangeDataProtos.Statement> transformed = new ArrayList<ExchangeDataProtos.Statement>();
		CloseableIterator<org.openrdf.model.Statement> it = sparqlConstruct
				.getStatements();
		while (it.hasNext()) {
			transformed.add(getProtoStatement(it.next()));

		}
		return transformed;
	}

	private static de.dfki.isreal.network.protos.ExchangeDataProtos.Statement getProtoStatement(
			org.openrdf.model.Statement next) {
		ExchangeDataProtos.Statement.Builder transformed = ExchangeDataProtos.Statement
				.newBuilder();
		transformed.setSubject(next.getSubject().stringValue());
		transformed.setPredicate(next.getPredicate().stringValue());
		transformed.setObject(next.getObject().stringValue());
		return transformed.build();
	}

	public static List<ExchangeDataProtos.Binding> getBindingList(VariableBinding sparqlSelect) {
		List<ExchangeDataProtos.Binding> transformed = new ArrayList<ExchangeDataProtos.Binding>();
		List<String> vars = sparqlSelect.getVariables();
		CloseableIterator<Binding> bnds = sparqlSelect.iterator();
		while (bnds.hasNext()) {
			ExchangeDataProtos.Binding.Builder p_binding = ExchangeDataProtos.Binding.newBuilder();
			Binding b = bnds.next();
			List<Value> vals = b.getValues();
			if (vals.size() == vars.size()) {
				for (int i = 0; i < vars.size(); i++) {
					p_binding.addSubstitutions(getSubstitution(vars.get(i), vals.get(i).stringValue()));
				}
			}
			transformed.add(p_binding.build());
		}
		return transformed;
	}

	private static Substitution getSubstitution(String variable, String value) {
		Substitution.Builder transformed = Substitution.newBuilder();
		transformed.setVariable(variable);
		transformed.setValue(value);
		return transformed.build();
	}

	public static List<ExchangeDataProtos.Statement> getStatementList(List<Statement> objectFacts) {
		List<ExchangeDataProtos.Statement> transformed = new ArrayList<ExchangeDataProtos.Statement>();
		for (Statement st : objectFacts){
			transformed.add(getProtoStatement(st));
		}
		return transformed;
	}

	private static ExchangeDataProtos.Statement getProtoStatement(
			Statement st) {
		ExchangeDataProtos.Statement.Builder transformed = ExchangeDataProtos.Statement.newBuilder();
		transformed.setSubject(st.getSubjectString());
		transformed.setPredicate(st.getPredicateString());
		transformed.setObject(st.getObjectString());
		return transformed.build();
	}

	public static VariableBinding getVariableBinding(ISRealAnswer a) {
		List<VariableBinding.Binding> bnds = getLarkcBindingList(a.getBindingsList());
		TransCloseableIterator it = new TransCloseableIterator(bnds.iterator());
		List<String> vars = getVariableList(a.getBindingsList());
		VariableBinding vb = new VariableBindingIteratorImpl(it, vars);
		return vb;
	}
	
	private static List<VariableBinding.Binding> getLarkcBindingList(List<ExchangeDataProtos.Binding> list) {
		List<VariableBinding.Binding> larkc_bl = new ArrayList<VariableBinding.Binding>();
		for (ExchangeDataProtos.Binding b : list){
			List<Value> vals = new ArrayList<Value>();
			for (Substitution s : b.getSubstitutionsList()){
				vals.add(getOpenRDFValue(s.getValue()));
			}
			larkc_bl.add(new BindingImpl(vals));
		}
		return larkc_bl;
	}

	private static Value getOpenRDFValue(String string) {
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
	
	private static List<String> getVariableList(List<ExchangeDataProtos.Binding> list) {
		List<String> vars = new ArrayList<String>();
		if (list.size() > 0){
			ExchangeDataProtos.Binding b = list.get(0);
			for (Substitution s : b.getSubstitutionsList()){
				vars.add(s.getVariable());
			}
		}
		return vars;
	}

	public static SetOfStatements getSetOfStatements(ISRealAnswer a) {
		return new SetOfStatementsStatementListImpl(getListOfStatements(a.getStatementsList()));
	}

	public static BooleanInformationSet getBooleanInformationSet(ISRealAnswer a) {
		return new BooleanInformationSetImpl(a.getBoolVal());
	}

	public static SPARQLDLResult getSPARQLDLResult(ISRealAnswer a) {
		if (a.hasBoolVal()){
			return new SPARQLDLResultImpl(a.getBoolVal());
		} else{ 
			return new SPARQLDLResultImpl(getVariableBinding(a));
		}
	}

	public static ExchangeDataProtos.Binding getBinding(
			de.dfki.isreal.data.BindingList inp_bnd) {
		ExchangeDataProtos.Binding.Builder b = ExchangeDataProtos.Binding.newBuilder();
		for (String var : inp_bnd.getVariableList()){
			ExchangeDataProtos.Substitution.Builder s = ExchangeDataProtos.Substitution.newBuilder();
			s.setVariable(var);
			s.setValue(inp_bnd.getInstance(var));
			b.addSubstitutions(s.build());
		}
		return b.build();
	}

	public static de.dfki.isreal.data.BindingList getBindingList(
			ExchangeDataProtos.Binding bnd) {
		de.dfki.isreal.data.BindingList b = new de.dfki.isreal.data.impl.BindingListImpl();
		for (Substitution s : bnd.getSubstitutionsList()){
			b.addPair(s.getVariable(), s.getValue());
		}
		return b;
	}

	public static HashMap<String, List<Statement>> getHashMapOfStatements(
			List<StatementList> statementListsList, List<String> stringsList) {
		HashMap<String, List<Statement>> map = new HashMap<String, List<Statement>>();
		
		for(int i = 0; i < stringsList.size(); i++) {
			map.put(stringsList.get(i), ProtoTransformer.getStatementList(statementListsList.get(i)));
		}
		
		return map;
	}
	
	public static List<Statement> getStatementList(StatementList statementList) {
		return ProtoTransformer.getListOfStatements(statementList.getStatementsList());
	}

	public static StatementList getProtoStatementList(List<Statement> list) {
		StatementList.Builder transformed = StatementList.newBuilder();
		transformed.addAllStatements(ProtoTransformer.getStatementList(list));

		return transformed.build();
	}
}
