package hipparcos.plot;

/** A point x,y expressed as two double numbers. */
public class DPoint {
   private double x=0, y=0;
   private boolean valid;
   private boolean semivalid;

   public DPoint(double x, double y) {
	valid=true;
	semivalid=true;
	this.x=x;
	this.y=y;
   }

   public DPoint(DPoint p) {
	valid=true;
	semivalid=true;
	this.x=p.getX();
	this.y=p.getY();
   }

   public DPoint() {
	valid=false;
	semivalid=false;
   }

   public boolean isValid() { return valid; };

   public double getX() {
     return x ;  
// should throw an exception here if !valid ...
   }

   public double getY() {
      return y;
   }

   public void setX(double x) {
      this.x= x;
      semiValidate(); 
   }

   public void setY(double y) {
      this.y= y;
      semiValidate(); 
   }

   private void semiValidate() {
	if (!valid) {
	   valid=semivalid;
	   semivalid= true;
	}
   }

   public String toString() {
	return (""+x+"  "+y);
   }
}
