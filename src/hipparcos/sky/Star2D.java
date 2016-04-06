// Written by William O'Mullane for the
// Astrophysics Division of ESTEC  - part of the European Space Agency.

package hipparcos.sky;

import java.awt.*;
import java.net.*;

import hipparcos.tools.*;

/** A star with info for plotting in 2D */
public class Star2D extends hipparcos.tools.Star {

  static public Color HipColor=Color.red;
  static public Color TycColor=Color.white;
  static public Color HTColor=Color.cyan;
  static private double thresholdMag=999;
  static private boolean ignoreDiff=false;
  static private int yearStep=100;
  protected Mag  gmag; 
  protected int x,y,mux,muy;
  protected boolean shown=false;

  static public void setThreshold(double threshold) {
        thresholdMag=threshold;
   }
  static public double getThreshold() {
        return thresholdMag;
   }

  public void init() {
	id=null;
  }

  public Star2D (Star star) {
	super(star);
    gmag  = new Mag(getMag());    
  }

  public Star2D (String str) throws Exception {
	super(str);
    gmag  = new Mag(getMag());    
  }

   public Mag getMag2D() {
      return gmag;
   }
   public void plot(java.awt.Graphics g,int x,int y) {
	this.x =x; this.y=y;	
        plot(g);
   }

   public void plot(java.awt.Graphics g) {
	int diameter= gmag.getPixelDiameter();
	Color tmp = g.getColor();
	if (ignoreDiff) {
	    g.setColor(TycColor) ;
	} else  
        if (type.startsWith("H")) {
	   g.setColor(HipColor);
        }else {
	   if (inHIPnTYC) {
                g.setColor(HTColor);
	   }
	   else {
	        g.setColor(TycColor) ;
	   }
        }
	if (getMag() <= thresholdMag) {
           g.fillOval(x-(diameter/2),y-(diameter/2),
                        diameter,diameter);
	}

        g.setColor(tmp);
   }

   public void unplot(java.awt.Graphics g,boolean tails) {
	int diameter= gmag.getPixelDiameter();
	Color tmp = g.getColor();
	if (tails && ( getMag() <= thresholdMag)) 
	   g.setColor(Color.yellow);
	else
	   g.setColor(Color.black);
        g.fillOval(x-(diameter/2),y-(diameter/2),
                        diameter,diameter);
        g.setColor(tmp);
   }

   public void showDetails(java.awt.Graphics g,int rightLimit) {
	int txof=105;
	if (type.startsWith("T")) txof=155;
	if (((x +txof) < rightLimit) && (y > 18)) {
	   showDetails(g);
	} else {
	   int newx=x,newy=y;
	   if (y<=18) newy=18;
	   if ((x +txof) > rightLimit) newx=x -((x+txof)-rightLimit);
	   showDetails(g,newx,newy);
	}
	shown=!shown;
   }
   public void showDetails(java.awt.Graphics g) {
	g.setXORMode(Color.green);
	g.drawString(shortInfo(),x+1,y-1);
   }
   public void showDetails(java.awt.Graphics g,int x,int y) {
	g.setXORMode(Color.green);
	g.drawString(shortInfo(),x,y);
   }

	/** What does near mean ?? - Here it is relativly close to 
		x and y in the plotting plane basically within a few 
		pixels. Thus we can tell which star someone clicks on.
	*/
   public boolean near(int x, int y) {
	boolean ret=false;
	int allow= (int)gmag.getPixelDiameter()/2;
	ret = (x >= (this.x - allow)) && (x <= (this.x + allow)) &&
		( y >= (this.y - allow)) &&  ( y <= (this.y + allow)) ;
	return ret;
   }

	/** Assumes Stars are sorted in a certain order - the order
		in the cataloge */
   public boolean before(int x, int y) {
	boolean ret=false;
	if (x == this.x ) {
	   ret = ( y < this.y );
	} else {
	   ret = (x < this.x);
	}
	return ret;
   }

   public boolean after(int x, int y) {
	boolean ret=false;
	if (x == this.x ) {
	   ret = ( y > this.y ) ;
	} else {
	   ret = (x > this.x);
	}
	return ret;
   }

	public void move(java.awt.Graphics g, int x, int y, int rightLimit,
					 boolean tails) {
	   if ((x != this.x) || (y != this.y)) {
		boolean details=shown;
		int diameter= gmag.getPixelDiameter();
 
		if (details) showDetails(g,rightLimit);
		unplot(g,tails);
		plot(g,x,y);
		if (details) showDetails(g,rightLimit);
	   }
	}

	public double getMuAlpha(int step) {
	   return (yearStep*((getMuAlpha()*step)/3600000));
	}

	public double getMuDelta(int step) {
	   return (yearStep*((getMuDelta()*step)/3600000));
	}

	static public boolean setIgnoreDiff() {
	   return (ignoreDiff=true);
	}

	static public boolean unSetIgnoreDiff() {
	   return (ignoreDiff=false);
	}
	
	static public boolean setYearStep(int ys) {
	   yearStep=ys;
	   return true;
	}
	static public int yearStep() {
	   return(yearStep);
	}
}; 
