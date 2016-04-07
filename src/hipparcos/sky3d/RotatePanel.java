
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
import java.awt.event.*;
import java.net.*;
import javax.vecmath.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.behaviors.mouse.*;



import hipparcos.tools.*;

public class RotatePanel extends Panel implements ItemListener, ActionListener {
  protected RotateAble  scene; 
  protected Button rotBut;
  protected Alpha alpha;
  protected Choice choice;
  protected Checkbox x,y,z;
  
  /** initialiser */ 
  protected void init() {
	GridBagLayout gridbag = new GridBagLayout();
    setLayout(gridbag);
	GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;	
    c.gridy = GridBagConstraints.RELATIVE;	
    c.gridx = 0;
    c.ipadx = 0;
    c.ipady = 0;
	//add ( new Label (" "));
	rotBut = new Button ("Rotate");
	rotBut.setForeground(Color.red);
	rotBut.addActionListener(this);
	choice = new Choice();	
	//choice.setForeground(Color.blue);
    choice.addItem("1");
    choice.addItem("2");
    choice.addItem("3");
    choice.addItem("4");
    choice.addItem("5");
    choice.addItem("10");
    choice.addItem("20");
    choice.addItem("30");
    choice.addItem("40");
    choice.addItem("50");
    choice.addItem("100");
    choice.addItem("200");
    choice.addItem("300");
    choice.addItem("400");
    choice.addItem("500");
	choice.select("10");
	choice.addItemListener(this);
	Panel fac = new Panel();
	fac.setLayout(new GridLayout(1,2));
	Label delay = new Label("Delay:");
	delay.setForeground(Color.blue);
	fac.add(delay);
	fac.add(choice);
	fac.setBackground(Color.white);
	Panel axis = new Panel();
	axis.setBackground(Color.white);
	CheckboxGroup cbg = new CheckboxGroup();
	x = new Checkbox("X",cbg,false);
	x.setForeground(Color.blue);
	x.addItemListener(this);
	y = new Checkbox("Y",cbg,true);
	y.setForeground(Color.blue);
	y.addItemListener(this);
	z = new Checkbox("Z",cbg,false);
	z.setForeground(Color.blue);
	z.addItemListener(this);
	axis.add(x);	
	axis.add(y);	
	axis.add(z);	
	gridbag.setConstraints(rotBut,c);
	add(rotBut);
	gridbag.setConstraints(axis,c);
	add(axis);
	gridbag.setConstraints(fac,c);
	add(fac);
	setBackground(Color.white);
  }
    
	/** Set Star */ 
	public void setScene(RotateAble scene) {
		this.scene = scene;
		//stop();
    }

	/** Set Speed - set alpha factor*/ 
	public void setFactor(int width) {
		/* given widht of fov who slow should we go .. */
	    int factor = 100;	
		if (width < 50 ) factor = 50;
		if (width < 40 ) factor = 40;
		if (width < 30 ) factor = 30;
		if (width < 20 ) factor = 20;
		if (width < 15 ) factor = 10;
		choice.select(""+factor);
		setSpeed(factor);
	}

	/** Set the axis of rotation */ 
	protected void setAxis(int x, int y , int z) {
		if (scene != null)  {
		   RotationInterpolator rot = scene.getRotationInterpolator();
			if (rot !=null) {
				Transform3D axis = new Transform3D();
				axis.set(new AxisAngle4f (x, y,z, 2));
				rot.setAxisOfRotation(axis);
			}
		}
	}
	/** Set Speed - set alpha factor*/ 
	protected void setSpeed(int factor) {
		alpha = new Alpha(-1,factor*1000);	
	}
  public RotatePanel () {
	init();
  }

    /** This is required to implement ActionListener - this gets
        called when someone hits a button */
    public void actionPerformed(ActionEvent e){
         String label= e.getActionCommand();
         if (label=="Rotate") {
			start();
         }
         if (label=="Stop") {
			stop();
         }
	}

	public void start() {
		rotBut.setLabel("Stop");
		scene.getRotationInterpolator().setAlpha(alpha);
	}
	public void stop() {
		rotBut.setLabel("Rotate");
		scene.getRotationInterpolator().setAlpha(null);
	}

	   /** Item listener imp  - for choice box */
   public void itemStateChanged(ItemEvent evt) {
		ItemSelectable cb=evt.getItemSelectable();
		if (cb == choice) {
        	int ch=new Integer(choice.getSelectedItem()).intValue();
			setSpeed(ch);
			start();
		}
		if (cb == x) {
			setAxis(0,0,1);
		}
		if (cb == y) {
			setAxis(0,1,0);
		}
		if (cb == z) {
			setAxis(1,0,0);
		}

	}
}; 
