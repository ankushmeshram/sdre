package de.dfki.isreal.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * Since when using the runtime.exec() command (in ISRealSetupInitializer)
 * you have to deal with some pitfalls. The issue is discussed at
 * http://www.javaworld.com/javaworld/jw-12-2000/jw-1229-traps.html
 * One of the pitfalls in handling the input and output streams to the started 
 * process. Therefore this class will redirect and empty them so the process
 * doesn't get stuck.
 *  
 *  @author http://www.javaworld.com/javaworld/jw-12-2000/jw-1229-traps.html
 *
 */
public class StreamGobbler extends Thread
{
    InputStream is;
    String type;
    OutputStream os;
    
    public StreamGobbler(InputStream is, String type)
    {
        this(is, type, null);
    }
    StreamGobbler(InputStream is, String type, OutputStream redirect)
    {
        this.is = is;
        this.type = type;
        this.os = redirect;
    }
    
    public void run()
    {
        try
        {
            PrintWriter pw = null;
            if (os != null)
                pw = new PrintWriter(os);
                
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line=null;
            while ( (line = br.readLine()) != null)
            {
                if (pw != null)
                    pw.println(line);
                System.out.println(/*type + ">" + */line);
                Thread.yield();
            }
            if (pw != null)
                pw.flush();
        } catch (IOException ioe)
            {
            ioe.printStackTrace();  
            }
    }
}
