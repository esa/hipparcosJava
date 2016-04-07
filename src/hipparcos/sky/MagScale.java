
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

import java.awt.*;

class MagScale extends Canvas {

   private int mid=11;

   public void init() {
	setBackground(Color.black);
   }
   public MagScale() {
	init();
   }

   public Dimension preferredSize() {
	return new Dimension(400,63);
   }

   public void paint( Graphics g) {
	int i;
	g.setColor(Color.blue);
	g.drawRect(1,1,size().width-2,size().height-2);	
	int offset=10;
	for (i=1; i<=12; i++){
	   Mag mag=new Mag(i);
	   int diameter=mag.getPixelDiameter();
	   g.setColor(Color.white);
	   g.fillOval(offset,mid-((diameter/2)-mid),diameter,diameter);	
	   g.setColor(Color.green);
	   g.drawString(""+i,offset-5+(diameter/2),54);
	   offset= offset+diameter +16;
	   
	}
   }
}
