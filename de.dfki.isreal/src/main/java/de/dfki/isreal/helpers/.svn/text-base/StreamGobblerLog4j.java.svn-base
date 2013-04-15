package de.dfki.isreal.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Since when using the runtime.exec() command (in ISRealSetupInitializer)
 * you have to deal with some pitfalls. The issue is discussed at
 * http://www.javaworld.com/javaworld/jw-12-2000/jw-1229-traps.html
 * One of the pitfalls in handling the input and output streams to the started 
 * process. Therefore this class will redirect and empty them so the process
 * doesn't get stuck.
 *  
 *  @author http://www.javaworld.com/javaworld/jw-12-2000/jw-1229-traps.html
 *	@author paka
 */
public class StreamGobblerLog4j extends Thread {
	
    InputStream	is;
    Level		level;
    Logger		logger;
    
    public StreamGobblerLog4j(InputStream is, Level level, Logger logger) {    	
        this.is		= is;
        this.level	= level;
        this.logger	= logger;
    }
    
    public void run() {
        try {                
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while((line = br.readLine()) != null) {
            	logger.log(level, line);
                Thread.yield();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();  
        }
    }
}
