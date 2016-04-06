package hipparcos.tools.test;

import hipparcos.tools.*;

/** Start up the factory with command line args print results */

public class Browse {
   public static void main (String argv[]) {
		Browser.goTo(argv[0]);
   }
}
