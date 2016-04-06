// Written by William O'Mullane for the
// Astrophysics Division of ESTEC  - part of the European Space Agency.

package hipparcos.sky;

import java.lang.Math;

class Mag {
   private double mag;
   static private double c=0.105;
   static private int factor=6;
   static private int maxPix=40;
   static private int minPix=40;

   private int pixelDiameter=0;
  
   public Mag(double mag) {
      this.mag =mag; 
      calculatePixelDiameter();
   }

   private void calculatePixelDiameter(){
	pixelDiameter= (int)Math.ceil(factor*Math.pow((double)10,(c*(9.3-mag))));
	if (pixelDiameter < 2) pixelDiameter=2;
	if (pixelDiameter > 40) pixelDiameter=40;

   }

   public double getMag (){ return mag ; };
   public int getPixelDiameter(){ 
      return pixelDiameter ;
   };

   public String toString() {
	return ((new Double(mag).toString()) );
   }

   static public boolean setFactor(int f) {
	Mag.factor=f;
	return true;
   }
   static public int getFactor() {
	return Mag.factor;
   }
   static public boolean setConstant(double c) {
	Mag.c=c;
	return true;
   }
   static public double getConstant() {
	return Mag.c;
   }
}
