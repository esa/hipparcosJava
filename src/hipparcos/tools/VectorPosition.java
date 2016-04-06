package hipparcos.tools;

/** Position in 3 dimensions  overlaps somewhat with javax.vecmath.vector3d.*/
public class VectorPosition {
  private double x,y,z;

   public VectorPosition () {
	x=0;y=0;z=0; 
   }
   public VectorPosition (double x,double y, double z) {
	this.x= x;
	this.y= y;
	this.z= z;
   }

   public double getX() { return x;};
   public double getY() { return y;};
   public double getZ() { return z;};

   public void setX(double x) { this.x= x;};
   public void setY(double z) { this.y= y;};
   public void setZ(double z) { this.z= z;};

   public String toString () {
	return (""+x+" "+y+" "+z);
   }
}
