
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
package hipparcos.plot;

public class DPoint {
   private double x=0, y=0;
   private boolean valid;
   private boolean semivalid;

   public DPoint(double x, double y) {
	valid=true;
	semivalid=true;
	this.x=x;
	this.y=y;
   }

   public DPoint(DPoint p) {
	valid=true;
	semivalid=true;
	this.x=p.getX();
	this.y=p.getY();
   }

   public DPoint() {
	valid=false;
	semivalid=false;
   }

   public boolean isValid() { return valid; };

   public double getX() {
     return x ;  
// should throw an exception here if !valid ...
   }

   public double getY() {
      return y;
   }

   public void setX(double x) {
      this.x= x;
      semiValidate(); 
   }

   public void setY(double y) {
      this.y= y;
      semiValidate(); 
   }

   private void semiValidate() {
	if (!valid) {
	   valid=semivalid;
	   semivalid= true;
	}
   }

   public String toString() {
	return (""+x+"  "+y);
   }
}
