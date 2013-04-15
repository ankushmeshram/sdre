package de.dfki.isreal.semantic.workers;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.dfki.isreal.main.Config;
import de.dfki.isreal.network.ISRealMessageFactory;
import de.dfki.isreal.network.ListenerRegistry;
import de.dfki.isreal.network.protos.ExchangeDataProtos.MaintenanceEventHandler;
import de.dfki.isreal.network.protos.ExchangeDataProtos.MetadataRecord;
import de.dfki.isreal.network.protos.ProtoTransformer;
import de.dfki.isreal.network.protos.ExchangeDataProtos.Binding;
import de.dfki.isreal.network.protos.ExchangeDataProtos.ISRealAnswer;
import de.dfki.isreal.network.protos.ExchangeDataProtos.ISRealMaintenance;
import de.dfki.isreal.network.protos.ExchangeDataProtos.ISRealMessage;
import de.dfki.isreal.network.protos.ExchangeDataProtos.ISRealQuery;
import de.dfki.isreal.network.protos.ExchangeDataProtos.SensorEvent;
import de.dfki.isreal.network.protos.ExchangeDataProtos.ServiceCall;
import de.dfki.isreal.network.protos.ExchangeDataProtos.Statement;
import de.dfki.isreal.network.protos.ExchangeDataProtos.ISRealMaintenance.MaintenanceTask;
import de.dfki.isreal.network.socket.SocketRegistry;
import de.dfki.isreal.network.workers.WorkerThread;
import de.dfki.isreal.subcomponents.FactChangeEventHandler;
import de.dfki.isreal.components.GSE;

/**
 * Worker implementation to handle all incoming GSE messages.
 * 
 * @author stenes
 * 
 */
public class GSEWorker extends WorkerThread {
	private static Logger logger = Logger.getLogger(GSEWorker.class);

	public GSEWorker(int sid, ISRealMessage msg) {
		super(sid, msg);
	}

	@Override
	protected void handleMessage(int sid, ISRealMessage msg) {
		logger.info(System.nanoTime() + " GSE message handling for " + msg);
		ISRealMessage.MSGType m_type = msg.getMsgType();
		if (!Config.isRoleRegistered("GSE")) {
			logger.error("No GlobalSE registered in Config...");
		} else {
			// SENSOR_EVENT
			if (m_type == ISRealMessage.MSGType.SENSOR_EVENT) {
				SensorEvent sensor_ev = msg.getSensorevent();
				SensorEvent.SensorType s_type = sensor_ev.getType();
				// init insert and delete list
				List<Statement>[] changes = new List[2];
				if (s_type == SensorEvent.SensorType.SCRIPT) {
					if (sensor_ev.getRemovesCount() > 0) {
						changes[0] = sensor_ev.getRemovesList();
					}
					if (sensor_ev.getInsertsCount() > 0) {
						changes[1] = sensor_ev.getInsertsList();
					}
					handleGSEUpdate(changes);
				} else if (s_type == SensorEvent.SensorType.ZONE_IN) {
					if (sensor_ev.getInsertsCount() > 0) {
						changes[1] = sensor_ev.getInsertsList();
					}
					handleGSEUpdate(changes);
				} else if (s_type == SensorEvent.SensorType.ZONE_OUT) {
					if (sensor_ev.getRemovesCount() > 0) {
						changes[0] = sensor_ev.getRemovesList();
					}
					handleGSEUpdate(changes);
				}
			} else if (m_type == ISRealMessage.MSGType.SERVICE_CALL) {
				ServiceCall sc = msg.getServicecall();
				handleServiceCall(sc.getServiceName(), sc.getBnd(), sid);
			} else if (m_type == ISRealMessage.MSGType.ANSWER) {
				// wake CallerThread
				ListenerRegistry.wakeCallerThread(msg.getAnswer());
				// QUERY
			} else if (m_type == ISRealMessage.MSGType.QUERY) {
				ISRealMessage answer = ISRealMessageFactory
						.createAnswerMessage(handleQuery(msg.getQuery()));
				logger.info("Sending answer...");
				// send answer back to source of query
				if (SocketRegistry.isRegistered(SocketRegistry
						.getSocketRole(sid))) {
					SocketRegistry.getISRealSocket(sid).send(answer);
				}
			} else if (m_type == ISRealMessage.MSGType.MAINTENANCE) {
				handleMaintenance(msg.getMaintenance(), sid);
			} else {
				logger.error("Incomming message type " + m_type
						+ " could not be handled!");
			}
		}
	}

	private void handleMaintenance(ISRealMaintenance maintenance, int sid) {
		if (maintenance.getType() == MaintenanceTask.INIT_FROM_FILES) {
			List<File> files = new ArrayList<File>();
			for (String fileName : maintenance.getStringsList()) {
				files.add(new File(fileName));
			}
			getGSE().initFromFiles(files);
		} else if (maintenance.getType() == MaintenanceTask.INSERT_SERVICE) {
			for (String service : maintenance.getStringsList()) {
				getGSE().addServices(service);
			}
		} else if (maintenance.getType() == MaintenanceTask.INSERT_STATEMENT) {
			getGSE().insert(ProtoTransformer.getListOfStatements(maintenance.getStatementsList()));
		} else if (maintenance.getType() == MaintenanceTask.REMOVE_SERVICE) {
			for (String service : maintenance.getStringsList()) {
				getGSE().removeService(service);
			}
		} else if (maintenance.getType() == MaintenanceTask.REMOVE_STATEMENTS) {
			getGSE().remove(ProtoTransformer.getListOfStatements(maintenance.getStatementsList()));
		} else if (maintenance.getType() == MaintenanceTask.UPDATE_STATEMENTS) {
			getGSE().update(ProtoTransformer.getListOfStatements(maintenance.getStatementsList()));
		} else if (maintenance.getType() == MaintenanceTask.REGISTER_EVENTHANDLERS) {
			for(MaintenanceEventHandler handler : maintenance.getEventhandlersList()) {
				int handlerId			= handler.getHandlerId();
				MetadataRecord object	= handler.getObject();
				List<String> params		= handler.getParametersList();
				String type				= handler.getType();
				Class<?>[] paramTypes	= new Class[2 + params.size()];
				Object[] paramList		= new Object[2 + params.size()];
				paramTypes[0] = Integer.class;
				paramTypes[1] = MetadataRecord.class;
				paramList[0] = handlerId;
				paramList[1] = object;
				for(int i = 2; i < paramTypes.length; i++) {
					paramTypes[i] = String.class;
					paramList[i] = params.get(i - 2);
				}
				
				try {
					Class<?> handlerClass = Class.forName(type);
					Constructor<?> constructor = handlerClass.getConstructor(paramTypes);
					FactChangeEventHandler handlerInstance = (FactChangeEventHandler) constructor.newInstance(paramList);
					getGSE().registerFactChangeEventHandler(handlerInstance);
				}
				catch(ClassNotFoundException e) {
					logger.error("Invalid class name " + type + " for FactChangeEventHandler.", e);
				}
				catch(NoSuchMethodException e) {
					logger.error("No suitable constructor for FactChangeEventHandler " + type + " and parameter types: " + paramTypes.toString() + ".", e);
				}
				catch(InvocationTargetException e) {
					logger.error("Unable to create instance of " + type + " FactChangeEventHanlder.", e);
				}
				catch(IllegalAccessException e) {
					logger.error("Unable to create instance of " + type + " FactChangeEventHanlder.", e);					
				}
				catch(InstantiationException e) {
					logger.error("Unable to create instance of " + type + " FactChangeEventHanlder.", e);
				}

				
			}
		} else if (maintenance.getType() == MaintenanceTask.UNREGISTER_EVENTHANDLERS) {
			for(MaintenanceEventHandler handler : maintenance.getEventhandlersList()) {
				int handlerId = handler.getHandlerId();
				getGSE().unregisterFactChangeEventHandler(handlerId);
			}
		}		
		else {
			logger.error("Incomming message type " + maintenance.getType()
					+ " could not be handled!");
		}
	}

	private void handleServiceCall(String serviceName, Binding bnd, int sid) {
		List<String> outs = getGSE().invokeSemanticService(serviceName,
				ProtoTransformer.getBindingList(bnd));
		if (outs != null) {
			if (outs.size() > 0) {
				ISRealMessage perc = ISRealMessageFactory
						.createSensorEventPerceptionMessage(outs);
				SocketRegistry.getSocket(SocketRegistry.getSocketRole(sid))
						.send(perc);
			}
		}
	}

	private ISRealAnswer handleQuery(ISRealQuery query) {
		ISRealQuery.QueryType q_type = query.getQType();
		if (q_type == ISRealQuery.QueryType.SPARQL_ASK) {
			// transform the result from the transformed ask query
			if (query.getStringsCount() == 1) {
				ISRealAnswer answer = ISRealMessageFactory
						.createSparqlAskAnswer(getGSE().sparqlAsk(
								ProtoTransformer.getSPARQLQuery(query
										.getStrings(0)), null), query
								.getQueryId());
				return answer;
			}
		} else if (q_type == ISRealQuery.QueryType.SPARQL_CONSTRUCT) {
			// transform the result from the transformed construct query
			if (query.getStringsCount() == 1) {
				ISRealAnswer answer = ISRealMessageFactory
						.createSparqlConstructAnswer(getGSE().sparqlConstruct(
								ProtoTransformer.getSPARQLQuery(query
										.getStrings(0)), null), query
								.getQueryId());
				return answer;
			}
		} else if (q_type == ISRealQuery.QueryType.SPARQL_SELECT) {
			if (query.getStringsCount() == 1) {
				ISRealAnswer answer = ISRealMessageFactory
						.createSparqlSelectAnswer(getGSE().sparqlSelect(
								ProtoTransformer.getSPARQLQuery(query
										.getStrings(0)), null), query
								.getQueryId());
				return answer;
			}
		} else if (q_type == ISRealQuery.QueryType.CHECK_CLASS_CONSISTENCY) {
			if (query.getStringsCount() == 1) {
				ISRealAnswer answer = ISRealMessageFactory
						.createClassConsistAnswer(getGSE()
								.checkClassConsistency(
										URI.create(query.getStrings(0))), query
								.getQueryId());
				return answer;
			}
		} else if (q_type == ISRealQuery.QueryType.CHECK_CONSISTENCY) {
			ISRealAnswer answer = ISRealMessageFactory
					.createClassConsistAnswer(getGSE().checkKBConsistency(),
							query.getQueryId());
			return answer;
		} else if (q_type == ISRealQuery.QueryType.PERCEPTION) {
			if (query.getStringsCount() == 1) {
				ISRealAnswer answer = ISRealMessageFactory
						.createPerceptionAnswer(getGSE().getObjectFacts(
								query.getStrings(0)), query.getQueryId());
				return answer;
			} else if (query.getStringsCount() > 1) {
				ISRealAnswer answer = ISRealMessageFactory
						.createPerceptionAnswer(getGSE().getObjectFacts(
								query.getStringsList()), query.getQueryId());
				return answer;
			}
		} else if (q_type == ISRealQuery.QueryType.SPARQLDL) {
			if (query.getStringsCount() == 1) {
				ISRealAnswer answer = ISRealMessageFactory
						.createSparqlDlAnswer(getGSE().sparqldlProcessing(
								query.getStrings(0)), query.getQueryId());
				return answer;
			}
		} else if (q_type == ISRealQuery.QueryType.CHECK_CLASS_DISJUNCTION) {
			if (query.getStringsCount() == 2) {
				ISRealAnswer answer = ISRealMessageFactory
						.createClassDisAnswer(getGSE().checkClassDisjunction(
								URI.create(query.getStrings(0)),
								URI.create(query.getStrings(1))), query
								.getQueryId());
				return answer;
			}
		} else if (q_type == ISRealQuery.QueryType.CHECK_CLASS_EQUIVALENCE) {
			if (query.getStringsCount() == 2) {
				ISRealAnswer answer = ISRealMessageFactory.createClassEqAnswer(
						getGSE().checkClassEquivalence(
								URI.create(query.getStrings(0)),
								URI.create(query.getStrings(1))), query
								.getQueryId());
				return answer;
			}
		} else if (q_type == ISRealQuery.QueryType.CHECK_CLASS_SUBSUMPTION) {
			if (query.getStringsCount() == 2) {
				ISRealAnswer answer = ISRealMessageFactory
						.createClassSubsAnswer(getGSE().checkClassSubsumption(
								URI.create(query.getStrings(0)),
								URI.create(query.getStrings(1))), query
								.getQueryId());
				return answer;
			}
		} else if (q_type == ISRealQuery.QueryType.INSTANCE_CHECKING) {
			if (query.getStringsCount() == 2) {
				ISRealAnswer answer = ISRealMessageFactory.createInstChkAnswer(
						getGSE().instanceChecking(
								URI.create(query.getStrings(0)),
								URI.create(query.getStrings(1))), query
								.getQueryId());
				return answer;
			}
		} else if (q_type == ISRealQuery.QueryType.GET_AGENT_SERVICES) {
			ISRealAnswer answer = ISRealMessageFactory
					.createGetAgentServicesAnswer(getGSE().getAgentServices(),
							query.getQueryId());
			return answer;
		} else if (q_type == ISRealQuery.QueryType.GET_INTERACTION_SERVICES) {
			ISRealAnswer answer = ISRealMessageFactory
					.createGetInteractionServicesAnswer(getGSE()
							.getInteractionServices(), query.getQueryId());
			return answer;
		} else if (q_type == ISRealQuery.QueryType.GET_SERVICE_DESCRIPTION) {
			if (query.getStringsCount() == 1) {
				ISRealAnswer answer = ISRealMessageFactory
						.createGetServiceDescriptionAnswer(getGSE()
								.getServiceDescription(query.getStrings(0)),
								query.getQueryId());
				return answer;
			}
		} else if (q_type == ISRealQuery.QueryType.GET_SERVICE_INPUTS) {
			if (query.getStringsCount() == 1) {
				ISRealAnswer answer = ISRealMessageFactory
						.createGetServiceInputsAnswer(getGSE()
								.getServiceInputs(query.getStrings(0)), query
								.getQueryId());
				return answer;
			}
		} else if (q_type == ISRealQuery.QueryType.GET_REPORT) {
			ISRealAnswer answer = ISRealMessageFactory.createGetReportAnswer(
					getGSE().getProfileReport(), query.getQueryId());
			return answer;

		}
		logger.error("Could not interpret query: " + query);
		return null;
	}

	private static void handleGSEUpdate(List<Statement>[] changes) {
		if (changes[0] != null) {
			getGSE().remove(ProtoTransformer.getListOfStatements(changes[0]));
		}
		if (changes[1] != null) {

			getGSE().insert(ProtoTransformer.getListOfStatements(changes[1]));
		}
	}

	private static GSE getGSE() {
		return (GSE) Config.getComponent(Config.getComponentNameForRole("GSE"));
	}

}
