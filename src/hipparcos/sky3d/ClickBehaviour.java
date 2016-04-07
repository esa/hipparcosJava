
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

package hipparcos.sky3d;
import com.sun.j3d.utils.behaviors.picking.*;
import javax.media.j3d.*;
import hipparcos.tools.*;

@SuppressWarnings("deprecation")
public class ClickBehaviour extends PickMouseBehavior {
  int pickMode = PickObject.USE_BOUNDS;
  StarPanel starPanel;

  /**
   * Creates a behavior that waits for user mouse events for
   * the scene graph. 
   **/

  public ClickBehaviour(BranchGroup root, Canvas3D canvas, Bounds bounds){
    super(canvas, root, bounds);
    this.setSchedulingBounds(bounds);
  }

  /**
   * Creates a click behavior that waits for user mouse events for
   * the scene graph.
   * @param root   Root of your scene graph.
   * @param canvas Java 3D drawing canvas.
   * @param bounds Bounds of your scene.
   * @param pickMode specifys PickObject.USE_BOUNDS or PickObject.USE_GEOMETRY.
   * Note: If pickMode is set to PickObject.USE_GEOMETRY, all geometry object in 
   * the scene graph that allows pickable must have its ALLOW_INTERSECT bit set. 
   **/

  public ClickBehaviour(BranchGroup root, Canvas3D canvas, Bounds bounds, 
			    int pickMode){
    super(canvas, root, bounds);
    this.setSchedulingBounds(bounds);
    this.pickMode = pickMode;
  }

  /**
   * Sets the STarPanel - the one ot be update when a star is clicked on
   * @param starPanel - the StarPanel to be used.
   **/  
  public void setStarPanel(StarPanel starPanel) {
    this.starPanel = starPanel;
	System.out.println("Panel set on clicker");
  }

  /**
   * Sets the pickMode component of this ClickBehaviour to the value of
   * the passed pickMode.
   * @param pickMode the pickMode to be copied.
   **/  
  public void setPickMode(int pickMode) {
    this.pickMode = pickMode;
  }

 /**
   * Return the pickMode component of this ClickBehaviour.
   **/ 

  public int getPickMode() {
    return pickMode;
  }

  /**
   * Update the scene to manipulate any nodes. This is not meant to be 
   * called by users. Behavior automatically calls this. You can call 
   * this only if you know what you are doing.
   * 
   * @param xpos Current mouse X pos.
   * @param ypos Current mouse Y pos.
   **/
  public void updateScene(int xpos, int ypos){
    Star3D tg = null;
    
    if (!mevent.isMetaDown() && !mevent.isAltDown()){
      tg =(Star3D)pickScene.pickNode(pickScene.pickClosest(xpos, ypos,pickMode),
					     PickObject.PRIMITIVE);
      // Make sure the selection exists and is movable. 
      if (tg != null)  {
		if (starPanel == null)
		   Browser.goTo(tg.getStar().makeUrl());
		else 
			starPanel.setStar(tg);
      } 
    }
  }

}

