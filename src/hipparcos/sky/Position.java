
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

package hipparcos.sky;

/** contaner class for Ra and Dec  with convers to HMS */
public class Position {

double ra;
double dec;

   public  Position(double ra, double dec) {
      this.ra=ra;
      this.dec=dec;
   }

   public double getRa() {
      return ra;
   }

   public double getDec() {
      return dec;
   }
   private String stringy(int num) {
	// puts a space in if necessary.
      String ret = new String(""+num);
      if (num < 10) ret= new String ("0"+ret);
      return ret;
   }
   public String HMS() {
	double hoursD=(ra/360)*24;
	int hours= (int)hoursD;
	
	double minsD=(hoursD-hours)*60;
	int mins= (int) minsD ;
	int secs= (int)Math.round((minsD-mins) *60);
	String hoursS = stringy(hours);
	String minsS = stringy(mins);
	String secsS = stringy(secs);
	return new String(" "+hoursS+" "+minsS+" "+secsS);
   }

   public String DMS() {
	String sign=new String("+");
	if (dec < 0) sign= new String ("-");
	int degs= (int)dec;
	double minsD=(dec-degs)*60;
	degs= (int)Math.abs(degs);
	int mins= (int) minsD ;
	int secs= (int)Math.abs(Math.round((minsD-mins) *60));
	mins= (int) Math.abs(mins) ;

	String degsS = stringy(degs);
	String minsS = stringy(mins);
	String secsS = stringy(secs);
	return new String(sign+degsS+" "+minsS+" "+secsS);
   }
}


