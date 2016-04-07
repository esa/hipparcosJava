
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

public class VectorPosition {
  private double x,y,z;

   public VectorPosition () {
	x=0;y=0;z=0; 
   }
   public VectorPosition (double x,double y, double z) {
	this.x= x;
	this.y= y;
	this.z= z;
   }

   public double getX() { return x;};
   public double getY() { return y;};
   public double getZ() { return z;};

   public void setX(double x) { this.x= x;};
   public void setY(double z) { this.y= y;};
   public void setZ(double z) { this.z= z;};

   public String toString () {
	return (""+x+" "+y+" "+z);
   }
}
