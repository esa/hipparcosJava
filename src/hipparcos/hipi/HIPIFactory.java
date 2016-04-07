
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

package hipparcos.hipi;

import java.io.*;
import java.util.*;
import java.net.URL;

import hipparcos.tools.*;

/** Get a HIPI from web or disk
*/
public class HIPIFactory extends Factory {
 
 public static HIPI get(int hipid) throws Exception{
	HIPIFactory fac = new HIPIFactory(hipid);
	HIPI h= (HIPI)fac.getObj();
	return h;
 }

 public HIPIFactory (int hipid) {
	super(hipid,"shipi","hipiId");
 }

	/* Seems ther eis sometimes a blanck after Pre - skip toData is not enogh*/
	protected void skipToHead() throws Exception {
		String skip="";
		while ( ! skip.startsWith("IH1   |IH2") ){
			skip=dstream.readLine();
			if (Constants.verbose > 5)  System.out.println("SkipToHead:"+skip);
		}
	}

	/* Parse the data to make the object */
	public Object getObj() throws Exception {
		String str,skip;
		HIPI hipi = new HIPI();
		skipToHead();
		str=dstream.readLine(); // The data line
		// The line with the header information on it
		if (Constants.verbose > 5) System.out.println("Split:"+str);
		DelimitedLine hinfo=new DelimitedLine(str,'|');
		try {
			hipi.ihip = hinfo.getNextInt();
			hipi.mag = hinfo.getNextDouble();
			hipi.radeg = hinfo.getNextDouble();
			hipi.decdeg = hinfo.getNextDouble();
			hipi.par = hinfo.getNextDouble();
			hipi.pma = hinfo.getNextDouble();
			hipi.pmd = hinfo.getNextDouble();
			hipi.code = hinfo.getNextString();
			hipi.nobs = hinfo.getNextInt();
			hipi.initVals();
		}
		catch (Exception e) {
			System.err.println("Bad Header Line ?");
			e.printStackTrace();
		}

		try {
		    // Now read the Abcissae
			if (Constants.verbose > 5) System.out.println("Reading abcissae");
			str=dstream.readLine();// Header line - skip it
			if (Constants.verbose > 5) System.out.println("skip:"+str);
			boolean first =true;
			boolean finished=false;
			str=dstream.readLine() ;
			while ((str=dstream.readLine())!=null) {
				try {
			 		if (Constants.verbose > 5) System.out.println("parsing "+str);
					Abcissa a = new Abcissa(str);
					a.setTobs(dstream.readLine());
					dstream.readLine(); // There is always a blank line
					hipi.abscissae.addElement(a);
       			} catch (BadlyFormatedABstring e) {
					System.err.println("Discarded "+str+" Not an EP point"+e);
        		}
			} /* close while loop */
        }      catch (IOException e) {
           System.err.println("can not load object: InputStream Error  " + e);
        }

        if (Constants.verbose > 3) System.out.println("Loaded - "+hipi.ihip);
      return hipi;
	}

}
