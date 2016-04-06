// Written by William O'Mullane for the
// Astrophysics Division of ESTEC  - part of the European Space Agency.
/**
	Simple panel to Display the range of colours
*/

package hipparcos.sky3d;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import javax.vecmath.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.behaviors.mouse.*;



import hipparcos.tools.*;

public class ColourPanel extends Panel {
  
  /** initialiser */ 
  protected void init() {
    GridBagLayout gridbag = new GridBagLayout();
    setLayout(new GridLayout(6,2,0,3));
    //setLayout(gridbag);
    GridBagConstraints lab = new GridBagConstraints();
    lab.fill = GridBagConstraints.HORIZONTAL;
    lab.gridy = GridBagConstraints.RELATIVE;
	lab.ipady = 0;
    lab.gridx = 0;
    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridy = GridBagConstraints.RELATIVE;
	c.ipady = 25;
    c.gridx = 0;

    //setLayout(new GridLayout(Constants.colours.length,1));
	Label title = new Label ("Colours",Label.CENTER);
	title.setForeground(Color.cyan);
	title.setBackground(Color.black);
	Label range = new Label ("B-V range",Label.CENTER);
	range.setForeground(Color.cyan);
	range.setBackground(Color.black);
	gridbag.setConstraints(title,lab);
	add ( title);
	add ( range);
	for (int col =  0 ; col < Constants.colours.length; col++) {
		ColourCell cell = new ColourCell();
		gridbag.setConstraints(cell,c);
		add(cell);
		String txt = "";
		if (col < Constants.colBands.length) {
		   txt =""+Constants.colBands[col];
			if (col == 0) 
				txt = "<"+txt;
			else 
				txt = Constants.colBands[col-1] +" - " +txt;
		} else {
		   txt =">"+Constants.colBands[col-1];
		}
		cell.showCol(Star3D.sgetAppearance(col));
		Label l = new Label(txt,Label.CENTER);
		l.setForeground(Color.cyan);
		l.setBackground(Color.black);
		gridbag.setConstraints(l,lab);
		add(l);

	}
  }
    
  public ColourPanel () {
	init();
  }
}; 
