package hipparcos.hipi.test;


import hipparcos.hipi.Earth;

public class TestEarth {

 static public void main(String argv[]) {    
     for (int i=0;i < argv.length; i++) { 
	Double year = new Double(argv[i]);
	System.out.println(argv[i] + " : "+Earth.where(year.doubleValue()));
     }
 }
 
}
