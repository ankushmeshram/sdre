package de.dfki.isreal.network.callerfactories;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import de.dfki.isreal.network.CallerThread;
import de.dfki.isreal.network.ISRealMessageFactory;
import de.dfki.isreal.network.protos.ProtoTransformer;
import de.dfki.isreal.network.protos.ExchangeDataProtos.ISRealAnswer;
import de.dfki.isreal.network.protos.ExchangeDataProtos.ISRealMessage;
import de.dfki.isreal.network.socket.SocketRegistry;
import de.dfki.isreal.subcomponents.FactChangeEventHandler;
import de.dfki.isreal.components.GSE;
import de.dfki.isreal.data.BindingList;
import de.dfki.isreal.data.SPARQLDLResult;
import de.dfki.isreal.data.State;
import de.dfki.isreal.data.Statement;
import eu.larkc.core.data.BooleanInformationSet;
import eu.larkc.core.data.SetOfStatements;
import eu.larkc.core.data.VariableBinding;
import eu.larkc.core.qos.QoSInformation;
import eu.larkc.core.qos.QoSParameters;
import eu.larkc.core.query.SPARQLQuery;

/**
 * CallerFactory implementing the GSE Interface.
 * 
 * @author stenes
 * 
 */
public class GSECallerFactory implements GSE {
	private static Logger logger = Logger.getLogger(CallerFactory.class);
	private static String gseRoleName = "GSE";
	// TODO IDs should be globally unique (in setting with different JVM running in P2P)!
	// private static int next_id = 0;
	private static Random idGen = new Random(System.currentTimeMillis());

	public boolean checkClassConsistency(URI cl) {
		if (SocketRegistry.isRoleRegistered(gseRoleName)) {
//			int qid = next_id;
//			next_id++;
			int qid = idGen.nextInt();
			
			ISRealMessage m = ISRealMessageFactory
					.createISRealQueryMessage(ISRealMessageFactory
							.createClassConsQuery(cl, qid));
			int sid = SocketRegistry.getSocketIdFromRole(gseRoleName);
			CallerThread t = new CallerThread(sid, m, true);
			t.start();
			try {
				t.join();
			} catch (InterruptedException e) {
				logger.info("THIS EXCEPTION SHOULD NOT BE HARMFULL!!!");
				e.printStackTrace();
			}
			ISRealAnswer a = t.getAnswer();
			if (a.getQueryId() == qid) {
				return a.getBoolVal();
			}
			logger.error("Error after joining the caller thread.");
			return false;
		} else {
			logger
					.error("Not connected to component with role: "
							+ gseRoleName);
		}
		return false;
	}

	public boolean checkClassDisjunction(URI c1, URI c2) {
		if (SocketRegistry.isRoleRegistered(gseRoleName)) {
//			int qid = next_id;
//			next_id++;
			int qid = idGen.nextInt();
			
			ISRealMessage m = ISRealMessageFactory
					.createISRealQueryMessage(ISRealMessageFactory
							.createClassDisQuery(c1, c2, qid));
			int sid = SocketRegistry.getSocketIdFromRole(gseRoleName);
			CallerThread t = new CallerThread(sid, m, true);
			t.start();
			try {
				t.join();
			} catch (InterruptedException e) {
				logger.info("THIS EXCEPTION SHOULD NOT BE HARMFULL!!!");
				e.printStackTrace();
			}
			ISRealAnswer a = t.getAnswer();
			if (a.getQueryId() == qid) {
				return a.getBoolVal();
			}
			logger.error("Error after joining the caller thread.");
			return false;
		} else {
			logger
					.error("Not connected to component with role: "
							+ gseRoleName);
		}
		return false;
	}

	public boolean checkClassEquivalence(URI c1, URI c2) {
		if (SocketRegistry.isRoleRegistered(gseRoleName)) {
//			int qid = next_id;
//			next_id++;
			int qid = idGen.nextInt();
			
			ISRealMessage m = ISRealMessageFactory
					.createISRealQueryMessage(ISRealMessageFactory
							.createClassEquQuery(c1, c2, qid));
			int sid = SocketRegistry.getSocketIdFromRole(gseRoleName);
			CallerThread t = new CallerThread(sid, m, true);
			t.start();
			try {
				t.join();
			} catch (InterruptedException e) {
				logger.info("THIS EXCEPTION SHOULD NOT BE HARMFULL!!!");
				e.printStackTrace();
			}
			ISRealAnswer a = t.getAnswer();
			if (a.getQueryId() == qid) {
				return a.getBoolVal();
			}
			logger.error("Error after joining the caller thread.");
			return false;
		} else {
			logger
					.error("Not connected to component with role: "
							+ gseRoleName);
		}
		return false;
	}

	public boolean checkClassSubsumption(URI c1, URI c2) {
		if (SocketRegistry.isRoleRegistered(gseRoleName)) {
//			int qid = next_id;
//			next_id++;
			int qid = idGen.nextInt();
			
			ISRealMessage m = ISRealMessageFactory
					.createISRealQueryMessage(ISRealMessageFactory
							.createClassSubSumQuery(c1, c2, qid));
			int sid = SocketRegistry.getSocketIdFromRole(gseRoleName);
			CallerThread t = new CallerThread(sid, m, true);
			t.start();
			try {
				t.join();
			} catch (InterruptedException e) {
				logger.info("THIS EXCEPTION SHOULD NOT BE HARMFULL!!!");
				e.printStackTrace();
			}
			ISRealAnswer a = t.getAnswer();
			if (a.getQueryId() == qid) {
				return a.getBoolVal();
			}
			logger.error("Error after joining the caller thread.");
			return false;
		} else {
			logger
					.error("Not connected to component with role: "
							+ gseRoleName);
		}
		return false;
	}

	public boolean checkKBConsistency() {
		if (SocketRegistry.isRoleRegistered(gseRoleName)) {
//			int qid = next_id;
//			next_id++;
			int qid = idGen.nextInt();
			
			ISRealMessage m = ISRealMessageFactory
					.createISRealQueryMessage(ISRealMessageFactory
							.createKBConsQuery(qid));
			int sid = SocketRegistry.getSocketIdFromRole(gseRoleName);
			CallerThread t = new CallerThread(sid, m, true);
			t.start();
			try {
				t.join();
			} catch (InterruptedException e) {
				logger.info("THIS EXCEPTION SHOULD NOT BE HARMFULL!!!");
				e.printStackTrace();
			}
			ISRealAnswer a = t.getAnswer();
			if (a.getQueryId() == qid) {
				return a.getBoolVal();
			}
			logger.error("Error after joining the caller thread.");
			return false;
		} else {
			logger
					.error("Not connected to component with role: "
							+ gseRoleName);
		}
		return false;
	}

	/**
	 * TODO Implement messages for relational requests and answers.
	 */
	public List<List<Statement>> computeTopRelationalTrees(
			List<String> entities, int i, boolean props) {
		if (SocketRegistry.isRoleRegistered(gseRoleName)) {
			logger.error("NOT YET IMPLEMENTED!");
		} else {
			logger
					.error("Not connected to component with role: "
							+ gseRoleName);
		}
		return null;
	}

	public List<Statement> getObjectFacts(String instance_uri) {
		if (SocketRegistry.isRoleRegistered(gseRoleName)) {
//			int qid = next_id;
//			next_id++;
			int qid = idGen.nextInt();
			
			ISRealMessage m = ISRealMessageFactory
					.createISRealQueryMessage(ISRealMessageFactory
							.createPerceptionQuery(instance_uri, qid));
			int sid = SocketRegistry.getSocketIdFromRole(gseRoleName);
			CallerThread t = new CallerThread(sid, m, true);
			t.start();
			try {
				t.join();
			} catch (InterruptedException e) {
				logger.info("THIS EXCEPTION SHOULD NOT BE HARMFULL!!!");
				e.printStackTrace();
			}
			ISRealAnswer a = t.getAnswer();
			if (a.getQueryId() == qid) {
				List<Statement> sts = ProtoTransformer.getListOfStatements(a
						.getStatementsList());
				return sts;
			}
			logger.error("Error after joining the caller thread.");
			return null;
		} else {
			logger
					.error("Not connected to component with role: "
							+ gseRoleName);
		}
		return null;
	}

	public void insert(List<Statement> a) {
		if (SocketRegistry.isRoleRegistered(gseRoleName)) {
			ISRealMessage m = ISRealMessageFactory
					.createMaintenance(ISRealMessageFactory
							.createInsertMaintenance(ProtoTransformer
									.getStatementList(a)));
			int sid = SocketRegistry.getSocketIdFromRole(gseRoleName);
			CallerThread t = new CallerThread(sid, m, false);
			t.start();
		} else {
			logger
					.error("Not connected to component with role: "
							+ gseRoleName);
		}
	}

	public boolean instanceChecking(URI i, URI c) {
		if (SocketRegistry.isRoleRegistered(gseRoleName)) {
//			int qid = next_id;
//			next_id++;
			int qid = idGen.nextInt();
			
			ISRealMessage m = ISRealMessageFactory
					.createISRealQueryMessage(ISRealMessageFactory
							.createClassInstQuery(i, c, qid));
			int sid = SocketRegistry.getSocketIdFromRole(gseRoleName);
			CallerThread t = new CallerThread(sid, m, true);
			t.start();
			try {
				t.join();
			} catch (InterruptedException e) {
				logger.info("THIS EXCEPTION SHOULD NOT BE HARMFULL!!!");
				e.printStackTrace();
			}
			ISRealAnswer a = t.getAnswer();
			if (a.getQueryId() == qid) {
				return a.getBoolVal();
			}
			logger.error("Error after joining the caller thread.");
			return false;
		} else {
			logger
					.error("Not connected to component with role: "
							+ gseRoleName);
		}
		return false;
	}

	/**
	 * Note that the return value here is always null!!!
	 */
	public List<String> invokeSemanticService(String service_uri,
			BindingList inp_bnd) {
		/**
		 * TODO no return since outputs are send back by perception. TODO it
		 * could be possible to implement adaptive service invokation, where in
		 * case of outputs a answer message is give and the invoking component
		 * wait for that, while in case of no outputs no answer message is
		 * expected.
		 */

		if (SocketRegistry.isRoleRegistered(gseRoleName)) {
			ISRealMessage m = ISRealMessageFactory.createServiceCall(
					service_uri, ProtoTransformer.getBinding(inp_bnd));
			int sid = SocketRegistry.getSocketIdFromRole(gseRoleName);
			CallerThread t = new CallerThread(sid, m, false);
			t.start();
		} else {
			logger
					.error("Not connected to component with role: "
							+ gseRoleName);
		}
		return null;
	}

	public String getServiceDescription(String service_uri) {
		// TODO implement correct
		if (SocketRegistry.isRoleRegistered(gseRoleName)) {
//			int qid = next_id;
//			next_id++;
			int qid = idGen.nextInt();
			
			ISRealMessage m = ISRealMessageFactory
					.createISRealQueryMessage(ISRealMessageFactory
							.createServiceServiceDescQuery(service_uri, qid));
			int sid = SocketRegistry.getSocketIdFromRole(gseRoleName);
			CallerThread t = new CallerThread(sid, m, true);
			t.start();
			try {
				t.join();
			} catch (InterruptedException e) {
				logger.info("THIS EXCEPTION SHOULD NOT BE HARMFULL!!!");
				e.printStackTrace();
			}
			ISRealAnswer a = t.getAnswer();
			if (a.getQueryId() == qid) {
				List<String> sl = a.getStringsList();
				if (sl.size() == 1)
					return sl.get(0);
				else {
					logger.error("Error wrong number of answers.");
					return null;
				}
			}
			logger.error("Error after joining the caller thread.");
			return null;
		} else {
			logger
					.error("Not connected to component with role: "
							+ gseRoleName);
		}
		return null;
	}

	public void remove(List<Statement> a) {
		if (SocketRegistry.isRoleRegistered(gseRoleName)) {
			ISRealMessage m = ISRealMessageFactory
					.createSensorEventScriptMessage(ProtoTransformer
							.getStatementList(a), null);
			int sid = SocketRegistry.getSocketIdFromRole(gseRoleName);
			CallerThread t = new CallerThread(sid, m, false);
			t.start();
		} else {
			logger
					.error("Not connected to component with role: "
							+ gseRoleName);
		}
	}

	public SPARQLDLResult sparqldlProcessing(String query) {
		if (SocketRegistry.isRoleRegistered(gseRoleName)) {
//			int qid = next_id;
//			next_id++;
			int qid = idGen.nextInt();
			
			ISRealMessage m = ISRealMessageFactory
					.createISRealQueryMessage(ISRealMessageFactory
							.createSparqlDlQuery(query, qid));
			int sid = SocketRegistry.getSocketIdFromRole(gseRoleName);
			CallerThread t = new CallerThread(sid, m, true);
			t.start();
			try {
				t.join();
			} catch (InterruptedException e) {
				logger.info("THIS EXCEPTION SHOULD NOT BE HARMFULL!!!");
				e.printStackTrace();
			}
			ISRealAnswer a = t.getAnswer();
			if (a.getQueryId() == qid) {
				return ProtoTransformer.getSPARQLDLResult(a);
			}
			logger.error("Error after joining the caller thread.");
			return null;
		} else {
			logger
					.error("Not connected to component with role: "
							+ gseRoleName);
		}
		return null;
	}

	public void update(List<Statement> a) {
		if (SocketRegistry.isRoleRegistered(gseRoleName)) {
			ISRealMessage m = ISRealMessageFactory
					.createMaintenance(ISRealMessageFactory
							.createUpdateMaintenance(ProtoTransformer
									.getStatementList(a)));
			int sid = SocketRegistry.getSocketId(gseRoleName);
			CallerThread t = new CallerThread(sid, m, false);
			t.start();
		} else {
			logger
					.error("Not connected to component with role: "
							+ gseRoleName);
		}
	}

	public BooleanInformationSet sparqlAsk(SPARQLQuery arg0, QoSParameters arg1) {
		if (SocketRegistry.isRoleRegistered(gseRoleName)) {
//			int qid = next_id;
//			next_id++;
			int qid = idGen.nextInt();
			
			ISRealMessage m = ISRealMessageFactory
					.createISRealQueryMessage(ISRealMessageFactory
							.createSparqlAskQuery(arg0, qid));
			int sid = SocketRegistry.getSocketIdFromRole(gseRoleName);
			CallerThread t = new CallerThread(sid, m, true);
			t.start();
			try {
				t.join();
			} catch (InterruptedException e) {
				logger.info("THIS EXCEPTION SHOULD NOT BE HARMFULL!!!");
				e.printStackTrace();
			}
			ISRealAnswer a = t.getAnswer();
			if (a.getQueryId() == qid) {
				return ProtoTransformer.getBooleanInformationSet(a);
			}
			logger.error("Error after joining the caller thread.");
			return null;
		} else {
			logger
					.error("Not connected to component with role: "
							+ gseRoleName);
		}
		return null;
	}

	public SetOfStatements sparqlConstruct(SPARQLQuery arg0, QoSParameters arg1) {
		if (SocketRegistry.isRoleRegistered(gseRoleName)) {
//			int qid = next_id;
//			next_id++;
			int qid = idGen.nextInt();
			
			ISRealMessage m = ISRealMessageFactory
					.createISRealQueryMessage(ISRealMessageFactory
							.createSparqlConstructQuery(arg0, qid));
			int sid = SocketRegistry.getSocketIdFromRole(gseRoleName);
			CallerThread t = new CallerThread(sid, m, true);
			t.start();
			try {
				t.join();
			} catch (InterruptedException e) {
				logger.info("THIS EXCEPTION SHOULD NOT BE HARMFULL!!!");
				e.printStackTrace();
			}
			ISRealAnswer a = t.getAnswer();
			if (a.getQueryId() == qid) {
				return ProtoTransformer.getSetOfStatements(a);
			}
			logger.error("Error after joining the caller thread.");
			return null;
		} else {
			logger
					.error("Not connected to component with role: "
							+ gseRoleName);
		}
		return null;
	}

	public VariableBinding sparqlSelect(SPARQLQuery arg0, QoSParameters arg1) {
		if (SocketRegistry.isRoleRegistered(gseRoleName)) {
//			int qid = next_id;
//			next_id++;
			int qid = idGen.nextInt();
			
			ISRealMessage m = ISRealMessageFactory
					.createISRealQueryMessage(ISRealMessageFactory
							.createSparqlSelectQuery(arg0, qid));
			int sid = SocketRegistry.getSocketIdFromRole(gseRoleName);
			CallerThread t = new CallerThread(sid, m, true);
			t.start();
			try {
				t.join();
			} catch (InterruptedException e) {
				logger.info("THIS EXCEPTION SHOULD NOT BE HARMFULL!!!");
				e.printStackTrace();
			}
			ISRealAnswer a = t.getAnswer();
			if (a.getQueryId() == qid) {
				return ProtoTransformer.getVariableBinding(a);
			}
			logger.error("Error after joining the caller thread.");
			return null;
		} else {
			logger
					.error("Not connected to component with role: "
							+ gseRoleName);
		}
		return null;
	}

	public List<String> getInteractionServices() {
		if (SocketRegistry.isRoleRegistered(gseRoleName)) {
//			int qid = next_id;
//			next_id++;
			int qid = idGen.nextInt();
			
			ISRealMessage m = ISRealMessageFactory
					.createISRealQueryMessage(ISRealMessageFactory
							.createGetInteractionServices(qid));
			int sid = SocketRegistry.getSocketIdFromRole(gseRoleName);
			CallerThread t = new CallerThread(sid, m, true);
			t.start();
			try {
				t.join();
			} catch (InterruptedException e) {
				logger.info("THIS EXCEPTION SHOULD NOT BE HARMFULL!!!");
				e.printStackTrace();
			}
			ISRealAnswer a = t.getAnswer();
			if (a.getQueryId() == qid) {
				return a.getStringsList();
			}
			logger.error("Error after joining the caller thread.");
			return null;
		} else {
			logger
					.error("Not connected to component with role: "
							+ gseRoleName);
		}
		return null;
	}

	public List<String> getAgentServices() {
		if (SocketRegistry.isRoleRegistered(gseRoleName)) {
//			int qid = next_id;
//			next_id++;
			int qid = idGen.nextInt();
			
			ISRealMessage m = ISRealMessageFactory
					.createISRealQueryMessage(ISRealMessageFactory
							.createGetAgentServicesQuery(qid));
			int sid = SocketRegistry.getSocketIdFromRole(gseRoleName);
			CallerThread t = new CallerThread(sid, m, true);
			t.start();
			try {
				t.join();
			} catch (InterruptedException e) {
				logger.info("THIS EXCEPTION SHOULD NOT BE HARMFULL!!!");
				e.printStackTrace();
			}
			ISRealAnswer a = t.getAnswer();
			if (a.getQueryId() == qid) {
				return a.getStringsList();
			}
			logger.error("Error after joining the caller thread.");
			return null;
		} else {
			logger
					.error("Not connected to component with role: "
							+ gseRoleName);
		}
		return null;
	}

	public List<String> getServiceInputs(String selectedService) {
		if (SocketRegistry.isRoleRegistered(gseRoleName)) {
//			int qid = next_id;
//			next_id++;
			int qid = idGen.nextInt();
			
			ISRealMessage m = ISRealMessageFactory
					.createISRealQueryMessage(ISRealMessageFactory
							.createGetServiceInputsQuery(selectedService, qid));
			int sid = SocketRegistry.getSocketIdFromRole(gseRoleName);
			CallerThread t = new CallerThread(sid, m, true);
			t.start();
			try {
				t.join();
			} catch (InterruptedException e) {
				logger.info("THIS EXCEPTION SHOULD NOT BE HARMFULL!!!");
				e.printStackTrace();
			}
			ISRealAnswer a = t.getAnswer();
			if (a.getQueryId() == qid) {
				return a.getStringsList();
			}
			logger.error("Error after joining the caller thread.");
			return null;
		} else {
			logger
					.error("Not connected to component with role: "
							+ gseRoleName);
		}
		return null;
	}

	public String getProfileReport() {
		if (SocketRegistry.isRoleRegistered(gseRoleName)) {
//			int qid = next_id;
//			next_id++;
			int qid = idGen.nextInt();
			
			ISRealMessage m = ISRealMessageFactory
					.createISRealQueryMessage(ISRealMessageFactory
							.createGetReportQuery(qid));
			int sid = SocketRegistry.getSocketIdFromRole(gseRoleName);
			CallerThread t = new CallerThread(sid, m, true);
			t.start();
			try {
				t.join();
			} catch (InterruptedException e) {
				logger.info("THIS EXCEPTION SHOULD NOT BE HARMFULL!!!");
				e.printStackTrace();
			}
			ISRealAnswer a = t.getAnswer();
			if (a.getQueryId() == qid) {
				List<String> sl = a.getStringsList();
				if (sl.size() == 1)
					return sl.get(0);
			}
			logger.error("Error after joining the caller thread.");
			return null;
		} else {
			logger
					.error("Not connected to component with role: "
							+ gseRoleName);
		}
		return null;
	}

	@Override
	public SetOfStatements sparqlDescribe(SPARQLQuery arg0, QoSParameters arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public org.openrdf.model.URI getIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QoSInformation getQoSInformation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addServices(String ont_uri) {
		if (SocketRegistry.isRoleRegistered(gseRoleName)) {
//			int qid = next_id;
//			next_id++;
			int qid = idGen.nextInt();
			
			ISRealMessage m = ISRealMessageFactory.createAddServices(ont_uri);
			int sid = SocketRegistry.getSocketIdFromRole(gseRoleName);
			CallerThread t = new CallerThread(sid, m, false);
			t.start();
		} else {
			logger
					.error("Not connected to component with role: "
							+ gseRoleName);
		}
	}

	@Override
	public List<String> getAllServices() {
		if (SocketRegistry.isRoleRegistered(gseRoleName)) {
//			int qid = next_id;
//			next_id++;
			int qid = idGen.nextInt();
			
			ISRealMessage m = ISRealMessageFactory
					.createISRealQueryMessage(ISRealMessageFactory
							.createGetAllServicesQuery(qid));
			int sid = SocketRegistry.getSocketIdFromRole(gseRoleName);
			CallerThread t = new CallerThread(sid, m, true);
			t.start();
			try {
				t.join();
			} catch (InterruptedException e) {
				logger.info("THIS EXCEPTION SHOULD NOT BE HARMFULL!!!");
				e.printStackTrace();
			}
			ISRealAnswer a = t.getAnswer();
			if (a.getQueryId() == qid) {
				return a.getStringsList();
			}
			logger.error("Error after joining the caller thread.");
			return null;
		} else {
			logger
					.error("Not connected to component with role: "
							+ gseRoleName);
		}
		return null;
	}

	@Override
	public List<String> getSEServices() {
		if (SocketRegistry.isRoleRegistered(gseRoleName)) {
//			int qid = next_id;
//			next_id++;
			int qid = idGen.nextInt();
			
			ISRealMessage m = ISRealMessageFactory
					.createISRealQueryMessage(ISRealMessageFactory
							.createGetSEServicesQuery(qid));
			int sid = SocketRegistry.getSocketIdFromRole(gseRoleName);
			CallerThread t = new CallerThread(sid, m, true);
			t.start();
			try {
				t.join();
			} catch (InterruptedException e) {
				logger.info("THIS EXCEPTION SHOULD NOT BE HARMFULL!!!");
				e.printStackTrace();
			}
			ISRealAnswer a = t.getAnswer();
			if (a.getQueryId() == qid) {
				return a.getStringsList();
			}
			logger.error("Error after joining the caller thread.");
			return null;
		} else {
			logger
					.error("Not connected to component with role: "
							+ gseRoleName);
		}
		return null;
	}

	@Override
	public void removeService(String uri) {
		if (SocketRegistry.isRoleRegistered(gseRoleName)) {
//			int qid = next_id;
//			next_id++;
			int qid = idGen.nextInt();
			
			ISRealMessage m = ISRealMessageFactory.createRemoveService(uri);
			int sid = SocketRegistry.getSocketIdFromRole(gseRoleName);
			CallerThread t = new CallerThread(sid, m, false);
			t.start();
		} else {
			logger
					.error("Not connected to component with role: "
							+ gseRoleName);
		}
	}

	@Override
	public void initFromFiles(List<File> ontologies) {
		List<String> onts = new ArrayList<String>();
		for (File f : ontologies) {
			onts.add(f.getAbsolutePath());
		}
		if (SocketRegistry.isRoleRegistered(gseRoleName)) {
//			int qid = next_id;
//			next_id++;
			int qid = idGen.nextInt();
			
			ISRealMessage m = ISRealMessageFactory.createInitFromFiles(onts);
			int sid = SocketRegistry.getSocketIdFromRole(gseRoleName);
			CallerThread t = new CallerThread(sid, m, false);
			t.start();
		} else {
			logger
					.error("Not connected to component with role: "
							+ gseRoleName);
		}
	}

	@Override
	public void classify() {
		if (SocketRegistry.isRoleRegistered(gseRoleName)) {
//			int qid = next_id;
//			next_id++;
			int qid = idGen.nextInt();
			
			ISRealMessage m = ISRealMessageFactory
					.createISRealQueryMessage(ISRealMessageFactory
							.createClassify(qid));
			int sid = SocketRegistry.getSocketIdFromRole(gseRoleName);
			CallerThread t = new CallerThread(sid, m, false);
			t.start();
		} else {
			logger
					.error("Not connected to component with role: "
							+ gseRoleName);
		}
	}

	@Override
	public List<Statement> listStatements() {
		if (SocketRegistry.isRoleRegistered(gseRoleName)) {
//			int qid = next_id;
//			next_id++;
			int qid = idGen.nextInt();
			
			ISRealMessage m = ISRealMessageFactory
					.createISRealQueryMessage(ISRealMessageFactory
							.createListStatements(qid));
			int sid = SocketRegistry.getSocketIdFromRole(gseRoleName);
			CallerThread t = new CallerThread(sid, m, true);
			t.start();
			try {
				t.join();
			} catch (InterruptedException e) {
				logger.info("THIS EXCEPTION SHOULD NOT BE HARMFULL!!!");
				e.printStackTrace();
			}
			ISRealAnswer a = t.getAnswer();
			if (a.getQueryId() == qid) {
				List<Statement> sts = ProtoTransformer.getListOfStatements(a
						.getStatementsList());
				return sts;
			}
			logger.error("Error after joining the caller thread.");
			return null;
		} else {
			logger
					.error("Not connected to component with role: "
							+ gseRoleName);
		}
		return null;
	}

	@Override
	public void realize() {
		if (SocketRegistry.isRoleRegistered(gseRoleName)) {
//			int qid = next_id;
//			next_id++;
			int qid = idGen.nextInt();
			
			ISRealMessage m = ISRealMessageFactory
					.createISRealQueryMessage(ISRealMessageFactory
							.createRealize(qid));
			int sid = SocketRegistry.getSocketIdFromRole(gseRoleName);
			CallerThread t = new CallerThread(sid, m, false);
			t.start();
		} else {
			logger
					.error("Not connected to component with role: "
							+ gseRoleName);
		}
	}

	@Override
	public State getInitialStateFromKB() {
		logger.error("getInitialStateFromKB() NOT IMPLEMENTED!!!");
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<String, List<Statement>> getObjectFacts(
			List<String> instanceUris) {
		
		if (SocketRegistry.isRoleRegistered(gseRoleName)) {
//			int qid = next_id;
//			next_id++;
			int qid = idGen.nextInt();
			
			ISRealMessage m = ISRealMessageFactory
					.createISRealQueryMessage(ISRealMessageFactory
							.createPerceptionQuery(instanceUris, qid));
			int sid = SocketRegistry.getSocketIdFromRole(gseRoleName);
			CallerThread t = new CallerThread(sid, m, true);
			t.start();
			try {
				t.join();
			} catch (InterruptedException e) {
				logger.info("THIS EXCEPTION SHOULD NOT BE HARMFULL!!!");
				e.printStackTrace();
			}
			ISRealAnswer a = t.getAnswer();
			if (a.getQueryId() == qid) {
				HashMap<String, List<Statement>> map = ProtoTransformer.getHashMapOfStatements(a
						.getStatementListsList(), a.getStringsList());
				return map;
			}
			logger.error("Error after joining the caller thread.");
			return null;
		} else {
			logger
					.error("Not connected to component with role: "
							+ gseRoleName);
		}
		return null;
	}

	@Override
	public void registerFactChangeEventHandler(FactChangeEventHandler handler) {
		// TODO Auto-generated method stub. Must be implemented, if this functionality is used from a Java component!!!
		
	}
	
	@Override
	public void unregisterFactChangeEventHandler(int handlerId) {
		// TODO Auto-generated method stub. Must be implemented, if this functionality is used from a Java component!!!
		
	}

	@Override
	public void closeTS() {
		// TODO Auto-generated method stub
		
	}
}
