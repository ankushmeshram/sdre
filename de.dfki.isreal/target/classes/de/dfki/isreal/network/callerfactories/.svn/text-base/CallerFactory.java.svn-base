package de.dfki.isreal.network.callerfactories;

import java.net.URI;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import de.dfki.isreal.network.CallerThread;
import de.dfki.isreal.network.ISRealMessageFactory;
import de.dfki.isreal.network.protos.ProtoTransformer;
import de.dfki.isreal.network.protos.ExchangeDataProtos.ISRealAnswer;
import de.dfki.isreal.network.protos.ExchangeDataProtos.ISRealMessage;
import de.dfki.isreal.network.socket.SocketRegistry;
import de.dfki.isreal.data.SPARQLDLResult;
import de.dfki.isreal.data.Statement;
import eu.larkc.core.data.BooleanInformationSet;
import eu.larkc.core.data.SetOfStatements;
import eu.larkc.core.data.VariableBinding;
import eu.larkc.core.query.SPARQLQuery;

/**
 * This class could also implement the Interfaces between the components iff
 * there is a static registry to look up the sockets.
 * 
 * @author stenes
 *
 */
public class CallerFactory {
	private static Logger logger = Logger.getLogger(CallerFactory.class);
	
	// TODO IDs should be globally unique (in setting with different JVM running in P2P)!
	// private static int next_id = 0;
	private static Random idGen = new Random(System.currentTimeMillis());
	
	public static BooleanInformationSet startSparqlAskCaller(SPARQLQuery q, String sname){
//		int qid = next_id;
//		next_id++;
		int qid = idGen.nextInt();
		
		ISRealMessage m = ISRealMessageFactory.createISRealQueryMessage(ISRealMessageFactory.createSparqlAskQuery(q, qid));
		int sid = SocketRegistry.getSocketId(sname);
		CallerThread t = new CallerThread(sid, m, true);
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			logger.info("THIS EXCEPTION SHOULD NOT BE HARMFULL!!!");
			e.printStackTrace();
		}
		ISRealAnswer a = t.getAnswer();
		if (a.getQueryId() == qid){
			return ProtoTransformer.getBooleanInformationSet(a);
		} 
		logger.error("Error after joining the caller thread.");
		return null;
	}
	
	public SetOfStatements startSparqlConstructCaller(SPARQLQuery q, String sname){
//		int qid = next_id;
//		next_id++;
		int qid = idGen.nextInt();
		
		ISRealMessage m = ISRealMessageFactory.createISRealQueryMessage(ISRealMessageFactory.createSparqlConstructQuery(q, qid));
		int sid = SocketRegistry.getSocketId(sname);
		CallerThread t = new CallerThread(sid, m, true);
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			logger.info("THIS EXCEPTION SHOULD NOT BE HARMFULL!!!");
			e.printStackTrace();
		}
		ISRealAnswer a = t.getAnswer();
		if (a.getQueryId() == qid){ 
			return ProtoTransformer.getSetOfStatements(a);
		} 
		logger.error("Error after joining the caller thread.");
		return null;
	}
	
	public static VariableBinding startSparqlSelectCaller(SPARQLQuery q, String sname){
//		int qid = next_id;
//		next_id++;
		int qid = idGen.nextInt();
		
		ISRealMessage m = ISRealMessageFactory.createISRealQueryMessage(ISRealMessageFactory.createSparqlSelectQuery(q, qid));
		int sid = SocketRegistry.getSocketId(sname);
		CallerThread t = new CallerThread(sid, m, true);
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			logger.info("THIS EXCEPTION SHOULD NOT BE HARMFULL!!!");
			e.printStackTrace();
		}
		ISRealAnswer a = t.getAnswer();
		if (a.getQueryId() == qid){
			return ProtoTransformer.getVariableBinding(a);
		} 
		logger.error("Error after joining the caller thread.");
		return null;
	}
	
	public SPARQLDLResult startSparqlDlQuery(String q, String sname){
//		int qid = next_id;
//		next_id++;
		int qid = idGen.nextInt();
		
		ISRealMessage m = ISRealMessageFactory.createISRealQueryMessage(ISRealMessageFactory.createSparqlDlQuery(q, qid));
		int sid = SocketRegistry.getSocketId(sname);
		CallerThread t = new CallerThread(sid, m, true);
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			logger.info("THIS EXCEPTION SHOULD NOT BE HARMFULL!!!");
			e.printStackTrace();
		}
		ISRealAnswer a = t.getAnswer();
		if (a.getQueryId() == qid){
			return ProtoTransformer.getSPARQLDLResult(a);
		} 
		logger.error("Error after joining the caller thread.");
		return null;
	}
	
	public boolean startClassConsistencyQuery(URI cl, String sname){
//		int qid = next_id;
//		next_id++;
		int qid = idGen.nextInt();
		
		ISRealMessage m = ISRealMessageFactory.createISRealQueryMessage(ISRealMessageFactory.createClassConsQuery(cl, qid));
		int sid = SocketRegistry.getSocketId(sname);
		CallerThread t = new CallerThread(sid, m, true);
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			logger.info("THIS EXCEPTION SHOULD NOT BE HARMFULL!!!");
			e.printStackTrace();
		}
		ISRealAnswer a = t.getAnswer();
		if (a.getQueryId() == qid){
			return a.getBoolVal();
		} 
		logger.error("Error after joining the caller thread.");
		return false;
	}
	
	public boolean startKBConsistencyQuery(String sname){
//		int qid = next_id;
//		next_id++;
		int qid = idGen.nextInt();
		
		ISRealMessage m = ISRealMessageFactory.createISRealQueryMessage(ISRealMessageFactory.createKBConsQuery(qid));
		int sid = SocketRegistry.getSocketId(sname);
		CallerThread t = new CallerThread(sid, m, true);
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			logger.info("THIS EXCEPTION SHOULD NOT BE HARMFULL!!!");
			e.printStackTrace();
		}
		ISRealAnswer a = t.getAnswer();
		if (a.getQueryId() == qid){
			return a.getBoolVal();
		} 
		logger.error("Error after joining the caller thread.");
		return false;
	}
	
	public List<Statement> startPerceptionQuery(String inst, String sname){
//		int qid = next_id;
//		next_id++;
		int qid = idGen.nextInt();
		
		ISRealMessage m = ISRealMessageFactory.createISRealQueryMessage(ISRealMessageFactory.createPerceptionQuery(inst, qid));
		int sid = SocketRegistry.getSocketId(sname);
		CallerThread t = new CallerThread(sid, m, true);
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			logger.info("THIS EXCEPTION SHOULD NOT BE HARMFULL!!!");
			e.printStackTrace();
		}
		ISRealAnswer a = t.getAnswer();
		if (a.getQueryId() == qid){
			List<Statement> sts = ProtoTransformer.getListOfStatements(a.getStatementsList());
			return sts;
		} 
		logger.error("Error after joining the caller thread.");
		return null;
	}
}
