// Written by William O'Mullane for the
// Astrophysics Division of ESTEC  - part of the European Space Agency.

package hipparcos.sky3d;

import java.awt.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import hipparcos.tools.*;


public class ColourScale extends Canvas3D {
	private BranchGroup     scene;
	private TransformGroup objTrans;
	private TransformGroup objScale;

    private SimpleUniverse uni=null;
   private int scale=2;

   public BranchGroup createSceneGraph() {
	    // Create the root of the branch graph
    BranchGroup objRoot = new BranchGroup();
	objTrans = new TransformGroup();
	objScale = new TransformGroup();

    objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
    objScale.addChild(objTrans);
	objRoot.addChild(objScale);
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
        Color3f light2Color = new Color3f(0.3f, 0.3f, 0.3f);
        Vector3f light2Direction  = new Vector3f(-6.0f, -2.0f, -1.0f);

        DirectionalLight light1
            = new DirectionalLight(light1Color,light1Direction);
        light1.setInfluencingBounds(bounds);
        objRoot.addChild(light1);
        DirectionalLight light2
            = new DirectionalLight(light2Color, light2Direction);
        light2.setInfluencingBounds(bounds);
        objRoot.addChild(light2);

    return objRoot;
   }

   public ColourScale() {
	 super(null);
	 scene = createSceneGraph();
     uni = new SimpleUniverse(this);
   }

    protected void setScale(int w) {
        Transform3D scale = new Transform3D();
        float sc = (float)(0.8/(w));
		this.scale=2;
        scale.setScale(sc);
        System.out.println("Scale "+sc + " w is "+w);
        objScale.setTransform(scale);
    }

	protected void populate() {
		float w = 0.5f;	
	    //setScale(w);
		double gap = w*5;
		for (int col = 0 ; col < Constants.colours.length; col++) {
			Appearance ap = Star3D.sgetAppearance(col);				
			Transform3D mat = new Transform3D();
            TransformGroup g = new TransformGroup(mat);
			double y = gap*col - ( (gap/2)*(Constants.colours.length-1));
			//float z =  (float)  -  (Math.abs(y))/7;
			float z =  (float)  -w;
        	mat.set(new Vector3d(0,(float)y,z));
	    System.out.println(" y:"+y+" z:"+z);
            g.setTransform(mat);
            g.addChild(new Sphere(w,Star3D.flags,15,ap));
            objTrans.addChild(g);

		}
		
	}
    public void showScene() {
	    populate();
        scene.compile();
        uni.addBranchGraph(scene);
        // This will move the ViewPlatform back a bit so the
        // objects in the scene can be viewed.
        uni.getViewingPlatform().setNominalViewingTransform();
    }
	  public Dimension getPreferredSize() {
        return new Dimension(50,100);
  }


}
