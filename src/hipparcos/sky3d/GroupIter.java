
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

package hipparcos.sky3d;

import java.util.*;
import javax.media.j3d.*;

/**
   Iterate over the children of a transform group and return only stars
 */
public class GroupIter implements Iterator {
	protected Object theNext;
	protected Enumeration factory;
	
	public void remove() {};

	public GroupIter(Enumeration f) {
		factory=f;
		preNext();
	}
	
	/**
	   Should be straight forward but actually not that simple. Dont no if there is a next until we get it !
	 */
	public boolean hasNext() {
		return (theNext!=null);
	}
	
	/**
	   Return theNext and getthe next object of possible. Next is always running one object ahead so it can respond to hasNext().
	 */
	public Object next() {
	    if (theNext == null) return null;
	    Star3D ret = (Star3D)theNext;
		preNext();
		return ret.getStar();
	}

	/** Find the next object but just hold on to it. **/
	protected void preNext() {
	    try {
			boolean keepgoing =true;
			while (keepgoing) { 
				theNext=factory.nextElement();
			    if (theNext instanceof TransformGroup) {	
					theNext = ((TransformGroup)theNext).getChild(0); 
					keepgoing = ! (theNext  instanceof Star3D);
				} 
				
			}
		} catch (Exception e) {
			theNext=null;
	    	}
	}
}
