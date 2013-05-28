/**
 * 
 */
package de.dfki.sdre;

/**
 * @author anme05
 *
 */
public interface SDRE {
	
	public SDRE getInstance();
	
	public void setSemanticRepository(String semanticRepository);
	
	public void setSemanticReasoner(String semanticReasoner, boolean enabled);
	
	

}
