// Interface for RotatePanel to allow it interact with the scene
// Did it as an interface incase we want to rotate other things
// later rather than just Sky3d.

package hipparcos.sky3d;
import javax.media.j3d.RotationInterpolator;

interface RotateAble {
	RotationInterpolator getRotationInterpolator ();
};
