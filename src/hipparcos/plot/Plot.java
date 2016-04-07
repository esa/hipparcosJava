
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
import java.io.*;
import java.util.*;

/** Plot just with axes and scale as base class for other plots
    like Histogram  **/
public class Plot extends Canvas {

  double scaleX,scaleY; 
  public double minx,miny,maxx,maxy;
  public int topRule,bottomRule,leftRule,rightRule,dheight;
  public  int ystep=-1,numberStep=5,ynumberStep=5;
  public double xstep=-1; 
  public int pointSize=4;
  private String botText,leftText;
  private boolean inited=false;

  private void init() {
	topRule=20;
 	bottomRule=30;
 	rightRule=20;
	leftRule=25;	
 	setBackground(Color.white);
	minx=0;
	maxx=10;
	miny=0;
	maxy=10;
	botText=new String ("X axis");
	leftText=new String ("Y axis");
	inited=true;
  }

  public Plot () {
	init();
  }

  public void setScales() {
    Dimension d = this.getSize();
    int gWidth = (int) d.width  - leftRule - rightRule ;
    int gHeight = (int) d.height -  bottomRule -topRule;
    dheight=gHeight;
    scaleX= (maxx - minx)/gWidth;
    scaleY= (maxy - miny)/gHeight;
    if (xstep<=-1) {xstep=getStep(scaleX); numberStep=getNumberStep(scaleX);}
    if (ystep==-1) {ystep=getStep(scaleY); ynumberStep=getNumberStep(scaleY);}
    //System.out.println (" ScaleX "+scaleX+" ScaleY "+scaleY +" Height "+dheight);
  }

  public int getStep(double scale) {
  /** how often to put a tick mark depending on scale */
     int ret=1;
     if (scale <= 2 ) {ret=50;  };
     if (scale <= 1 ) {ret=10 ;};
     if (scale < .1 ) {ret=1 ;};
     return ret;
  }

 public int getNumberStep(double scale) {
     int ret=1;
     if (scale <= 2 ) {ret=50;  };
     if (scale <= 1 ) {ret=10 ; };
     if (scale < .1 ) {ret=5 ;};
     return ret;
 }
  public void paint (Graphics g) {

	if (!inited) init();
	drawRulers(g);
	plotGraph(g);
  }

public void plotGraph( Graphics g) {
/** Override this one in subclasses to do your thing*/
	g.setColor(Color.red);
	g.drawString(" Not Implemented",30,(int)(this.getSize().height/2));
}
  
public int calcX(double d) {
	Double ret=new Double(leftRule+((d - minx)/scaleX));
	return ( ret.intValue() );
} 
public int calcY(double d) {
	Double ret=new Double((d- miny)/scaleY);
	return ( topRule + dheight - ret.intValue() );
} 

private void drawRulers (Graphics g)  {
     String ly,hy,st;
     int cShift=9;
     int highY,lowY,lowX,highX,tick;
     double dtick;
     int posLowY,posHighY,step,posLowX,posHighX;

     setScales();

     Float stF;
   // ystuff
     stF = new Float(maxy + (maxy%ystep));
     highY =  stF.intValue();
     stF = new Float(miny - (miny%ystep));
     lowY = stF.intValue();
     posHighY= calcY(highY) + (cShift/2);
     posLowY= calcY(lowY) + (cShift/2);
//System.out.println("Min Y "+miny+" lowY "+lowY +" PoslowY "+posLowY);
//System.out.println("Max Y "+maxy+" highY "+highY +" posHighY "+posHighY);
    // x stuff
     stF = new Float(maxx - (maxx%xstep));
     highX = stF.intValue();
     stF = new Float(minx  - (minx%xstep));
     lowX = stF.intValue();
     posHighX=  calcX(highX) -(cShift/2);
     posLowX= calcX(lowX) - cShift;
     // X and Y axis and Scale (Netscape seems to have a big problem
     // with strings hence the rather over kill appraoch here
     g.setColor(Color.black);
     g.drawString(leftText,1,12);
     Dimension d = this.getSize();
     Double center= new Double ((d.width/2) - ((botText.length()/2)*7));
     g.drawString(botText,center.intValue(),d.height - (cShift/2));
     g.drawLine (leftRule-1,topRule,leftRule-1,d.height-bottomRule);
     g.drawLine (leftRule-1,d.height-bottomRule,d.width-rightRule,d.height-bottomRule);

     tick = lowY - (lowY%ystep);
     int endTick = highY - (highY%ystep); 
     while (tick <= endTick) {
         int posTick= calcY(tick);
	 g.drawLine(leftRule-5,posTick, leftRule,posTick);
	 if (tick%ynumberStep==0) g.drawString(""+tick,3,(posTick+(cShift/2)));
	 tick+=ystep;
     }

     // ticks on X
     dtick = lowX - (lowX%xstep);
     endTick = (int) (highX - (highX%xstep)); 
     while (dtick <= endTick) {
         int posTick= calcX(dtick);
	 g.drawLine(posTick,(d.height -bottomRule), posTick, (d.height -bottomRule +5));
	 if (dtick%numberStep==0) {
	     Double t = new Double (dtick);
	     g.drawString(""+t.intValue(),(posTick-(cShift/2)),(d.height - 14));
	 }
	 dtick+=xstep;
     }
}

public void setXlabel(String l) {
   botText=l;
}
public void setYlabel(String l) {
   leftText=l;
}

   public void plotPoint(double rx, double ry, Graphics g){
        int x = calcX(rx);
        int y = calcY(ry);
	//System.out.println("Plotting "+x+", "+y);
	Double offd = new Double(pointSize/2);
	int off = offd.intValue();
	g.fillOval((x-off),(y-off),pointSize,pointSize);
   }

   public void plotPoint(DPoint p, Graphics g){
        plotPoint(p.getX(),p.getY(),g);
   }

}
