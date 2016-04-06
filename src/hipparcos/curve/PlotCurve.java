package hipparcos.curve;

import java.awt.*;
import java.io.*;
import java.util.*;

public class PlotCurve extends Canvas {

  private HIP_EP ep=null;
  private double period;
  private int rectWidth,rectHeight,border,bottomRule,leftRule,rightRule;
   
  private void init() {
	rectWidth=3;
	rectHeight=3;
	border=25; // space left above highest and below lowest
 	bottomRule=20;
 	rightRule=20;
	leftRule=50;	
	period=50;
 	setBackground(Color.white);
  }

  public PlotCurve () {
	init();
  }
  public void paint (Graphics g) {
     if (ep !=null && ep.getHipNumber()!=0) {
    	plotGraph(g);
     }
     else {
        g.setColor(Color.red);
	g.drawString("Data Not Loaded",20,(int)(this.size().height/2));
     }
  }
  public void plotGraph (Graphics g) {
    EPpoint p;
    double adjustedBJD;
    int x,y,err;

    Dimension d = this.size();
    int phaseWidth = (int) d.width  - leftRule - rightRule ;
    //2/3 cause we plot 1.5 period
    phaseWidth = (int) (phaseWidth * 2/3) ;

    // The scale for X is the width /  period  
    double scaleX= period /phaseWidth; 
    // The scale for Y is the number of y points /Max -Minvalue .
    double scaleY=(ep.getHpRange())/(d.height -(border*2) - bottomRule); 

    drawRulers(g,scaleY, ep.getMinHp(),ep.getMaxHp(), ep.getMedianMagnitude());

    Vector Epoints = ep.getEPpoints();
    p=(EPpoint)Epoints.elementAt(0);
    for (Enumeration e = Epoints.elements() ; e.hasMoreElements() ;){
	p=(EPpoint)e.nextElement();
	if (p.qualityFlag==0 || p.qualityFlag==1) {
	   // Only plot if qulaty zero or bit 0 set (NDAC data)
	   // X-coordinate is the time less the epoch refference time  
	   // modulo the period 
	   adjustedBJD= (p.BJD- ep.getRefferenceEpoch())%period;
	   if (adjustedBJD < 0)  adjustedBJD = adjustedBJD + period;
	   x= leftRule +(int)(adjustedBJD /scaleX ); 

	   y= border + (int )((p.Hp - ep.getMinHp())/scaleY);
	   err= (int) (p.standardError /scaleY); // size of error bar

	   plotPoint(g,x,y,err,p.qualityFlag);
	   if ((x + phaseWidth) < (d.width -rightRule)){
	      plotPoint(g,x+phaseWidth,y,err,p.qualityFlag);
	   }
	   if (y > d.height -border) {
	     System.err.println("Height " +d.height +" y "+y+" p.Hp " + p.Hp );
	   }
	   if (x < leftRule || x > (d.width-rightRule)) {
	     System.err.println("Left " +leftRule+" x "+x+" p.BJD " + p.BJD );
	   }
	}
    }
  }

private float getYstep(float range) {
     Float ret = new Float(0);
     if (range > 1) {
	ret=new Float((float)0.5);
	};
     if (range < 1 && range > 0.5) {
	ret=new Float((float)0.1); 
	};
     if (range < 0.5 && range > 0.3) {
	ret=new Float((float)0.05);
	};
     if (range < 0.3) {
	ret=new Float((float)0.01);
	};
     return ret.floatValue();
}

private void drawRulers (Graphics g, double scaleY, double minY, 
				double maxY, double median) {
     String ly,hy,st;
     float ystep;
     int cShift=9;
     float lowY,highY,tick;
     int posLowY,posHighY,step;

     float range = (float)(maxY-minY);
     ystep=getYstep(range);
     Float stF= new Float(ystep);
     g.setColor(Color.blue);
     g.drawString("Scale: ",1,12);
     g.drawString(stF.toString(),40,12);

     highY = (float)(maxY - (maxY%ystep)) ;
     //hy = new Float(highY).toString();

     lowY = (float)(minY + ystep - (minY%ystep)) ;
     posHighY= border -(cShift/2) + (int)((maxY-minY)/scaleY);
     posLowY= border -(cShift/2) ;
     Dimension d = this.size();
     int medianLine= border + (int) ((median -minY)/scaleY);

     // X and Y axis
     g.setColor(Color.black);
     g.drawLine (leftRule-1,border,leftRule-1,d.height-bottomRule);
     g.drawLine (leftRule-1,d.height-bottomRule,d.width-rightRule,d.height-bottomRule);

     // Median line
     g.setColor(Color.red);
     g.drawLine (leftRule-1,medianLine,d.width-rightRule,medianLine);
     //g.drawString(""+median,1,medianLine);
     g.setColor(Color.blue);
   
     // lowY highY
     //g.drawString(ly,1,posLowY+cShift);
     //g.drawString(hy,1,posHighY+cShift);
     g.drawString(""+minY,1,posLowY+cShift);
     g.drawString(""+maxY,1,posHighY+cShift);
     // Tick Marks on Y
     tick = lowY;
     float endval = (float)(highY+ystep);
     while (tick <= highY) {
         int posTick= border + (int)((tick-minY)/scaleY);
	 g.drawLine(leftRule-5,posTick, leftRule,posTick);
         //g.drawString(""+tick,1,(posTick+(cShift/2)));
	 tick+=ystep;
     }

     // Phase 
     g.drawString("Phase",(d.width/2 -10),d.height-3);
     int quarterPhase=(d.width-leftRule-rightRule)/6; 
     //labels
     g.drawString("0.0",(leftRule -cShift),d.height-3);
     g.drawString("0.5",(leftRule+(2*quarterPhase) -cShift),d.height-3);
     g.drawString("1.0",(leftRule+(4*quarterPhase) -cShift),d.height-3);
     g.drawString("1.5",(d.width -cShift -rightRule),d.height-3);
     // ticks 
     for (int i=0; i< 7; i++) {
	int x = leftRule + (i*quarterPhase);
	g.drawLine(x,(d.height -bottomRule), x, (d.height -bottomRule +5));
     }
}
private void plotPoint (Graphics g, int x, int y, int err, int quality) {
// Decide which symbol to plot based on quality 
   if (quality==0) {
	plotSquare(g,x,y,err);
   }
   else {
	plotTriangle(g,x,y,err);
   }
}
private void plotSquare (Graphics g, int x, int y, int err) {
// Plot the point and the error bar.
   g.setColor(Color.cyan);
   g.drawLine (x, y-err,x,y+err);// Error bar
   g.setColor(Color.blue);
   g.drawRect (x-((rectWidth-1)/2), y-((rectHeight-1)/2),rectWidth,rectHeight);
   g.fillRect (x-((rectWidth-1)/2), y-((rectHeight-1)/2),rectWidth,rectHeight);
}
private void plotTriangle (Graphics g, int x, int y, int err) {
// Plot the NDAC Data and the error bar.
   int xpoints[] = new int[4];
   int ypoints[] = new int[4];
   //Apex
   xpoints[0] = x;
   ypoints[0] = y- rectHeight;
   //left corner
   xpoints[1] = x -rectWidth +1;
   ypoints[1] = y+ ((rectHeight-1)/2);
   //right corner
   xpoints[2] = x +rectWidth-1;
   ypoints[2] = y+ ((rectHeight-1)/2);
   //Apex
   xpoints[3] = x;
   ypoints[3] = y- rectHeight;

   g.setColor(Color.cyan);
   g.drawLine (x, y-err,x,y+err);// Error bar
   g.setColor(Color.black);
   g.drawPolygon (xpoints, ypoints,4);
}

// Properties
  public void setEp(HIP_EP ep) {
	this.ep = ep;
	if (ep.getSolutionPeriod() != 0) {
	   this.period=ep.getSolutionPeriod();
	} else {
	   this.period=50;
	}
           repaint();
	
  }


  public void setPeriod(double period) {
    if (this.period != period) {
      this.period = period;
      repaint();
    }
  }

  public HIP_EP getEp() {
    return ep;
  }
  public double getPeriod() {
    return period;
  }
}
