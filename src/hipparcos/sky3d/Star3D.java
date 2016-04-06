// Written by William O'Mullane for the
// Astrophysics Division of ESTEC  - part of the European Space Agency.
// 30 Nov 1999 - changed order in while statment in getApperance
// lazy eval seesm to have changed.

package hipparcos.sky3d;

import java.awt.*;
import java.net.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.geometry.*;

import hipparcos.tools.*;

/** A star as a 3D renderable object */
public class Star3D extends Sphere {
	public static final int flags=GENERATE_NORMALS|ENABLE_GEOMETRY_PICKING|ENABLE_PICK_REPORTING;

  protected Star  star; 
  static protected float colStep=0;
  static protected Appearance appearances[];

	/** Set up appearances for the colours */
	static void init() {
		appearances = new Appearance[ Constants.colours.length];	
		float r,g,b;
		for (int i = 0; i< appearances.length; i++) {
			appearances[i] = new Appearance();
			r = Constants.colours[i][0];
			b = Constants.colours[i][1];
			g = Constants.colours[i][2];
	 		if (Constants.verbose > 3)	
				System.out.println("Col:"+i +"r:"+r+" b:"+b+" g:"+g );
			Material material = new Material();
			material.setDiffuseColor(r+0.05f,g+0.05f,b+0.05f);
			material.setShininess(10);
			material.setAmbientColor(r,g,b);
			material.setLightingEnable(true);
			appearances[i].setMaterial(material);	
		}
	}

   public static Appearance sgetAppearance(int col) {
	if (appearances == null) init();
	return appearances[col];
   	}

  /** Get Appearance for given star */ 
   public static Appearance getAppearance(Star star) {
	if (appearances == null) init();
	// search forward through the colorbands until the we find
	// the band for this b-v. 
	int col = 0;
	double b_v = star.getB_V();
	while ( col < Constants.colBands.length && b_v > Constants.colBands[col]) col++;
	if (col > (Constants.colBands.length)) col = Constants.colBands.length;
	
	if (Constants.verbose > 3) System.out.println("Allocating colour "+col+" for "+b_v+ " "+ star.getId());
	return appearances[col];
  }


	/** Size for given Magnitude*/ 
	static public float calcSize(Star star) {
		float size = (float)( star.getMag() + 5 + 5 * Math.log(star.getParalax()));
		size = (float)((60f - size)/60);
		return size;
    }

  public Star3D (Star star) {
	super(calcSize(star),flags,15,getAppearance(star));
	this.star=star;
  }
  public Star getStar() {
		return star;
	}

	public String toString() {
		return star.toString();
	}

}; 
