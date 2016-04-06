// Written by William O'Mullane for the
// Astrophysics Division of ESTEC  - part of the European Space Agency.

package hipparcos.tools;

import java.io.*;
import java.util.*;
import java.net.URL;

/** This is a threaded class which uses a star factory to load data 
	while the application still has control - as new Stars are manufactured
	they are put in the StarStore - StarStore also has interfaces
	for initialising the loader.
	So essential this attaches a factory to a store to fill the store up.
*/
public class StarLoader  implements Runnable {
  private Thread loader=null;
  private StarStore sky;
  private StarFactory fac;

 /** Initialise class - called by the constructors */
 protected void init (StarStore sky, double deltaTol, boolean hipOnly) {
   if (Constants.verbose > 2) System.out.println ("Store - hipOnly:"+hipOnly);
   this.sky = sky;
   this.fac = new StarFactory (sky.getAlpha(), sky.getDelta(), 
								sky.getTol()+deltaTol, 99,hipOnly);
   loader = new Thread(this);
   loader.start();
 }


 public StarLoader ( StarStore sky ) {
   init (sky,0,false);
 }

 /** Allows hiponly and delta tolerance. Basically for proper motion
	we need to have more data than is displayed on the Sky */
 public StarLoader ( StarStore sky, double deltaTol, boolean hipOnly ) {
	init (sky,deltaTol,hipOnly);
 }

  /** Obligatory for runable */
  public void run() {
     System.out.println("Starting StarLoader V3...");
     load();
     loader.stop();
     loader=null;
  }

	/** Keep taking stars from the factory untiul noMoreStars and put them 
		in the Store.  */
	public boolean load() {
		boolean more=true;
		Star star;
		while (more) {
			try {
				star = fac.getNext();
				sky.addStar(star);
			} catch (NoMoreStars nms) { more = false; };
		};
      return true;
	}

}
