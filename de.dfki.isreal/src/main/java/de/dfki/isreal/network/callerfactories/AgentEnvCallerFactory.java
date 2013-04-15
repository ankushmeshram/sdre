package de.dfki.isreal.network.callerfactories;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import de.dfki.isreal.components.AgentEnv;
import de.dfki.isreal.data.BindingList;
import de.dfki.isreal.data.Parameter;
import de.dfki.isreal.data.PlannerOutput;
import de.dfki.isreal.data.SPARQLDLResult;
import de.dfki.isreal.data.ServiceWrapper;
import de.dfki.isreal.data.State;
import de.dfki.isreal.data.Statement;
import de.dfki.isreal.data.impl.ConditionalEffect;
import de.dfki.isreal.network.CallerThread;
import de.dfki.isreal.network.ISRealMessageFactory;
import de.dfki.isreal.network.protos.ProtoTransformer;
import de.dfki.isreal.network.protos.ExchangeDataProtos.ISRealAnswer;
import de.dfki.isreal.network.protos.ExchangeDataProtos.ISRealMessage;
import de.dfki.isreal.network.protos.ExchangeDataProtos.ISRealQuery;
import de.dfki.isreal.network.protos.ExchangeDataProtos.MetadataRecord;
import de.dfki.isreal.network.socket.SocketRegistry;
import eu.larkc.core.data.BooleanInformationSet;
import eu.larkc.core.data.SetOfStatements;
import eu.larkc.core.data.VariableBinding;
import eu.larkc.core.qos.QoSInformation;
import eu.larkc.core.qos.QoSParameters;
import eu.larkc.core.query.SPARQLQuery;

public class AgentEnvCallerFactory implements AgentEnv {
	private static Logger logger = Logger.getLogger(AgentEnvCallerFactory.class);
	private static String agentEnvRoleName = "AgentEnv";
	// TODO IDs should be globally unique (in setting with different JVM running in P2P)!
	// private static int next_id = 0;
	private static Random idGen = new Random(System.currentTimeMillis());

	@Override
	public void handlePerception(String agentID,
			List<MetadataRecord> perceptions) {
		ISRealMessage m = ISRealMessageFactory.createSensorEventPerceptionMessage(agentID, perceptions);
		int sid = SocketRegistry.getSocketIdFromRole(agentEnvRoleName);
		CallerThread t = new CallerThread(sid, m, true);
		t.start();
	}

	@Override
	public boolean checkClassConsistency(String agentID, URI cl) {
		if (SocketRegistry.isRoleRegistered(agentEnvRoleName)){
//			int qid = next_id;
//			next_id++;
			int qid = idGen.nextInt();
			
			ISRealMessage m = ISRealMessageFactory.createAgentQuery(agentID, ISRealMessageFactory.createClassConsQuery(cl, qid));
			int sid = SocketRegistry.getSocketIdFromRole(agentEnvRoleName);
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
		} else {
			logger.error("Not connected to component with role: " + agentEnvRoleName);
		}
		return false;
	}

	@Override
	public boolean checkClassDisjunction(String agentID, URI c1, URI c2) {
		if (SocketRegistry.isRoleRegistered(agentEnvRoleName)){
//				int qid = next_id;
//				next_id++;
				int qid = idGen.nextInt();
			
				ISRealMessage m = ISRealMessageFactory.createAgentQuery(agentID, ISRealMessageFactory.createClassDisQuery(c1, c2, qid));
				int sid = SocketRegistry.getSocketIdFromRole(agentEnvRoleName);
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
			} else {
				logger.error("Not connected to component with role: " + agentEnvRoleName);
			}
			return false;
	}

	@Override
	public boolean checkClassEquivalence(String agentID, URI c1, URI c2) {
		if (SocketRegistry.isRoleRegistered(agentEnvRoleName)){
//				int qid = next_id;
//				next_id++;
				int qid = idGen.nextInt();
			
				ISRealMessage m = ISRealMessageFactory.createAgentQuery(agentID, ISRealMessageFactory.createClassEquQuery(c1, c2, qid));
				int sid = SocketRegistry.getSocketId(agentEnvRoleName);
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
			} else {
				logger.error("Not connected to component with role: " + agentEnvRoleName);
			}
		return false;
	}

	@Override
	public boolean checkClassSubsumption(String agentID, URI c1, URI c2) {
		if (SocketRegistry.isRoleRegistered(agentEnvRoleName)){
//				int qid = next_id;
//				next_id++;
				int qid = idGen.nextInt();

				ISRealMessage m = ISRealMessageFactory.createAgentQuery(agentID, ISRealMessageFactory.createClassSubSumQuery(c1, c2, qid));
				int sid = SocketRegistry.getSocketIdFromRole(agentEnvRoleName);
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
			} else {
				logger.error("Not connected to component with role: " + agentEnvRoleName);
			}
			return false;
	}

	@Override
	public boolean checkKBConsistency(String agentID) {
		if (SocketRegistry.isRoleRegistered(agentEnvRoleName)){
//			int qid = next_id;
//			next_id++;
			int qid = idGen.nextInt();

			ISRealMessage m = ISRealMessageFactory.createAgentQuery(agentID, ISRealMessageFactory.createKBConsQuery(qid));
			int sid = SocketRegistry.getSocketIdFromRole(agentEnvRoleName);
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
		} else {
			logger.error("Not connected to component with role: " + agentEnvRoleName);
		}
		return false;
	}

	@Override
	public List<List<Statement>> computeTopRelationalTrees(String agentID,
			List<String> entities, int i, boolean props) {
		if (SocketRegistry.isRoleRegistered(agentEnvRoleName)){
			// TODO implement
		} else {
			logger.error("Not connected to component with role: " + agentEnvRoleName);
		}
		return null;
	}
	
	@Override
	public void insert(String agentID, List<Statement> a) {
		if (SocketRegistry.isRoleRegistered(agentEnvRoleName)){
			ISRealMessage m = ISRealMessageFactory.createAgentMaintenance(agentID, ISRealMessageFactory.createInsertMaintenance(ProtoTransformer.getStatementList(a)));
			int sid = SocketRegistry.getSocketIdFromRole(agentEnvRoleName);
			CallerThread t = new CallerThread(sid, m, true);
			t.start();
		} else {
			logger.error("Not connected to component with role: " + agentEnvRoleName);
		}
	}

	@Override
	public boolean instanceChecking(String agentID, URI i, URI c) {
		if (SocketRegistry.isRoleRegistered(agentEnvRoleName)){
//				int qid = next_id;
//				next_id++;
				int qid = idGen.nextInt();

				ISRealMessage m = ISRealMessageFactory.createAgentQuery(agentID, ISRealMessageFactory.createClassInstQuery(i, c, qid));
				int sid = SocketRegistry.getSocketIdFromRole(agentEnvRoleName);
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
			} else {
				logger.error("Not connected to component with role: " + agentEnvRoleName);
			}
			return false;
	}
	
	@Override
	public void remove(String agentID, List<Statement> a) {
		if (SocketRegistry.isRoleRegistered(agentEnvRoleName)){
			ISRealMessage m = ISRealMessageFactory.createSensorEventScriptMessage(ProtoTransformer.getStatementList(a), null);
			int sid = SocketRegistry.getSocketIdFromRole(agentEnvRoleName);
			CallerThread t = new CallerThread(sid, m, true);
			t.start();
		} else {
			logger.error("Not connected to component with role: " + agentEnvRoleName);
		}
	}
	
	@Override
	public SPARQLDLResult sparqldlProcessing(String agentID, String query) {
		if (SocketRegistry.isRoleRegistered(agentEnvRoleName)){
//			int qid = next_id;
//			next_id++;
			int qid = idGen.nextInt();

			ISRealMessage m = ISRealMessageFactory.createAgentQuery(agentID, ISRealMessageFactory.createSparqlDlQuery(query, qid));
			int sid = SocketRegistry.getSocketIdFromRole(agentEnvRoleName);
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
		} else {
			logger.error("Not connected to component with role: " + agentEnvRoleName);
		}
		return null;
	}

	@Override
	public void update(String agentID, List<Statement> a) {
		if (SocketRegistry.isRoleRegistered(agentEnvRoleName)){
			ISRealMessage m = ISRealMessageFactory.createAgentMaintenance(agentID, ISRealMessageFactory.createUpdateMaintenance(ProtoTransformer.getStatementList(a)));
			int sid = SocketRegistry.getSocketIdFromRole(agentEnvRoleName);
			CallerThread t = new CallerThread(sid, m, true);
			t.start();
		} else {
			logger.error("Not connected to component with role: " + agentEnvRoleName);
		}
	}
	
	@Override
	public BooleanInformationSet sparqlAsk(String agentID, SPARQLQuery arg0, QoSParameters arg1) {
		if (SocketRegistry.isRoleRegistered(agentEnvRoleName)){
//			int qid = next_id;
//			next_id++;
			int qid = idGen.nextInt();

			ISRealMessage m = ISRealMessageFactory.createAgentQuery(agentID, ISRealMessageFactory.createSparqlAskQuery(arg0, qid));
			int sid = SocketRegistry.getSocketIdFromRole(agentEnvRoleName);
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
		} else {
			logger.error("Not connected to component with role: " + agentEnvRoleName);
		}
		return null;
	}

	@Override
	public SetOfStatements sparqlConstruct(String agentID, SPARQLQuery arg0, QoSParameters arg1) {
		if (SocketRegistry.isRoleRegistered(agentEnvRoleName)){
//			int qid = next_id;
//			next_id++;
			int qid = idGen.nextInt();

			ISRealMessage m = ISRealMessageFactory.createAgentQuery(agentID, ISRealMessageFactory.createSparqlConstructQuery(arg0, qid));
			int sid = SocketRegistry.getSocketIdFromRole(agentEnvRoleName);
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
		} else {
			logger.error("Not connected to component with role: " + agentEnvRoleName);
		}
		return null;
	}
	
	@Override
	public VariableBinding sparqlSelect(String agentID, SPARQLQuery arg0, QoSParameters arg1) {
		if (SocketRegistry.isRoleRegistered(agentEnvRoleName)){
//			int qid = next_id;
//			next_id++;
			int qid = idGen.nextInt();

			ISRealMessage m = ISRealMessageFactory.createAgentQuery(agentID, ISRealMessageFactory.createSparqlSelectQuery(arg0, qid));
			int sid = SocketRegistry.getSocketIdFromRole(agentEnvRoleName);
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
		} else {
			logger.error("Not connected to component with role: " + agentEnvRoleName);
		}
		return null;
	}

	@Override
	public SetOfStatements sparqlDescribe(String agentID, SPARQLQuery arg0, QoSParameters arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initFromFiles(String agentID, List<File> ontologies) {
		List<String> onts = new ArrayList<String>();
		for (File f : ontologies){
			onts.add(f.getAbsolutePath());
		}
		if (SocketRegistry.isRoleRegistered(agentEnvRoleName)){
//			int qid = next_id;
//			next_id++;
			int qid = idGen.nextInt();

			ISRealMessage m = ISRealMessageFactory.createInitFromFiles(onts);
			int sid = SocketRegistry.getSocketIdFromRole(agentEnvRoleName);
			CallerThread t = new CallerThread(sid, m, false);
			t.start();
		} else {
			logger.error("Not connected to component with role: " + agentEnvRoleName);
		}
	}

	@Override
	public void classify(String agentID) {
		if (SocketRegistry.isRoleRegistered(agentEnvRoleName)){
//			int qid = next_id;
//			next_id++;
			int qid = idGen.nextInt();

			ISRealMessage m = ISRealMessageFactory.createAgentQuery(agentID, ISRealMessageFactory.createClassify(qid));
			int sid = SocketRegistry.getSocketIdFromRole(agentEnvRoleName);
			CallerThread t = new CallerThread(sid, m, false);
			t.start();
		} else {
			logger.error("Not connected to component with role: " + agentEnvRoleName);
		}
	}

	@Override
	public List<Statement> listStatements(String agentID) {
		if (SocketRegistry.isRoleRegistered(agentEnvRoleName)){
//			int qid = next_id;
//			next_id++;
			int qid = idGen.nextInt();

			ISRealMessage m = ISRealMessageFactory.createAgentQuery(agentID, ISRealMessageFactory.createListStatements(qid));
			int sid = SocketRegistry.getSocketIdFromRole(agentEnvRoleName);
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
		} else {
			logger.error("Not connected to component with role: " + agentEnvRoleName);
		}
		return null;
	}

	@Override
	public void realize(String agentID) {
		if (SocketRegistry.isRoleRegistered(agentEnvRoleName)){
//			int qid = next_id;
//			next_id++;
			int qid = idGen.nextInt();

			ISRealMessage m = ISRealMessageFactory.createAgentQuery(agentID, ISRealMessageFactory.createRealize(qid));
			int sid = SocketRegistry.getSocketIdFromRole(agentEnvRoleName);
			CallerThread t = new CallerThread(sid, m, false);
			t.start();
		} else {
			logger.error("Not connected to component with role: " + agentEnvRoleName);
		}
	}

	@Override
	public State getInitialStateFromKB(String agentID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void handleAgentQuery(String agent, ISRealQuery query) {
		
	}

}
