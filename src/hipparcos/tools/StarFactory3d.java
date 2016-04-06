// Written by William O'Mullane for the 
// Astrophysics Division of ESTEC  - part of the European Space Agency.

package hipparcos.tools;

import java.io.*;
import java.util.*;
import java.net.URL;

/** For given alpha delta and d get all stars from the database
	in that area. Optional vlim may be given and hipOnly.
*/
public class StarFactory3d extends StarFactory {
  protected double x,y,z,rad;

 public StarFactory3d (double x, double y, double z, double rad) {
	super(99,true);
	this.x = x;
	this.y = y;
	this.z = z;
	this.rad = rad;
	catprogs[0]="shipmainxyz";
	catprogs[1]= null;
 }

 public StarFactory3d ( double x, double y, double z, double rad,
						double vlim ) {
	super(vlim,true);
	this.x = x;
	this.y = y;
	this.z = z;
	this.rad = rad;
	catprogs[0]="shipmainxyz";
	catprogs[1]= null;
 }


	protected boolean loadFromURL() {
      try {
         String theUrl=HIPproperties.getProperty("hipurl");
         if (Constants.verbose > 2) System.out.println("Got property hipurl"+theUrl+".");
		 theUrl+="?noLinks=1&X=";
         theUrl+=new Integer((int)x).toString();
         theUrl+="&Y=";
         theUrl+=new Integer((int)y).toString();
         theUrl+="&Z=";
         theUrl+=new Integer((int)z).toString();
         theUrl+="&radius=";
         theUrl+=new Integer((int)rad).toString();
         theUrl+="&threshold=";
         theUrl+=new Double(vlim).toString();
		 URL url = new URL(theUrl);
		 InputStreamReader istream  = new InputStreamReader(url.openStream());
         dstream = new BufferedReader(istream);
         if (Constants.verbose > 1) System.out.println("opened "+theUrl+".");
      }
      catch (Exception e) {
         System.err.println("loadFromURL3d: " + e);
		 e.printStackTrace();
         return false;
      }
      return true;
  }
    protected boolean loadFromDisk(String catprog) {
        try {
            String bins=HIPproperties.getProperty("bins");
            String cmd =bins+"/"+catprog +" -tq ";
            cmd +=new Integer((int)x).toString() + " ";
            cmd +=new Integer((int)y).toString() + " ";
            cmd +=new Integer((int)z).toString() + " ";
            cmd +=new Integer((int)rad).toString() + " ";
            cmd +=new Double(vlim).toString() + " ";
            if (Constants.verbose > 1) System.out.println("Running "+cmd+".");
            Runtime rt = Runtime.getRuntime();
            Process p = rt.exec(cmd);
            InputStreamReader istream  = new InputStreamReader(p.getInputStream(
));
            dstream = new BufferedReader(istream);
            return true;
        } catch (Exception e) {
            System.err.println("Some problem starting cat progs");
            e.printStackTrace();
            return false;
        }

    }


}
