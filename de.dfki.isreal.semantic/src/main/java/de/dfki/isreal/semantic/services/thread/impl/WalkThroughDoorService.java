package de.dfki.isreal.semantic.services.thread.impl;

import java.util.ArrayList;
import java.util.List;

import de.dfki.isreal.network.callerfactories.GraphicsCallerFactory;
import de.dfki.isreal.data.BindingList;
import de.dfki.isreal.data.ServiceWrapper;
import de.dfki.isreal.semantic.services.ServiceImplementationThread;

public class WalkThroughDoorService extends ServiceImplementationThread {

	public WalkThroughDoorService(ServiceWrapper service, BindingList b) {
		super(service, b);
	}

	@Override
	protected List<String> serviceImplementation() {
		List<String> params = new ArrayList<String>();
		params
				.add(bnd
						.getInstance("http://www.dfki.de/isreal/walk_through_door.owl#self"));
		params
				.add(bnd
						.getInstance("http://www.dfki.de/isreal/walk_through_door.owl#Door"));
		graphics.sendAnimationCall("move_agent_through_object",
				params);
		return new ArrayList<String>();
	}

}
