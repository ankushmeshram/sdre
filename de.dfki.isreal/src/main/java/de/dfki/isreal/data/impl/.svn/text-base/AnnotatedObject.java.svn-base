// former package de.dfki.isreal.agents.rtsg;
package de.dfki.isreal.data.impl;

import java.io.Serializable;
import java.util.HashMap;

/**
 * This class represents an annotated 3D object.
 * It consists of an id number for the identification in RTSG1, 
 * a nodeName for identification in XML3D or OpenSceneGraph.
 * And a metadatarecord, consisting of several Lists of URI Strings.
 * These lists are object, concept, services, context and dependentObjects.
 * 
 * TODO what is still needed in AnotatedObject
 * TODO make a Interface and serveral Implementations. 
 *  
 * @author Stefan Nesbigall; Sadia Masood
 *
 */
public class AnnotatedObject implements Serializable{

	private int id = -1;
	private String[] concept = null;
	private String[] object = null;
	private String[] services = null;
	private String[] context = null;
	private String[] dependentObjects = null;
	
	private String nodeName=null;
		
	/**
	 * Creates an AnnotatedObject with the given out of a HashMap of data.
	 * As default a id number of -1 should be given.
	 * This constructor is usually used, when a HashMap is send from the
	 * Python of the ISReal first year RTSG1 implementation.
	 * If the Map has not the right format an exception is thrown.
	 * 
	 * @param id
	 * @param data
	 * @throws Exception
	 */
	public AnnotatedObject(int id, HashMap data) throws Exception {
		this.id = id;		
		concept= new String[]{};
		object= new String[]{};
		services= new String[]{};
		context= new String[]{};
		dependentObjects= new String[]{};
		
		init(data);
	}
	
	/**
	 * This constructor creates an empty
	 * AnnotatedObject, with default id -1 and the given URI as 
	 * @param uri
	 */
	public AnnotatedObject(String uri) {
		concept= new String[]{};
		object= new String[]{uri};
		services= new String[]{};
		context= new String[]{};
		dependentObjects= new String[]{};
	}
	
	/**
	 * This cconstructor creates an complete empty AnnotatedRecord.
	 * The id is default -1. 
	 */
	public AnnotatedObject(){
			
		concept= new String[]{};
		object= new String[]{};
		services= new String[]{};
		context= new String[]{};
		nodeName=new String();
		dependentObjects= new String[]{};
		
	}
	
	/**
	 * Another constructor creating an AnnotatedObject. It gets all the content
	 * from possible empty String Arrays.
	 * @param name
	 * @param objs
	 * @param concs
	 * @param servs
	 * @param conts
	 * @param dep_objs
	 */
	public AnnotatedObject(String name, String[] objs, String[] concs,
			String[] servs, String[] conts, String[] dep_objs) {
		nodeName = name;
		object = objs;
		concept = concs;
		services = servs;
		context = conts;
		dependentObjects = dep_objs;
		
	}

	private void init(HashMap data) throws Exception {
		if(((String)data.get("nodeType")).equalsIgnoreCase("MetadataSet")) {
			
			if(((String)data.get("name")).equalsIgnoreCase("semantic_annotation")) {
				
				Object[] content = (Object[]) data.get("value");
				
				for(int i=0; i<content.length; i++) {
					HashMap map = (HashMap)content[i];
					
					if(((String)map.get("name")).equalsIgnoreCase("concept")) {
						Object[] c= (Object[])map.get("value");
						
						concept = new String[c.length];
						for(int z=0; z<c.length; z++) {
							concept[z] = (String)c[z];
						}
					} else if (((String)map.get("name")).equalsIgnoreCase("object")) {
						Object[] o= (Object[])map.get("value");
						
						object = new String[o.length];
						for(int z=0; z<o.length; z++) {
							object[z] = (String)o[z];
						}
					} else if (((String)map.get("name")).equalsIgnoreCase("services")) {
						Object[] s= (Object[])map.get("value");
						
						services = new String[s.length];
						for(int z=0; z<s.length; z++) {
							services[z] = (String)s[z];
						}
					} else if (((String)map.get("name")).equalsIgnoreCase("relational_context")) {
						Object[] c= (Object[])map.get("value");
						
						context = new String[c.length];
						for(int z=0; z<c.length; z++) {
							context[z] = (String)c[z];
						}
					} else if (((String)map.get("name")).equalsIgnoreCase("dependent_objects")) {
						Object[] c= (Object[])map.get("value");
						
						dependentObjects = new String[c.length];
						for(int z=0; z<c.length; z++) {
							dependentObjects[z] = (String)c[z];
						}
					}
				}	    	
			} else throw new Exception("IllegalX3DAnnotationException: found no semantic annotations");
		} else throw new Exception("IllegalX3DAnnotationException: found no metadata set");
	}

	/**
	 * Returns the id of the AnnotatedObject.
	 * @return 
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Returns the concepts of the AnnotatedObject.
	 * @return String array of the concept URIs.
	 */
	public String[] getConcept() {
		return concept;
	}
	
	/**
	 * Returns the object of the AnnotatedObject.
	 * @return String array of the object URIs.
	 */
	public String[] getObject() {
		return object;
	}
	
	/**
	 * Returns the service of the AnnotatedObject.
	 * @return String array of the service URIs.
	 */
	public String[] getServices() {
		return services;
	}
	
	/**
	 * Returns the SEServices of the AnnotatedObject.
	 * @return String array of the SEService URIs.
	 */
	public String[] getRelationalContext() {
		return context;
	}
	
	/**
	 * Returns the dependent objects of the AnnotatedObject.
	 * @return String array of the dependent object URIs.
	 */
	public String[] getDependentObjects() {
		return dependentObjects;
	}
	
	/**
	 * 
	 * Returns a readable short string of the AnnotatedObject.
	 */
	public String toString() {
		return "(" + id + "; " + object[0] + ")"; 
	}
	
	/**
	 * ??
	 * @return
	 */
	public Object fetchSemantics() {
		return null;
	}
	
	/**
	 * ??
	 * @return
	 */
	public Object fetchServices() {
		return null;
	}
	
	/**
	 * Returns the name of the 3D node of the AnnotatedObject.
	 * @return String of the node name.
	 */
	public String getNodeName() {
		return nodeName;
	}
	
	public String[] getContext() {
		return context;
	}
	
	// Added Set Methods
	public void setId(int i) {
		id=i;
	}
	
	public void setNodeName(String n){
		nodeName=n;
	}
	
	public void setConcepts(String[] c) {
		
		concept=new String[c.length];		
		for (int i=0; i<c.length; i++)
			concept[i]=(String)c[i];		
	}
	
	public void setObjects(String[] o) {
		
		object=new String[o.length];		
		for (int i=0; i<o.length; i++)
			object[i]=(String)o[i];		
	}
	

	

	
	public void setContexts(String[] ct) {
		
		context=new String[ct.length];		
		for (int i=0; i<ct.length; i++)
			context[i]=(String)ct[i];		
	}
	
	
	
	
	public void setServices(String[] s) {
		
		services=new String[s.length];		
		for (int i=0; i<s.length; i++)
			services[i]=(String)s[i];		
	}
	
	
	
	
}
