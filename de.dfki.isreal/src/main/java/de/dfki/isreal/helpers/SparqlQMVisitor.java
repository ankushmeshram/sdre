package de.dfki.isreal.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.openrdf.query.algebra.Add;
import org.openrdf.query.algebra.And;
import org.openrdf.query.algebra.ArbitraryLengthPath;
import org.openrdf.query.algebra.Avg;
import org.openrdf.query.algebra.BNodeGenerator;
import org.openrdf.query.algebra.BindingSetAssignment;
import org.openrdf.query.algebra.Bound;
import org.openrdf.query.algebra.Clear;
import org.openrdf.query.algebra.Coalesce;
import org.openrdf.query.algebra.Compare;
import org.openrdf.query.algebra.CompareAll;
import org.openrdf.query.algebra.CompareAny;
import org.openrdf.query.algebra.Copy;
import org.openrdf.query.algebra.Count;
import org.openrdf.query.algebra.Create;
import org.openrdf.query.algebra.Datatype;
import org.openrdf.query.algebra.DeleteData;
import org.openrdf.query.algebra.Difference;
import org.openrdf.query.algebra.Distinct;
import org.openrdf.query.algebra.EmptySet;
import org.openrdf.query.algebra.Exists;
import org.openrdf.query.algebra.Extension;
import org.openrdf.query.algebra.ExtensionElem;
import org.openrdf.query.algebra.Filter;
import org.openrdf.query.algebra.FunctionCall;
import org.openrdf.query.algebra.Group;
import org.openrdf.query.algebra.GroupConcat;
import org.openrdf.query.algebra.GroupElem;
import org.openrdf.query.algebra.IRIFunction;
import org.openrdf.query.algebra.If;
import org.openrdf.query.algebra.In;
import org.openrdf.query.algebra.InsertData;
import org.openrdf.query.algebra.Intersection;
import org.openrdf.query.algebra.IsBNode;
import org.openrdf.query.algebra.IsLiteral;
import org.openrdf.query.algebra.IsNumeric;
import org.openrdf.query.algebra.IsResource;
import org.openrdf.query.algebra.IsURI;
import org.openrdf.query.algebra.Join;
import org.openrdf.query.algebra.Label;
import org.openrdf.query.algebra.Lang;
import org.openrdf.query.algebra.LangMatches;
import org.openrdf.query.algebra.LeftJoin;
import org.openrdf.query.algebra.Like;
import org.openrdf.query.algebra.Load;
import org.openrdf.query.algebra.LocalName;
import org.openrdf.query.algebra.MathExpr;
import org.openrdf.query.algebra.Max;
import org.openrdf.query.algebra.Min;
import org.openrdf.query.algebra.Modify;
import org.openrdf.query.algebra.Move;
import org.openrdf.query.algebra.MultiProjection;
import org.openrdf.query.algebra.Namespace;
import org.openrdf.query.algebra.Not;
import org.openrdf.query.algebra.Or;
import org.openrdf.query.algebra.Order;
import org.openrdf.query.algebra.OrderElem;
import org.openrdf.query.algebra.Projection;
import org.openrdf.query.algebra.ProjectionElem;
import org.openrdf.query.algebra.ProjectionElemList;
import org.openrdf.query.algebra.QueryModelNode;
import org.openrdf.query.algebra.QueryModelVisitor;
import org.openrdf.query.algebra.QueryRoot;
import org.openrdf.query.algebra.Reduced;
import org.openrdf.query.algebra.Regex;
import org.openrdf.query.algebra.SameTerm;
import org.openrdf.query.algebra.Sample;
import org.openrdf.query.algebra.Service;
import org.openrdf.query.algebra.SingletonSet;
import org.openrdf.query.algebra.Slice;
import org.openrdf.query.algebra.StatementPattern;
import org.openrdf.query.algebra.Str;
import org.openrdf.query.algebra.Sum;
import org.openrdf.query.algebra.Union;
import org.openrdf.query.algebra.ValueConstant;
import org.openrdf.query.algebra.Var;
import org.openrdf.query.algebra.ZeroLengthPath;
import org.openrdf.query.algebra.evaluation.function.rdfterm.StrDt;
import org.openrdf.query.algebra.evaluation.function.rdfterm.StrLang;

import de.dfki.isreal.data.Statement;
import de.dfki.isreal.data.impl.StatementImpl;


/**
 * This class is part of the Query Execution Engine of sesame.
 * It is used to extract variables and triple patterns in ISReal datatatypes
 * from the query.
 * 
 * @author stenes
 *
 */
public class SparqlQMVisitor implements QueryModelVisitor {
	private static Logger logger = Logger.getLogger(SparqlQMVisitor.class);
	
	private List<String> vars = new ArrayList<String>();
	private List<Statement> sts = new ArrayList<Statement>();
	private List<Statement> cons = new ArrayList<Statement>();
	private boolean verbose = false;
	
	private List<String[]> projs = new ArrayList<String[]>();
	
	private HashMap<String, String> proj = new HashMap<String, String>();
	private HashMap<String, String> constants = new HashMap<String, String>();

	
	public List<String> getVariables(){
		return vars;
	}
	
	public List<Statement> getStatementPatterns(){
		return sts;
	}
	
	public List<Statement> getConstructPatterns(){
		return cons;
	}
	
	@Override
	public void meet(QueryRoot arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		arg0.visitChildren(this);
	}

	@Override
	public void meet(And arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		arg0.visitChildren(this);
	}

	@Override
	public void meet(BNodeGenerator arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		arg0.visitChildren(this);
	}

	@Override
	public void meet(Bound arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		arg0.visitChildren(this);
	}

	@Override
	public void meet(Compare arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		arg0.visitChildren(this);
	}

	@Override
	public void meet(CompareAll arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		arg0.visitChildren(this);
	}

	@Override
	public void meet(CompareAny arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		arg0.visitChildren(this);
	}

	@Override
	public void meet(Count arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		arg0.visitChildren(this);
	}

	@Override
	public void meet(Datatype arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		arg0.visitChildren(this);
	}

	@Override
	public void meet(Difference arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		arg0.visitChildren(this);
	}

	@Override
	public void meet(Distinct arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		arg0.visitChildren(this);
	}

	@Override
	public void meet(EmptySet arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		arg0.visitChildren(this);
	}

	@Override
	public void meet(Exists arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		arg0.visitChildren(this);
	}

	@Override
	public void meet(Extension arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		arg0.visitChildren(this);
		
		
		for(String[] triple : projs){
			String subj = getTriplePart(triple[0]);
			String pred = getTriplePart(triple[1]);
			String obj = getTriplePart(triple[2]);
			Statement c = new StatementImpl(subj, pred, obj);
			cons.add(c);
		}
		
	}


	private String getTriplePart(String string) {
		if (string.startsWith("-const")){
			return constants.get(string);
		} else{
			return "?" + string; 
		}
	}

	@Override
	public void meet(ExtensionElem arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		ValueConstant v = (ValueConstant) arg0.getExpr();
		constants.put(arg0.getName(), v.getValue().stringValue());
		arg0.visitChildren(this);
	}

	@Override
	public void meet(FunctionCall arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		arg0.visitChildren(this);
	}

	@Override
	public void meet(Group arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		arg0.visitChildren(this);
	}

	@Override
	public void meet(GroupElem arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		arg0.visitChildren(this);
	}

	@Override
	public void meet(In arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		arg0.visitChildren(this);
	}

	@Override
	public void meet(Intersection arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		arg0.visitChildren(this);
	}

	@Override
	public void meet(IsBNode arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		arg0.visitChildren(this);
	}

	@Override
	public void meet(IsLiteral arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		arg0.visitChildren(this);
	}

	@Override
	public void meet(IsResource arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		arg0.visitChildren(this);
	}

	@Override
	public void meet(IsURI arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		arg0.visitChildren(this);
	}

	@Override
	public void meet(Join arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		arg0.visitChildren(this);
		
	}

	@Override
	public void meet(Label arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		arg0.visitChildren(this);
	}

	@Override
	public void meet(Lang arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		arg0.visitChildren(this);
	}

	@Override
	public void meet(LangMatches arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		arg0.visitChildren(this);
	}

	@Override
	public void meet(Like arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		arg0.visitChildren(this);
	}

	@Override
	public void meet(LocalName arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		arg0.visitChildren(this);
	}

	@Override
	public void meet(MathExpr arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		arg0.visitChildren(this);
	}

	@Override
	public void meet(Max arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		arg0.visitChildren(this);
	}

	@Override
	public void meet(Min arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		arg0.visitChildren(this);
	}

	@Override
	public void meet(MultiProjection arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		arg0.visitChildren(this);
	}

	@Override
	public void meet(Namespace arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		arg0.visitChildren(this);
	}

	@Override
	public void meet(Not arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		arg0.visitChildren(this);
	}

	@Override
	public void meet(LeftJoin arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		arg0.visitChildren(this);
	}

	@Override
	public void meet(Or arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		arg0.visitChildren(this);
	}

	@Override
	public void meet(Order arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		arg0.visitChildren(this);
	}

	@Override
	public void meet(OrderElem arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		arg0.visitChildren(this);
	}

	@Override
	public void meet(Projection arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		if (vars.size() == 0){
			for (String var : arg0.getBindingNames()){
				vars.add("?" + var);
			}
		}
		arg0.visitChildren(this);
	}

	@Override
	public void meet(ProjectionElemList arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		
		String[] triple = new String[arg0.getElements().size()];
		int i = 0;
		for (ProjectionElem pe : arg0.getElements()){
			triple[i] = pe.getSourceName();
			i++;
		}
		projs.add(triple);
		arg0.visitChildren(this);
	}

	@Override
	public void meet(ProjectionElem arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		proj.put(arg0.getSourceName(), arg0.getTargetName());
		arg0.visitChildren(this);
	}

	@Override
	public void meet(Reduced arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		arg0.visitChildren(this);
	}

	@Override
	public void meet(Regex arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		arg0.visitChildren(this);
	}

	@Override
	public void meet(Slice arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		arg0.visitChildren(this);
	}

	@Override
	public void meet(SameTerm arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		arg0.visitChildren(this);
	}

	@Override
	public void meet(Filter arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		arg0.visitChildren(this);
	}

	@Override
	public void meet(SingletonSet arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		arg0.visitChildren(this);
	}

	@Override
	public void meet(StatementPattern arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		String subj = getValueString(arg0.getSubjectVar());
		String pred = getValueString(arg0.getPredicateVar());
		String obj = getValueString(arg0.getObjectVar());
		Statement s = new StatementImpl(subj, pred, obj);
		sts.add(s);
	}

	private String getValueString(Var subjectVar) {
		if (subjectVar.getName().startsWith("-const")){
			return subjectVar.getValue().stringValue();
		} else if (subjectVar.getName().startsWith("-anon")){
			String name = subjectVar.getName();
			String num = name.substring(name.lastIndexOf("-")+1);
			int i = Integer.parseInt(num);
			return "_:b" + i;
		}
		return "?" + subjectVar.getName();
	}

	@Override
	public void meet(Str arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		arg0.visitChildren(this);
	}

	@Override
	public void meet(Union arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		arg0.visitChildren(this);
	}

	@Override
	public void meet(ValueConstant arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		arg0.visitChildren(this);
	}

	@Override
	public void meet(Var arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		arg0.visitChildren(this);
	}

	@Override
	public void meetOther(QueryModelNode arg0) throws Exception {
		if (verbose){
			logger.info(arg0.getSignature());
		}
		arg0.visitChildren(this);
	}

	@Override
	public void meet(ZeroLengthPath arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void meet(ArbitraryLengthPath arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void meet(Avg arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void meet(Coalesce arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void meet(GroupConcat arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void meet(If arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void meet(IsNumeric arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void meet(Sample arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void meet(Sum arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void meet(IRIFunction arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void meet(Add arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void meet(BindingSetAssignment arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void meet(Clear arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void meet(Copy arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void meet(Create arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void meet(DeleteData arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void meet(InsertData arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void meet(Load arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void meet(Modify arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void meet(Move arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void meet(Service arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	

}
