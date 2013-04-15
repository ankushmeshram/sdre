package de.dfki.isreal.network;

import java.net.URI;
import java.util.HashMap;
import java.util.List;

import de.dfki.isreal.data.SPARQLDLResult;
import de.dfki.isreal.data.Statement;
import de.dfki.isreal.network.protos.ExchangeDataProtos;
import de.dfki.isreal.network.protos.ExchangeDataProtos.AgentMaintenance;
import de.dfki.isreal.network.protos.ExchangeDataProtos.AgentQuery;
import de.dfki.isreal.network.protos.ProtoTransformer;
import de.dfki.isreal.network.protos.ExchangeDataProtos.AnimationCall;
import de.dfki.isreal.network.protos.ExchangeDataProtos.DemonstratorMessage;
import de.dfki.isreal.network.protos.ExchangeDataProtos.ISRealAnswer;
import de.dfki.isreal.network.protos.ExchangeDataProtos.ISRealMaintenance;
import de.dfki.isreal.network.protos.ExchangeDataProtos.ISRealMessage;
import de.dfki.isreal.network.protos.ExchangeDataProtos.ISRealQuery;
import de.dfki.isreal.network.protos.ExchangeDataProtos.MetadataRecord;
import de.dfki.isreal.network.protos.ExchangeDataProtos.SensorEvent;
import de.dfki.isreal.network.protos.ExchangeDataProtos.ServiceCall;
import de.dfki.isreal.network.protos.ExchangeDataProtos.DemonstratorMessage.Component;
import de.dfki.isreal.network.protos.ExchangeDataProtos.DemonstratorMessage.DMsgType;
import de.dfki.isreal.network.protos.ExchangeDataProtos.ISRealMaintenance.MaintenanceTask;
import de.dfki.isreal.network.protos.ExchangeDataProtos.ISRealQuery.QueryType;
import de.dfki.isreal.network.protos.ExchangeDataProtos.SensorEvent.SensorType;
import eu.larkc.core.data.BooleanInformationSet;
import eu.larkc.core.data.SetOfStatements;
import eu.larkc.core.data.VariableBinding;
import eu.larkc.core.query.SPARQLQuery;

/**
 * 
 * This class creates ISRealMessages for the different components (agent
 * environment, GSE). The created Messages can directly send via a Socket (after
 * standard encoding, see Socket implementations).
 * 
 * This class uses also the ProtoTransformer to transform the data types into
 * the messages defined by the proto file.
 * 
 * @author stenes
 * 
 */
public class ISRealMessageFactory {

	public static ISRealMessage createKeepAlive() {
		ISRealMessage.Builder m = ISRealMessage.newBuilder();
		m.setMsgType(ISRealMessage.MSGType.KEEP_ALIVE);
		return m.build();
	}
	
	public static ISRealMessage createReset() {
		ISRealMessage.Builder m = ISRealMessage.newBuilder();
		m.setMsgType(ISRealMessage.MSGType.RESET);
		return m.build();
	}
	
	public static ISRealMessage createError(String msg) {
		ISRealMessage.Builder m = ISRealMessage.newBuilder();
		m.setMsgType(ISRealMessage.MSGType.ERROR);
		m.setErrorMessage(msg);
		return m.build();
	}

	public static ISRealMessage createSensorEventScriptMessage(
			List<ExchangeDataProtos.Statement> to_remove,
			List<ExchangeDataProtos.Statement> to_insert) {
		ISRealMessage.Builder m = ISRealMessage.newBuilder();
		m.setMsgType(ISRealMessage.MSGType.SENSOR_EVENT);

		SensorEvent.Builder ev = SensorEvent.newBuilder();
		ev.setType(SensorEvent.SensorType.SCRIPT);
		if (to_remove != null) {
			ev.addAllRemoves(to_remove);
		}
		if (to_insert != null) {
			ev.addAllInserts(to_insert);
		}

		m.setSensorevent(ev.build());
		return m.build();
	}

	public static ISRealMessage createInitAcknowledgement() {
		ISRealMessage.Builder init_ack = ISRealMessage.newBuilder();
		init_ack.setMsgType(ISRealMessage.MSGType.INIT_ACK);
		return init_ack.build();
	}

	public static ISRealMessage createInitRegisterClient(String client_name) {
		ISRealMessage.Builder init_reg = ISRealMessage.newBuilder();
		init_reg.setMsgType(ISRealMessage.MSGType.INIT_REGISTER_CLIENT);
		init_reg.setClientName(client_name);
		return init_reg.build();
	}

	public static ISRealMessage createAnswerMessage(ISRealAnswer a) {
		ISRealMessage.Builder answer_message = ISRealMessage.newBuilder();
		answer_message.setMsgType(ISRealMessage.MSGType.ANSWER);
		answer_message.setAnswer(a);
		return answer_message.build();
	}

	public static ISRealAnswer createSparqlAskAnswer(
			BooleanInformationSet sparqlAsk, int id) {
		ISRealAnswer.Builder answer = ISRealAnswer.newBuilder();
		answer.setQueryId(id);
		boolean res = ProtoTransformer.getBoolean(sparqlAsk);
		answer.setBoolVal(res);
		return answer.build();
	}

	public static ISRealQuery createSparqlAskQuery(SPARQLQuery q, int qid) {
		ISRealQuery.Builder query = ISRealQuery.newBuilder();
		query.setQueryId(qid);
		query.setQType(QueryType.SPARQL_ASK);
		query.addStrings(q.toString());

		return query.build();
	}
	
	public static ISRealMessage createISRealQueryMessage(ISRealQuery q) {
		ISRealMessage.Builder m = ISRealMessage.newBuilder();
		m.setMsgType(ISRealMessage.MSGType.QUERY);

		m.setQuery(q);
		return m.build();
	}

	public static ISRealAnswer createSparqlConstructAnswer(
			SetOfStatements sparqlConstruct, int id) {
		ISRealAnswer.Builder answer = ISRealAnswer.newBuilder();
		answer.setQueryId(id);
		List<ExchangeDataProtos.Statement> res = ProtoTransformer
				.getStatementList(sparqlConstruct);
		answer.addAllStatements(res);
		return answer.build();
	}

	public static ISRealQuery createSparqlConstructQuery(SPARQLQuery q,
			int qid) {
		ISRealQuery.Builder query = ISRealQuery.newBuilder();
		query.setQueryId(qid);
		query.setQType(QueryType.SPARQL_CONSTRUCT);
		query.addStrings(q.toString());

		return query.build();
	}

	public static ISRealAnswer createSparqlSelectAnswer(
			VariableBinding sparqlSelect, int id) {
		ISRealAnswer.Builder answer = ISRealAnswer.newBuilder();
		answer.setQueryId(id);
		List<ExchangeDataProtos.Binding> res = ProtoTransformer
				.getBindingList(sparqlSelect);
		answer.addAllBindings(res);
		return answer.build();
	}

	public static ISRealQuery createSparqlSelectQuery(SPARQLQuery q, int qid) {
		ISRealQuery.Builder query = ISRealQuery.newBuilder();
		query.setQueryId(qid);
		query.setQType(QueryType.SPARQL_SELECT);
		query.addStrings(q.toString());

		return query.build();
	}

	public static ISRealAnswer createSparqlDlAnswer(SPARQLDLResult sparqldl,
			int id) {
		ISRealAnswer.Builder answer = ISRealAnswer.newBuilder();
		answer.setQueryId(id);
		if (sparqldl.isBoolean()) {
			answer.setBoolVal(sparqldl.getBoolean());
		} else {
			answer.addAllBindings(ProtoTransformer.getBindingList(sparqldl
					.getBinding()));
		}
		return answer.build();
	}

	public static ISRealAnswer createPerceptionAnswer(List<Statement> facts,
			int id) {
		ISRealAnswer.Builder answer = ISRealAnswer.newBuilder();
		answer.setQueryId(id);
		List<ExchangeDataProtos.Statement> res = ProtoTransformer
				.getStatementList(facts);
		answer.addAllStatements(res);
		return answer.build();
	}

	public static ISRealAnswer createClassConsistAnswer(boolean cons, int id) {
		ISRealAnswer.Builder answer = ISRealAnswer.newBuilder();
		answer.setQueryId(id);
		answer.setBoolVal(cons);
		return answer.build();
	}

	public static ISRealAnswer createClassDisAnswer(boolean cons, int id) {
		ISRealAnswer.Builder answer = ISRealAnswer.newBuilder();
		answer.setQueryId(id);
		answer.setBoolVal(cons);
		return answer.build();
	}

	public static ISRealAnswer createClassEqAnswer(boolean cons, int id) {
		ISRealAnswer.Builder answer = ISRealAnswer.newBuilder();
		answer.setQueryId(id);
		answer.setBoolVal(cons);
		return answer.build();
	}

	public static ISRealAnswer createClassSubsAnswer(boolean cons, int id) {
		ISRealAnswer.Builder answer = ISRealAnswer.newBuilder();
		answer.setQueryId(id);
		answer.setBoolVal(cons);
		return answer.build();
	}

	public static ISRealAnswer createInstChkAnswer(boolean cons, int id) {
		ISRealAnswer.Builder answer = ISRealAnswer.newBuilder();
		answer.setQueryId(id);
		answer.setBoolVal(cons);
		return answer.build();
	}

	public static ISRealAnswer createKBConsistAnswer(boolean cons, int id) {
		ISRealAnswer.Builder answer = ISRealAnswer.newBuilder();
		answer.setQueryId(id);
		answer.setBoolVal(cons);
		return answer.build();
	}

	public static ISRealQuery createSparqlDlQuery(String q, int qid) {
		ISRealQuery.Builder query = ISRealQuery.newBuilder();
		query.setQueryId(qid);
		query.addStrings(q);
		query.setQType(QueryType.SPARQLDL);

		return query.build();
	}

	public static ISRealQuery createClassConsQuery(URI cl, int qid) {
		ISRealQuery.Builder query = ISRealQuery.newBuilder();
		query.setQueryId(qid);
		query.setQType(QueryType.CHECK_CLASS_CONSISTENCY);
		query.addStrings(cl.toString());

		return query.build();
	}

	public static ISRealQuery createClassDisQuery(URI c1, URI c2, int qid) {
		ISRealQuery.Builder query = ISRealQuery.newBuilder();
		query.setQueryId(qid);
		query.setQType(QueryType.CHECK_CLASS_CONSISTENCY);
		query.addStrings(c1.toString());
		query.addStrings(c2.toString());

		return query.build();
	}

	public static ISRealQuery createKBConsQuery(int qid) {
		ISRealQuery.Builder query = ISRealQuery.newBuilder();
		query.setQueryId(qid);
		query.setQType(QueryType.CHECK_CONSISTENCY);

		return query.build();
	}

	public static ISRealQuery createPerceptionQuery(String inst, int qid) {
		ISRealQuery.Builder query = ISRealQuery.newBuilder();
		query.setQueryId(qid);
		query.setQType(QueryType.PERCEPTION);
		query.addStrings(inst);

		return query.build();
	}

	public static ISRealMessage createDemonstratorMsg(String msg, int comp,
			int t, boolean s) {
		ISRealMessage.Builder m = ISRealMessage.newBuilder();
		m.setMsgType(ISRealMessage.MSGType.DEMONSTRATOR_MSG);

		DemonstratorMessage.Builder dm = DemonstratorMessage.newBuilder();
		dm.setSpeech(s);
		dm.setComp(Component.valueOf(comp));
		dm.setType(DMsgType.valueOf(t));
		dm.setMsg(msg);

		m.setDmsg(dm.build());
		return m.build();
	}

	public static ISRealMessage createServiceCall(String service_uri,
			ExchangeDataProtos.Binding inp_bnd) {
		ISRealMessage.Builder m = ISRealMessage.newBuilder();
		m.setMsgType(ISRealMessage.MSGType.SERVICE_CALL);

		ServiceCall.Builder sc = ServiceCall.newBuilder();
		sc.setServiceName(service_uri);
		sc.setBnd(inp_bnd);

		m.setServicecall(sc.build());
		return m.build();
	}

	public static ISRealQuery createClassEquQuery(URI c1, URI c2, int qid) {
		ISRealQuery.Builder query = ISRealQuery.newBuilder();
		query.setQueryId(qid);
		query.setQType(QueryType.CHECK_CLASS_EQUIVALENCE);
		query.addStrings(c1.toString());
		query.addStrings(c2.toString());

		return query.build();
	}

	public static ISRealQuery createClassSubSumQuery(URI c1, URI c2, int qid) {
		ISRealQuery.Builder query = ISRealQuery.newBuilder();
		query.setQueryId(qid);
		query.setQType(QueryType.CHECK_CLASS_SUBSUMPTION);
		query.addStrings(c1.toString());
		query.addStrings(c2.toString());

		return query.build();
	}

	public static ISRealQuery createClassInstQuery(URI i, URI c, int qid) {
		ISRealQuery.Builder query = ISRealQuery.newBuilder();
		query.setQueryId(qid);
		query.setQType(QueryType.CHECK_CLASS_SUBSUMPTION);
		query.addStrings(i.toString());
		query.addStrings(c.toString());

		return query.build();
	}

	public static ISRealMessage createAnimationCall(String method,
			List<String> params) {
		AnimationCall.Builder ac = AnimationCall.newBuilder();
		ac.setAnimationName(method);
		ac.setAck(false);
		if (params != null)
			ac.addAllParams(params);

		ISRealMessage.Builder im = ISRealMessage.newBuilder();
		im.setMsgType(ISRealMessage.MSGType.ANIMATION_CALL);
		im.setAnimationcall(ac);
		return im.build();
	}
	
	public static ISRealMessage createAnimationCallWithAck(String method,
			List<String> params, int id) {
		AnimationCall.Builder ac = AnimationCall.newBuilder();
		ac.setAnimationName(method);
		ac.setAck(true);
		ac.setAnimationId(id);
		if (params != null)
			ac.addAllParams(params);

		ISRealMessage.Builder im = ISRealMessage.newBuilder();
		im.setMsgType(ISRealMessage.MSGType.ANIMATION_CALL);
		im.setAnimationcall(ac);
		return im.build();
	}

	public static ISRealQuery createServiceServiceDescQuery(
			String service_uri, int qid) {
		ISRealQuery.Builder query = ISRealQuery.newBuilder();
		query.setQueryId(qid);
		query.setQType(QueryType.GET_SERVICE_DESCRIPTION);
		query.addStrings(service_uri);

		return query.build();
	}

	public static ISRealQuery createGetInteractionServices(int qid) {
		ISRealQuery.Builder query = ISRealQuery.newBuilder();
		query.setQueryId(qid);
		query.setQType(QueryType.GET_INTERACTION_SERVICES);

		return query.build();
	}

	public static ISRealQuery createGetAgentServicesQuery(int qid) {
		ISRealQuery.Builder query = ISRealQuery.newBuilder();
		query.setQueryId(qid);
		query.setQType(QueryType.GET_AGENT_SERVICES);

		return query.build();
	}

	public static ISRealQuery createGetServiceInputsQuery(String s_uri,
			int qid) {
		ISRealQuery.Builder query = ISRealQuery.newBuilder();
		query.setQueryId(qid);
		query.setQType(QueryType.GET_SERVICE_INPUTS);
		query.addStrings(s_uri);

		return query.build();
	}

	public static ISRealAnswer createGetAgentServicesAnswer(List<String> list,
			int queryId) {
		ISRealAnswer.Builder answer = ISRealAnswer.newBuilder();
		answer.setQueryId(queryId);
		answer.addAllStrings(list);
		return answer.build();
	}

	public static ISRealAnswer createGetInteractionServicesAnswer(
			List<String> interactionServices, int queryId) {
		ISRealAnswer.Builder answer = ISRealAnswer.newBuilder();
		answer.setQueryId(queryId);
		answer.addAllStrings(interactionServices);
		return answer.build();
	}

	public static ISRealAnswer createGetServiceDescriptionAnswer(
			String serviceDescription, int queryId) {
		ISRealAnswer.Builder answer = ISRealAnswer.newBuilder();
		answer.setQueryId(queryId);
		answer.addStrings(serviceDescription);
		return answer.build();
	}

	public static ISRealAnswer createGetServiceInputsAnswer(
			List<String> serviceInputs, int queryId) {
		ISRealAnswer.Builder answer = ISRealAnswer.newBuilder();
		answer.setQueryId(queryId);
		answer.addAllStrings(serviceInputs);
		return answer.build();
	}

	public static ISRealQuery createGetReportQuery(int qid) {
		ISRealQuery.Builder query = ISRealQuery.newBuilder();
		query.setQueryId(qid);
		query.setQType(QueryType.GET_REPORT);

		return query.build();
	}

	public static ISRealAnswer createGetReportAnswer(
			String globalSESProfileReport, int queryId) {
		ISRealAnswer.Builder answer = ISRealAnswer.newBuilder();
		answer.setQueryId(queryId);
		answer.addStrings(globalSESProfileReport);
		return answer.build();
	}

	public static ISRealMessage createSensorEventPerceptionMessage(
			List<String> outs) {
		ISRealMessage.Builder m = ISRealMessage.newBuilder();
		m.setMsgType(ISRealMessage.MSGType.SENSOR_EVENT);

		SensorEvent.Builder ev = SensorEvent.newBuilder();
		ev.setType(SensorType.PERCEPTION);

		for (String obj : outs) {
			MetadataRecord.Builder md = MetadataRecord.newBuilder();
			md.setObject(obj);
			ev.addRecord(md.build());
		}
		m.setSensorevent(ev.build());
		return m.build();
	}
	

	public static ISRealMessage createSensorEventPerceptionMessage(
			String agentID, List<MetadataRecord> perceptions) {
		ISRealMessage.Builder m = ISRealMessage.newBuilder();
		m.setMsgType(ISRealMessage.MSGType.SENSOR_EVENT);

		SensorEvent.Builder ev = SensorEvent.newBuilder();
		ev.setType(SensorType.PERCEPTION);
		ev.setAgent(agentID);
		ev.addAllRecord(perceptions);

		m.setSensorevent(ev.build());
		return m.build();
	}

	public static ISRealQuery createClassify(int qid) {
		ISRealQuery.Builder query = ISRealQuery.newBuilder();
		query.setQueryId(qid);
		query.setQType(QueryType.REALIZE);

		return query.build();
	}

	public static ISRealQuery createListStatements(int qid) {
		ISRealQuery.Builder query = ISRealQuery.newBuilder();
		query.setQueryId(qid);
		query.setQType(QueryType.LIST_STATEMENTS);

		return query.build();
	}

	public static ISRealQuery createRealize(int qid) {
		ISRealQuery.Builder query = ISRealQuery.newBuilder();
		query.setQueryId(qid);
		query.setQType(QueryType.REALIZE);

		return query.build();
	}

	public static ISRealMessage createInitFromFiles(List<String> onts) {
		ISRealMessage.Builder m = ISRealMessage.newBuilder();
		m.setMsgType(ISRealMessage.MSGType.MAINTENANCE);

		ISRealMaintenance.Builder mt = ISRealMaintenance.newBuilder();
		mt.setType(MaintenanceTask.INIT_FROM_FILES);
		mt.addAllStrings(onts);

		m.setMaintenance(mt.build());
		return m.build();
	}

	public static ISRealMessage createRemoveService(String uri) {
		ISRealMessage.Builder m = ISRealMessage.newBuilder();
		m.setMsgType(ISRealMessage.MSGType.MAINTENANCE);

		ISRealMaintenance.Builder mt = ISRealMaintenance.newBuilder();
		mt.setType(MaintenanceTask.REMOVE_SERVICE);
		mt.addStrings(uri);

		m.setMaintenance(mt.build());
		return m.build();
	}

	public static ISRealQuery createGetAllServicesQuery(int qid) {
		ISRealQuery.Builder query = ISRealQuery.newBuilder();
		query.setQueryId(qid);
		query.setQType(QueryType.GET_ALL_SERVICES);

		return query.build();
	}

	public static ISRealMessage createAddServices(String ont_uri) {
		ISRealMessage.Builder m = ISRealMessage.newBuilder();
		m.setMsgType(ISRealMessage.MSGType.MAINTENANCE);
		
		ISRealMaintenance.Builder mt = ISRealMaintenance.newBuilder();
		mt.setType(MaintenanceTask.INSERT_SERVICE);
		mt.addStrings(ont_uri);
		
		m.setMaintenance(mt.build());
		return m.build();
	}

	public static ISRealQuery createGetSEServicesQuery(int qid) {
		ISRealQuery.Builder query = ISRealQuery.newBuilder();
		query.setQueryId(qid);
		query.setQType(QueryType.GET_SE_SERVICES);

		return query.build();
	}

	public static ISRealMessage createAgentQuery(String agentID,
			ISRealQuery query) {
		ISRealMessage.Builder m = ISRealMessage.newBuilder();
		m.setMsgType(ISRealMessage.MSGType.AGENT_QUERY);
		
		AgentQuery.Builder q = AgentQuery.newBuilder();
		q.setAgent(agentID);
		q.setQuery(query);
		
		m.setAQuery(q.build());
		return m.build();
	}
	
	public static ISRealMessage createMaintenance(ISRealMaintenance mt){
		ISRealMessage.Builder m = ISRealMessage.newBuilder();
		m.setMsgType(ISRealMessage.MSGType.MAINTENANCE);
		
		m.setMaintenance(mt);
		return m.build();
	}

	public static ISRealMaintenance createInsertMaintenance(
			List<de.dfki.isreal.network.protos.ExchangeDataProtos.Statement> statementList) {
		ISRealMaintenance.Builder mt = ISRealMaintenance.newBuilder();
		mt.setType(MaintenanceTask.INSERT_STATEMENT);
		mt.addAllStatements(statementList);
		
		return mt.build();
	}

	public static ISRealMessage createAgentMaintenance(String agentID,
			ISRealMaintenance mt) {
		ISRealMessage.Builder m = ISRealMessage.newBuilder();
		m.setMsgType(ISRealMessage.MSGType.AGENT_MAINTENANCE);
		
		AgentMaintenance.Builder am = AgentMaintenance.newBuilder();
		am.setAgent(agentID);
		am.setMaintenance(mt);
		
		
		m.setAMaintenance(am.build());
		return m.build();
	}

	public static ISRealMessage createDemonstratorCommunicationMsg(String msg,
			String from, String to, boolean s) {
		ISRealMessage.Builder m = ISRealMessage.newBuilder();
		m.setMsgType(ISRealMessage.MSGType.DEMONSTRATOR_MSG);

		DemonstratorMessage.Builder dm = DemonstratorMessage.newBuilder();
		dm.setSpeech(s);
		dm.setComp(Component.AGENT);
		dm.setType(DMsgType.COMMUNICATION);
		dm.setMsg(msg);
		dm.setSender(from);
		dm.setReceiver(to);

		m.setDmsg(dm.build());
		return m.build();
	}

	public static ISRealMaintenance createUpdateMaintenance(
			List<de.dfki.isreal.network.protos.ExchangeDataProtos.Statement> statementList) {
		ISRealMaintenance.Builder mt = ISRealMaintenance.newBuilder();
		mt.setType(MaintenanceTask.UPDATE_STATEMENTS);
		mt.addAllStatements(statementList);
		
		return mt.build();
	}

	public static ISRealMessage createDemonstratorAgentMsg(String msg,
			int comp, int t, String from, boolean s) {
		ISRealMessage.Builder m = ISRealMessage.newBuilder();
		m.setMsgType(ISRealMessage.MSGType.DEMONSTRATOR_MSG);

		DemonstratorMessage.Builder dm = DemonstratorMessage.newBuilder();
		dm.setSpeech(s);
		dm.setComp(Component.valueOf(comp));
		dm.setType(DMsgType.valueOf(t));
		dm.setMsg(msg);
		dm.setSender(from);

		m.setDmsg(dm.build());
		return m.build();
	}

	public static ISRealQuery createPerceptionQuery(List<String> instanceUris,
			int qid) {
		ISRealQuery.Builder query = ISRealQuery.newBuilder();
		query.setQueryId(qid);
		query.setQType(QueryType.PERCEPTION);
		query.addAllStrings(instanceUris);

		return query.build();
	}

	public static ISRealAnswer createPerceptionAnswer(
			HashMap<String, List<Statement>> objectFacts, int qid) {
		ISRealAnswer.Builder answer = ISRealAnswer.newBuilder();
		answer.setQueryId(qid);
		
		for(String instance : objectFacts.keySet()) {
			answer.addStatementLists(ProtoTransformer.getProtoStatementList(objectFacts.get(instance)));
			answer.addStrings(instance);
		}
		
		return answer.build();
	}


}
