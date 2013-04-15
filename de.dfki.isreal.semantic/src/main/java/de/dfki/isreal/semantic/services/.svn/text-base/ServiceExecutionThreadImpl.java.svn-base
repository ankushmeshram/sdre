package de.dfki.isreal.semantic.services;

import java.util.List;

import org.apache.log4j.Logger;

import de.dfki.isreal.data.BindingList;
import de.dfki.isreal.semantic.oms.components.DetailedServiceRegistry;
import de.dfki.isreal.subcomponents.ServiceExecution;

public class ServiceExecutionThreadImpl implements ServiceExecution {

private Logger logger = Logger.getLogger(ServiceExecutionThreadImpl.class);
	
	private DetailedServiceRegistry s_reg = null;
	private ServiceInvokationQueue task_queue= null;
	private OutputQueue out_queue = null;
	
	public ServiceExecutionThreadImpl(DetailedServiceRegistry sreg){
		s_reg = sreg;
		out_queue = new OutputQueue();
		task_queue = new ServiceInvokationQueue(s_reg, out_queue);
	}
	
	@Override
	public List<String> invokeSemanticService(String service_uri, BindingList inp_bnd) {
		logger.info("Invoke Semantic service: " + service_uri);
		
		// insert into task queue
		int id = task_queue.addServiceTask(service_uri, inp_bnd);
		
		// get outputs
		WaiterThread w = out_queue.getOutput(id);
		
		try {
			w.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return w.getOutputList();
	}

}
