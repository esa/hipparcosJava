
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




package hipparcos.hipi.test;

import hipparcos.hipi.*;

public class TestHIPI {

 static public void main(String argv[]) {    
	try {
	HIPI tmp =HIPIFactory.get( new Integer(argv[0]).intValue());
	System.out.println(tmp);
	System.out.println(tmp.getInfoText1());
	} catch (Exception e) {
		
		System.out.println( "Not Found" );
	}
 }
 
}
