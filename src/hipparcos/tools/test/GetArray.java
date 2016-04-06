package hipparcos.tools.test;

import hipparcos.tools.*;

/** Test getDoubleArray from Delimited line */

public class GetArray {
   public static void main (String argv[]) {
		DelimitedLine l = null;
		if (argv.length > 0) {
			l = new DelimitedLine(argv[0],' ');
		} else {
			
			l = new DelimitedLine("56,77,77,98,68,90",',');
		}
		try {
		double[] nums = l.getDoubleArray();
		System.out.println("Got "+nums.length+" doubles ");
		
		for (int n=0; n< nums.length ; n++) {
			System.out.println("   "+nums[n]);
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
   }

	
}
