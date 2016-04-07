
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

import java.awt.*;
import java.lang.*;
import java.util.*;


/** Same as a simple plot but take in a phase */
public class PhasePlot extends SimplePlot {

   private double phase;

   public PhasePlot(String xlabel, String ylabel, double lowY, double highY, Color col) {
	super (xlabel,ylabel,0,1.5,lowY,highY,col);
	xstep=0.1;
	numberStep=1;
  	phase=0.5;
   }

   public void resetGraph() {
	super.resetGraph();
	xstep=0.5;
	numberStep=1;
   }

   public void plotGraph(Graphics g) {
      if (gotOne) {
	if (plotZeroLine) plotZero(g);
        g.setColor(plotcol);
        for (Enumeration e = points.elements() ; e.hasMoreElements() ;) {
                DPoint op = (DPoint)e.nextElement() ;
                DPoint p = new DPoint(op) ;
		double x = ((p.getX()%phase) *(1/phase));
		p.setX(x);
		plotPoint(p,g);
		if (p.getX() <= 0.5) { // plot some points again on the end
		   p.setX(p.getX()+1);
		   plotPoint(p,g);
		}
        }

      } else {
        g.setColor(Color.red);
        g.drawString(" No data",30,(int)(this.getSize().height/2));
      };
   }

   public void setPhase(double phase) {
      this.phase = phase;
      repaint();
  } 

  public double getPhase() {
	return phase;
  }
}
