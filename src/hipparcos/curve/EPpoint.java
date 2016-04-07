
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
package hipparcos.curve;
import hipparcos.tools.*;

class EPpoint {

  public double BJD;
  public double  Hp;
  public double  standardError;
  public int  qualityFlag;

  public EPpoint (String str) throws BadlyFormatedEPstring {
     DelimitedLine line= new DelimitedLine(str,'|');
     try {
        BJD = line.getNextDouble();
        Hp = line.getNextDouble();
        standardError = line.getNextDouble();
        qualityFlag = line.getNextInt();
     }
     catch (Exception e) {
         throw( new BadlyFormatedEPstring());
     }
  }

 public String toString() {
   String ret = new String ( (new Double(BJD)).toString() );
   ret+=" ";
   ret+=Hp;
   ret+=" ";
   ret+=standardError;
   ret+=" ";
   ret+=qualityFlag;
   return (ret);
 }

}; 
