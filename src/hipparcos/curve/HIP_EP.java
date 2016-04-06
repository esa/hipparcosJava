package hipparcos.curve;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.net.URL;

/* class representing one HIP_EP entry */
public class HIP_EP {

  public Vector Epoints;
  public int	hipNumber;
  public double medianMagnitude;
  public double standardErrorMedianMagnitude;
  public double V_I;
  public double solutionPeriod;
  public double sPeriodLit;
  public double refferenceEpoch;
  public double maxError,minHp,maxHp,fifth,nintyfifth;
  public String variabilityType,component;
  public String gap=new String("       ");
   
  private void init() {
	this.hipNumber=0;
        Epoints= new Vector();
  };

  public HIP_EP () {
	init();
  }

  public String getInfoText() {
     if (hipNumber!=0) {
        String ret = new String ("HIP ");
        ret +=hipNumber + gap;
        ret +="Ref. Epoch: " + refferenceEpoch +gap;
        ret +="Ref. Period(days): "+ solutionPeriod +gap;
 	ret +="Med. Mag.: "+medianMagnitude+gap; 
 	ret += "5th %tile: "+fifth+gap;
	ret +="95th %tile: "+nintyfifth; 
        return ret;
     }
     else
	return "No Data";
  };

  public String getInfoText1() {
     if (hipNumber!=0) {
        String ret = new String ("HIP ");
        ret +=hipNumber + gap +gap;
        ret +="Reference Epoch: " + refferenceEpoch +gap ;
        ret +="Reference Period(days): "+ solutionPeriod +gap ;
        ret +="Literature Period(days): "+ sPeriodLit ;
        return ret;
     }
     else
	return "No Data";
  }
  public String getInfoText2() {
     if (hipNumber!=0) {
        String ret = new String ("");
 	ret +="Median Magnitude(red line): "+medianMagnitude+gap+gap; 
 	ret += "5th Percentile: "+fifth+gap+gap;
	ret +="95th Percentile: "+nintyfifth; 
        return ret;
     }
     else
	return "";
   }
  public String toString() {
     String ret = new String ("HIP ");
     ret +=hipNumber + " SolvedPeriod:" + solutionPeriod +"\n";
     for (Enumeration e = Epoints.elements() ; e.hasMoreElements() ;) {
                ret += e.nextElement() +"\n";
            }
     return ret;
  } ;
// Properties

  public double getMaxError() {
    return maxError;
  }
  public double getMinHp() {
    return minHp;
  }
  public double getMaxHp() {
    return maxHp;
  }
  public double getHpRange() {
    return (maxHp-minHp);
  }
  public double getSolutionPeriod() {
    if (solutionPeriod > 0 ) 
       return solutionPeriod;
    return sPeriodLit;
  }
  public double getMedianMagnitude() {
    return medianMagnitude;
  }
  public double getRefferenceEpoch() {
    return refferenceEpoch;
  }
  public int getHipNumber() {
    return hipNumber;
  }
  public Vector getEPpoints() {
    return Epoints;
  }

}
