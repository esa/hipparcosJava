package hipparcos.hipi;

import hipparcos.plot.*;

import java.io.*;
import java.lang.*;
import hipparcos.tools.*;
class Abcissa {

  public int nrgc;	// IA1
  public String consortia;// IA2
  public double a3;	// IA3
  public double a4;	// IA3
  public double a5;	// IA3
  public double a6;	// IA3
  public double a7;	// IA3
  public double a8;	// IA3
  public double a9;	// IA3
  public double tobs;	// Observation time extracted from rgcData

  public Abcissa (String str) throws BadlyFormatedABstring {
     DelimitedLine line= new DelimitedLine(str,'|');
     try {
        nrgc = line.getNextInt();
        consortia = line.getNextString();
        a3 = line.getNextDouble();
        a4 = line.getNextDouble();
        a5 = line.getNextDouble();
        a6 = line.getNextDouble();
        a7 = line.getNextDouble();
        a8 = line.getNextDouble();
        a9 = line.getNextDouble();
     }
     catch (Exception e) {
         throw( new BadlyFormatedABstring());
     }
  }

 public String toString() {
   String ret = new String ( (new Integer(nrgc)).toString() );
   ret+="|";
   ret+=consortia;
   ret+="|";
   ret+=a3;
   ret+="|";
   ret+=a4;
   ret+="|";
   ret+=a5;
   ret+="|";
   ret+=a6;
   ret+="|";
   ret+=a7;
   ret+="|";
   ret+=a8;
   ret+="|";
   ret+=a9;
   ret+=" tobs "+tobs;
   return (ret);
 }

 public void setTobs(String str) throws BadlyFormatedABstring {
 /** Take aline of rgc data and extract the relevent field 
	depending on wheter FAST or NDAC  */ 
     DelimitedLine line= new DelimitedLine(str,'|');
     int skipCol=0;
     if (consortia.startsWith("N")) { 
	skipCol=4;
      } else { skipCol=1; };
     try {
	for (int i = 0; i<skipCol ; i++) {
	   String skip= line.getNextString();
  	}
	tobs=line.getNextDouble();
     }
     catch (Exception e) {
	 System.err.println(" Bad RGC Data ");
         throw( new BadlyFormatedABstring());
     }

 }
}; 
