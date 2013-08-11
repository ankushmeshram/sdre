package de.dfki.isreal.semantic.services.thread.impl;

import java.util.ArrayList;
import java.util.List;

import de.dfki.isreal.main.Config;
import de.dfki.isreal.network.callerfactories.GraphicsCallerFactory;
import de.dfki.isreal.components.GSE;
import de.dfki.isreal.data.BindingList;
import de.dfki.isreal.data.ServiceWrapper;
import de.dfki.isreal.data.Statement;
import de.dfki.isreal.data.impl.StatementImpl;
import de.dfki.isreal.semantic.services.ServiceImplementationThread;

public class FillMagazineService extends ServiceImplementationThread {

	private static String type = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
	private static String isLocatedIn = "http://www.dfki.de/isreal/spatial_ontology.owl#isLocatedIn";
	private static String carriesObject = "http://www.dfki.de/isreal/messemodul.owl#carriesObject";

	public FillMagazineService(ServiceWrapper service, BindingList b) {
		super(service, b);
	}

	@Override
	protected List<String> serviceImplementation() {
		// Trigger animation.
		graphics.sendAnimationCall("add_blue_pill", null);
		
		// Change GSE as Effect
		Statement in1 = new StatementImpl(
				bnd
						.getInstance("http://www.dfki.de/isreal/fill_magazine.owl#mag"),
				type, "http://www.dfki.de/isreal/abstract_concepts.owl#Full");
		Statement in2 = new StatementImpl(
				bnd
						.getInstance("http://www.dfki.de/isreal/fill_magazine.owl#pill"),
				isLocatedIn,
				bnd
						.getInstance("http://www.dfki.de/isreal/fill_magazine.owl#magarea"));
		List<Statement> ins = new ArrayList<Statement>();
		ins.add(in1);
		ins.add(in2);

		Statement out1 = new StatementImpl(
				bnd
						.getInstance("http://www.dfki.de/isreal/fill_magazine.owl#mag"),
				type,
				"http://www.dfki.de/isreal/abstract_concepts.owl#Empty");
		Statement out2 = new StatementImpl(
				bnd
						.getInstance("http://www.dfki.de/isreal/fill_magazine.owl#self"),
				carriesObject,
				bnd
						.getInstance("http://www.dfki.de/isreal/fill_magazine.owl#pill"));
		List<Statement> outs = new ArrayList<Statement>();
		outs.add(out1);
		outs.add(out2);

		((GSE) Config.getComponent(Config.getComponentNameForRole("GSE")))
				.insert(ins);

		((GSE) Config.getComponent(Config.getComponentNameForRole("GSE")))
				.remove(outs);
		
		services_to_check.add("http://www.dfki.de/isreal/cr_redlamp_off2.owl#cr_redlamp_off2Service");

		List<String> returns = new ArrayList<String>();
		returns.add(bnd.getInstance("http://www.dfki.de/isreal/fill_magazine.owl#mag"));
		returns.add(bnd.getInstance("http://www.dfki.de/isreal/fill_magazine.owl#self"));
		
		return returns;
	}

}
