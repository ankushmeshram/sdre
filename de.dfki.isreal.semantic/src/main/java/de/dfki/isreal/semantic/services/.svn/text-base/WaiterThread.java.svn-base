package de.dfki.isreal.semantic.services;

import java.util.List;

import de.dfki.isreal.data.impl.Output;

/**
 * This class waits for the output of a ServiceImplementationThread. Since it
 * is not clear how long it takes for a service implementation to produce the
 * output this class waits until the output is produced.
 * 
 * On the other hand if another thread is waiting for the output to continue
 * this class is also created and joined afterwards.
 * 
 * @author stenes
 *
 */
public class WaiterThread extends Thread implements Runnable  {

	private Output o = null;
	private int i = -1;
	private boolean complete = false;
	
	private Thread thread = null;
	
	public WaiterThread(int id) {
		i = id;
		o = new Output();
		thread = new Thread(this);
		thread.start();
	}

	public WaiterThread(int id, List<String> out) {
		i = id;
		o = new Output(out);
		complete = true;
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		while (!complete) {
			synchronized (o) {
				if (o.isNull()) {
					try {
						o.wait();
					} catch (InterruptedException e) {
					}
				} 
				complete = true;
			}
		}
	}

	public void setOutput(List<String> out) {
		synchronized(o){
			o.setList(out);
			o.notify();
		}
	}
	
	public List<String> getOutputList(){
		return o.getList();
	}

}
