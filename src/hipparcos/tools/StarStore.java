// Written by William O'Mullane for the
// Astrophysics Division of ESTEC  - part of the European Space Agency.

package hipparcos.tools;
import java.util.Iterator;

/** Simple interface for acepting Stars - used by StarLoader 
	Hence to make use fo Star loader you need somwthing which implements
	StarStore.
*/
public interface StarStore {
   public void addStar(Star star);
   public double getAlpha();
   public double getDelta();
   public double getTol();
   public Iterator getStars();
}
