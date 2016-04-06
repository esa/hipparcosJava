// Written by William O'Mullane for the 
// Astrophysics Division of ESTEC  - part of the European Space Agency.

package hipparcos.tools;

import java.net.*;
import java.io.*;
import java.applet.*;

/** Allow interaction with an already running browser - the default one
	*/

public class Browser {
	protected static AppletContext apCon=null;
	protected static boolean mainFrame =false;

/* Call if starting applet in Main frame */
	public static void setMainFrame() {
		mainFrame=true;
	}
	public static void setAppletContext(AppletContext apCon) {
		Browser.apCon=apCon;
	}
	public static boolean inApplet () { return (apCon!=null && mainFrame==false); };

	public static String goTo (String location) {
		String cmd=null;
		try {
			String theBrowser= HIPproperties.getProperty("browser");
			cmd = theBrowser + " -raise -remote openURL(" + location+")";
			if (Constants.verbose > 1) System.out.print ("Running "+cmd);
			Runtime rt = Runtime.getRuntime();
			Process p = rt.exec(cmd);
			p.waitFor();
			if (Constants.verbose > 1) System.out.println (".. exit "+p.exitValue());
			if (p.exitValue() != 0) {
				cmd = theBrowser +" "+ location;
			    if (Constants.verbose > 1) System.out.println ("Trying "+cmd+" instead. ");
			    p = rt.exec(cmd);
			}
		} catch (Exception e) {
			String err = "Could not run browser "+cmd;
			System.err.println (err +":"+e);
			return err;
		}
		return cmd;
	}

	public static String goTo (URL location) {
		if (inApplet()) {
			apCon.showDocument(location,"The Hipparcos Catalogue");
			return "Browser :"+location;
		}
		return goTo(location.toString());
	}
}
