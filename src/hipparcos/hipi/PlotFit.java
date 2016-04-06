package hipparcos.hipi;

import hipparcos.plot.*;
import hipparcos.tools.*;

import java.awt.*;
import java.io.*;
import java.util.*;

/* Plots the barycentric and observed lines of motion for HIPI data */
public class PlotFit extends Canvas {
  static double w=5.0 ;// width of line aritrary
  static Color baryColour=new Color(255,155,255);
  private boolean showfast=true,showndac=false;
  double scaleX,scaleY;
  int dheight;
  DPoint min,max;
  DPoint[] bary,norm;
  DPoint baryStart= new DPoint();
  DPoint baryEnd= new DPoint();
  public Bary baryMover=null;

  private HIPI hipi=null;
  private int rectWidth,rectHeight,border,bottomRule,leftRule,rightRule;
   
  public void start () {
	  //System.out.println("Start" + this.getGraphics());
	if (baryMover != null ) baryMover.start(this.getGraphics());

  }

  public void stop () {
	if (baryMover != null ) baryMover.stop();
  }

  private void init() {
	rectWidth=3;
	rectHeight=3;
	border=25; // space left above highest and below lowest
 	bottomRule=20;
 	rightRule=20;
	leftRule=50;	
 	setBackground(Color.white);
	min=new DPoint();
	max=new DPoint();
  }

  public PlotFit () {
	init();
  }

  public void hideFAST() { showfast=false ; plotObservations();};
  public void showFAST() { showfast=true ; plotObservations();};
  public void hideNDAC() { showndac=false ;plotObservations(); };
  public void showNDAC() { showndac=true ;plotObservations(); };

  private void plotObservations() {
     if (hipi !=null && hipi.getHipNumber()!=0) {
	stop();
	repaint();
	start();
     }
  }
  public void paint (Graphics g) {
     if (hipi !=null && hipi.getHipNumber()!=0) {
    	plotGraph(g);
     }
     else {
        g.setColor(Color.red);
	g.drawString("Data Not Loaded",20,(int)(this.getSize().height/2));
     }
  }
  public void plotGraph (Graphics g) {
    Abcissa p;
    double adjustedBJD;
    int x,y,err;

    Dimension d = this.getSize();
    int gWidth = (int) d.width  - leftRule - rightRule ;
    int gHeight = (int) d.height -  bottomRule ;
    dheight=gHeight;

    // The scale for X is the width /  period  
    scaleX= (max.getX() - min.getX())/(gWidth - leftRule);
    // The scale for Y is the number of y points /Max -Minvalue .
    scaleY= (max.getY() - min.getY())/(gHeight -bottomRule);
    if (scaleY > scaleX) scaleX=scaleY;
    else scaleY=scaleX;

    drawRulers(g);

    plotBary(g);
    plotNorm(g);
    plotObservations(g);

  }

   private void  calcBaryCentric() {
         double tmin = 1989.8;
         double tmax = 1993.3;
	 double dt = 0.1;
         Vector vbary=new Vector();
         Vector vnorm=new Vector();

	 for (double t = tmin ; t < (tmax+(0.5*dt)); t+=dt) {
	    VectorPosition earth= Earth.where(t);
	    DPoint b = new DPoint();
	    DPoint n = new DPoint();

	    double pa = earth.getX()*hipi.sinRa() - earth.getY()*hipi.cosRa();
	    double pd = (earth.getX()*hipi.cosRa() +earth.getY()*hipi.sinRa())*hipi.sinDec() - earth.getZ()*hipi.cosDec();

	    b.setX((t-Constants.t0)*hipi.pma);
	    b.setY((t-Constants.t0)*hipi.pmd);
	    n.setX(b.getX() + (hipi.par*pa));
	    n.setY(b.getY() + (hipi.par*pd));

	    vbary.addElement(b); // keep thesse in the Vectors
	    vnorm.addElement(n);

	    //System.out.println(" Time "+t+" "+b+" "+n);
	   
            if (!min.isValid() || (min.getX() > n.getX())) { 
		min.setX(n.getX());
	    } ;
            if (!min.isValid() || (min.getY() > n.getY())) { 
		min.setY(n.getY());
	    } ;
            if (!max.isValid() || (max.getX() < n.getX())) { 
		max.setX(n.getX());
	    } ;
            if (!max.isValid() || (max.getY() < n.getY())) { 
		max.setY(n.getY());
	    } ;
	 }
	min.setX(min.getX()-20);
	min.setY(min.getY()-20);
	max.setX(max.getX()+20);
	max.setY(max.getY()+20);
    	//System.out.println ("Max "+max+"   Min "+min);
	// put the vectors in arrays which are better
	bary= new DPoint[vbary.size()];
	int i=0;
	for (Enumeration e = vbary.elements() ; e.hasMoreElements() ;) {
		bary[i] = (DPoint)e.nextElement();
		i++;
	};
	i=0;

	norm= new DPoint[vnorm.size()];
	for (Enumeration e = vnorm.elements() ; e.hasMoreElements() ;) {
		norm[i] = (DPoint)e.nextElement();
		i++;
	};

	calcBaryEndPoints();	
	
   }

private double calcYpoint(double x, double x1, double y1, 
					 double x2, double y2) {
   return (x * ((y2-y1) / (x2-x1)) +((x2*y1 - x1*y2)/ (x2-x1)) ) ;
}

private void calcBaryEndPoints ()  {
   // Calculate line end points of bary centric motion
	double x1,y1,x2,y2;
	x1=bary[0].getX();
	y1=bary[0].getY();
	x2=bary[bary.length-1].getX();
	y2=bary[bary.length-1].getY();
        baryStart= new DPoint(min.getX(),calcYpoint(min.getX(),x1,y1,x2,y2));	
        baryEnd= new DPoint(max.getX(),calcYpoint(max.getX(),x1,y1,x2,y2));	
}
  
public int calcX(double d) {
	Double ret=new Double(leftRule+((d - min.getX())/scaleX));
	return ( ret.intValue() );
} 
public int calcY(double d) {
	Double ret=new Double((d- min.getY())/scaleY);
	return ( dheight - ret.intValue() );
} 

private void plotObservations (Graphics g)  {
   // Take each transit and plot it
    double pa,pd;
    for (Enumeration e = hipi.abscissae.elements() ; e.hasMoreElements() ;) {
	Abcissa a = (Abcissa) e.nextElement();
	if (((a.consortia.startsWith("N")||a.consortia.startsWith("n") )
		&& showndac) ||
	    ((a.consortia.startsWith("F")||a.consortia.startsWith("f") )
		&& showfast)) {
	  if (a.consortia.startsWith("N") ||a.consortia.startsWith("n")) {
             g.setColor(Color.red);
	  } else {
             g.setColor(Color.blue);
	  }
	  VectorPosition earth=Earth.where(Constants.t0+a.tobs);
	  pa = earth.getX()*hipi.sinRa() - earth.getY()*hipi.cosRa();
	  pd = (earth.getX()*hipi.cosRa() +earth.getY()*hipi.sinRa())*hipi.sinDec() - earth.getZ()*hipi.cosDec();
	  DPoint fit = new DPoint ((a.tobs*hipi.pma) +(hipi.par*pa),
				 (a.tobs*hipi.pmd) + (hipi.par*pd));

	  DPoint obs = new DPoint (fit.getX() +(a.a8 * a.a3),
				 fit.getY() +(a.a8 * a.a4));
	  if (a.consortia.startsWith("f")||a.consortia.startsWith("n")){
	     plotOpenPoint(g,obs);
	  } else {
	     plotPoint(g,fit);
	     plotLine (g,obs,fit);
	     DPoint barStart= new DPoint ( obs.getX() + (w*a.a4),
				      obs.getY() - (w*a.a3));
	     DPoint barEnd= new DPoint ( obs.getX() - (w*a.a4),
				      obs.getY() + (w*a.a3));
	     plotLine(g,barStart,barEnd);
	  }

       } // end if consortia and sho
	
    }
     
}
private void plotPoint(Graphics g,DPoint p) {
	int diameter=4;
            g.fillOval( calcX(p.getX())-(diameter/2),
			calcY(p.getY())-(diameter/2),
                        diameter,diameter);
}
private void plotOpenPoint(Graphics g,DPoint p) {
	int diameter=4;
            g.drawOval( calcX(p.getX())-(diameter/2),
			calcY(p.getY())-(diameter/2),
                        diameter,diameter);
}
private void plotLine(Graphics g,DPoint p1,DPoint p2) {
        g.drawLine( calcX(p1.getX()), calcY(p1.getY()),
                    calcX(p2.getX()), calcY(p2.getY()));
}
private void plotBary (Graphics g)  {
   // plot line through end points of bary centric motion
     g.setColor(baryColour);
     plotLine(g,baryStart,baryEnd);
    // g.drawLine (calcX(baryStart.getX()),calcY(baryStart.getY()),
//		 calcX(baryEnd.getX()),calcY(baryEnd.getY()));
}

private void plotNorm (Graphics g)  {
   // plot Norm Points

        Color tmp = g.getColor();
        int diameter=4; 
	double dt=.01;
        double tmin = 1989.8;
        double tmax = 1993.3;

        g.setColor(Color.green);
	for (double t = tmin ; t < (tmax+(0.5*dt)); t+=dt) {
	    VectorPosition earth= Earth.where(t);
	    DPoint b = new DPoint();
	    double pa = earth.getX()*hipi.sinRa() - earth.getY()*hipi.cosRa();
	    double pd = (earth.getX()*hipi.cosRa() +earth.getY()*hipi.sinRa())*hipi.sinDec() - earth.getZ()*hipi.cosDec();

	    b.setX((t-Constants.t0)*hipi.pma);
	    b.setY((t-Constants.t0)*hipi.pmd);

            g.fillOval( calcX(b.getX() + (hipi.par*pa))-(diameter/2),
			calcY(b.getY() + (hipi.par*pd))-(diameter/2),
                        diameter,diameter);
	}


        g.setColor(tmp);
}

private void drawRulers (Graphics g)  {
     String ly,hy,st;
     int ystep=10,xstep=10;
     int cShift=9;
     int highY,lowY,lowX,highX,tick;
     int posLowY,posHighY,step,posLowX,posHighX;

     Float stF;
   // ystuff
     stF = new Float(max.getY() - (max.getY()%ystep));
     highY =  stF.intValue();
     stF = new Float(min.getY() - (min.getY()%ystep));
     lowY = stF.intValue();
     posHighY= calcY(highY) + (cShift/2);
     posLowY= calcY(lowY) + (cShift/2);
     ystep= getStep(highY-lowY);
    // x stuff
     stF = new Float(max.getX() - (max.getX()%ystep));
     highX = stF.intValue();
     stF = new Float(min.getX()  - (min.getX()%xstep));
     lowX = stF.intValue();
     posHighX=  calcX(highX) -(cShift/2);
     posLowX= calcX(lowX) - cShift;
     xstep= getStep(highX-lowX);
     // X and Y axis and Scale (Netscape seems to have a big problem
     // with strings hence the rather over kill appraoch here
     g.setColor(Color.black);
     String scaleBanner= new String ("Tick Scale: ");
     String tmp= new String ("(mas) Dec ");
     scaleBanner+=ystep+tmp;
     tmp= new String ("(mas) RA");
     scaleBanner+=xstep+tmp;
     g.drawString(scaleBanner,1,12);//+ystep+"(mas) Dec, "+xstep+"(mas) RA",1,12);
     g.drawString("Dec",1,50);
     Dimension d = this.getSize();
     g.drawString("RA",d.width -30,d.height -cShift);
     g.drawLine (leftRule-1,border,leftRule-1,d.height-bottomRule);
     g.drawLine (leftRule-1,d.height-bottomRule,d.width-rightRule,d.height-bottomRule);

     // low high
     g.drawString(""+lowY,3,posLowY);
     g.drawString(""+highY,3,posHighY);
     g.drawString(""+lowX,posLowX, d.height -(cShift/2));
     g.drawString(""+highX,posHighX, d.height -(cShift/2));
     // Tick Marks on Y
     tick = lowY - (lowY%ystep);
     int endTick = highY - (highY%ystep); 
     while (tick <= endTick) {
         //int posTick= border + (int)((tick-min.getY())/scaleY);
         int posTick= calcY(tick);
	 g.drawLine(leftRule-5,posTick, leftRule,posTick);
	 if (tick==0) g.drawString(""+tick,3,(posTick+(cShift/2)));
	 tick+=ystep;
     }

     // ticks on X
     tick = lowX - (lowX%xstep);
     endTick = highX - (highX%xstep); 
     while (tick <= endTick) {
         int posTick= calcX(tick);
	 g.drawLine(posTick,(d.height -bottomRule), posTick, (d.height -bottomRule +5));
	 if (tick==0) g.drawString(""+tick,(posTick-(cShift/2)),(d.height -(cShift/2)));
	 tick+=xstep;
     }
}

// Properties
  public void setHipi(HIPI obj) {
      stop();
      this.hipi = obj;
      min= new DPoint();
      max= new DPoint();
      calcBaryCentric();
      repaint();
      if (baryMover==null) {
         baryMover= new Bary(this.getGraphics(),this);
      }
      if (baryMover!=null) {
	baryMover.setBary(bary,norm);
      }
	start();
  }

  public HIPI getHipi() {
    return hipi;
  }

  private int getStep(int range) {
	int ret=10;
	if (range >= 200) ret=100;
	return ret;
  }
}
