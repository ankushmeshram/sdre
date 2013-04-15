package de.dfki.isreal.semantic.services;

import de.dfki.isreal.data.BindingList;
import de.dfki.isreal.data.ServiceWrapper;
import de.dfki.isreal.network.callerfactories.DemonstratorCallerFactory;
import de.dfki.isreal.network.protos.ExchangeDataProtos.DemonstratorMessage;

public abstract class SEServiceImplementationThread extends ServiceImplementationThread {
	
	private boolean prec_checked = false;
	
	// used for demonstrator messages in the GUI
	private boolean logging = true;
	private DemonstratorCallerFactory dem = new DemonstratorCallerFactory();

	public SEServiceImplementationThread(ServiceWrapper service, BindingList b) {
		super(service, b);
	}

	public void run(){
		if (!prec_checked){
			bnd = checkPrecondition();
		}
		if (bnd != null){
			output = serviceImplementation();
			if (logging)
				dem.sendMessage("SE service " + this.getClass().getSimpleName() + " TRIGGERED.",
						DemonstratorMessage.Component.GSE.getNumber(), DemonstratorMessage.DMsgType.EXECUTION.getNumber(), false);
		} else {
			output = null;
			
			if (logging)
				dem.sendMessage("SE " + this.getClass().getSimpleName() + ": precondition check FAILED.",
						DemonstratorMessage.Component.GSE.getNumber(), DemonstratorMessage.DMsgType.EXECUTION.getNumber(), false);
		}
	}
	
	public BindingList checkPreconditionInFront(){
		bnd = checkPrecondition();
		if (bnd != null){
			prec_checked = true;
		}
		return bnd;
	}
	
}
