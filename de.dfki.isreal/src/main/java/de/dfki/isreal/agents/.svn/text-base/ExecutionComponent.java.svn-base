package de.dfki.isreal.agents;

/**
 * This interface provides the basic methods for controlling active components
 * in the agent platform.
 * 
 * @author warwas
 *
 */
public interface ExecutionComponent {

	public static final int NOT_ACTIVATED = -1;
	public static final int ACTIVATED = 0;
	public static final int PAUSED = 1;
	public static final int SHUTDOWN = 2;
	
	/**
	 * Start/resume the component.
	 */
	public void activate();
	
	/**
	 * Shutdown the component. Cannot be undone.
	 */
	public void shutdown();
	
	/**
	 * Pause the component. Activate will resume the component.
	 */
	public void pause();
	
	/**
	 * Returns the current status of the component.
	 * @return
	 */
	public int getStatus();
	
}
