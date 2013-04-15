package de.dfki.isreal.semantic.services.thread.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.dfki.isreal.components.GSE;
import de.dfki.isreal.data.BindingList;
import de.dfki.isreal.data.ServiceWrapper;
import de.dfki.isreal.data.Statement;
import de.dfki.isreal.data.impl.StatementImpl;
import de.dfki.isreal.main.Config;
import de.dfki.isreal.network.callerfactories.GraphicsCallerFactory;
import de.dfki.isreal.semantic.services.SEServiceImplementationThread;

public class cr_greenlamp_onService extends SEServiceImplementationThread {
	private static Logger logger = Logger
			.getLogger(cr_greenlamp_onService.class);

	private static String type = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
	private static String on = "http://www.dfki.de/isreal/abstract_concepts.owl#On";
	private static String off = "http://www.dfki.de/isreal/abstract_concepts.owl#Off";

	public cr_greenlamp_onService(ServiceWrapper service, BindingList b) {
		super(service, b);
	}

	@Override
	protected List<String> serviceImplementation() {
		if (Config.isRoleRegistered("GSE")) {
			// Trigger animation.
			graphics.sendAnimationCall("switch_green_lamp_on",
					null);

			// TODO Change GSE as Effect since no graphical sensor script yet
			Statement in = new StatementImpl(
					bnd
							.getInstance("http://www.dfki.de/isreal/cr_greenlamp_on.owl#self"),
					type, on);
			List<Statement> ins = new ArrayList<Statement>();
			ins.add(in);

			Statement out = new StatementImpl(
					bnd
							.getInstance("http://www.dfki.de/isreal/cr_greenlamp_on.owl#self"),
					type, off);
			List<Statement> outs = new ArrayList<Statement>();
			outs.add(out);

			((GSE) Config.getComponent(Config.getComponentNameForRole("GSE"))).insert(ins);
			((GSE) Config.getComponent(Config.getComponentNameForRole("GSE"))).remove(outs);

			services_to_check
					.add("http://www.dfki.de/isreal/cr_carriage_to_reader.owl#cr_carriage_to_readerService");
			services_to_check
					.add("http://www.dfki.de/isreal/cr_carriage_to_fill.owl#cr_carriage_to_fillService");
			services_to_check
					.add("http://www.dfki.de/isreal/cr_carriage_to_writer.owl#cr_carriage_to_writerService");
			services_to_check
					.add("http://www.dfki.de/isreal/cr_carriage_to_end.owl#cr_carriage_to_endService");
			services_to_check
					.add("http://www.dfki.de/isreal/cr_delete_chip_at_writer.owl#cr_delete_chip_at_writerService");
			services_to_check
					.add("http://www.dfki.de/isreal/cr_flip_slider_down.owl#cr_flip_slider_downService");
			services_to_check
					.add("http://www.dfki.de/isreal/cr_flip_slider_up.owl#cr_flip_slider_upService");
			services_to_check
					.add("http://www.dfki.de/isreal/cr_pill_to_cup.owl#cr_pill_to_cupService");
			services_to_check
					.add("http://www.dfki.de/isreal/cr_pill_to_end.owl#cr_pill_to_endService");
			services_to_check
					.add("http://www.dfki.de/isreal/cr_pill_to_reader.owl#cr_pill_to_readerService");
			services_to_check
					.add("http://www.dfki.de/isreal/cr_pill_to_fill.owl#cr_pill_to_fillService");
			services_to_check
					.add("http://www.dfki.de/isreal/cr_pill_to_slider.owl#cr_pill_to_sliderService");
			services_to_check
					.add("http://www.dfki.de/isreal/cr_pill_to_writer.owl#cr_pill_to_writerService");
			services_to_check
					.add("http://www.dfki.de/isreal/cr_magazine_empty.owl#cr_magazine_emptyService");
			
			return new ArrayList<String>();
		} else {
			logger.error("No GlobalSE available in thread: "
					+ cr_greenlamp_onService.class.getName());
		}
		return new ArrayList<String>();
	}
}
