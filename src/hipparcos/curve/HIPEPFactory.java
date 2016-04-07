
/*
* Copyright (C) 1997-2016 European Space Agency
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 2.1 of the License, or (at your option) any later version.
*
* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
*/
// Written by William O'Mullane for the 
// Astrophysics Division of ESTEC  - part of the European Space Agency.

package hipparcos.curve;

import java.io.*;
import java.util.*;
import java.net.URL;

import hipparcos.tools.*;

/** Get a HIP EP from web or disk
*/
public class HIPEPFactory extends Factory{
 
 public static HIP_EP get(int hipid) throws Exception{
	HIPEPFactory fac = new HIPEPFactory(hipid);
	HIP_EP h= (HIP_EP)fac.getObj();
	getLiteraturePeriod(h);
	return h;
 }
 public static HIP_EP get(String fn) throws Exception {
	HIPEPFactory fac = new HIPEPFactory(fn);
	if (Constants.verbose > 4) System.out.println("Made factory for:"+fn);
	HIP_EP t = (HIP_EP)fac.getObj(); 
	if (Constants.verbose > 4) System.out.println(" got:"+t);
	return t;
 }

 public HIPEPFactory (int hipid) {
	super(hipid,"shipep","hipepId");
 }
 public HIPEPFactory (String filename) {
	loadFromFile(filename);
 }

	protected boolean loadFromFile(String fileName) {
	try {
         if (Constants.verbose > 2) System.out.println("Loading "+fileName);
		InputStreamReader istream  = null;
     	if (fileName.startsWith("http") || fileName.startsWith("HTTP")) {
            istream = new InputStreamReader(new URL(fileName).openStream());
     	} else {
            istream = new InputStreamReader(new FileInputStream(fileName));
     	}
		dstream = new BufferedReader(istream);
		return true;
      }
      catch (Exception e) {
         System.err.println("loadEPfromFile: " + e);
     return false;
      }

	}

	/* Parse the data to make the object */
	public Object getObj() throws Exception {
		String str,skip;
		HIP_EP hipep = new HIP_EP();
		str=dstream.readLine(); // Header line - skip it
		if (Constants.verbose > 5) System.out.println("Skip:"+str);
		str=dstream.readLine(); // The data line
		// The line with the header information on it
		if (Constants.verbose > 5) System.out.println("Split:"+str);
		DelimitedLine hinfo=new DelimitedLine(str,'|');
		try {
			hipep.hipNumber=hinfo.getNextInt(); //HH1
			hipep.component=hinfo.getNextString(); //HH2
			hipep.V_I=hinfo.getNextDouble(); //HH3
			skip=hinfo.getNextString(); //Number transits HH4
			skip=hinfo.getNextString(); //Number acepted transits HH5
			hipep.medianMagnitude=hinfo.getNextDouble(); //HH6
			hipep.standardErrorMedianMagnitude=hinfo.getNextDouble(); //HH7
			hipep.fifth=hinfo.getNextDouble(); //HH8
			hipep.nintyfifth=hinfo.getNextDouble(); //HH9
			try {
				hipep.solutionPeriod=hinfo.getNextDouble(); //HH10
			} catch (Exception e) {
				hipep.solutionPeriod=0;
			}
			try {
		    	hipep.refferenceEpoch=hinfo.getNextDouble(); //HH11
			} catch (Exception e) { hipep.refferenceEpoch = 0;};
			//leaving out HH12 HH13 and HH14 Annex flags
		}
		catch (Exception e) {
			System.err.println("can not create HIPEP: Bad Header Line " + e);
			e.printStackTrace();
		}

		try {
		    // Now read the individual transits
			if (Constants.verbose > 5) System.out.println("Reading transits");
			str=dstream.readLine();// Header line - skip it
			boolean first =true;
			boolean finished=false;
			str=dstream.readLine() ;
			while (!finished) {
				try {
			 		if (Constants.verbose > 5) System.out.println("parsing "+str);
					EPpoint p = new EPpoint(str);
				  	if (first) {
						first=false;
						hipep.minHp=p.Hp;
						hipep.maxHp=p.Hp;
						hipep.maxError=p.standardError;
          			} else {
						if (p.Hp > hipep.maxHp) hipep.maxHp = p.Hp;
						if (p.Hp < hipep.minHp) hipep.minHp = p.Hp;
						if (p.standardError > hipep.maxError) hipep.maxError = p.standardError;
		          	}
					hipep.Epoints.addElement(p);
       			} catch (BadlyFormatedEPstring e) {
					if (str != null) {
						if (str.startsWith("</pre") || str.startsWith("</PRE")){
							finished=true;
        				} else {
						   if (Constants.verbose > 2) 
							System.err.println("Discarded "+str+" Not an EP point"+e);
        				}
					}
       			}
				str=dstream.readLine() ;
				finished = finished || (str==null) ;
			} /* close while loop */
        }      catch (IOException e) {
           System.err.println("can not load object: InputStream Error  " + e);
        }

        if (Constants.verbose > 3) System.out.println("Loaded - "+hipep.hipNumber);
      return hipep;

	}

	/* the litreature period can be used for the plot but is
		not included in the data - must get it elsewhere */
	public static void getLiteraturePeriod(HIP_EP hipep) {
		if (hipep.hipNumber > 0){
            //try {
				String hipId = new Integer(hipep.hipNumber).toString();
				if (!disk) {
					hipep.sPeriodLit = getPeriodWWW(hipId);
				} else {
					hipep.sPeriodLit = getPeriodDisk(hipId);
				}
		}
  }

	protected static double getPeriodDisk(String hipid) {
		try {
            String bins=HIPproperties.getProperty("bins");
            String cmd =bins+"/shipva -tq "+hipid;
            if (Constants.verbose > 1) System.out.println("Running "+cmd+".");
            Runtime rt = Runtime.getRuntime();
            Process p = rt.exec(cmd);
            InputStreamReader istream  = new InputStreamReader(p.getInputStream());
			BufferedReader dstream = new BufferedReader(istream);
            String str=dstream.readLine();
        	if ( str.indexOf("not found") == -1) {
				// need P18
				DelimitedLine l = new DelimitedLine(str,'|');
				for (int i = 0; i < 17; i++ ) {
					str=l.getNextString();
				}
           		return (l.getNextDouble()); //P18
			}
       
         } catch (Exception e) {
            System.err.println(" could not getLiteraturePeriod: " + e);
         }
		return 0;
    }

	protected static double getPeriodWWW(String hipid) {
		try {
         	String theUrl=HIPproperties.getProperty("hipurl");
			theUrl += "?p18="+hipid;
            InputStreamReader istream  = new InputStreamReader(new URL(theUrl).openStream());
			BufferedReader d= new BufferedReader(istream);
            String str=d.readLine();
        	if ( str.indexOf("not found") == -1)
           		return (new Double(str.trim()).doubleValue()); //P18
       
         } catch (Exception e) {
            System.err.println(" could not getLiteraturePeriod: " + e);
         }
		return 0;
    }


}
