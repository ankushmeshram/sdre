package de.dfki.isreal.semantic.services.thread.impl;

import java.util.ArrayList;
import java.util.List;

import de.dfki.isreal.network.callerfactories.GraphicsCallerFactory;
import de.dfki.isreal.data.BindingList;
import de.dfki.isreal.data.ServiceWrapper;
import de.dfki.isreal.semantic.services.ServiceImplementationThread;

public class CloseDoorService extends ServiceImplementationThread{

	public CloseDoorService(ServiceWrapper service, BindingList b) {
		super(service, b);
	}

	@Override
	protected List<String> serviceImplementation() {
		List<String> params = new ArrayList<String>();
		params
				.add(bnd
						.getInstance("http://www.dfki.de/isreal/close_door.owl#self"));
		graphics.sendAnimationCall("close_door", params);
		return new ArrayList<String>();
	}

}
