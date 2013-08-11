package de.dfki.isreal.data.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.semanticweb.owl.model.OWLIndividual;


/**
 * This class is used in the service implementation to represent a conditional
 * effect.
 * @author stenes
 *
 */
public class ConditionalEffect {

	String name;
	List<String> effects;
	List<String> conditions;
	List<OWLIndividual> eff;
	
	public ConditionalEffect(String n){
		name = n;
		effects = new ArrayList<String>();
		conditions = new ArrayList<String>();
		eff = new ArrayList<OWLIndividual>();
	}
	
	public void addCondition(String c){
		conditions.add(c);
	}
	
	public void addEffect(String e, OWLIndividual eff){
		effects.add(e);
		this.eff.add(eff);
	}
	
	public List<String> getEffects(){
		return effects;
	}
	
	public List<String> getCondition(){
		return conditions;
	}
}
