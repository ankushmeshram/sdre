package de.dfki.isreal.helpers;

import java.io.IOException;

/**
 * This thread class gets an process object with its constructor and kills the
 * process when starting the thread. It is used as hook, when ending a root
 * process in order to kill all child processes.
 *  
 * @author stenes
 *
 */
public class KillProcessThread extends Thread {

	private Process running_process = null;
	
	public KillProcessThread(Process p){
		running_process = p;
	}
	
	public void run(){
		try {
			running_process.getErrorStream().close();
			running_process.getInputStream().close();
			running_process.getOutputStream().close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		running_process.destroy();
	}
	
}
