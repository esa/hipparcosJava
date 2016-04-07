
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

public class Histogram extends Plot {

   private int occurences[];
   private boolean gotOne=false;
   private Color plotcol;

   public Histogram(String xlabel, String ylabel, int low,int high, 
				double step, Color col) {
	setXlabel(xlabel);
	setYlabel(ylabel);
	plotcol=col;
	init (low, high,step);
   }

   public void resetGraph() {
	miny=0;
	maxy=10;
	if (occurences != null) {
	   for (int i = 0; i < occurences.length; i++) {
		occurences[i]=0;
	   };
	};
   }

   public void init(int low,int high, double step) {
	gotOne=false;
	minx=low;
	maxx=high;
	miny=0;
	maxy=10;
	xstep=step;
	numberStep=5;
	/* we will then split the data in to these groups
	   which we can just keep in array only interestedin how many
	*/
	Double nogrps= new Double ((high - low)/ step);
	occurences=new int[nogrps.intValue()];
   } 

   public void addOccurence (double o) {
	gotOne=true;
	int which=0;
	double category=minx+xstep;
	while ((category < o) && (category < maxx)) {
		category = category+xstep;
		which++;
	}
	//System.out.println("o "+o+" "+occurences.length);
	while  (which >= occurences.length) which--; // off the end
	occurences[which]++; //add one to ocurences for this);
	// which may cause us to have to many !!
	if (occurences[which] > maxy) {
	   maxy=occurences[which];
	   ystep=-1;
	}
	repaint();
		
   }

   public void plotGraph(Graphics g) {
      if (gotOne) {
        g.setColor(plotcol);
	double categ=minx;
	for (int i=0; i< occurences.length; i++ ) {
	    plotBar(categ,occurences[i],g);
	    categ+=xstep;
	}	
      } else {
        g.setColor(Color.red);
        g.drawString(" No data",30,(int)(this.getSize().height/2));
      };
   }
 
   public void plotBar(double cat, int height, Graphics g){
	int x = calcX(cat);
	int w = calcX(cat+xstep) - x;
	int y = calcY(height);
	int h = calcY(miny) -y ;
	g.fillRect(x,y,w,h);
	//System.out.println("x "+x+" y "+y+" w "+w+" h "+h);
   }
}
