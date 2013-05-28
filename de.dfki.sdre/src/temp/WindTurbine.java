package temp;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

public class WindTurbine {
	
	Map<String, String> mapWTInfo = new HashMap<>();
	
	Properties info = new Properties();
	
	public void readWindTurbineInfo(String wind_turbine_name) {
		String path = GlobalInitializer.get().getSourceFor(wind_turbine_name);
		
		try {
			info.load(new FileInputStream(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for(Entry<Object, Object> entry : info.entrySet()) {
			mapWTInfo.put(entry.getKey().toString(), entry.getValue().toString());
		}
	}
	
	// TODO make it serializable
	public Map<String, String> getWindTurbineInfo() {
		return mapWTInfo;
	}

}
