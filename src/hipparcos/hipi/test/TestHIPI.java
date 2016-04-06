package hipparcos.hipi.test;

import hipparcos.hipi.*;

public class TestHIPI {

 static public void main(String argv[]) {    
	try {
	HIPI tmp =HIPIFactory.get( new Integer(argv[0]).intValue());
	System.out.println(tmp);
	System.out.println(tmp.getInfoText1());
	} catch (Exception e) {
		
		System.out.println( "Not Found" );
	}
 }
 
}
