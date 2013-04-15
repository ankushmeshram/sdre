package de.dfki.isreal.semantic.oms.components.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import de.dfki.isreal.helpers.ConcurrentCounter;
import de.dfki.isreal.data.Statement;
import de.dfki.isreal.helpers.StatementHelpers;

/**
 * JUST FOR TESTING... NOT USED IN THE SZENARIO
 * 
 * This class extends Thread. It provides a registry for statements and effect
 * threads. The lifecycle of the class waits for insert updates at the GSE. If
 * they are applied, this class checks its registry if a statement (containing
 * variables) is fullfilled by the inserted triple. If yes, the effect thread is
 * started.
 * 
 * The idea behind this is, to register a query that is constantly checked every
 * time the GSE is updated. This can be used for SEServices to check the
 * applicability without sending the query to the gse every time.
 * 
 * 
 * @author stenes
 * 
 */
public class ContinousQueryRegistry implements Runnable {

	private Logger logger = Logger.getLogger(ContinousQueryRegistry.class);

	private Hashtable<Integer, String> rule_reg = null;
	private ConcurrentCounter count;

	private Hashtable<String, List<Statement>> subj_hash;
	private Hashtable<Statement, List<Integer>> ids;
	private List<Statement> to_check;
	private boolean stop;
	private Thread thread = null;

	public ContinousQueryRegistry() {
		count = new ConcurrentCounter();
		rule_reg = new Hashtable<Integer, String>();
		ids = new Hashtable<Statement, List<Integer>>();
		subj_hash = new Hashtable<String, List<Statement>>();
		to_check = new ArrayList<Statement>();
		stop = false;
		thread = new Thread(this);
		thread.start();
	}

	public void registerQuery(List<Statement> q, String uri) {
		// get unique id for thread
		int id = count.getCount();
		rule_reg.put(id, uri);

		// register this id for the statements
		for (Statement s : q) {
			if (!StatementHelpers.isVariable(s.getSubjectString())) {
				insertSubj(s.getSubjectString(), s);
				insertIds(s, id);
			} else {
				insertSubj("VAR", s);
				insertIds(s, id);
			}
		}
		count.increase();
		logger.info("CONTINOUS_QUERY: Registered rule: " + uri);
	}

	private void insertIds(Statement s, int id) {
		if (ids.contains(s)){
			List<Integer> s_ids = ids.get(s);
			ids.remove(s);
			s_ids.add(id);
			ids.put(s, s_ids);
		} else{
			List<Integer> new_list = new ArrayList<Integer>();
			new_list.add(id);
			ids.put(s, new_list);
		}
	}

	public void checkStatement(Statement s){
		synchronized(to_check){
			to_check.add(s);
			to_check.notify();
			logger.debug("CONTINOUS_QUERY: Statement added for checking: " + s.stringValue());
		}
	}

	private void insertSubj(String subject, Statement s) {
		if (subj_hash.containsKey(subject)) {
			List<Statement> val = subj_hash.get(subject);
			subj_hash.remove(subject);
			val.add(s);
			subj_hash.put(subject, val);
		} else {
			ArrayList<Statement> val = new ArrayList<Statement>();
			val.add(s);
			subj_hash.put(subject, val);
		}
	}
	
	public void run(){
		while (!stop) {
			synchronized (to_check) {
				if (to_check.size() == 0) {
					try {
						to_check.wait();
					} catch (InterruptedException e) {
					}
				}
				Set<Integer> to_execute = new HashSet<Integer>();
				for (Statement s : to_check){
					logger.debug("CONTINOUS_QUERY: Checking statement: " + s.stringValue());
					
					logger.debug("CONTINOUS_QUERY: Registered subjects:");
					Iterator<String> it = subj_hash.keySet().iterator();
					while (it.hasNext()){
						logger.debug(it.next());
					}
					
					// all canditates are the matching subjects and all statements
					// that have a variable as subject.
					List<Statement> checkedSubs = subj_hash.get(s.getSubjectString());
					if (checkedSubs == null) checkedSubs = new ArrayList<Statement>();
					checkedSubs.addAll(subj_hash.get("VAR"));
					
					// Filter the matching predicates
					List<Statement> checkedSubPred = new ArrayList<Statement>();
					for (Statement cand : checkedSubs){
						String pred = cand.getPredicateString();
						if (pred.equals(s.getPredicateString()) || StatementHelpers.isVariable(pred)){
							checkedSubPred.add(cand);
						}
					}
					
					// Filter the matching objects
					List<Statement> checkedAll = new ArrayList<Statement>();
					for (Statement cand2 : checkedSubPred){
						String obj = cand2.getObjectString();
						if (obj.equals(s.getObjectString()) || StatementHelpers.isVariable(obj)){
							checkedAll.add(cand2);
						}
					}
					
					// add matches to execute list
					for (Statement match : checkedAll){
						to_execute.addAll(ids.get(match));
					}
				}
				to_check.clear();
				
				// execute rules by get binding and invoke as service
				Iterator<Integer> it = to_execute.iterator();
				while(it.hasNext()){
					logger.info("CONTINOUS_QUERY: Execute thread with id: " + it.next());
				}
			}
		}
	}


}
