// Written by William O'Mullane for the 
// Astrophysics Division of ESTEC  - part of the European Space Agency.
package hipparcos.tools;
import java.io.*;
import java.lang.*;
import java.util.*;
/** Wraps up a line in s Stringtokenizeer using a given delimiter 
	provide usefull casting methods to get back doubles etc. */
public class DelimitedLine {
   protected StringTokenizer line;

   public DelimitedLine(String line, char delim) {	
      this.line = new StringTokenizer(line,new String(delim+""));
   }
   protected String getNext() throws Exception{	
		return (line.nextToken().trim());
   }
   public String getNextString() throws Exception{	
     return (getNext());
   }

   public int getNextInt() throws Exception{	
     return (new Integer(getNext()).intValue());
   }

   public double  getNextDouble() throws Exception{	
	String tmp = getNext();
	if (tmp.length() == 0) {
	   //System.err.println ("Not a valid double|"+tmp+".");
	   throw (new Exception ());
	}
     return (new Double(tmp).doubleValue());
   }

	/** parse the string as a set of doubles and return it in an array */
   public double[] getDoubleArray()  throws Exception{
		if (line.countTokens() == 0 ) 
			throw new Exception("Empty String for array");
		double[] dnums = new double[line.countTokens()];
		for (int t=0; t< dnums.length; t++ )  {
			dnums[t] = getNextDouble();
		}
		return dnums;
	}
	/** parse the string as a set of ints and return it in an array */
   public int[] getIntArray()  throws Exception{
		if (line.countTokens() == 0 ) 
			throw new Exception("Empty String for array");
		int[] inums = new int[line.countTokens()];
		for (int t=0; t< inums.length; t++ )  {
			inums[t] = getNextInt();
		}
		return inums;
	}
}
   
