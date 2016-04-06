package hipparcos.hipi.test;

import static org.junit.Assert.*;

import org.junit.Test;

import hipparcos.hipi.Earth;
import junit.framework.Assert;

public class EarthTest {

	@Test
	public void test() {

		  for (int i=0;i <  10; i++) { 
				Double year = new Double(1985+i);
				System.out.println(year + " : "+Earth.where(year.doubleValue()));
			     }
		assertTrue("No problem", true);
	}

}
