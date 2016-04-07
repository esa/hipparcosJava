
/*
* Copyright (C) 1997-2016 European Space Agency
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 2.1 of the License, or (at your option) any later version.
*
* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
*/
// Written by William O'Mullane for the
// Astrophysics Division of ESTEC  - part of the European Space Agency.

package hipparcos.sky3d;

import java.awt.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import hipparcos.tools.*;


public class ColourCell extends Canvas3D {
	private BranchGroup     scene;
	private TransformGroup objTrans;

    private SimpleUniverse uni=null;
   private int scale=2;
	public static final float swidth = 0.2f;
	final static Font3D font3d = new Font3D(new Font("Times", Font.PLAIN, 8),
									new FontExtrusion());

   public BranchGroup createSceneGraph() {
	    // Create the root of the branch graph
    BranchGroup objRoot = new BranchGroup();
	objTrans = new TransformGroup();

    objRoot.addChild(objTrans);
	        // Set up the background
		BoundingSphere bounds =
        new BoundingSphere(new Point3d(0.0,0.0,0.0), 50.0);

        Color3f bgColor = new Color3f(0.01f, 0.01f, 0.1f);
        Background bgNode = new Background(bgColor);
        bgNode.setApplicationBounds(bounds);
        objRoot.addChild(bgNode);

        // Set up the ambient light
        AmbientLight ambientLightNode = new AmbientLight();
        ambientLightNode.setInfluencingBounds(bounds);
        objRoot.addChild(ambientLightNode);

        // Set up the directional lights
        Color3f light1Color = new Color3f(0.8f, 0.8f, 0.8f);
        Vector3f light1Direction  = new Vector3f(4.0f, -7.0f, -12.0f);

        DirectionalLight light1
            = new DirectionalLight(light1Color,light1Direction);
        light1.setInfluencingBounds(bounds);
        objRoot.addChild(light1);

    return objRoot;
   }

   public ColourCell() {
	 super(null);
	 scene = createSceneGraph();
     uni = new SimpleUniverse(this);
   }

    public void showCol(Appearance ap) {
        objTrans.addChild(new Sphere(swidth,Star3D.flags,15,ap));
	
        scene.compile();
        uni.addBranchGraph(scene);
        // This will move the ViewPlatform back a bit so the
        // objects in the scene can be viewed.
        uni.getViewingPlatform().setNominalViewingTransform();
    }
	  public Dimension getPreferredSize() {
        return new Dimension(20,30);
  }


}
