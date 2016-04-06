package hipparcos.curve.test;

import hipparcos.curve.*;

public class TestHIPEP {

 static public void main(String argv[]) {    
	try {
	HIP_EP tmp =HIPEPFactory.get( new Integer(argv[0]).intValue());
	System.out.println(tmp);
	} catch (Exception e) {
		System.out.println( "Not Found" );
	}
 }
 
}
