package hipparcos.hipi;

import hipparcos.plot.*;
import hipparcos.tools.Constants;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.net.URL;

/* Represents an entire HIPI entry from the catalogue */
public class HIPI {

  private boolean initVals=false;
  private double ra,dec,sinra,cosra,sindec,cosdec;
  public Vector abscissae;
  public int	ihip;   //IH1
  public double mag;   //IH2
  public double radeg; //IH3
  public double decdeg;//IH4
  public double par;   //IH5
  public double pma;   //IH6
  public double pmd;   //IH7
  public String code;    //IH8
  public int nobs;	//IH9
  private String gap=new String("       ");
   
  public void initVals() {
     initVals=true;
     ra    =radeg/Constants.degc;
     dec   =decdeg/Constants.degc;
     sinra =Math.sin(ra);
     cosra =Math.cos(ra);
     sindec=Math.sin(dec);
     cosdec=Math.cos(dec);
  };

  public HIPI () {
	this.ihip=0;
    abscissae= new Vector();
  }

  public String getInfoText1() {
     if (ihip!=0) {
        String ret = new String ("HIP ");
        ret +=ihip + gap;
        ret +="RA(deg): " + radeg +gap;
        ret +="Dec(deg): "+ decdeg +gap;
	ret +="Magnitude (Hp): " + mag;
        return ret;
     }
     else
	return "No Data";
  };

  public String getInfoText2() {
     if (ihip!=0) {
        String ret = new String (" ");
	ret +="Solution Code: " + code+gap;
 	ret +="par: "+par+gap; 
 	ret += "pma: "+pma+gap;
	ret +="pmd: "+pmd+gap; 
	ret +="nobs(FAST+NDAC): "+nobs; 
        return ret;
     }
     else
	return "No Data";
  };

  public String toString() {
     String ret = new String ("HIP ");
     ret +=ihip + "|" + mag + "|" +radeg+ "|"+decdeg+ "|"+par+ "|"+pma+ "|"+pmd+ "|"+code  +"\n";
     for (Enumeration e = abscissae.elements() ; e.hasMoreElements() ;) {
                ret += e.nextElement() +"\n";
            }
     return ret;
  } ;

// Properties
  public double getRa() {
	return ra;
  }

  public double getDec() {
	return dec;
  }
  public double sinRa() {
	return sinra;
  }
  public double sinDec() {
	return sindec;
  }
  public double cosDec() {
	return cosdec;
  }
  public double cosRa() {
	return cosra;
  }
 
  public int getHipNumber() {
      return ihip;
   }
}
