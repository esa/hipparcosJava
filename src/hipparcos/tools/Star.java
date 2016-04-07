
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

package hipparcos.tools;
import java.text.*;
import java.awt.*;
import java.net.*;

/** A class to represent one entry in the hipparcos catalog
	not all fields are put in here just the ones used elsewhere
	could easily add them all but then chewing up memory
*/
public class Star {

  public static DecimalFormat lform = new DecimalFormat("000.#########");
  public static DecimalFormat decform = new DecimalFormat(" 00.#########;-00.########");
  public static DecimalFormat sform = new DecimalFormat("00.###;-0.###");
  public static DecimalFormat mform = new DecimalFormat(" 000.###;-000.###");
  public String type; // H0 or T0 contains H or T
  public String id; // H1 ot T1
  public double  mag; // H5
  public double alpha; // H8
  public double  delta; /// H9
  public double  paralax; // H11
  public double muAlpha; //H12 
  public double muDelta; //H13 
  public double b_v; //H37 
  public boolean inHIPnTYC=false;
  public boolean inHIP=false;

  public Star () {
	id=null;
  }
	/* copy constructor */
  public Star (Star copy) {
	type =copy.type;
	id =copy.id;
	mag =copy.mag;
	alpha =copy.alpha;
	delta =copy.delta;
	paralax =copy.paralax;
	muAlpha =copy.muAlpha;
	muDelta =copy.muDelta;
	inHIPnTYC =copy.inHIPnTYC;
	inHIP =copy.inHIP;
	b_v= copy.b_v;
  }

  /** COntruct the star from the delimetd line as it appears in the catalog*/
  public Star (String str) throws Exception {
     DelimitedLine line= new DelimitedLine(str,'|');
	//System.out.println("Got - "+str);
	String skip;
	type = line.getNextString();
	id   = line.getNextString();
  	skip = line.getNextString();	//H2
  	skip = line.getNextString();	//H3
  	skip = line.getNextString();	//H4
    mag  = line.getNextDouble();    //H5
  	skip = line.getNextString();	//H6
  	skip = line.getNextString();	//H7
    alpha = line.getNextDouble();
    delta = line.getNextDouble();
  	skip=line.getNextString();	//H10
  	paralax=line.getNextDouble();	//H11
	try {
           muAlpha = line.getNextDouble();
	} catch (Exception e) {};
	try {
           muDelta = line.getNextDouble(); //h13
	} catch (Exception e) {};

	int i=13;
	if (type.startsWith("T")) {// get T31 
	   try {
	      for (i=13; i<30; i++) {
			try {
		    	skip=line.getNextString();
			} catch (Exception e) {
				System.out.println ("Skip Failed "+ e);
		    }
	      }
		  i++;
	      String tmpStr=line.getNextString(); //extractHipNo from T31
	      int tmp= new Integer(tmpStr).intValue(); 
	      inHIPnTYC=(tmp >=1);
	   } catch (Exception e) { 
	      //System.out.println ("Convert Failed "+ e);
	      inHIPnTYC=false;
	   }
	} else { inHIP=true; };
	// skip to H37  b-v
	while (i <  36) {
         try {
            skip=line.getNextString();
			i++;
         } catch (Exception e) {
            System.out.println ("Skip Failed "+ e);
         }
    }
    b_v = line.getNextDouble(); // H37

  }

 public String shortInfo() {
   String ret = null;
   if (type.startsWith("T")) {
      ret = new String ( "TYC"+id );
   }
   else {
      ret = new String ( "HIP"+id );
   }
   ret+=" V=";
   ret+=mag;
   return (ret);
 }
  public static String header() { 
		return "HIP/TYC        |Vmag |alpha       |delta    |parallax|mu_alpha|mu_delta|B-V";
  }
 public String toString() {
   String ret = null;
   if (type.startsWith("T")) {
      ret = new String ( "TYC"+id );
   }
   else {
      ret = new String ( "HIP"+id );
   }
	ret=buf(ret,15);
   ret+="|";
   ret+=buf(sform.format(mag),5);
   ret+="|";
   ret+=buf(lform.format(alpha),12);
   ret+="|";
   ret+=buf(decform.format(delta),12);
   ret+="|";
   ret+=buf(mform.format(paralax),7);
   ret+="|";
   ret+=buf(mform.format(muAlpha),7);
   ret+="|";
   ret+=buf(mform.format(muDelta),7);
   ret+="|";
   ret+=sform.format(b_v);
   return (ret);
 }
	public String buf(String in,int len) {
		String ret = in;
		while (ret.length() < len) ret +=" ";
		return ret;
	}
   public double getMag() {
      return mag;
   }
   public double getAlpha() {
      return alpha;
   }
   public double getDelta() {
      return delta;
   }
   public double getParalax() {
      return paralax;
   }
   public double getMuAlpha() {
      return muAlpha;
   }
   public double getMuDelta() {
      return muDelta;
   }
   public double getB_V() {
      return b_v;
   }
   public String getId() {
      return id;
   }
   public String getType() {
      return type;
   }
   public boolean inHIP() {
      return inHIP;
   }

   public URL makeUrl () {
        String theUrl=HIPproperties.getProperty("hipurl")+"?";

      try{
        if (getType().startsWith("T")) {
	   DelimitedLine tycnos = new DelimitedLine(id, ' ');
	   theUrl+="tyc1="+tycnos.getNextInt();
	   theUrl+="&tyc2="+tycnos.getNextInt();
	   theUrl+="&tyc3="+tycnos.getNextInt();
        }
        else{
           theUrl+="hipId="+getId();
        }
        URL url=new URL(theUrl);
        return url;
      } catch (Exception e) {
        System.err.println(e);
      }
        return null;
   }

	public double getMuAlpha(int years) {
	   return (years*((muAlpha)/3600000));
	}

	public double getMuDelta(int years) {
	   return (years*((muDelta)/3600000));
	}

}; 
