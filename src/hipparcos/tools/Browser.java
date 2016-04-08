
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

package hipparcos.tools;

import java.net.*;
import java.applet.*;
import java.awt.Desktop;
import java.io.IOException;

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
			if (theBrowser==null) {
				gotToDesktop(location);
			}
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
			String err = "Could not run browser "+cmd +" check the browser property - leave it empty for default behaviour.";
			System.err.println (err +":"+e);
			return err;
		}
		return cmd;
	}

	/**
	 * use standard awt desktop browser connection which was not around in 1997 ..
	 *
	 * @param location
	 * @throws URISyntaxException 
	 * @throws IOException 
	 */
	public static void gotToDesktop(String location) throws IOException, URISyntaxException {
		if(Desktop.isDesktopSupported())
		{
		  Desktop.getDesktop().browse(new URI(location));
		}
		
	}
	public static String goTo (URL location) {
		if (inApplet()) {
			apCon.showDocument(location,"The Hipparcos Catalogue");
			return "Browser :"+location;
		}
		return goTo(location.toString());
	}
}
