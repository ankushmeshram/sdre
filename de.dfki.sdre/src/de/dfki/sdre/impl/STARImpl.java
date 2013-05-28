/**
 * 
 */
package de.dfki.sdre.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import naga.steinertrees.queries.Entity;
import naga.steinertrees.queries.Fact;
import naga.steinertrees.queries.Relation;
import naga.steinertrees.queries.ResultGraph;
import naga.steinertrees.queries.STARfromMM;

import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

import de.dfki.isreal.data.impl.StatementImpl;
import de.dfki.isreal.helpers.Profiler;
import de.dfki.isreal.semantic.oms.helpers.URIFilter;

/**
 * @author anme05
 *
 */
public class STARImpl {

//	public OntModel model = null; //ModelFactory.createOntologyModel();
	private Logger logger = Logger.getLogger(STARImpl.class);
//	public URIFilter filter = null; // new URIFilter("http://www.dfki.de/isreal");
	
	public STARImpl() {
//		model = ModelFactory.createOntologyModel();
//
//		model.getDocumentManager().addAltEntry("http://www.icmwind.com/icmwindontology.owl", "file:C:/workspace_sdre/i2s/icmwindontology.owl");
//		
//		OntModel aboxmodel = ModelFactory.createOntologyModel();
//		aboxmodel.getDocumentManager().addAltEntry("http://www.icmwind.com/instances/IWO-10.10.2008-12.10.2008.owl", "file:C:/workspace_sdre/de.dfki.sdre/res/gse_config/ontologies/iwo-abox-10.10.2008-12.10.2008.owl");
//		aboxmodel.read("http://www.icmwind.com/instances/IWO-10.10.2008-12.10.2008.owl");
//		
//		model.addSubModel(aboxmodel);
//
//		OntModel abox = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
//		abox.getDocumentManager().addAltEntry("http://www.icmwind.com/instance/iwo_abox.owl", "file:C:/workspace_sdre/de.dfki.sdre/res/gse_config/ontologies/iwo_abox.owl");
//		abox.read("http://www.icmwind.com/instance/iwo_abox.owl");
//
//		model.addSubModel(abox);
//		
//		OntModel infobox = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
//		infobox.getDocumentManager().addAltEntry("http://www.icmwind.com/instance/iwo-infobox-10.10.2008-12.10.2008.owl", "file:C:/workspace_sdre/de.dfki.sdre/res/gse_config/ontologies/iwo-infobox-10.10.2008-12.10.2008.owl");
//		infobox.read("http://www.icmwind.com/instance/iwo-infobox-10.10.2008-12.10.2008.owl");
//
//		model.addSubModel(infobox);
//
//				
//		model.read("http://www.icmwind.com/icmwindontology.owl");
//		
//		filter =  new URIFilter("http://www.dfki.de/isreal");
	}
	
//	public List<List<de.dfki.isreal.data.Statement>> computeTopRelationalTrees(
//			List<String> entities, int num, boolean props) {
//		Profiler.startMonitor(this.getClass().getName(),
//				"computeTopRelationalTrees");
//		List<List<de.dfki.isreal.data.Statement>> result = new ArrayList<List<de.dfki.isreal.data.Statement>>();
//
//		Set<Fact> graph = new TreeSet<Fact>();
//		Queue<OntClass> to_check = new LinkedList<OntClass>();
//		OntClass thing = model
//				.getOntClass("http://www.w3.org/2002/07/owl#Thing");
//		to_check.add(thing);
//		// construct graph by his implicit class hierarchy.
//		while (!to_check.isEmpty()) {
//			OntClass ocl = to_check.poll();
//			logger.info("Class to check: " + ocl.getURI());
//			ExtendedIterator<OntClass> eit = ocl.listSubClasses(true);
//			while (eit.hasNext()) {
//				OntClass scl = eit.next();
//				if (!scl.getURI().equals(
//						"http://www.w3.org/2002/07/owl#Nothing")
//						&& filter.accept(scl.getURI())) {
//					to_check.add(scl);
//					Fact f = new Fact(new Entity(scl.getURI()), new Entity(
//							ocl.getURI()), new Relation(
//							"http://www.w3.org/2000/01/rdf-schema#subClassOf"),
//							0.0F);
//					try {
//						graph.add(f);
//					} catch (Exception e) {
//						logger.error("Could not insert fact:\n"
//								+ scl.getURI().toString() + " subClassOf "
//								+ ocl.getURI().toString());
//					}
//				}
//			}
//			// ExtendedIterator<OntResource> e2it =
//			// (ExtendedIterator<OntResource>) ocl
//			// .listInstances(true);
//			ExtendedIterator<? extends OntResource> e2it = ocl
//					.listInstances(true);
//			List<? extends OntResource> inds = e2it.toList();
//			logger.info("Number of direct individuals: " + inds.size());
//			// add instances into class hierarchy
//			// while (e2it.hasNext()) {
//			for (OntResource ind : inds) {
//				// OntResource ind = e2it.next();
//				if (filter.accept(ind.getURI())) {
//					Fact f = new Fact(new Entity(ind.getURI()), new Entity(
//							ocl.getURI()), new Relation(
//							"http://www.w3.org/1999/02/22-rdf-syntax-ns#type"),
//							0.0F);
//					try {
//						graph.add(f);
//					} catch (Exception e) {
//						logger.error("Could not insert fact.");
//						// + ind.getURI().toString() + " type " );
//					}
//
//					if (props) {
//						ExtendedIterator<ObjectProperty> e3it = model
//								.listObjectProperties();
//						// add object properties between instances
//						while (e3it.hasNext()) {
//							ObjectProperty prop = e3it.next();
//							if (filter.accept(prop.getURI())) {
//								NodeIterator nit = ind.listPropertyValues(prop);
//								while (nit.hasNext()) {
//									RDFNode node = nit.next();
//									// System.out.println(ind.getLocalName() +
//									// " -> " +
//									// prop.getLocalName() + " -> " +
//									// node.toString());
//									Fact pf = new Fact(
//											new Entity(ind.getURI()),
//											new Entity(node.toString()),
//											new Relation(prop.getURI()), 0.0F);
//									logger.debug(">>>" + ind.getURI() + " : "
//											+ node.toString() + " : "
//											+ prop.getURI());
//									try {
//										graph.add(pf);
//									} catch (Exception e) {
//										logger.error("Could not insert fact:\n"
//												+ ind.toString() + " "
//												+ prop.getURI().toString()
//												+ " " + node.toString());
//									}
//								}
//							}
//						}
//					}
//				}
//			}
//		}
//
//		try {
//			//debug
//			System.out.println(entities.toString());
//			
//			String[] ents = (String[]) entities.toArray(new String[0]);
//			STARfromMM steiner = new STARfromMM(ents, graph);
//			Queue<ResultGraph> rg = steiner.getTopKTrees(num);
//			while (!rg.isEmpty()) {
//				result.add(getResultStatements(rg.poll()));
//			}
//		} catch (Exception e) {
//			logger.error("Could not compute Steiner Tree.", e);
//			e.printStackTrace();
//		}
//		Profiler.stopMonitor(this.getClass().getName(),
//				"computeTopRelationalTrees");
//		return result;
//	}
	
	
	private Set<Fact> graph = new TreeSet<Fact>();
	
	public List<List<de.dfki.isreal.data.Statement>> computeRelationalQuery(List<String> entities, int num, boolean props) {
		Profiler.startMonitor(this.getClass().getName(),"computeTopRelationalTrees");
		List<List<de.dfki.isreal.data.Statement>> result = new ArrayList<List<de.dfki.isreal.data.Statement>>();
		
		try {
			//debug
			System.out.println(entities.toString());
			
			String[] ents = (String[]) entities.toArray(new String[0]);
			STARfromMM steiner = new STARfromMM(ents, graph);
			Queue<ResultGraph> rg = steiner.getTopKTrees(num);
			while (!rg.isEmpty()) {
				result.add(getResultStatements(rg.poll()));
			}
		} catch (Exception e) {
			logger.error("Could not compute Steiner Tree.", e);
			e.printStackTrace();
		}
		
		Profiler.stopMonitor(this.getClass().getName(),	"computeTopRelationalTrees");
		return result;
	}
		
	
	public void fillGraphWithTriple(String subject, String predicate, String object) {
		Fact f = new Fact(new Entity(subject),new Entity(object),new Relation(predicate), 0.0F);
		try {
			graph.add(f);
		} catch (Exception e) {
			logger.error("Could not insert fact: " + subject + "----" + predicate + "----" + object);
		}
	}
	
	private List<de.dfki.isreal.data.Statement> getResultStatements(
			ResultGraph poll) {
		List<de.dfki.isreal.data.Statement> st_list = new ArrayList<de.dfki.isreal.data.Statement>();
		for (Fact f : poll.getEdges()) {
			de.dfki.isreal.data.Statement st = new StatementImpl(f.getN1()
					.name(), f.label().name(), f.getN2().name());
			st_list.add(st);
		}
		return st_list;
	}
	
}
