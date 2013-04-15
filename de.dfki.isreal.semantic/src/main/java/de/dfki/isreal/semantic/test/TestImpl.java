package de.dfki.isreal.semantic.test;

import de.dfki.isreal.helpers.Profiler;
import de.dfki.isreal.semantic.impl.GlobalSESControllerImpl;

public class TestImpl {
	
	private static GlobalSESControllerImpl gseInst = null;
	
	public static void init(){
		Profiler.init();
		gseInst =  new GlobalSESControllerImpl("C:\\Users\\anme05\\git\\icmwindsem\\icmwind\\gse_config\\sab_2011.isrealomsconfig");
		System.out.println(gseInst.checkKBConsistency());
	}
}
