// Written by William O'Mullane for the 
// Astrophysics Division of ESTEC  - part of the European Space Agency.

package hipparcos.tools;

import java.io.*;
import java.util.*;
import java.net.URL;

/** For given alpha delta and d get all stars from the database
	in that area. Optional vlim may be given and hipOnly.
*/
public class StarFactory  {
  protected boolean hipOnly=false;
  protected double alpha=0;
  protected double delta=0;
  protected double vlim=99;
  protected double d=0;
  protected BufferedReader dstream=null;
  protected boolean finished=false;
  protected boolean hipDone=false;
  protected boolean opened=false;
  protected boolean disk=false;
  protected String[] catprogs = {"shipmainra","stycmainra"};


 protected StarFactory(double vlim, boolean hipOnly) {
	 this.vlim=vlim;
	this.hipOnly = hipOnly;
 } ;

 public StarFactory (double alpha, double delta, double d) {
	this.alpha = alpha;
	this.delta = delta;
	this.d = d;
	finished = ! openStream();
 }

 public StarFactory ( double alpha, double delta, double d, 
						double vlim, boolean hipOnly ) {
   this.hipOnly = hipOnly;
	this.alpha = alpha;
	this.delta = delta;
	this.d = d;
	this.vlim = vlim;
	finished = ! openStream();
 }

   protected boolean openStream() {
	  opened=true;
	  finished=false;
	  disk = HIPproperties.getProperty("datasource","www").startsWith("disk");
	  if (disk) {
		return loadFromDisk(catprogs[0]);
	  } else {
	  if (loadFromURL()) {
		try{
		 skipToData();
		 return true;
		} catch (Exception e) {
		  System.err.println("openStream: could not skip to data " +e );
		  e.printStackTrace();
		}
	  } }
	  finished=true;
	  return false;
   }

	protected boolean loadFromDisk(String catprog) {
		try {
			String bins=HIPproperties.getProperty("bins");
			String cmd =bins+"/"+catprog +" -tq ";
			cmd +=new Double(alpha).toString()+ " ";
			cmd +=new Double(delta).toString()+ " ";
			cmd +=new Double(d).toString() ;
			if (Constants.verbose > 1) System.out.println("Running "+cmd+".");
			Runtime rt = Runtime.getRuntime();
			Process p = rt.exec(cmd);
			InputStreamReader istream  = new InputStreamReader(p.getInputStream());
			dstream = new BufferedReader(istream);
			return true;
		} catch (Exception e) {
			System.err.println("Some problem starting cat progs");
			e.printStackTrace();
			return false;
		}
	
	}
	protected boolean loadFromURL() {
      try {
         String theUrl=HIPproperties.getProperty("hipurl");
         if (Constants.verbose > 2) System.out.println("Got property hipurl"+theUrl+".");
		 theUrl+="?noLinks=1&raDecim=";
         theUrl+=new Double(alpha).toString();
         theUrl+="&decDecim=";
         theUrl+=new Double(delta).toString();
         theUrl+="&box=";
         theUrl+=new Double(d).toString();
		 URL url = new URL(theUrl);
		 InputStreamReader istream  = new InputStreamReader(url.openStream());
         dstream = new BufferedReader(istream);
         if (Constants.verbose > 1) System.out.println("opened "+theUrl+".");
      }
      catch (Exception e) {
         System.err.println("loadFromURL: " + e);
         return false;
      }
      return true;
  }

	/** Skip down to next PRE tag in html stream */
	protected void skipToData() throws Exception {
		if (Constants.verbose > 3) System.out.println("Skipping to data ..") ;
		String str;
        boolean found=false;
        while (!found ) { // skip to pre
           str=dstream.readLine();
           found = (str == null) || str.startsWith("<pre>")
                                 || str.startsWith("<PRE>");
        }
        str=dstream.readLine(); // skip header
	}

	/** keep getting stars until none left */
	public Star getNext() throws NoMoreStars {
		if (!opened) openStream();
		if (finished) throw new NoMoreStars();
        boolean found=false;
		String str;
		Star star=null;
        while (!found ) {
			str=null;
			try {
				str=dstream.readLine();
				if (Constants.verbose > 5 ) System.out.println(str);
			} catch (Exception e) { 
				if (Constants.verbose > 5 ) 
				 	e.printStackTrace();
			};
			if (str==null) {
				if (disk && catprogs[1]!=null && !hipDone && !hipOnly) { 
					loadFromDisk(catprogs[1]);
					hipDone=true;
					try {
						str=dstream.readLine();
					} catch (Exception tyce) {
						tyce.printStackTrace();
						throw new NoMoreStars();
					}
				} else {
					finished = true;
					try {
						dstream.close();
					} catch (Exception e) {};
					throw new NoMoreStars();
				}
			} else {
				if (str.startsWith("</pre>") || str.startsWith("</PRE>")) {
					if (hipDone || hipOnly ) {
						finished=true;
						try {
							dstream.close();
						} catch (Exception e) {};
					    throw new NoMoreStars();
					} else {
						try {
							skipToData();
							str=dstream.readLine();
						} catch (Exception e){
							System.err.println("StarFactory:header "+e);
							throw new NoMoreStars();
						}
						hipDone=true;
					}
				} 
			}
            try {
                star= new Star(str);
                found=true;
            } catch (Exception e) {
               if (Constants.verbose > 3)  
					System.err.println("getNext: Unusable Star "+e);
            }
        }
        if (Constants.verbose > 3)  
		   System.out.println("got: "+star);
		return star;
	}

}
