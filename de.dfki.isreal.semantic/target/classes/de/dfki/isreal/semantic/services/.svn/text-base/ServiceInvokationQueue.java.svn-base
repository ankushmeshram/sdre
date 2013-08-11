package de.dfki.isreal.semantic.services;

import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import de.dfki.isreal.helpers.ConcurrentCounter;
import de.dfki.isreal.data.BindingList;
import de.dfki.isreal.semantic.oms.components.DetailedServiceRegistry;
import de.dfki.isreal.data.impl.InvokationTask;


/**
 * This class maintains a list of invocation tasks. For every task the
 * Registry starts a new ServiceExecutor to handle it.
 * 
 * @author stenes
 *
 */
public class ServiceInvokationQueue implements Runnable {
	private static Logger logger = Logger.getLogger(ServiceInvokationQueue.class);
	private DetailedServiceRegistry s_reg = null;
	private OutputQueue output_queue = null;
	private SEServiceInvokationQueue se_queue = null;

	private Vector<InvokationTask> registry = null;

	private boolean stop = false;
	private Thread thread = null;

	private ConcurrentCounter count;

	public ServiceInvokationQueue(DetailedServiceRegistry sreg, OutputQueue output_q) {
		s_reg = sreg;
		output_queue = output_q;
		se_queue = new SEServiceInvokationQueue(s_reg);
		registry = new Vector<InvokationTask>();
		count = new ConcurrentCounter();
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		while (!stop) {
			logger.info("lock registry in run");
			synchronized (registry) {
				if (registry.size() == 0) {
					try {
						logger.info("Wait for registry to fill");
						registry.wait();
					} catch (InterruptedException e) {
					}
				}
				for (InvokationTask t : registry) {
					// invoke
					logger.info("Starting ServiceExecutor for "
							+ t.getServiceUri());
					ServiceExecutor s = new ServiceExecutor(t, s_reg, output_queue, se_queue);
					s.start();
				}
				registry.clear();
			}
			logger.info("unlock registry in run");
		}

	}

	public int addServiceTask(String service_uri, BindingList inp_bnd) {
		int id = count.getCount();
		logger.info("Adding invokation task for " + service_uri);
		logger.info("lock registry in addService");
		synchronized (registry) {
			registry.add(new InvokationTask(service_uri, id, inp_bnd));
			registry.notify();
		}
		logger.info("unlock registry in addService");
		count.increase();
		return id;
	}

	public void addServiceTasks(List<InvokationTask> tasks) {
		logger.info("lock registry in addServices");
		synchronized (registry) {
			for (InvokationTask s : tasks) {
				int id = count.getCount();
				logger.info("Adding invokation task for " + s.getServiceUri());
				s.setId(id);
				registry.add(s);
				count.increase();
			}
			registry.notify();
		}
		logger.info("unlock registry in addServices");
	}

}
