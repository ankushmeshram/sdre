package temp;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

public class GlobalInitializer {
	
	private final String wts_path = "WTS.info";
	private final String ont_path = "windturbineonto.owl";
	
	private final static Map<String, String> mapWTS = new HashMap<String, String>();
	private static Properties prop = new Properties();
	
	private static GlobalInitializer INSTANCE;
	
	private GlobalInitializer() {
		
		try {
			prop.load(new FileInputStream(wts_path));
		} catch ( IOException e) {
			e.printStackTrace();
		}
		
		// Since the prop file has standard structure, no need to manipulate it
		// TODO when the rows are empty 
		for(Entry<Object, Object> entry : prop.entrySet()) {
			mapWTS.put(entry.getKey().toString(), entry.getValue().toString());
		}
	}
	
	public static GlobalInitializer get() {
		if(INSTANCE == null)
			INSTANCE = new GlobalInitializer();
		return INSTANCE;
	}
	
	public List<String> listWindTurbines() {
		List<String> listWT = new ArrayList<>();
		
		for(Object wt : prop.keySet())
			listWT.add(wt.toString());
		
		return listWT;
	}
	
	public String getSourceFor(String wt_name) {
		return mapWTS.get(wt_name);
	}
	
	

}
