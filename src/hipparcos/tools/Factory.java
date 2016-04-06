// Written by William O'Mullane for the 
// Astrophysics Division of ESTEC  - part of the European Space Agency.

package hipparcos.tools;

import java.io.*;
import java.util.*;
import java.net.URL;

import hipparcos.tools.*;

/** Get a HIPI from web or disk
*/
public abstract class Factory {
   protected BufferedReader dstream=null;
   static protected boolean disk=HIPproperties.getProperty("datasource","www").startsWith("disk");
	String catprog;
	String wwwid;
 
 public Factory (int id,String catprog, String wwwid) {
	this.catprog=catprog;
	this.wwwid=wwwid;
    if (disk)
        loadFromDisk(id);
    else {
        loadFromURL(id);
        skipToData();
	}
 }

  public Factory() {};


 /*  only for the html pages skip to PRE */
 protected void skipToData() {
	 String str=null;
      try {
        boolean found=false;
        while (!found ) { // skip to pre in html file
           str=dstream.readLine();
			if (Constants.verbose > 5) System.out.println ("Skip :"+str);
           found = (str == null) || str.startsWith("<pre")
                                 || str.startsWith("<PRE");
        }
	 } catch (Exception e) {};
 }

	/* Open the stream using the WEB */
	public boolean loadFromURL(int id) {
		String theUrl=HIPproperties.getProperty("hipurl");
        if (Constants.verbose > 2) System.out.println("Got property hipurl"+theUrl+".");
        theUrl+="?noLinks=1&tabular=1&";
		theUrl+=wwwid+"="+new Integer(id).toString();
		return openURL(theUrl);
	} 

	public boolean loadFromDisk(int id) {
		String bins=HIPproperties.getProperty("bins");
        String cmd =bins+"/"+catprog +" -ts ";
        cmd +=new Integer((int)id).toString() + " ";
		return openDisk(cmd);
	}

	/** pred should be all keywords for the HIPURL.*/
	protected boolean openURL(String theUrl) {
      try {
         URL url = new URL(theUrl);
         InputStreamReader istream  = new InputStreamReader(url.openStream());
         dstream = new BufferedReader(istream);
         if (Constants.verbose > 1) System.out.println("opened "+theUrl+".");
      }
      catch (Exception e) {
         System.err.println("loadFromURL HIPEP: " + e);
		 e.printStackTrace();
         return false;
      }
      return true;
  }

	/* convenience funtion - cmd should be the progrom plus args */
    protected boolean openDisk(String cmd) {
        try {
            if (Constants.verbose > 1) System.out.println("Running "+cmd+".");
            Runtime rt = Runtime.getRuntime();
            Process p = rt.exec(cmd);
            InputStreamReader istream  = new InputStreamReader(p.getInputStream(
));
            dstream = new BufferedReader(istream);
            return true;
        } catch (Exception e) {
            System.err.println("Some problem with "+cmd);
            e.printStackTrace();
            return false;
        }

    }

	/* Parse the data on dstream to make the object */
	abstract public Object getObj() throws Exception ;

}
