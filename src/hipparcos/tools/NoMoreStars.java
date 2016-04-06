// Written by William O'Mullane for the 
// Astrophysics Division of ESTEC  - part of the European Space Agency.

package hipparcos.tools;
/** Exception tjrown when a factory has no more stars */
public class NoMoreStars extends Exception {
   public NoMoreStars() {};
   public NoMoreStars(String msg) { super(msg);};
}
