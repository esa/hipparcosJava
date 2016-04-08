
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
package hipparcos;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Properties;

import hipparcos.curve.LightCurve;
import hipparcos.hipi.PlotHipi;
import hipparcos.sky.ShowSky;
import hipparcos.tools.Browser;
import hipparcos.tools.MainFrame;

public class StartPanel extends Frame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private MainFrame curve = null;
	private MainFrame sky = null;
	private MainFrame hipi = null;
	
	private static String[] args= {"5","0.00996534","40.59122440"};


	public StartPanel() {
		init();
	}

	
	private void init() {
	    MenuBar mb = new MenuBar();
	    Menu m = new Menu( "Menu" );
	    m.add( new MenuItem( "Restart" ) );
	    m.add( new MenuItem( "Quit" ) );
	    mb.add( m );
	    setMenuBar( mb );
	    
		setTitle( "Hipparcos Java StartPanel");

		// Set up properties.
		Properties props = System.getProperties();
		props.put( "browser", "hipparcos.tools.StartPanel" );
		props.put( "browser.version", "Apr2016" );
		props.put( "browser.vendor", "European Space Agency" );
		props.put( "browser.vendor.url", "http://www.cosmos.esa.int/" );

		setLayout( new BorderLayout() );
		pack();
		validate();
		
		Panel pan = new Panel();
		pan.setLayout(new GridLayout(3,1));
		
	    Button showSky = new Button("Show Sky");
	    showSky.setActionCommand("ShowSky");
	    showSky.addActionListener(this);
        pan.add(showSky);
	    
	    Button curve = new Button("Light Curve");
	    curve.setActionCommand("LightCurve");
	    curve.addActionListener(this);
	    pan.add(curve);

	    Button hipi = new Button("Intermediate Data");
	    hipi.setActionCommand("PlotHipi");

	    hipi.addActionListener(this);
	    pan.add(hipi);
	    
        add("Center",pan);
        
        this.setSize(400, 400);

		setVisible(true);

	/*
     WindowListener inner class to detect close events.
	*/
	        addWindowListener(new WindowAdapter()
	        {
	            public void windowClosing(WindowEvent winEvent)
	            {
	                System.exit(0);
	            }
	        });		
	}

	public static void main(String[] args) {
		if (args!=null && args.length>0) {
			StartPanel.args=args;
		}
		Browser.setMainFrame();
		new StartPanel() ;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		 String label = e.getActionCommand();
		// System.err.println("Got : "+label);
		 if (label=="Quit") {
           System.exit(0);
         }

		 if (label=="PlotHipi") {
			 if (hipi == null) {
			     String[] largs= {"id="+args[0]};
			  	     hipi = new MainFrame(new PlotHipi(),largs, 730, 570);
				 } else {
					 hipi.toFront();
				 }
         }

		 if (label=="ShowSky") {
			 if (sky == null) {
				 	 String[] largs={args[1],args[2]};
				     ShowSky ss = new ShowSky(largs);
			  	     sky = new MainFrame(ss,null,990,800);			  	     
				 } else {
					 sky.toFront();
				 }
         }

		 if (label=="LightCurve") {
			 if (curve == null) {
		  	     curve = new MainFrame(new LightCurve(),null,800,450);
			 } else {
				 curve.toFront();
			 }
		 }		
	}

}
