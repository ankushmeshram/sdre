package de.dfki.isreal.semantic.services.thread.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.dfki.isreal.main.Config;
import de.dfki.isreal.network.callerfactories.GraphicsCallerFactory;
import de.dfki.isreal.components.GSE;
import de.dfki.isreal.data.BindingList;
import de.dfki.isreal.data.ServiceWrapper;
import de.dfki.isreal.data.Statement;
import de.dfki.isreal.data.impl.StatementImpl;
import de.dfki.isreal.semantic.services.ServiceImplementationThread;

public class WriteOnChipService extends ServiceImplementationThread {
	private static Logger logger = Logger.getLogger(WriteOnChipService.class);

	private static String type = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
	private static String stores = "http://www.dfki.de/isreal/messemodul.owl#stores";

	public WriteOnChipService(ServiceWrapper service, BindingList b) {
		super(service, b);
	}

	@Override
	protected List<String> serviceImplementation() {
		if (Config.isRoleRegistered("GSE")) {
			// Change GSE as Effect
			Statement in = new StatementImpl(
					bnd
							.getInstance("http://www.dfki.de/isreal/write_on_chip.owl#chip"),
					stores,
					bnd
							.getInstance("http://www.dfki.de/isreal/write_on_chip.owl#col"));
			List<Statement> ins = new ArrayList<Statement>();
			ins.add(in);

			((GSE) Config.getComponent(Config.getComponentNameForRole("GSE"))).insert(ins);

			services_to_check
					.add("http://www.dfki.de/isreal/cr_pill_to_slider.owl#cr_pill_to_sliderService");
			services_to_check
					.add("http://www.dfki.de/isreal/cr_carriage_to_fill.owl#cr_carriage_to_fillService");
			services_to_check
					.add("http://www.dfki.de/isreal/cr_pill_to_fill.owl#cr_pill_to_fillService");
			services_to_check
					.add("http://www.dfki.de/isreal/cr_redlamp_on.owl#CR_REDLAMP_ONService");

			return new ArrayList<String>();
		} else {
			logger.error("No GlobalSE available in thread: "
					+ WriteOnChipService.class.getName());
		}
		return new ArrayList<String>();
	}

}
