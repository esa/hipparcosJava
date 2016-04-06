package hipparcos.tools.test;

import hipparcos.tools.*;

/** Start up the factory with command line args print results */

public class StarFac {
   public static void main (String argv[]) {
		if (argv.length < 3) {
			System.err.println ("At least 3 numebrs please ");
			System.exit(1);
		}
		double ra = new Double(argv[0]).doubleValue();
		double dec = new Double(argv[1]).doubleValue();
		double d = new Double(argv[2]).doubleValue();
		StarFactory fac = new StarFactory(ra,dec,d);
		Star star ;
		boolean more=true;
		while (more) {
			try {
				star = fac.getNext(); 
				System.out.println(star);
			} catch (NoMoreStars nms) { more = false; };
		}
   }

	
}
