
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






package hipparcos.hipi;

import hipparcos.tools.*;

/** things we know about the Earth - ephemeris in this case. */
public class Earth {

  static private double omega = 0.0172021240/86400;
  static private double e     = 0.016714;
  static private double g0    = -0.04128;
  static private double au    = 1.496e+11;
  static private double c[][] = {{.540817e+09, -.334118e+11, -.145868e+12},
                           {-.202315e+10, .133781e+12, -.306652e+11},
			   {-.883589e+09, .580048e+11, -.132951e+11} };

  static double[] cearth (double year) {
/** this is from L Lindegrens code to give the barycentric position of the
    earth in a given year between 1988 an 1993 */

     double ret[] = { 0,0,0 };
     double t,arg,co,si;

     t  = (year-1988.0)*365.25*86400.0;
     arg= omega*t+g0;
     arg= arg + e* Math.sin(arg);
     co = Math.cos(arg);
     si = Math.sin(arg);
     
     for (int i=0; i<3; i++) {
	ret[i]=(c[i][0] + c[i][1]*co + c[i][2]*si)/au;
     }
     return ret;
  } 

  public static VectorPosition where(double year) {
     double posArray[] = cearth(year);
     return (new VectorPosition(posArray[0],posArray[1],posArray[2]));
  }
} 
