// Written by William O'Mullane for the
// Astrophysics Division of ESTEC  - part of the European Space Agency.

package hipparcos.sky;

import hipparcos.tools.*;
import java.awt.*;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

/** This class is basically a plotting canvas for stars centred on
	a given coordinate. It implements StarStore (hipparcos.tools) which 
	means it can be populated in the background using a StarLoader.
*/
public class SkyArea extends Canvas implements StarStore{

  private Vector stars=null;
  private int firstTyc=0;
  private String gap=new String("       ");
  private double alpha,delta,tol;
  private double scale=0 ; 	
  private boolean inited=false;
  private Star2D lastSelected=null;
  private boolean viewHipOnly=false,tails=false;
  private Label infoRA=null,infoDEC=null,infoHMS=null,infoDMS=null;
  DecimalFormat raf = new DecimalFormat("000.00000000");  
  DecimalFormat decf = new DecimalFormat("00.00000000");  
  


	public Iterator getStars() {
		return stars.iterator();
	}
  public SkyArea () { 
        setBackground(Color.black);
	};

  public SkyArea (double alpha, double delta, double tol) {
        setBackground(Color.black);
	inited=true;
	this.alpha=alpha;
	this.delta=delta;
	this.tol=tol;
        stars= new Vector();
        this.scale=this.getSize().width/(tol*2);
  }

  public void init(double alpha, double delta, double tol) {
	inited=true;
	this.alpha=alpha;
	this.delta=delta;
	this.tol=tol;
	firstTyc=0;
        stars= new Vector();
	this.getGraphics().setColor(Color.black);
	this.getGraphics().setPaintMode();
	this.getGraphics().fillRect(0,0,getSize().width,getSize().height);
        this.scale=this.getSize().width/(tol*2);
  }

  /** Optional Lables may be provided in which info is displayed
	  as the mouse runs over the canvas */
  public void infoInit(Label RA,Label DEC,Label HMS, Label DMS) {
	infoRA=RA;
	infoDEC=DEC;
	infoHMS=HMS;
	infoDMS=DMS;
  }

  public void paint (Graphics g) {
        this.setBackground(Color.black);
	if (inited) {
	   double tmpscale=this.getSize().width/(tol*2);
	   if (tmpscale != scale) {
              plotAll(tmpscale);
           }
	   else
              plotAll();
	}

  }

// Properties

  public double getAlpha() {
    return alpha;
  }
  public double getDelta() {
    return delta;
  }
  public double getTol() {
    return tol;
  }

  public void addStar(Star hstar) {
	Star2D star = new Star2D(hstar);
	plotStar(star);
 	stars.addElement(star);
	if (firstTyc==0) 	{
	   if (star.getType().startsWith("T")){
		firstTyc=stars.size()-1;
	   }
	}
  }

  private void plotStar(Star2D star) {
	int x = whichX(star.getAlpha());
	int y = whichY( star.getDelta());
	star.plot(this.getGraphics(),x,y);
	//System.out.println(" Plot at "+x+" "+y+" al="+alpha+" del="+delta+" sal="+star.getAlpha()+" sdel="+star.getDelta()+ " Scale="+scale);
  }
	
   /** use proper motion to figure out where it should be
       Step is used becuase in one step no move may occur
       but over a few it might move 1 pixel. 
	*/
  private void moveStar(Star2D star,int step) {
	int startx = whichX(star.getAlpha());
	int starty = whichX(star.getDelta());
	int x = whichX(star.getAlpha() + star.getMuAlpha(step));
	int y = whichY( star.getDelta() + star.getMuDelta(step));
	Graphics g=this.getGraphics();
	star.move(g,x,y,getSize().width,tails);
}

  public  void moveStars(int step) {
     if (inited) {
        for (Enumeration e = stars.elements(); e.hasMoreElements();) {
   	   Star2D star= (Star2D) e.nextElement() ;
	   if (!viewHipOnly || (viewHipOnly && star.inHIP())) { 
	      moveStar(star,step);
	   }
        }
     }
  }

  public  void plotAll() {
     if (inited) {
        for (Enumeration e = stars.elements(); e.hasMoreElements();) {
   	   Star2D star= (Star2D) e.nextElement() ;
	   if (!viewHipOnly || (viewHipOnly && star.inHIP())) { 
	      star.plot(this.getGraphics());
	   }
        }
     }
  }

  public  void plotAll(double scale) {
     if (inited) {
        this.scale=scale;
        System.out.println("Rescaled ");
        for (Enumeration e = stars.elements(); e.hasMoreElements();) {
	   Star2D star= (Star2D) e.nextElement() ;
	   this.plotStar(star);
        }
     }
   }

  public  void replotAll(double thres) {
     if (inited) {
        for (Enumeration e = stars.elements(); e.hasMoreElements();) {
	   Star2D star= (Star2D) e.nextElement() ;
	   if (star.getMag() > thres) {
	      star.unplot(this.getGraphics(),false);
	   } 
        }
	this.plotAll(); // unplot sometimes obliterates a star.
			// plotting the individulas above does not work
     }
   }
  public Star2D findStarNear(int x, int y) {
    if (inited) {
	Star2D star=null;
	int right = stars.size() -1;
	//System.out.println("Looking for "+x+","+y+" FirstTyc "+firstTyc); 
	// first look for a hip entry;
	star=findStarNear(x,y,0,firstTyc-1);
	if (star==null)  star=findStarNear(x,y,firstTyc,right);
	if (star!=null) return star;
    }
 	return null;
  }

  public Star2D findStarNear(int x, int y, int sleft, int sright) {
 	boolean finished=false;
	boolean found = false;
	Star2D star=null;
	int left = sleft;
	int right = sright;
	int mid=0;
	// the array is back to front so to speak
	// stars in the top left are loaded at the end
	while (!(found ||  finished)) {
	   mid= (int)((left +right) /2);
	   star= (Star2D) stars.elementAt(mid);
	   found = star.near(x,y); 
	   finished = ((left==mid) && (right==mid)) || (left>right) ||
			star.near(x+5,y);
   //System.out.println("Looking for "+x+","+y+" at "+star +" left:"+left+" right:"+right+" mid:"+mid); 
 
	   if (star.before(x+5,y)) {
 		left=mid+1;
	   } else {
	   	right=mid;
	   }
	}
	finished=false;
	while (!finished) {
	   star= (Star2D) stars.elementAt(mid);
   //System.out.println("Forward  "+x+","+y+" to "+star +" mid:"+mid); 
	   found=star.near(x,y);
	   mid++;
	   finished= found || star.after(x-5,y) || (mid>=sright);
	}

	if (found) return star;

 	return null;
   }

	/* Last Star clicked on */
   public Star2D getLastSelected() {
	return lastSelected;
   }

	/* Set flag to turn off tycho stars */
   public void setViewHipOnly() {
	viewHipOnly=true;
	repaint();
   }
   public void setTails() {
	tails=true;
   }
   public void unSetTails() {
	tails=false;
	repaint();
   }
   public void unSetViewHipOnly() {
	viewHipOnly=false;
	repaint();
   }
   public boolean mouseDown(java.awt.Event evt, int x, int y) {
        Star2D astar;
        if ((astar=findStarNear(x,y))!=null) {
	    astar.showDetails(this.getGraphics(),this.getSize().width);
	    lastSelected=astar;
        }
    return true;
    }

    public boolean mouseMove(java.awt.Event evt, int x, int y) {
    // This would be much simpler in the New even model of J1.1;
    Position pos=new Position(whichAlpha(x),whichDelta(y));
    if (infoRA != null )     infoRA.setText(raf.format(pos.getRa()));
    if (infoDEC != null )    infoDEC.setText(decf.format(pos.getDec()));
    if (infoHMS != null )    infoHMS.setText(pos.HMS());
    if (infoDMS != null )    infoDMS.setText(pos.DMS());

    return true;
    }

    public double whichAlpha (int x) {
	double ret=0;
	if (inited)
	{
	   ret = (alpha + tol) - (x/scale);
	   if (ret < 0) ret+=360;
       	}
   	return ret; 
    }
    public double whichDelta (int y) {
	double ret=0;
	if (inited)
	{
	   ret = (delta + tol) - (y/scale);
       	}
   	return ret; 
    }
    public int whichX (double somealpha) {
	double adjustedAlpha = alpha - somealpha +tol;
	if (adjustedAlpha < 0) adjustedAlpha += 360; 
	if (adjustedAlpha > (tol*2)) adjustedAlpha -= 360; 
	int x = (int)Math.round(( adjustedAlpha)*scale);
	return x;
    }
    public int whichY (double somedelta) {
	double adjustedDelta = delta - somedelta  +tol;
	if (adjustedDelta < 0) adjustedDelta += 90; 
	if (adjustedDelta > (tol*2)) adjustedDelta -= 90; 
	int y = (int)Math.round(( adjustedDelta)*scale);
	return y;
    }

	public Dimension getPreferedSize() {
		return new Dimension(500,500);
	}
}
