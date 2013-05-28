package de.dfki.sdre.test;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.openrdf.model.Value;

import com.hp.hpl.jena.rdf.model.impl.StatementImpl;

import de.dfki.isreal.data.SPARQLDLResult;
import de.dfki.isreal.data.Statement;
import de.dfki.isreal.helpers.Profiler;
import de.dfki.sdre.impl.SDREImpl;
import eu.larkc.core.data.BooleanInformationSet;
import eu.larkc.core.data.CloseableIterator;
import eu.larkc.core.data.SetOfStatements;
import eu.larkc.core.data.VariableBinding;
import eu.larkc.core.data.VariableBinding.Binding;
import eu.larkc.core.query.SPARQLQuery;
import eu.larkc.core.query.SPARQLQueryImpl;

public class Test {
	
	static String ns = "http://www.icmwind.com/icmwindontology.owl" + "#";
	private static SDREImpl sdre = null;
	static String iwoPrfx = "PREFIX iwo: <http://www.icmwind.com/icmwindontology.owl#>";
	static String rdfPrfx = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>";
	static String sparqldlPrfx = "PREFIX sparqldl: <http://pellet.owldl.com/ns/sdle#>";
	
	// time in millis
	static long loadTime = 0;
	static long tbvTime = 0;
	static long feTime = 0;
	static long nosTime = 0;
	static long s2crTime = 0;
	static long dlTime = 0;
	
	public static void main(String[] args) {
		Profiler.init();
		TimeManagement.get().startTime();
		
		sdre = new SDREImpl("C:\\workspace_sdre\\de.dfki.sdre\\res\\gse_config\\sab_2011.isrealomsconfig");
		loadTime = TimeManagement.get().elapsedTime() / 1000000; TimeManagement.get().restart();
		
		
		getWTDetails();
		
//		printTriples();
		
//		checkConstruct();
		
//		checkSPARQLDLQuery();
//		dlTime = TimeManagement.get().elapsedTime() / 1000000; TimeManagement.get().restart();
				
//		checkTripleNos();
//		nosTime = TimeManagement.get().elapsedTime() / 1000000; TimeManagement.get().restart();
		
//		checkFaultsExistence();
//		feTime = TimeManagement.get().elapsedTime() / 1000000; TimeManagement.get().restart();
		
//		checkSensor2ComponentRelation();
//		s2crTime = TimeManagement.get().elapsedTime() / 1000000; TimeManagement.get().restart();
		
//		checkTBVFault();
//		tbvTime = TimeManagement.get().elapsedTime() / 1000000; TimeManagement.get().restart();
		
	 	System.out.println("Load Time - " + loadTime + "; #Triples Time - " + nosTime +
	 			"; Fault Existence Time - " + feTime + "; S2CR Time - " + s2crTime +
	 			"; TBV Time - " + tbvTime + "; DL Time - " + dlTime );
		System.exit(0);
	}
	
	static void checkSPARQLDLQuery() {
		
		System.out.println("CHECLK SPARQL-DL QUERY");
//		String prefix = iwoPrfx + rdfPrfx + sparqldlPrfx + "\n";
				
//		String q =
//				"PREFIX iwo: <http://www.icmwind.com/icmwindontology.owl#>\n" +
//				"SELECT ?x WHERE {\n" +
//					"StrictSubClassOf(?x, iwo:Sensor)" +
//				"}";
				
		String q2 = 
				"PREFIX wtd: <http://www.icmwind.com/icmwindontology.owl#>\n" +
				"SELECT DISTINCT ?s ?qs WHERE {\n" +
						"SubClassOf(?qs, wtd:Sensor), \n" +
						"DirectSubClassOf(?qs, ?s)" +
				"}";
				
//		String q3 = 
//				"PREFIX wtd: <http://www.icmwind.com/icmwindontology.owl#>\n" +
//				"ASK WHERE {\n" +
//						"Property(wtd:hasPosition)" +
//				"}";
				
		SPARQLDLResult result = sdre.sparqldlProcessing(q2);
		VariableBinding binding = result.getBinding();
		
		CloseableIterator<Binding> it = binding.iterator();
		while(it.hasNext()) {
			try {
				Binding b = (Binding) it.next();
				List<Value> vals = b.getValues();
				String s_val = ((Value) vals.get(binding.getVariables().indexOf("s"))).stringValue();
				String qs_val = ((Value) vals.get(binding.getVariables().indexOf("qs"))).stringValue();
				if(!s_val.equals("http://www.w3.org/2002/07/owl#Nothing") && !qs_val.equals("http://www.w3.org/2002/07/owl#Nothing") 
						&& !qs_val.equals("http://www.w3.org/2002/07/owl#Thing") && !s_val.equals("http://www.w3.org/2002/07/owl#Thing")) {
					System.out.println(qs_val + "--" + s_val);
				}
					
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
		
	}
		
	static void checkTBVFault() {
		
		String prefix = iwoPrfx + rdfPrfx;
		String query = prefix + "\n" +
				"ASK WHERE { " +
				"?tbv rdf:type iwo:Thermo_Bypass_Valve. " +
				"?tbv iwo:hasStatus ?st. " +
				"?st rdf:type iwo:Close. " +
				"?st iwo:isValveOpen ?vlv. " +
				"?tbv iwo:hasObservation ?obs. " +
				"?htcd rdf:type iwo:High_Cooler_Temperature_Difference. " +
				"?htcd iwo:hasObservation ?obs. " +
				"}";

		SPARQLQuery askQuery = new SPARQLQueryImpl(query);
		BooleanInformationSet bis = sdre.sparqlAsk(askQuery, null);
		
		if(bis.getValue()) {
			// get faults and information
			
			String query2 = prefix + "\n" +
					"SELECT ?vlv ?tmp " +
					"WHERE { " +
					"?tbv rdf:type iwo:Thermo_Bypass_Valve. " +
					"?tbv iwo:hasStatus ?st. " +
					"?st rdf:type iwo:Close. " +
					"?st iwo:isValveOpen ?vlv. " +
					"?tbv iwo:hasObservation ?obs. " +
					"?htcd rdf:type iwo:High_Cooler_Temperature_Difference. " +
					"?htcd iwo:hasValue ?tmp. " +
					"?htcd iwo:hasObservation ?obs. " +
					"}";
			
			SPARQLQuery slctQuery = new SPARQLQueryImpl(query2);
			VariableBinding binding = sdre.sparqlSelect(slctQuery, null);
				
			CloseableIterator<Binding> it = binding.iterator();
			while(it.hasNext()) {
				try {
					Binding b = (Binding) it.next();
					List<Value> vals = b.getValues();
					String vlv_val = ((Value) vals.get(binding.getVariables().indexOf("vlv"))).stringValue();
					String tmp_val = ((Value) vals.get(binding.getVariables().indexOf("tmp"))).stringValue();
					System.out.println(vlv_val + "\t" + tmp_val);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
						
		} else {
			System.out.println("**----------------Test.checkTBVFault : No fault found.");
		}
	}
	
	static void checkFaultsExistence() {
		String prefix = iwoPrfx + rdfPrfx;
		String query = prefix + "\n" +
				"ASK WHERE { " +
				"?s rdf:type iwo:Fault. " +
				"}";
		
		SPARQLQuery askQuery = new SPARQLQueryImpl(query);
		BooleanInformationSet bis = sdre.sparqlAsk(askQuery, null);
		
		if(bis.getValue()) {
			// get faults and information
			
			String query2 = prefix + "\n" +
					"SELECT DISTINCT ?flt ?comp ?cond " +
					"WHERE { " +
					"?flt rdf:type iwo:Fault. " +
					"?flt iwo:faultyComponent ?comp. " +
					"?flt iwo:faultCondition ?cond. " +
					"}";
			
			SPARQLQuery slctQuery = new SPARQLQueryImpl(query2);
			VariableBinding binding = sdre.sparqlSelect(slctQuery, null);
			
			CloseableIterator<Binding> it = binding.iterator();
			System.out.println("**----------------Test.checkFaultsExistence() : Faults found...");
			while(it.hasNext()) {
				try {
					Binding b = (Binding) it.next();
					List<Value> vals = b.getValues();
					String flt_val = ((Value) vals.get(binding.getVariables().indexOf("flt"))).stringValue();
					String comp_val = ((Value) vals.get(binding.getVariables().indexOf("comp"))).stringValue();
					String cond_val = ((Value) vals.get(binding.getVariables().indexOf("cond"))).stringValue();
					System.out.println(flt_val + "\t" + comp_val + "\t" + cond_val);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		} else {
			System.out.println("**----------------Test.checkFaultsExistence() : No faults found.");
		}
	}
	
	static void getWTDetails() {
		// Get WT, its SubSystems and the corresponding Sensors. 
		
		String prefix = iwoPrfx + rdfPrfx;
		String query = 
				prefix + "\n" +
				"SELECT DISTINCT ?wt ?sub ?sen " +
				"WHERE { " +
				"?wt rdf:type iwo:Wind_Turbine. " +
				"?wt iwo:hasPart ?sub. " +
				"?sub iwo:hasSensor ?sen. " +
				"}";
			
		SPARQLQuery slctQuery = new SPARQLQueryImpl(query);
		VariableBinding binding = sdre.sparqlSelect(slctQuery, null);
			
		CloseableIterator<Binding> it = binding.iterator();
		System.out.println("**----------------Test.checkFaultsExistence() : Faults found...");
		while(it.hasNext()) {
			try {
				Binding b = (Binding) it.next();
				List<Value> vals = b.getValues();
				String wt_val = ((Value) vals.get(binding.getVariables().indexOf("wt"))).stringValue();
				String sub_val = ((Value) vals.get(binding.getVariables().indexOf("sub"))).stringValue();
				String sen_val = ((Value) vals.get(binding.getVariables().indexOf("sen"))).stringValue();
				System.out.println(wt_val + "\t" + sub_val + "\t" + sen_val);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
			
	}
	
	static void getSimilarSensors() {
		
	}
	
	static void checkConstruct() {
		String prefix = iwoPrfx + rdfPrfx;
		
		String query3 = 
				prefix + "\n" +
				"CONSTRUCT { " +
				"?sens rdf:type iwo:Fault. " +
				"} WHERE { " +
				"?sens rdf:type iwo:CS_1000. " +
				"}";
		
		SPARQLQuery q3 = new SPARQLQueryImpl(query3);
		SetOfStatements sos = sdre.sparqlConstruct(q3, null);
		
		List<Statement> list = new ArrayList<Statement>();
		CloseableIterator<org.openrdf.model.Statement> it3 = sos.getStatements();
		System.out.println("**----------------Test.checkConstruct() : Construct Query Execution...");
		while(it3.hasNext()) {
			org.openrdf.model.Statement st = it3.next();
			Statement s = new de.dfki.isreal.data.impl.StatementImpl(st.getSubject().toString(), st.getPredicate().toString(), st.getObject().toString());
			list.add(s);
		}
		
		
		System.out.println("**----------------Test.checkConstruct() : Construct Query Inserting...");
		sdre.insert(list);
		
		String query2 = 
				prefix + "\n" +
				"SELECT DISTINCT ?sen " +
				"WHERE { " +
				"?sen rdf:type iwo:Fault. " +
				"?sen rdf:type iwo:CS_1000. " +
				"}";
		
		SPARQLQuery slctQuery = new SPARQLQueryImpl(query2);
		VariableBinding binding = sdre.sparqlSelect(slctQuery, null);
		
		CloseableIterator<Binding> it = binding.iterator();
		System.out.println("**----------------Test.checkConstruct() : Construct Query Check...");
		while(it.hasNext()) {
			try {
				Binding b = (Binding) it.next();
				List<Value> vals = b.getValues();
				String sen_val = ((Value) vals.get(binding.getVariables().indexOf("sen"))).stringValue();
				System.out.println(sen_val);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		
	}
	
	static void checkTripleNos() {
		List<Statement> triples = sdre.listStatements();
		System.out.println("**----------------Test.checkTripleNos() : Number of triples - " + triples.size());
	}
		
	static void checkSensor2ComponentRelation() {
		
		// check the realtion between sensor CS and Gearbox
		//(http://www.icmwind.com/icmwindontology.owl#gb_marpI, http://www.icmwind.com/icmwindontology.owl#cs_marpI)
		
		List<String> entities = new ArrayList<String>();
		entities.add("http://www.icmwind.com/icmwindontology.owl#cs_marpI");
//		entities.add("http://www.icmwind.com/icmwindontology.owl#Connection");
		entities.add("http://www.icmwind.com/icmwindontology.owl#hlb_marpI");
		
		List<List<Statement>> answer = sdre.computeTopRelationalTrees(entities, 3, true);
		System.out.println("**----------------Test.checkSensor2ComponentRelation() : Paths found...");
		for(List<Statement> paths : answer ) {
			System.out.println("PATH: ");
			for(Statement s : paths) {
				System.out.println(s.getSubjectString() + "--" + s.getPredicateString() + "--" + s.getObjectString());
			}
		}
	}
	
	static void printTriples() {
		String query = "" +
				"SELECT ?s ?p ?o " +
				"WHERE { " +
				"?s ?p ?o. " +
				"}";
		
		SPARQLQuery selecttQuery = new SPARQLQueryImpl(query);
		VariableBinding binding = sdre.sparqlSelect(selecttQuery, null);
		
		CloseableIterator<Binding> it = binding.iterator();
		System.out.println("**----------------Test.printTriples() : Triples found...");
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("triples.txt");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		while(it.hasNext()) {
			try {
				Binding b = (Binding) it.next();
				List<Value> vals = b.getValues();
				String s_val = ((Value) vals.get(binding.getVariables().indexOf("s"))).stringValue();
				String p_val = ((Value) vals.get(binding.getVariables().indexOf("p"))).stringValue();
				String o_val = ((Value) vals.get(binding.getVariables().indexOf("o"))).stringValue();
				
//				System.out.println(s_val + "--" + p_val + "--" + o_val);
				writer.println(s_val + "--" + p_val + "--" + o_val);
				
				// Fill STAR graph with triples
//				if(s_val.startsWith("_:node") || o_val.startsWith("_:node") || s_val.contains("http://www.w3.org/2002/07/owl#") || o_val.contains("http://www.w3.org/2002/07/owl#")) {
				if(s_val.contains("http://www.w3.org/2002/07/owl#") || o_val.contains("http://www.w3.org/2002/07/owl#")) {
					System.out.println("subject - " + s_val + " object - " + o_val);
				} else {
//					
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		writer.close();
		
	}

	static class TimeManagement {
		
		private long startTime = 0;
		private long elapsedTime = 0;
		
		static TimeManagement tm = new TimeManagement();
		
		public TimeManagement() {
		}
		public static TimeManagement get() {
			return tm;
		}
		
		private void startTime() {
			startTime = System.nanoTime();
			System.out.println("**------------------TimeManagement.startTime() : Timer has start. ");
		}
		
		public long elapsedTime() {
			elapsedTime = System.nanoTime() - startTime;
			System.out.println("**------------------TimeManagement.elapsedTime() : " + elapsedTime / 1000000 + " millis");
			return elapsedTime;
		}
		
		public void restart() {
			startTime = 0; elapsedTime = 0;
			System.out.println("**------------------TimeManagement.restartTime() : Timer has been reset. ");
			startTime();
		}
		
	}
}
