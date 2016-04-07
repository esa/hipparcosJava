
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

import static org.junit.Assert.*;

import org.junit.Test;

import hipparcos.hipi.Earth;
import junit.framework.Assert;

public class EarthTest {

	@Test
	public void test() {

		  for (int i=0;i <  10; i++) { 
				Double year = new Double(1985+i);
				System.out.println(year + " : "+Earth.where(year.doubleValue()));
			     }
		assertTrue("No problem", true);
	}

}
