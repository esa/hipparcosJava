
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
import java.awt.*;
import java.awt.event.*;
import hipparcos.tools.*;
import javax.vecmath.*;
import hipparcos.sky.StarList;

/**This is an application which extends Frame - i.e. it is a window
   it also implements ActionListener so it can pick up on button presses
*/
public class StarView extends Frame implements ActionListener {
   private Sky3D theSky;
   private Label info1;
   private TextField raField,decField,distField,radiusField,threshField;
   protected StarPanel starPanel;
   protected RotatePanel rotPanel;
   protected StarList lister;
   private double step=0.1;

/** Create a new StarView and Display it on the screen */
   public static void main(String argv[]) {
      StarView ap = new StarView();
      ap.pack();
      ap.init();
      ap.show();
  }

  /** Default constructor - Sets name */
  public StarView() {  super ("Hipparcos 3D");}

  /** Window manager sets the size of the window officially but we can 
	suggest which size we would like */
  public Dimension getPreferredSize() {
        return new Dimension(750,680);
  }

  /** init lays out the window and sets up components */
  public void init () {
    /* BorderLayout just makes sure the Object in the Center gets most of 
	the screen  */
    setLayout (new BorderLayout());

    // Informationi area where we can put some text ocassionaly 
    info1 = new Label("Welcome !");
    // put it along the top
    add("North",info1);

    // Plotting area - create our own sky class
    theSky = new Sky3D();
    // put it in the center of the window - most space 
    add ("Center",theSky);

    //  Create a panel to put the controls in - panel is a long strip
	FlowLayout fl = new FlowLayout(FlowLayout.CENTER,1,1);
    Panel p = new Panel (fl);

    // Label for Date field  static text
    Label raLabel = new Label("Alpha:");
    // create the text field for input 
    raField = new TextField(8);
	raField.setText("65.5");
    // add this as a text listener so we get notified when text changes
    //ra.addTextListener(this);
    // add the label Date and the field to the left side of the panel
    p.add (raLabel);
    p.add (raField);

    Label decLabel = new Label("Delta:");
    decField = new TextField(8);
	decField.setText("15.5");
    p.add (decLabel);
    p.add (decField);
    Label dLabel = new Label("dist(pc):");
    distField = new TextField(5);
	distField.setText("40");
    p.add (dLabel);
    p.add (distField);
    Label lparLabel = new Label("radius(pc):");
    radiusField = new TextField("10",4);
    p.add (lparLabel);
    p.add (radiusField);
    Label threshLabel = new Label("V(lim):");
    threshField = new TextField("99",3);
    p.add (threshLabel);
    p.add (threshField);

    // Create buttons and add this as Actionlistener - add them to panel
    Button fetch=new Button("View");
    fetch.addActionListener(this);
    p.add ("West",fetch);
    Button quit=new Button("Quit");
    quit.addActionListener(this);
    p.add("West",quit);
    Button list=new Button("List");
    list.addActionListener(this);
    p.add("East",list);
    // put the panel at the bottom of the screen
    add("South",p);
	
	Panel rp = new Panel();
	rp.setBackground(Color.black);
	
//	rp.setLayout(new GridLayout(3,1));
    GridBagLayout gridbag = new GridBagLayout();
    rp.setLayout(gridbag);
    GridBagConstraints cons = new GridBagConstraints();
    cons.fill = GridBagConstraints.BOTH;
    cons.gridy = GridBagConstraints.RELATIVE;
	cons.gridx = 0;
	cons.ipadx = 0;
	cons.ipady = 10;

	// Create StarPanel for showing star info
	starPanel = new StarPanel();
	gridbag.setConstraints(starPanel,cons);
	rp.add(starPanel);
	// attach the display panel to the sky
	theSky.setStarPanel(starPanel);
	starPanel.setStatus(info1);

	// Colour scale
	//rp.add(new Label(" "));
	ColourPanel cp = new ColourPanel();
	gridbag.setConstraints(cp,cons);
	rp.add(cp);

	// Create control panel for rotations
	rotPanel = new RotatePanel();
	gridbag.setConstraints(rotPanel,cons);
	rp.add(rotPanel);
	// attach the rotPanel panel to the sky
	rotPanel.setScene(theSky);
	// add to right of screen
	this.add("East",rp);

	lister = new StarList(theSky);
  };

   /** This is required to implement ActionListener - this gets
	called when someone hits a button */
    public void actionPerformed(ActionEvent e){
	 String label= e.getActionCommand();
	 if (label=="List") {
		lister.setVisible(true);
		lister.refresh();
	 }
	 if (label=="Quit") {
	    System.exit(0);
	 }
	 if (label=="View") {
		float ra =65.5f;
		float dec =15.5f;
		float d =40;
		float rad = 10;
		float vlim =99;
		try{
			ra = new Float(raField.getText().trim()).floatValue();
			dec = new Float(decField.getText().trim()).floatValue();
			d = new Float(distField.getText().trim()).intValue();
			rad = new Float(radiusField.getText().trim()).intValue();
			vlim = new Float(threshField.getText().trim()).intValue();
		} catch (Exception exp) { 
			System.err.println (" Parameter error "+exp);
		};
		info1.setText("Fetching Data "+ra +" "+dec+" "+d+" "+rad+" V(lim):"+vlim);
		theSky.setupScene(ra,dec,d,rad);
		populate(ra,dec,d,rad,vlim);
		theSky.showScene();
		rotPanel.setFactor((int)rad);
		rotPanel.stop();
		info1.setText("Center is "+theSky.center());
	   	return;
	 }
    }

	private void populate(double ra, double dec, double d,double rad, double vlim) {
		//StarFactory fac = new StarFactory(ra, dec,d,vlim,true);
		Vector3d vec = Sky3D.makeVecParsec(ra,dec,d);
		StarFactory fac = new StarFactory3d(vec.x,vec.y,vec.z,rad,vlim);
		boolean more=true;
		Star star;
		while (more) {
			try {
				star = fac.getNext(); 
				theSky.addStar(star);
			} catch (NoMoreStars nms) { 
				if (Constants.verbose > 2) System.out.println("No more stars");
				more = false; 
			};
		}
	}
}
