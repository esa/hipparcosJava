package hipparcos.plot;

import java.awt.*;
import java.lang.*;
import java.util.*;

/* Just plots points on a grpah - nothing fancy. Need to supply the 
   max and min when setting up */
public class SimplePlot extends Plot {

   public Vector points;
   public boolean gotOne=false;
   public Color plotcol;
   public boolean plotZeroLine=true;
   static public Color zeroLineColor=new Color(255,105,255);

   public SimplePlot(String xlabel, String ylabel, double lowX,double highX, 
	double lowY, double highY, Color col) {
	setXlabel(xlabel);
	setYlabel(ylabel);
	plotcol=col;
	init (lowX, highX, lowY, highY);
   }

   public void resetGraph() {
	points=new Vector();
   }

   public void init(double lowX,double highX, double lowY, double highY) {
	gotOne=false;
	minx=lowX;
	maxx=highX;
	miny=lowY;
	maxy=highY;
	xstep=1;
	ystep=1;
	numberStep=1;
	ynumberStep=5;
	points=new Vector();
   } 

   public void addPoint (DPoint p ) {
	gotOne=true;
	if (points != null) points.addElement(p);
	repaint();
   }

   public void plotGraph(Graphics g) {
      if (gotOne && (points != null)) {
	if (plotZeroLine) plotZero(g);
        g.setColor(plotcol);
        for (Enumeration e = points.elements() ; e.hasMoreElements() ;) {
                DPoint p = (DPoint)e.nextElement() ;
		plotPoint(p,g);
        }

      } else {
        g.setColor(Color.red);
        g.drawString(" No data",30,(int)(this.getSize().height/2));
      };
   }

   public void plotZero( Graphics g) {
	int y1= calcY(0);
	int x1= calcX(minx);
	int y2= y1;
	int x2= calcX(maxx);
	int xi= calcX(minx);
	g.setColor(zeroLineColor);
	g.drawLine(x1,y1,x2,y2);
   } 
}
