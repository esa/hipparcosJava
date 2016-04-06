package hipparcos.curve;
import hipparcos.tools.*;

class EPpoint {

  public double BJD;
  public double  Hp;
  public double  standardError;
  public int  qualityFlag;

  public EPpoint (String str) throws BadlyFormatedEPstring {
     DelimitedLine line= new DelimitedLine(str,'|');
     try {
        BJD = line.getNextDouble();
        Hp = line.getNextDouble();
        standardError = line.getNextDouble();
        qualityFlag = line.getNextInt();
     }
     catch (Exception e) {
         throw( new BadlyFormatedEPstring());
     }
  }

 public String toString() {
   String ret = new String ( (new Double(BJD)).toString() );
   ret+=" ";
   ret+=Hp;
   ret+=" ";
   ret+=standardError;
   ret+=" ";
   ret+=qualityFlag;
   return (ret);
 }

}; 
