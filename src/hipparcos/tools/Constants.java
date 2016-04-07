
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
package hipparcos.tools;

public class Constants {
public static final double PI = Math.PI;
public static final double t0 = 1991.25;
public static final double cPi = 2.0*Math.atan(1);
public static final double cPr = cPi/180;
public static final double degc=57.29578;
public static final int vlev = 2;
public static final double EPS = 0.0000001;
public static final double c = 0.105; // for Mag graphics
public static int verbose = Integer.parseInt(HIPproperties.getProperty("verbose","1"));
public static final float[] colBands = {0.3f,0.4f,0.7f,1.1f}; // b-v bands
/* this array holds the r,b,g values for the color bands
   Note there is one more than the bands for greater than the last band */
public static final float[][] colours = {	{0.1f,0.5f,0.1f},
											{0.13f,0.36f,0.37f},
											{0.5f,0.15f,0.40f},
											{0.55f,0.1f,0.3f},
											{0.6f,0.1f,0.1f}};
public static float maxBV = 5.46f;
public static float minBV = -0.4f;
}
