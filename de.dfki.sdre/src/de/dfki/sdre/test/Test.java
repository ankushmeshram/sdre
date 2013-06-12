package de.dfki.sdre.test;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openrdf.model.Value;


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
	static String rdfsPrfx = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>";
	static String sparqldlPrfx = "PREFIX sparqldl: <http://pellet.owldl.com/ns/sdle#>";
	static String xsdPrfx = "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>";
	static String fnPrfx = "PREFIX fn: <http://www.w3.org/2005/xpath-functions#>";
		
	// time in millis
	static long loadTime = 0;
	static long tbvTime = 0;
	static long feTime = 0;
	static long nosTime = 0;
	static long s2crTime = 0;
	static long dlTime = 0;
	static long fcmTime = 0;
	static long diagTime = 0;
	
	static PrintWriter logger = null;

	public static void main(String[] args) {
		Profiler.init();
		TimeManagement.get().startTime();
		
		logger = getLogWriter();
		sdre = new SDREImpl("C:\\Users\\anme05\\git\\sdre\\de.dfki.sdre\\res\\gse_config\\sab_2011_1.isrealomsconfig");
		loadTime = TimeManagement.get().elapsedTime() / 1000000; TimeManagement.get().restart();
		runQueries();
	 	
	 	sdre.closeTS();
	 	
	 	System.gc(); System.gc(); System.gc(); System.gc();
	 	logger = getLogWriter();
	 	sdre = new SDREImpl("C:\\Users\\anme05\\git\\sdre\\de.dfki.sdre\\res\\gse_config\\sab_2011_2.isrealomsconfig");
	 	loadTime = TimeManagement.get().elapsedTime() / 1000000; TimeManagement.get().restart();
	 	runQueries();
	 	
	 	sdre.closeTS();
	 	
	 	System.gc(); System.gc(); System.gc(); System.gc();
	 	logger = getLogWriter();
	 	sdre = new SDREImpl("C:\\Users\\anme05\\git\\sdre\\de.dfki.sdre\\res\\gse_config\\sab_2011_3.isrealomsconfig");
	 	loadTime = TimeManagement.get().elapsedTime() / 1000000; TimeManagement.get().restart();
	 	runQueries();
	 	
	 	sdre.closeTS();
	 	
	 	System.gc(); System.gc(); System.gc(); System.gc();
	 	logger = getLogWriter();
	 	sdre = new SDREImpl("C:\\Users\\anme05\\git\\sdre\\de.dfki.sdre\\res\\gse_config\\sab_2011_4.isrealomsconfig");
	 	loadTime = TimeManagement.get().elapsedTime() / 1000000; TimeManagement.get().restart();
	 	runQueries();
	 	
		System.exit(0);
	}
	
	static void runQueries() {
		
		long startTime = 0;
		long elapsedTime = 0;
		
		startTime = System.currentTimeMillis();
		checkTBVFault(true);
		elapsedTime = System.currentTimeMillis() - startTime;
		tbvTime = elapsedTime;
		
		startTime = System.currentTimeMillis();
		getFCMConfig();
		elapsedTime = System.currentTimeMillis() - startTime;
		fcmTime = elapsedTime;
		
		startTime = System.currentTimeMillis();
		doSensorSensorDiagnosis();
		elapsedTime = System.currentTimeMillis() - startTime;
		diagTime = elapsedTime;
		
	 	System.out.println("Load Time - " + loadTime + "; TBV Time - " + tbvTime + "; FCM Config Time - " + fcmTime + "; Diagnosis Time - " + diagTime);
	 	
	 	logger.println("Load Time - " + loadTime + "; TBV Time - " + tbvTime + "; FCM Config Time - " + fcmTime + "; Diagnosis Time - " + diagTime);
	 	logger.close();
	}

	static public PrintWriter getLogWriter() {
		Date date= new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		
		String timeStamp = sdf.format(date).substring(0, 10).replaceAll("\\.", "-") 
							+ "_" + sdf.format(date).substring(11).replaceAll(":", "-");
		
		try {
			logger = new PrintWriter("log-" + timeStamp + ".txt");
			logger.println("Created At " + date.toString());
			logger.flush();
			
			return logger;
		} catch (FileNotFoundException e) {
			System.out.println("\n**ERROR GI.getLogWriter() : Error in creating Log file.");
			e.printStackTrace();
			return null;
		}
		
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
		
	static boolean askTBVFault() {
		
		logger.println();
		logger.println("****Check for TBV Fault*****");
		logger.println();
		logger.println("Initialising ASK Query...");
				
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
		
		System.out.println("Processing ASK Query...");
		logger.println("------ASK Query------");
		logger.println(query);
		logger.println("---------------------");
		
		SPARQLQuery askQuery = new SPARQLQueryImpl(query);
		BooleanInformationSet bis = sdre.sparqlAsk(askQuery, null);
		
		long askQueryEvalTime = TimeManagement.get().elapsedTime() / 1000000; TimeManagement.get().restart();

		System.out.println("ASK Query Processing time : " + askQueryEvalTime + "ms");
		logger.println("ASK Query Processing time : " + askQueryEvalTime + "ms");
		logger.flush();
		
		System.out.println("Processing ASK Query Results...");
				
		System.out.println("ASK Query Result - " + bis.getValue());
		logger.println("ASK Query Result - " + bis.getValue());
		logger.println("*****************");
		
		return bis.getValue();
	}
	
	static void checkTBVFault(boolean save) {
		
		int faultsFound = 0;
		int faultTriplesInserted = 0;
		
		String prefix = iwoPrfx + rdfPrfx + xsdPrfx;
		
		if(askTBVFault()) {
			/*
			
			// get faults and information
			logger.println();
			logger.println("*****FAULT DETECTION***************");
			logger.println();
			logger.println("Initialising SELECT Query for TBV Fault information...");
			System.out.println("Initialising SELECT Query for TBV Fault information...");
			
			String query2 = prefix + "\n" +
					"SELECT ?tbv ?st ?obs ?htcd ?tmp " +
					"WHERE { " +
						"?tbv rdf:type iwo:Thermo_Bypass_Valve. " +
						"?tbv iwo:hasStatus ?st. " +
						"?st rdf:type iwo:Close. " +
						"?tbv iwo:hasObservation ?obs. " +
						"?st  iwo:hasObservation ?obs. " +
						"?htcd rdf:type iwo:High_Cooler_Temperature_Difference. " +
						"?htcd iwo:hasValue ?tmp. " +
						"?htcd iwo:hasObservation ?obs. " +
					"}";
			
			System.out.println("Processing SELECT Query...");
			logger.println("----TBV Fault Info SELECT Query----");
			logger.println(query2);
			logger.println("-----------------------------------");
			
			
			SPARQLQuery slctQuery = new SPARQLQueryImpl(query2);
			VariableBinding binding = sdre.sparqlSelect(slctQuery, null);
			
			long selectQueryEvalTime = TimeManagement.get().elapsedTime() / 1000000; TimeManagement.get().restart();
			
			System.out.println("SELECT Query Processing time : " + selectQueryEvalTime + "ms");
			logger.println("SELECT Query TBV Fault Info Processing time : " + selectQueryEvalTime + "ms");
			logger.flush();
			
			System.out.println("Processing SELECT Query Results...");
			logger.println("######## SELECT Query TBV Fault Info Results ########");
			logger.println("Observation" + ": " + "TBV" + "-" + "Status" + "\t" + "HTCD" + "-" + "Temperature");
			
			CloseableIterator<Binding> it = binding.iterator();
			while(it.hasNext()) {
				try {
					Binding b = (Binding) it.next();
					List<Value> vals = b.getValues();
					String tbv_val = ((Value) vals.get(binding.getVariables().indexOf("tbv"))).stringValue();
					String st_val = ((Value) vals.get(binding.getVariables().indexOf("st"))).stringValue();
					String obs_val = ((Value) vals.get(binding.getVariables().indexOf("obs"))).stringValue();
					String htcd_val = ((Value) vals.get(binding.getVariables().indexOf("htcd"))).stringValue();
					String tmp_val = ((Value) vals.get(binding.getVariables().indexOf("tmp"))).stringValue();

					System.out.println(tbv_val.substring(43) + "\t" + st_val.substring(43) + "\t" + htcd_val.substring(43) + "\t" + tmp_val + "\t" + obs_val.substring(43) );
					logger.println(
							obs_val.substring(43) + 
								": " + tbv_val.substring(43) + "-" + st_val.substring(43) + 
								"\t" + htcd_val.substring(43) + "-" + tmp_val );
					
					faultsFound++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			logger.println("#####################################################");
			logger.println();
						
			long slctQueryResultProcessTime = TimeManagement.get().elapsedTime() / 1000000; TimeManagement.get().restart();
			System.out.println("Faults Found: " + faultsFound + "; SELECT Query TBV Fault Info Result Processing time : " + slctQueryResultProcessTime + "ms");
			logger.println("Faults Found: " + faultsFound + "; SELECT Query TBV Fault Info Result Processing time : " + slctQueryResultProcessTime + "ms");
			logger.flush();
			
			*/
			if(save) {
				
				logger.println();
				logger.println("**** Inserting Faults *****");
				logger.println();
				logger.println("Initialising CONSTRUCT Query for Insert TBV Faults...");
				
				String query3 = prefix + "\n" +
						"CONSTRUCT { " +
							"?cflt rdf:type iwo:Component_Fault. " +
							"?cflt iwo:faultyComponent ?tbv. " +
							"?cflt iwo:faultCondition ?sts. " +
							"?cflt iwo:faultCondition ?htcd. " +
							"?cflt iwo:faultOccurrenceTime ?time. " +
							"?cflt iwo:faultLevel ?alm. " +
							"?alm rdf:type iwo:Alarm. " +
							"?alm iwo:faultMessage \"Check the component\"^^xsd:string. " +
						"} WHERE { " +
							"?tbv rdf:type iwo:Thermo_Bypass_Valve. " +
							"?tbv iwo:hasStatus ?sts. " +
							"?sts rdf:type iwo:Close. " +
							"?tbv iwo:hasObservation ?obs. " +
							"?sts  iwo:hasObservation ?obs. " +
							"?htcd rdf:type iwo:High_Cooler_Temperature_Difference. " +
							"?htcd iwo:hasObservation ?obs. " +
							"?htcd iwo:hasValue ?tmp. " +
							"BIND(STRAFTER(str(?obs), \"#\") AS ?o). " +
							"BIND(STRAFTER(str(?tbv), \"#\") AS ?t). " +
							"BIND(IRI(CONCAT(\"http://www.icmwind.com/icmwindontology.owl#CF_\",  CONCAT(CONCAT(?t, \"_\"), ?o))) AS ?cflt). " +
							"BIND(IRI(CONCAT(\"http://www.icmwind.com/icmwindontology.owl#ALM_\", CONCAT(CONCAT(?t, \"_\"), ?o))) AS ?alm). " +
							"?obs iwo:hasSamplingTime ?time " +
						"}";
				
				System.out.println("Processing CONSTRUCT Query...");
				logger.println("---- TBV Fault Insert CONSTRUCT Query ----");
				logger.println(query3);
				logger.println("------------------------------------------");
				
				SPARQLQuery q3 = new SPARQLQueryImpl(query3);
				SetOfStatements sos = sdre.sparqlConstruct(q3, null);
				
				long constructQueryEvalTime = TimeManagement.get().elapsedTime() / 1000000; TimeManagement.get().restart();
				System.out.println("CONSTRUCT Query Processing time : " + constructQueryEvalTime + "ms");
				logger.println("CONSTRUCT Query TBV Fault Insert Processing time : " + constructQueryEvalTime + "ms");
				logger.flush();
								
				List<Statement> list = new ArrayList<Statement>();
				CloseableIterator<org.openrdf.model.Statement> it3 = sos.getStatements();
				System.out.println("**----------------Test.checkConstruct() : Construct Query Execution...");
				while(it3.hasNext()) {
					org.openrdf.model.Statement st = it3.next();
					Statement s = new de.dfki.isreal.data.impl.StatementImpl(st.getSubject().toString(), st.getPredicate().toString(), st.getObject().toString());
					list.add(s);
					System.out.println(s.stringValue());
//					logger.println(st.getSubject().toString().substring(43) + "--" + st.getPredicate().toString().substring(43) + "--" + st.getObject().toString().substring(43));
//					System.out.println(st.getSubject().toString().substring(43) + "--" + st.getPredicate().toString().substring(43) + "--" + st.getObject().toString().substring(43));
					
					faultTriplesInserted++;
				}

				System.out.println("**----------------Test.checkConstruct() : Construct Query Inserting...");
				System.out.println("Inserting CONSTRUCT Query Results...");
//				logger.println("Inserting CONSTRUCT Query Results...");
				
				sdre.insert(list);
				
				long constructQueryInsertTime = TimeManagement.get().elapsedTime() / 1000000; TimeManagement.get().restart();
				System.out.println("Fault Triples Inserted: " + faultTriplesInserted + "; CONSTRUCT Query Insert time : " + constructQueryInsertTime + "ms");
				logger.println("Fault Triples Inserted: " + faultTriplesInserted + "; CONSTRUCT Query TBV Fault Insert time : " + constructQueryInsertTime + "ms");
				logger.println("*********************");
				logger.flush();
				
//				logger.println();
//				logger.println("********************");
//				logger.println("Initialising SELECT Query...");
//								
//				String query4 = prefix + "\n" +
//						"SELECT ?flt ?time ?cond ?lvl ?msg " +
//						"WHERE { " +
//							"?flt rdf:type iwo:Component_Fault. " +
//							"?flt iwo:faultyComponent iwo:Thermo_Bypass_Valve. " +
//							"?flt iwo:faultCondition ?cond. " +
//							"?flt iwo:faultOccurrenceTime ?time. " +
//							"?flt iwo:faultLevel ?lvl. " +
//							"?lvl iwo:faultMessage ?msg" +
//						"}";
//				
//				System.out.println("Processing SELECT Query...");
//				logger.println("Processing SELECT Query...");
//								
//				SPARQLQuery slctQuery2 = new SPARQLQueryImpl(query4);
//				VariableBinding binding2 = sdre.sparqlSelect(slctQuery2, null);
//				
//				long selectQuery2EvalTime = TimeManagement.get().elapsedTime() / 1000000; TimeManagement.get().restart();
//				System.out.println("Faults Inserted: " + faultTriplesInserted + "; SELECT Query Processing time : " + selectQuery2EvalTime + "ms");
//				logger.println("Faults Inserted: " + faultTriplesInserted + "; SELECT Query Processing time : " + selectQuery2EvalTime + "ms");
//				logger.flush();
//				
//				System.out.println("Processing SELECT Query Results...");
//				logger.println("Processing SELECT Query Results...");
//				logger.println("?flt" + "\t" + "time" + "\t" + "cond" + "\t" + "lvl" + "\t" + "msg");
//				
//				CloseableIterator<Binding> it2 = binding2.iterator();
//				System.out.println("**----------------Test.checkConstruct() : Construct Query Check...");
//				while(it2.hasNext()) {
//					try {
//						Binding b = (Binding) it2.next();
//						List<Value> vals = b.getValues();
//						String flt_val = ((Value) vals.get(binding2.getVariables().indexOf("flt"))).stringValue();
//						String time_val = ((Value) vals.get(binding2.getVariables().indexOf("time"))).stringValue();
//						String cond_val = ((Value) vals.get(binding2.getVariables().indexOf("cond"))).stringValue();
//						String lvl_val = ((Value) vals.get(binding2.getVariables().indexOf("lvl"))).stringValue();
//						String msg_val = ((Value) vals.get(binding2.getVariables().indexOf("msg"))).stringValue();
//						
//						System.out.println(flt_val + "\t" + time_val + "\t" + cond_val + "\t" + lvl_val + "\t" + msg_val);
//						logger.println(flt_val + "\t" + time_val + "\t" + cond_val + "\t" + lvl_val + "\t" + msg_val);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//				
//				long slctQuery2ResultProcessTime = TimeManagement.get().elapsedTime() / 1000000; TimeManagement.get().restart();
//				System.out.println("SELECT Query Result Processing time : " + slctQuery2ResultProcessTime + "ms");
//				logger.println("SELECT Query Result Processing time : " + slctQuery2ResultProcessTime + "ms");
//				logger.flush();
			}
						
		} else {
			System.out.println("**----------------Test.checkTBVFault : No fault found.");
			logger.println("**----------------Test.checkTBVFault : No fault found.");
		}
	}
	
	static boolean askSensorComponentFaultOccurrence() {
		String prefix = iwoPrfx + rdfPrfx;
		String query = prefix + "\n" +
				"ASK WHERE { " +
					"?sflt rdf:type iwo:Sensor_Fault. " +
					"?cflt rdf:type iwo:Component_Fault. " +
					"?sflt iwo:faultOccurrenceTime ?time. " +
					"?cflt iwo:faultOccurrenceTime ?time. " +
				"}";
		
		SPARQLQuery askQuery = new SPARQLQueryImpl(query);
		BooleanInformationSet bis = sdre.sparqlAsk(askQuery, null);
				
		long askQueryEvalTime = TimeManagement.get().elapsedTime() / 1000000; TimeManagement.get().restart();
		System.out.println("ASK Query Processing time : " + askQueryEvalTime + "ms");
		System.out.println("ASK Query Result - " + bis.getValue());
		
		return bis.getValue();
	}
	
	static boolean askSameSensorFaultsOccurrence() {
		
		logger.println();
		logger.println("****Check for Faults for Same Sensor*****");
		logger.println();
		logger.println("Initialising ASK Query...");
		
		String prefix = iwoPrfx + rdfPrfx;
		String query = prefix + "\n" +
				"ASK WHERE { " +
					"?sflt rdf:type iwo:Sensor_Fault. " +
					"?sflt2 rdf:type iwo:Sensor_Fault. " +
					"?sflt iwo:faultySensor ?sen. " +
					"?sflt2 iwo:faultySensor ?sen. " +
					"?sflt iwo:faultCondition ?cond. " +
					"?sflt2 iwo:faultCondition ?cond2. " +
					"?sflt iwo:faultOccurrenceTime ?time. " +
					"?sflt2 iwo:faultOccurrenceTime ?time. " +
					"FILTER(?cond != ?cond2)" +
				"}";
		
		logger.println("------ASK Query------");
		logger.println(query);
		logger.println("---------------------");
		
		SPARQLQuery askQuery = new SPARQLQueryImpl(query);
		BooleanInformationSet bis = sdre.sparqlAsk(askQuery, null);
				
		long askQueryEvalTime = TimeManagement.get().elapsedTime() / 1000000; TimeManagement.get().restart();
		System.out.println("ASK Query Processing time : " + askQueryEvalTime + "ms");
		logger.println("ASK Query Processing time : " + askQueryEvalTime + "ms");
		logger.flush();
		
		System.out.println("ASK Query Result - " + bis.getValue());
		logger.println("ASK Query Result - " + bis.getValue());
		logger.println("*****************");
		
		return bis.getValue();
	}
	
	static void starSensorComponentFaultRelation() {
		String prefix = iwoPrfx + rdfPrfx;
		String query = prefix + "\n" +
				"SELECT DISTINCT ?sen ?comp " +
				"WHERE { " +
					"?sflt rdf:type iwo:Sensor_Fault. " +
					"?cflt rdf:type iwo:Component_Fault. " +
					"?sflt iwo:faultOccurrenceTime ?time. " +
					"?cflt iwo:faultOccurrenceTime ?time. " +
					"?sflt iwo:faultySensor ?sen. " +
					"?cflt iwo:faultyComponent ?comp. " +					
				"}";
		
		SPARQLQuery slctQuery = new SPARQLQueryImpl(query);
		VariableBinding binding = sdre.sparqlSelect(slctQuery, null);
		
		CloseableIterator<Binding> it = binding.iterator();
		System.out.println("**----------------Test.checkFaultsExistence() : Faults found...");
		while(it.hasNext()) {
			try {
				Binding b = (Binding) it.next();
				List<Value> vals = b.getValues();
				String comp_val = ((Value) vals.get(binding.getVariables().indexOf("comp"))).stringValue();
				String sen_val = ((Value) vals.get(binding.getVariables().indexOf("sen"))).stringValue();
				System.out.println(comp_val + "\t" + sen_val);
				
				String senType = getType(sen_val);
				String compType = getType(comp_val);
				
				System.out.println(compType);
				
				String position = getSensorPosition(senType);
				
				List<String> entities = new ArrayList<String>();
				entities.add(comp_val.substring(43));
				entities.add(compType);
				entities.add(position);
				entities.add(sen_val.substring(43));
				
				List<List<Statement>> answer = sdre.computeTopRelationalTrees(entities, 3, true);
				System.out.println("**----------------Test.checkSensor2ComponentRelation() : Paths found...");
				for(List<Statement> paths : answer ) {
					System.out.println("PATH: ");
					for(Statement s : paths) {
						System.out.println(s.getSubjectString() + "--" + s.getPredicateString() + "--" + s.getObjectString());
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	static String getType(String sensorInstance) {
		
		String type = null;
		
		String checkSensor = "iwo:" + sensorInstance.substring(43);
		
		String query = 
				"PREFIX iwo: <http://www.icmwind.com/icmwindontology.owl#>\n" +
				"SELECT ?type \n" +
				"WHERE { \n" +
					"DirectType(" + checkSensor + ", ?type) }";
		
		SPARQLDLResult result = sdre.sparqldlProcessing(query);
		VariableBinding binding = result.getBinding();
		
		CloseableIterator<Binding> it = binding.iterator();
		while(it.hasNext()) {
			try {
				Binding b = (Binding) it.next();
				List<Value> vals = b.getValues();
					
				String type_val = ((Value) vals.get(binding.getVariables().indexOf("type"))).stringValue();
				type = type_val.substring(43);
										
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
				
		return type;
	}
		
	static void getFCMConfig() {

		logger.println();
		logger.println("****Get FCM Configuration****");
		logger.println();
		logger.println("Initializing SPARQL-DL Query...");

		String query = 
				"PREFIX iwo: <http://www.icmwind.com/icmwindontology.owl#>\n" +
//				"SELECT ?fcm ?comp ?sen ?type ?class ?flt \n" +
				"SELECT ?fcm ?comp ?sen ?type ?class \n" +
				"WHERE { \n" +
					"Type(?fcm, iwo:Fluid_Condition_Monitoring), \n" +
					"PropertyValue(?fcm, iwo:installedAt, ?comp), \n" +
//					"PropertyValue(?comp, iwo:isPartOf, iwo:marpI) }"; //, \n" +
					"PropertyValue(?fcm, iwo:consistsOf, ?sen), \n" +
					"DirectType(?sen, ?type), \n" +
					"Type(?sen, ?class), \n" +
					"DirectSubClassOf(?class, iwo:Sensor) }" + 
				"OR WHERE { \n" +
					"Type(?fcm, iwo:Fluid_Condition_Monitoring), \n" +
					"PropertyValue(?fcm, iwo:installedAt, ?comp), \n" +
//					"PropertyValue(?comp, iwo:isPartOf, iwo:marpI), \n" +
					"PropertyValue(?fcm, iwo:consistsOf, ?sen), \n" +
					"DirectType(?sen, ?type), \n" +
					"Type(?sen, ?class), \n" +
					"DirectSubClassOf(?class, iwo:Sensor), \n" +
					"DirectType(?flt, iwo:Sensor_Fault), \n" +
					"PropertyValue(?flt, iwo:faultySensor, ?sen) }";
		
		System.out.println("Processing SPARQL-DL Query...");
		logger.println("-----QUERY----");
		logger.println(query);
		logger.println("--------------");
		logger.println();
				
		SPARQLDLResult result = sdre.sparqldlProcessing(query);
		
		long dlProcessEvalTime = TimeManagement.get().elapsedTime() / 1000000; TimeManagement.get().restart();
		System.out.println("SPARQL-DL Query Processing time : " + dlProcessEvalTime + "ms");
		logger.println("SPARQL-DL FCM Config Query Processing time : " + dlProcessEvalTime + "ms");
		logger.println();
		logger.flush();
		
		VariableBinding binding = result.getBinding();
		
		System.out.println("Processing SPARQL-DL Query Results...");
		logger.println("Processing SPARQL-DL Query Results...");
		logger.println();
		logger.println("###### SPARQL-DL Query FCM Config Results ######");

		CloseableIterator<Binding> it = binding.iterator();
		while(it.hasNext()) {
			try {
				Binding b = (Binding) it.next();
				List<Value> vals = b.getValues();
				
				String fcm_val = ((Value) vals.get(binding.getVariables().indexOf("fcm"))).stringValue();
				String comp_val = ((Value) vals.get(binding.getVariables().indexOf("comp"))).stringValue();
				String sen_val = ((Value) vals.get(binding.getVariables().indexOf("sen"))).stringValue();
				String type_val = ((Value) vals.get(binding.getVariables().indexOf("type"))).stringValue();
				String class_val = ((Value) vals.get(binding.getVariables().indexOf("class"))).stringValue();
				String flt_val = "NO FAULT";  //((Value) vals.get(binding.getVariables().indexOf("flt"))).stringValue();
				
				if(! (binding.getVariables().indexOf("flt") < 0) ) {
					flt_val = ((Value) vals.get(binding.getVariables().indexOf("flt"))).stringValue();
				}
				
				if(askSensorFault(sen_val.substring(43))) {
					flt_val = "YES";
				}
				
				String pos_val = getSensorPosition(type_val.substring(43));

				System.out.println("*****" + fcm_val.substring(43) + "-" + comp_val.substring(43) + "-" + sen_val.substring(43)
						+ "::" + type_val.substring(43) + "::" + class_val.substring(43) + "::" + pos_val + "-" + flt_val);
								
				logger.println("~~" + fcm_val.substring(43) + " - " + comp_val.substring(43) + " - " + sen_val.substring(43)
						+ " -- " + type_val.substring(43) + " -- " + class_val.substring(43) + " - " + pos_val + " - " + flt_val);
				
				System.out.println();
				logger.println();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		logger.println("################################################");
		logger.println();
		
		long dlResultProcessTime = TimeManagement.get().elapsedTime() / 1000000; TimeManagement.get().restart();
		System.out.println("SPARQL-DL Result Processing time : " + dlResultProcessTime + "ms");
		logger.println("SPARQL-DL FCM Config Query Result Processing time : " + dlResultProcessTime + "ms");
		logger.println("****************");
		logger.flush();
		
	}
	
	static boolean askSensorFault(String sensor) {
		
		String checkSensor = "iwo:" + sensor; 
		
		String prefix = iwoPrfx + rdfPrfx;
		String query = prefix + "\n" +
				"ASK WHERE { " +
					"?sflt rdf:type iwo:Sensor_Fault. " +
					"?sflt iwo:faultySensor " + checkSensor  + ". " +				
				"}";
		
		System.out.println("Processing ASK Query..." + checkSensor);
				
		SPARQLQuery askQuery = new SPARQLQueryImpl(query);
		BooleanInformationSet bis = sdre.sparqlAsk(askQuery, null);
		
		long askQueryEvalTime = TimeManagement.get().elapsedTime() / 1000000; TimeManagement.get().restart();

		System.out.println("ASK Query Processing time : " + askQueryEvalTime + "ms");
				
		System.out.println("Processing ASK Query Results...");
				
		System.out.println("ASK Query Result - " + bis.getValue());
		
		return bis.getValue();

	}
	
	static String getHasPositionConnection(String sensor_class) {
		logger.print("STAR Query: " + sensor_class + ", " + "Connection" + "; ");
			
		String connection = null;

		List<String> entities = new ArrayList<String>();
		entities.add(sensor_class);
		entities.add("Connection");
				
		List<List<Statement>> answer = sdre.computeTopRelationalTrees(entities, 1, true);
		
//		System.out.println("**----------------Test.checkSensor2ConnectionRelation() : Paths found...");
		
		for(List<Statement> paths : answer ) {
			System.out.println("PATH: ");
			
			String hasPositionObject = null;
			String subClassOfSubject = null; 
			
			for(Statement s : paths) {
				if(s.getPredicateString().equals("hasPosition"))
					hasPositionObject = s.getObjectString();
				else if(s.getPredicateString().equals("subClassOf"))
					subClassOfSubject = s.getSubjectString();
				
				System.out.println(s.getSubjectString() + "--" + s.getPredicateString() + "--" + s.getObjectString());
			}
			
			if(hasPositionObject.equals(subClassOfSubject))
				connection = hasPositionObject;
			else
				connection = "NA";
		}
		
		long starConnectionProcessTime = TimeManagement.get().elapsedTime() / 1000000; TimeManagement.get().restart();
		System.out.println("STAR Connection Query Processing time : " + starConnectionProcessTime + "ms");
		logger.println("STAR Connection Query Processing time : " + starConnectionProcessTime + "ms");
		logger.flush();
		
		return connection;
	}
	
	static String getSensorPosition(String sensor_class) {
		logger.print("STAR Query: " + sensor_class + ", " + "Component" + "; ");
		System.out.println("STAR Query: " + sensor_class + ", " + "Component" );
		
		String position = null;

		List<String> entities = new ArrayList<String>();
		entities.add(sensor_class);
		entities.add("Component");
				
		List<List<Statement>> answer = sdre.computeTopRelationalTrees(entities, 1, true);
		
//		System.out.println("**----------------Test.checkSensor2ComponentRelation() : Paths found...");
//		logger.println("STAR Query Paths - ");
		
		for(List<Statement> paths : answer ) {
			System.out.println("PATH: ");
//			logger.println("*PATH: ");
			
			String hasPositionObject = null;
			String subClassOfSubject = null; 
			
			for(Statement s : paths) {
				if(s.getPredicateString().equals("hasPosition"))
					hasPositionObject = s.getObjectString();
				else if(s.getPredicateString().equals("subClassOf"))
					subClassOfSubject = s.getSubjectString();
				
				System.out.println(s.getSubjectString() + "--" + s.getPredicateString() + "--" + s.getObjectString());
//				logger.println("\t" + s.getSubjectString() + "--" + s.getPredicateString() + "--" + s.getObjectString());
			}
			
			if(hasPositionObject.equals(subClassOfSubject))
				position = hasPositionObject;
			else
				position = "NA";
		}
		
		long starComponentProcessTime = TimeManagement.get().elapsedTime() / 1000000; TimeManagement.get().restart();
		System.out.println("STAR Component Query Processing time : " + starComponentProcessTime + "ms");
		logger.println("STAR Component Query Processing time : " + starComponentProcessTime + "ms");
		logger.flush();
		
		return position;
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
					"WHERE {" +
						"{ " +
							"?flt rdf:type iwo:Fault. " +
							"?flt iwo:faultyComponent ?comp. " +
							"?flt iwo:faultCondition ?cond. " +
						"} UNION {" +
							"?flt rdf:type iwo:Fault. " +
							"?flt iwo:faultySensor ?comp. " +
							"?flt iwo:faultCondition ?cond. " +
						"}" +
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
	
	static void doSensorSensorDiagnosis() {
		
		logger.println();
		logger.println("****Sensor-Sensor Diagnosis****");
				
		String prefix = iwoPrfx + rdfPrfx;
		if(askSameSensorFaultsOccurrence()) {
			
			logger.println();
			logger.println("Initializing SELECT Query....");
						
			String query2 = prefix + "\n" +
					"SELECT DISTINCT ?time ?sen ?cond ?val ?cond2 ?val2 " +
					"WHERE { " +
						"?sflt rdf:type iwo:Sensor_Fault. " +
						"?sflt2 rdf:type iwo:Sensor_Fault. " +
						"?sflt iwo:faultySensor ?sen. " +
						"?sflt2 iwo:faultySensor ?sen. " +
						"?sflt iwo:faultCondition ?cond. " +
						"?cond iwo:hasValue ?val. " +
						"?sflt2 iwo:faultCondition ?cond2. " +
						"?cond2 iwo:hasValue ?val2. " +
						"?sflt iwo:faultOccurrenceTime ?time. " +
						"?sflt2 iwo:faultOccurrenceTime ?time. " +
						"FILTER(?cond != ?cond2)" +
					"}";
			
			System.out.println("Processing SPARQL SELECT Query...");
			logger.println("-----QUERY----");
			logger.println(query2);
			logger.println("--------------");
			logger.println();
			
			SPARQLQuery slctQuery = new SPARQLQueryImpl(query2);
			VariableBinding binding = sdre.sparqlSelect(slctQuery, null);
			
			long selectQueryEvalTime = TimeManagement.get().elapsedTime() / 1000000; TimeManagement.get().restart();
			System.out.println("SELECT Query Processing time : " + selectQueryEvalTime + "ms");
			logger.println("SELECT Query Faulty Sensor Info Processing time : " + selectQueryEvalTime + "ms");
			logger.flush();
		
			Set<String> timeSet = new HashSet<>();
			Set<String> senSet = new HashSet<>();
			Set<String> propSet = new HashSet<>();
					
			CloseableIterator<Binding> it = binding.iterator();
			while(it.hasNext()) {
				try {
					Binding b = (Binding) it.next();
					List<Value> vals = b.getValues();
					String time_val = ((Value) vals.get(binding.getVariables().indexOf("time"))).stringValue();
					String sen_val = ((Value) vals.get(binding.getVariables().indexOf("sen"))).stringValue();
					String cond_val = ((Value) vals.get(binding.getVariables().indexOf("cond"))).stringValue();
					String val_val = ((Value) vals.get(binding.getVariables().indexOf("val"))).stringValue();
					String cond2_val = ((Value) vals.get(binding.getVariables().indexOf("cond2"))).stringValue();
					String val2_val = ((Value) vals.get(binding.getVariables().indexOf("val2"))).stringValue();
					
					timeSet.add(time_val.substring(43));
					senSet.add( sen_val.substring(43));
					propSet.add(cond_val.substring(43) + " (" + val_val + ")");
					propSet.add(cond2_val.substring(43) + " (" + val2_val + ")");
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			for(String t : timeSet) {
				System.out.print("At Time \"" + t + "\",");
				logger.print("At Time \"" + t + "\",");
				
				for(String s : senSet) {
					System.out.print("Sensor \"" + s + "\" has faulty measurement properties: ");
					logger.print("Sensor \"" + s + "\" has faulty measurement properties: ");
					
					for(String p : propSet) {
						System.out.print(p + ", ");
						logger.print(p + ", ");
					}
					
					System.out.println(". Replace Sensor!!");
					logger.println(". Replace Sensor!!");
				}
			}
			
			long selectQueryProcessTime = TimeManagement.get().elapsedTime() / 1000000; TimeManagement.get().restart();
			System.out.println("SELECT Query Insert time : " + selectQueryProcessTime + "ms");
			logger.println("SELECT Query Faulty Sensor Info Result Processing time : " + selectQueryProcessTime + "ms");
			logger.println("*********************");
			logger.flush();
			
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
	
	static void tempSparql() {
		String prefix = iwoPrfx + rdfsPrfx + rdfPrfx;
		String query = 
				prefix + "\n" +
				"SELECT DISTINCT ?t ?p ?o " +
				"WHERE { " +
				"?t rdfs:subClassOf iwo:Temperature. " +
//				"?t iwo:isPropertyOf iwo:Oil. " +
				"?t ?p ?o. " +
				"}";
			
		SPARQLQuery slctQuery = new SPARQLQueryImpl(query);
		VariableBinding binding = sdre.sparqlSelect(slctQuery, null);
			
		CloseableIterator<Binding> it = binding.iterator();
		System.out.println("**----------------Test.checkFaultsExistence() : Faults found...");
		while(it.hasNext()) {
			try {
				Binding b = (Binding) it.next();
				List<Value> vals = b.getValues();
				String t_val = ((Value) vals.get(binding.getVariables().indexOf("t"))).stringValue();
				String p_val = ((Value) vals.get(binding.getVariables().indexOf("p"))).stringValue();
				String o_val = ((Value) vals.get(binding.getVariables().indexOf("o"))).stringValue();
				System.out.println(t_val + "\t" + p_val + "\t" + o_val);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	static void getSimilarSensors() {
		
		// get equivalent properties
		String q1 = 
				"PREFIX iwo: <http://www.icmwind.com/icmwindontology.owl#>\n" +
				"SELECT ?p ?ep WHERE {\n" +
					"SubClassOf(?p, iwo:Property), \n" +
					"EquivalentClass(?p, ?ep)" +
				"}";
		
	 	SPARQLDLResult q1result = sdre.sparqldlProcessing(q1);
		VariableBinding binding = q1result.getBinding();
		
		CloseableIterator<Binding> it = binding.iterator();
		while(it.hasNext()) {
			try {
				Binding b = (Binding) it.next();
				List<Value> vals = b.getValues();
				String p_val = ((Value) vals.get(binding.getVariables().indexOf("p"))).stringValue();
				String ep_val = ((Value) vals.get(binding.getVariables().indexOf("ep"))).stringValue();
				System.out.println(p_val + "\t" + ep_val);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
	 	
		
		// get sensors measuring these properties
		
		
		// find equivalence between these sensors
		
		
		
	}
	
	static void checkConstruct() {
		String prefix = iwoPrfx + rdfPrfx;// + fnPrfx ;
		
//		String query3 = 
//				prefix + "\n" +
//				"CONSTRUCT { " +
//				"?sens rdf:type iwo:Fault. " +
//				"} WHERE { " +
//				"?sens rdf:type iwo:CS_1000. " +
//				"}";
		
		String query3 = 
				prefix + "\n" +
				"CONSTRUCT { " +
				"?cflt rdf:type iwo:Fault. " +
				"} WHERE { " +
				"?sens rdf:type iwo:CS_1000. " +
				"BIND(STRAFTER(str(?sens), \"#\") AS ?t). " +
//				"BIND(IRI(CONCAT(\"http://www.icmwind.com/icmwindontology.owl#CF_\", str(?sens))) AS ?cflt). " +
//				"BIND((\"http://www.icmwind.com/icmwindontology.owl#CF_CS_1000\") AS ?cflt). " +
				"bind(iri(concat(\"http://www.icmwind.com/icmwindontology.owl#CF_\", ?t)) AS ?cflt) " +
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
				"SELECT ?sen " +
				"WHERE { " +
				"?sen rdf:type iwo:Fault. " +
//				"?sen rdf:type iwo:CS_1000. " +
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
				writer.println(s_val + " -- " + p_val + " -- " + o_val);
				
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
